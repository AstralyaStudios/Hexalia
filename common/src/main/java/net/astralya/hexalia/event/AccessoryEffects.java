package net.astralya.hexalia.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.TickEvent;
import net.astralya.hexalia.Hexalia;
import net.astralya.hexalia.integration.accessories.AccessoriesIntegration;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public final class AccessoryEffects {
  private static final ResourceLocation WITCHHEART_HEALTH_ID =
      ResourceLocation.fromNamespaceAndPath(Hexalia.MOD_ID, "witchheart_cluster");
  private static final AttributeModifier WITCHHEART_HEALTH =
      new AttributeModifier(WITCHHEART_HEALTH_ID, 4.0, AttributeModifier.Operation.ADD_VALUE);
  private static final float WYRD_DODGE_CHANCE = 0.1F;
  private static final int SEAFOAM_AIR_RESTORE_INTERVAL = 200;
  private static final int SEAFOAM_AIR_RESTORE_AMOUNT = 20;

  private AccessoryEffects() {}

  public static void register() {
    TickEvent.PLAYER_POST.register(AccessoryEffects::onPlayerTick);
    EntityEvent.LIVING_HURT.register(AccessoryEffects::onLivingHurt);
  }

  private static void onPlayerTick(Player player) {
    if (player.level().isClientSide()) {
      return;
    }

    updateWitchheart(player);
    updateMoonward(player);
    updateSeafoam(player);
  }

  private static void updateWitchheart(Player player) {
    AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
    if (maxHealth == null) {
      return;
    }

    if (AccessoriesIntegration.isEquipped(player, ModItems.WITCHHEART_CLUSTER.get())) {
      if (!maxHealth.hasModifier(WITCHHEART_HEALTH_ID)) {
        maxHealth.addTransientModifier(WITCHHEART_HEALTH);
      }
    } else {
      maxHealth.removeModifier(WITCHHEART_HEALTH_ID);
    }
  }

  private static void updateMoonward(Player player) {
    if (!AccessoriesIntegration.isEquipped(player, ModItems.MOONWARD_RING.get())) {
      return;
    }

    player.removeEffect(MobEffects.DARKNESS);
    player.removeEffect(MobEffects.BLINDNESS);
  }

  private static void updateSeafoam(Player player) {
    if (!player.isUnderWater()
        || !AccessoriesIntegration.isEquipped(player, ModItems.SEAFOAM_TALISMAN.get())) {
      return;
    }

    int air = player.getAirSupply();
    int restoredAir = air > 0 && player.tickCount % 4 != 0 ? air + 1 : air;
    if (player.tickCount % SEAFOAM_AIR_RESTORE_INTERVAL == 0) {
      restoredAir += SEAFOAM_AIR_RESTORE_AMOUNT;
    }
    player.setAirSupply(Math.min(restoredAir, player.getMaxAirSupply()));
  }

  private static EventResult onLivingHurt(
      LivingEntity entity, DamageSource source, float amount) {
    if (!(entity instanceof Player player)
        || player.level().isClientSide()
        || !isDirectAttack(source)
        || !AccessoriesIntegration.isEquipped(player, ModItems.WYRD_FEATHER.get())
        || player.getRandom().nextFloat() >= WYRD_DODGE_CHANCE) {
      return EventResult.pass();
    }

    ServerLevel level = (ServerLevel) player.level();
    level.sendParticles(
        ParticleTypes.POOF,
        player.getX(),
        player.getY() + player.getBbHeight() * 0.5,
        player.getZ(),
        6,
        0.25,
        0.35,
        0.25,
        0.02);
    level.playSound(
        null,
        player.getX(),
        player.getY(),
        player.getZ(),
        SoundEvents.BREEZE_WIND_CHARGE_BURST,
        SoundSource.PLAYERS,
        0.25F,
        1.4F);
    return EventResult.interruptFalse();
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
