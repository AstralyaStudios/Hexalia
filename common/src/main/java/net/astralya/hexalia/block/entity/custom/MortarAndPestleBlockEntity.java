package net.astralya.hexalia.block.entity.custom;

import java.util.Objects;
import java.util.Optional;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.recipe.MortarAndPestleRecipe;
import net.astralya.hexalia.util.ItemHandler;
import net.astralya.hexalia.util.ItemStackHandler;
import net.astralya.hexalia.util.SidedItemHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MortarAndPestleBlockEntity extends SyncBlockEntity {

  public static final int SPIN_TICKS = 20;
  public static final int REQUIRED_SPINS = 3;

  public static final int INPUT_0 = 0;
  public static final int INPUT_1 = 1;
  public static final int INPUT_2 = 2;
  public static final int OUTPUT = 3;

  private final ItemStackHandler inventory;
  private final ItemHandler upInputOptional;
  private final ItemHandler downOutputOptional;

  private ItemStack pendingResult;
  private int pestleTick;
  private int pestleCount;
  private boolean pestling;

  public MortarAndPestleBlockEntity(BlockPos pos, BlockState state) {
    super(ModBlockEntityTypes.MORTAR_AND_PESTLE.get(), pos, state);
    this.inventory = createHandler();
    this.upInputOptional =
        SidedItemHandlers.view(this.inventory, new int[] {INPUT_0, INPUT_1, INPUT_2}, true, false);
    this.downOutputOptional =
        SidedItemHandlers.view(this.inventory, new int[] {OUTPUT}, false, true);
    this.pendingResult = ItemStack.EMPTY;
  }

  private ItemStackHandler createHandler() {
    return new ItemStackHandler(4) {
      @Override
      protected void onContentsChanged(int slot) {
        if (!hasLevel() || level == null) {
          setChanged();
          return;
        }

        if (!level.isClientSide) {
          if (slot != OUTPUT) {
            recomputePestlingState();
          }
          inventoryChanged();
        } else {
          setChanged();
        }
      }

      @Override
      public int getSlotLimit(int slot) {
        return slot == OUTPUT ? 64 : 1;
      }
    };
  }

  public static void tick(
      Level level, BlockPos pos, BlockState state, MortarAndPestleBlockEntity blockEntity) {
    if (!level.isClientSide && level.hasNeighborSignal(pos) && blockEntity.canStartSpin()) {
      blockEntity.startSpin();
    }

    if (blockEntity.pestleTick > 0) {
      blockEntity.pestleTick--;
      blockEntity.setChanged();

      if (!level.isClientSide && blockEntity.pestleTick == 0) {
        blockEntity.tryFinishOnSpinEnd();
        blockEntity.inventoryChanged();
      }
    }
  }

  public ItemStackHandler getItems() {
    return this.inventory;
  }

  public boolean hasAnyInputs() {
    return !this.inventory.getStackInSlot(INPUT_0).isEmpty()
        || !this.inventory.getStackInSlot(INPUT_1).isEmpty()
        || !this.inventory.getStackInSlot(INPUT_2).isEmpty();
  }

  public boolean hasOutput() {
    return !this.inventory.getStackInSlot(OUTPUT).isEmpty();
  }

  public int getPestleTick() {
    return this.pestleTick;
  }

  public boolean canInsertOne(ItemStack stack) {
    if (stack.isEmpty() || this.hasOutput()) {
      return false;
    }

    return this.inventory.getStackInSlot(INPUT_0).isEmpty()
        || this.inventory.getStackInSlot(INPUT_1).isEmpty()
        || this.inventory.getStackInSlot(INPUT_2).isEmpty();
  }

  public boolean insertOneIntoNextEmpty(ItemStack held) {
    if (!this.canInsertOne(held)) {
      return false;
    }

    ItemStack one = held.copy();
    one.setCount(1);

    for (int slot = INPUT_0; slot <= INPUT_2; slot++) {
      if (this.inventory.getStackInSlot(slot).isEmpty()) {
        this.inventory.setStackInSlot(slot, one);
        return true;
      }
    }

    return false;
  }

  public ItemStack extractOneInput() {
    for (int slot = INPUT_2; slot >= INPUT_0; slot--) {
      ItemStack stack = this.inventory.getStackInSlot(slot);
      if (!stack.isEmpty()) {
        ItemStack out = stack.copy();
        out.setCount(1);
        this.inventory.setStackInSlot(slot, ItemStack.EMPTY);

        if (this.level != null && !this.level.isClientSide) {
          this.recomputePestlingState();
          this.inventoryChanged();
        } else {
          this.setChanged();
        }

        return out;
      }
    }

    return ItemStack.EMPTY;
  }

  public ItemStack takeOutputOne() {
    ItemStack out = this.inventory.getStackInSlot(OUTPUT);
    if (out.isEmpty()) {
      return ItemStack.EMPTY;
    }

    ItemStack give = out.copy();
    give.setCount(1);

    out.shrink(1);
    this.inventory.setStackInSlot(OUTPUT, out.isEmpty() ? ItemStack.EMPTY : out);

    if (this.level != null && !this.level.isClientSide) {
      this.inventoryChanged();
    } else {
      this.setChanged();
    }

    return give;
  }

  public void recomputePestlingState() {
    if (this.level == null || this.level.isClientSide) {
      return;
    }

    if (this.hasOutput() || !this.hasAnyInputs()) {
      this.pendingResult = ItemStack.EMPTY;
      this.pestling = false;
      this.pestleTick = 0;
      this.pestleCount = 0;
      return;
    }

    ItemStack newResult = this.computeRecipeResult();
    boolean valid = !newResult.isEmpty();

    if (!ItemStack.isSameItemSameTags(this.pendingResult, newResult)
        || this.pendingResult.getCount() != newResult.getCount()) {
      this.pestleTick = 0;
      this.pestleCount = 0;
    }

    this.pendingResult = newResult;
    this.pestling = valid;

    if (!valid) {
      this.pestleTick = 0;
      this.pestleCount = 0;
    }
  }

  private ItemStack computeRecipeResult() {
    if (this.level == null) {
      return ItemStack.EMPTY;
    }

    SimpleContainer container =
        new SimpleContainer(
            this.inventory.getStackInSlot(INPUT_0),
            this.inventory.getStackInSlot(INPUT_1),
            this.inventory.getStackInSlot(INPUT_2));

    Optional<MortarAndPestleRecipe> match =
        this.level
            .getRecipeManager()
            .getRecipeFor(MortarAndPestleRecipe.Type.INSTANCE, container, this.level);

    return match
        .map(recipe -> recipe.getResultItem(this.level.registryAccess()).copy())
        .orElse(ItemStack.EMPTY);
  }

  public boolean canStartSpin() {
    return !this.hasOutput()
        && !this.pendingResult.isEmpty()
        && this.pestleTick <= 0
        && this.pestleCount < REQUIRED_SPINS;
  }

  public boolean startSpin() {
    if (this.level == null) {
      return false;
    }

    if (!this.level.isClientSide) {
      this.recomputePestlingState();
    }

    if (!this.canStartSpin()) {
      return false;
    }

    this.pestling = true;
    this.pestleTick = SPIN_TICKS;
    this.pestleCount++;

    if (this.level instanceof ServerLevel serverLevel) {
      this.spawnCrushParticles(serverLevel);
    }

    if (!this.level.isClientSide) {
      this.inventoryChanged();
    } else {
      this.setChanged();
    }

    return true;
  }

  private void tryFinishOnSpinEnd() {
    if (this.level == null
        || this.level.isClientSide
        || !this.pestling
        || this.pestleCount < REQUIRED_SPINS
        || this.pendingResult.isEmpty()
        || this.hasOutput()) {
      return;
    }

    this.inventory.setStackInSlot(OUTPUT, this.pendingResult.copy());
    this.inventory.setStackInSlot(INPUT_0, ItemStack.EMPTY);
    this.inventory.setStackInSlot(INPUT_1, ItemStack.EMPTY);
    this.inventory.setStackInSlot(INPUT_2, ItemStack.EMPTY);

    this.pendingResult = ItemStack.EMPTY;
    this.pestling = false;
    this.pestleTick = 0;
    this.pestleCount = 0;
  }

  private void spawnCrushParticles(ServerLevel serverLevel) {
    double x = this.worldPosition.getX() + 0.5D;
    double y = this.worldPosition.getY() + 0.20D;
    double z = this.worldPosition.getZ() + 0.5D;

    for (int slot = INPUT_0; slot <= INPUT_2; slot++) {
      ItemStack stack = this.inventory.getStackInSlot(slot);
      if (stack.isEmpty()) {
        continue;
      }

      ItemParticleOption particle = new ItemParticleOption(ParticleTypes.ITEM, stack);

      for (int i = 0; i < 3; i++) {
        double ox = (serverLevel.random.nextDouble() - 0.5D) * 0.12D;
        double oy = serverLevel.random.nextDouble() * 0.06D;
        double oz = (serverLevel.random.nextDouble() - 0.5D) * 0.12D;
        double vx = (serverLevel.random.nextDouble() - 0.5D) * 0.03D;
        double vy = 0.02D + serverLevel.random.nextDouble() * 0.02D;
        double vz = (serverLevel.random.nextDouble() - 0.5D) * 0.03D;

        serverLevel.sendParticles(particle, x + ox, y + oy, z + oz, 1, vx, vy, vz, 0.0D);
      }
    }
  }

  public void drops() {
    if (this.level == null) {
      return;
    }

    SimpleContainer container = new SimpleContainer(this.inventory.getSlots());
    for (int i = 0; i < this.inventory.getSlots(); i++) {
      container.setItem(i, this.inventory.getStackInSlot(i));
    }

    Containers.dropContents(this.level, this.worldPosition, container);

    for (int i = 0; i < this.inventory.getSlots(); i++) {
      this.inventory.setStackInSlot(i, ItemStack.EMPTY);
    }
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
  }

  @Override
  public CompoundTag getUpdateTag() {
    CompoundTag tag = super.getUpdateTag();
    tag.put("Items", this.inventory.serializeNBT());
    tag.putInt("PestleTick", this.pestleTick);
    tag.putInt("PestleCount", this.pestleCount);
    tag.putBoolean("Pestling", this.pestling);

    if (!this.pendingResult.isEmpty()) {
      CompoundTag resultTag = new CompoundTag();
      this.pendingResult.save(resultTag);
      tag.put("PendingResult", resultTag);
    }

    return tag;
  }

  public void handleUpdateTag(CompoundTag tag) {

    if (tag.contains("Items", Tag.TAG_COMPOUND)) {
      this.inventory.deserializeNBT(tag.getCompound("Items"));
    }

    this.pestleTick = tag.getInt("PestleTick");
    this.pestleCount = tag.getInt("PestleCount");
    this.pestling = tag.getBoolean("Pestling");

    if (tag.contains("PendingResult", Tag.TAG_COMPOUND)) {
      this.pendingResult = ItemStack.of(tag.getCompound("PendingResult"));
    } else {
      this.pendingResult = ItemStack.EMPTY;
    }
  }

  @Override
  public @Nullable ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
    this.handleUpdateTag(pkt.getTag());
    if (this.level != null && this.level.isClientSide) {
      this.level.sendBlockUpdated(
          this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
    }
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);

    if (tag.contains("Items", Tag.TAG_COMPOUND)) {
      this.inventory.deserializeNBT(tag.getCompound("Items"));
    }

    this.pestleTick = tag.getInt("PestleTick");
    this.pestleCount = tag.getInt("PestleCount");
    this.pestling = tag.getBoolean("Pestling");

    if (tag.contains("PendingResult", Tag.TAG_COMPOUND)) {
      this.pendingResult = ItemStack.of(tag.getCompound("PendingResult"));
    } else {
      this.pendingResult = ItemStack.EMPTY;
    }
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.put("Items", this.inventory.serializeNBT());
    tag.putInt("PestleTick", this.pestleTick);
    tag.putInt("PestleCount", this.pestleCount);
    tag.putBoolean("Pestling", this.pestling);

    if (!this.pendingResult.isEmpty()) {
      CompoundTag resultTag = new CompoundTag();
      this.pendingResult.save(resultTag);
      tag.put("PendingResult", resultTag);
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.worldPosition);
  }
}
