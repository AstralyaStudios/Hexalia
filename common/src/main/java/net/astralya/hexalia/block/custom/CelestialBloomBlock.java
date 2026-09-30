package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.HexaliaConfig;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.particle.ModParticleTypes;
import net.astralya.hexalia.util.CelestialTime;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

public class CelestialBloomBlock extends HerbBlock {
  public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

  public CelestialBloomBlock(Holder<MobEffect> effect, float seconds, Properties properties) {
    super(effect, seconds, properties);
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
  protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    updateOpenState(state, level, pos, random);
  }

  @Override
  protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    updateOpenState(state, level, pos, random);
  }

  private void updateOpenState(
      BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    if (!CelestialTime.hasDayNightCycle(level)) return;
    boolean open = CelestialTime.isNight(level);
    if (state.getValue(OPEN) == open) return;
    if (!level.setBlock(pos, state.setValue(OPEN, open), 3)) return;
    level.sendParticles(
        ModParticleTypes.SPARKLE.get(),
        pos.getX() + 0.5, pos.getY() + 0.65, pos.getZ() + 0.5,
        open ? 3 : 1, 0.12, 0.12, 0.12, 0.005);
    level.playSound(
        null, pos, open ? SoundEvents.AMETHYST_BLOCK_CHIME : SoundEvents.CANDLE_EXTINGUISH,
        SoundSource.BLOCKS, 0.2F, open ? 1.3F : 0.85F);
    int scheduled = 0;
    for (int attempt = 0; attempt < 12 && scheduled < 3; attempt++) {
      BlockPos nearby = pos.offset(
          random.nextInt(7) - 3, random.nextInt(3) - 1, random.nextInt(7) - 3);
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
  public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
    return state.is(ModBlocks.CELESTIAL_BLOOM.get())
        && super.isValidBonemealTarget(level, pos, state);
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    if (!HexaliaConfig.celestialBloomEmitsParticles() || !state.getValue(OPEN)) return;
    boolean withered = state.is(ModBlocks.WITHERED_CELESTIAL_BLOOM.get());
    if (random.nextInt(withered ? 10 : 5) != 0) {
      return;
    }
    double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.35;
    double y = pos.getY() + 0.45 + random.nextDouble() * 0.45;
    double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.35;
    double velocityScale = withered ? 0.6 : 1.0;
    level.addParticle(
        ModParticleTypes.SPARKLE.get(),
        x,
        y,
        z,
        (random.nextDouble() - 0.5) * 0.003 * velocityScale,
        (0.01 + random.nextDouble() * 0.01) * velocityScale,
        (random.nextDouble() - 0.5) * 0.003 * velocityScale);
  }
}
