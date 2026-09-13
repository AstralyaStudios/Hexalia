package net.astralya.hexalia.compat.rei;

import java.util.ArrayList;
import java.util.List;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.astralya.hexalia.compat.HexaliaRecipeGuiLayout;
import net.astralya.hexalia.compat.jei.util.JeiRecipeLookup;
import net.astralya.hexalia.compat.rei.HexaliaReiDisplay.Layout;
import net.astralya.hexalia.compat.rei.category.HexaliaReiCategory;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.recipe.CelestialInfusionRecipe;
import net.astralya.hexalia.recipe.ModRecipeTypes;
import net.astralya.hexalia.recipe.MortarAndPestleRecipe;
import net.astralya.hexalia.recipe.MutationRecipe;
import net.astralya.hexalia.recipe.NaturesRitualRecipe;
import net.astralya.hexalia.recipe.SmallCauldronRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;

public class HexaliaReiPlugin implements REIClientPlugin {
  @Override
  public void registerCategories(CategoryRegistry registry) {
    registry.add(
        new HexaliaReiCategory(
            HexaliaReiRecipeTypes.MORTAR_AND_PESTLE,
            "jei.hexalia.category.mortar_and_pestle",
            ModItems.MORTAR_AND_PESTLE.get(),
            HexaliaRecipeGuiLayout.MORTAR_AND_PESTLE));
    registry.add(
        new HexaliaReiCategory(
            HexaliaReiRecipeTypes.SMALL_CAULDRON,
            "jei.hexalia.category.small_cauldron",
            ModItems.SMALL_CAULDRON.get(),
            HexaliaRecipeGuiLayout.SMALL_CAULDRON));
    registry.add(
        new HexaliaReiCategory(
            HexaliaReiRecipeTypes.NATURES_RITUAL,
            "jei.hexalia.category.natures_ritual",
            ModItems.RITUAL_TABLE.get(),
            HexaliaRecipeGuiLayout.NATURES_RITUAL));
    registry.add(
        new HexaliaReiCategory(
            HexaliaReiRecipeTypes.CELESTIAL_INFUSION,
            "jei.hexalia.category.celestial_infusion",
            ModItems.RITUAL_BRAZIER.get(),
            HexaliaRecipeGuiLayout.CELESTIAL_INFUSION));
    registry.add(
        new HexaliaReiCategory(
            HexaliaReiRecipeTypes.MUTATION,
            "jei.hexalia.category.mutation",
            ModItems.MUTAVIS.get(),
            HexaliaRecipeGuiLayout.MUTATION));

    addWorkstation(
        registry, HexaliaReiRecipeTypes.MORTAR_AND_PESTLE, ModItems.MORTAR_AND_PESTLE.get());
    addWorkstation(registry, HexaliaReiRecipeTypes.SMALL_CAULDRON, ModItems.SMALL_CAULDRON.get());
    addWorkstation(registry, HexaliaReiRecipeTypes.NATURES_RITUAL, ModItems.RITUAL_TABLE.get());
    addWorkstation(registry, HexaliaReiRecipeTypes.NATURES_RITUAL, ModItems.RITUAL_BRAZIER.get());
    addWorkstation(registry, HexaliaReiRecipeTypes.NATURES_RITUAL, ModItems.HEX_FOCUS.get());
    addWorkstation(
        registry, HexaliaReiRecipeTypes.CELESTIAL_INFUSION, ModItems.RITUAL_BRAZIER.get());
    addWorkstation(
        registry, HexaliaReiRecipeTypes.CELESTIAL_INFUSION, ModItems.CELESTIAL_CRYSTAL.get());
    addWorkstation(registry, HexaliaReiRecipeTypes.CELESTIAL_INFUSION, ModItems.HEX_FOCUS.get());
    addWorkstation(registry, HexaliaReiRecipeTypes.MUTATION, ModItems.MUTAVIS.get());
  }

  @Override
  public void registerDisplays(DisplayRegistry registry) {
    for (MortarAndPestleRecipe recipe : recipes(registry, ModRecipeTypes.MORTAR_AND_PESTLE.get())) {
      registry.add(
          display(
              HexaliaReiRecipeTypes.MORTAR_AND_PESTLE,
              Layout.MORTAR_AND_PESTLE,
              recipe.ingredients(),
              recipe.output()));
    }
    for (SmallCauldronRecipe recipe : recipes(registry, ModRecipeTypes.SMALL_CAULDRON.get())) {
      registry.add(
          displayWithOutputTooltips(
              HexaliaReiRecipeTypes.SMALL_CAULDRON,
              Layout.SMALL_CAULDRON,
              recipe.getIngredients(),
              recipe.getResultItem(null),
              smallCauldronOutputTooltips(recipe)));
    }
    for (NaturesRitualRecipe recipe : recipes(registry, ModRecipeTypes.NATURES_RITUAL.get())) {
      registry.add(
          displayWithRecipeTooltips(
              HexaliaReiRecipeTypes.NATURES_RITUAL,
              Layout.NATURES_RITUAL,
              naturesRitualInputs(recipe),
              naturesRitualResult(recipe),
              List.of(),
              recipe.requiresSoul()));
    }
    for (CelestialInfusionRecipe recipe :
        recipes(registry, ModRecipeTypes.CELESTIAL_INFUSION.get())) {
      registry.add(
          displayWithRecipeTooltips(
              HexaliaReiRecipeTypes.CELESTIAL_INFUSION,
              Layout.CELESTIAL_INFUSION,
              List.of(recipe.inputItem()),
              recipe.output(),
              celestialInfusionTooltips()));
    }
    for (MutationRecipe recipe : recipes(registry, ModRecipeTypes.MUTATION.get())) {
      registry.add(
          display(
              HexaliaReiRecipeTypes.MUTATION,
              Layout.MUTATION,
              List.of(recipe.inputItem()),
              recipe.output()));
    }
  }

