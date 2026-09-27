package net.astralya.hexalia.effect.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class ArachnidGraceEffect extends MobEffect {

    public ArachnidGraceEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.level().isClientSide() && livingEntity.hasEffect(MobEffects.POISON)) {
            livingEntity.removeEffect(MobEffects.POISON);
        }
        if (livingEntity.horizontalCollision && !livingEntity.isCrouching()) {
            Vec3 initialVec = livingEntity.getDeltaMovement();
            Vec3 climbVec = new Vec3(initialVec.x, 0.2D, initialVec.z);
            livingEntity.setDeltaMovement(climbVec.scale(0.96D));
            return;
        }

        if (livingEntity.isInWaterRainOrBubble()) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 0, false, true, true));
        }

        super.applyEffectTick(livingEntity, amplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
