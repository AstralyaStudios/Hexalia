package net.astralya.hexalia.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class GhostParticle extends TextureSheetParticle {

    private final SpriteSet spriteSet;
    private final double orbitOffset;
    private final float baseSize;
    private final double centerX;
    private final double centerZ;
    private final double orbitRadius;
    private final double upwardDrift;

    protected GhostParticle(ClientLevel level, double x, double y, double z,
                            double velocityX, double velocityY, double velocityZ,
                            SpriteSet spriteSet) {
        super(level, x, y, z, velocityX, velocityY, velocityZ);
        this.spriteSet = spriteSet;
        this.orbitOffset = this.random.nextDouble() * Math.PI * 2.0D;
        this.centerX = x;
        this.centerZ = z;
        this.orbitRadius = 0.025D + this.random.nextDouble() * 0.03D;
        this.upwardDrift = 0.003D + this.random.nextDouble() * 0.002D;
        this.xd = 0.0D;
        this.yd = this.upwardDrift;
        this.zd = 0.0D;
        this.baseSize = 0.05F + this.random.nextFloat() * 0.03F;
        this.quadSize = this.baseSize;
        this.lifetime = 50 + this.random.nextInt(20);
        this.gravity = 0.0F;
        this.friction = 0.92F;
        this.hasPhysics = false;
        this.alpha = 0.0F;
        this.setSpriteFromAge(spriteSet);
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

        this.setSpriteFromAge(this.spriteSet);

        float lifeT = (float) this.age / (float) this.lifetime;
        this.alpha = lifeT < 0.2F
                ? Mth.clamp(lifeT / 0.2F, 0.0F, 1.0F) * 0.35F
                : lifeT > 0.75F
                  ? Mth.clamp((1.0F - lifeT) / 0.25F, 0.0F, 1.0F) * 0.35F
                  : 0.35F;

        double angle = this.orbitOffset + this.age * 0.08D;
        double targetX = this.centerX + Math.cos(angle) * this.orbitRadius;
        double targetZ = this.centerZ + Math.sin(angle) * this.orbitRadius;

        this.xd = (targetX - this.x) * 0.08D;
        this.zd = (targetZ - this.z) * 0.08D;
        this.yd = this.upwardDrift;

        this.move(this.xd, this.yd, this.zd);
    }

    @Override
    public float getQuadSize(float partialTick) {
        float lifeT = ((float) this.age + partialTick) / (float) this.lifetime;
        float scale = lifeT < 0.5F
                ? 1.0F + lifeT * 0.2F
                : 1.1F - (lifeT - 0.5F) * 0.2F;
        return this.baseSize * scale;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double velocityX, double velocityY, double velocityZ) {
            return new GhostParticle(level, x, y, z, velocityX, velocityY, velocityZ, this.spriteSet);
        }
    }
}
