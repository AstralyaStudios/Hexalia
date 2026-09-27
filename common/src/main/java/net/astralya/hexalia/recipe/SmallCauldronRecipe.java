package net.astralya.hexalia.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.astralya.hexalia.item.ModItems;
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

public class SmallCauldronRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack output;
    private final float experience;
    private final int duration;

    public SmallCauldronRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack output, float experience, int duration) {
        this.id = id;
        this.ingredients = ingredients;
        this.output = output;
        this.experience = experience;
        this.duration = duration;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        int inputCount = 0;
        for (int i = 0; i < container.getContainerSize(); i++) {
            if (!container.getItem(i).isEmpty()) {
                inputCount++;
            }
        }

        if (inputCount != this.ingredients.size()) {
            return false;
        }

        boolean[] used = new boolean[container.getContainerSize()];

        for (Ingredient ingredient : this.ingredients) {
            boolean found = false;

            for (int i = 0; i < container.getContainerSize(); i++) {
                if (!used[i] && ingredient.test(container.getItem(i))) {
                    used[i] = true;
                    found = true;
                    break;
                }
            }

            if (!found) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(SimpleContainer container, RegistryAccess registryAccess) {
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= this.ingredients.size();
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.output.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public float getExperience() {
        return this.experience;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getBrewTime() {
        return this.duration;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ModItems.SMALL_CAULDRON.get());
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<SmallCauldronRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "small_cauldron";
    }

    public static class Serializer implements RecipeSerializer<SmallCauldronRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public SmallCauldronRecipe fromJson(ResourceLocation id, JsonObject json) {
            JsonArray ingredientArray = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> ingredients = NonNullList.create();

            for (int i = 0; i < ingredientArray.size(); i++) {
                ingredients.add(Ingredient.fromJson(ingredientArray.get(i)));
            }

            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            float experience = GsonHelper.getAsFloat(json, "experience", 0.0F);
            int duration = GsonHelper.getAsInt(json, "duration", 200);

            return new SmallCauldronRecipe(id, ingredients, output, experience, duration);
        }

        @Override
        public SmallCauldronRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            int size = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);

            for (int i = 0; i < size; i++) {
                ingredients.set(i, Ingredient.fromNetwork(buffer));
            }

            ItemStack output = buffer.readItem();
            float experience = buffer.readFloat();
            int duration = buffer.readInt();

            return new SmallCauldronRecipe(id, ingredients, output, experience, duration);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, SmallCauldronRecipe recipe) {
            buffer.writeVarInt(recipe.ingredients.size());

            for (Ingredient ingredient : recipe.ingredients) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.output);
            buffer.writeFloat(recipe.experience);
            buffer.writeInt(recipe.duration);
        }
    }
}
