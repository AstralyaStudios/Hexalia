package net.astralya.hexalia.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

final class HerbSpreading {
    private HerbSpreading() {}
    static void spread(ServerWorld world, Random random, BlockPos origin, BlockState state) {
        spread(world, random, origin, state, false);
    }
    static void spread(ServerWorld world, Random random, BlockPos origin, BlockState state, boolean aquatic) {
        int remaining = 8;
        for (BlockPos nearby : BlockPos.iterate(origin.add(-3, -1, -3), origin.add(3, 1, 3))) {
            if (world.getBlockState(nearby).isOf(state.getBlock()) && --remaining <= 0) return;
        }
        BlockPos anchor = origin;
        BlockPos candidate = step(anchor, random);
        for (int attempt = 0; attempt < 3; attempt++) {
            if (isValidTarget(world, candidate, state, aquatic)) anchor = candidate;
            candidate = step(anchor, random);
        }
        if (isValidTarget(world, candidate, state, aquatic))
            world.setBlockState(candidate, state, Block.NOTIFY_ALL);
    }
    private static boolean isValidTarget(ServerWorld world, BlockPos pos, BlockState state, boolean aquatic) {
        boolean available = aquatic
                ? world.getBlockState(pos).isOf(Blocks.WATER) && world.getFluidState(pos).isIn(FluidTags.WATER)
                : world.isAir(pos);
        return available && state.canPlaceAt(world, pos);
    }
    private static BlockPos step(BlockPos origin, Random random) {
        return origin.add(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
    }
}
