package net.astralya.hexalia.block.entity.custom;

import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.block.custom.HerbJarBlock;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class HerbJarBlockEntity extends BlockEntity implements SidedInventory {
    public static final int SIZE = 8;
    private static final int[] SLOTS = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(SIZE, ItemStack.EMPTY);

    public HerbJarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.HERB_JAR, pos, state);
    }

    public boolean canInsert(ItemStack stack) {
        if (!canStore(stack)) return false;
        return items.stream().anyMatch(ItemStack::isEmpty);
    }

    private boolean canStore(ItemStack stack) {
        if (stack.isEmpty() || !stack.isIn(ModTags.Items.HERB_JAR_STORABLE)) return false;
        ItemStack locked = getLockedItem();
        if (!locked.isEmpty() && !ItemStack.canCombine(locked, stack)) return false;
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

    public NbtCompound createStorageNbt() {
        NbtCompound storage = new NbtCompound();
        Inventories.writeNbt(storage, items);
        return storage;
    }

    public void restoreStorageNbt(NbtCompound storage) {
        DefaultedList<ItemStack> restored = DefaultedList.ofSize(SIZE, ItemStack.EMPTY);
        Inventories.readNbt(storage, restored);
        normalize(restored);
        changed();
    }

    private void normalize(DefaultedList<ItemStack> source) {
        items.clear();
        ItemStack identity = ItemStack.EMPTY;
        int target = 0;
        for (ItemStack stack : source) {
            if (stack.isEmpty() || !stack.isIn(ModTags.Items.HERB_JAR_STORABLE)) continue;
            if (!identity.isEmpty() && !ItemStack.canCombine(identity, stack)) continue;
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
        markDirty();
        if (world != null && !world.isClient) {
            BlockState state = getCachedState();
            boolean hasContents = !isEmpty();
            if (state.get(HerbJarBlock.HAS_CONTENTS) != hasContents) {
                world.setBlockState(pos, state.with(HerbJarBlock.HAS_CONTENTS, hasContents), 3);
            } else {
                world.updateListeners(pos, state, state, 3);
            }
        }
    }

    @Override public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, items);
    }
    @Override public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        DefaultedList<ItemStack> restored = DefaultedList.ofSize(SIZE, ItemStack.EMPTY);
        Inventories.readNbt(nbt, restored);
        normalize(restored);
    }
    @Override public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
    @Override public NbtCompound toInitialChunkDataNbt() { return createNbt(); }
    @Override public int size() { return SIZE; }
    @Override public boolean isEmpty() { return items.stream().allMatch(ItemStack::isEmpty); }
    @Override public ItemStack getStack(int slot) { return slot >= 0 && slot < SIZE ? items.get(slot) : ItemStack.EMPTY; }
    @Override public ItemStack removeStack(int slot, int amount) {
        if (slot < 0 || slot >= SIZE || amount <= 0) return ItemStack.EMPTY;
        ItemStack result = items.get(slot);
        items.set(slot, ItemStack.EMPTY);
        if (!result.isEmpty()) changed();
        return result;
    }
    @Override public ItemStack removeStack(int slot) { return removeStack(slot, 1); }
    @Override public void setStack(int slot, ItemStack stack) {
        if (slot < 0 || slot >= SIZE || (!stack.isEmpty() && !isValid(slot, stack))) return;
        items.set(slot, stack.isEmpty() ? ItemStack.EMPTY : stack.copyWithCount(1));
        changed();
    }
    @Override public int[] getAvailableSlots(Direction side) { return SLOTS; }
    @Override public boolean canInsert(int slot, ItemStack stack, @Nullable Direction direction) {
        return direction != Direction.DOWN && isValid(slot, stack);
    }
    @Override public boolean canExtract(int slot, ItemStack stack, Direction direction) {
        return direction == Direction.DOWN && slot >= 0 && slot < SIZE && !items.get(slot).isEmpty();
    }
    @Override public boolean isValid(int slot, ItemStack stack) {
        return slot >= 0 && slot < SIZE && items.get(slot).isEmpty() && canStore(stack);
    }
    @Override public void markDirty() { super.markDirty(); }
    @Override public boolean canPlayerUse(PlayerEntity player) {
        return world != null && world.getBlockEntity(pos) == this
                && player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64;
    }
    @Override public void clear() {
        items.clear();
        changed();
    }
    @Override public int getMaxCountPerStack() { return 1; }
}
