package net.astralya.hexalia.compat;

import java.util.ArrayList;
import java.util.List;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.astralya.hexalia.recipe.RitualTableRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;

public final class NaturesRitualViewer {
  public static final ResourceLocation SOUL_ICON =
      new ResourceLocation("hexalia", "textures/gui/category/soul_required.png");
  public static final ResourceLocation TEXTURE =
      new ResourceLocation("hexalia", "textures/gui/ritual_table_gui.png");
  public static final int WIDTH = 118;
  public static final int HEIGHT = 80;
  public static final int ENERGY_Y = 84;
  public static final int ENERGY_HEIGHT = 28;
  public static final int[] INPUT_X = {27, 3, 27, 51, 3, 51, 3, 27, 51};
  public static final int[] INPUT_Y = {30, 6, 6, 6, 30, 30, 54, 54, 54};
  public static final int OUTPUT_X = 88;
  public static final int OUTPUT_Y = 30;
  public static final int SOUL_X = 72;
  public static final int SOUL_Y = 4;
  public static final int SOUL_SIZE = 10;
  public static final String SOUL_TOOLTIP = "tooltip.hexalia.requires_soul";

  private NaturesRitualViewer() {}

  public static ResourceLocation categoryId(RitualTableRecipe.RitualKind kind) {
    return new ResourceLocation("hexalia", switch (kind) {
      case NATURE -> "natures_ritual";
      case CELESTIAL -> "celestial_ritual";
      case SUMMONING -> "summoning_ritual";
    });
  }

  public static Component categoryTitle(RitualTableRecipe.RitualKind kind) {
    return Component.translatable("jei.hexalia.category." + categoryId(kind).getPath());
  }

  public static List<ItemStack> energyIcons(RitualTableRecipe recipe) {
    if (recipe.ritualKind() == RitualTableRecipe.RitualKind.CELESTIAL)
      return List.of(new ItemStack(ModBlocks.CELESTIAL_BLOOM.get()),
          new ItemStack(ModBlocks.WITHERED_CELESTIAL_BLOOM.get()));
    List<ItemStack> crops = new ArrayList<>();
    BuiltInRegistries.BLOCK.getTagOrEmpty(BlockTags.CROPS).forEach(holder -> addCrop(crops, holder));
    BuiltInRegistries.BLOCK.getTagOrEmpty(ModTags.Blocks.CROPS).forEach(holder -> addCrop(crops, holder));
    return crops.isEmpty() ? List.of(new ItemStack(Items.WHEAT_SEEDS)) : List.copyOf(crops);
  }

  private static void addCrop(List<ItemStack> crops, Holder<Block> holder) {
    boolean hasAge = holder.value().defaultBlockState().getProperties().stream()
        .anyMatch(property -> property instanceof IntegerProperty && property.getName().equals("age"));
    if (hasAge && holder.value().asItem() != Items.AIR
        && crops.stream().noneMatch(stack -> stack.is(holder.value().asItem())))
      crops.add(new ItemStack(holder.value().asItem()));
  }

  private static String energyKey(RitualTableRecipe recipe, String suffix) {
    return "jei.hexalia.energy."
        + (recipe.ritualKind() == RitualTableRecipe.RitualKind.CELESTIAL ? "bloom_" : "crop_")
        + suffix;
  }

  public static Component energyLabel(RitualTableRecipe recipe) {
    return Component.translatable(energyKey(recipe, "label"), recipe.resolvedEnergyCost());
  }

  public static Component energyFirstLine(RitualTableRecipe recipe) {
    return Component.translatable(energyKey(recipe, "line_1"), recipe.resolvedEnergyCost());
  }

  public static Component energySecondLine(RitualTableRecipe recipe) {
    return Component.translatable(energyKey(recipe, "line_2"));
  }

  public static List<Component> energyTooltip(RitualTableRecipe recipe) {
    return List.of(Component.translatable(energyKey(recipe, "title")),
        Component.translatable(energyKey(recipe, "requirement"), recipe.resolvedEnergyCost()),
        Component.translatable(energyKey(recipe, "effect")));
  }

  public static ItemStack result(RitualTableRecipe recipe) {
    if (!recipe.isEntityResult()) return recipe.getResultItem(null).copy();
    ResourceLocation id = recipe.entityResult();
    if (id != null && "hexalia".equals(id.getNamespace())) {
      if ("silk_moth".equals(id.getPath()))
        return new ItemStack(ModItems.SILK_MOTH_SPAWN_EGG.get());
      if ("cacofey".equals(id.getPath())) return new ItemStack(ModItems.CACOFEY_SPAWN_EGG.get());
    }
    EntityType<?> type =
        id == null ? null : BuiltInRegistries.ENTITY_TYPE.getOptional(id).orElse(null);
    SpawnEggItem egg = type == null ? null : SpawnEggItem.byId(type);
    return egg == null ? ItemStack.EMPTY : egg.getDefaultInstance();
  }
}
