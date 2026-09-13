package net.astralya.hexalia.block.entity.custom;

import net.astralya.hexalia.Configuration;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.gameplay.enchantedplant.WindsongDeflection;
import net.astralya.hexalia.sound.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WindsongBlockEntity extends BlockEntity {

    private int activeTicks = 0;
    private long activationTime = -1;
    private int duration = 600;
    private int particleCooldown = 0;

    public WindsongBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntityTypes.WINDSONG.get(), pPos, pBlockState);
    }

    public void activate() {
        activate(Configuration.WINDSONG_DURATION.get());
    }

    public void activate(int customDuration) {
        this.activeTicks = customDuration;
        this.duration = customDuration;
        if (this.level != null) {
            this.activationTime = this.level.getGameTime();
        }
        setChanged();
    }

    public boolean isActive() {
        return this.activeTicks > 0;
    }

    public int getDuration() {
        return duration;
    }

    public float getProgress() {
        if (duration <= 0) {
            return 0.0f;
        }
        return Math.min(1.0f, (float) (duration - activeTicks) / duration);
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (this.isActive()) {
            this.activeTicks--;

            if (pLevel instanceof ServerLevel serverLevel) {
                int radius = Configuration.WINDSONG_EFFECT_RADIUS.get();
                AABB area = new AABB(pPos).inflate(radius);
                List<Entity> projectiles = serverLevel.getEntitiesOfClass(Entity.class, area, entity -> entity instanceof Projectile);

                for (Entity projectile : projectiles) {
                    if (!projectile.isRemoved()) {
                        deflectProjectile(serverLevel, pPos, (Projectile) projectile);
                    }
                }

                emitParticles(serverLevel, pPos);
            }

            if (this.activeTicks <= 0) {
                pLevel.playSound(null, pPos, ModSoundEvents.WIND_BURST.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                pLevel.destroyBlock(pPos, false);
            }
        }
    }

    private void deflectProjectile(ServerLevel pLevel, BlockPos sourcePos, Projectile projectile) {
        Vec3 source = Vec3.atCenterOf(sourcePos);
        Vec3 outward = projectile.position().subtract(source);
        outward = new Vec3(outward.x, 0.0, outward.z);
        if (!WindsongDeflection.deflect(projectile, outward)) return;
        pLevel.playSound(null, projectile.getX(), projectile.getY(), projectile.getZ(),
                ModSoundEvents.WIND_BURST.get(), SoundSource.BLOCKS, 1.0f, 1.0f);

        Vec3 pos = projectile.position();
        for (int i = 0; i < 5; i++) {
            double angle = Math.random() * 2 * Math.PI;
            double radius = Math.random() * 0.5;
            double x = pos.x + radius * Math.cos(angle);
            double z = pos.z + radius * Math.sin(angle);
            double y = pos.y + Math.random() * 0.5;

            pLevel.sendParticles(ParticleTypes.EFFECT, x, y, z, 1, 0, 0, 0, 0.1);
        }

    }

    private void emitParticles(ServerLevel level, BlockPos pos) {
        if (particleCooldown <= 0) {
            Vec3 center = new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);

            float progress = getProgress();
            int particleCount = Math.max(1, (int)(3 * (1.0f - progress * 0.5f)));

            for (int i = 0; i < particleCount; i++) {
                double angle = Math.random() * 2 * Math.PI;
                double radius = Math.random() * Configuration.WINDSONG_EFFECT_RADIUS.get();
                double x = center.x + radius * Math.cos(angle);
                double z = center.z + radius * Math.sin(angle);
                double y = center.y + Math.random() * 2;
                level.sendParticles(ParticleTypes.CLOUD, x, y, z, 1, 0, 0, 0, 0.1);
            }
            particleCooldown = 5;
        } else {
            particleCooldown--;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("ActiveTicks", activeTicks);
        tag.putLong("ActivationTime", activationTime);
        tag.putInt("Duration", duration);
        tag.putInt("ParticleCooldown", particleCooldown);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.activeTicks = tag.getInt("ActiveTicks");
        this.activationTime = tag.getLong("ActivationTime");
        this.duration = tag.contains("Duration") ? tag.getInt("Duration") : Configuration.WINDSONG_DURATION.get();
        this.particleCooldown = tag.getInt("ParticleCooldown");

        if (this.activationTime != -1 && this.level != null && this.activeTicks > 0) {
            long currentTime = this.level.getGameTime();
            long elapsed = currentTime - this.activationTime;
            this.activeTicks = Math.max(0, this.duration - (int)elapsed);
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.putInt("ActiveTicks", activeTicks);
        tag.putLong("ActivationTime", activationTime);
        tag.putInt("Duration", duration);
        tag.putInt("ParticleCooldown", particleCooldown);
        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return super.getUpdatePacket();
    }
}
