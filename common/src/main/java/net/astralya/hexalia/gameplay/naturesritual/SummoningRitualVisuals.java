package net.astralya.hexalia.gameplay.naturesritual;

import net.astralya.hexalia.particle.ModParticleType;
import net.astralya.hexalia.sound.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public final class SummoningRitualVisuals {
  private static final double GLYPH_HEIGHT = 1.9;

  private SummoningRitualVisuals() {}

  public static void onStart(ServerLevel server, BlockPos pos) {
    server.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE,
        SoundSource.BLOCKS, 0.4F, 1.0F);
    server.sendParticles(
        ModParticleType.SUMMONING_GLYPH.get(),
        pos.getX() + 0.5, pos.getY() + GLYPH_HEIGHT, pos.getZ() + 0.5,
        0, 0.0, 0.0, 0.0, 1.0);
  }

  public static void onTick(ServerLevel server, BlockPos pos, int elapsed, int ticksRemaining) {
    if (elapsed > 0 && elapsed % 52 == 0 && ticksRemaining > 30)
      server.sendParticles(
          ModParticleType.SUMMONING_GLYPH.get(),
          pos.getX() + 0.5, pos.getY() + GLYPH_HEIGHT, pos.getZ() + 0.5,
          0, 0.0, -0.01, 0.0, 1.0);
  }

  public static void onPrepared(ServerLevel server, BlockPos pos) {
    server.sendParticles(
        ModParticleType.SUMMONING_GLYPH.get(),
        pos.getX() + 0.5, pos.getY() + GLYPH_HEIGHT, pos.getZ() + 0.5,
        0, 0.0, -0.01, 0.0, 1.0);
    server.playSound(null, pos, ModSoundEvents.RITUAL_WHISPERS.get(), SoundSource.BLOCKS, 0.7F, 1.0F);
  }

  public static void onComplete(ServerLevel server, BlockPos pos) {
    server.sendParticles(
        ModParticleType.HEX_MOTES.get(),
        pos.getX() + 0.5,
        pos.getY() + 1.1,
        pos.getZ() + 0.5,
        8,
        0.22,
        0.18,
        0.22,
        0.025);
    server.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 0.8F, 0.85F);
  }

  public static void spawnAwaitingSoulParticles(ServerLevel server, BlockPos center) {
    long gameTime = server.getGameTime();
    if (gameTime % 52 == 0)
      server.sendParticles(
          ModParticleType.SUMMONING_GLYPH.get(),
          center.getX() + 0.5, center.getY() + GLYPH_HEIGHT, center.getZ() + 0.5,
          0, 0.0, -0.01, 0.0, 1.0);
    if (gameTime % 16 == 0) {
      double angle = server.random.nextDouble() * Math.PI * 2.0;
      double radius = 1.0 + server.random.nextDouble() * 1.5;
      server.sendParticles(ModParticleType.HEX_MOTES.get(),
          center.getX() + 0.5 + Math.cos(angle) * radius,
          center.getY() + 0.85 + server.random.nextDouble() * 0.2,
          center.getZ() + 0.5 + Math.sin(angle) * radius,
          0, -Math.cos(angle) * radius * 0.006, 0.005,
          -Math.sin(angle) * radius * 0.006, 1.0);
    }
    if (gameTime % 28 == 0) {
      double angle = server.random.nextDouble() * Math.PI * 2.0;
      double radius = 0.45 + server.random.nextDouble() * 0.45;
      server.sendParticles(
          ParticleTypes.SOUL,
          center.getX() + 0.5 + Math.cos(angle) * radius,
          center.getY() + 0.85 + server.random.nextDouble() * 0.3,
          center.getZ() + 0.5 + Math.sin(angle) * radius,
          0, 0.0, 0.012, 0.0, 1.0);
    }
  }

  public static void spawnSoulBindingParticles(ServerLevel server, BlockPos center) {
    server.sendParticles(
        ModParticleType.SUMMONING_GLYPH.get(),
        center.getX() + 0.5, center.getY() + GLYPH_HEIGHT, center.getZ() + 0.5,
        0, 0.0, 0.01, 0.0, 1.0);
    for (int index = 0; index < 6; index++) {
      double angle = index * Math.PI / 3.0;
      double radius = 0.65 + server.random.nextDouble() * 0.25;
      double offsetX = Math.cos(angle) * radius;
      double offsetZ = Math.sin(angle) * radius;
      server.sendParticles(
          ParticleTypes.SOUL,
          center.getX() + 0.5 + offsetX,
          center.getY() + 1.0 + server.random.nextDouble() * 0.25,
          center.getZ() + 0.5 + offsetZ,
          0, -offsetX * 0.055, 0.005, -offsetZ * 0.055, 1.0);
      if (index % 2 == 0)
        server.sendParticles(
            ModParticleType.HEX_MOTES.get(),
            center.getX() + 0.5 + offsetX,
            center.getY() + 1.05,
            center.getZ() + 0.5 + offsetZ,
            0, -offsetX * 0.08, 0.005, -offsetZ * 0.08, 1.0);
    }
  }

  public static void spawnManifestationParticles(
      ServerLevel server, BlockPos center, BlockPos capturedSoulOrigin, int ticksRemaining, int duration) {
    int elapsed = duration - ticksRemaining;
    float progress = Math.min(1.0F, elapsed / (float) duration);

    if (ticksRemaining == 6)
      server.sendParticles(
          ModParticleType.SUMMONING_GLYPH.get(),
          center.getX() + 0.5, center.getY() + GLYPH_HEIGHT, center.getZ() + 0.5,
          0, 0.0, 0.01, 0.0, 1.0);

    if (capturedSoulOrigin != null && elapsed < 12 && elapsed % 2 == 0) {
      double pathProgress = (elapsed + 1.0) / 12.0;
      double sourceX = capturedSoulOrigin.getX() + 0.5;
      double sourceY = capturedSoulOrigin.getY() + 0.75;
      double sourceZ = capturedSoulOrigin.getZ() + 0.5;
      double x = sourceX + (center.getX() + 0.5 - sourceX) * pathProgress;
      double y =
          sourceY
              + (center.getY() + 1.1 - sourceY) * pathProgress
              + Math.sin(pathProgress * Math.PI) * 0.65;
      double z = sourceZ + (center.getZ() + 0.5 - sourceZ) * pathProgress;
      server.sendParticles(
          ParticleTypes.SOUL,
          x + (server.random.nextDouble() - 0.5) * 0.18,
          y + (server.random.nextDouble() - 0.5) * 0.12,
          z + (server.random.nextDouble() - 0.5) * 0.18,
          1, 0.0, 0.0, 0.0, 0.0);
    }

    if (elapsed % 6 == 0) {
      double angle = elapsed * 0.42;
      double radius = 0.95 - progress * 0.65;
      double offsetX = Math.cos(angle) * radius;
      double offsetZ = Math.sin(angle) * radius;
      server.sendParticles(
          ParticleTypes.SOUL,
          center.getX() + 0.5 + offsetX,
          center.getY() + 1.25 - progress * 0.2,
          center.getZ() + 0.5 + offsetZ,
          0, -offsetX * 0.025, -0.004, -offsetZ * 0.025, 1.0);
    }

    int naturalInterval = 5 - (int) (progress * 3.0F);
    if (elapsed % naturalInterval == 0) {
      int count = progress >= 0.6F ? 3 : progress >= 0.25F ? 2 : 1;
      for (int index = 0; index < count; index++) {
        double angle = server.random.nextDouble() * Math.PI * 2.0;
        double radius = 0.4 + (1.0 - progress) * (1.0 + server.random.nextDouble() * 1.5);
        double offsetX = Math.cos(angle) * radius;
        double offsetZ = Math.sin(angle) * radius;
        server.sendParticles(
            ModParticleType.HEX_MOTES.get(),
            center.getX() + 0.5 + offsetX,
            center.getY() + 0.9 + server.random.nextDouble() * 0.3,
            center.getZ() + 0.5 + offsetZ,
            0, -offsetX * 0.035, 0.005, -offsetZ * 0.035, 1.0);
      }
    }
  }
}
