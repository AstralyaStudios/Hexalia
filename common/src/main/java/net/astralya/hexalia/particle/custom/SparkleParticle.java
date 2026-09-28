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
  private final float shimmerSpeed;
  private final float shimmerStrength;
  private final float yBoost;
  private final double startX;
  private final double startZ;

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

    this.hasPhysics = false;
    this.friction = 0.96F;
    this.gravity = 0.0F;

    this.startX = x;
    this.startZ = z;

    if (velocityX == 0.0D && velocityY == 0.0D && velocityZ == 0.0D) {
      this.xd = (random.nextDouble() - 0.5D) * 0.004D;
      this.yd = 0.008D + random.nextDouble() * 0.010D;
      this.zd = (random.nextDouble() - 0.5D) * 0.004D;
    } else {
      this.xd = velocityX;
      this.yd = velocityY;
      this.zd = velocityZ;
    }

    this.baseSize = 0.08F + random.nextFloat() * 0.08F;
    this.quadSize *= this.baseSize;

    this.lifetime = 18 + random.nextInt(14);

    this.shimmerSpeed = 0.18F + random.nextFloat() * 0.25F;
    this.shimmerStrength = 0.010F + random.nextFloat() * 0.010F;
    this.yBoost = 0.002F + random.nextFloat() * 0.004F;

    this.setColor(15916745);
    this.alpha = 0.0F;

    this.setSpriteFromAge(sprites);
  }

  @Override
  public void tick() {
    super.tick();

    if (this.removed) {
      return;
    }

    float t = (float) this.age / (float) this.lifetime;

    float fadeIn = Mth.clamp(t / 0.15F, 0.0F, 1.0F);
    float fadeOut = Mth.clamp((1.0F - t) / 0.35F, 0.0F, 1.0F);
    this.alpha = Math.min(fadeIn, fadeOut);

    float pulse = 0.85F + 0.25F * Mth.sin((this.age + this.random.nextFloat()) * 0.35F);
    this.quadSize = this.baseSize * pulse;

    double wobble =
        Mth.sin((this.age + this.random.nextFloat()) * this.shimmerSpeed) * this.shimmerStrength;
    this.x = this.startX + wobble + (this.x - this.startX) * 0.98D;
    this.z = this.startZ + wobble + (this.z - this.startZ) * 0.98D;

    this.yd += this.yBoost;
  }

  @Override
  public void move(double x, double y, double z) {
    this.setBoundingBox(this.getBoundingBox().move(x, y, z));
    this.setLocationFromBoundingbox();
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
      return new SparkleParticle(level, x, y, z, velocityX, velocityY, velocityZ, this.spriteSet);
    }
  }
}
