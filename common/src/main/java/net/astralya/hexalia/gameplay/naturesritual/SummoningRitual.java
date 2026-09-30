package net.astralya.hexalia.gameplay.naturesritual;

import dev.architectury.event.EventResult;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.astralya.hexalia.Hexalia;
import net.astralya.hexalia.block.entity.custom.RitualTableBlockEntity;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.particle.ModParticleTypes;
import net.astralya.hexalia.recipe.NaturesRitualRecipe;
import net.astralya.hexalia.sound.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class SummoningRitual {
  private static final int CAPTURE_RADIUS = 8;
  private static final double GLYPH_HEIGHT = 1.9;

  private SummoningRitual() {}

  public static void onStart(ServerLevel server, BlockPos pos) {
    server.sendParticles(
        ModParticleTypes.SUMMONING_GLYPH.get(),
        pos.getX() + 0.5, pos.getY() + GLYPH_HEIGHT, pos.getZ() + 0.5,
        0, 0.0, 0.0, 0.0, 1.0);
  }

  public static void onTick(ServerLevel server, BlockPos pos, int elapsed, int ticksRemaining) {
    if (elapsed > 0 && elapsed % 52 == 0 && ticksRemaining > 30)
      server.sendParticles(
          ModParticleTypes.SUMMONING_GLYPH.get(),
          pos.getX() + 0.5, pos.getY() + GLYPH_HEIGHT, pos.getZ() + 0.5,
          0, 0.0, -0.01, 0.0, 1.0);
  }

  public static boolean canBind(Level level, RitualTableBlockEntity table) {
    return level instanceof ServerLevel && table.isAwaitingSoul();
  }

  public static void onPrepared(ServerLevel server, BlockPos pos) {
    server.sendParticles(
        ModParticleTypes.SUMMONING_GLYPH.get(),
        pos.getX() + 0.5, pos.getY() + GLYPH_HEIGHT, pos.getZ() + 0.5,
        0, 0.0, -0.01, 0.0, 1.0);
    server.playSound(null, pos, ModSoundEvents.RITUAL_WHISPERS.get(), SoundSource.BLOCKS, 0.7F, 1.0F);
  }

  public static boolean spawnEntities(ServerLevel server, BlockPos pos, ResourceLocation recipeId) {
    var holder = server.getRecipeManager().byKey(recipeId);
    if (holder.isEmpty() || !(holder.get().value() instanceof NaturesRitualRecipe recipe)) {
      manifestationFailed(server, pos, "recipe could not be resolved");
      return false;
    }
    var result = recipe.entityResult();
    if (result.isEmpty()) {
      manifestationFailed(server, pos, "recipe no longer has an entity result");
      return false;
    }
    NaturesRitualRecipe.EntityResult entityResult = result.get();
    EntityType<?> entityType =
        BuiltInRegistries.ENTITY_TYPE.getOptional(entityResult.entity()).orElse(null);
    if (entityType == null || entityResult.count() < 1) {
      manifestationFailed(server, pos, "entity result is invalid");
      return false;
    }

    List<Entity> entities = new ArrayList<>(entityResult.count());
    for (int index = 0; index < entityResult.count(); index++) {
      Entity entity = entityType.create(server);
      if (entity == null) {
        entities.forEach(Entity::discard);
        manifestationFailed(server, pos, "entity could not be created");
        return false;
      }
      double angle = entityResult.count() == 1 ? 0.0 : Math.PI * 2.0 * index / entityResult.count();
      double radius = entityResult.count() == 1 ? 0.0 : 0.8;
      entity.moveTo(
          pos.getX() + 0.5 + Math.cos(angle) * radius,
          pos.getY() + 1.0,
          pos.getZ() + 0.5 + Math.sin(angle) * radius,
          server.random.nextFloat() * 360.0F,
          0.0F);
      if (entity instanceof Mob mob) {
        mob.finalizeSpawn(
            server,
            server.getCurrentDifficultyAt(entity.blockPosition()),
            MobSpawnType.MOB_SUMMONED,
            null);
      }
      entities.add(entity);
    }

    List<Entity> added = new ArrayList<>();
    for (Entity entity : entities) {
      if (!server.addFreshEntity(entity)) {
        added.forEach(Entity::discard);
        entities.stream().filter(value -> !added.contains(value)).forEach(Entity::discard);
        manifestationFailed(server, pos, "entity could not be added to the world");
        return false;
      }
      added.add(entity);
    }
    return true;
  }

  public static void onComplete(ServerLevel server, BlockPos pos) {
    server.sendParticles(
        ModParticleTypes.HEX_MOTES.get(),
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

  public static void manifestationFailed(Level level, BlockPos pos, String reason) {
    Hexalia.LOGGER.warn("Nature's Ritual manifestation at {} failed: {}", pos, reason);
    if (level instanceof ServerLevel server)
      server.sendParticles(
          ParticleTypes.SMOKE,
          pos.getX() + 0.5,
          pos.getY() + 1.0,
          pos.getZ() + 0.5,
          8, 0.25, 0.2, 0.25, 0.01);
  }

  public static EventResult onLivingDeath(LivingEntity slain, DamageSource source) {
    if (!(slain.level() instanceof ServerLevel level)
        || slain instanceof Player
        || !(source.getEntity() instanceof Player)
        || !isAthame(source.getWeaponItem())) {
      return EventResult.pass();
    }

    BlockPos origin = slain.blockPosition();
    List<Candidate> candidates = new ArrayList<>();
    BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
    for (int x = -CAPTURE_RADIUS; x <= CAPTURE_RADIUS; x++) {
      for (int y = -CAPTURE_RADIUS; y <= CAPTURE_RADIUS; y++) {
        for (int z = -CAPTURE_RADIUS; z <= CAPTURE_RADIUS; z++) {
          cursor.setWithOffset(origin, x, y, z);
          double distance = origin.distSqr(cursor);
          if (distance <= CAPTURE_RADIUS * CAPTURE_RADIUS
              && level.getBlockEntity(cursor) instanceof RitualTableBlockEntity table
              && table.isAwaitingSoul()) {
            candidates.add(new Candidate(cursor.immutable(), table, distance));
          }
        }
      }
    }
    candidates.sort(
        Comparator.comparingDouble(Candidate::distance)
            .thenComparingLong(candidate -> candidate.pos().asLong()));
    for (Candidate candidate : candidates) {
      if (candidate.table().tryCaptureSoul(origin)) {
        level.sendParticles(
            ParticleTypes.SOUL,
            slain.getX(),
            slain.getY() + slain.getBbHeight() * 0.5,
            slain.getZ(),
            8,
            0.3,
            0.35,
            0.3,
            0.02);
        break;
      }
    }
    return EventResult.pass();
  }

  private static boolean isAthame(ItemStack weapon) {
    return weapon != null && weapon.is(ModItems.ATHAME.get());
  }

  private record Candidate(BlockPos pos, RitualTableBlockEntity table, double distance) {}

  public static void spawnAwaitingSoulParticles(ServerLevel server, BlockPos center) {
    long gameTime = server.getGameTime();
    if (gameTime % 52 == 0)
      server.sendParticles(
          ModParticleTypes.SUMMONING_GLYPH.get(),
          center.getX() + 0.5, center.getY() + GLYPH_HEIGHT, center.getZ() + 0.5,
          0, 0.0, -0.01, 0.0, 1.0);
    if (gameTime % 16 == 0) {
      double angle = server.random.nextDouble() * Math.PI * 2.0;
      double radius = 1.0 + server.random.nextDouble() * 1.5;
      NaturesRitual.sendConvergingParticle(
          server,
          center,
          center.getX() + 0.5 + Math.cos(angle) * radius,
          center.getY() + 0.25 + server.random.nextDouble() * 0.35,
          center.getZ() + 0.5 + Math.sin(angle) * radius,
          0.006);
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
        ModParticleTypes.SUMMONING_GLYPH.get(),
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
            ModParticleTypes.HEX_MOTES.get(),
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
          ModParticleTypes.SUMMONING_GLYPH.get(),
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
            ModParticleTypes.HEX_MOTES.get(),
            center.getX() + 0.5 + offsetX,
            center.getY() + 0.9 + server.random.nextDouble() * 0.3,
            center.getZ() + 0.5 + offsetZ,
            0, -offsetX * 0.035, 0.005, -offsetZ * 0.035, 1.0);
      }
    }
  }
}