  private static <I extends RecipeInput, T extends Recipe<I>> List<T> recipes(
      DisplayRegistry registry, RecipeType<T> recipeType) {
    return JeiRecipeLookup.getRecipes(registry.getRecipeManager(), recipeType);
  }

  private static HexaliaReiDisplay display(
      CategoryIdentifier<HexaliaReiDisplay> category,
      Layout layout,
      List<Ingredient> ingredients,
      ItemStack output) {
    return new HexaliaReiDisplay(
        category,
        layout,
        EntryIngredients.ofIngredients(ingredients),
        List.of(output(output, List.of())));
  }

  private static HexaliaReiDisplay displayWithOutputTooltips(
      CategoryIdentifier<HexaliaReiDisplay> category,
      Layout layout,
      List<Ingredient> ingredients,
      ItemStack output,
      List<Component> outputTooltips) {
    return new HexaliaReiDisplay(
        category,
        layout,
        EntryIngredients.ofIngredients(ingredients),
        List.of(output(output, outputTooltips)));
  }

  private static HexaliaReiDisplay displayWithRecipeTooltips(
      CategoryIdentifier<HexaliaReiDisplay> category,
      Layout layout,
      List<Ingredient> ingredients,
      ItemStack output,
      List<Component> recipeTooltips) {
    return new HexaliaReiDisplay(
        category,
        layout,
        EntryIngredients.ofIngredients(ingredients),
        List.of(output(output, List.of())),
        recipeTooltips);
  }

  private static HexaliaReiDisplay displayWithRecipeTooltips(
      CategoryIdentifier<HexaliaReiDisplay> category,
      Layout layout,
      List<Ingredient> ingredients,
      ItemStack output,
      List<Component> recipeTooltips,
      boolean showSoulIndicator) {
    return new HexaliaReiDisplay(
        category,
        layout,
        EntryIngredients.ofIngredients(ingredients),
        List.of(output(output, List.of())),
        recipeTooltips,
        showSoulIndicator);
  }

  private static EntryIngredient output(ItemStack output, List<Component> tooltips) {
    if (tooltips.isEmpty()) {
      return EntryIngredients.of(output);
    }
    return EntryIngredient.of(EntryStacks.of(output).tooltip(tooltips));
  }

  private static void addWorkstation(
      CategoryRegistry registry, CategoryIdentifier<HexaliaReiDisplay> category, ItemLike item) {
    registry.addWorkstations(category, EntryIngredients.of(item));
  }

  private static List<Component> smallCauldronOutputTooltips(SmallCauldronRecipe recipe) {
    if (recipe.getExperience() > 0.0F) {
      return List.of(
          tooltip("jei.hexalia.tooltip.brew_time", recipe.getBrewTime()),
          tooltip("jei.hexalia.tooltip.experience", recipe.getExperience()));
    }
    return List.of(tooltip("jei.hexalia.tooltip.brew_time", recipe.getBrewTime()));
  }

  private static List<Ingredient> naturesRitualInputs(NaturesRitualRecipe recipe) {
    List<Ingredient> inputs = new ArrayList<>();
    inputs.add(recipe.centerIngredient());
    inputs.addAll(recipe.offerings().subList(0, Math.min(recipe.offerings().size(), 8)));
    return inputs;
  }

  private static ItemStack naturesRitualResult(NaturesRitualRecipe recipe) {
    if (recipe.isItemResult()) return recipe.itemResult();
    EntityType<?> type =
        recipe.entityResult()
            .flatMap(result -> BuiltInRegistries.ENTITY_TYPE.getOptional(result.entity()))
            .orElse(null);
    SpawnEggItem spawnEgg = type == null ? null : SpawnEggItem.byId(type);
    return spawnEgg == null ? ItemStack.EMPTY : spawnEgg.getDefaultInstance();
  }

  private static List<Component> celestialInfusionTooltips() {
    return List.of(
        tooltip("jei.hexalia.tooltip.requires_hex_focus"),
        tooltip("jei.hexalia.tooltip.requires_salted_brazier"),
        tooltip("jei.hexalia.tooltip.requires_celestial_blooms"),
        tooltip("jei.hexalia.tooltip.requires_open_sky"));
  }

  private static Component tooltip(String key, Object... arguments) {
    return Component.translatable(key, arguments).withStyle(ChatFormatting.GRAY);
  }
}
