package net.astralya.hexalia.block.entity.custom;

import net.astralya.hexalia.block.custom.HerbJarBlock;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.component.item.HerbJarData;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class HerbJarBlockEntity extends BlockEntity implements WorldlyContainer, Clearable {
  public static final int SIZE = 8;
  private static final int[] SLOTS = {0, 1, 2, 3, 4, 5, 6, 7};

  private final NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);

  public HerbJarBlockEntity(BlockPos pos, BlockState state) {
    super(ModBlockEntityTypes.HERB_JAR.get(), pos, state);
  }

  public HerbJarData createData() {
    return HerbJarData.fromItems(items);
  }

  public void restoreData(HerbJarData data) {
    NonNullList<ItemStack> restored = NonNullList.withSize(SIZE, ItemStack.EMPTY);
    for (int slot = 0; slot < SIZE; slot++) {
      restored.set(slot, data.getItem(slot));
    }
    restoreItems(restored);
    setChangedAndSync();
  }

  public boolean canInsert(ItemStack stack) {
    if (!canStore(stack)) {
      return false;
    }
    for (ItemStack stored : items) {
      if (stored.isEmpty()) {
        return true;
      }
    }
    return false;
  }

  public int insert(ItemStack stack) {
    if (!canInsert(stack)) {
      return 0;
    }

    for (int slot = 0; slot < SIZE; slot++) {
      if (items.get(slot).isEmpty()) {
        items.set(slot, stack.copyWithCount(1));
        setChangedAndSync();
        return 1;
      }
    }
    return 0;
  }

  public ItemStack extractLastStack() {
    for (int slot = SIZE - 1; slot >= 0; slot--) {
      if (!items.get(slot).isEmpty()) {
        return removeItemNoUpdate(slot);
      }
    }
    return ItemStack.EMPTY;
  }

  @Override
  public int getContainerSize() {
    return SIZE;
  }

  @Override
  public int getMaxStackSize() {
    return 1;
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
    ItemStack removed = ContainerHelper.takeItem(items, slot);
    if (!removed.isEmpty()) {
      setChangedAndSync();
    }
    return removed;
  }

  @Override
  public void setItem(int slot, ItemStack stack) {
    if (slot < 0 || slot >= SIZE || (!stack.isEmpty() && !canPlaceItem(slot, stack))) {
      return;
    }
    ItemStack stored = stack.copy();
    if (!stored.isEmpty()) {
      stored.setCount(1);
    }
    items.set(slot, stored);
    setChangedAndSync();
  }

  @Override
  public boolean canPlaceItem(int slot, ItemStack stack) {
    return slot >= 0 && slot < SIZE && items.get(slot).isEmpty() && canStore(stack);
  }

  private boolean canStore(ItemStack stack) {
    if (stack.isEmpty() || !stack.is(ModTags.Items.HERB_JAR_STORABLE)) {
      return false;
    }
    ItemStack lockedItem = getLockedItem();
    return lockedItem.isEmpty() || ItemStack.isSameItemSameComponents(lockedItem, stack);
  }

  @Override
  public int[] getSlotsForFace(Direction side) {
    return SLOTS;
  }

  @Override
  public boolean canPlaceItemThroughFace(
      int slot, ItemStack stack, @Nullable Direction direction) {
    return direction != Direction.DOWN && canPlaceItem(slot, stack);
  }

  @Override
  public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
    return direction == Direction.DOWN && slot >= 0 && slot < SIZE && !items.get(slot).isEmpty();
  }

  @Override
  public boolean stillValid(Player player) {
    return Container.stillValidBlockEntity(this, player);
  }

  @Override
  public void clearContent() {
    items.clear();
    setChangedAndSync();
  }

  private ItemStack getLockedItem() {
    for (ItemStack stack : items) {
      if (!stack.isEmpty()) {
        return stack;
      }
    }
    return ItemStack.EMPTY;
  }

  private void restoreItems(NonNullList<ItemStack> restored) {
    items.clear();
    int targetSlot = 0;
    for (ItemStack stack : restored) {
      for (int count = 0; count < stack.getCount() && targetSlot < SIZE; count++) {
        if (!canPlaceItem(targetSlot, stack)) {
          break;
        }
        items.set(targetSlot++, stack.copyWithCount(1));
      }
    }
  }

  private void setChangedAndSync() {
    setChanged();
    if (level != null && !level.isClientSide()) {
      BlockState state = getBlockState();
      boolean hasContents = !isEmpty();
      if (state.getValue(HerbJarBlock.HAS_CONTENTS) != hasContents) {
        level.setBlock(worldPosition, state.setValue(HerbJarBlock.HAS_CONTENTS, hasContents), 3);
      } else {
        level.sendBlockUpdated(worldPosition, state, state, 3);
      }
    }
  }

  @Override
  protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
    NonNullList<ItemStack> restored = NonNullList.withSize(SIZE, ItemStack.EMPTY);
    ContainerHelper.loadAllItems(tag, restored, registries);
    restoreItems(restored);
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
