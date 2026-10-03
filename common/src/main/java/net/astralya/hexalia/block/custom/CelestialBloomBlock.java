package net.astralya.hexalia.block.custom;

import java.util.function.Supplier;
import net.astralya.hexalia.Configuration;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.particle.ModParticleType;
import net.astralya.hexalia.util.CelestialTime;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

public class CelestialBloomBlock extends HerbBlock {
  public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

  private static final double MIN_X = 5.0D;
  private static final double MAX_X = 11.0D;
  private static final double MIN_Z = 5.0D;
  private static final double MAX_Z = 11.0D;
  private static final double HEIGHT = 10.0D;

  public CelestialBloomBlock(
      Supplier<MobEffect> effectSupplier, int effectDuration, Properties properties) {
    super(effectSupplier, effectDuration, properties);
    registerDefaultState(defaultBlockState().setValue(OPEN, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(OPEN);
  }

  @Override
  public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
    return defaultBlockState().setValue(OPEN, CelestialTime.isNight(context.getLevel()));
  }

  @Override
  public void onPlace(
      BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
    super.onPlace(state, level, pos, oldState, movedByPiston);
    if (!level.isClientSide && !oldState.is(this) && CelestialTime.hasDayNightCycle(level)) {
      level.scheduleTick(pos, this, 20 + level.getRandom().nextInt(40));
    }
  }

  @Override
  public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    updateOpenState(state, level, pos, random);
  }

  @Override
  public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    if (!CelestialTime.hasDayNightCycle(level)) return;
    updateOpenState(state, level, pos, random);
    level.scheduleTick(pos, this, 60 + random.nextInt(61));
  }

  private void updateOpenState(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    if (!CelestialTime.hasDayNightCycle(level)) return;
    boolean open = CelestialTime.isNight(level);
    if (state.getValue(OPEN) == open || !level.setBlock(pos, state.setValue(OPEN, open), 3)) return;
    level.sendParticles(ModParticleType.SPARKLE.get(),
        pos.getX() + 0.5, pos.getY() + 0.65, pos.getZ() + 0.5,
        open ? 3 : 1, 0.12, 0.12, 0.12, 0.005);
    level.playSound(null, pos,
        open ? SoundEvents.AMETHYST_BLOCK_CHIME : SoundEvents.CANDLE_EXTINGUISH,
        SoundSource.BLOCKS, 0.2F, open ? 1.3F : 0.85F);
    int scheduled = 0;
    for (int attempt = 0; attempt < 12 && scheduled < 3; attempt++) {
      BlockPos nearby = pos.offset(random.nextInt(7) - 3, random.nextInt(3) - 1,
          random.nextInt(7) - 3);
      if (nearby.equals(pos) || !level.hasChunkAt(nearby)) continue;
      BlockState nearbyState = level.getBlockState(nearby);
      if (nearbyState.getBlock() instanceof CelestialBloomBlock
          && nearbyState.getValue(OPEN) != open) {
        level.scheduleTick(nearby, nearbyState.getBlock(), 8 + random.nextInt(24));
        scheduled++;
      }
    }
  }

  @Override
  public boolean isValidBonemealTarget(
      LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
    return state.is(ModBlocks.CELESTIAL_BLOOM.get())
        && super.isValidBonemealTarget(level, pos, state, isClient);
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    if (!Configuration.CELESTIAL_BLOOM_EMITS_PARTICLES.get() || !state.getValue(OPEN)
        || !CelestialTime.isNight(level)) return;

    boolean withered = state.is(ModBlocks.WITHERED_CELESTIAL_BLOOM.get());

    int spawnChance = withered ? 10 : 5;

    if (random.nextInt(spawnChance) != 0) return;

    double cx = pos.getX() + 0.5D;
    double cz = pos.getZ() + 0.5D;
    double x = cx + (random.nextDouble() - 0.5D) * 0.35D;
    double y = pos.getY() + 0.45D + random.nextDouble() * 0.45D;
    double z = cz + (random.nextDouble() - 0.5D) * 0.35D;
    double velocityScale = withered ? 0.6D : 1.0D;
    double vx = (random.nextDouble() - 0.5D) * 0.003D;
    double vy = 0.010D + random.nextDouble() * 0.010D;
    double vz = (random.nextDouble() - 0.5D) * 0.003D;

    level.addParticle(ModParticleType.SPARKLE.get(), x, y, z,
        vx * velocityScale, vy * velocityScale, vz * velocityScale);


  }
}
