package net.astralya.hexalia.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

final class WildCropSpreading {
  private WildCropSpreading() {}

  static void spread(ServerLevel level, RandomSource random, BlockPos origin, BlockState state) {
    int remaining = 10;
    for (BlockPos nearby : BlockPos.betweenClosed(origin.offset(-4, -1, -4), origin.offset(4, 1, 4))) {
      if (level.getBlockState(nearby).is(state.getBlock()) && --remaining <= 0) return;
    }

    BlockPos anchor = origin;
    BlockPos candidate = step(anchor, random);
    for (int attempt = 0; attempt < 4; attempt++) {
      if (level.isEmptyBlock(candidate) && state.canSurvive(level, candidate)) anchor = candidate;
      candidate = step(anchor, random);
    }
    if (level.isEmptyBlock(candidate) && state.canSurvive(level, candidate))
      level.setBlock(candidate, state, Block.UPDATE_ALL);
  }

  private static BlockPos step(BlockPos origin, RandomSource random) {
    return origin.offset(
        random.nextInt(3) - 1,
        random.nextInt(2) - random.nextInt(2),
        random.nextInt(3) - 1);
  }
}
