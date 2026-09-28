package net.astralya.hexalia.effect.custom;

import net.astralya.hexalia.Configuration;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class BleedingEffect extends MobEffect {

  public BleedingEffect(MobEffectCategory pCategory, int pColor) {
    super(pCategory, pColor);
  }

  @Override
  public void applyEffectTick(LivingEntity entity, int amplifier) {
    if (!entity.level().isClientSide && entity.getHealth() > 0f) {
      float damage = (float) Configuration.BLEEDING_DAMAGE.get() + amplifier * 0.2f;
      entity.hurt(entity.damageSources().generic(), damage);
    }
  }

  @Override
  public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
    return true;
  }
}
