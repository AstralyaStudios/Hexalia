package net.astralya.hexalia.particle.custom;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class RitualGlyphParticle extends TextureSheetParticle {
  private static final float CORE_SCALE = 1.15F;
  private static final float HALO_SCALE = 1.15F;
  private static final float HALO_ALPHA = 0.16F;
  private static final double HALO_OFFSET = 0.002;
  private final float startingSize;
  private final float finalSize;
  private final float baseAlpha;
  private final float rollSpeed;

  protected RitualGlyphParticle(
      ClientLevel level, double x, double y, double z, double vx, double vy, double vz, SpriteSet sprites) {
    super(level, x, y, z, 0.0, 0.0, 0.0);
    boolean completion = vy > 0.0;
    boolean ambient = vy < 0.0;
    this.startingSize = (completion ? 0.21F : 0.18F) * CORE_SCALE;
    this.finalSize = (completion ? 0.28F : 0.24F) * CORE_SCALE;
    this.baseAlpha = ambient ? 0.32F : completion ? 0.82F : 0.68F;
    this.quadSize = this.startingSize;
    this.roll = this.random.nextFloat() * Mth.TWO_PI;
    this.oRoll = this.roll;
    this.rollSpeed = (this.random.nextBoolean() ? 1.0F : -1.0F)
        * (0.045F + this.random.nextFloat() * 0.01F);
    this.lifetime = completion ? 42 + this.random.nextInt(5) : 54 + this.random.nextInt(5);
    this.gravity = 0.0F;
    this.hasPhysics = false;
    this.alpha = this.baseAlpha;
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
  public void render(VertexConsumer buffer, Camera camera, float partialTick) {
    float coreSize = this.quadSize;
    float coreAlpha = this.alpha;
    double coreX = this.x;
    double coreY = this.y;
    double coreZ = this.z;
    double previousX = this.xo;
    double previousY = this.yo;
    double previousZ = this.zo;
    double dx = coreX - camera.getPosition().x;
    double dy = coreY - camera.getPosition().y;
    double dz = coreZ - camera.getPosition().z;
    double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

    this.quadSize = coreSize * HALO_SCALE;
    this.alpha = coreAlpha * HALO_ALPHA;
    if (distance > 0.0) {
      double offsetX = dx / distance * HALO_OFFSET;
      double offsetY = dy / distance * HALO_OFFSET;
      double offsetZ = dz / distance * HALO_OFFSET;
      this.x += offsetX;
      this.y += offsetY;
      this.z += offsetZ;
      this.xo += offsetX;
      this.yo += offsetY;
      this.zo += offsetZ;
    }
    super.render(buffer, camera, partialTick);

    this.x = coreX;
    this.y = coreY;
    this.z = coreZ;
    this.xo = previousX;
    this.yo = previousY;
    this.zo = previousZ;
    this.quadSize = coreSize;
    this.alpha = coreAlpha;
    super.render(buffer, camera, partialTick);
  }

  @Override
  public void tick() {
    this.xo = this.x;
    this.yo = this.y;
    this.zo = this.z;
    this.oRoll = this.roll;
    if (this.age++ >= this.lifetime) {
      this.remove();
      return;
    }
    float progress = (float) this.age / this.lifetime;
    this.quadSize = this.startingSize + (this.finalSize - this.startingSize) * progress;
    this.roll += this.rollSpeed;
    float pulse = 0.9F + 0.1F * Mth.sin(this.age * 0.22F);
    this.alpha = this.baseAlpha * pulse
        * Mth.clamp((1.0F - progress) / 0.3F, 0.0F, 1.0F);
  }

  public static class Factory implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet sprites;

    public Factory(SpriteSet sprites) {
      this.sprites = sprites;
    }

    @Override
    public Particle createParticle(
        SimpleParticleType type, ClientLevel level, double x, double y, double z,
        double vx, double vy, double vz) {
      return new RitualGlyphParticle(level, x, y, z, vx, vy, vz, sprites);
    }
  }
}
