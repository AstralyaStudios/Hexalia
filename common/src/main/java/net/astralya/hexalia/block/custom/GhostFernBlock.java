package net.astralya.hexalia.block.custom;

import java.util.function.Supplier;
import net.astralya.hexalia.Configuration;
import net.astralya.hexalia.particle.ModParticleType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class GhostFernBlock extends HerbBlock {

  public GhostFernBlock(
      Supplier<MobEffect> effectSupplier, int pEffectDuration, Properties pProperties) {
    super(effectSupplier, pEffectDuration, pProperties);
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    if (level.isClientSide() && Configuration.GHOST_FERN_EMITS_PARTICLES.get()) {
      double centerX = pos.getX() + 0.5;
      double centerY = pos.getY() + 0.5;
      double centerZ = pos.getZ() + 0.5;

      double offsetX = (random.nextDouble() - 0.5) * 0.2;
      double offsetZ = (random.nextDouble() - 0.5) * 0.2;

      level.addParticle(
          ModParticleType.GHOST.get(),
          centerX + offsetX,
          centerY,
          centerZ + offsetZ,
          (random.nextDouble() - 0.5) * 0.02,
          0.08,
          (random.nextDouble() - 0.5) * 0.02);
    }
  }
}
