package net.astralya.hexalia.compat;

import java.util.ArrayList;
import java.util.List;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.gameplay.naturesritual.NatureRitual;
import net.astralya.hexalia.recipe.NaturesRitualRecipe;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

public record RitualEnergyViewerIndicator(
    List<ItemStack> icons,
    Component label,
    Component firstLine,
    Component secondLine,
    List<Component> tooltip) {
  public static final int Y = 84;
  public static final int HEIGHT = 28;

  public static RitualEnergyViewerIndicator of(NaturesRitualRecipe recipe) {
    int cost = recipe.resolvedEnergyCost();
    if (recipe.ritualKind() == NaturesRitualRecipe.RitualKind.CELESTIAL) {
      return new RitualEnergyViewerIndicator(
          List.of(
              new ItemStack(ModBlocks.CELESTIAL_BLOOM.get()),
              new ItemStack(ModBlocks.WITHERED_CELESTIAL_BLOOM.get())),
          Component.translatable("jei.hexalia.energy.bloom_label", cost),
          Component.translatable("jei.hexalia.energy.bloom_short", cost),
          Component.translatable("jei.hexalia.energy.nearby"),
          List.of(
              Component.translatable("jei.hexalia.energy.bloom_title"),
              Component.translatable("jei.hexalia.energy.bloom_requirement", cost),
              Component.translatable("jei.hexalia.energy.bloom_effect")));
    }
    List<ItemStack> crops = new ArrayList<>();
    BuiltInRegistries.BLOCK.getTagOrEmpty(BlockTags.CROPS)
        .forEach(holder -> addCrop(crops, holder));
    BuiltInRegistries.BLOCK.getTagOrEmpty(ModTags.Blocks.CROPS)
        .forEach(holder -> addCrop(crops, holder));
    return new RitualEnergyViewerIndicator(
        List.copyOf(crops),
        Component.translatable("jei.hexalia.energy.crop_label", cost),
        Component.translatable("jei.hexalia.energy.crop_short", cost),
        Component.translatable("jei.hexalia.energy.nearby"),
        List.of(
            Component.translatable("jei.hexalia.energy.crop_title"),
            Component.translatable("jei.hexalia.energy.crop_requirement", cost),
            Component.translatable("jei.hexalia.energy.crop_effect")));
  }

  private static void addCrop(List<ItemStack> crops, Holder<Block> holder) {
    if (NatureRitual.findAgeProperty(holder.value().defaultBlockState()) != null
        && holder.value().asItem() != Items.AIR
        && crops.stream().noneMatch(stack -> stack.is(holder.value().asItem()))) {
      crops.add(new ItemStack(holder.value().asItem()));
    }
  }
}
