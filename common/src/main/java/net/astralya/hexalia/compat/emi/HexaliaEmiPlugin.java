package net.astralya.hexalia.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import java.util.ArrayList;
import java.util.List;
import net.astralya.hexalia.compat.HexaliaRecipeGuiLayout;
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
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;

@EmiEntrypoint
public final class HexaliaEmiPlugin implements EmiPlugin {
  @Override
  public void register(EmiRegistry registry) {
    registerCategories(registry);
    registerWorkstations(registry);
    registerRecipes(registry);
  }

  private static void registerCategories(EmiRegistry registry) {
    registry.addCategory(HexaliaEmiRecipeCategories.MORTAR_AND_PESTLE);
    registry.addCategory(HexaliaEmiRecipeCategories.SMALL_CAULDRON);
    registry.addCategory(HexaliaEmiRecipeCategories.NATURES_RITUAL);
    registry.addCategory(HexaliaEmiRecipeCategories.CELESTIAL_INFUSION);
    registry.addCategory(HexaliaEmiRecipeCategories.MUTATION);
  }

  private static void registerWorkstations(EmiRegistry registry) {
    addWorkstation(
        registry,
        HexaliaEmiRecipeCategories.MORTAR_AND_PESTLE,
        ModItems.MORTAR_AND_PESTLE.get());
    addWorkstation(
        registry, HexaliaEmiRecipeCategories.SMALL_CAULDRON, ModItems.SMALL_CAULDRON.get());
    addWorkstation(
        registry, HexaliaEmiRecipeCategories.NATURES_RITUAL, ModItems.RITUAL_TABLE.get());
    addWorkstation(
        registry, HexaliaEmiRecipeCategories.NATURES_RITUAL, ModItems.RITUAL_BRAZIER.get());
    addWorkstation(registry, HexaliaEmiRecipeCategories.NATURES_RITUAL, ModItems.HEX_FOCUS.get());
    addWorkstation(
        registry, HexaliaEmiRecipeCategories.CELESTIAL_INFUSION, ModItems.RITUAL_BRAZIER.get());
    addWorkstation(
        registry, HexaliaEmiRecipeCategories.CELESTIAL_INFUSION, ModItems.CELESTIAL_CRYSTAL.get());
    addWorkstation(
        registry, HexaliaEmiRecipeCategories.CELESTIAL_INFUSION, ModItems.HEX_FOCUS.get());
    addWorkstation(registry, HexaliaEmiRecipeCategories.MUTATION, ModItems.MUTAVIS.get());
  }

  private static void registerRecipes(EmiRegistry registry) {
    RecipeManager recipeManager = registry.getRecipeManager();

    for (RecipeHolder<MortarAndPestleRecipe> holder :
        recipes(recipeManager, ModRecipeTypes.MORTAR_AND_PESTLE.get())) {
      MortarAndPestleRecipe recipe = holder.value();
      addRecipe(
          registry,
          HexaliaEmiRecipeCategories.MORTAR_AND_PESTLE,
          HexaliaRecipeGuiLayout.MORTAR_AND_PESTLE,
          holder,
          recipe.ingredients(),
          recipe.output());
    }

    for (RecipeHolder<SmallCauldronRecipe> holder :
        recipes(recipeManager, ModRecipeTypes.SMALL_CAULDRON.get())) {
      SmallCauldronRecipe recipe = holder.value();
      addRecipe(
          registry,
          HexaliaEmiRecipeCategories.SMALL_CAULDRON,
          HexaliaRecipeGuiLayout.SMALL_CAULDRON,
          holder,
          recipe.getIngredients(),
          recipe.getResultItem(null),
          List.of(),
          smallCauldronOutputTooltips(recipe));
    }

    for (RecipeHolder<NaturesRitualRecipe> holder :
        recipes(recipeManager, ModRecipeTypes.NATURES_RITUAL.get())) {
      NaturesRitualRecipe recipe = holder.value();
      addRecipe(
          registry,
          HexaliaEmiRecipeCategories.NATURES_RITUAL,
          HexaliaRecipeGuiLayout.NATURES_RITUAL,
          holder,
          naturesRitualInputs(recipe),
          naturesRitualResult(recipe),
          List.of(),
          List.of(),
          false,
          recipe.offerings().size() > 3,
          false,
          recipe.requiresSoul());
    }

    for (RecipeHolder<CelestialInfusionRecipe> holder :
        recipes(recipeManager, ModRecipeTypes.CELESTIAL_INFUSION.get())) {
      CelestialInfusionRecipe recipe = holder.value();
      addRecipe(
          registry,
          HexaliaEmiRecipeCategories.CELESTIAL_INFUSION,
          HexaliaRecipeGuiLayout.CELESTIAL_INFUSION,
          holder,
          List.of(recipe.inputItem()),
          recipe.output(),
          List.of(),
          List.of(),
          true,
          false,
          false,
          false);
    }

    for (RecipeHolder<MutationRecipe> holder :
        recipes(recipeManager, ModRecipeTypes.MUTATION.get())) {
      MutationRecipe recipe = holder.value();
      addRecipe(
          registry,
          HexaliaEmiRecipeCategories.MUTATION,
          HexaliaRecipeGuiLayout.MUTATION,
          holder,
          List.of(recipe.inputItem()),
          recipe.output(),
          List.of(),
          List.of(),
          false,
          false,
          true,
          false);
    }
  }

