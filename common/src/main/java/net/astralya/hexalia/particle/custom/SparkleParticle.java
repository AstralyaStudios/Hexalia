package net.astralya.hexalia.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class SparkleParticle extends SimpleAnimatedParticle {
  private final float baseSize;
  private final float driftPhase;
  private final float driftSpeed;
  private final float driftStrength;

  protected SparkleParticle(
      ClientLevel level,
      double x,
      double y,
      double z,
      double velocityX,
      double velocityY,
      double velocityZ,
      SpriteSet sprites) {
    super(level, x, y, z, sprites, 0.01F);
    hasPhysics = false;
    friction = 0.94F;
    gravity = 0.0F;

    if (velocityX == 0.0D && velocityY == 0.0D && velocityZ == 0.0D) {
      xd = (random.nextDouble() - 0.5D) * 0.004D;
      yd = 0.008D + random.nextDouble() * 0.010D;
      zd = (random.nextDouble() - 0.5D) * 0.004D;
    } else {
      xd = velocityX;
      yd = velocityY;
      zd = velocityZ;
    }

    baseSize = 0.08F + random.nextFloat() * 0.08F;
    quadSize = baseSize;
    lifetime = 18 + random.nextInt(14);
    driftPhase = random.nextFloat() * Mth.TWO_PI;
    driftSpeed = 0.12F + random.nextFloat() * 0.08F;
    driftStrength = 0.0002F + random.nextFloat() * 0.0002F;
    setColor(15916745);
    alpha = 0.0F;
    setSpriteFromAge(sprites);
  }

  @Override
  public void tick() {
    xd += Mth.sin(age * driftSpeed + driftPhase) * driftStrength;
    zd += Mth.cos(age * driftSpeed + driftPhase) * driftStrength;
    yd = Math.min(yd + 0.00015D, 0.014D);
    super.tick();
    if (removed) {
      return;
    }

    float ageProgress = (float) age / (float) lifetime;
    float fadeIn = Mth.clamp(ageProgress / 0.15F, 0.0F, 1.0F);
    float fadeOut = Mth.clamp((1.0F - ageProgress) / 0.45F, 0.0F, 1.0F);
    alpha = Math.min(fadeIn, fadeOut) * 0.8F;
    quadSize = baseSize * (0.96F + 0.06F * Mth.sin(age * 0.24F + driftPhase))
        * (1.0F - ageProgress * 0.15F);
  }

  @Override
  public void move(double x, double y, double z) {
    setBoundingBox(getBoundingBox().move(x, y, z));
    setLocationFromBoundingbox();
  }

  public static class Factory implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet spriteSet;

    public Factory(SpriteSet spriteSet) {
      this.spriteSet = spriteSet;
    }

    @Override
    public Particle createParticle(
        SimpleParticleType type,
        ClientLevel level,
        double x,
        double y,
        double z,
        double velocityX,
        double velocityY,
        double velocityZ) {
      return new SparkleParticle(level, x, y, z, velocityX, velocityY, velocityZ, spriteSet);
    }
  }
}
