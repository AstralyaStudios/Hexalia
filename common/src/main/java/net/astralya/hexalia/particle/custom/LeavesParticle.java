package net.astralya.hexalia.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class LeavesParticle extends SimpleAnimatedParticle {

    protected LeavesParticle(ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteSet spriteSet) {
        super(level, x, y, z, spriteSet, 0.01F);

        this.hasPhysics = false;
        this.friction   = 0.96F;
        this.gravity    = 0.0F;

        if (velocityX == 0 && velocityY == 0 && velocityZ == 0) {
            this.xd = (random.nextDouble() - 0.5D) * 0.02D;
            this.yd =  random.nextDouble()            * 0.02D;
            this.zd = (random.nextDouble() - 0.5D) * 0.02D;
        } else {
            this.xd = velocityX;
            this.yd = velocityY;
            this.zd = velocityZ;
        }

        this.quadSize *= (0.2F + random.nextFloat() * 0.4F);
        this.lifetime  = 14 + random.nextInt(6);

        this.setColor(15916745);

        this.setSpriteFromAge(sprites);
    }

    @Override
    public void move(double dx, double dy, double dz) {
        this.setBoundingBox(this.getBoundingBox().move(dx, dy, dz));
        this.setLocationFromBoundingbox();
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new LeavesParticle(level, x, y, z, velocityX, velocityY, velocityZ, this.spriteSet);
        }
    }
}
