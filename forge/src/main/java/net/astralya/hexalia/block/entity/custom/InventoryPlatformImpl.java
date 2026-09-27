package net.astralya.hexalia.block.entity.custom.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.Nullable;

public final class InventoryPlatformImpl {
    private InventoryPlatformImpl() {}

    public static @Nullable ItemStack insertAbove(Level level, BlockPos pos, ItemStack stack) {
        var blockEntity = level.getBlockEntity(pos);
        if (blockEntity == null) return null;
        var capability = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.UP);
        if (!capability.isPresent()) return null;
        ItemStack remaining = stack;
        var handler = capability.orElseThrow(IllegalStateException::new);
        for (int slot = 0; slot < handler.getSlots() && !remaining.isEmpty(); slot++) {
            remaining = handler.insertItem(slot, remaining, false);
        }
        return remaining;
    }

    public static @Nullable ItemStack insertAny(Level level, BlockPos pos, ItemStack stack) {
        var blockEntity = level.getBlockEntity(pos);
        if (blockEntity == null) return null;
        for (Direction direction : Direction.values()) {
            var capability = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, direction);
            if (!capability.isPresent()) continue;
            ItemStack remaining = stack;
            var handler = capability.orElseThrow(IllegalStateException::new);
            for (int slot = 0; slot < handler.getSlots() && !remaining.isEmpty(); slot++) {
                remaining = handler.insertItem(slot, remaining, false);
            }
            return remaining;
        }
        return null;
    }
}

