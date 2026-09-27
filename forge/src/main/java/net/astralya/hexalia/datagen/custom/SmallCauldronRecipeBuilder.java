package net.astralya.hexalia.forge.datagen.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.astralya.hexalia.recipe.SmallCauldronRecipe;
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
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SmallCauldronRecipeBuilder implements RecipeBuilder {

    private final ItemStack outputStack;
    private final float experience;
    private final int brewTime;
    private final Item result;
    private final Ingredient[] inputs;

    private final Map<String, CriterionTriggerInstance> criteria = new LinkedHashMap<>();

    private SmallCauldronRecipeBuilder(Ingredient[] inputs, ItemStack outputStack, float experience, int brewTime) {
        this.inputs = inputs;
        this.outputStack = outputStack;
        this.experience = experience;
        this.brewTime = brewTime;
        this.result = outputStack.getItem();
    }

    public static SmallCauldronRecipeBuilder cauldron(Ingredient input, ItemStack output) {
        return new SmallCauldronRecipeBuilder(new Ingredient[]{input}, output, 0.0F, 200);
    }

    public static SmallCauldronRecipeBuilder cauldron(Ingredient input1, Ingredient input2, ItemStack output) {
        return new SmallCauldronRecipeBuilder(new Ingredient[]{input1, input2}, output, 0.0F, 200);
    }

    public static SmallCauldronRecipeBuilder cauldron(Ingredient input1, Ingredient input2, Ingredient input3, ItemStack output) {
        return new SmallCauldronRecipeBuilder(new Ingredient[]{input1, input2, input3}, output, 0.0F, 200);
    }

    public static SmallCauldronRecipeBuilder cauldron(Ingredient input1, Ingredient input2, Ingredient input3, Ingredient input4, ItemStack output) {
        return new SmallCauldronRecipeBuilder(new Ingredient[]{input1, input2, input3, input4}, output, 0.0F, 200);
    }

    public SmallCauldronRecipeBuilder experience(float experience) {
        return new SmallCauldronRecipeBuilder(this.inputs, this.outputStack, experience, this.brewTime).copyCriteriaFrom(this);
    }

    public SmallCauldronRecipeBuilder brewTime(int brewTime) {
        return new SmallCauldronRecipeBuilder(this.inputs, this.outputStack, this.experience, brewTime).copyCriteriaFrom(this);
    }

    private SmallCauldronRecipeBuilder copyCriteriaFrom(SmallCauldronRecipeBuilder other) {
        this.criteria.putAll(other.criteria);
        return this;
    }

    @Override
    public SmallCauldronRecipeBuilder unlockedBy(String name, CriterionTriggerInstance criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    public SmallCauldronRecipeBuilder unlockedByItem(String name, Item item) {
        return this.unlockedBy(name, InventoryChangeTrigger.TriggerInstance.hasItems(item));
    }

    @Override
    public RecipeBuilder group(@Nullable String group) {
        return this;
    }

    @Override
    public Item getResult() {
        return this.result;
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        Advancement.Builder advancement = Advancement.Builder.recipeAdvancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(RequirementsStrategy.OR);

        for (Map.Entry<String, CriterionTriggerInstance> entry : this.criteria.entrySet()) {
            advancement.addCriterion(entry.getKey(), entry.getValue());
        }

        consumer.accept(new Result(
                id,
                this.inputs,
                this.outputStack,
                this.experience,
                this.brewTime,
                advancement,
                ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "recipes/small_cauldron/" + id.getPath())
        ));
    }

    public static class Result implements FinishedRecipe {

        private final ResourceLocation id;
        private final Ingredient[] inputs;
        private final ItemStack outputStack;
        private final float experience;
        private final int brewTime;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, Ingredient[] inputs, ItemStack outputStack, float experience, int brewTime,
                      Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.inputs = inputs;
            this.outputStack = outputStack;
            this.experience = experience;
            this.brewTime = brewTime;
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

            JsonObject resultObject = new JsonObject();
            resultObject.addProperty("item", ForgeRegistries.ITEMS.getKey(this.outputStack.getItem()).toString());
            if (this.outputStack.getCount() > 1) {
                resultObject.addProperty("count", this.outputStack.getCount());
            }
            json.add("result", resultObject);

            if (this.experience != 0.0F) {
                json.addProperty("experience", this.experience);
            }
            json.addProperty("duration", this.brewTime);
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return SmallCauldronRecipe.Serializer.INSTANCE;
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
