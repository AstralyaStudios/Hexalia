package net.astralya.hexalia.gameplay.censer.effects;

import java.util.ArrayList;
import java.util.List;
import net.astralya.hexalia.Configuration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class TidesMemoryEffect implements ICenserEffect {

  private static final int PULSE_INTERVAL = 32;
  private static final float GUARDIAN_CHANCE = 0.10F;

  private record LootEntry(ItemStack stack, int weight) {}

  private static final List<LootEntry> LOOT_TABLE =
      List.of(
          new LootEntry(new ItemStack(Items.COD, 2), 40),
          new LootEntry(new ItemStack(Items.SALMON), 30),
          new LootEntry(new ItemStack(Items.KELP, 3), 35),
          new LootEntry(new ItemStack(Items.SEAGRASS, 2), 30),
          new LootEntry(new ItemStack(Items.NAUTILUS_SHELL), 12),
          new LootEntry(new ItemStack(Items.PRISMARINE_SHARD, 2), 15),
          new LootEntry(new ItemStack(Items.PRISMARINE_CRYSTALS), 12),
          new LootEntry(new ItemStack(Items.SCUTE), 8));

  private static final int TOTAL_WEIGHT = LOOT_TABLE.stream().mapToInt(LootEntry::weight).sum();

  private int tickCounter = 0;

  @Override
  public String getMessageKey() {
    return "message.hexalia.censer.tides_memory";
  }

  @Override
  public void onTick(ServerLevel level, BlockPos pos) {
    spawnAmbientParticles(level, pos);

    this.tickCounter++;
    if (this.tickCounter < PULSE_INTERVAL) {
      return;
    }
    this.tickCounter = 0;

    BlockPos waterSurface = findWaterSurface(level, pos);
    if (waterSurface != null) {
      spawnLoot(level, waterSurface);
      spawnSurfaceParticles(level, waterSurface);
      level.playSound(
          null,
          waterSurface,
          SoundEvents.FISHING_BOBBER_SPLASH,
          SoundSource.BLOCKS,
          0.5F,
          0.8F + level.random.nextFloat() * 0.4F);
    }

    if (level.random.nextFloat() < GUARDIAN_CHANCE) {
      trySpawnGuardian(level, pos);
    }
  }

  private static BlockPos findWaterSurface(ServerLevel level, BlockPos center) {
    int radius = Configuration.CENSER_EFFECT_RADIUS.get();
    List<BlockPos> surfaces = new ArrayList<>();
    BlockPos min = new BlockPos(center.getX() - radius, center.getY() - 4, center.getZ() - radius);
    BlockPos max = new BlockPos(center.getX() + radius, center.getY() + 4, center.getZ() + radius);

    for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
      if (level.getBlockState(pos).is(Blocks.WATER) && level.getBlockState(pos.above()).isAir()) {
        surfaces.add(pos.immutable());
      }
    }

    if (surfaces.isEmpty()) {
      return null;
    }

    return surfaces.get(level.random.nextInt(surfaces.size()));
  }

  private static void spawnLoot(ServerLevel level, BlockPos surface) {
    ItemStack drop = rollLoot(level);
    double x = surface.getX() + 0.5D + (level.random.nextDouble() - 0.5D) * 0.4D;
    double y = surface.getY() + 0.8D;
    double z = surface.getZ() + 0.5D + (level.random.nextDouble() - 0.5D) * 0.4D;

    ItemEntity itemEntity = new ItemEntity(level, x, y, z, drop);
    itemEntity.setDeltaMovement(0.0D, 0.2D + level.random.nextDouble() * 0.1D, 0.0D);
    level.addFreshEntity(itemEntity);
  }

  private static ItemStack rollLoot(ServerLevel level) {
    int roll = level.random.nextInt(TOTAL_WEIGHT);
    int cumulative = 0;

    for (LootEntry entry : LOOT_TABLE) {
      cumulative += entry.weight();
      if (roll < cumulative) {
        return entry.stack().copy();
      }
    }

    return new ItemStack(Items.COD);
  }

  private static void trySpawnGuardian(ServerLevel level, BlockPos center) {
    int radius = Configuration.CENSER_EFFECT_RADIUS.get();
    List<BlockPos> waterBlocks = new ArrayList<>();
    BlockPos min = new BlockPos(center.getX() - radius, center.getY() - 4, center.getZ() - radius);
    BlockPos max = new BlockPos(center.getX() + radius, center.getY() + 4, center.getZ() + radius);

    for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
      if (level.getBlockState(pos).is(Blocks.WATER)) {
        waterBlocks.add(pos.immutable());
      }
    }

    if (waterBlocks.isEmpty()) {
      return;
    }

    BlockPos spawnPos = waterBlocks.get(level.random.nextInt(waterBlocks.size()));
    Guardian guardian = new Guardian(EntityType.GUARDIAN, level);
    guardian.moveTo(Vec3.atCenterOf(spawnPos));
    guardian.finalizeSpawn(
        level, level.getCurrentDifficultyAt(spawnPos), MobSpawnType.TRIGGERED, null, null);
    level.addFreshEntity(guardian);
    level.playSound(null, spawnPos, SoundEvents.GUARDIAN_AMBIENT, SoundSource.HOSTILE, 1.0F, 1.0F);
  }

  private static void spawnAmbientParticles(ServerLevel level, BlockPos pos) {
    int radius = Configuration.CENSER_EFFECT_RADIUS.get();

    for (int i = 0; i < 2; i++) {
      double x = pos.getX() + (level.random.nextDouble() * 2.0D - 1.0D) * radius;
      double y = pos.getY() + level.random.nextDouble() * 1.5D;
      double z = pos.getZ() + (level.random.nextDouble() * 2.0D - 1.0D) * radius;
      level.sendParticles(ParticleTypes.DRIPPING_WATER, x, y, z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
    }
  }

  private static void spawnSurfaceParticles(ServerLevel level, BlockPos surface) {
    level.sendParticles(
        ParticleTypes.SPLASH,
        surface.getX() + 0.5D,
        surface.getY() + 1.0D,
        surface.getZ() + 0.5D,
        12,
        0.3D,
        0.1D,
        0.3D,
        0.1D);
    level.sendParticles(
        ParticleTypes.FISHING,
        surface.getX() + 0.5D,
        surface.getY() + 1.0D,
        surface.getZ() + 0.5D,
        6,
        0.2D,
        0.1D,
        0.2D,
        0.05D);
  }
}
