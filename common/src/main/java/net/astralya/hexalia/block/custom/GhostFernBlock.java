package net.astralya.hexalia.block.custom;

import java.util.function.Supplier;
import net.astralya.hexalia.Configuration;
import net.astralya.hexalia.particle.custom.ColoredSporeParticleOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

public class GhostFernBlock extends HerbBlock {

  public GhostFernBlock(
      Supplier<MobEffect> effectSupplier, int pEffectDuration, Properties pProperties) {
    super(effectSupplier, pEffectDuration, pProperties);
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    if (!level.isClientSide() || !Configuration.GHOST_FERN_EMITS_PARTICLES.get()
        || random.nextFloat() > 0.25F) return;

    double x = pos.getX() + 0.1D + random.nextDouble() * 0.8D;
    double y = pos.getY() + random.nextDouble() * 0.7D;
    double z = pos.getZ() + 0.1D + random.nextDouble() * 0.8D;

    level.addParticle(
        new ColoredSporeParticleOptions(new Vector3f(0.72F, 0.82F, 0.76F)),
        x, y, z, 0.0D, 0.0D, 0.0D);
  }
}
