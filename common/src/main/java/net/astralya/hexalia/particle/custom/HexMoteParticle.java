package net.astralya.hexalia.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class HexMoteParticle extends TextureSheetParticle {
  private final SpriteSet sprites;
  private final int startingColor;
  private final int colorDirection;
  private final int colorInterval;
  private final double swayOffset;

  public HexMoteParticle(
      ClientLevel level,
      double x,
      double y,
      double z,
      double velocityX,
      double velocityY,
      double velocityZ,
      SpriteSet spriteSet) {
    super(level, x, y, z, velocityX, velocityY, velocityZ);
    this.sprites = spriteSet;
    this.startingColor = this.random.nextInt(4);
    this.colorDirection = this.random.nextBoolean() ? 1 : -1;
    this.colorInterval = 6 + this.random.nextInt(6);
    this.swayOffset = this.random.nextDouble() * Math.PI * 2.0;
    this.xd = velocityX + (this.random.nextDouble() - 0.5) * 0.01;
    this.yd = velocityY + (this.random.nextDouble() - 0.5) * 0.01;
    this.zd = velocityZ + (this.random.nextDouble() - 0.5) * 0.01;
    this.quadSize *= 0.07F + this.random.nextFloat() * 0.04F;
    this.lifetime = 30 + this.random.nextInt(21);
    this.gravity = 0.0F;
    this.friction = 0.98F;
    this.hasPhysics = false;
    this.alpha = 0.0F;
    this.setSprite(this.sprites.get(this.startingColor, 3));
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
    int color = Math.floorMod(this.startingColor + this.colorDirection * (this.age / this.colorInterval), 4);
    this.setSprite(this.sprites.get(color, 3));
    float life = (float) this.age / this.lifetime;
    this.alpha = life < 0.12F ? life / 0.12F : life > 0.75F ? Mth.clamp((1.0F - life) / 0.25F, 0.0F, 1.0F) : 1.0F;
    this.xd += Math.sin((this.age + this.swayOffset) * 0.12) * 0.0008;
    this.zd += Math.cos((this.age + this.swayOffset) * 0.12) * 0.0008;
    this.move(this.xd, this.yd, this.zd);
    this.yd *= 0.98;
  }

  public static class Factory implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet spriteSet;

    public Factory(SpriteSet spriteSet) {
      this.spriteSet = spriteSet;
    }

    @Override
    public Particle createParticle(
        SimpleParticleType type,
        ClientLevel world,
        double x,
        double y,
        double z,
        double vx,
        double vy,
        double vz) {
      return new HexMoteParticle(world, x, y, z, vx, vy, vz, this.spriteSet);
    }
  }
}
