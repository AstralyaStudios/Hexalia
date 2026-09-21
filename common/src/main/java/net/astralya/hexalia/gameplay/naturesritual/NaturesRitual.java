package net.astralya.hexalia.gameplay.naturesritual;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.astralya.hexalia.HexaliaConfig;
import net.astralya.hexalia.block.custom.RitualBrazierBlock;
import net.astralya.hexalia.block.entity.custom.RitualBrazierBlockEntity;
import net.astralya.hexalia.block.entity.custom.RitualTableBlockEntity;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.recipe.ModRecipeTypes;
import net.astralya.hexalia.recipe.NaturesRitualRecipe;
import net.astralya.hexalia.recipe.NaturesRitualRecipeInput;
import net.astralya.hexalia.util.ItemInteractionHelper;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
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

    int cropRequirement = HexaliaConfig.naturesRitualCropRequirement();
    List<BlockPos> grownCrops =
        cropRequirement == 0 ? List.of() : findFullyGrownCrops(level, pos, cropRequirement, 8);
    if (grownCrops.size() < cropRequirement) {
      fail(level, pos, player, "message.hexalia.natures_ritual.invalid_crops");
      return true;
    }

    table.startTransformation(
        match.recipe.getResultItem(level.registryAccess()).copy(),
        match.usedBraziers.size() * 40,
        match.usedBraziers,
        match.id,
        match.recipe.requiresSoul(),
        player);
    table.setGrownCropPositions(grownCrops);
    play(level, pos);
    puff(level, pos, ParticleTypes.POOF, 5, 10);
    return true;
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

  private static List<BlockPos> findFullyGrownCrops(
      Level level, BlockPos center, int required, int radius) {
    List<BlockPos> found = new ArrayList<>();
    for (int dx = -radius; dx <= radius; dx++) {
      for (int dz = -radius; dz <= radius; dz++) {
        BlockPos cropPos = center.offset(dx, 0, dz);
        BlockState cropState = level.getBlockState(cropPos);
        if (isCrop(cropState) && isFullyGrown(cropState)) {
          found.add(cropPos);
          if (found.size() >= required) {
            return found;
          }
        }
      }
    }
    return found;
  }

  public static boolean isFullyGrown(BlockState state) {
    IntegerProperty ageProperty = findAgeProperty(state);
    return ageProperty != null && state.getValue(ageProperty) >= maxAge(ageProperty);
  }

  public static @Nullable IntegerProperty findAgeProperty(BlockState state) {
    for (var property : state.getProperties()) {
      if (property instanceof IntegerProperty integerProperty
          && "age".equals(integerProperty.getName())) {
        return integerProperty;
      }
    }
    return null;
  }

  public static int maxAge(IntegerProperty property) {
    int max = 0;
    for (Integer value : property.getPossibleValues()) {
      max = Math.max(max, value);
    }
    return max;
  }

  public static void resetCrop(Level level, BlockPos cropPos) {
    BlockState cropState = level.getBlockState(cropPos);
    if (!isCrop(cropState)) {
      return;
    }
    IntegerProperty ageProperty = findAgeProperty(cropState);
    if (ageProperty != null && cropState.hasProperty(ageProperty)) {
      level.setBlock(cropPos, cropState.setValue(ageProperty, 0), 3);
    }
  }

  private static boolean isCrop(BlockState state) {
    return state.is(BlockTags.CROPS) || state.is(ModTags.Blocks.CROPS);
  }

  private static void fail(Level level, BlockPos pos, Player player, String key) {
    puff(level, pos, ParticleTypes.SMOKE, 8, 12);
    if (!level.isClientSide) {
      player.displayClientMessage(Component.translatable(key), true);
    }
    level.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 0.4F, 0.6F);
  }

  private static void puff(Level level, BlockPos pos, SimpleParticleType type, int min, int max) {
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

  private record Match(
      net.minecraft.resources.ResourceLocation id,
      NaturesRitualRecipe recipe,
      List<RitualBrazierBlockEntity> usedBraziers) {}
}
