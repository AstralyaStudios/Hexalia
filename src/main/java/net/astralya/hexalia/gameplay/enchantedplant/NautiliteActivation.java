package net.astralya.hexalia.gameplay.enchantedplant;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.astralya.hexalia.Configuration;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

final class NautiliteActivation {
  private static final float PULSE_DAMAGE = 6.0F;
  private static final Map<UUID, Integer> ACTIVE_PLAYERS = new HashMap<>();

  private NautiliteActivation() {}

  static boolean activate(ServerLevel level, Player player) {
    int duration = Math.max(1, Configuration.NAUTILITE_DURATION.get());
    player.removeEffect(MobEffects.DIG_SLOWDOWN);
    player.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, duration, 0, true, false));
    ACTIVE_PLAYERS.put(player.getUUID(), level.getServer().getTickCount() + duration);

    int radius = Math.max(1, Configuration.NAUTILITE_EFFECT_RADIUS.get());
    AABB area = player.getBoundingBox().inflate(radius);
    for (LivingEntity target : level.getEntitiesOfClass(
        LivingEntity.class,
        area,
        entity -> entity instanceof Guardian || entity instanceof Drowned)) {
      target.hurt(level.damageSources().magic(), PULSE_DAMAGE);
      level.sendParticles(
          ParticleTypes.NAUTILUS,
          target.getX(),
          target.getY() + target.getBbHeight() * 0.6,
          target.getZ(),
          4,
          target.getBbWidth() * 0.3,
          target.getBbHeight() * 0.25,
          target.getBbWidth() * 0.3,
          0.02);
    }

    level.sendParticles(
        ParticleTypes.NAUTILUS,
        player.getX(),
        player.getY() + 1.0,
        player.getZ(),
        18,
        0.65,
        0.8,
        0.65,
        0.04);
    level.playSound(
        null,
        player.blockPosition(),
        SoundEvents.CONDUIT_ACTIVATE,
        SoundSource.PLAYERS,
        0.65F,
        1.15F);
    return true;
  }

  static void tick(MinecraftServer server) {
    Iterator<Map.Entry<UUID, Integer>> entries = ACTIVE_PLAYERS.entrySet().iterator();
    while (entries.hasNext()) {
      Map.Entry<UUID, Integer> entry = entries.next();
      Player player = server.getPlayerList().getPlayer(entry.getKey());
      if (server.getTickCount() >= entry.getValue()
          || player == null
          || !player.isAlive()
          || player.isRemoved()) {
        entries.remove();
        continue;
      }
      if (player.hasEffect(MobEffects.DIG_SLOWDOWN)) {
        player.removeEffect(MobEffects.DIG_SLOWDOWN);
      }
    }
  }
}
