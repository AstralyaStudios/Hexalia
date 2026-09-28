package net.astralya.hexalia.block.custom;

import java.util.function.Supplier;
import net.astralya.hexalia.Configuration;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.particle.ModParticleType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

public class CelestialBloomBlock extends HerbBlock {

  private static final double MIN_X = 5.0D;
  private static final double MAX_X = 11.0D;
  private static final double MIN_Z = 5.0D;
  private static final double MAX_Z = 11.0D;
  private static final double HEIGHT = 10.0D;

  public CelestialBloomBlock(
      Supplier<MobEffect> effectSupplier, int effectDuration, Properties properties) {
    super(effectSupplier, effectDuration, properties);
  }

  @Override
  public boolean isValidBonemealTarget(
      LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
    return state.is(ModBlocks.CELESTIAL_BLOOM.get())
        && super.isValidBonemealTarget(level, pos, state, isClient);
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    if (!Configuration.CELESTIAL_BLOOM_EMITS_PARTICLES.get()) return;

    boolean withered = state.is(ModBlocks.WITHERED_CELESTIAL_BLOOM.get());

    int spawnChance = withered ? 6 : 3;

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

    level.addParticle(ModParticleType.SPARKLE.get(), x, y, z, vx, vy, vz);

    int clusterChance = withered ? 14 : 8;

    if (random.nextInt(clusterChance) == 0) {
      int clusterCount = withered ? 1 : 3;
      for (int i = 0; i < clusterCount; i++) {
        double x2 = cx + (random.nextDouble() - 0.5D) * 0.25D;
        double y2 = pos.getY() + 0.60D + random.nextDouble() * 0.35D;
        double z2 = cz + (random.nextDouble() - 0.5D) * 0.25D;
        double vx2 = (random.nextDouble() - 0.5D) * 0.004D * velocityScale;
        double vy2 = (0.014D + random.nextDouble() * 0.012D) * velocityScale;
        double vz2 = (random.nextDouble() - 0.5D) * 0.004D * velocityScale;
        level.addParticle(ModParticleType.SPARKLE.get(), x2, y2, z2, vx2, vy2, vz2);
      }
    }
  }
}
