package net.astralya.hexalia.util;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public final class ContainerItemHandler implements ItemHandler {
    private final Container container;

    public ContainerItemHandler(Container container) { this.container = container; }
    public int getSlots() { return container.getContainerSize(); }
    public ItemStack getStackInSlot(int slot) { return container.getItem(slot); }
    public int getSlotLimit(int slot) { return container.getMaxStackSize(); }
    public boolean isItemValid(int slot, ItemStack stack) { return container.canPlaceItem(slot, stack); }

    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        ItemStack current = container.getItem(slot);
        if (!isItemValid(slot, stack) || (!current.isEmpty() && !ItemStack.isSameItemSameTags(current, stack))) return stack;
        int accepted = Math.min(stack.getCount(), getSlotLimit(slot) - current.getCount());
        if (accepted <= 0) return stack;
        if (!simulate) {
            if (current.isEmpty()) {
                ItemStack copy = stack.copy();
                copy.setCount(accepted);
                container.setItem(slot, copy);
            } else current.grow(accepted);
            container.setChanged();
        }
        ItemStack remainder = stack.copy();
        remainder.shrink(accepted);
        return remainder;
    }

    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack current = container.getItem(slot);
        if (current.isEmpty()) return ItemStack.EMPTY;
        int count = Math.min(amount, current.getCount());
        ItemStack result = current.copy();
        result.setCount(count);
        if (!simulate) container.removeItem(slot, count);
        return result;
    }
}
