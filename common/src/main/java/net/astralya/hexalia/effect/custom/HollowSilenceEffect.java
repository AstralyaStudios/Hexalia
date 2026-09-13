package net.astralya.hexalia.effect.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class HollowSilenceEffect extends MobEffect {
  public HollowSilenceEffect(MobEffectCategory category, int color) {
    super(category, color);
  }

  @Override
  public boolean applyEffectTick(LivingEntity entity, int amplifier) {
    if (!entity.level().isClientSide()
        && entity instanceof Player
        && entity.tickCount % 40 == 0) {
      entity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60, 0, true, false, true));
    }
    return true;
  }

  @Override
  public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
    return true;
  }
}
