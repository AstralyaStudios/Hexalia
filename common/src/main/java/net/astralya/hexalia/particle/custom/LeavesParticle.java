package net.astralya.hexalia.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class LeavesParticle extends SimpleAnimatedParticle {
  private final float tumbleSpeed;
  private final double swayOffset;

  protected LeavesParticle(
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
    friction = 0.93F;
    gravity = 0.0F;
    tumbleSpeed = (random.nextBoolean() ? 1.0F : -1.0F)
        * (0.035F + random.nextFloat() * 0.025F);
    swayOffset = random.nextDouble() * Math.PI * 2.0;
    roll = random.nextFloat() * Mth.TWO_PI;
    oRoll = roll;

    if (velocityX == 0.0D && velocityY == 0.0D && velocityZ == 0.0D) {
      xd = (random.nextDouble() - 0.5D) * 0.02D;
      yd = random.nextDouble() * 0.02D;
      zd = (random.nextDouble() - 0.5D) * 0.02D;
    } else {
      xd = velocityX;
      yd = velocityY;
      zd = velocityZ;
    }

    quadSize *= 0.2F + random.nextFloat() * 0.4F;
    lifetime = 14 + random.nextInt(6);
    setColor(15916745);
    setSpriteFromAge(sprites);
  }

  @Override
  public void tick() {
    oRoll = roll;
    super.tick();
    if (!isAlive()) return;
    roll += tumbleSpeed;
    xd += Math.sin(age * 0.2 + swayOffset) * 0.0007;
    zd += Math.cos(age * 0.2 + swayOffset) * 0.0007;
    alpha = Mth.clamp((1.0F - (float) age / lifetime) / 0.35F, 0.0F, 1.0F);
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
      return new LeavesParticle(level, x, y, z, velocityX, velocityY, velocityZ, spriteSet);
    }
  }
}
