package net.astralya.hexalia.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.Optional;

public final class RitualTableRecipe implements Recipe<SimpleInventory> {
    public static final int INPUT_SLOTS = 1;

    public sealed interface RitualResult permits ItemResult, EntityResult {}
    public record ItemResult(ItemStack stack) implements RitualResult {
        public ItemResult { stack = stack.copy(); }
    }
    public record EntityResult(Identifier entityId, int count) implements RitualResult {
        public EntityResult {
            if (count < 1) throw new IllegalArgumentException("Entity result count must be at least one");
        }
    }

    private final Identifier id;
    private final Ingredient centerIngredient;
    private final DefaultedList<Ingredient> offerings;
    private final RitualResult result;
    private final boolean requiresSoul;

    public RitualTableRecipe(Identifier id, Ingredient centerIngredient, DefaultedList<Ingredient> offerings,
                             RitualResult result, boolean requiresSoul) {
        this.id = id;
        this.centerIngredient = centerIngredient;
        this.offerings = DefaultedList.copyOf(Ingredient.EMPTY, offerings.toArray(Ingredient[]::new));
        this.result = result;
        this.requiresSoul = requiresSoul || result instanceof EntityResult;
    }

    public RitualTableRecipe(Identifier id, Ingredient centerIngredient, DefaultedList<Ingredient> offerings, ItemStack output) {
        this(id, centerIngredient, offerings, new ItemResult(output), false);
    }

    public RitualTableRecipe(Identifier id, DefaultedList<Ingredient> ingredients, ItemStack output) {
        this(id, legacyCenter(ingredients), legacyOfferings(ingredients), output);
    }

    private static Ingredient legacyCenter(DefaultedList<Ingredient> ingredients) {
        if (ingredients.isEmpty()) throw new IllegalArgumentException("Ritual Table recipe requires a center ingredient");
        return ingredients.get(0);
    }

    private static DefaultedList<Ingredient> legacyOfferings(DefaultedList<Ingredient> ingredients) {
        DefaultedList<Ingredient> result = DefaultedList.of();
        for (int i = 1; i < ingredients.size(); i++) result.add(ingredients.get(i));
        return result;
    }

    public Ingredient centerIngredient() { return centerIngredient; }
    public DefaultedList<Ingredient> offerings() { return DefaultedList.copyOf(Ingredient.EMPTY, offerings.toArray(Ingredient[]::new)); }
    public RitualResult result() { return result; }
    public boolean requiresSoul() { return requiresSoul; }
    public ItemStack itemResult() { return result instanceof ItemResult item ? item.stack().copy() : ItemStack.EMPTY; }
    public Optional<EntityResult> entityResult() { return result instanceof EntityResult entity ? Optional.of(entity) : Optional.empty(); }

    @Override public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> ingredients = DefaultedList.of();
        ingredients.add(centerIngredient);
        ingredients.addAll(offerings);
        return ingredients;
    }
    @Override public boolean matches(SimpleInventory inv, World world) { return !world.isClient && centerIngredient.test(inv.getStack(0)); }
    @Override public ItemStack craft(SimpleInventory inv, DynamicRegistryManager registries) { return itemResult(); }
    @Override public boolean fits(int width, int height) { return width * height >= offerings.size() + 1; }
    @Override public ItemStack getOutput(DynamicRegistryManager registries) { return itemResult(); }
    @Override public Identifier getId() { return id; }
    @Override public RecipeSerializer<?> getSerializer() { return ModRecipes.RITUAL_TABLE_SERIALIZER; }
    @Override public RecipeType<?> getType() { return ModRecipes.RITUAL_TABLE_TYPE; }

    public static final class Serializer implements RecipeSerializer<RitualTableRecipe> {
        @Override public RitualTableRecipe read(Identifier id, JsonObject json) {
            boolean legacy = json.has("ingredients");
            boolean explicit = json.has("center") || json.has("offerings");
            if (legacy && explicit) throw new JsonParseException("Nature's Ritual cannot define both ingredients and center/offerings");
            if (!legacy && !json.has("center")) throw new JsonParseException("Nature's Ritual requires either ingredients or center");

            Ingredient center;
            DefaultedList<Ingredient> offerings = DefaultedList.of();
            if (legacy) {
                JsonArray ingredients = json.getAsJsonArray("ingredients");
                if (ingredients.isEmpty()) throw new JsonParseException("Nature's Ritual ingredients must contain a center ingredient");
                center = Ingredient.fromJson(ingredients.get(0));
                for (int i = 1; i < ingredients.size(); i++) offerings.add(Ingredient.fromJson(ingredients.get(i)));
            } else {
                center = Ingredient.fromJson(json.get("center"));
                if (json.has("offerings")) {
                    JsonArray values = json.getAsJsonArray("offerings");
                    for (int i = 0; i < values.size(); i++) offerings.add(Ingredient.fromJson(values.get(i)));
                }
            }

            boolean hasOutput = json.has("output");
            boolean hasResult = json.has("result");
            if (hasOutput == hasResult) throw new JsonParseException("Nature's Ritual requires exactly one item output or entity result");
            RitualResult result;
            if (hasOutput) {
                result = new ItemResult(ShapedRecipe.outputFromJson(json.getAsJsonObject("output")));
            } else {
                JsonObject resultJson = json.getAsJsonObject("result");
                String type = resultJson.has("type") ? resultJson.get("type").getAsString() : "entity";
                if (!"entity".equals(type)) throw new JsonParseException("Unsupported Nature's Ritual result type: " + type);
                int count = resultJson.has("count") ? resultJson.get("count").getAsInt() : 1;
                if (count < 1) throw new JsonParseException("Entity result count must be at least one");
                result = new EntityResult(new Identifier(resultJson.get("entity").getAsString()), count);
            }
            return new RitualTableRecipe(id, center, offerings, result,
                    json.has("requires_soul") && json.get("requires_soul").getAsBoolean());
        }

        @Override public RitualTableRecipe read(Identifier id, PacketByteBuf buf) {
            Ingredient center = Ingredient.fromPacket(buf);
            DefaultedList<Ingredient> offerings = DefaultedList.ofSize(buf.readVarInt(), Ingredient.EMPTY);
            for (int i = 0; i < offerings.size(); i++) offerings.set(i, Ingredient.fromPacket(buf));
            RitualResult result = buf.readBoolean()
                    ? new ItemResult(buf.readItemStack())
                    : new EntityResult(buf.readIdentifier(), buf.readVarInt());
            return new RitualTableRecipe(id, center, offerings, result, buf.readBoolean());
        }

        @Override public void write(PacketByteBuf buf, RitualTableRecipe recipe) {
            recipe.centerIngredient.write(buf);
            buf.writeVarInt(recipe.offerings.size());
            for (Ingredient ingredient : recipe.offerings) ingredient.write(buf);
            buf.writeBoolean(recipe.result instanceof ItemResult);
            if (recipe.result instanceof ItemResult item) buf.writeItemStack(item.stack());
            else {
                EntityResult entity = (EntityResult) recipe.result;
                buf.writeIdentifier(entity.entityId());
                buf.writeVarInt(entity.count());
            }
            buf.writeBoolean(recipe.requiresSoul);
        }
    }
}
