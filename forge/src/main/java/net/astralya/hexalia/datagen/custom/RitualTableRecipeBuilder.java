package net.astralya.hexalia.forge.datagen.custom;

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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class RitualTableRecipeBuilder implements RecipeBuilder {

    private final NonNullList<Ingredient> ingredients = NonNullList.create();
    private final ItemStack outputStack;
    private final Item resultItem;
    private int energyCost = -1;
    private String ritualKind = "nature";
    private @Nullable ResourceLocation entityResult;
    private int entityCount;

    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    private final Map<String, CriterionTriggerInstance> criteria = new LinkedHashMap<>();

    public RitualTableRecipeBuilder(ItemStack output) {
        this.outputStack = output.copy();
        this.resultItem = this.outputStack.getItem();
    }

    public static RitualTableRecipeBuilder ritualTable(ItemStack output) {
        return new RitualTableRecipeBuilder(output);
    }

    public RitualTableRecipeBuilder tableInput(Ingredient ingredient) {
        if (!ingredients.isEmpty()) {
            throw new IllegalStateException("Table input must be added first.");
        }
        this.ingredients.add(ingredient);
        return this;
    }
    public RitualTableRecipeBuilder tableInput(Item item)     { return tableInput(Ingredient.of(item)); }
    public RitualTableRecipeBuilder tableInput(ItemStack stack){ return tableInput(Ingredient.of(stack)); }

    public RitualTableRecipeBuilder brazier(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        return this;
    }
    public RitualTableRecipeBuilder brazier(Item item)     { return brazier(Ingredient.of(item)); }
    public RitualTableRecipeBuilder brazier(ItemStack stack){ return brazier(Ingredient.of(stack)); }

    public RitualTableRecipeBuilder energyCost(int cost) {
        if (cost < 0) throw new IllegalArgumentException("energy_cost must be nonnegative");
        this.energyCost = cost;
        return this;
    }

    public RitualTableRecipeBuilder ritualKind(String kind) {
        if (!kind.equals("nature") && !kind.equals("celestial") && !kind.equals("summoning"))
            throw new IllegalArgumentException("Unknown ritual kind: " + kind);
        this.ritualKind = kind;
        return this;
    }

    public RitualTableRecipeBuilder entityResult(ResourceLocation entity, int count) {
        if (count < 1) throw new IllegalArgumentException("Entity count must be positive");
        this.entityResult = entity;
        this.entityCount = count;
        return this;
    }

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
        if (this.energyCost < 0) throw new IllegalStateException("Missing energy_cost: " + id);
        if (this.outputStack.isEmpty() != (this.entityResult != null))
            throw new IllegalStateException("Expected exactly one ritual result: " + id);
        if (this.ingredients.isEmpty()) {
            throw new IllegalStateException("Ritual Table recipe requires at least 1 ingredient (the table input).");
        }
        if (this.ingredients.size() > RitualTableRecipe.INPUT_SLOTS + 1) { // 1 table + up to 4 braziers
            throw new IllegalStateException("Too many ingredients for Ritual Table (max " + (RitualTableRecipe.INPUT_SLOTS + 1) + ").");
        }

        this.advancement.parent(ResourceLocation.parse("recipes/root"))
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(RequirementsStrategy.OR);

        ResourceLocation advId = id.withPrefix("recipes/ritual_table/");

        out.accept(new Result(
                id,
                this.ingredients,
                this.outputStack,
                this.energyCost,
                this.ritualKind,
                this.entityResult,
                this.entityCount,
                this.advancement,
                advId
        ));
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final NonNullList<Ingredient> ingredients;
        private final ItemStack output;
        private final int energyCost;
        private final String ritualKind;
        private final @Nullable ResourceLocation entityResult;
        private final int entityCount;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id,
                      NonNullList<Ingredient> ingredients,
                      ItemStack output,
                      int energyCost,
                      String ritualKind,
                      @Nullable ResourceLocation entityResult,
                      int entityCount,
                      Advancement.Builder advancement,
                      ResourceLocation advancementId) {
            this.id = id;
            this.ingredients = ingredients;
            this.output = output.copy();
            this.energyCost = energyCost;
            this.ritualKind = ritualKind;
            this.entityResult = entityResult;
            this.entityCount = entityCount;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty("type", "hexalia:natures_ritual");
            json.addProperty("ritual_kind", ritualKind);
            json.addProperty("energy_cost", energyCost);
            json.add("center", ingredients.get(0).toJson());

            JsonArray offerings = new JsonArray();
            for (int i = 1; i < ingredients.size(); i++) {
                offerings.add(ingredients.get(i).toJson());
            }
            json.add("offerings", offerings);

            if (this.entityResult != null) {
                JsonObject result = new JsonObject();
                result.addProperty("type", "entity");
                result.addProperty("entity", this.entityResult.toString());
                result.addProperty("count", this.entityCount);
                json.add("result", result);
                json.addProperty("requires_soul", true);
            } else if (this.output.getCount() == 1) {
                json.addProperty("output", ForgeRegistries.ITEMS.getKey(this.output.getItem()).toString());
            } else {
                JsonObject out = new JsonObject();
                out.addProperty("item", ForgeRegistries.ITEMS.getKey(this.output.getItem()).toString());
                out.addProperty("count", this.output.getCount());
                json.add("output", out);
            }
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return RitualTableRecipe.Serializer.NATURES_RITUAL_INSTANCE;
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
