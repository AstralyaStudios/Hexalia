package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SaltsproutBlock extends BushBlock implements BonemealableBlock {

  protected static final VoxelShape SHAPE = Shapes.or(Block.box(2, 0, 1, 15, 7, 15));
  public static final int MAX_AGE = 2;
  public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 2);

  public SaltsproutBlock(Properties pProperties) {
    super(pProperties);
    registerDefaultState(this.defaultBlockState().setValue(AGE, 0));
  }

  @Override
  public VoxelShape getShape(
      BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
    return SHAPE;
  }

  @Override
  public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
    return new ItemStack(ModItems.SALTSPROUT.get());
  }

  @Override
  public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
    if (!(pEntity instanceof LivingEntity) || pEntity.getType() == EntityType.BEE) {
      return;
    }
    if (!pLevel.isClientSide) {
      double deltaX = Math.abs(pEntity.getX() - pEntity.xOld);
      double deltaZ = Math.abs(pEntity.getZ() - pEntity.zOld);
      if (deltaX >= 0.003f || deltaZ >= 0.003f) {
        pEntity.hurt(pLevel.damageSources().cactus(), 0.5f);
      }
    }
  }

  @Override
  public InteractionResult use(
      BlockState pState,
      Level pLevel,
      BlockPos pPos,
      Player pPlayer,
      InteractionHand pHand,
      BlockHitResult pHit) {
    int age = pState.getValue(AGE);
    if (age == MAX_AGE) {
      int i = 1 + pLevel.random.nextInt(1);
      popResource(pLevel, pPos, new ItemStack(ModItems.SALTSPROUT.get(), i));
      pLevel.playSound(
          null,
          pPos,
          SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
          SoundSource.BLOCKS,
          1.0f,
          0.8f + pLevel.random.nextFloat() * 0.4f);
      BlockState newState = pState.setValue(AGE, 1);
      pLevel.setBlock(pPos, newState, Block.UPDATE_ALL);
      pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pPlayer, newState));
      return InteractionResult.sidedSuccess(pLevel.isClientSide);
    } else {
      return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }
  }

  @Override
  public boolean isValidBonemealTarget(
      LevelReader pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
    return pState.getValue(AGE) < MAX_AGE;
  }

  @Override
  public boolean isBonemealSuccess(
      Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
    return true;
  }

  @Override
  public void performBonemeal(
      ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
    int age = Math.min(MAX_AGE, pState.getValue(AGE) + 1);
    pLevel.setBlock(pPos, pState.setValue(AGE, age), 2);
  }

  @Override
  public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
    for (Direction direction : Direction.Plane.HORIZONTAL) {
      BlockState blockState = pLevel.getBlockState(pPos.relative(direction));
      if (blockState.isSolid()
          || pLevel.getFluidState(pPos.relative(direction)).is(FluidTags.LAVA)) {
        return false;
      }
    }
    BlockState blockBelow = pLevel.getBlockState(pPos.below());
    return (blockBelow.is(ModBlocks.INFUSED_FARMLAND.get()) || blockBelow.is(BlockTags.SAND))
        && !pLevel.getBlockState(pPos.above()).getFluidState().isSource();
  }

  @Override
  public boolean isRandomlyTicking(BlockState state) {
    return state.getValue(AGE) < MAX_AGE;
  }

  @Override
  public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
    if (pState.getValue(AGE) < MAX_AGE
        && pRandom.nextInt(5) == 0
        && pLevel.getRawBrightness(pPos.above(), 0) >= 9) {
      pLevel.setBlockAndUpdate(pPos, pState.setValue(AGE, pState.getValue(AGE) + 1));
      pLevel.gameEvent(
          GameEvent.BLOCK_CHANGE,
          pPos,
          GameEvent.Context.of(pState.setValue(AGE, pState.getValue(AGE) + 1)));
    }
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    pBuilder.add(AGE);
  }
}
