package net.astralya.hexalia.block.entity.custom;

import net.astralya.hexalia.Configuration;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.particle.ModParticleType;
import net.astralya.hexalia.util.ModUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class LourdesBlockEntity extends BlockEntity {
    private static final int DURATION_TICKS = Configuration.LOURDES_DURATION.get();
    private static final int PULSE_INTERVAL_TICKS = 20;
    private static final double RADIUS = Configuration.LOURDES_EFFECT_RADIUS.get();

    private long activeUntilGameTime;

    public LourdesBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.LOURDES, pos, state);
    }

    public boolean isActive() {
        return world != null && activeUntilGameTime > world.getTime();
    }

    public void activate(long gameTime) {
        activeUntilGameTime = gameTime + DURATION_TICKS;
        markDirty();
        sync();
    }

    public static void tick(World world, BlockPos pos, BlockState state, LourdesBlockEntity blockEntity) {
        if (world.isClient) {
            return;
        }

        long gameTime = world.getTime();

        if (blockEntity.activeUntilGameTime != 0L && gameTime >= blockEntity.activeUntilGameTime) {
            blockEntity.activeUntilGameTime = 0L;
            blockEntity.markDirty();
            blockEntity.sync();
            return;
        }

        if (blockEntity.activeUntilGameTime == 0L || gameTime % PULSE_INTERVAL_TICKS != 0L) {
            return;
        }

        Box box = new Box(pos).expand(RADIUS);
        List<AnimalEntity> targets = world.getEntitiesByClass(AnimalEntity.class, box, LivingEntity::isAlive);
        for (AnimalEntity target : targets) {
            cleanseEffects(target);
            applyHealingAura(target);
        }
    }

    private static void cleanseEffects(LivingEntity entity) {
        ModUtil.removeHarmfulEffects(entity);
    }

    private static void applyHealingAura(LivingEntity entity) {
        StatusEffectInstance existing = entity.getStatusEffect(StatusEffects.REGENERATION);
        if (existing == null || existing.getDuration() < 30) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 40, 0, true, true, true));
        }
    }

    public void spawnActiveParticles(World world, BlockPos pos, Random random) {
        double centerX = pos.getX() + 0.5D;
        double centerY = pos.getY() + 0.35D;
        double centerZ = pos.getZ() + 0.5D;
        double red = 0.95D;
        double green = 0.45D;
        double blue = 0.75D;

        for (int i = 0; i < 4; i++) {
            double angle = random.nextDouble() * Math.PI * 2.0D;
            double distance = 0.8D + random.nextDouble() * 2.2D;
            double x = centerX + Math.cos(angle) * distance;
            double z = centerZ + Math.sin(angle) * distance;
            double y = centerY + random.nextDouble() * 0.6D;

            world.addParticle(ParticleTypes.ENTITY_EFFECT, x, y, z, red, green, blue);
            world.addParticle(ModParticleType.SPARKLE, x, y, z, 0.0D, 0.01D, 0.0D);
        }
    }

    private void sync() {
        if (world != null && !world.isClient) {
            world.updateListeners(pos, getCachedState(), getCachedState(), 3);
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putLong("ActiveUntil", activeUntilGameTime);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        activeUntilGameTime = nbt.getLong("ActiveUntil");
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
}
