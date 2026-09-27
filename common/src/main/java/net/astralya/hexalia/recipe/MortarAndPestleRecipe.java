package net.astralya.hexalia.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

import java.util.ArrayList;
import java.util.List;

public class MortarAndPestleRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack output;

    public MortarAndPestleRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack output) {
        this.id = id;
        this.ingredients = ingredients;
        this.output = output;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                stacks.add(stack);
            }
        }

        if (stacks.size() != this.ingredients.size()) {
            return false;
        }

        boolean[] used = new boolean[stacks.size()];
        return matchIngredients(this.ingredients, stacks, used, 0);
    }

    private static boolean matchIngredients(List<Ingredient> ingredients, List<ItemStack> stacks, boolean[] used, int index) {
        if (index >= ingredients.size()) {
            return true;
        }

        Ingredient ingredient = ingredients.get(index);
        for (int i = 0; i < stacks.size(); i++) {
            if (used[i]) {
                continue;
            }
            if (!ingredient.test(stacks.get(i))) {
                continue;
            }
            used[i] = true;
            if (matchIngredients(ingredients, stacks, used, index + 1)) {
                return true;
            }
            used[i] = false;
        }

        return false;
    }

    @Override
    public ItemStack assemble(SimpleContainer container, RegistryAccess registryAccess) {
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.output.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
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

    public static class Type implements RecipeType<MortarAndPestleRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "mortar_and_pestle";
    }

    public static class Serializer implements RecipeSerializer<MortarAndPestleRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public MortarAndPestleRecipe fromJson(ResourceLocation id, JsonObject json) {
            JsonArray ingredientArray = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> ingredients = NonNullList.create();

            for (int i = 0; i < ingredientArray.size(); i++) {
                ingredients.add(Ingredient.fromJson(ingredientArray.get(i)));
            }

            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            return new MortarAndPestleRecipe(id, ingredients, output);
        }

        @Override
        public MortarAndPestleRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            int size = buf.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);

            for (int i = 0; i < size; i++) {
                ingredients.set(i, Ingredient.fromNetwork(buf));
            }

            ItemStack output = buf.readItem();
            return new MortarAndPestleRecipe(id, ingredients, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, MortarAndPestleRecipe recipe) {
            buf.writeVarInt(recipe.ingredients.size());

            for (Ingredient ingredient : recipe.ingredients) {
                ingredient.toNetwork(buf);
            }

            buf.writeItem(recipe.output);
        }
    }
}
