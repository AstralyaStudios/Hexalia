package net.astralya.hexalia.gameplay.censer.effects;

import net.astralya.hexalia.Configuration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class HollowAuraEffect implements ICenserEffect {

    private static final int STRIP_INTERVAL = 16;
    private static final double HOLLOW_R = 0.15D;
    private static final double HOLLOW_G = 0.0D;
    private static final double HOLLOW_B = 0.2D;

    private int tickCounter = 0;

    @Override
    public String getMessageKey() {
        return "message.hexalia.censer.hollow_aura";
    }

    @Override
    public void onTick(ServerLevel level, BlockPos pos) {
        spawnAmbientParticles(level, pos);

        this.tickCounter++;
        if (this.tickCounter < STRIP_INTERVAL) {
            return;
        }
        this.tickCounter = 0;

        int radius = Configuration.CENSER_EFFECT_RADIUS.get();
        AABB area = new AABB(pos).inflate(radius);

        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, area)) {
            stripBeneficialEffects(level, entity);
        }
    }

    private static void stripBeneficialEffects(ServerLevel level, LivingEntity entity) {
        List<MobEffect> toRemove = new ArrayList<>();

        for (MobEffectInstance instance : entity.getActiveEffects()) {
            MobEffectCategory category = instance.getEffect().getCategory();
            if (category == MobEffectCategory.BENEFICIAL || category == MobEffectCategory.NEUTRAL) {
                toRemove.add(instance.getEffect());
            }
        }

        if (toRemove.isEmpty()) {
            return;
        }

        for (MobEffect effect : toRemove) {
            entity.removeEffect(effect);
        }

        spawnStripParticles(level, entity);
    }

    private static void spawnAmbientParticles(ServerLevel level, BlockPos pos) {
        int radius = Configuration.CENSER_EFFECT_RADIUS.get();

        for (int i = 0; i < 3; i++) {
            double x = pos.getX() + (level.random.nextDouble() * 2.0D - 1.0D) * radius;
            double y = pos.getY() + level.random.nextDouble() * 2.5D;
            double z = pos.getZ() + (level.random.nextDouble() * 2.0D - 1.0D) * radius;
            level.sendParticles(ParticleTypes.ENTITY_EFFECT, x, y, z, 0, HOLLOW_R, HOLLOW_G, HOLLOW_B, 1.0D);
        }
    }

    private static void spawnStripParticles(ServerLevel level, LivingEntity entity) {
        for (int i = 0; i < 12; i++) {
            double x = entity.getX() + (level.random.nextDouble() - 0.5D) * 0.6D;
            double y = entity.getY() + entity.getBbHeight() * 0.5D + (level.random.nextDouble() - 0.5D) * 0.8D;
            double z = entity.getZ() + (level.random.nextDouble() - 0.5D) * 0.6D;
            level.sendParticles(ParticleTypes.ENTITY_EFFECT, x, y, z, 0, HOLLOW_R, HOLLOW_G, HOLLOW_B, 1.0D);
        }
    }
}
