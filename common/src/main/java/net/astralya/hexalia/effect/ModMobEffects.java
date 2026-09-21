package net.astralya.hexalia.effect;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.astralya.hexalia.Hexalia;
import net.astralya.hexalia.effect.custom.ArachnidGraceEffect;
import net.astralya.hexalia.effect.custom.BleedingEffect;
import net.astralya.hexalia.effect.custom.BloodlustEffect;
import net.astralya.hexalia.effect.custom.BrambleguardEffect;
import net.astralya.hexalia.effect.custom.DaybloomEffect;
import net.astralya.hexalia.effect.custom.GravebloomEffect;
import net.astralya.hexalia.effect.custom.HollowSilenceEffect;
import net.astralya.hexalia.effect.custom.OverfedEffect;
import net.astralya.hexalia.effect.custom.SiphonEffect;
import net.astralya.hexalia.effect.custom.SlimewalkerEffect;
import net.astralya.hexalia.effect.custom.SpikeskinEffect;
import net.astralya.hexalia.effect.custom.StunnedEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public final class ModMobEffects {
  public static final DeferredRegister<MobEffect> MOB_EFFECTS =
      DeferredRegister.create(Hexalia.MOD_ID, Registries.MOB_EFFECT);

  public static final RegistrySupplier<MobEffect> OVERFED =
      MOB_EFFECTS.register(
          "overfed",
          () ->
              new OverfedEffect(MobEffectCategory.NEUTRAL, 0xB02B2B)
                  .addAttributeModifier(
                      Attributes.MOVEMENT_SPEED,
                      id("overfed"),
                      -0.1F,
                      AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

  public static final RegistrySupplier<MobEffect> DAYBLOOM =
      MOB_EFFECTS.register(
          "daybloom", () -> new DaybloomEffect(MobEffectCategory.NEUTRAL, 0xFFD95E));

  public static final RegistrySupplier<MobEffect> GRAVEBLOOM =
      MOB_EFFECTS.register(
          "gravebloom", () -> new GravebloomEffect(MobEffectCategory.BENEFICIAL, 0x527A48));

  public static final RegistrySupplier<MobEffect> BLOODLUST =
      MOB_EFFECTS.register(
          "bloodlust",
          () ->
              new BloodlustEffect(MobEffectCategory.NEUTRAL, 0x8A0303, 3.0D)
                  .addAttributeModifier(
                      Attributes.ATTACK_DAMAGE,
                      id("bloodlust"),
                      3.0F,
                      AttributeModifier.Operation.ADD_VALUE));

  public static final RegistrySupplier<MobEffect> SPIKESKIN =
      MOB_EFFECTS.register(
          "spikeskin",
          () ->
              new SpikeskinEffect(MobEffectCategory.NEUTRAL, 0x3E6B2F, 3.0D)
                  .addAttributeModifier(
                      Attributes.ARMOR,
                      id("spikeskin_armor"),
                      3.0F,
                      AttributeModifier.Operation.ADD_VALUE)
                  .addAttributeModifier(
                      Attributes.MOVEMENT_SPEED,
                      id("spikeskin_slow"),
                      -0.10F,
                      AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

  public static final RegistrySupplier<MobEffect> SIPHON =
      MOB_EFFECTS.register(
          "siphon",
          () ->
              new SiphonEffect(MobEffectCategory.NEUTRAL, 0x6E6EF2, 3.0D)
                  .addAttributeModifier(
                      Attributes.ATTACK_SPEED,
                      id("siphon"),
                      0.4F,
                      AttributeModifier.Operation.ADD_VALUE));

  public static final RegistrySupplier<MobEffect> HOLLOW_SILENCE =
      MOB_EFFECTS.register(
          "hollow_silence", () -> new HollowSilenceEffect(MobEffectCategory.NEUTRAL, 0x141218));

  public static final RegistrySupplier<MobEffect> SLIMEWALKER =
      MOB_EFFECTS.register(
          "slimewalker", () -> new SlimewalkerEffect(MobEffectCategory.NEUTRAL, 0x6BEA45));

  public static final RegistrySupplier<MobEffect> ARACHNID_GRACE =
      MOB_EFFECTS.register(
          "arachnid_grace", () -> new ArachnidGraceEffect(MobEffectCategory.NEUTRAL, 0x3B2E4A));

  public static final RegistrySupplier<MobEffect> BRAMBLEGUARD =
      MOB_EFFECTS.register(
          "brambleguard", () -> new BrambleguardEffect(MobEffectCategory.NEUTRAL, 0x415437));

  public static final RegistrySupplier<MobEffect> STUNNED =
      MOB_EFFECTS.register("stunned", () -> new StunnedEffect(MobEffectCategory.HARMFUL, 0xFFFFDD));

  public static final RegistrySupplier<MobEffect> BLEEDING =
      MOB_EFFECTS.register(
          "bleeding", () -> new BleedingEffect(MobEffectCategory.HARMFUL, 0x8B0000));

  private ModMobEffects() {}

  private static ResourceLocation id(String path) {
    return ResourceLocation.fromNamespaceAndPath(Hexalia.MOD_ID, path);
  }

  public static Holder<MobEffect> holder(RegistrySupplier<MobEffect> effect) {
    return BuiltInRegistries.MOB_EFFECT
        .getResourceKey(effect.get())
        .map(BuiltInRegistries.MOB_EFFECT::getHolderOrThrow)
        .orElseThrow(() -> new IllegalStateException("Unregistered mob effect: " + effect.get()));
  }

  public static void init() {
    MOB_EFFECTS.register();
  }
}
