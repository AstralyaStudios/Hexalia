package net.astralya.hexalia.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class RitualTableRecipe implements Recipe<SimpleContainer> {
    public static final int INPUT_SLOTS = 1;

    public sealed interface RitualResult permits ItemResult, EntityResult {}
    public record ItemResult(ItemStack stack) implements RitualResult {
        public ItemResult { stack = stack.copy(); }
    }
    public record EntityResult(ResourceLocation entityId, int count) implements RitualResult {
        public EntityResult {
            if (count < 1) throw new IllegalArgumentException("Entity result count must be at least one");
        }
    }

    private final ResourceLocation id;
    private final Ingredient centerIngredient;
    private final NonNullList<Ingredient> offerings;
    private final RitualResult result;
    private final boolean requiresSoul;

    public RitualTableRecipe(ResourceLocation id, Ingredient centerIngredient, NonNullList<Ingredient> offerings,
                             RitualResult result, boolean requiresSoul) {
        this.id = id;
        this.centerIngredient = centerIngredient;
        this.offerings = NonNullList.create();
        this.offerings.addAll(offerings);
        this.result = result;
        this.requiresSoul = requiresSoul || result instanceof EntityResult;
    }

    public RitualTableRecipe(ResourceLocation id, Ingredient centerIngredient, NonNullList<Ingredient> offerings, ItemStack output) {
        this(id, centerIngredient, offerings, new ItemResult(output), false);
    }

    public RitualTableRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack output) {
        this(id, legacyCenter(ingredients), legacyOfferings(ingredients), output);
    }

    private static Ingredient legacyCenter(NonNullList<Ingredient> ingredients) {
        if (ingredients.isEmpty()) throw new IllegalArgumentException("Ritual Table recipe requires a center ingredient");
        return ingredients.get(0);
    }

    private static NonNullList<Ingredient> legacyOfferings(NonNullList<Ingredient> ingredients) {
        NonNullList<Ingredient> result = NonNullList.create();
        if (ingredients.size() > 1) result.addAll(ingredients.subList(1, ingredients.size()));
        return result;
    }

    public Ingredient centerIngredient() { return centerIngredient; }
    public NonNullList<Ingredient> offerings() {
        NonNullList<Ingredient> copy = NonNullList.create();
        copy.addAll(offerings);
        return copy;
    }
    public RitualResult result() { return result; }
    public boolean requiresSoul() { return requiresSoul; }
    public ItemStack itemResult() { return result instanceof ItemResult item ? item.stack().copy() : ItemStack.EMPTY; }
    public Optional<EntityResult> entityResult() {
        return result instanceof EntityResult entity ? Optional.of(entity) : Optional.empty();
    }

    @Override public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(centerIngredient);
        ingredients.addAll(offerings);
        return ingredients;
    }
    @Override public boolean matches(SimpleContainer inv, Level level) {
        return !level.isClientSide && centerIngredient.test(inv.getItem(0));
    }
    @Override public ItemStack assemble(SimpleContainer inv, RegistryAccess access) { return itemResult(); }
    @Override public boolean canCraftInDimensions(int width, int height) { return width * height >= offerings.size() + 1; }
    @Override public ItemStack getResultItem(RegistryAccess access) { return itemResult(); }
    @Override public ResourceLocation getId() { return id; }
    @Override public RecipeSerializer<?> getSerializer() { return Serializer.INSTANCE; }
    @Override public RecipeType<?> getType() { return Type.INSTANCE; }

    public static class Type implements RecipeType<RitualTableRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "ritual_table";
    }

    public static class Serializer implements RecipeSerializer<RitualTableRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        @Override public RitualTableRecipe fromJson(ResourceLocation id, JsonObject json) {
            boolean legacy = json.has("ingredients");
            boolean explicit = json.has("center") || json.has("offerings");
            if (legacy && explicit) throw new JsonParseException("Nature's Ritual cannot define both ingredients and center/offerings");
            if (!legacy && !json.has("center")) throw new JsonParseException("Nature's Ritual requires either ingredients or center");

            Ingredient center;
            NonNullList<Ingredient> offerings = NonNullList.create();
            if (legacy) {
                JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
                if (ingredients.size() == 0) throw new JsonParseException("Nature's Ritual ingredients must contain a center ingredient");
                center = Ingredient.fromJson(ingredients.get(0));
                for (int i = 1; i < ingredients.size(); i++) offerings.add(Ingredient.fromJson(ingredients.get(i)));
            } else {
                center = Ingredient.fromJson(json.get("center"));
                if (json.has("offerings")) {
                    for (var element : GsonHelper.getAsJsonArray(json, "offerings")) offerings.add(Ingredient.fromJson(element));
                }
            }

            boolean hasOutput = json.has("output");
            boolean hasResult = json.has("result");
            if (hasOutput == hasResult) throw new JsonParseException("Nature's Ritual requires exactly one item output or entity result");
            RitualResult result;
            if (hasOutput) {
                result = new ItemResult(ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output")));
            } else {
                JsonObject resultJson = GsonHelper.getAsJsonObject(json, "result");
                String type = GsonHelper.getAsString(resultJson, "type", "entity");
                if (!"entity".equals(type)) throw new JsonParseException("Unsupported Nature's Ritual result type: " + type);
                int count = GsonHelper.getAsInt(resultJson, "count", 1);
                if (count < 1) throw new JsonParseException("Entity result count must be at least one");
                result = new EntityResult(new ResourceLocation(GsonHelper.getAsString(resultJson, "entity")), count);
            }
            return new RitualTableRecipe(id, center, offerings, result,
                    GsonHelper.getAsBoolean(json, "requires_soul", false));
        }

        @Override public RitualTableRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            Ingredient center = Ingredient.fromNetwork(buffer);
            NonNullList<Ingredient> offerings = NonNullList.withSize(buffer.readVarInt(), Ingredient.EMPTY);
            for (int i = 0; i < offerings.size(); i++) offerings.set(i, Ingredient.fromNetwork(buffer));
            RitualResult result = buffer.readBoolean()
                    ? new ItemResult(buffer.readItem())
                    : new EntityResult(buffer.readResourceLocation(), buffer.readVarInt());
            return new RitualTableRecipe(id, center, offerings, result, buffer.readBoolean());
        }

        @Override public void toNetwork(FriendlyByteBuf buffer, RitualTableRecipe recipe) {
            recipe.centerIngredient.toNetwork(buffer);
            buffer.writeVarInt(recipe.offerings.size());
            for (Ingredient ingredient : recipe.offerings) ingredient.toNetwork(buffer);
            buffer.writeBoolean(recipe.result instanceof ItemResult);
            if (recipe.result instanceof ItemResult item) buffer.writeItem(item.stack());
            else {
                EntityResult entity = (EntityResult) recipe.result;
                buffer.writeResourceLocation(entity.entityId());
                buffer.writeVarInt(entity.count());
            }
            buffer.writeBoolean(recipe.requiresSoul);
        }
    }
}
