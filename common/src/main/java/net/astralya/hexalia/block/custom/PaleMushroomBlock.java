package net.astralya.hexalia.block.custom;

import java.util.function.BiFunction;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PaleMushroomBlock extends ShroomBlock implements BonemealableBlock {

  public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
  public static final IntegerProperty AMOUNT = IntegerProperty.create("flower_amount", 1, 4);

  private static final BiFunction<Direction, Integer, VoxelShape> SHAPE_BY_PROPERTIES =
      Util.memoize(
          (dir, amount) -> {
            VoxelShape[] quads =
                new VoxelShape[] {
                  Block.box(8.0, 0.0, 8.0, 16.0, 6.0, 16.0),
                  Block.box(8.0, 0.0, 0.0, 16.0, 6.0, 8.0),
                  Block.box(0.0, 0.0, 0.0, 8.0, 6.0, 8.0),
                  Block.box(0.0, 0.0, 8.0, 8.0, 6.0, 16.0)
                };
            VoxelShape shape = Shapes.empty();
            for (int i = 0; i < amount; i++) {
              int idx = Math.floorMod(i - dir.get2DDataValue(), 4);
              shape = Shapes.or(shape, quads[idx]);
            }
            return shape;
          });

  public PaleMushroomBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(
        this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(AMOUNT, 1));
  }

  @Override
  public VoxelShape getShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return SHAPE_BY_PROPERTIES.apply(state.getValue(FACING), state.getValue(AMOUNT));
  }

  @Override
  public boolean canBeReplaced(BlockState state, BlockPlaceContext ctx) {
    return (!ctx.isSecondaryUseActive()
            && ctx.getItemInHand().is(this.asItem())
            && state.getValue(AMOUNT) < 4)
        || super.canBeReplaced(state, ctx);
  }

  @Override
  @Nullable public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    BlockState existing = ctx.getLevel().getBlockState(ctx.getClickedPos());
    if (existing.is(this)) {
      int next = Math.min(4, existing.getValue(AMOUNT) + 1);
      return existing.setValue(AMOUNT, next);
    }
    BlockState base =
        this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    return this.canSurvive(base, ctx.getLevel(), ctx.getClickedPos()) ? base : null;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) {
    b.add(FACING, AMOUNT);
  }

  @Override
  public BlockState rotate(BlockState state, Rotation rot) {
    return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
  }

  @Override
  public BlockState mirror(BlockState state, Mirror mirror) {
    return state.rotate(mirror.getRotation(state.getValue(FACING)));
  }

  @Override
  public boolean isValidBonemealTarget(
      LevelReader level, BlockPos pos, BlockState state, boolean isClientSide) {
    return true;
  }

  @Override
  public boolean isBonemealSuccess(
      Level level, RandomSource random, BlockPos pos, BlockState state) {
    return true;
  }

  @Override
  public void performBonemeal(
      ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
    int amount = state.getValue(AMOUNT);
    if (amount < 4) {
      level.setBlock(pos, state.setValue(AMOUNT, amount + 1), 2);
    } else {
      popResource(level, pos, new ItemStack(this));
    }
  }
}
