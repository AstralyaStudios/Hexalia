package net.astralya.hexalia.neoforge.datagen.custom;

import java.util.LinkedHashMap;
import java.util.Map;
import net.astralya.hexalia.recipe.NaturesRitualRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ItemLike;

public final class NaturesRitualRecipeBuilder implements RecipeBuilder {
  private final RecipeCategory category;
  private final Ingredient centerIngredient;
  private final NonNullList<Ingredient> offerings = NonNullList.create();
  private final Item result;
  private final NaturesRitualRecipe.RitualResult ritualResult;
  private boolean requiresSoul;
  private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
  private String group;

  private NaturesRitualRecipeBuilder(
      RecipeCategory category, Ingredient centerIngredient, ItemLike result) {
    this.category = category;
    this.result = result.asItem();
    this.ritualResult = new NaturesRitualRecipe.ItemResult(new ItemStack(this.result));
    this.centerIngredient = centerIngredient;
  }

  private NaturesRitualRecipeBuilder(
      RecipeCategory category,
      Ingredient centerIngredient,
      ResourceLocation entity,
      int count) {
    if (count < 1) {
      throw new IllegalArgumentException("Summoning entity count must be at least one");
    }
    this.category = category;
    this.centerIngredient = centerIngredient;
    this.result = Items.AIR;
    this.ritualResult = new NaturesRitualRecipe.EntityResult(entity, count);
    this.requiresSoul = true;
  }

  public static NaturesRitualRecipeBuilder ritual(
      RecipeCategory category, Ingredient centerIngredient, ItemLike result) {
    return new NaturesRitualRecipeBuilder(category, centerIngredient, result);
  }

  public static NaturesRitualRecipeBuilder summoning(
      RecipeCategory category, Ingredient centerIngredient, EntityType<?> entityType) {
    return summoning(category, centerIngredient, entityType, 1);
  }

  public static NaturesRitualRecipeBuilder summoning(
      RecipeCategory category,
      Ingredient centerIngredient,
      EntityType<?> entityType,
      int count) {
    return summoning(
        category, centerIngredient, BuiltInRegistries.ENTITY_TYPE.getKey(entityType), count);
  }

  public static NaturesRitualRecipeBuilder summoning(
      RecipeCategory category,
      Ingredient centerIngredient,
      ResourceLocation entity,
      int count) {
    return new NaturesRitualRecipeBuilder(category, centerIngredient, entity, count);
  }

  public NaturesRitualRecipeBuilder requiresSoul(boolean requiresSoul) {
    this.requiresSoul = requiresSoul;
    return this;
  }

  public NaturesRitualRecipeBuilder requiresBrazierIngredient(Ingredient ingredient) {
    offerings.add(ingredient);
    return this;
  }

  @Override
  public NaturesRitualRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
    criteria.put(name, criterion);
    return this;
  }

  @Override
  public NaturesRitualRecipeBuilder group(String recipeGroup) {
    group = recipeGroup;
    return this;
  }

  @Override
  public Item getResult() {
    return result;
  }

  @Override
  public void save(RecipeOutput recipeOutput, ResourceLocation recipeId) {
    ensureValid(recipeId);

    Advancement.Builder advancement =
        recipeOutput
            .advancement()
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
            .rewards(AdvancementRewards.Builder.recipe(recipeId));
    criteria.forEach(advancement::addCriterion);

    recipeOutput.accept(
        recipeId,
        new NaturesRitualRecipe(centerIngredient, offerings, ritualResult, requiresSoul),
        advancement.build(recipeId.withPrefix("recipes/" + category.getFolderName() + "/")));
  }

  private void ensureValid(ResourceLocation recipeId) {
    if (criteria.isEmpty()) {
      throw new IllegalStateException("No way of obtaining recipe " + recipeId);
    }
  }
}
