package net.astralya.hexalia.block.entity.custom;

import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.util.ItemInteractionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RitualBrazierBlockEntity extends BlockEntity
    implements Container, Clearable, ItemInteractionHelper.SingleItemStorage {
  private static final String TAG_ITEM = "Item";
  private ItemStack item = ItemStack.EMPTY;
  private float rotation;

  public RitualBrazierBlockEntity(BlockPos pos, BlockState state) {
    super(ModBlockEntityTypes.RITUAL_BRAZIER.get(), pos, state);
  }

  public boolean addItem(ItemStack stack) {
    if (!item.isEmpty() || stack.isEmpty()) {
      return false;
    }
    item = stack.split(1);
    inventoryChanged();
    return true;
  }

  public ItemStack removeItem() {
    if (item.isEmpty()) {
      return ItemStack.EMPTY;
    }
    ItemStack removed = item.split(1);
    if (item.isEmpty()) item = ItemStack.EMPTY;
    inventoryChanged();
    return removed;
  }

  @Override
  public int getContainerSize() {
    return 1;
  }

  @Override
  public boolean isEmpty() {
    return item.isEmpty();
  }

  @Override
  public ItemStack getItem(int slot) {
    return slot == 0 ? item : ItemStack.EMPTY;
  }

  @Override
  public ItemStack removeItem(int slot, int amount) {
    if (slot != 0 || amount <= 0) {
      return ItemStack.EMPTY;
    }
    ItemStack removed = item.split(amount);
    if (!removed.isEmpty()) {
      inventoryChanged();
    }
    return removed;
  }

  @Override
  public ItemStack removeItemNoUpdate(int slot) {
    if (slot != 0) {
      return ItemStack.EMPTY;
    }
    ItemStack removed = item;
    item = ItemStack.EMPTY;
    return removed;
  }

  @Override
  public void setItem(int slot, ItemStack stack) {
    if (slot != 0) {
      return;
    }
    item = stack.copyWithCount(Math.min(stack.getCount(), getMaxStackSize()));
    inventoryChanged();
  }

  @Override
  public int getMaxStackSize() {
    return 1;
  }

  @Override
  public boolean canPlaceItem(int slot, ItemStack stack) {
    return slot == 0 && item.isEmpty() && !stack.isEmpty();
  }

  @Override
  public boolean canTakeItem(Container target, int slot, ItemStack stack) {
    return slot == 0;
  }

  @Override
  public boolean stillValid(net.minecraft.world.entity.player.Player player) {
    return Container.stillValidBlockEntity(this, player);
  }

  @Override
  public void clearContent() {
    item = ItemStack.EMPTY;
    inventoryChanged();
  }

  public ItemStack getStoredItem() {
    return item;
  }

  public float getRenderingRotation() {
    if (level == null || !level.isClientSide) {
      return rotation;
    }
    rotation += 0.5F;
    if (rotation >= 360.0F) {
      rotation = 0.0F;
    }
    return rotation;
  }

  private void inventoryChanged() {
    setChanged();
    if (level != null && !level.isClientSide()) {
      level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }
  }

  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
    CompoundTag tag = new CompoundTag();
    saveAdditional(tag, provider);
    return tag;
  }

  @Override
  public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.loadAdditional(tag, provider);
    item =
        tag.contains(TAG_ITEM)
            ? ItemStack.parseOptional(provider, tag.getCompound(TAG_ITEM))
            : ItemStack.EMPTY;
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.saveAdditional(tag, provider);
    tag.put(TAG_ITEM, item.isEmpty() ? new CompoundTag() : item.save(provider));
  }
}
