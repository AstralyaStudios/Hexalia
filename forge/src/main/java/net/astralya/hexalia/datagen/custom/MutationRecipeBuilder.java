package net.astralya.hexalia.forge.datagen.custom;

import com.google.gson.JsonObject;
import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.recipe.MutationRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
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

public class MutationRecipeBuilder implements RecipeBuilder {

    private final Ingredient input;
    private final ItemStack outputStack;
    private final Item result;
    private final Map<String, net.minecraft.advancements.CriterionTriggerInstance> criteria = new LinkedHashMap<>();

    public MutationRecipeBuilder(Ingredient input, ItemStack output) {
        this.input = input;
        this.outputStack = output.copy();
        this.result = output.getItem();
    }

    public static MutationRecipeBuilder mutation(Ingredient input, ItemStack output) {
        return new MutationRecipeBuilder(input, output);
    }

    @Override
    public MutationRecipeBuilder unlockedBy(String name, net.minecraft.advancements.CriterionTriggerInstance criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String s) {
        return this;
    }

    public MutationRecipeBuilder unlockedByItem(String name, Item item) {
        return this.unlockedBy(name, InventoryChangeTrigger.TriggerInstance.hasItems(item));
    }

    @Override
    public Item getResult() {
        return this.result;
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        Advancement.Builder adv = Advancement.Builder.advancement()
                .parent(ResourceLocation.parse("recipes/root"))
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(RequirementsStrategy.OR);

        this.criteria.forEach(adv::addCriterion);

        ResourceLocation advId = id.withPrefix("recipes/mutation/");

        consumer.accept(new Result(
                id,
                this.input,
                this.outputStack,
                adv,
                advId
        ));
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient input;
        private final ItemStack output;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id,
                      Ingredient input,
                      ItemStack output,
                      Advancement.Builder advancement,
                      ResourceLocation advancementId) {
            this.id = id;
            this.input = input;
            this.output = output.copy();
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty("type", HexaliaMod.MODID + ":mutation");

            json.add("input", input.toJson());

            JsonObject outputJson = new JsonObject();
            outputJson.addProperty("item", ForgeRegistries.ITEMS.getKey(this.output.getItem()).toString());
            if (this.output.getCount() != 1) {
                outputJson.addProperty("count", this.output.getCount());
            }

            json.add("output", outputJson);
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return MutationRecipe.Serializer.INSTANCE;
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
