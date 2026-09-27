package net.astralya.hexalia.event;

import net.astralya.hexalia.effect.ModMobEffects;
import net.astralya.hexalia.item.custom.armor.GhostveilItem;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

public class ModGameEvents {

    private static final double GHOSTVEIL_FORGET_DISTANCE = 16.0D;
    private static final double GHOSTVEIL_SNEAK_FORGET_DISTANCE = 24.0D;
    private static final double GHOSTVEIL_MIN_DETECT_DISTANCE = 6.0D;

    private static final ResourceKey<DamageType> MAGIC =
            ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("minecraft", "magic"));
    private static final ResourceKey<DamageType> INDIRECT_MAGIC =
            ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("minecraft", "indirect_magic"));
    private static final ResourceKey<DamageType> WITHER =
            ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("minecraft", "wither"));
    private static final ResourceKey<DamageType> DRAGON_BREATH =
            ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("minecraft", "dragon_breath"));

    public static void register() {
        registerBrambleguardDamageReduction();
        registerSiphonBlockBreak();
    }

    private static void registerBrambleguardDamageReduction() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            var instance = entity.getEffect(ModMobEffects.BRAMBLEGUARD.get());
            if (instance == null) {
                return true;
            }

            int level = instance.getAmplifier() + 1;
            float reduction = getBrambleguardReduction(source, level);
            if (reduction <= 0.0F) {
                return true;
            }

            float reduced = amount * (1.0F - reduction);
            return reduced > 0.0F;
        });
    }

    private static void registerSiphonBlockBreak() {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            if (world.isClientSide) {
                return true;
            }

            if (player.isCreative()) {
                return true;
            }

            var instance = player.getEffect(ModMobEffects.SIPHON.get());
            if (instance == null) {
                return true;
            }

            int amplifier = instance.getAmplifier();
            float extraExhaustion = 0.025F * (amplifier + 1);
            player.causeFoodExhaustion(extraExhaustion);
            return true;
        });
    }

    private static float getBrambleguardReduction(DamageSource source, int level) {
        if (isMagicDamage(source)) {
            return clamp01(0.10F * level);
        }

        return clamp01(0.05F * level);
    }

    private static float clamp01(float value) {
        if (value < 0.0F) {
            return 0.0F;
        }

        return Math.min(value, 1.0F);
    }

    public static boolean isMagicDamage(DamageSource source) {
        return source.is(MAGIC)
                || source.is(INDIRECT_MAGIC)
                || source.is(WITHER)
                || source.is(DRAGON_BREATH);
    }

    public static void handleHollowSilenceDarkness(Player player) {
        if (player.level().isClientSide) {
            return;
        }

        if (player.getEffect(ModMobEffects.HOLLOW_SILENCE.get()) == null) {
            return;
        }

        if (player.tickCount % 40 != 0) {
            return;
        }

        player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60, 0, true, false, true));
    }

    public static boolean shouldGhostveilPreventTarget(LivingEntity mob, LivingEntity proposedTarget) {
        if (!(proposedTarget instanceof Player player)) {
            return false;
        }

        if (!GhostveilItem.isWornBy(player)) {
            return false;
        }

        double forgetDistance = player.isShiftKeyDown() ? GHOSTVEIL_SNEAK_FORGET_DISTANCE : GHOSTVEIL_FORGET_DISTANCE;
        double distanceSquared = mob.distanceToSqr(player);
        if (distanceSquared <= GHOSTVEIL_MIN_DETECT_DISTANCE * GHOSTVEIL_MIN_DETECT_DISTANCE) {
            return false;
        }

        return distanceSquared <= forgetDistance * forgetDistance;
    }
}
