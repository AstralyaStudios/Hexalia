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
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModMobEffects {

    public static final StatusEffect OVERFED = register("overfed",
            new OverfedEffect(StatusEffectCategory.NEUTRAL, 0xB02B2B).addAttributeModifier(
                    EntityAttributes.GENERIC_MOVEMENT_SPEED,
                    "5f2f3c8d-1e74-4d9d-9d75-3b1c6d9d8a11",
                    -0.1D,
                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL
            ));

    public static final StatusEffect DAYBLOOM = register("daybloom",
            new DaybloomEffect(StatusEffectCategory.NEUTRAL, 0xFFD95E));

    public static final StatusEffect GRAVEBLOOM = register("gravebloom",
            new GravebloomEffect(StatusEffectCategory.BENEFICIAL, 0x527A48));

    public static final StatusEffect BLOODLUST = register("bloodlust",
            new BloodlustEffect(StatusEffectCategory.NEUTRAL, 0x8A0303, 3.0).addAttributeModifier(
                    EntityAttributes.GENERIC_ATTACK_DAMAGE,
                    "6d7d66d6-0f2d-4d82-92b3-8f44f63f9012",
                    0.0D,
                    EntityAttributeModifier.Operation.ADDITION
            ));

    public static final StatusEffect SPIKESKIN = register("spikeskin",
            new SpikeskinEffect(StatusEffectCategory.NEUTRAL, 0x3E6B2F, 3.0)
                    .addAttributeModifier(
                            EntityAttributes.GENERIC_ARMOR,
                            "93e2ecfd-4d77-42e1-8c3c-fde7fdab5d41",
                            0.0D,
                            EntityAttributeModifier.Operation.ADDITION
                    )
                    .addAttributeModifier(
                            EntityAttributes.GENERIC_MOVEMENT_SPEED,
                            "3f97d9e1-7db8-4c5f-a5ff-0d0c1b2d0a63",
                            -0.10D,
                            EntityAttributeModifier.Operation.MULTIPLY_TOTAL
                    ));

    public static final StatusEffect SIPHON = register("siphon",
            new SiphonEffect(StatusEffectCategory.NEUTRAL, 0x6E6EF2, 3.0).addAttributeModifier(
                    EntityAttributes.GENERIC_ATTACK_SPEED,
                    "17c80cb5-8f68-49a8-8b92-3f7e29d66b25",
                    0.4D,
                    EntityAttributeModifier.Operation.ADDITION
            ));

    public static final StatusEffect HOLLOW_SILENCE = register("hollow_silence",
            new HollowSilenceEffect(StatusEffectCategory.NEUTRAL, 0x141218));

    public static final StatusEffect SLIMEWALKER = register("slimewalker",
            new SlimewalkerEffect(StatusEffectCategory.NEUTRAL, 0x6BEA45));

    public static final StatusEffect ARACHNID_GRACE = register("arachnid_grace",
            new ArachnidGraceEffect(StatusEffectCategory.NEUTRAL, 0x3B2E4A));

    public static final StatusEffect BRAMBLEGUARD = register("brambleguard",
            new BrambleguardEffect(StatusEffectCategory.NEUTRAL, 0x415437));

    public static final StatusEffect STUNNED = register("stunned",
            new StunnedEffect(StatusEffectCategory.HARMFUL, 0xFFFFDD));

    public static final StatusEffect BLEEDING = register("bleeding",
            new BleedingEffect(StatusEffectCategory.HARMFUL, 0x8B0000));

    private static StatusEffect register(String name, StatusEffect effect) {
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(HexaliaMod.MODID, name), effect);
    }

    public static void register() {
    }
}
