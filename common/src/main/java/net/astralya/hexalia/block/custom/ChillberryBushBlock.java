package net.astralya.hexalia.block.custom;

import com.mojang.serialization.MapCodec;
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
import net.minecraft.world.ItemInteractionResult;
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
  public static final MapCodec<ChillberryBushBlock> CODEC = simpleCodec(ChillberryBushBlock::new);
  public static final int MAX_AGE = 3;
  public static final IntegerProperty AGE = IntegerProperty.create("age", 0, MAX_AGE);

  private static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 10.0, 11.0);

  public ChillberryBushBlock(Properties properties) {
    super(properties);
    registerDefaultState(stateDefinition.any().setValue(AGE, 0));
  }

  @Override
  protected MapCodec<? extends BushBlock> codec() {
    return CODEC;
  }

  @Override
  protected VoxelShape getShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return SHAPE;
  }

  @Override
  protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
    if (entity instanceof LivingEntity
        && !(entity instanceof CacofeyEntity)
        && entity.getType() != EntityType.FOX
        && entity.getType() != EntityType.BEE
        && state.getValue(AGE) >= 2) {
      entity.makeStuckInBlock(state, new Vec3(0.8F, 0.75D, 0.8F));
      entity.setIsInPowderSnow(true);
      if (level.isClientSide()) {
        RandomSource random = level.getRandom();
        if (random.nextBoolean()) {
          level.addParticle(
              ParticleTypes.SNOWFLAKE,
              entity.getX(),
              pos.getY() + 1,
              entity.getZ(),
              Mth.randomBetween(random, -1.0F, 1.0F) * 0.083333336F,
              0.05D,
              Mth.randomBetween(random, -1.0F, 1.0F) * 0.083333336F);
        }
      }
    }
  }

  @Override
  protected InteractionResult useWithoutItem(
      BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
    int age = state.getValue(AGE);
    boolean mature = age == MAX_AGE;
    if (age > 1) {
      int count = 1 + level.random.nextInt(2) + (mature ? 1 : 0);
      popResource(level, pos, new ItemStack(ModItems.CHILLBERRIES.get(), count));
      level.playSound(
          null,
          pos,
          SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
          SoundSource.BLOCKS,
          1.0F,
          0.8F + level.random.nextFloat() * 0.4F);
      BlockState harvested = state.setValue(AGE, 1);
      level.setBlock(pos, harvested, 2);
      level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, harvested));
      return InteractionResult.sidedSuccess(level.isClientSide());
    }
    return super.useWithoutItem(state, level, pos, player, hitResult);
  }

  @Override
  protected ItemInteractionResult useItemOn(
      ItemStack stack,
      BlockState state,
      Level level,
      BlockPos pos,
      Player player,
      InteractionHand hand,
      BlockHitResult hitResult) {
    return state.getValue(AGE) != MAX_AGE && stack.is(Items.BONE_MEAL)
        ? ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION
        : super.useItemOn(stack, state, level, pos, player, hand, hitResult);
  }

  @Override
  public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
    return state.getValue(AGE) < MAX_AGE;
  }

  @Override
  public boolean isBonemealSuccess(
      Level level, RandomSource random, BlockPos pos, BlockState state) {
    return true;
  }

  @Override
  public void performBonemeal(
      ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
    level.setBlock(pos, state.setValue(AGE, Math.min(MAX_AGE, state.getValue(AGE) + 1)), 2);
  }

  @Override
  protected boolean isRandomlyTicking(BlockState state) {
    return state.getValue(AGE) < MAX_AGE;
  }

  @Override
  protected void randomTick(
      BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    if (state.getValue(AGE) < MAX_AGE
        && random.nextInt(5) == 0
        && level.getRawBrightness(pos.above(), 0) >= 9) {
      BlockState grown = state.setValue(AGE, state.getValue(AGE) + 1);
      level.setBlock(pos, grown, 2);
      level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(grown));
    }
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(AGE);
  }
}
