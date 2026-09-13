package net.astralya.hexalia.block.entity.custom;

import net.astralya.hexalia.Configuration;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.gameplay.enchantedplant.WindsongDeflection;
import net.astralya.hexalia.sound.ModSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WindsongBlockEntity extends BlockEntity {
    private int activeTicks = 0;
    private long activationTime = -1;
    private int duration = 0;
    private int particleCooldown = 0;

    public WindsongBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.WINDSONG, pos, state);
    }

    private static int cfgDuration() {
        return Math.max(1, Configuration.WINDSONG_DURATION.get());
    }

    private static int cfgRadius() {
        return Math.max(1, Configuration.WINDSONG_EFFECT_RADIUS.get());
    }

    public static void tick(World world, BlockPos pos, BlockState state, WindsongBlockEntity blockEntity) {
        blockEntity.tick(world, pos, state);
    }

    public void activate() {
        activate(cfgDuration());
    }

    public void activate(int customDuration) {
        this.duration = Math.max(1, customDuration);
        this.activeTicks = this.duration;
        this.activationTime = this.world != null ? this.world.getTime() : -1;
        markDirty();
    }

    public boolean isActive() {
        return this.activeTicks > 0;
    }

    public int getDuration() {
        return duration;
    }

    public float getProgress() {
        return duration <= 0 ? 0.0f : Math.min(1.0f, (float) (duration - activeTicks) / duration);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (this.activationTime != -1 && this.activeTicks > 0 && this.duration > 0) {
            long elapsed = world.getTime() - this.activationTime;
            int expected = this.duration - (int) elapsed;
            if (Math.abs(this.activeTicks - expected) > 5) {
                this.activeTicks = Math.max(0, expected);
            }
        }

        if (!this.isActive()) {
            return;
        }

        this.activeTicks--;

        if (world instanceof ServerWorld serverWorld) {
            int radius = cfgRadius();
            Box area = new Box(pos).expand(radius);
            List<Entity> projectiles = serverWorld.getEntitiesByClass(Entity.class, area, entity -> entity instanceof ProjectileEntity);

            for (Entity projectile : projectiles) {
                if (!projectile.isRemoved()) {
                    deflectProjectile(serverWorld, pos, (ProjectileEntity) projectile);
                }
            }

            emitParticles(serverWorld, pos, radius);
        }

        if (this.activeTicks <= 0) {
            world.playSound(null, pos, ModSoundEvents.WIND_BURST, SoundCategory.BLOCKS, 1.0f, 1.0f);
            world.breakBlock(pos, false);
        } else {
            markDirty();
        }
    }

    private void deflectProjectile(ServerWorld world, BlockPos sourcePos, ProjectileEntity projectile) {
        Vec3d source = Vec3d.ofCenter(sourcePos);
        Vec3d outward = projectile.getPos().subtract(source);
        outward = new Vec3d(outward.x, 0.0, outward.z);
        if (!WindsongDeflection.deflect(projectile, outward)) return;
        world.playSound(null, projectile.getX(), projectile.getY(), projectile.getZ(), ModSoundEvents.WIND_BURST, SoundCategory.BLOCKS, 1.0f, 1.0f);

        Vec3d projectilePos = projectile.getPos();
        for (int i = 0; i < 5; i++) {
            double angle = Math.random() * 2.0 * Math.PI;
            double radius = Math.random() * 0.5;
            double x = projectilePos.x + radius * Math.cos(angle);
            double z = projectilePos.z + radius * Math.sin(angle);
            double y = projectilePos.y + Math.random() * 0.5;
            world.spawnParticles(ParticleTypes.EFFECT, x, y, z, 1, 0.0, 0.0, 0.0, 0.1);
        }

    }

    private void emitParticles(ServerWorld world, BlockPos pos, int radius) {
        if (particleCooldown > 0) {
            particleCooldown--;
            return;
        }

        Vec3d center = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        float progress = getProgress();
        int particleCount = Math.max(1, (int) (3 * (1.0f - progress * 0.5f)));

        for (int i = 0; i < particleCount; i++) {
            double angle = Math.random() * 2.0 * Math.PI;
            double distance = Math.random() * radius;
            double x = center.x + distance * Math.cos(angle);
            double z = center.z + distance * Math.sin(angle);
            double y = center.y + Math.random() * 2.0;
            world.spawnParticles(ParticleTypes.CLOUD, x, y, z, 1, 0.0, 0.0, 0.0, 0.1);
        }

        particleCooldown = 5;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.activeTicks = nbt.getInt("ActiveTicks");
        this.activationTime = nbt.getLong("ActivationTime");
        this.duration = nbt.contains("Duration") ? nbt.getInt("Duration") : cfgDuration();
        this.particleCooldown = nbt.getInt("ParticleCooldown");

        if (this.activationTime != -1 && this.world != null && this.activeTicks > 0 && this.duration > 0) {
            long elapsed = this.world.getTime() - this.activationTime;
            this.activeTicks = Math.max(0, this.duration - (int) elapsed);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("ActiveTicks", activeTicks);
        nbt.putLong("ActivationTime", activationTime);
        nbt.putInt("Duration", duration);
        nbt.putInt("ParticleCooldown", particleCooldown);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = createNbt();
        nbt.putInt("ActiveTicks", activeTicks);
        nbt.putLong("ActivationTime", activationTime);
        nbt.putInt("Duration", duration);
        nbt.putInt("ParticleCooldown", particleCooldown);
        return nbt;
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
}
