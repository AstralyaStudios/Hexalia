package net.astralya.hexalia.datagen.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.astralya.hexalia.recipe.ModRecipes;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityType;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class RitualTableRecipeBuilder implements CraftingRecipeJsonBuilder {

    private Ingredient centerIngredient;
    private final DefaultedList<Ingredient> offerings = DefaultedList.of();
    private final ItemStack output;
    private final Item resultItem;
    private final Identifier entityResult;
    private final int entityCount;
    private final Advancement.Builder advancement = Advancement.Builder.create();

    private RitualTableRecipeBuilder(ItemStack output) {
        this.output = output.copy();
        this.resultItem = this.output.getItem();
        this.entityResult = null;
        this.entityCount = 0;
    }

    private RitualTableRecipeBuilder(EntityType<?> entityType, int count) {
        this.output = ItemStack.EMPTY;
        this.resultItem = net.minecraft.item.Items.AIR;
        this.entityResult = Registries.ENTITY_TYPE.getId(entityType);
        this.entityCount = count;
    }

    public static RitualTableRecipeBuilder ritual(ItemConvertible result, int count) {
        return new RitualTableRecipeBuilder(new ItemStack(result, count));
    }

    public static RitualTableRecipeBuilder ritual(ItemStack output) {
        return new RitualTableRecipeBuilder(output);
    }

    public static RitualTableRecipeBuilder summon(EntityType<?> entityType, int count) {
        if (count < 1) throw new IllegalArgumentException("Entity result count must be at least one");
        return new RitualTableRecipeBuilder(entityType, count);
    }

    public RitualTableRecipeBuilder tableItem(ItemConvertible item) {
        this.centerIngredient = Ingredient.ofItems(item);
        return this;
    }

    public RitualTableRecipeBuilder brazierItem(ItemConvertible item) {
        this.offerings.add(Ingredient.ofItems(item));
        return this;
    }

    public RitualTableRecipeBuilder addIngredient(Ingredient ingredient) {
        if (this.centerIngredient == null) this.centerIngredient = ingredient;
        else this.offerings.add(ingredient);
        return this;
    }

    public RitualTableRecipeBuilder addIngredient(ItemConvertible item) {
        return addIngredient(Ingredient.ofItems(item));
    }

    public RitualTableRecipeBuilder addIngredient(ItemStack stack) {
        return addIngredient(Ingredient.ofStacks(stack));
    }

    @Override
    public RitualTableRecipeBuilder criterion(String name, CriterionConditions conditions) {
        this.advancement.criterion(name, conditions);
        return this;
    }

    @Override
    public RitualTableRecipeBuilder group(@Nullable String group) {
        return this;
    }

    @Override
    public Item getOutputItem() {
        return this.resultItem;
    }

    @Override
    public void offerTo(Consumer<RecipeJsonProvider> exporter, Identifier recipeId) {
        if (this.centerIngredient == null) {
            throw new IllegalStateException("Ritual Table recipe must have at least 1 ingredient (table item).");
        }
        this.advancement.parent(new Identifier("recipes/root"))
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId))
                .rewards(AdvancementRewards.Builder.recipe(recipeId));

        Identifier advId = new Identifier(recipeId.getNamespace(), "recipes/ritual_table/" + recipeId.getPath());

        exporter.accept(new JsonBuilder(
                recipeId,
                this.output,
                this.entityResult,
                this.entityCount,
                this.centerIngredient,
                this.offerings,
                this.advancement,
                advId
        ));
    }

    public static class JsonBuilder implements RecipeJsonProvider {
        private final Identifier id;
        private final ItemStack output;
        private final Identifier entityResult;
        private final int entityCount;
        private final Ingredient centerIngredient;
        private final DefaultedList<Ingredient> offerings;
        private final Advancement.Builder advancement;
        private final Identifier advancementId;

        public JsonBuilder(Identifier id, ItemStack output, Identifier entityResult, int entityCount,
                           Ingredient centerIngredient, DefaultedList<Ingredient> offerings,
                           Advancement.Builder advancement, Identifier advancementId) {
            this.id = id;
            this.output = output.copy();
            this.entityResult = entityResult;
            this.entityCount = entityCount;
            this.centerIngredient = centerIngredient;
            this.offerings = offerings;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("type", "hexalia:ritual_table");

            json.add("center", this.centerIngredient.toJson());
            JsonArray offeringJson = new JsonArray();
            for (Ingredient offering : this.offerings) offeringJson.add(offering.toJson());
            json.add("offerings", offeringJson);

            if (entityResult != null) {
                json.addProperty("requires_soul", true);
                JsonObject result = new JsonObject();
                result.addProperty("type", "entity");
                result.addProperty("entity", entityResult.toString());
                result.addProperty("count", entityCount);
                json.add("result", result);
            } else {
                JsonObject out = new JsonObject();
                out.addProperty("item", Registries.ITEM.getId(this.output.getItem()).toString());
                if (this.output.getCount() > 1) out.addProperty("count", this.output.getCount());
                json.add("output", out);
            }
        }

        @Override
        public Identifier getRecipeId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return ModRecipes.RITUAL_TABLE_SERIALIZER;
        }

        @Nullable
        @Override
        public JsonObject toAdvancementJson() {
            return this.advancement.toJson();
        }

        @Nullable
        @Override
        public Identifier getAdvancementId() {
            return this.advancementId;
        }
    }
}
