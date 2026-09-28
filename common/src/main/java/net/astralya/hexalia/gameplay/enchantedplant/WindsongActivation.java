package net.astralya.hexalia.gameplay.enchantedplant;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import net.astralya.hexalia.sound.ModSoundEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class WindsongActivation {
  private static final int HAND_DURATION = 300;
  private static final double HALF_WIDTH = 2.5;
  private static final double HALF_HEIGHT = 2.5;
  private static final double HALF_THICKNESS = 0.35;
  private static final double FORWARD_DISTANCE = 2.0;
  private static final Map<UUID, Integer> ACTIVE_PLAYERS = new HashMap<>();

  private WindsongActivation() {}

  static boolean activate(ServerLevel level, Player player) {
    ACTIVE_PLAYERS.put(player.getUUID(), level.getServer().getTickCount() + HAND_DURATION);
    Vec3 normal = horizontalFacing(player);
    Vec3 center = barrierCenter(player, normal);
    level.sendParticles(
        ParticleTypes.CLOUD, center.x, center.y, center.z, 18, 1.6, 1.8, 0.35, 0.03);
    level.playSound(
        null,
        player.blockPosition(),
        ModSoundEvents.WIND_BURST.get(),
        SoundSource.BLOCKS,
        1.0F,
        1.0F);
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
      ServerLevel level = (ServerLevel) player.level();
      Vec3 normal = horizontalFacing(player);
      Vec3 tangent = new Vec3(-normal.z, 0.0, normal.x);
      Vec3 center = barrierCenter(player, normal);
      AABB plane =
          new AABB(
              center.x - Math.abs(tangent.x) * HALF_WIDTH - Math.abs(normal.x) * HALF_THICKNESS,
              center.y - HALF_HEIGHT,
              center.z - Math.abs(tangent.z) * HALF_WIDTH - Math.abs(normal.z) * HALF_THICKNESS,
              center.x + Math.abs(tangent.x) * HALF_WIDTH + Math.abs(normal.x) * HALF_THICKNESS,
              center.y + HALF_HEIGHT,
              center.z + Math.abs(tangent.z) * HALF_WIDTH + Math.abs(normal.z) * HALF_THICKNESS);

      boolean playedDeflectionSound = false;
      for (Projectile projectile :
          level.getEntitiesOfClass(
              Projectile.class,
              plane,
              candidate -> candidate.isAlive() && candidate.getOwner() != player)) {
        if (!WindsongDeflection.deflect(projectile, normal)) continue;
        if (!playedDeflectionSound) {
          level.playSound(
              null,
              projectile.getX(),
              projectile.getY(),
              projectile.getZ(),
              ModSoundEvents.WIND_BURST.get(),
              SoundSource.BLOCKS,
              1.0F,
              1.0F);
          playedDeflectionSound = true;
        }
        level.sendParticles(
            ParticleTypes.POOF,
            projectile.getX(),
            projectile.getY(),
            projectile.getZ(),
            5,
            0.2,
            0.2,
            0.2,
            0.03);
      }

      if (server.getTickCount() % 4 == 0) {
        emitPlaneParticles(level, center, tangent);
      }
    }
  }

  private static Vec3 horizontalFacing(Player player) {
    Vec3 look = player.getLookAngle();
    Vec3 horizontal = new Vec3(look.x, 0.0, look.z);
    return horizontal.lengthSqr() < 1.0E-6 ? new Vec3(0.0, 0.0, 1.0) : horizontal.normalize();
  }

  private static Vec3 barrierCenter(Player player, Vec3 normal) {
    return new Vec3(player.getX(), player.getY() + HALF_HEIGHT, player.getZ())
        .add(normal.scale(FORWARD_DISTANCE));
  }

  private static void emitPlaneParticles(ServerLevel level, Vec3 center, Vec3 tangent) {
    for (int i = 0; i < 6; i++) {
      double across = (level.random.nextDouble() * 2.0 - 1.0) * HALF_WIDTH;
      double vertical = (level.random.nextDouble() * 2.0 - 1.0) * HALF_HEIGHT;
      Vec3 point = center.add(tangent.scale(across)).add(0.0, vertical, 0.0);
      level.sendParticles(
          ParticleTypes.CLOUD, point.x, point.y, point.z, 1, 0.04, 0.04, 0.04, 0.01);
    }
  }
}
