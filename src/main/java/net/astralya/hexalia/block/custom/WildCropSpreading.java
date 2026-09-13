package net.astralya.hexalia.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

final class WildCropSpreading {
    private WildCropSpreading() {}

    static void spread(ServerWorld world, Random random, BlockPos origin, BlockState state) {
        int remaining = 10;
        for (BlockPos nearby : BlockPos.iterate(origin.add(-4, -1, -4), origin.add(4, 1, 4))) {
            if (world.getBlockState(nearby).isOf(state.getBlock()) && --remaining <= 0) return;
        }

        BlockPos anchor = origin;
        BlockPos candidate = step(anchor, random);
        for (int attempt = 0; attempt < 4; attempt++) {
            if (world.isAir(candidate) && state.canPlaceAt(world, candidate)) anchor = candidate;
            candidate = step(anchor, random);
        }
        if (world.isAir(candidate) && state.canPlaceAt(world, candidate))
            world.setBlockState(candidate, state, Block.NOTIFY_ALL);
    }

    private static BlockPos step(BlockPos origin, Random random) {
        return origin.add(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
    }
}
