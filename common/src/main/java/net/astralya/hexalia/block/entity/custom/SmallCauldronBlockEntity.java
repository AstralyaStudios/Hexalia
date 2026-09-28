package net.astralya.hexalia.block.entity.custom;

import java.util.List;
import net.astralya.hexalia.block.custom.SmallCauldronBlock;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.gameplay.smallcauldron.SmallCauldronContents;
import net.astralya.hexalia.util.ItemHandler;
import net.astralya.hexalia.util.SidedItemHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public class SmallCauldronBlockEntity extends SyncBlockEntity {

  public static final int STIR_ANIM_TICKS = 20;

  private static final String TAG_STIR_ANIM_TICK = "StirAnimTick";
  private static final int SPOILED_AURA_INTERVAL_TICKS = 20;
  private static final int SPOILED_POISON_DURATION_TICKS = 60;
  private static final int SPOILED_POISON_AMPLIFIER = 0;

  private final SmallCauldronContents contents = new SmallCauldronContents();
  private final ItemHandler upInputHandler = new UpInputHandler(this);

  private int stirAnimTick;
  private long clientStirStartGameTime;
  private int clientStirStartTick;
  private boolean stirAnimDirty;

  public SmallCauldronBlockEntity(BlockPos pos, BlockState state) {
    super(ModBlockEntityTypes.SMALL_CAULDRON.get(), pos, state);
  }

  public ItemHandler getItemHandler(@Nullable Direction side) {
    if (this.contents.isCooking() || this.contents.isSpoiled()) {
      return SidedItemHandlers.blocked();
    }

    if (side == Direction.UP) {
      return this.upInputHandler;
    }

    return SidedItemHandlers.blocked();
  }

  public void tick(Level level, BlockPos pos, BlockState state) {
    if (!(level instanceof ServerLevel serverLevel)) {
      return;
    }

    boolean blockLit = state.getValue(SmallCauldronBlock.LIT);

    if (this.stirAnimTick > 0) {
      this.stirAnimTick--;
      this.stirAnimDirty = true;
    }

    this.contents.tickServer(serverLevel, blockLit);

    if (this.contents.isSpoiled()) {
      this.applySpoiledAura(serverLevel, pos);
    }

    this.syncIfNeeded();
  }

  private void applySpoiledAura(ServerLevel serverLevel, BlockPos pos) {
    if ((serverLevel.getGameTime() % SPOILED_AURA_INTERVAL_TICKS) != 0) {
      return;
    }

    AABB area = new AABB(pos).inflate(1.0D, 1.0D, 1.0D);

    for (Player player : serverLevel.getEntitiesOfClass(Player.class, area)) {
      player.addEffect(
          new MobEffectInstance(
              MobEffects.POISON,
              SPOILED_POISON_DURATION_TICKS,
              SPOILED_POISON_AMPLIFIER,
              false,
              true,
              true));
    }
  }

  public boolean canStir(BlockState state, Player player) {
    return this.contents.canStir(state.getValue(SmallCauldronBlock.LIT));
  }

  public boolean tryStir(BlockState state, Player player) {
    if (!(this.level instanceof ServerLevel serverLevel)) {
      return false;
    }

    if (!this.contents.canStir(state.getValue(SmallCauldronBlock.LIT))) {
      return false;
    }

    SmallCauldronContents.StirResult result = this.contents.stir(serverLevel);

    this.stirAnimTick = STIR_ANIM_TICKS;
    this.stirAnimDirty = true;

    this.syncIfNeeded();
    return result == SmallCauldronContents.StirResult.STIRRED
        || result == SmallCauldronContents.StirResult.STARTED_COOKING;
  }

  public void triggerStirAnimation() {
    if (!(this.level instanceof ServerLevel)) {
      return;
    }

    this.stirAnimTick = STIR_ANIM_TICKS;
    this.stirAnimDirty = true;

    this.syncIfNeeded();
  }

  public int getStirAnimTick() {
    return this.stirAnimTick;
  }

  public float getStirProgress(float partialTicks) {
    Level level = this.getLevel();
    if (level == null) {
      return 0.0F;
    }

    if (level.isClientSide) {
      if (this.clientStirStartTick <= 0) {
        return 0.0F;
      }

      float elapsed = (level.getGameTime() - this.clientStirStartGameTime) + partialTicks;
      float progress = elapsed / (float) this.clientStirStartTick;
      if (progress >= 1.0F) {
        return 0.0F;
      }

      return Mth.clamp(progress, 0.0F, 1.0F);
    }

    int tick = this.getStirAnimTick();
    if (tick <= 0) {
      return 0.0F;
    }

    float progress = (STIR_ANIM_TICKS - (tick - partialTicks)) / (float) STIR_ANIM_TICKS;
    return Mth.clamp(progress, 0.0F, 1.0F);
  }

  public List<ItemStack> getIngredientsForRender() {
    return this.contents.getIngredientsForRender();
  }

  public float getLiquidFill01() {
    return this.contents.getLiquidFill01();
  }

  public float getVisualLiquidFill01() {
    return this.contents.getVisualLiquidFill01();
  }

  public int getVisualLiquidColor() {
    return this.contents.getVisualLiquidColor();
  }

  public boolean isSpoiled() {
    return this.contents.isSpoiled();
  }

  public boolean isCooking() {
    return this.contents.isCooking();
  }

  public boolean hasMixture() {
    return this.contents.hasMixture();
  }

  public boolean isOvercooked() {
    return this.contents.isOvercooked();
  }

  public int getMixtureBaseColor() {
    return this.contents.getMixtureBaseColor();
  }

  public boolean canExtractOneIngredient() {
    return this.contents.canExtractOneIngredient(
        this.getBlockState().getValue(SmallCauldronBlock.LIT));
  }

  public ItemStack extractOneIngredient() {
    if (!(this.level instanceof ServerLevel)) {
      return ItemStack.EMPTY;
    }

    ItemStack out = this.contents.extractOneIngredient();
    this.syncIfNeeded();
    return out;
  }

  public boolean canInsertOne(ItemStack stack) {
    return this.contents.canInsertOne(stack);
  }

  public boolean insertOneIntoCauldron(ItemStack held) {
    if (!(this.level instanceof ServerLevel)) {
      return false;
    }

    boolean ok = this.contents.insertOne(held);
    this.syncIfNeeded();
    return ok;
  }

  public boolean canScoopMixtureWithRusticBottle() {
    return this.contents.canScoopMixtureWithRusticBottle();
  }

  public boolean tryScoopBottlePublic(Player player, InteractionHand hand, ItemStack held) {
    if (!(this.level instanceof ServerLevel serverLevel)) {
      return false;
    }

    boolean ok =
        this.contents.tryScoopBottle(
            serverLevel, this.centerX(), this.topY(), this.centerZ(), player, hand, held);
    this.syncIfNeeded();
    return ok;
  }

  public boolean isRusticBottle(ItemStack stack) {
    return this.contents.isRusticBottle(stack);
  }

  public boolean isLotusBlossom(ItemStack stack) {
    return this.contents.isLotusBlossom(stack);
  }

  public boolean isWaterContainer(ItemStack stack) {
    return this.contents.isWaterContainer(stack);
  }

  public boolean canCleanseSpoiledWithLotus() {
    return this.contents.canCleanseSpoiledWithLotus();
  }

  public boolean tryCleanseSpoiledPublic(Player player, InteractionHand hand, ItemStack held) {
    if (!(this.level instanceof ServerLevel)) {
      return false;
    }

    boolean ok = this.contents.tryCleanseSpoiled(player, hand, held);
    this.syncIfNeeded();
    return ok;
  }

  public boolean canUseWaterContainer(ItemStack stack) {
    return this.contents.canUseWaterContainer(stack);
  }

  public boolean tryFillWithWaterPublic(Player player, InteractionHand hand, ItemStack held) {
    if (!(this.level instanceof ServerLevel serverLevel)) {
      return false;
    }

    boolean ok =
        this.contents.tryUseWaterContainer(
            serverLevel, this.centerX(), this.centerY(), this.centerZ(), player, hand, held);
    this.syncIfNeeded();
    return ok;
  }

  public void dropAll(Level level) {
    if (level == null || level.isClientSide) {
      return;
    }

    this.contents.dropAll(level, this.centerX(), this.centerY(), this.centerZ());
    this.contents.clearDirty();
    this.stirAnimDirty = false;

    this.setChanged();
    this.inventoryChanged();
  }

  private void syncIfNeeded() {
    boolean needsSync = this.stirAnimDirty || this.contents.isDirty();
    if (!needsSync) {
      return;
    }

    if (this.contents.isDirty()) {
      this.contents.clearDirty();
    }

    this.stirAnimDirty = false;
    this.inventoryChanged();
  }

  private double centerX() {
    return this.worldPosition.getX() + 0.5D;
  }

  private double centerY() {
    return this.worldPosition.getY() + 0.5D;
  }

  private double centerZ() {
    return this.worldPosition.getZ() + 0.5D;
  }

  private double topY() {
    return this.worldPosition.getY() + 1.0D;
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putInt(TAG_STIR_ANIM_TICK, this.stirAnimTick);
    this.contents.save(tag);
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);

    int previousStir = this.stirAnimTick;

    this.stirAnimTick = tag.getInt(TAG_STIR_ANIM_TICK);
    if (this.stirAnimTick < 0) {
      this.stirAnimTick = 0;
    }
    if (this.stirAnimTick > STIR_ANIM_TICKS) {
      this.stirAnimTick = STIR_ANIM_TICKS;
    }

    this.contents.load(tag);

    Level level = this.getLevel();
    if (level != null && level.isClientSide) {
      if (this.stirAnimTick > previousStir) {
        this.clientStirStartGameTime = level.getGameTime();
        this.clientStirStartTick = this.stirAnimTick;
      } else if (this.stirAnimTick <= 0) {
        this.clientStirStartTick = 0;
      }
    }

    this.stirAnimDirty = false;
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
  }

  @Override
  public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public CompoundTag getUpdateTag() {
    return this.saveWithoutMetadata();
  }

  private static final class UpInputHandler implements ItemHandler {

    private final SmallCauldronBlockEntity blockEntity;

    private UpInputHandler(SmallCauldronBlockEntity blockEntity) {
      this.blockEntity = blockEntity;
    }

    @Override
    public int getSlots() {
      return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
      return ItemStack.EMPTY;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
      if (slot != 0 || stack.isEmpty()) {
        return stack;
      }
      if (!this.blockEntity.canInsertOne(stack)) {
        return stack;
      }

      ItemStack remainder = stack.copy();
      remainder.shrink(1);

      if (simulate) {
        return remainder;
      }

      ItemStack single = stack.copy();
      single.setCount(1);

      boolean ok = this.blockEntity.insertOneIntoCauldron(single);
      return ok ? remainder : stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
      return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
      return 64;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return slot == 0 && this.blockEntity.canInsertOne(stack);
    }
  }
}
