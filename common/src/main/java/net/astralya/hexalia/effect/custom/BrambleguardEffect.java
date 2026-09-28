package net.astralya.hexalia.effect.custom;

import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class BrambleguardEffect extends MobEffect {

  public BrambleguardEffect(MobEffectCategory category, int color) {
    super(category, color);
  }

  @Override
  public void applyEffectTick(LivingEntity entity, int amplifier) {
    List<MobEffectInstance> bleedingEffects =
        entity.getActiveEffects().stream()
            .filter(
                instance -> {
                  ResourceLocation id = BuiltInRegistries.MOB_EFFECT.getKey(instance.getEffect());
                  return id != null
                      && (id.getPath().contains("bleed") || id.getPath().contains("bleeding"));
                })
            .toList();

    bleedingEffects.forEach(instance -> entity.removeEffect(instance.getEffect()));
  }

  @Override
  public boolean isDurationEffectTick(int duration, int amplifier) {
    return true;
  }
}
