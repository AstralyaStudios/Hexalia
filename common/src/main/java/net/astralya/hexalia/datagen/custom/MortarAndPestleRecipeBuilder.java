package net.astralya.hexalia.datagen.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.astralya.hexalia.recipe.MortarAndPestleRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

public class MortarAndPestleRecipeBuilder implements RecipeBuilder {

  private final Ingredient[] inputs;
  private final ItemStack outputStack;
  private final Item result;
  private final Map<String, CriterionTriggerInstance> criteria = new LinkedHashMap<>();

  private MortarAndPestleRecipeBuilder(Ingredient[] inputs, ItemStack output) {
    this.inputs = inputs;
    this.outputStack = output;
    this.result = output.getItem();
  }

  public static MortarAndPestleRecipeBuilder mortar(Ingredient input, ItemStack output) {
    return new MortarAndPestleRecipeBuilder(new Ingredient[] {input}, output);
  }

  public static MortarAndPestleRecipeBuilder mortar(
      Ingredient input1, Ingredient input2, ItemStack output) {
    return new MortarAndPestleRecipeBuilder(new Ingredient[] {input1, input2}, output);
  }

  public static MortarAndPestleRecipeBuilder mortar(
      Ingredient input1, Ingredient input2, Ingredient input3, ItemStack output) {
    return new MortarAndPestleRecipeBuilder(new Ingredient[] {input1, input2, input3}, output);
  }

  @Override
  public MortarAndPestleRecipeBuilder unlockedBy(String name, CriterionTriggerInstance criterion) {
    this.criteria.put(name, criterion);
    return this;
  }

  @Override
  public RecipeBuilder group(@Nullable String group) {
    return this;
  }

  public MortarAndPestleRecipeBuilder unlockedByItem(String name, Item item) {
    return this.unlockedBy(name, InventoryChangeTrigger.TriggerInstance.hasItems(item));
  }

  @Override
  public Item getResult() {
    return this.result;
  }

  @Override
  public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
    Advancement.Builder advancement =
        Advancement.Builder.recipeAdvancement()
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
            .rewards(AdvancementRewards.Builder.recipe(id))
            .requirements(RequirementsStrategy.OR);

    for (Map.Entry<String, CriterionTriggerInstance> entry : this.criteria.entrySet()) {
      advancement.addCriterion(entry.getKey(), entry.getValue());
    }

    consumer.accept(
        new Result(
            id,
            this.inputs,
            this.outputStack,
            advancement,
            new ResourceLocation(id.getNamespace(), "recipes/mortar_and_pestle/" + id.getPath())));
  }

  public static class Result implements FinishedRecipe {

    private final ResourceLocation id;
    private final Ingredient[] inputs;
    private final ItemStack outputStack;
    private final Advancement.Builder advancement;
    private final ResourceLocation advancementId;

    public Result(
        ResourceLocation id,
        Ingredient[] inputs,
        ItemStack outputStack,
        Advancement.Builder advancement,
        ResourceLocation advancementId) {
      this.id = id;
      this.inputs = inputs;
      this.outputStack = outputStack;
      this.advancement = advancement;
      this.advancementId = advancementId;
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
      JsonArray ingredientsArray = new JsonArray();
      for (Ingredient input : this.inputs) {
        ingredientsArray.add(input.toJson());
      }
      json.add("ingredients", ingredientsArray);

      JsonObject outputObject = new JsonObject();
      outputObject.addProperty(
          "item", this.outputStack.getItem().builtInRegistryHolder().key().location().toString());
      if (this.outputStack.getCount() > 1) {
        outputObject.addProperty("count", this.outputStack.getCount());
      }
      json.add("output", outputObject);
    }

    @Override
    public ResourceLocation getId() {
      return this.id;
    }

    @Override
    public RecipeSerializer<?> getType() {
      return MortarAndPestleRecipe.Serializer.INSTANCE;
    }

    @Override
    public @Nullable JsonObject serializeAdvancement() {
      return this.advancement.serializeToJson();
    }

    @Override
    public @Nullable ResourceLocation getAdvancementId() {
      return this.advancementId;
    }
  }
}
