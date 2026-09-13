package net.astralya.hexalia.effect;

import net.astralya.hexalia.HexaliaMod;
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
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, HexaliaMod.MODID);

    public static final RegistryObject<MobEffect> OVERFED = MOB_EFFECTS.register("overfed",
            () -> new OverfedEffect(MobEffectCategory.NEUTRAL, 0xB02B2B).addAttributeModifier(
                    Attributes.MOVEMENT_SPEED,
                    "5f2f3c8d-1e74-4d9d-9d75-3b1c6d9d8a11",
                    -0.1D,
                    AttributeModifier.Operation.MULTIPLY_TOTAL
            ));

    public static final RegistryObject<MobEffect> DAYBLOOM = MOB_EFFECTS.register("daybloom",
            () -> new DaybloomEffect(MobEffectCategory.NEUTRAL, 0xFFD95E));

    public static final RegistryObject<MobEffect> GRAVEBLOOM = MOB_EFFECTS.register("gravebloom",
            () -> new GravebloomEffect(MobEffectCategory.BENEFICIAL, 0x527A48));

    public static final RegistryObject<MobEffect> BLOODLUST = MOB_EFFECTS.register("bloodlust",
            () -> new BloodlustEffect(MobEffectCategory.NEUTRAL, 0x8A0303).addAttributeModifier(
                    Attributes.ATTACK_DAMAGE,
                    "6d7d66d6-0f2d-4d82-92b3-8f44f63f9012",
                    0.0D,
                    AttributeModifier.Operation.ADDITION
            ));

    public static final RegistryObject<MobEffect> SPIKESKIN = MOB_EFFECTS.register("spikeskin",
            () -> new SpikeskinEffect(MobEffectCategory.NEUTRAL, 0x3E6B2F, 3.0)
                    .addAttributeModifier(
                            Attributes.ARMOR,
                            "93e2ecfd-4d77-42e1-8c3c-fde7fdab5d41",
                            0.0D,
                            AttributeModifier.Operation.ADDITION
                    )
                    .addAttributeModifier(
                            Attributes.MOVEMENT_SPEED,
                            "3f97d9e1-7db8-4c5f-a5ff-0d0c1b2d0a63",
                            -0.10D,
                            AttributeModifier.Operation.MULTIPLY_TOTAL
                    ));

    public static final RegistryObject<MobEffect> SIPHON = MOB_EFFECTS.register("siphon",
            () -> new SiphonEffect(MobEffectCategory.NEUTRAL, 0x6E6EF2, 3.0).addAttributeModifier(
                    Attributes.ATTACK_SPEED,
                    "17c80cb5-8f68-49a8-8b92-3f7e29d66b25",
                    0.4D,
                    AttributeModifier.Operation.ADDITION
            ));

    public static final RegistryObject<MobEffect> HOLLOW_SILENCE = MOB_EFFECTS.register("hollow_silence",
            () -> new HollowSilenceEffect(MobEffectCategory.NEUTRAL, 0x141218));

    public static final RegistryObject<MobEffect> SLIMEWALKER = MOB_EFFECTS.register("slimewalker",
            () -> new SlimewalkerEffect(MobEffectCategory.NEUTRAL, 0x6BEA45));

    public static final RegistryObject<MobEffect> ARACHNID_GRACE = MOB_EFFECTS.register("arachnid_grace",
            () -> new ArachnidGraceEffect(MobEffectCategory.NEUTRAL, 0x3B2E4A));

    public static final RegistryObject<MobEffect> BRAMBLEGUARD = MOB_EFFECTS.register("brambleguard",
            () -> new BrambleguardEffect(MobEffectCategory.NEUTRAL, 0x415437));

    public static final RegistryObject<MobEffect> STUNNED = MOB_EFFECTS.register("stunned",
            () -> new StunnedEffect(MobEffectCategory.HARMFUL, 0xFFFFDD));

    public static final RegistryObject<MobEffect> BLEEDING = MOB_EFFECTS.register("bleeding",
            () -> new BleedingEffect(MobEffectCategory.HARMFUL, 0x8B0000));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
