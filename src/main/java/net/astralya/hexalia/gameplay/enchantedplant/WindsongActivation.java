package net.astralya.hexalia.gameplay.enchantedplant;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import net.astralya.hexalia.Configuration;
import net.astralya.hexalia.sound.ModSoundEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public final class WindsongActivation {
  private static final int HAND_DURATION = 300;
  private static final double HALF_WIDTH = 2.5;
  private static final double HALF_HEIGHT = 2.5;
  private static final double HALF_THICKNESS = 0.35;
  private static final Map<UUID, Integer> ACTIVE_PLAYERS = new HashMap<>();
  private static final Map<PlacedBarrier, Integer> ACTIVE_PLANTS = new HashMap<>();
  private WindsongActivation() {}

  static boolean activate(ServerLevel level, Player player) {
    ACTIVE_PLAYERS.put(player.getUUID(), level.getServer().getTickCount() + HAND_DURATION);
    Vec3 normal = horizontalFacing(player);
    Vec3 center = barrierCenter(player, normal);
    level.sendParticles(ParticleTypes.CLOUD, center.x, center.y, center.z, 18, 1.6, 1.8, 0.35, 0.03);
    level.playSound(null, player.blockPosition(), ModSoundEvents.WIND_BURST.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
    return true;
  }

  public static void activatePlaced(ServerLevel level, BlockPos pos) {
    ACTIVE_PLANTS.put(new PlacedBarrier(level.dimension(), pos.immutable()),
        level.getServer().getTickCount() + Configuration.WINDSONG_DURATION.get());
  }

  static void tick(MinecraftServer server) {
    Iterator<Map.Entry<UUID, Integer>> entries = ACTIVE_PLAYERS.entrySet().iterator();
    while (entries.hasNext()) {
      Map.Entry<UUID, Integer> entry = entries.next();
      Player player = server.getPlayerList().getPlayer(entry.getKey());
      if (server.getTickCount() >= entry.getValue() || player == null || !player.isAlive() || player.isRemoved()) {
        entries.remove();
        continue;
      }
      ServerLevel level = (ServerLevel) player.level();
      Vec3 normal = horizontalFacing(player);
      Vec3 tangent = new Vec3(-normal.z, 0.0, normal.x);
      Vec3 center = barrierCenter(player, normal);
      AABB plane = plane(center, normal, tangent);
      boolean playedDeflectionSound = false;
      for (Projectile projectile : level.getEntitiesOfClass(Projectile.class, plane, p -> p.isAlive() && p.getOwner() != player)) {
        if (!WindsongDeflection.deflect(projectile, normal)) continue;
        if (!playedDeflectionSound) {
          level.playSound(null, projectile.getX(), projectile.getY(), projectile.getZ(), ModSoundEvents.WIND_BURST.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
          playedDeflectionSound = true;
        }
        level.sendParticles(ParticleTypes.POOF, projectile.getX(), projectile.getY(), projectile.getZ(), 5, 0.2, 0.2, 0.2, 0.03);
      }
      if (server.getTickCount() % 4 == 0) emitPlaneParticles(level, center, tangent);
    }
    Iterator<Map.Entry<PlacedBarrier, Integer>> plants = ACTIVE_PLANTS.entrySet().iterator();
    while (plants.hasNext()) {
      Map.Entry<PlacedBarrier, Integer> entry = plants.next();
      ServerLevel level = server.getLevel(entry.getKey().dimension());
      if (level == null) { plants.remove(); continue; }
      BlockPos pos = entry.getKey().pos();
      if (server.getTickCount() >= entry.getValue()) {
        level.playSound(null, pos, ModSoundEvents.WIND_BURST.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        plants.remove();
        continue;
      }
      double radius = Configuration.WINDSONG_EFFECT_RADIUS.get();
      for (Projectile projectile : level.getEntitiesOfClass(Projectile.class, new AABB(pos).inflate(radius), Projectile::isAlive)) {
        Vec3 outward = projectile.position().subtract(Vec3.atCenterOf(pos)).multiply(1.0, 0.0, 1.0);
        if (WindsongDeflection.deflect(projectile, outward))
          level.playSound(null, projectile.getX(), projectile.getY(), projectile.getZ(), ModSoundEvents.WIND_BURST.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
      }
      if (server.getTickCount() % 5 == 0)
        level.sendParticles(ParticleTypes.CLOUD, pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5, 3, radius, 1.0, radius, .01);
    }
  }

  private static Vec3 horizontalFacing(Player player) {
    Vec3 look = player.getLookAngle();
    Vec3 horizontal = new Vec3(look.x, 0.0, look.z);
    return horizontal.lengthSqr() < 1.0E-6 ? new Vec3(0.0, 0.0, 1.0) : horizontal.normalize();
  }
  private static Vec3 barrierCenter(Player player, Vec3 normal) {
    return new Vec3(player.getX(), player.getY() + HALF_HEIGHT, player.getZ()).add(normal.scale(2.0));
  }
  private static AABB plane(Vec3 center, Vec3 normal, Vec3 tangent) {
    double x = Math.abs(tangent.x) * HALF_WIDTH + Math.abs(normal.x) * HALF_THICKNESS;
    double z = Math.abs(tangent.z) * HALF_WIDTH + Math.abs(normal.z) * HALF_THICKNESS;
    return new AABB(center.x-x, center.y-HALF_HEIGHT, center.z-z, center.x+x, center.y+HALF_HEIGHT, center.z+z);
  }
  private static void emitPlaneParticles(ServerLevel level, Vec3 center, Vec3 tangent) {
    for (int i=0;i<6;i++) {
      Vec3 point=center.add(tangent.scale((level.random.nextDouble()*2.0-1.0)*HALF_WIDTH)).add(0.0,(level.random.nextDouble()*2.0-1.0)*HALF_HEIGHT,0.0);
      level.sendParticles(ParticleTypes.CLOUD, point.x, point.y, point.z, 1, 0.04, 0.04, 0.04, 0.01);
    }
  }
  private record PlacedBarrier(ResourceKey<Level> dimension, BlockPos pos) {}
}
