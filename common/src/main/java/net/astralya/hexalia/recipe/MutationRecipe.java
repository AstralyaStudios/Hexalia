package net.astralya.hexalia.recipe;

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

public class MutationRecipe implements Recipe<SimpleContainer> {

  private final ResourceLocation id;
  private final Ingredient inputItem;
  private final ItemStack output;

  public MutationRecipe(ResourceLocation id, Ingredient inputItem, ItemStack output) {
    this.id = id;
    this.inputItem = inputItem;
    this.output = output;
  }

  @Override
  public boolean matches(SimpleContainer container, Level level) {
    if (level.isClientSide()) return false;
    return inputItem.test(container.getItem(0));
  }

  @Override
  public ItemStack assemble(SimpleContainer container, RegistryAccess registryAccess) {
    return output.copy();
  }

  @Override
  public boolean canCraftInDimensions(int w, int h) {
    return true;
  }

  @Override
  public ItemStack getResultItem(RegistryAccess registryAccess) {
    return output.copy();
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    NonNullList<Ingredient> list = NonNullList.create();
    list.add(inputItem);
    return list;
  }

  public Ingredient getInput() {
    return inputItem;
  }

  @Override
  public ResourceLocation getId() {
    return id;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return Serializer.INSTANCE;
  }

  @Override
  public RecipeType<?> getType() {
    return Type.INSTANCE;
  }

  public static class Type implements RecipeType<MutationRecipe> {
    public static final Type INSTANCE = new Type();
    public static final String ID = "mutation";
  }

  public static class Serializer implements RecipeSerializer<MutationRecipe> {
    public static final Serializer INSTANCE = new Serializer();

    @Override
    public MutationRecipe fromJson(ResourceLocation id, JsonObject json) {
      Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));
      ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
      return new MutationRecipe(id, input, output);
    }

    @Override
    public MutationRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
      Ingredient input = Ingredient.fromNetwork(buf);
      ItemStack output = buf.readItem();
      return new MutationRecipe(id, input, output);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, MutationRecipe recipe) {
      recipe.inputItem.toNetwork(buf);
      buf.writeItem(recipe.output);
    }
  }
}
