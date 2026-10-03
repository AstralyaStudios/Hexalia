package net.astralya.hexalia.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

/** A small, freely wandering light, following the newer vanilla Firefly's lifetime and fades. */
public class FireflyParticle extends TextureSheetParticle {
  private double accelerationX;
  private double accelerationY;
  private double accelerationZ;
  private int turnTicks;
  private FireflyParticle(ClientLevel level, double x, double y, double z,
      double vx, double vy, double vz, SpriteSet sprites) {
    super(level, x, y, z);
    this.xd = vx;
    this.yd = vy;
    this.zd = vz;
    this.quadSize *= 0.35F;
    this.lifetime = 100 + this.random.nextInt(81);
    this.gravity = 0.0F;
    this.friction = 0.9F;
    this.hasPhysics = false;
    this.alpha = 0.0F;
    this.pickSprite(sprites);
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  @Override
  public int getLightColor(float partialTick) {
    return 0xF000F0;
  }

  @Override
  public void tick() {
    this.xo = this.x;
    this.yo = this.y;
    this.zo = this.z;
    if (this.age++ >= this.lifetime) {
      this.remove();
      return;
    }
    float progress = (float) this.age / this.lifetime;
    this.alpha = Math.min(Mth.clamp(progress / 0.3F, 0.0F, 1.0F),
        Mth.clamp((1.0F - progress) / 0.5F, 0.0F, 1.0F));
    if (--this.turnTicks <= 0) {
      this.accelerationX = (this.random.nextDouble() - 0.5) * 0.003;
      this.accelerationY = (this.random.nextDouble() - 0.5) * 0.002;
      this.accelerationZ = (this.random.nextDouble() - 0.5) * 0.003;
      this.turnTicks = 8 + this.random.nextInt(13);
    }
    this.xd = Mth.clamp(this.xd * this.friction + this.accelerationX, -0.02, 0.02);
    this.yd = Mth.clamp(this.yd * this.friction + this.accelerationY, -0.014, 0.014);
    this.zd = Mth.clamp(this.zd * this.friction + this.accelerationZ, -0.02, 0.02);
    this.move(this.xd, this.yd, this.zd);
  }

  public static class Factory implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet sprites;

    public Factory(SpriteSet sprites) {
      this.sprites = sprites;
    }

    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level,
        double x, double y, double z, double vx, double vy, double vz) {
      return new FireflyParticle(level, x, y, z, vx, vy, vz, sprites);
    }
  }
}
