package net.astralya.hexalia.item;

import net.astralya.hexalia.effect.ModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;

public final class ModFoodProperties {
  public static final FoodProperties SIREN_KELP =
      new FoodProperties.Builder()
          .nutrition(1)
          .alwaysEdible()
          .saturationModifier(0.1F)
          .effect(new MobEffectInstance(MobEffects.WATER_BREATHING, 100), 1.0F)
          .build();

  public static final FoodProperties CHILLBERRIES =
      new FoodProperties.Builder().nutrition(2).saturationModifier(0.3F).build();

  public static final FoodProperties CHILLBERRY_PIE =
      new FoodProperties.Builder()
          .nutrition(6)
          .saturationModifier(0.8F)
          .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 900), 1.0F)
          .build();

  public static final FoodProperties MANDRAKE_STEW =
      new FoodProperties.Builder()
          .nutrition(6)
          .usingConvertsTo(Items.BOWL)
          .saturationModifier(0.7F)
          .effect(new MobEffectInstance(ModMobEffects.holder(ModMobEffects.OVERFED), 900), 1.0F)
          .build();

  public static final FoodProperties SUNFIRE_TOMATO =
      new FoodProperties.Builder().nutrition(1).saturationModifier(0.3F).build();

  public static final FoodProperties SPICY_SANDWICH =
      new FoodProperties.Builder()
          .nutrition(6)
          .saturationModifier(0.6F)
          .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 900), 1.0F)
          .build();

  public static final FoodProperties GALEBERRIES =
      new FoodProperties.Builder().nutrition(2).saturationModifier(0.3F).build();

  public static final FoodProperties GALEBERRIES_COOKIE =
      new FoodProperties.Builder()
          .nutrition(2)
          .saturationModifier(0.1F)
          .effect(new MobEffectInstance(MobEffects.NIGHT_VISION, 600), 1.0F)
          .build();

  public static final FoodProperties SALTSPROUT =
      new FoodProperties.Builder()
          .nutrition(2)
          .saturationModifier(0.1F)
          .effect(new MobEffectInstance(MobEffects.HUNGER, 600), 1.0F)
          .build();

  private ModFoodProperties() {}
}
