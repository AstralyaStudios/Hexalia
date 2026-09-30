package net.astralya.hexalia.block.entity.custom;

import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.util.ItemHandler;
import net.astralya.hexalia.util.ItemStackHandler;
import net.astralya.hexalia.util.SidedItemHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class RitualBrazierBlockEntity extends SyncBlockEntity {

  private static final int SLOT = 0;

  private static final String TAG_IS_ITEM_IMBUED = "IsItemImbued";
  private static final String TAG_INVENTORY = "Inventory";
  private static final String TAG_CHAN_LEFT = "ChanLeft";
  private static final String TAG_CHAN_TOTAL = "ChanTotal";
  private final ItemStackHandler inventory;
  private final ItemHandler upInputOptional;
  private final ItemHandler downOutputOptional;

  private boolean isRitualFocusItem;
  private float rotation;

  private int channelTicksRemaining;
  private int channelTotalTicks;
  public RitualBrazierBlockEntity(BlockPos pos, BlockState state) {
    super(ModBlockEntityTypes.RITUAL_BRAZIER.get(), pos, state);
    this.inventory = createHandler();
    this.upInputOptional = SidedItemHandlers.view(this.inventory, new int[] {SLOT}, true, false);
    this.downOutputOptional = SidedItemHandlers.view(this.inventory, new int[] {SLOT}, false, true);
  }

  private ItemStackHandler createHandler() {
    return new ItemStackHandler(1) {
      @Override
      protected void onContentsChanged(int slot) {
        inventoryChanged();
      }

      @Override
      public int getSlotLimit(int slot) {
        return 1;
      }
    };
  }

  public boolean isChanneling() {
    return this.channelTicksRemaining > 0;
  }

  public float getChannelProgress(float partialTick) {
    if (!this.isChanneling() || this.channelTotalTicks <= 0) {
      return 0.0F;
    }
    float remaining = this.channelTicksRemaining - partialTick;
    float elapsed = this.channelTotalTicks - remaining;
    return Mth.clamp(elapsed / (float) this.channelTotalTicks, 0.0F, 1.0F);
  }

  public static void serverTick(
      Level level, BlockPos pos, BlockState state, RitualBrazierBlockEntity blockEntity) {
    if (level instanceof ServerLevel serverLevel && blockEntity.isChanneling()) {
      blockEntity.cancelChannel(serverLevel, pos);
    }
  }

  private void cancelChannel(ServerLevel level, BlockPos pos) {
    this.channelTicksRemaining = 0;
    this.channelTotalTicks = 0;

    level.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 0.35F, 0.7F);
    this.sync(level, pos);
  }

  public boolean addItem(ItemStack itemStack) {
    if (this.isChanneling()) {
      return false;
    }

    if (this.isEmpty() || itemStack.isEmpty()) {
      if (!itemStack.isEmpty() && this.isEmpty()) {
        this.inventory.setStackInSlot(SLOT, itemStack.split(1));
        this.isRitualFocusItem = false;
        this.inventoryChanged();
        return true;
      }
    }

    return false;
  }

  public ItemStack removeItem() {
    if (this.isChanneling()) {
      return ItemStack.EMPTY;
    }

    if (!this.isEmpty()) {
      this.isRitualFocusItem = false;
      ItemStack item = this.getStoredItem().split(1);
      this.inventoryChanged();
      return item;
    }

    return ItemStack.EMPTY;
  }

  public ItemHandler getInventory() {
    return this.inventory;
  }

  public ItemStack getStoredItem() {
    return this.inventory.getStackInSlot(SLOT);
  }

  public boolean isEmpty() {
    return this.getStoredItem().isEmpty();
  }

  public float getRenderingRotation() {
    if (this.level == null || !this.level.isClientSide) {
      return this.rotation;
    }

    this.rotation += this.isChanneling() ? 1.5F : 0.5F;
    if (this.rotation >= 360.0F) {
      this.rotation = 0.0F;
    }

    return this.rotation;
  }

  private void sync(ServerLevel level, BlockPos pos) {
    this.setChanged();
    level.sendBlockUpdated(pos, this.getBlockState(), this.getBlockState(), 3);
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
  }

  @Override
  public CompoundTag getUpdateTag() {
    CompoundTag tag = super.getUpdateTag();
    tag.putBoolean(TAG_IS_ITEM_IMBUED, this.isRitualFocusItem);
    tag.put(TAG_INVENTORY, this.inventory.serializeNBT());
    tag.putInt(TAG_CHAN_LEFT, this.channelTicksRemaining);
    tag.putInt(TAG_CHAN_TOTAL, this.channelTotalTicks);

    return tag;
  }

  public void handleUpdateTag(CompoundTag tag) {
    this.isRitualFocusItem = tag.getBoolean(TAG_IS_ITEM_IMBUED);
    this.inventory.deserializeNBT(tag.getCompound(TAG_INVENTORY));
    this.channelTicksRemaining = tag.getInt(TAG_CHAN_LEFT);
    this.channelTotalTicks = tag.getInt(TAG_CHAN_TOTAL);
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
    this.isRitualFocusItem = tag.getBoolean(TAG_IS_ITEM_IMBUED);
    this.inventory.deserializeNBT(tag.getCompound(TAG_INVENTORY));
    this.channelTicksRemaining = tag.getInt(TAG_CHAN_LEFT);
    this.channelTotalTicks = tag.getInt(TAG_CHAN_TOTAL);
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putBoolean(TAG_IS_ITEM_IMBUED, this.isRitualFocusItem);
    tag.put(TAG_INVENTORY, this.inventory.serializeNBT());
    tag.putInt(TAG_CHAN_LEFT, this.channelTicksRemaining);
    tag.putInt(TAG_CHAN_TOTAL, this.channelTotalTicks);

  }
}
