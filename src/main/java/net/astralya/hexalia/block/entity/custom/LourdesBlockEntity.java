package net.astralya.hexalia.block.entity.custom;

import net.astralya.hexalia.Configuration;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.particle.ModParticleType;
import net.astralya.hexalia.util.ModUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class LourdesBlockEntity extends BlockEntity {

    private static final int DURATION_TICKS = Configuration.LOURDES_DURATION.get();
    private static final int PULSE_INTERVAL_TICKS = 20;
    private static final double RADIUS = Configuration.LOURDES_EFFECT_RADIUS.get();

    private long activeUntilGameTime;

    public LourdesBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.LOURDES.get(), pos, state);
    }

    public boolean isActive() {
        if (this.level == null) {
            return false;
        }
        return this.activeUntilGameTime > this.level.getGameTime();
    }

    public void activate(long gameTime) {
        this.activeUntilGameTime = gameTime + DURATION_TICKS;
        this.setChanged();

        if (this.level != null) {
            BlockState state = this.getBlockState();
            this.level.sendBlockUpdated(this.worldPosition, state, state, 3);
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, LourdesBlockEntity blockEntity) {
        if (level.isClientSide()) {
            return;
        }

        long gameTime = level.getGameTime();

        if (blockEntity.activeUntilGameTime != 0L && gameTime >= blockEntity.activeUntilGameTime) {
            blockEntity.activeUntilGameTime = 0L;
            blockEntity.setChanged();
            level.sendBlockUpdated(pos, state, state, 3);
            return;
        }

        if (blockEntity.activeUntilGameTime == 0L) {
            return;
        }

        if ((gameTime % PULSE_INTERVAL_TICKS) != 0L) {
            return;
        }

        AABB aabb = new AABB(pos).inflate(RADIUS);
        List<Animal> targets = level.getEntitiesOfClass(Animal.class, aabb, LivingEntity::isAlive);
        for (Animal target : targets) {
            cleanseEffects(target);
            applyHealingAura(target);
        }
    }

    private static void cleanseEffects(LivingEntity entity) {
        ModUtil.removeHarmfulEffects(entity);
    }

    private static void applyHealingAura(LivingEntity entity) {
        MobEffectInstance existing = entity.getEffect(MobEffects.REGENERATION);
        if (existing == null || existing.getDuration() < 30) {
            entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 40, 0, true, true, true));
        }
    }

    public void spawnActiveParticles(Level level, BlockPos pos, RandomSource random) {
        double cx = pos.getX() + 0.5D;
        double cy = pos.getY() + 0.35D;
        double cz = pos.getZ() + 0.5D;

        float r = 0.95F;
        float g = 0.45F;
        float b = 0.75F;

        for (int i = 0; i < 4; i++) {
            double angle = random.nextDouble() * Math.PI * 2.0D;
            double dist = 0.8D + random.nextDouble() * 2.2D;

            double x = cx + Math.cos(angle) * dist;
            double z = cz + Math.sin(angle) * dist;
            double y = cy + random.nextDouble() * 0.6D;

            double vx = (random.nextDouble() - 0.5D) * 0.01D;
            double vy = 0.01D + random.nextDouble() * 0.02D;
            double vz = (random.nextDouble() - 0.5D) * 0.01D;

            level.addParticle(ParticleTypes.ENTITY_EFFECT, x, y, z, r, g, b);
            level.addParticle(ModParticleType.SPARKLE.get(), x, y, z, 0.0D, 0.01D, 0.0D);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putLong("ActiveUntil", this.activeUntilGameTime);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.activeUntilGameTime = tag.getLong("ActiveUntil");
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.putLong("ActiveUntil", this.activeUntilGameTime);
        return tag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
