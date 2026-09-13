package net.astralya.hexalia.block.entity.custom;

import net.astralya.hexalia.block.custom.HerbJarBlock;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HerbJarBlockEntity extends BlockEntity implements WorldlyContainer {
    public static final int SIZE = 8;
    private static final int[] SLOTS = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
    private final NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);
    private final LazyOptional<IItemHandler> inputOptional = LazyOptional.of(() -> new SidedInvWrapper(this, Direction.UP));
    private final LazyOptional<IItemHandler> outputOptional = LazyOptional.of(() -> new SidedInvWrapper(this, Direction.DOWN));

    public HerbJarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.HERB_JAR.get(), pos, state);
    }

    public boolean canInsert(ItemStack stack) {
        if (!canStore(stack)) return false;
        return items.stream().anyMatch(ItemStack::isEmpty);
    }

    private boolean canStore(ItemStack stack) {
        if (stack.isEmpty() || !stack.is(ModTags.Items.HERB_JAR_STORABLE)) return false;
        ItemStack locked = getLockedItem();
        if (!locked.isEmpty() && !ItemStack.isSameItemSameTags(locked, stack)) return false;
        return true;
    }

    public boolean insertOne(ItemStack stack) {
        if (!canInsert(stack)) return false;
        for (int slot = 0; slot < SIZE; slot++) {
            if (items.get(slot).isEmpty()) {
                items.set(slot, stack.copyWithCount(1));
                changed();
                return true;
            }
        }
        return false;
    }

    public ItemStack extractOne() {
        for (int slot = SIZE - 1; slot >= 0; slot--) {
            if (!items.get(slot).isEmpty()) {
                ItemStack result = items.get(slot);
                items.set(slot, ItemStack.EMPTY);
                changed();
                return result;
            }
        }
        return ItemStack.EMPTY;
    }

    public CompoundTag createStorageNbt() {
        CompoundTag storage = new CompoundTag();
        ContainerHelper.saveAllItems(storage, items);
        return storage;
    }

    public void restoreStorageNbt(CompoundTag storage) {
        NonNullList<ItemStack> restored = NonNullList.withSize(SIZE, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(storage, restored);
        normalize(restored);
        changed();
    }

    private void normalize(NonNullList<ItemStack> source) {
        items.clear();
        ItemStack identity = ItemStack.EMPTY;
        int target = 0;
        for (ItemStack stack : source) {
            if (stack.isEmpty() || !stack.is(ModTags.Items.HERB_JAR_STORABLE)) continue;
            if (!identity.isEmpty() && !ItemStack.isSameItemSameTags(identity, stack)) continue;
            if (identity.isEmpty()) identity = stack.copyWithCount(1);
            for (int count = 0; count < stack.getCount() && target < SIZE; count++) {
                items.set(target++, stack.copyWithCount(1));
            }
        }
    }

    private ItemStack getLockedItem() {
        for (ItemStack stack : items) if (!stack.isEmpty()) return stack;
        return ItemStack.EMPTY;
    }

    private void changed() {
        setChanged();
        if (level != null && !level.isClientSide) {
            BlockState state = getBlockState();
            boolean hasContents = !isEmpty();
            if (state.getValue(HerbJarBlock.HAS_CONTENTS) != hasContents) {
                level.setBlock(worldPosition, state.setValue(HerbJarBlock.HAS_CONTENTS, hasContents), 3);
            } else {
                level.sendBlockUpdated(worldPosition, state, state, 3);
            }
        }
    }

    @Override public void load(CompoundTag tag) {
        super.load(tag);
        NonNullList<ItemStack> restored = NonNullList.withSize(SIZE, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, restored);
        normalize(restored);
    }
    @Override protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, items);
    }
    @Override public CompoundTag getUpdateTag() { return saveWithoutMetadata(); }
    @Override public @Nullable ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override public int getContainerSize() { return SIZE; }
    @Override public boolean isEmpty() { return items.stream().allMatch(ItemStack::isEmpty); }
    @Override public ItemStack getItem(int slot) { return slot >= 0 && slot < SIZE ? items.get(slot) : ItemStack.EMPTY; }
    @Override public ItemStack removeItem(int slot, int amount) {
        if (slot < 0 || slot >= SIZE || amount <= 0) return ItemStack.EMPTY;
        ItemStack result = items.get(slot);
        items.set(slot, ItemStack.EMPTY);
        if (!result.isEmpty()) changed();
        return result;
    }
    @Override public ItemStack removeItemNoUpdate(int slot) { return removeItem(slot, 1); }
    @Override public void setItem(int slot, ItemStack stack) {
        if (slot < 0 || slot >= SIZE || (!stack.isEmpty() && !canPlaceItem(slot, stack))) return;
        items.set(slot, stack.isEmpty() ? ItemStack.EMPTY : stack.copyWithCount(1));
        changed();
    }
    @Override public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot >= 0 && slot < SIZE && items.get(slot).isEmpty() && canStore(stack);
    }
    @Override public int[] getSlotsForFace(Direction side) { return SLOTS; }
    @Override public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction direction) {
        return direction != Direction.DOWN && canPlaceItem(slot, stack);
    }
    @Override public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return direction == Direction.DOWN && slot >= 0 && slot < SIZE && !items.get(slot).isEmpty();
    }
    @Override public boolean stillValid(Player player) {
        return level != null && level.getBlockEntity(worldPosition) == this
                && player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5,
                worldPosition.getZ() + 0.5) <= 64;
    }
    @Override public void clearContent() {
        items.clear();
        changed();
    }
    @Override public int getMaxStackSize() { return 1; }
    @Override public void setRemoved() {
        super.setRemoved();
        inputOptional.invalidate();
        outputOptional.invalidate();
    }
    @Override public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            return (side == Direction.DOWN ? outputOptional : inputOptional).cast();
        }
        return super.getCapability(capability, side);
    }
}
