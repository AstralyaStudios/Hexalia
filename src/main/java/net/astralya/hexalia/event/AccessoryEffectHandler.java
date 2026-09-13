package net.astralya.hexalia.event;

import net.astralya.hexalia.compat.accessory.AccessoryLookup;
import net.astralya.hexalia.item.ModItems;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public final class AccessoryEffectHandler {
    private static final UUID WITCHHEART_HEALTH_ID = UUID.nameUUIDFromBytes(
            "hexalia:witchheart_cluster".getBytes(StandardCharsets.UTF_8));
    private static final EntityAttributeModifier WITCHHEART_HEALTH = new EntityAttributeModifier(
            WITCHHEART_HEALTH_ID, "hexalia:witchheart_cluster", 4.0,
            EntityAttributeModifier.Operation.ADDITION);
    private static final float WYRD_DODGE_CHANCE = 0.1F;
    private static final int SEAFOAM_AIR_RESTORE_INTERVAL = 200;
    private static final int SEAFOAM_AIR_RESTORE_AMOUNT = 20;

    private AccessoryEffectHandler() {
    }

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server ->
                server.getPlayerManager().getPlayerList().forEach(AccessoryEffectHandler::onPlayerTick));
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(AccessoryEffectHandler::allowDamage);
    }

    private static void onPlayerTick(ServerPlayerEntity player) {
        updateWitchheart(player);
        updateMoonward(player);
        updateSeafoam(player);
    }

    private static void updateWitchheart(PlayerEntity player) {
        EntityAttributeInstance maxHealth = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (maxHealth == null) {
            return;
        }
        if (AccessoryLookup.hasEquipped(player, ModItems.WITCHHEART_CLUSTER)) {
            if (maxHealth.getModifier(WITCHHEART_HEALTH_ID) == null) {
                maxHealth.addTemporaryModifier(WITCHHEART_HEALTH);
            }
        } else {
            maxHealth.removeModifier(WITCHHEART_HEALTH_ID);
        }
    }

    private static void updateMoonward(PlayerEntity player) {
        if (AccessoryLookup.hasEquipped(player, ModItems.MOONWARD_RING)) {
            player.removeStatusEffect(StatusEffects.DARKNESS);
            player.removeStatusEffect(StatusEffects.BLINDNESS);
        }
    }

    private static void updateSeafoam(PlayerEntity player) {
        if (!player.isSubmergedInWater()
                || !AccessoryLookup.hasEquipped(player, ModItems.SEAFOAM_TALISMAN)) {
            return;
        }
        int air = player.getAir();
        int restoredAir = air > 0 && player.age % 4 != 0 ? air + 1 : air;
        if (player.age % SEAFOAM_AIR_RESTORE_INTERVAL == 0) {
            restoredAir += SEAFOAM_AIR_RESTORE_AMOUNT;
        }
        player.setAir(Math.min(restoredAir, player.getMaxAir()));
    }

    private static boolean allowDamage(net.minecraft.entity.LivingEntity entity, DamageSource source, float amount) {
        if (!(entity instanceof ServerPlayerEntity player)
                || !isDirectAttack(source)
                || !AccessoryLookup.hasEquipped(player, ModItems.WYRD_FEATHER)
                || player.getRandom().nextFloat() >= WYRD_DODGE_CHANCE) {
            return true;
        }
        ServerWorld world = player.getServerWorld();
        world.spawnParticles(ParticleTypes.POOF, player.getX(),
                player.getY() + player.getHeight() * 0.5, player.getZ(),
                6, 0.25, 0.35, 0.25, 0.02);
        return false;
    }

    private static boolean isDirectAttack(DamageSource source) {
        if (source.isIn(DamageTypeTags.IS_PROJECTILE)) {
            return source.getSource() != null;
        }
        return source.isOf(DamageTypes.PLAYER_ATTACK)
                || source.isOf(DamageTypes.MOB_ATTACK)
                || source.isOf(DamageTypes.MOB_ATTACK_NO_AGGRO);
    }
}
