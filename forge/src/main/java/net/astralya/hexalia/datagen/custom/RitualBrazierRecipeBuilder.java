package net.astralya.hexalia.forge.datagen.custom;

import com.google.gson.JsonObject;
import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.recipe.RitualBrazierRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class RitualBrazierRecipeBuilder implements RecipeBuilder {
    private final Ingredient input;
    private final Item result;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();

    public RitualBrazierRecipeBuilder(ItemLike input, ItemLike result) {
        this.input = Ingredient.of(input);
        this.result = result.asItem();
    }

    @Override
    public RecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
        this.advancement.addCriterion(pCriterionName, pCriterionTrigger);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String pGroupName) {
        return this;
    }

    @Override
    public Item getResult() {
        return result;
    }

    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        this.advancement.parent(ResourceLocation.parse("recipes/root"))
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pRecipeId))
                .rewards(AdvancementRewards.Builder.recipe(pRecipeId))
                .requirements(RequirementsStrategy.OR);

        pFinishedRecipeConsumer.accept(new Result(pRecipeId, this.result, this.input,
                this.advancement, ResourceLocation.fromNamespaceAndPath(pRecipeId.getNamespace(), "recipes/"
                + pRecipeId.getPath())));
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Item result;
        private final Ingredient input;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, Item result, Ingredient input,
                      Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.result = result;
            this.input = input;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty("type", "hexalia:ritual_brazier");

            json.add("input", input.toJson());

            JsonObject outputJson = new JsonObject();
            outputJson.addProperty("item", ForgeRegistries.ITEMS.getKey(this.result).toString());
            json.add("output", outputJson);
        }

        @Override
        public ResourceLocation getId() {
            return ResourceLocation.fromNamespaceAndPath(HexaliaMod.MODID, ForgeRegistries.ITEMS.getKey(this.result).getPath() + "_from_ritual_brazier");
        }

        @Override
        public RecipeSerializer<?> getType() {
            return RitualBrazierRecipe.Serializer.INSTANCE;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
