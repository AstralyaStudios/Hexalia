package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ShroomBlock extends BushBlock {
  public static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 6.0, 11.0);

  public ShroomBlock(BlockBehaviour.Properties properties) {
    super(properties);
  }

  @Override
  public VoxelShape getShape(
      BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
    return SHAPE;
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    if (!canSurvive(defaultBlockState(), context.getLevel(), context.getClickedPos())) {
      return null;
    }
    return defaultBlockState();
  }

  @Override
  public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
    BlockPos blockpos = pPos.below();
    BlockState blockstate = pLevel.getBlockState(blockpos);
    if (blockstate.is(ModBlocks.INFUSED_DIRT.get())) {
      return mayPlaceOn(blockstate, pLevel, blockpos);
    }
    if (blockstate.is(BlockTags.MUSHROOM_GROW_BLOCK)) {
      return true;
    } else {
      return pLevel.getRawBrightness(pPos, 0) < 13 && mayPlaceOn(blockstate, pLevel, blockpos);
    }
  }

  @Override
  protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
    return pState.isSolidRender(pLevel, pPos);
  }
}
