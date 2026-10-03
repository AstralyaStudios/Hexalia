package net.astralya.hexalia.gameplay.naturesritual;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.astralya.hexalia.block.custom.RitualBrazierBlock;
import net.astralya.hexalia.block.entity.custom.RitualBrazierBlockEntity;
import net.astralya.hexalia.block.entity.custom.RitualTableBlockEntity;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.particle.ModParticleTypes;
import net.astralya.hexalia.recipe.ModRecipeTypes;
import net.astralya.hexalia.recipe.NaturesRitualRecipe;
import net.astralya.hexalia.recipe.NaturesRitualRecipeInput;
import net.astralya.hexalia.util.ItemInteractionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public final class NaturesRitual {
  private NaturesRitual() {}

  public static ItemInteractionResult useItemOn(
      Level level,
      BlockPos pos,
      Player player,
      InteractionHand hand,
      RitualTableBlockEntity table) {
    ItemStack heldStack = player.getItemInHand(hand);
    ItemStack offhandStack = player.getOffhandItem();
    ItemStack focusStack =
        heldStack.is(ModItems.HEX_FOCUS.get())
            ? heldStack
            : offhandStack.is(ModItems.HEX_FOCUS.get()) ? offhandStack : ItemStack.EMPTY;

    if (!focusStack.isEmpty() && tryStart(level, pos, player, table)) {
      return ItemInteractionResult.SUCCESS;
    }

    return ItemInteractionHelper.tryHandleSingleItem(
        level, pos, player, hand, table, item -> !item.is(ModItems.HEX_FOCUS.get()));
  }

  public static boolean tryStart(
      Level level, BlockPos pos, Player player, RitualTableBlockEntity table) {
    if (!(level instanceof ServerLevel server)) return true;
    if (table.isRitualActive()) return true;
    ItemStack tableItem = table.getItem(0);
    if (tableItem.isEmpty()) {
      fail(level, pos, player, "message.hexalia.natures_ritual.missing_ingredients");
      return true;
    }

    Match match = findMatch(level, pos, tableItem, table);
    if (match == null) {
      fail(level, pos, player, "message.hexalia.natures_ritual.wrong_recipe");
      return true;
    }

    for (RitualBrazierBlockEntity brazier : match.usedBraziers) {
      BlockState brazierState = level.getBlockState(brazier.getBlockPos());
      if (!brazierState.hasProperty(RitualBrazierBlock.SALTED)
          || !brazierState.getValue(RitualBrazierBlock.SALTED)) {
        fail(level, pos, player, "message.hexalia.natures_ritual.missing_salt");
        return true;
      }
    }

    NaturesRitualRecipe.RitualKind kind = match.recipe.ritualKind();
    if (kind == NaturesRitualRecipe.RitualKind.CELESTIAL && !CelestialRitual.isNightTime(server)) {
      fail(level, pos, player, "message.hexalia.celestial_infusion.requires_night");
      return true;
    }
    int requirement = match.recipe.resolvedEnergyCost();
    List<BlockPos> energy =
        kind == NaturesRitualRecipe.RitualKind.CELESTIAL
            ? CelestialRitual.findCelestialBlooms(level, pos, requirement)
            : NatureRitual.findFullyGrownCrops(level, pos, requirement, 8);
    if (energy.size() < requirement) {
      fail(
          level,
          pos,
          player,
          kind == NaturesRitualRecipe.RitualKind.CELESTIAL
              ? "message.hexalia.celestial_infusion.no_celestial_blooms"
              : "message.hexalia.natures_ritual.invalid_crops");
      return true;
    }

    table.startTransformation(
        match.recipe.getResultItem(level.registryAccess()).copy(),
        match.usedBraziers.size() * 50
            + (kind == NaturesRitualRecipe.RitualKind.NATURE
                ? Math.max(24, energy.size() * 3 + 12) + 6
                : 0),
        match.usedBraziers,
        match.id,
        match.recipe.requiresSoul(),
        player,
        kind,
        energy);
    if (kind == NaturesRitualRecipe.RitualKind.NATURE) {
      NatureRitual.onStart(server, pos, match.usedBraziers);
    } else {
      play(level, pos);
      if (kind == NaturesRitualRecipe.RitualKind.CELESTIAL)
        CelestialRitual.onStart(server, pos);
      else SummoningRitual.onStart(server, pos);
    }
    return true;
  }

  public static boolean validEnergy(
      Level level, BlockPos pos, NaturesRitualRecipe.RitualKind kind) {
    return kind == NaturesRitualRecipe.RitualKind.CELESTIAL
        ? CelestialRitual.validEnergy(level, pos)
        : NatureRitual.validEnergy(level, pos);
  }

  private static @Nullable Match findMatch(
      Level level, BlockPos tablePos, ItemStack tableItem, RitualTableBlockEntity table) {
    NaturesRitualRecipeInput input = new NaturesRitualRecipeInput(table);
    List<RecipeHolder<NaturesRitualRecipe>> candidates =
        level.getRecipeManager().getRecipesFor(ModRecipeTypes.NATURES_RITUAL.get(), input, level);
    List<RitualBrazierBlockEntity> available = new ArrayList<>();
    for (int dx = -8; dx <= 8; dx++) {
      for (int dz = -8; dz <= 8; dz++) {
        if (dx * dx + dz * dz > 64) {
          continue;
        }
        BlockPos brazierPos = tablePos.offset(dx, 0, dz);
        if (level.getBlockEntity(brazierPos) instanceof RitualBrazierBlockEntity brazier
            && !brazier.getStoredItem().isEmpty()) {
          available.add(brazier);
        }
      }
    }
    available.sort(
        Comparator.comparingDouble(
                (RitualBrazierBlockEntity brazier) -> tablePos.distSqr(brazier.getBlockPos()))
            .thenComparingLong(brazier -> brazier.getBlockPos().asLong()));

    for (RecipeHolder<NaturesRitualRecipe> holder : candidates) {
      NaturesRitualRecipe recipe = holder.value();
      if (!recipe.centerIngredient().test(tableItem)) {
        continue;
      }

      List<RitualBrazierBlockEntity> pool = new ArrayList<>(available);
      List<RitualBrazierBlockEntity> used = new ArrayList<>();
      boolean matches = true;

      for (Ingredient needed : recipe.offerings()) {
        int matchIndex = -1;
        for (int index = 0; index < pool.size(); index++) {
          if (needed.test(pool.get(index).getStoredItem())) {
            matchIndex = index;
            break;
          }
        }
        if (matchIndex == -1) {
          matches = false;
          break;
        }
        used.add(pool.remove(matchIndex));
      }

      if (matches) {
        return new Match(holder.id(), recipe, used);
      }
    }
    return null;
  }

  private static void fail(Level level, BlockPos pos, Player player, String key) {
    puff(level, pos, ParticleTypes.SMOKE, 8, 12);
    if (!level.isClientSide) {
      player.displayClientMessage(Component.translatable(key), true);
    }
    level.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 0.4F, 0.6F);
  }

  static void puff(Level level, BlockPos pos, SimpleParticleType type, int min, int max) {
    if (level instanceof ServerLevel server) {
      int count = ThreadLocalRandom.current().nextInt(min, max);
      for (int index = 0; index < count; index++) {
        server.sendParticles(
            type,
            pos.getX() + 0.5 + ThreadLocalRandom.current().nextDouble(-0.5, 0.5),
            pos.getY() + 1.0 + ThreadLocalRandom.current().nextDouble(0.0, 0.5),
            pos.getZ() + 0.5 + ThreadLocalRandom.current().nextDouble(-0.5, 0.5),
            1,
            0,
            0,
            0,
            0);
      }
    }
  }

  private static void play(Level level, BlockPos pos) {
    level.playSound(
        null, pos, SoundEvents.CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundSource.BLOCKS, 0.8F, 0.5F);
    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.8F, 0.5F);
  }

  public static void sendConvergingParticle(
      ServerLevel server,
      BlockPos center,
      double sourceX,
      double sourceY,
      double sourceZ,
      double speed) {
    double targetX = center.getX() + 0.5 + (server.random.nextDouble() - 0.5) * 1.5;
    double targetZ = center.getZ() + 0.5 + (server.random.nextDouble() - 0.5) * 1.5;
    server.sendParticles(
        ModParticleTypes.HEX_MOTES.get(),
        sourceX,
        sourceY,
        sourceZ,
        0,
        (targetX - sourceX) * Math.max(speed, 0.025),
        0.01,
        (targetZ - sourceZ) * Math.max(speed, 0.025),
        1.0);
  }

  private record Match(
      net.minecraft.resources.ResourceLocation id,
      NaturesRitualRecipe recipe,
      List<RitualBrazierBlockEntity> usedBraziers) {}
}
