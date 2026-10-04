package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.Configuration;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.particle.custom.ColoredSporeParticleOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

public class DreamshroomBlock extends ShroomBlock implements BonemealableBlock {

  public DreamshroomBlock(Properties pProperties) {
    super(pProperties);
  }

  @Override
  protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
    return (Block.isFaceFull(floor.getCollisionShape(world, pos), Direction.UP)
            || floor.is(ModBlocks.INFUSED_DIRT.get()))
        && !floor.is(Blocks.MAGMA_BLOCK);
  }

  @Override
  public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
    if (!Configuration.DREAMSHROOM_EMITS_PARTICLES.get()) return;
    if (pRandom.nextFloat() > 0.35F) return;
    createSporeParticles(pLevel, pPos, pRandom);
  }

  @Override
  public boolean isValidBonemealTarget(
      LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
    return level.getBlockState(pos.below()).is(ModBlocks.INFUSED_DIRT.get());
  }

  public static void createSporeParticles(
      Level world, BlockPos pos, RandomSource random) {
    double x = pos.getX() + 0.2D + random.nextDouble() * 0.6D;
    double y = pos.getY() + 0.15D + random.nextDouble() * 0.4D;
    double z = pos.getZ() + 0.2D + random.nextDouble() * 0.6D;

    world.addParticle(
        new ColoredSporeParticleOptions(new Vector3f(0.95F, 0.45F, 0.75F)),
        x, y, z, 0.0D, 0.0D, 0.0D);
  }

  public boolean isBonemealSuccess(
      Level level, RandomSource random, BlockPos pos, BlockState state) {
    return random.nextFloat() < 0.6F;
  }

  @Override
  public void performBonemeal(
      ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
    HerbSpreading.spread(level, random, pos, state);
  }
}
