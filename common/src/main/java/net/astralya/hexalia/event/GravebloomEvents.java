package net.astralya.hexalia.event;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.astralya.hexalia.effect.ModMobEffects;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class GravebloomEvents {
  private static final int PATCH_RADIUS = 2;

  private GravebloomEvents() {}

  public static void register() {
    EntityEvent.LIVING_DEATH.register(GravebloomEvents::onLivingDeath);
  }

  private static EventResult onLivingDeath(LivingEntity slain, DamageSource source) {
    if (!(slain.level() instanceof ServerLevel level)
        || !(slain instanceof Monster)
        || !(source.getEntity() instanceof Player player)
        || !player.hasEffect(
            BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModMobEffects.GRAVEBLOOM.get()))) {
      return EventResult.pass();
    }

    bloom(level, slain.blockPosition());
    return EventResult.pass();
  }

  private static void bloom(ServerLevel level, BlockPos origin) {
    RandomSource random = level.getRandom();
    List<BlockPos> candidates = new ArrayList<>();
    for (int x = -PATCH_RADIUS; x <= PATCH_RADIUS; x++) {
      for (int z = -PATCH_RADIUS; z <= PATCH_RADIUS; z++) {
        if (Math.abs(x) == PATCH_RADIUS && Math.abs(z) == PATCH_RADIUS) {
          continue;
        }
        BlockPos ground = findGround(level, origin.offset(x, 0, z));
        if (ground != null) {
          candidates.add(ground);
        }
      }
    }
    Collections.shuffle(candidates, new Random(random.nextLong()));

    List<BlockPos> moss = new ArrayList<>();
    for (BlockPos pos : candidates) {
      double distance = Math.sqrt(origin.distSqr(pos));
      float chance = distance < 1.5 ? 0.9F : 0.55F;
      if (random.nextFloat() < chance
          && level.setBlock(pos, Blocks.MOSS_BLOCK.defaultBlockState(), 3)) {
        moss.add(pos.immutable());
      }
    }

    int plants = placeSprouts(level, moss, 2 + random.nextInt(3));
    if (!moss.isEmpty() || plants > 0) {
      level.sendParticles(
          ParticleTypes.FALLING_SPORE_BLOSSOM,
          origin.getX() + 0.5,
          origin.getY() + 0.8,
          origin.getZ() + 0.5,
          8 + random.nextInt(8),
          1.35,
          0.45,
          1.35,
          0.01);
      level.playSound(
          null,
          origin,
          SoundEvents.MOSS_PLACE,
          SoundSource.BLOCKS,
          0.55F,
          0.9F + random.nextFloat() * 0.2F);
    }
  }

  private static BlockPos findGround(ServerLevel level, BlockPos column) {
    for (int yOffset = 1; yOffset >= -2; yOffset--) {
      BlockPos pos = column.offset(0, yOffset, 0);
      if (level.getBlockState(pos).is(BlockTags.MOSS_REPLACEABLE)
          && level.getBlockEntity(pos) == null
          && level.getBlockState(pos.above()).getFluidState().isEmpty()
          && level.getBlockState(pos.above()).canBeReplaced()) {
        return pos;
      }
    }
    return null;
  }

  private static int placeSprouts(ServerLevel level, List<BlockPos> moss, int targetCount) {
    if (moss.isEmpty()) {
      return 0;
    }
    HolderSet.Named<Block> gravebloomPlants =
        level
            .registryAccess()
            .registryOrThrow(Registries.BLOCK)
            .getTag(ModTags.Blocks.GRAVEBLOOM_PLANTS)
            .orElse(null);
    if (gravebloomPlants == null || gravebloomPlants.size() == 0) {
      return 0;
    }

    Set<BlockPos> used = new HashSet<>();
    List<Holder<Block>> herbPlants = new ArrayList<>();
    gravebloomPlants.forEach(
        plant -> {
          if (plant.is(ModTags.Blocks.HERBS)) {
            herbPlants.add(plant);
          }
        });
    if (herbPlants.isEmpty()) {
      return 0;
    }
    if (!tryPlaceSprout(level, moss, herbPlants, used)) {
      return 0;
    }
    List<Holder<Block>> plants = new ArrayList<>();
    gravebloomPlants.forEach(plants::add);
    int placed = 1;
    while (placed < targetCount && tryPlaceSprout(level, moss, plants, used)) {
      placed++;
    }
    return placed;
  }

  private static boolean tryPlaceSprout(
      ServerLevel level, List<BlockPos> moss, List<Holder<Block>> pool, Set<BlockPos> used) {
    Random random = new Random(level.getRandom().nextLong());
    List<BlockPos> targets = new ArrayList<>(moss);
    List<Holder<Block>> plants = new ArrayList<>(pool);
    Collections.shuffle(targets, random);
    Collections.shuffle(plants, random);
    for (BlockPos ground : targets) {
      BlockPos target = ground.above();
      if (!used.contains(target) && level.getBlockState(target).canBeReplaced()) {
        for (Holder<Block> selected : plants) {
          BlockState state = selected.value().defaultBlockState();
          if (state.canSurvive(level, target) && level.setBlock(target, state, 3)) {
            used.add(target);
            return true;
          }
        }
      }
    }
    return false;
  }
}
