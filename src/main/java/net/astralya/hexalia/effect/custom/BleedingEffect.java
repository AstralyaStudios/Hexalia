package net.astralya.hexalia.effect.custom;

import net.astralya.hexalia.Configuration;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class BleedingEffect extends StatusEffect {

    private static final float BASE_DAMAGE = 0.5f;

    public BleedingEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (!entity.getEntityWorld().isClient && entity.getHealth() > 0f) {
            float damage = (float) Configuration.BLEEDING_DAMAGE.get() + amplifier * 0.2f;
            entity.damage(entity.getDamageSources().generic(), damage);
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
