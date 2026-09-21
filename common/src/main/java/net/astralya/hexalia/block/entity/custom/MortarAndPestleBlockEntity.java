package net.astralya.hexalia.block.entity.custom;

import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.recipe.ModRecipeTypes;
import net.astralya.hexalia.recipe.MortarAndPestleRecipeInput;
import net.astralya.hexalia.util.ItemInteractionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MortarAndPestleBlockEntity extends BlockEntity
    implements WorldlyContainer, Clearable, ItemInteractionHelper.ItemStorage {
  public static final int SPIN_TICKS = 20;
  public static final int REQUIRED_SPINS = 3;
  public static final int INPUT_0 = 0;
  public static final int INPUT_1 = 1;
  public static final int INPUT_2 = 2;
  public static final int OUTPUT = 3;

  private static final int SLOT_COUNT = 4;
  private static final int[] INPUT_SLOTS = {INPUT_0, INPUT_1, INPUT_2};
  private static final int[] OUTPUT_SLOTS = {OUTPUT};
  private static final String TAG_PESTLE_TICK = "PestleTick";
  private static final String TAG_PESTLE_COUNT = "PestleCount";
  private static final String TAG_PESTLING = "Pestling";
  private static final String TAG_PENDING_RESULT = "PendingResult";

  private final NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
  private ItemStack pendingResult = ItemStack.EMPTY;
  private int pestleTick;
  private int pestleCount;
  private boolean pestling;

  public MortarAndPestleBlockEntity(BlockPos pos, BlockState state) {
    super(ModBlockEntityTypes.MORTAR_AND_PESTLE.get(), pos, state);
  }

  public static void tick(
      Level level, BlockPos pos, BlockState state, MortarAndPestleBlockEntity mortar) {
    if (!level.isClientSide() && level.hasNeighborSignal(pos) && mortar.canStartSpin()) {
      mortar.startSpin();
    }

    if (mortar.pestleTick <= 0) {
      return;
    }
    mortar.pestleTick--;
    mortar.setChanged();

    if (!level.isClientSide() && mortar.pestleTick == 0) {
      mortar.tryFinishOnSpinEnd();
      mortar.inventoryChanged();
    }
  }

  public ItemStack getItem(int slot) {
    return slot >= 0 && slot < SLOT_COUNT ? items.get(slot) : ItemStack.EMPTY;
  }

  @Override
  public int getContainerSize() {
    return SLOT_COUNT;
  }

  @Override
  public boolean isEmpty() {
    for (ItemStack stack : items) {
      if (!stack.isEmpty()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public ItemStack removeItem(int slot, int amount) {
    ItemStack removed = ContainerHelper.removeItem(items, slot, amount);
    if (!removed.isEmpty()) {
      recomputeAndSync();
    }
    return removed;
  }

  @Override
  public ItemStack removeItemNoUpdate(int slot) {
    return ContainerHelper.takeItem(items, slot);
  }

  @Override
  public void setItem(int slot, ItemStack stack) {
    if (slot < 0 || slot >= SLOT_COUNT) {
      return;
    }
    items.set(slot, stack.copyWithCount(Math.min(stack.getCount(), getMaxStackSize())));
    recomputeAndSync();
  }

  @Override
  public int getMaxStackSize() {
    return 1;
  }

  @Override
  public boolean canPlaceItem(int slot, ItemStack stack) {
    return slot >= INPUT_0
        && slot <= INPUT_2
        && !stack.isEmpty()
        && !hasOutput()
        && items.get(slot).isEmpty();
  }

  @Override
  public boolean canTakeItem(Container target, int slot, ItemStack stack) {
    return slot == OUTPUT && hasOutput();
  }

  @Override
  public int[] getSlotsForFace(Direction side) {
    return side == Direction.DOWN ? OUTPUT_SLOTS : INPUT_SLOTS;
  }

  @Override
  public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction direction) {
    return direction != Direction.DOWN && canPlaceItem(slot, stack);
  }

  @Override
  public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
    return direction == Direction.DOWN && slot == OUTPUT && hasOutput();
  }

  @Override
  public boolean stillValid(Player player) {
    return Container.stillValidBlockEntity(this, player);
  }

  @Override
  public void clearContent() {
    items.clear();
    recomputeAndSync();
  }

  public int getPestleTick() {
    return pestleTick;
  }

  public boolean hasAnyInputs() {
    return !items.get(INPUT_0).isEmpty()
        || !items.get(INPUT_1).isEmpty()
        || !items.get(INPUT_2).isEmpty();
  }

  public boolean hasOutput() {
    return !items.get(OUTPUT).isEmpty();
  }

  public boolean canStartSpin() {
    return !hasOutput()
        && !pendingResult.isEmpty()
        && pestleTick <= 0
        && pestleCount < REQUIRED_SPINS;
  }

  public boolean startSpin() {
    if (level == null) {
      return false;
    }
    if (!level.isClientSide()) {
      recomputePestlingState();
    }
    if (!canStartSpin()) {
      return false;
    }

    pestling = true;
    pestleTick = SPIN_TICKS;
    pestleCount++;
    if (level instanceof ServerLevel server) {
      spawnCrushParticles(server);
      inventoryChanged();
    } else {
      setChanged();
    }
    return true;
  }

  @Override
  public boolean canInsertItem(ItemStack stack) {
    return !stack.isEmpty() && !hasOutput() && firstEmptyInputSlot() != -1;
  }

  @Override
  public boolean addItem(ItemStack stack) {
    if (!canInsertItem(stack)) {
      return false;
    }
    int slot = firstEmptyInputSlot();
    items.set(slot, stack.split(1));
    recomputeAndSync();
    return true;
  }

  @Override
  public boolean canExtractItem() {
    return hasOutput() || hasAnyInputs();
  }

  @Override
  public ItemStack removeItem() {
    return hasOutput() ? takeOutputOne() : extractOneInput();
  }

  public ItemStack extractOneInput() {
    for (int slot = INPUT_2; slot >= INPUT_0; slot--) {
      ItemStack stack = items.get(slot);
      if (stack.isEmpty()) {
        continue;
      }
      ItemStack out = stack.copyWithCount(1);
      items.set(slot, ItemStack.EMPTY);
      recomputeAndSync();
      return out;
    }
    return ItemStack.EMPTY;
  }

  public ItemStack takeOutputOne() {
    ItemStack output = items.get(OUTPUT);
    if (output.isEmpty()) {
      return ItemStack.EMPTY;
    }
    ItemStack out = output.copyWithCount(1);
    output.shrink(1);
    items.set(OUTPUT, output.isEmpty() ? ItemStack.EMPTY : output);
    inventoryChanged();
    return out;
  }

  private void recomputePestlingState() {
    if (level == null || level.isClientSide()) {
      return;
    }
    if (hasOutput() || !hasAnyInputs()) {
      pendingResult = ItemStack.EMPTY;
      pestling = false;
      pestleTick = 0;
      pestleCount = 0;
      return;
    }

    ItemStack newResult = computeRecipeResult();
    if (!ItemStack.isSameItemSameComponents(pendingResult, newResult)
        || pendingResult.getCount() != newResult.getCount()) {
      pestleTick = 0;
      pestleCount = 0;
    }
    pendingResult = newResult;
    pestling = !newResult.isEmpty();
    if (!pestling) {
      pestleTick = 0;
      pestleCount = 0;
    }
  }

  private ItemStack computeRecipeResult() {
    if (level == null) {
      return ItemStack.EMPTY;
    }
    MortarAndPestleRecipeInput input =
        new MortarAndPestleRecipeInput(items.get(INPUT_0), items.get(INPUT_1), items.get(INPUT_2));
    return level
        .getRecipeManager()
        .getRecipeFor(ModRecipeTypes.MORTAR_AND_PESTLE.get(), input, level)
        .map(RecipeHolder::value)
        .map(recipe -> recipe.getResultItem(level.registryAccess()).copy())
        .orElse(ItemStack.EMPTY);
  }

  private void tryFinishOnSpinEnd() {
    if (level == null || level.isClientSide() || !pestling || pestleCount < REQUIRED_SPINS) {
      return;
    }
    if (pendingResult.isEmpty() || hasOutput()) {
      return;
    }
    items.set(OUTPUT, pendingResult.copy());
    items.set(INPUT_0, ItemStack.EMPTY);
    items.set(INPUT_1, ItemStack.EMPTY);
    items.set(INPUT_2, ItemStack.EMPTY);
    pendingResult = ItemStack.EMPTY;
    pestling = false;
    pestleTick = 0;
    pestleCount = 0;
  }

  private void spawnCrushParticles(ServerLevel server) {
    double x = worldPosition.getX() + 0.5;
    double y = worldPosition.getY() + 0.2;
    double z = worldPosition.getZ() + 0.5;

    for (int slot = INPUT_0; slot <= INPUT_2; slot++) {
      ItemStack stack = items.get(slot);
      if (stack.isEmpty()) {
        continue;
      }
      ItemParticleOption particle = new ItemParticleOption(ParticleTypes.ITEM, stack);
      for (int index = 0; index < 3; index++) {
        server.sendParticles(
            particle,
            x + (server.random.nextDouble() - 0.5) * 0.12,
            y + server.random.nextDouble() * 0.06,
            z + (server.random.nextDouble() - 0.5) * 0.12,
            1,
            (server.random.nextDouble() - 0.5) * 0.03,
            0.02 + server.random.nextDouble() * 0.02,
            (server.random.nextDouble() - 0.5) * 0.03,
            0.0);
      }
    }
  }

  public void drops() {
    if (level == null || level.isClientSide()) {
      return;
    }
    SimpleContainer container = new SimpleContainer(SLOT_COUNT);
    for (int index = 0; index < SLOT_COUNT; index++) {
      container.setItem(index, items.get(index));
      items.set(index, ItemStack.EMPTY);
    }
    Containers.dropContents(level, worldPosition, container);
  }

  private int firstEmptyInputSlot() {
    for (int slot = INPUT_0; slot <= INPUT_2; slot++) {
      if (items.get(slot).isEmpty()) {
        return slot;
      }
    }
    return -1;
  }

  private void recomputeAndSync() {
    recomputePestlingState();
    inventoryChanged();
  }

  private void inventoryChanged() {
    setChanged();
    if (level != null && !level.isClientSide()) {
      level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.saveAdditional(tag, registries);
    ContainerHelper.saveAllItems(tag, items, registries);
    tag.putInt(TAG_PESTLE_TICK, pestleTick);
    tag.putInt(TAG_PESTLE_COUNT, pestleCount);
    tag.putBoolean(TAG_PESTLING, pestling);
    if (!pendingResult.isEmpty()) {
      tag.put(TAG_PENDING_RESULT, pendingResult.save(registries));
    }
  }

  @Override
  protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
    for (int index = 0; index < SLOT_COUNT; index++) {
      items.set(index, ItemStack.EMPTY);
    }
    ContainerHelper.loadAllItems(tag, items, registries);
    pestleTick = tag.getInt(TAG_PESTLE_TICK);
    pestleCount = tag.getInt(TAG_PESTLE_COUNT);
    pestling = tag.getBoolean(TAG_PESTLING);
    pendingResult =
        tag.contains(TAG_PENDING_RESULT)
            ? ItemStack.parseOptional(registries, tag.getCompound(TAG_PENDING_RESULT))
            : ItemStack.EMPTY;
  }

  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
    return saveWithoutMetadata(registries);
  }

  @Override
  public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }
}
