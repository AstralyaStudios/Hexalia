package net.astralya.hexalia.util;

import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TeleportUtil {
  public static boolean canReturn(Level world, Player player, boolean allowInterdimensional) {
    if (world.isClientSide()) return false;
    ResourceKey<Level> spawnDim = ((ServerPlayer) player).getRespawnDimension();
    return !allowInterdimensional && world.dimension() != spawnDim;
  }

  public static void teleportPlayerToSpawn(
      Level currentWorld, Player player, boolean allowInterdimensional) {
    if (currentWorld.isClientSide() || canReturn(currentWorld, player, allowInterdimensional))
      return;

    ServerLevel targetWorld = getTargetWorld(currentWorld, player);

    player.stopRiding();
    if (player.isSleeping()) {
      player.stopSleeping();
    }

    if (targetWorld != null) {
      BlockPos spawnPoint = getSpawnPointPosition(player, targetWorld);
      if (spawnPoint != null) {
        Optional<Vec3> pos = findRespawnPosition(targetWorld, spawnPoint, player);
        pos.ifPresent(vec -> doReturn(player, currentWorld, targetWorld, vec.x, vec.y, vec.z));
      }
    }
  }

  private static ServerLevel getTargetWorld(Level currentWorld, Player player) {
    ResourceKey<Level> spawnDim = ((ServerPlayer) player).getRespawnDimension();
    return currentWorld.dimension() == spawnDim
        ? (ServerLevel) currentWorld
        : Objects.requireNonNull(currentWorld.getServer()).getLevel(spawnDim);
  }

  private static BlockPos getSpawnPointPosition(Player player, ServerLevel world) {
    return player instanceof ServerPlayer ? ((ServerPlayer) player).getRespawnPosition() : null;
  }

  private static Optional<Vec3> findRespawnPosition(
      ServerLevel world, BlockPos spawnPoint, Player player) {
    return Player.findRespawnPositionAndUseSpawnBlock(
            world, spawnPoint, ((ServerPlayer) player).getRespawnAngle(), false, true)
        .map(
            pos ->
                new Vec3(
                    pos.get(Direction.Axis.X) + 0.5,
                    pos.get(Direction.Axis.Y),
                    pos.get(Direction.Axis.Z) + 0.5));
  }

  private static void doReturn(
      Player player, Level origin, ServerLevel target, double x, double y, double z) {
    playTeleportSound(player, target, x, y, z);
    if (origin != target) {
      ((ServerPlayer) player).teleportTo(target, x, y, z, player.getYRot(), player.getXRot());
    } else {
      player.teleportTo(x, y, z);
    }
    if (player.fallDistance > 0.0F) {
      player.fallDistance = 0.0F;
    }
    playTeleportSound(player, target, x, y, z);
  }

  private static void playTeleportSound(
      Player player, ServerLevel world, double x, double y, double z) {
    world.playSound(null, x, y, z, SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 1f, 1f);
  }
}
