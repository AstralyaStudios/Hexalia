package net.astralya.hexalia.worldgen.feature;

import com.mojang.serialization.Codec;
import net.astralya.hexalia.block.custom.CelestialBloomBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class WildCropFeature extends Feature<WildCropConfiguration> {

  public WildCropFeature(Codec<WildCropConfiguration> codec) {
    super(codec);
  }

  @Override
  public boolean place(FeaturePlaceContext<WildCropConfiguration> context) {
    WildCropConfiguration config = context.config();
    RandomSource random = context.random();
    BlockPos origin = context.origin();
    WorldGenLevel level = context.level();

    int placed = 0;
    for (int i = 0; i < config.tries(); i++) {
      BlockPos candidate =
          origin.offset(
              random.nextInt(config.xzSpread() * 2 + 1) - config.xzSpread(),
              random.nextInt(config.ySpread() * 2 + 1) - config.ySpread(),
              random.nextInt(config.xzSpread() * 2 + 1) - config.xzSpread());

      if (level.isOutsideBuildHeight(candidate)) continue;

      boolean positionValid =
          config.waterPlant()
              ? level.getBlockState(candidate).is(Blocks.WATER)
              : level.getBlockState(candidate).isAir();

      if (positionValid && config.groundPredicate().test(level, candidate.below())) {
        BlockState placedState = config.toPlace().getState(random, candidate);
        level.setBlock(candidate, placedState, Block.UPDATE_CLIENTS);
        if (placedState.getBlock() instanceof CelestialBloomBlock) {
          level.scheduleTick(candidate, placedState.getBlock(), 20 + random.nextInt(40));
        }
        placed++;
      }
    }
    return placed > 0;
  }
}
