package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CaveVinesPlantBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GaleberriesVinePlantBlock extends CaveVinesPlantBlock {

  public static final BooleanProperty BERRIES = BlockStateProperties.BERRIES;
  private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 16, 14);

  public GaleberriesVinePlantBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected GrowingPlantHeadBlock getHeadBlock() {
    return (GrowingPlantHeadBlock) ModBlocks.GALEBERRIES_VINE.get();
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
  protected BlockState updateHeadAfterConvertedFromBody(BlockState state, BlockState state1) {
    return state1.setValue(BERRIES, state.getValue(BERRIES));
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
