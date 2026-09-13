package net.astralya.hexalia.datagen.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.astralya.hexalia.HexaliaMod; // if you prefer using your modid in getId()
import net.astralya.hexalia.recipe.RitualTableRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class RitualTableRecipeBuilder implements RecipeBuilder {

    private Ingredient centerIngredient;
    private final NonNullList<Ingredient> offerings = NonNullList.create();
    private final ItemStack outputStack;
    private final Item resultItem;
    private final ResourceLocation entityResult;
    private final int entityCount;

    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    private final Map<String, CriterionTriggerInstance> criteria = new LinkedHashMap<>();

    public RitualTableRecipeBuilder(ItemStack output) {
        this.outputStack = output.copy();
        this.resultItem = this.outputStack.getItem();
        this.entityResult = null;
        this.entityCount = 0;
    }

    private RitualTableRecipeBuilder(EntityType<?> entityType, int count) {
        this.outputStack = ItemStack.EMPTY;
        this.resultItem = Items.AIR;
        this.entityResult = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
        this.entityCount = count;
    }

    public static RitualTableRecipeBuilder ritualTable(ItemStack output) {
        return new RitualTableRecipeBuilder(output);
    }

    public static RitualTableRecipeBuilder summon(EntityType<?> entityType, int count) {
        if (count < 1) throw new IllegalArgumentException("Entity result count must be at least one");
        return new RitualTableRecipeBuilder(entityType, count);
    }

    public RitualTableRecipeBuilder tableInput(Ingredient ingredient) {
        if (centerIngredient != null) {
            throw new IllegalStateException("Table input must be added first.");
        }
        this.centerIngredient = ingredient;
        return this;
    }
    public RitualTableRecipeBuilder tableInput(Item item)     { return tableInput(Ingredient.of(item)); }
    public RitualTableRecipeBuilder tableInput(ItemStack stack){ return tableInput(Ingredient.of(stack)); }

    public RitualTableRecipeBuilder brazier(Ingredient ingredient) {
        this.offerings.add(ingredient);
        return this;
    }
    public RitualTableRecipeBuilder brazier(Item item)     { return brazier(Ingredient.of(item)); }
    public RitualTableRecipeBuilder brazier(ItemStack stack){ return brazier(Ingredient.of(stack)); }

    @Override
    public RitualTableRecipeBuilder unlockedBy(String name, CriterionTriggerInstance criterion) {
        this.criteria.put(name, criterion);
        this.advancement.addCriterion(name, criterion);
        return this;
    }

    public RitualTableRecipeBuilder unlockedByItem(String name, Item item) {
        return this.unlockedBy(name, InventoryChangeTrigger.TriggerInstance.hasItems(item));
    }

    @Override
    public RecipeBuilder group(@Nullable String group) { return this; }

    @Override
    public Item getResult() { return this.resultItem; }

    @Override
    public void save(Consumer<FinishedRecipe> out, ResourceLocation id) {
        if (this.centerIngredient == null) {
            throw new IllegalStateException("Ritual Table recipe requires at least 1 ingredient (the table input).");
        }
        this.advancement.parent(new ResourceLocation("recipes/root"))
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(RequirementsStrategy.OR);

        ResourceLocation advId = id.withPrefix("recipes/ritual_table/");

        out.accept(new Result(
                id,
                this.centerIngredient,
                this.offerings,
                this.outputStack,
                this.entityResult,
                this.entityCount,
                this.advancement,
                advId
        ));
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient centerIngredient;
        private final NonNullList<Ingredient> offerings;
        private final ItemStack output;
        private final ResourceLocation entityResult;
        private final int entityCount;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id,
                      Ingredient centerIngredient,
                      NonNullList<Ingredient> offerings,
                      ItemStack output,
                      ResourceLocation entityResult,
                      int entityCount,
                      Advancement.Builder advancement,
                      ResourceLocation advancementId) {
            this.id = id;
            this.centerIngredient = centerIngredient;
            this.offerings = offerings;
            this.output = output.copy();
            this.entityResult = entityResult;
            this.entityCount = entityCount;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty("type", "hexalia:ritual_table");

            json.add("center", centerIngredient.toJson());
            JsonArray arr = new JsonArray();
            for (Ingredient offering : offerings) arr.add(offering.toJson());
            json.add("offerings", arr);

            if (entityResult != null) {
                json.addProperty("requires_soul", true);
                JsonObject result = new JsonObject();
                result.addProperty("type", "entity");
                result.addProperty("entity", entityResult.toString());
                result.addProperty("count", entityCount);
                json.add("result", result);
            } else {
                JsonObject out = new JsonObject();
                out.addProperty("item", ForgeRegistries.ITEMS.getKey(this.output.getItem()).toString());
                if (this.output.getCount() > 1) out.addProperty("count", this.output.getCount());
                json.add("output", out);
            }
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return RitualTableRecipe.Serializer.INSTANCE;
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
