package net.astralya.hexalia.gameplay.enchantedplant;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.astralya.hexalia.Configuration;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;

final class NautiliteActivation {
  private static final float PULSE_DAMAGE = 6.0F;
  private static final Map<UUID, Integer> ACTIVE_PLAYERS = new HashMap<>();

  private NautiliteActivation() {}

  static boolean activate(ServerWorld world, PlayerEntity player) {
    int duration = Math.max(1, Configuration.NAUTILITE_DURATION.get());
    player.removeStatusEffect(StatusEffects.MINING_FATIGUE);
    player.addStatusEffect(new StatusEffectInstance(StatusEffects.CONDUIT_POWER, duration, 0, true, false));
    ACTIVE_PLAYERS.put(player.getUuid(), world.getServer().getTicks() + duration);

    int radius = Math.max(1, Configuration.NAUTILITE_EFFECT_RADIUS.get());
    Box area = player.getBoundingBox().expand(radius);
    for (LivingEntity target : world.getEntitiesByClass(
        LivingEntity.class,
        area,
        entity -> entity instanceof GuardianEntity || entity instanceof DrownedEntity)) {
      target.damage(world.getDamageSources().magic(), PULSE_DAMAGE);
      world.spawnParticles(
          ParticleTypes.NAUTILUS,
          target.getX(),
          target.getY() + target.getHeight() * 0.6,
          target.getZ(),
          4,
          target.getWidth() * 0.3,
          target.getHeight() * 0.25,
          target.getWidth() * 0.3,
          0.02);
    }

    world.spawnParticles(
        ParticleTypes.NAUTILUS,
        player.getX(),
        player.getY() + 1.0,
        player.getZ(),
        18,
        0.65,
        0.8,
        0.65,
        0.04);
    world.playSound(
        null,
        player.getBlockPos(),
        SoundEvents.BLOCK_CONDUIT_ACTIVATE,
        SoundCategory.PLAYERS,
        0.65F,
        1.15F);
    return true;
  }

  static void tick(MinecraftServer server) {
    Iterator<Map.Entry<UUID, Integer>> entries = ACTIVE_PLAYERS.entrySet().iterator();
    while (entries.hasNext()) {
      Map.Entry<UUID, Integer> entry = entries.next();
      PlayerEntity player = server.getPlayerManager().getPlayer(entry.getKey());
      if (server.getTicks() >= entry.getValue()
          || player == null
          || !player.isAlive()
          || player.isRemoved()) {
        entries.remove();
        continue;
      }
      if (player.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
        player.removeStatusEffect(StatusEffects.MINING_FATIGUE);
      }
    }
  }
}
