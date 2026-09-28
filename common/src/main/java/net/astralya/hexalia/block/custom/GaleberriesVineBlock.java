package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CaveVinesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GaleberriesVineBlock extends CaveVinesBlock implements BonemealableBlock {

  public static final BooleanProperty BERRIES = BlockStateProperties.BERRIES;
  private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 16, 14);

  public GaleberriesVineBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected Block getBodyBlock() {
    return ModBlocks.GALEBERRIES_VINE_PLANT.get();
  }

  @Override
  public InteractionResult use(
      BlockState state,
      Level level,
      BlockPos pos,
      Player player,
      InteractionHand hand,
      BlockHitResult hit) {
    if (state.getValue(BERRIES)) {
      if (!level.isClientSide) {
        Block.popResource(level, pos, new ItemStack(ModItems.GALEBERRIES.get()));
        level.setBlock(pos, state.setValue(BERRIES, false), 2);
      }
      return InteractionResult.sidedSuccess(level.isClientSide);
    }
    return InteractionResult.PASS;
  }

  @Override
  public boolean isValidBonemealTarget(
      LevelReader levelReader, BlockPos pos, BlockState state, boolean isClient) {
    return !state.getValue(BERRIES);
  }

  @Override
  public boolean isBonemealSuccess(
      Level level, RandomSource random, BlockPos pos, BlockState state) {
    return true;
  }

  @Override
  public void performBonemeal(
      ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
    level.setBlock(pos, state.setValue(BERRIES, true), 2);
  }

  @Override
  public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
    return new ItemStack(ModItems.GALEBERRIES.get());
  }

  @Override
  public VoxelShape getShape(
      BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
    return SHAPE;
  }
}
