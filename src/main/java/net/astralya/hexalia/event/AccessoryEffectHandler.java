package net.astralya.hexalia.event;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.compat.accessory.AccessoryLookup;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = HexaliaMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class AccessoryEffectHandler {
    private static final UUID WITCHHEART_HEALTH_ID = UUID.nameUUIDFromBytes(
            "hexalia:witchheart_cluster".getBytes(StandardCharsets.UTF_8));
    private static final AttributeModifier WITCHHEART_HEALTH = new AttributeModifier(
            WITCHHEART_HEALTH_ID, "hexalia:witchheart_cluster", 4.0,
            AttributeModifier.Operation.ADDITION);
    private static final float WYRD_DODGE_CHANCE = 0.1F;
    private static final int SEAFOAM_AIR_RESTORE_INTERVAL = 200;
    private static final int SEAFOAM_AIR_RESTORE_AMOUNT = 20;

    private AccessoryEffectHandler() {
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) {
            return;
        }
        updateWitchheart(event.player);
        updateMoonward(event.player);
        updateSeafoam(event.player);
    }

    private static void updateWitchheart(Player player) {
        AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth == null) {
            return;
        }
        if (AccessoryLookup.hasEquipped(player, ModItems.WITCHHEART_CLUSTER.get())) {
            if (maxHealth.getModifier(WITCHHEART_HEALTH_ID) == null) {
                maxHealth.addTransientModifier(WITCHHEART_HEALTH);
            }
        } else {
            maxHealth.removeModifier(WITCHHEART_HEALTH_ID);
        }
    }

    private static void updateMoonward(Player player) {
        if (AccessoryLookup.hasEquipped(player, ModItems.MOONWARD_RING.get())) {
            player.removeEffect(MobEffects.DARKNESS);
            player.removeEffect(MobEffects.BLINDNESS);
        }
    }

    private static void updateSeafoam(Player player) {
        if (!player.isUnderWater()
                || !AccessoryLookup.hasEquipped(player, ModItems.SEAFOAM_TALISMAN.get())) {
            return;
        }
        int air = player.getAirSupply();
        int restoredAir = air > 0 && player.tickCount % 4 != 0 ? air + 1 : air;
        if (player.tickCount % SEAFOAM_AIR_RESTORE_INTERVAL == 0) {
            restoredAir += SEAFOAM_AIR_RESTORE_AMOUNT;
        }
        player.setAirSupply(Math.min(restoredAir, player.getMaxAirSupply()));
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (!(event.getEntity() instanceof Player player)
                || player.level().isClientSide
                || !isDirectAttack(event.getSource())
                || !AccessoryLookup.hasEquipped(player, ModItems.WYRD_FEATHER.get())
                || player.getRandom().nextFloat() >= WYRD_DODGE_CHANCE) {
            return;
        }
        ServerLevel level = (ServerLevel) player.level();
        level.sendParticles(ParticleTypes.POOF, player.getX(),
                player.getY() + player.getBbHeight() * 0.5, player.getZ(),
                6, 0.25, 0.35, 0.25, 0.02);
        event.setCanceled(true);
    }

    private static boolean isDirectAttack(DamageSource source) {
        if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            return source.getDirectEntity() != null;
        }
        return source.is(DamageTypes.PLAYER_ATTACK)
                || source.is(DamageTypes.MOB_ATTACK)
                || source.is(DamageTypes.MOB_ATTACK_NO_AGGRO);
    }
}
