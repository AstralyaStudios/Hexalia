package net.astralya.hexalia.block.entity.custom.fabric;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public final class InventoryPlatformImpl {
    private InventoryPlatformImpl() {}

    public static @Nullable ItemStack insertAbove(Level level, BlockPos pos, ItemStack stack) {
        Storage<ItemVariant> storage = ItemStorage.SIDED.find(level, pos, Direction.UP);
        if (storage == null) return null;
        ItemStack remaining = stack.copy();
        try (Transaction transaction = Transaction.openOuter()) {
            long inserted = storage.insert(ItemVariant.of(stack), stack.getCount(), transaction);
            transaction.commit();
            remaining.shrink((int) inserted);
        }
        return remaining;
    }

    public static @Nullable ItemStack insertAny(Level level, BlockPos pos, ItemStack stack) {
        for (Direction direction : Direction.values()) {
            Storage<ItemVariant> storage = ItemStorage.SIDED.find(level, pos, direction);
            if (storage == null) continue;
            if (stack.isEmpty()) return ItemStack.EMPTY;
            ItemStack remaining = stack.copy();
            try (Transaction transaction = Transaction.openOuter()) {
                long inserted = storage.insert(ItemVariant.of(stack), stack.getCount(), transaction);
                transaction.commit();
                remaining.shrink((int) inserted);
            }
            return remaining;
        }
        return null;
    }
}