  private static <I extends RecipeInput, T extends Recipe<I>> List<RecipeHolder<T>> recipes(
      RecipeManager recipeManager, RecipeType<T> recipeType) {
    return recipeManager.getAllRecipesFor(recipeType);
  }

  private static <I extends RecipeInput, T extends Recipe<I>> void addRecipe(
      EmiRegistry registry,
      EmiRecipeCategory category,
      HexaliaRecipeGuiLayout layout,
      RecipeHolder<T> holder,
      List<Ingredient> ingredients,
      ItemStack output) {
    addRecipe(registry, category, layout, holder, ingredients, output, List.of(), List.of());
  }

  private static <I extends RecipeInput, T extends Recipe<I>> void addRecipe(
      EmiRegistry registry,
      EmiRecipeCategory category,
      HexaliaRecipeGuiLayout layout,
      RecipeHolder<T> holder,
      List<Ingredient> ingredients,
      ItemStack output,
      List<Component> recipeTooltips,
      List<Component> outputTooltips) {
    addRecipe(
        registry,
        category,
        layout,
        holder,
        ingredients,
        output,
        recipeTooltips,
        outputTooltips,
        false,
        false,
        false,
        false);
  }

  private static <I extends RecipeInput, T extends Recipe<I>> void addRecipe(
      EmiRegistry registry,
      EmiRecipeCategory category,
      HexaliaRecipeGuiLayout layout,
      RecipeHolder<T> holder,
      List<Ingredient> ingredients,
      ItemStack output,
      List<Component> recipeTooltips,
      List<Component> outputTooltips,
      boolean drawRitualBrazierFocus,
      boolean showRitualTableFocusTooltip,
      boolean showMutationTooltip,
      boolean showSoulIndicator) {
    List<EmiIngredient> inputs = ingredients.stream().map(EmiIngredient::of).toList();
    EmiStack emiOutput = EmiStack.of(output.copy());
    if (inputs.isEmpty() || emiOutput.isEmpty()) {
      return;
    }

    registry.addRecipe(
        new HexaliaEmiRecipe(
            category,
            layout,
            holder.id(),
            inputs,
            emiOutput,
            recipeTooltips,
            outputTooltips,
            drawRitualBrazierFocus,
            showRitualTableFocusTooltip,
            showMutationTooltip,
            showSoulIndicator));
  }

  private static void addWorkstation(
      EmiRegistry registry, EmiRecipeCategory category, ItemLike item) {
    registry.addWorkstation(category, EmiStack.of(item));
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

  private static Component tooltip(String key, Object... arguments) {
    return Component.translatable(key, arguments).withStyle(ChatFormatting.GRAY);
  }
}
