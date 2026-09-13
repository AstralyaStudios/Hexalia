package net.astralya.hexalia.gameplay.enchantedplant;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import net.astralya.hexalia.Configuration;
import net.astralya.hexalia.sound.ModSoundEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

public final class WindsongActivation {
  private static final int HAND_DURATION = 300;
  private static final double HALF_WIDTH = 2.5;
  private static final double HALF_HEIGHT = 2.5;
  private static final double HALF_THICKNESS = 0.35;
  private static final Map<UUID, Integer> ACTIVE_PLAYERS = new HashMap<>();
  private static final Map<PlacedBarrier, Integer> ACTIVE_PLANTS = new HashMap<>();
  private WindsongActivation() {}

  static boolean activate(ServerWorld world, PlayerEntity player) {
    ACTIVE_PLAYERS.put(player.getUuid(), world.getServer().getTicks() + HAND_DURATION);
    Vec3d normal = horizontalFacing(player);
    Vec3d center = barrierCenter(player, normal);
    world.spawnParticles(ParticleTypes.CLOUD, center.x, center.y, center.z, 18, 1.6, 1.8, 0.35, 0.03);
    world.playSound(null, player.getBlockPos(), ModSoundEvents.WIND_BURST, SoundCategory.BLOCKS, 1.0F, 1.0F);
    return true;
  }

  public static void activatePlaced(ServerWorld world, BlockPos pos) {
    ACTIVE_PLANTS.put(new PlacedBarrier(world.getRegistryKey(), pos.toImmutable()),
        world.getServer().getTicks() + Configuration.WINDSONG_DURATION.get());
  }

  static void tick(MinecraftServer server) {
    Iterator<Map.Entry<UUID, Integer>> entries = ACTIVE_PLAYERS.entrySet().iterator();
    while (entries.hasNext()) {
      Map.Entry<UUID, Integer> entry = entries.next();
      PlayerEntity player = server.getPlayerManager().getPlayer(entry.getKey());
      if (server.getTicks() >= entry.getValue() || player == null || !player.isAlive() || player.isRemoved()) {
        entries.remove();
        continue;
      }
      ServerWorld world = (ServerWorld) player.getWorld();
      Vec3d normal = horizontalFacing(player);
      Vec3d tangent = new Vec3d(-normal.z, 0.0, normal.x);
      Vec3d center = barrierCenter(player, normal);
      Box plane = plane(center, normal, tangent);
      boolean playedDeflectionSound = false;
      for (ProjectileEntity projectile : world.getEntitiesByClass(ProjectileEntity.class, plane, p -> p.isAlive() && p.getOwner() != player)) {
        if (!WindsongDeflection.deflect(projectile, normal)) continue;
        if (!playedDeflectionSound) {
          world.playSound(null, projectile.getX(), projectile.getY(), projectile.getZ(), ModSoundEvents.WIND_BURST, SoundCategory.BLOCKS, 1.0F, 1.0F);
          playedDeflectionSound = true;
        }
        world.spawnParticles(ParticleTypes.POOF, projectile.getX(), projectile.getY(), projectile.getZ(), 5, 0.2, 0.2, 0.2, 0.03);
      }
      if (server.getTicks() % 4 == 0) emitPlaneParticles(world, center, tangent);
    }
    Iterator<Map.Entry<PlacedBarrier, Integer>> plants = ACTIVE_PLANTS.entrySet().iterator();
    while (plants.hasNext()) {
      Map.Entry<PlacedBarrier, Integer> entry = plants.next();
      ServerWorld world = server.getWorld(entry.getKey().dimension());
      if (world == null) { plants.remove(); continue; }
      BlockPos pos = entry.getKey().pos();
      if (server.getTicks() >= entry.getValue()) {
        world.playSound(null, pos, ModSoundEvents.WIND_BURST, SoundCategory.BLOCKS, 1.0F, 1.0F);
        plants.remove();
        continue;
      }
      double radius = Configuration.WINDSONG_EFFECT_RADIUS.get();
      for (ProjectileEntity projectile : world.getEntitiesByClass(ProjectileEntity.class, new Box(pos).expand(radius), p -> p.isAlive())) {
        Vec3d outward = projectile.getPos().subtract(Vec3d.ofCenter(pos)).multiply(1.0, 0.0, 1.0);
        if (WindsongDeflection.deflect(projectile, outward))
          world.playSound(null, projectile.getX(), projectile.getY(), projectile.getZ(), ModSoundEvents.WIND_BURST, SoundCategory.BLOCKS, 1.0F, 1.0F);
      }
      if (server.getTicks() % 5 == 0)
        world.spawnParticles(ParticleTypes.CLOUD, pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5, 3, radius, 1.0, radius, .01);
    }
  }

  private static Vec3d horizontalFacing(PlayerEntity player) {
    Vec3d look = player.getRotationVec(1.0F);
    Vec3d horizontal = new Vec3d(look.x, 0.0, look.z);
    return horizontal.lengthSquared() < 1.0E-6 ? new Vec3d(0.0, 0.0, 1.0) : horizontal.normalize();
  }
  private static Vec3d barrierCenter(PlayerEntity player, Vec3d normal) {
    return new Vec3d(player.getX(), player.getY() + HALF_HEIGHT, player.getZ()).add(normal.multiply(2.0));
  }
  private static Box plane(Vec3d center, Vec3d normal, Vec3d tangent) {
    double x = Math.abs(tangent.x) * HALF_WIDTH + Math.abs(normal.x) * HALF_THICKNESS;
    double z = Math.abs(tangent.z) * HALF_WIDTH + Math.abs(normal.z) * HALF_THICKNESS;
    return new Box(center.x-x, center.y-HALF_HEIGHT, center.z-z, center.x+x, center.y+HALF_HEIGHT, center.z+z);
  }
  private static void emitPlaneParticles(ServerWorld world, Vec3d center, Vec3d tangent) {
    for (int i=0;i<6;i++) {
      Vec3d point=center.add(tangent.multiply((world.random.nextDouble()*2.0-1.0)*HALF_WIDTH)).add(0.0,(world.random.nextDouble()*2.0-1.0)*HALF_HEIGHT,0.0);
      world.spawnParticles(ParticleTypes.CLOUD, point.x, point.y, point.z, 1, 0.04, 0.04, 0.04, 0.01);
    }
  }
  private record PlacedBarrier(RegistryKey<World> dimension, BlockPos pos) {}
}
