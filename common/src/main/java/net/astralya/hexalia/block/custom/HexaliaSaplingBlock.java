package net.astralya.hexalia.block.custom;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class HexaliaSaplingBlock extends SaplingBlock {
  private static final float BONEMEAL_SUCCESS_CHANCE = 0.45F;

  public HexaliaSaplingBlock(
      String name, ResourceKey<ConfiguredFeature<?, ?>> configuredFeature, Properties properties) {
    super(createTreeGrower(name, configuredFeature), properties.randomTicks());
  }

  private static TreeGrower createTreeGrower(
      String name, ResourceKey<ConfiguredFeature<?, ?>> configuredFeature) {
    return new TreeGrower(name, Optional.empty(), Optional.of(configuredFeature), Optional.empty());
  }

  @Override
  public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
    return true;
  }

  @Override
  public boolean isBonemealSuccess(
      Level level, RandomSource random, BlockPos pos, BlockState state) {
    return random.nextFloat() < BONEMEAL_SUCCESS_CHANCE;
  }

  @Override
  public void performBonemeal(
      ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
    advanceTree(level, pos, state, random);
  }
}
