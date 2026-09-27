package net.astralya.hexalia.util;

import net.astralya.hexalia.Configuration;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public final class MutationOutput {

    private MutationOutput() {
    }

    public static void apply(ServerLevel level, BlockPos pos, ItemStack result) {
        if (result.isEmpty()) {
            return;
        }

        if (!Configuration.MUTATION_SPAWNS_ITEM_ENTITY.get()) {
            if (result.getItem() instanceof BlockItem blockItem) {
                Block block = blockItem.getBlock();
                BlockState placed = block.defaultBlockState();
                level.setBlock(pos, placed, Block.UPDATE_ALL);
                return;
            }
        }

        level.addFreshEntity(new ItemEntity(
                level,
                pos.getX() + 0.5D,
                pos.getY() + 0.25D,
                pos.getZ() + 0.5D,
                result.copy()
        ));
    }
}
