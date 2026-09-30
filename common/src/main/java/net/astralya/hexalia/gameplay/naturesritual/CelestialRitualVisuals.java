package net.astralya.hexalia.gameplay.naturesritual;

import java.util.List;
import net.astralya.hexalia.block.custom.CelestialBloomBlock;
import net.astralya.hexalia.particle.ModParticleType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

public final class CelestialRitualVisuals {
  private static final double GLYPH_HEIGHT = 1.9;

  private CelestialRitualVisuals() {}

  public static void onStart(ServerLevel server, BlockPos pos) {
    server.sendParticles(
        ModParticleType.CELESTIAL_GLYPH.get(),
        pos.getX() + 0.5, pos.getY() + GLYPH_HEIGHT, pos.getZ() + 0.5,
        0, 0.0, 0.0, 0.0, 1.0);
    for (int index = 0; index < 4; index++) {
      double angle = index * Math.PI * 0.5 + server.random.nextDouble() * 0.3;
      double radius = 0.3 + server.random.nextDouble() * 0.3;
      server.sendParticles(
          ModParticleType.SPARKLE.get(),
          pos.getX() + 0.5 + Math.cos(angle) * radius,
          pos.getY() + 0.8 + server.random.nextDouble() * 0.4,
          pos.getZ() + 0.5 + Math.sin(angle) * radius,
          1, 0.0, 0.0, 0.0, 0.0);
    }
    server.playSound(
        null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 0.45F, 1.2F);
    
  }

  public static void onTick(
      ServerLevel server, BlockPos pos, List<BlockPos> energyPositions,
      int elapsed, int ticksRemaining, boolean hasOutput, boolean requiresSoul) {
    spawnCelestialEnergy(server, pos, energyPositions, elapsed);
    if (elapsed > 0 && elapsed % 52 == 0 && ticksRemaining > 30)
      server.sendParticles(
          ModParticleType.CELESTIAL_GLYPH.get(),
          pos.getX() + 0.5, pos.getY() + GLYPH_HEIGHT, pos.getZ() + 0.5,
          0, 0.0, -0.01, 0.0, 1.0);
    if (ticksRemaining == 6 && hasOutput) {
      server.sendParticles(
          ModParticleType.CELESTIAL_GLYPH.get(),
          pos.getX() + 0.5, pos.getY() + GLYPH_HEIGHT, pos.getZ() + 0.5,
          0, 0.0, 0.01, 0.0, 1.0);
      spawnCelestialTableSparkles(server, pos, 5);
    } else if (ticksRemaining < 6
        && ticksRemaining > 0
        && ticksRemaining % 2 == 0
        && hasOutput) {
      spawnCelestialTableSparkles(server, pos, 1);
    }
    if (!requiresSoul && ticksRemaining <= 6)
      for (int index = 0; index < 3; index++) {
        double angle = elapsed * 0.4 + index * Math.PI * 2.0 / 3.0;
        double radius = 0.5 - (6 - ticksRemaining) * 0.06;
        server.sendParticles(ModParticleType.HEX_MOTES.get(),
            pos.getX() + 0.5 + Math.cos(angle) * radius,
            pos.getY() + 1.05,
            pos.getZ() + 0.5 + Math.sin(angle) * radius,
            0, -Math.cos(angle) * 0.01, 0.005, -Math.sin(angle) * 0.01, 1.0);
      }
  }

  public static void onComplete(Level level, BlockPos pos) {
    level.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 0.8F, 1.25F);
    if (level instanceof ServerLevel server) {
      server.sendParticles(
          ModParticleType.HEX_MOTES.get(),
          pos.getX() + 0.5,
          pos.getY() + 1.0,
          pos.getZ() + 0.5,
          8,
          0.3,
          0.3,
          0.3,
          0.0);
      server.sendParticles(
          ModParticleType.SPARKLE.get(),
          pos.getX() + 0.5,
          pos.getY() + 1.1,
          pos.getZ() + 0.5,
          4,
          0.3,
          0.25,
          0.3,
          0.04);
    }
  }

  public static void spawnCelestialEnergy(
      ServerLevel server, BlockPos center, List<BlockPos> energyPositions, int elapsed) {
    if (energyPositions.isEmpty()) return;
    if (elapsed % 7 == 0) {
      for (BlockPos bloom : energyPositions) {
        if (server.getBlockState(bloom).hasProperty(CelestialBloomBlock.OPEN)
            && server.getBlockState(bloom).getValue(CelestialBloomBlock.OPEN)
            && server.random.nextInt(4) == 0) {
          server.sendParticles(
              ModParticleType.SPARKLE.get(),
              bloom.getX() + 0.3 + server.random.nextDouble() * 0.4,
              bloom.getY() + 0.55 + server.random.nextDouble() * 0.35,
              bloom.getZ() + 0.3 + server.random.nextDouble() * 0.4,
              1, 0.0, 0.0, 0.0, 0.0);
        }
      }
    }
    BlockPos bloom = energyPositions.get((elapsed / 16) % energyPositions.size());
    if (server.getBlockState(bloom).hasProperty(CelestialBloomBlock.OPEN)
        && server.getBlockState(bloom).getValue(CelestialBloomBlock.OPEN)
        && (elapsed % 16 == 0 || elapsed % 16 == 8)) {
      server.sendParticles(
          ModParticleType.SPARKLE.get(),
          bloom.getX() + 0.4 + server.random.nextDouble() * 0.2,
          bloom.getY() + 0.6 + server.random.nextDouble() * 0.25,
          bloom.getZ() + 0.4 + server.random.nextDouble() * 0.2,
          1, 0.0, 0.0, 0.0, 0.0);
    }
    double progress = (elapsed % 16) / 15.0;
    double dx = center.getX() - bloom.getX();
    double dz = center.getZ() - bloom.getZ();
    double bend = Math.sin(progress * Math.PI) * 0.35 / Math.max(1.0, Math.sqrt(dx * dx + dz * dz));
    server.sendParticles(
        ModParticleType.HEX_MOTES.get(),
        bloom.getX() + 0.5 + dx * progress - dz * bend,
        bloom.getY() + 0.75 + 0.3 * progress + Math.sin(progress * Math.PI) * 0.2,
        bloom.getZ() + 0.5 + dz * progress + dx * bend,
        1, 0.01, 0.01, 0.01, 0.0);
  }

  public static void spawnCelestialTableSparkles(ServerLevel server, BlockPos center, int count) {
    for (int index = 0; index < count; index++) {
      double angle = server.random.nextDouble() * Math.PI * 2.0;
      double radius = 0.2 + server.random.nextDouble() * 0.45;
      server.sendParticles(
          ModParticleType.SPARKLE.get(),
          center.getX() + 0.5 + Math.cos(angle) * radius,
          center.getY() + 0.9 + server.random.nextDouble() * 0.4,
          center.getZ() + 0.5 + Math.sin(angle) * radius,
          1, 0.0, 0.0, 0.0, 0.0);
    }
  }
}
