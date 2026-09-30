package net.astralya.hexalia.gameplay.naturesritual;

import java.util.List;
import net.astralya.hexalia.block.entity.custom.RitualBrazierBlockEntity;
import net.astralya.hexalia.particle.ModParticleType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public final class NatureRitualVisuals {
  private static final double GLYPH_HEIGHT = 1.9;

  private NatureRitualVisuals() {}

  public static void onStart(
      ServerLevel server, BlockPos pos, List<RitualBrazierBlockEntity> braziers) {
    server.sendParticles(
        ModParticleType.RITUAL_GLYPH.get(),
        pos.getX() + 0.5, pos.getY() + GLYPH_HEIGHT, pos.getZ() + 0.5,
        0, 0.01, 0.0, 0.0, 1.0);
    for (RitualBrazierBlockEntity brazier : braziers) {
      BlockPos target = brazier.getBlockPos();
      double dx = target.getX() - pos.getX();
      double dz = target.getZ() - pos.getZ();
      for (int index = 0; index < 3; index++) {
        server.sendParticles(
            ModParticleType.HEX_MOTES.get(),
            pos.getX() + 0.5, pos.getY() + 0.85, pos.getZ() + 0.5,
            0, dx * (0.035 + index * 0.007), 0.01, dz * (0.035 + index * 0.007), 1.0);
      }
    }
    playNatureSound(server, pos, SoundEvents.AMETHYST_BLOCK_CHIME, 0.42F, 0.9F);
  }

  public static void onTick(
      ServerLevel server, BlockPos pos, List<BlockPos> energyPositions,
      List<BlockPos> activeBraziers, int nextBrazierIndex, @Nullable BlockPos offeringOrigin,
      int elapsed, int energyElapsed, int ticksRemaining) {
    spawnNatureAmbient(server, pos, activeBraziers, nextBrazierIndex,
        offeringOrigin, elapsed, ticksRemaining);
    if (energyElapsed >= 0 && ticksRemaining > 6) {
      for (int index = Math.max(0, (energyElapsed - 11 + 2) / 3);
          index <= energyElapsed / 3 && index < energyPositions.size(); index++) {
        int cropAge = energyElapsed - index * 3;
        if (cropAge == 0)
          spawnNatureCropEnergy(server, pos, energyPositions.get(index));
        if (cropAge < 12)
          spawnNatureCropTrail(server, pos, energyPositions.get(index), cropAge);
      }
    }
    if (ticksRemaining == 6) {
      server.sendParticles(
          ModParticleType.RITUAL_GLYPH.get(),
          pos.getX() + 0.5, pos.getY() + GLYPH_HEIGHT, pos.getZ() + 0.5,
          0, 0.01, 0.01, 0.0, 1.0);
    }
    if (ticksRemaining <= 6)
      spawnNatureFinale(server, pos, ticksRemaining);
  }

  public static void onComplete(Level level, BlockPos pos) {
    playNatureCompletion(level, pos);
    if (level instanceof ServerLevel server) {
      server.sendParticles(
          ModParticleType.HEX_MOTES.get(),
          pos.getX() + 0.5, pos.getY() + 1.05, pos.getZ() + 0.5,
          12, 0.15, 0.12, 0.15, 0.045);
      spawnNatureLeaves(server, pos, 1.0, 4);
    }
  }

  public static void onFailure(ServerLevel server, BlockPos pos) {
    server.sendParticles(
        ModParticleType.HEX_MOTES.get(),
        pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
        7, 0.25, 0.2, 0.25, 0.07);
  }

  public static void playNatureCompletion(Level level, BlockPos pos) {
    playNatureSound(level, pos, SoundEvents.AMETHYST_BLOCK_CHIME, 0.45F, 1.3F);
  }

  public static void playNatureFailure(Level level, BlockPos pos) {
    playNatureSound(level, pos, SoundEvents.CANDLE_EXTINGUISH, 0.4F, 0.6F);
  }

  private static void playNatureSound(
      Level level, BlockPos pos, SoundEvent sound, float volume, float pitch) {
    level.playSound(null, pos, sound, SoundSource.BLOCKS, volume, pitch);
  }

  public static void spawnNatureCropEnergy(ServerLevel server, BlockPos center, BlockPos crop) {
    spawnNatureLeaves(server, crop, 0.65, 1 + server.random.nextInt(3));
    double sourceX = crop.getX() + 0.5;
    double sourceY = crop.getY() + 0.65;
    double sourceZ = crop.getZ() + 0.5;
    double dx = center.getX() + 0.5 - sourceX;
    double dz = center.getZ() + 0.5 - sourceZ;
    double distance = Math.sqrt(dx * dx + dz * dz);
    for (int index = 0; index < 4; index++) {
      double sideways = (index - 1.5) * 0.012 / Math.max(1.0, distance);
      server.sendParticles(
          ModParticleType.HEX_MOTES.get(),
          sourceX + (server.random.nextDouble() - 0.5) * 0.25,
          sourceY + server.random.nextDouble() * 0.2,
          sourceZ + (server.random.nextDouble() - 0.5) * 0.25,
          0,
          dx / 35.0 - dz * sideways,
          0.012 + index * 0.003,
          dz / 35.0 + dx * sideways,
          1.0);
    }
  }

  public static void spawnNatureCropTrail(
      ServerLevel server, BlockPos center, BlockPos crop, int age) {
    double sourceX = crop.getX() + 0.5;
    double sourceY = crop.getY() + 0.7;
    double sourceZ = crop.getZ() + 0.5;
    double dx = center.getX() + 0.5 - sourceX;
    double dz = center.getZ() + 0.5 - sourceZ;
    double progress = age / 11.0;
    double bend = Math.sin(progress * Math.PI) * 0.32 / Math.max(1.0, Math.sqrt(dx * dx + dz * dz));
    server.sendParticles(
        ModParticleType.HEX_MOTES.get(),
        sourceX + dx * progress - dz * bend,
        sourceY + (center.getY() + 1.05 - sourceY) * progress
            + Math.sin(progress * Math.PI) * 0.22,
        sourceZ + dz * progress + dx * bend,
        1, 0.015, 0.015, 0.015, 0.0);
    if (age == 2 || age == 5)
      server.sendParticles(
          ModParticleType.LEAVES.get(),
          sourceX + dx * progress - dz * bend,
          sourceY + (center.getY() + 1.05 - sourceY) * progress
              + Math.sin(progress * Math.PI) * 0.22,
          sourceZ + dz * progress + dx * bend,
          0,
          dx * 0.0015 - dz * bend * 0.012
              + (server.random.nextDouble() - 0.5) * 0.014,
          0.006 + server.random.nextDouble() * 0.008,
          dz * 0.0015 + dx * bend * 0.012
              + (server.random.nextDouble() - 0.5) * 0.014,
          1.0);
  }

  public static void spawnNatureFinale(ServerLevel server, BlockPos center, int ticksRemaining) {
    double angle = (6 - ticksRemaining) * 0.8;
    double radius = 0.52 - (6 - ticksRemaining) * 0.065;
    for (int index = 0; index < 3; index++) {
      double orbit = angle + index * Math.PI * 2.0 / 3.0;
      server.sendParticles(
          ModParticleType.HEX_MOTES.get(),
          center.getX() + 0.5 + Math.cos(orbit) * radius,
          center.getY() + 1.05 + index * 0.08,
          center.getZ() + 0.5 + Math.sin(orbit) * radius,
          0, -Math.cos(orbit) * 0.012, 0.005, -Math.sin(orbit) * 0.012, 1.0);
    }
  }

  private static void spawnNatureAmbient(
      ServerLevel server, BlockPos center, List<BlockPos> braziers, int nextBrazierIndex,
      @Nullable BlockPos offeringOrigin, int elapsed, int ticksRemaining) {
    double focus = Math.max(0.0, Math.min(1.0, (30.0 - ticksRemaining) / 24.0));
    if (elapsed > 0 && elapsed % 52 == 0 && ticksRemaining > 30)
      server.sendParticles(
          ModParticleType.RITUAL_GLYPH.get(),
          center.getX() + 0.5, center.getY() + GLYPH_HEIGHT, center.getZ() + 0.5,
          0, 0.01, -0.01, 0.0, 1.0);
    if (elapsed % (focus > 0.5 ? 3 : 5) == 0) {
      double angle = elapsed * 0.21 + server.random.nextDouble() * 1.2;
      double radius = (0.55 + server.random.nextDouble() * 0.75) * (1.0 - focus * 0.68);
      double offsetX = Math.cos(angle) * radius;
      double offsetZ = Math.sin(angle) * radius;
      double x = center.getX() + 0.5 + offsetX;
      double y = center.getY() + 0.75 + server.random.nextDouble() * 0.55;
      double z = center.getZ() + 0.5 + offsetZ;
      server.sendParticles(
          ModParticleType.HEX_MOTES.get(),
          x, y, z,
          0,
          -Math.sin(angle) * 0.009 - offsetX * (0.003 + focus * 0.018),
          0.004 + server.random.nextDouble() * 0.006,
          Math.cos(angle) * 0.009 - offsetZ * (0.003 + focus * 0.018),
          1.0);
      if (server.random.nextInt(focus > 0.5 ? 8 : 3) == 0)
        server.sendParticles(
            ModParticleType.LEAVES.get(),
            x + offsetX * 0.2 + (server.random.nextDouble() - 0.5) * 0.2,
            y - 0.12 + server.random.nextDouble() * 0.2,
            z + offsetZ * 0.2 + (server.random.nextDouble() - 0.5) * 0.2,
            0,
            -Math.sin(angle) * 0.006 - offsetX * 0.002,
            0.004 + server.random.nextDouble() * 0.004,
            Math.cos(angle) * 0.006 - offsetZ * 0.002,
            1.0);
    }

    int pending = braziers.size() - nextBrazierIndex - (offeringOrigin == null ? 0 : 1);
    if (pending > 0 && elapsed % 10 == 4) {
      int firstPending = nextBrazierIndex + (offeringOrigin == null ? 0 : 1);
      BlockPos brazier = braziers.get(firstPending + server.random.nextInt(pending));
      double x = brazier.getX() + 0.5 + (server.random.nextDouble() - 0.5) * 0.4;
      double z = brazier.getZ() + 0.5 + (server.random.nextDouble() - 0.5) * 0.4;
      double angle = server.random.nextDouble() * Math.PI * 2.0;
      server.sendParticles(
          ModParticleType.HEX_MOTES.get(), x,
          brazier.getY() + 0.8 + server.random.nextDouble() * 0.35, z,
          0,
          (center.getX() + 0.5 - x) * 0.003 + Math.cos(angle) * 0.007,
          0.006 + server.random.nextDouble() * 0.008,
          (center.getZ() + 0.5 - z) * 0.003 + Math.sin(angle) * 0.007,
          1.0);
      if (ticksRemaining > 24 && server.random.nextInt(3) == 0)
        server.sendParticles(
            ModParticleType.LEAVES.get(),
            x + (server.random.nextDouble() - 0.5) * 0.3,
            brazier.getY() + 0.78 + server.random.nextDouble() * 0.38,
            z + (server.random.nextDouble() - 0.5) * 0.3,
            0,
            (center.getX() + 0.5 - x) * 0.0015
                + (server.random.nextDouble() - 0.5) * 0.01,
            0.004 + server.random.nextDouble() * 0.006,
            (center.getZ() + 0.5 - z) * 0.0015
                + (server.random.nextDouble() - 0.5) * 0.01,
            1.0);
    }

    if (offeringOrigin != null && elapsed % 4 == 2) {
      int offeringAge = elapsed - nextBrazierIndex * 40;
      double progress = Math.max(0.0, Math.min(1.0, (offeringAge - 16) / 17.0));
      double x = offeringOrigin.getX() + 0.5
          + (center.getX() - offeringOrigin.getX()) * progress;
      double y = offeringOrigin.getY() + 1.05 + (center.getY() - offeringOrigin.getY() + 0.1) * progress;
      double z = offeringOrigin.getZ() + 0.5
          + (center.getZ() - offeringOrigin.getZ()) * progress;
      double angle = offeringAge * 0.43 + server.random.nextDouble() * 1.1;
      double radius = 0.13 + server.random.nextDouble() * 0.1;
      server.sendParticles(
          ModParticleType.HEX_MOTES.get(),
          x + Math.cos(angle) * radius,
          y + (server.random.nextDouble() - 0.5) * 0.18,
          z + Math.sin(angle) * radius,
          0,
          -Math.sin(angle) * 0.006 + (center.getX() + 0.5 - x) * 0.003,
          0.002,
          Math.cos(angle) * 0.006 + (center.getZ() + 0.5 - z) * 0.003,
          1.0);
      if (ticksRemaining > 24 && server.random.nextInt(3) == 0) {
        double dx = center.getX() - offeringOrigin.getX();
        double dz = center.getZ() - offeringOrigin.getZ();
        double distance = Math.max(1.0, Math.sqrt(dx * dx + dz * dz));
        double trail = 0.16 + server.random.nextDouble() * 0.12;
        server.sendParticles(
            ModParticleType.LEAVES.get(),
            x - dx / distance * trail + Math.cos(angle) * 0.13,
            y - 0.12 + (server.random.nextDouble() - 0.5) * 0.16,
            z - dz / distance * trail + Math.sin(angle) * 0.13,
            0,
            dx / distance * 0.006 - Math.sin(angle) * 0.004,
            0.003 + server.random.nextDouble() * 0.004,
            dz / distance * 0.006 + Math.cos(angle) * 0.004,
            1.0);
      }
    }
  }

  private static void spawnNatureLeaves(
      ServerLevel server, BlockPos source, double height, int count) {
    for (int index = 0; index < count; index++) {
      server.sendParticles(
          ModParticleType.LEAVES.get(),
          source.getX() + 0.5 + (server.random.nextDouble() - 0.5) * 0.35,
          source.getY() + height + server.random.nextDouble() * 0.2,
          source.getZ() + 0.5 + (server.random.nextDouble() - 0.5) * 0.35,
          0,
          (server.random.nextDouble() - 0.5) * 0.024,
          0.012 + server.random.nextDouble() * 0.014,
          (server.random.nextDouble() - 0.5) * 0.024,
          1.0);
    }
  }
}
