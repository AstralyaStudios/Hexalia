package net.astralya.hexalia.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

final class HerbSpreading {
  private HerbSpreading() {}

  static void spread(ServerLevel level, RandomSource random, BlockPos origin, BlockState state) {
    spread(level, random, origin, state, false);
  }

  static void spread(
      ServerLevel level, RandomSource random, BlockPos origin, BlockState state, boolean aquatic) {
    int remaining = 8;
    for (BlockPos nearby :
        BlockPos.betweenClosed(origin.offset(-3, -1, -3), origin.offset(3, 1, 3))) {
      if (level.getBlockState(nearby).is(state.getBlock()) && --remaining <= 0) return;
    }

    BlockPos anchor = origin;
    BlockPos candidate = step(anchor, random);
    for (int attempt = 0; attempt < 3; attempt++) {
      if (isValidTarget(level, candidate, state, aquatic)) anchor = candidate;
      candidate = step(anchor, random);
    }
    if (isValidTarget(level, candidate, state, aquatic))
      level.setBlock(candidate, state, Block.UPDATE_ALL);
  }

  private static boolean isValidTarget(
      ServerLevel level, BlockPos pos, BlockState state, boolean aquatic) {
    boolean available =
        aquatic
            ? level.getBlockState(pos).canBeReplaced()
                && level.getFluidState(pos).is(FluidTags.WATER)
            : level.isEmptyBlock(pos);
    return available && state.canSurvive(level, pos);
  }

  private static BlockPos step(BlockPos origin, RandomSource random) {
    return origin.offset(
        random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
  }
}
