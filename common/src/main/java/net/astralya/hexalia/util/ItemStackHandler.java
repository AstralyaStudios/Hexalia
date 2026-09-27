package net.astralya.hexalia.util;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;

public class ItemStackHandler implements ItemHandler {
    private final NonNullList<ItemStack> stacks;

    public ItemStackHandler(int size) {
        this.stacks = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    public int getSlots() { return stacks.size(); }
    public ItemStack getStackInSlot(int slot) { return stacks.get(slot); }
    public int getSlotLimit(int slot) { return 64; }
    public boolean isItemValid(int slot, ItemStack stack) { return true; }

    public void setStackInSlot(int slot, ItemStack stack) {
        stacks.set(slot, stack);
        onContentsChanged(slot);
    }

    public ItemStack insertItem(int slot, ItemStack incoming, boolean simulate) {
        if (incoming.isEmpty() || !isItemValid(slot, incoming)) return incoming;
        ItemStack stored = stacks.get(slot);
        if (!stored.isEmpty() && !ItemStack.isSameItemSameTags(stored, incoming)) return incoming;
        int limit = Math.min(getSlotLimit(slot), incoming.getMaxStackSize());
        int accepted = Math.min(limit - stored.getCount(), incoming.getCount());
        if (accepted <= 0) return incoming;
        if (!simulate) {
            if (stored.isEmpty()) {
                ItemStack copy = incoming.copy();
                copy.setCount(accepted);
                stacks.set(slot, copy);
            } else {
                stored.grow(accepted);
            }
            onContentsChanged(slot);
        }
        ItemStack remainder = incoming.copy();
        remainder.shrink(accepted);
        return remainder;
    }

    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack stored = stacks.get(slot);
        if (stored.isEmpty() || amount <= 0) return ItemStack.EMPTY;
        int removed = Math.min(amount, stored.getCount());
        ItemStack result = stored.copy();
        result.setCount(removed);
        if (!simulate) {
            stored.shrink(removed);
            if (stored.isEmpty()) stacks.set(slot, ItemStack.EMPTY);
            onContentsChanged(slot);
        }
        return result;
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ContainerHelper.saveAllItems(tag, stacks);
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        for (int slot = 0; slot < stacks.size(); slot++) stacks.set(slot, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, stacks);
    }

    protected void onContentsChanged(int slot) {}
}
