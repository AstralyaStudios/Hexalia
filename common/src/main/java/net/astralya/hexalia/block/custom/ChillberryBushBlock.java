package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.entity.custom.CacofeyEntity;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ChillberryBushBlock extends BushBlock implements BonemealableBlock {

  protected static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D);

  public static final int MAX_AGE = 3;
  public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 3);

  public ChillberryBushBlock(Properties pProperties) {
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
    return new ItemStack(ModItems.CHILLBERRIES.get());
  }

  @Override
  public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
    if (pEntity instanceof LivingEntity
        && !(pEntity instanceof CacofeyEntity)
        && pEntity.getType() != EntityType.FOX
        && pEntity.getType() != EntityType.BEE
        && pState.getValue(AGE) >= 2) {
      pEntity.makeStuckInBlock(pState, new Vec3(0.8f, 0.75, 0.8f));
      pEntity.setIsInPowderSnow(true);
      if (pLevel.isClientSide) {
        RandomSource randomsource = pLevel.getRandom();
        boolean flag = pEntity.xOld != pEntity.getX() || pEntity.zOld != pEntity.getZ();
        if (flag && randomsource.nextBoolean()) {
          pLevel.addParticle(
              ParticleTypes.SNOWFLAKE,
              pEntity.getX(),
              pPos.getY() + 1,
              pEntity.getZ(),
              Mth.randomBetween(randomsource, -1.0F, 1.0F) * 0.083333336F,
              0.05F,
              Mth.randomBetween(randomsource, -1.0F, 1.0F) * 0.083333336F);
        }
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
    ItemStack itemStack = pPlayer.getItemInHand(pHand);

    if (age == MAX_AGE) {
      int berryCount = 1 + pLevel.random.nextInt(2);
      popResource(pLevel, pPos, new ItemStack(ModItems.CHILLBERRIES.get(), berryCount + 1));
      pLevel.playSound(
          null,
          pPos,
          SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
          SoundSource.BLOCKS,
          1.0F,
          0.8F + pLevel.random.nextFloat() * 0.4F);
      pLevel.setBlockAndUpdate(pPos, pState.setValue(AGE, 1));
      pLevel.gameEvent(
          GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pPlayer, pState.setValue(AGE, 1)));
      return InteractionResult.SUCCESS;
    } else if (age > 1) {
      int berryCount = 1 + pLevel.random.nextInt(2);
      popResource(pLevel, pPos, new ItemStack(ModItems.CHILLBERRIES.get(), berryCount));
      pLevel.playSound(
          null,
          pPos,
          SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
          SoundSource.BLOCKS,
          1.0F,
          0.8F + pLevel.random.nextFloat() * 0.4F);
      pLevel.setBlockAndUpdate(pPos, pState.setValue(AGE, 1));
      pLevel.gameEvent(
          GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pPlayer, pState.setValue(AGE, 1)));
      return InteractionResult.SUCCESS;
    } else if (itemStack.is(Items.BONE_MEAL)) {
      return InteractionResult.PASS;
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
