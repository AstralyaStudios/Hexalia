package net.astralya.hexalia.block.entity.custom;

import net.astralya.hexalia.block.custom.ShelfBlock;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Clearable;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ShelfBlockEntity extends BlockEntity implements WorldlyContainer, Clearable {
  private static final int SIZE = 6;
  private static final int[] TOP_SLOTS = {0, 1, 2, 3, 4, 5};
  private static final int[] NO_SLOTS = {};

  private final NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);

  public ShelfBlockEntity(BlockPos pos, BlockState state) {
    super(ModBlockEntityTypes.SHELF.get(), pos, state);
  }

  public NonNullList<ItemStack> getItems() {
    return items;
  }

  @Override
  public int[] getSlotsForFace(Direction side) {
    return side == Direction.UP ? TOP_SLOTS : NO_SLOTS;
  }

  @Override
  public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction direction) {
    return direction == Direction.UP && canPlaceItem(slot, stack);
  }

  @Override
  public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
    return false;
  }

  @Override
  public int getContainerSize() {
    return SIZE;
  }

  @Override
  public boolean isEmpty() {
    return items.stream().allMatch(ItemStack::isEmpty);
  }

  @Override
  public ItemStack getItem(int slot) {
    return slot >= 0 && slot < SIZE ? items.get(slot) : ItemStack.EMPTY;
  }

  @Override
  public ItemStack removeItem(int slot, int amount) {
    ItemStack removed = ContainerHelper.removeItem(items, slot, amount);
    if (!removed.isEmpty()) {
      setChangedAndSync();
    }
    return removed;
  }

  @Override
  public ItemStack removeItemNoUpdate(int slot) {
    if (slot < 0 || slot >= SIZE) {
      return ItemStack.EMPTY;
    }
    ItemStack removed = items.get(slot);
    if (removed.isEmpty()) {
      return ItemStack.EMPTY;
    }
    items.set(slot, ItemStack.EMPTY);
    setChangedAndSync();
    return removed;
  }

  @Override
  public void setItem(int slot, ItemStack stack) {
    if (slot < 0 || slot >= SIZE) {
      return;
    }
    ItemStack one = stack.copy();
    if (!one.isEmpty()) {
      one.setCount(1);
    }
    items.set(slot, one);
    setChangedAndSync();
  }

  @Override
  public boolean canPlaceItem(int slot, ItemStack stack) {
    return slot >= 0 && slot < SIZE && items.get(slot).isEmpty() && ShelfBlock.isValidItem(stack);
  }

  @Override
  public boolean stillValid(Player player) {
    if (level == null || level.getBlockEntity(worldPosition) != this) {
      return false;
    }
    return player.distanceToSqr(
            worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D)
        <= 64.0D;
  }

  @Override
  public void clearContent() {
    items.clear();
    setChangedAndSync();
  }

  private void setChangedAndSync() {
    setChanged();
    if (level != null && !level.isClientSide()) {
      level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }
  }

  @Override
  protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
    items.clear();
    ContainerHelper.loadAllItems(tag, items, registries);
    for (ItemStack stack : items) {
      if (!stack.isEmpty()) {
        stack.setCount(1);
      }
    }
  }

  @Override
  protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.saveAdditional(tag, registries);
    ContainerHelper.saveAllItems(tag, items, registries);
  }

  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
    return saveWithoutMetadata(registries);
  }

  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }
}
