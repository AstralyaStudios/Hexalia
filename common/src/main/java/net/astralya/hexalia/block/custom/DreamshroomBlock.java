package net.astralya.hexalia.block.custom;

import com.mojang.serialization.MapCodec;
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
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3f;

public class DreamshroomBlock extends BushBlock implements BonemealableBlock {
  public static final MapCodec<DreamshroomBlock> CODEC = simpleCodec(DreamshroomBlock::new);
  private static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 10.0, 11.0);

  public DreamshroomBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected MapCodec<? extends BushBlock> codec() {
    return CODEC;
  }

  @Override
  protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
    return (state.isFaceSturdy(level, pos, Direction.UP) || state.is(ModBlocks.INFUSED_DIRT.get()))
        && !state.is(Blocks.MAGMA_BLOCK);
  }

  @Override
  protected VoxelShape getShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    Vec3 offset = state.getOffset(level, pos);
    return SHAPE.move(offset.x, offset.y, offset.z);
  }

  @Override
  public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
    return level.getBlockState(pos.below()).is(ModBlocks.INFUSED_DIRT.get());
  }

  @Override
  public boolean isBonemealSuccess(
      Level level, RandomSource random, BlockPos pos, BlockState state) {
    return random.nextFloat() < 0.6F;
  }

  @Override
  public void performBonemeal(
      ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
    HerbSpreading.spread(level, random, pos, state);
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    super.animateTick(state, level, pos, random);

    if (random.nextFloat() > 0.35F) {
      return;
    }

    double x = pos.getX() + 0.2D + random.nextDouble() * 0.6D;
    double y = pos.getY() + 0.15D + random.nextDouble() * 0.4D;
    double z = pos.getZ() + 0.2D + random.nextDouble() * 0.6D;

    level.addParticle(
        new ColoredSporeParticleOptions(new Vector3f(0.95F, 0.45F, 0.75F)),
        x,
        y,
        z,
        0.0D,
        0.0D,
        0.0D);
  }
}
