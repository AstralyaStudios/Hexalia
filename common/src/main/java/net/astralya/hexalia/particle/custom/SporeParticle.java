package net.astralya.hexalia.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;

public class SporeParticle extends TextureSheetParticle {

  private final SpriteSet sprites;
  private final double baseX;
  private final double baseY;
  private final double baseZ;
  private final double swayOffset;
  private final double verticalOffset;

  public SporeParticle(
      ClientLevel level,
      double x,
      double y,
      double z,
      double velocityX,
      double velocityY,
      double velocityZ,
      SpriteSet spriteSet,
      float red,
      float green,
      float blue) {
    super(level, x, y, z, velocityX, velocityY, velocityZ);
    this.sprites = spriteSet;
    this.baseX = x;
    this.baseY = y;
    this.baseZ = z;
    this.swayOffset = this.random.nextDouble() * Math.PI * 2.0D;
    this.verticalOffset = this.random.nextDouble() * Math.PI * 2.0D;
    this.quadSize *= 0.12F + this.random.nextFloat() * 0.05F;
    this.lifetime = 80 + this.random.nextInt(40);
    this.gravity = 0.0F;
    this.friction = 1.0F;
    this.hasPhysics = false;
    this.alpha = 0.0F;
    this.setColor(red, green, blue);
    this.pickSprite(spriteSet);
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
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

    this.pickSprite(this.sprites);

    float lifeT = (float) this.age / (float) this.lifetime;
    this.alpha =
        lifeT < 0.15F
            ? lifeT / 0.15F
            : lifeT > 0.75F ? Mth.clamp((1.0F - lifeT) / 0.25F, 0.0F, 1.0F) : 1.0F;

    this.x = this.baseX + Math.sin((this.age * 0.06D) + this.swayOffset) * 0.03D;
    this.y = this.baseY + Math.sin((this.age * 0.04D) + this.verticalOffset) * 0.01D;
    this.z = this.baseZ + Math.cos((this.age * 0.06D) + this.swayOffset) * 0.03D;
  }

  public static class Factory implements ParticleProvider<ColoredSporeParticleOptions> {

    private final SpriteSet spriteSet;

    public Factory(SpriteSet spriteSet) {
      this.spriteSet = spriteSet;
    }

    @Override
    public Particle createParticle(
        ColoredSporeParticleOptions options,
        ClientLevel level,
        double x,
        double y,
        double z,
        double vx,
        double vy,
        double vz) {
      return new SporeParticle(
          level, x, y, z, vx, vy, vz, this.spriteSet,
          options.color().x(), options.color().y(), options.color().z());
    }
  }
}
