package net.astralya.hexalia.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Locale;
import java.util.Optional;
import net.astralya.hexalia.Configuration;
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
import org.jetbrains.annotations.Nullable;

public class RitualTableRecipe implements Recipe<SimpleContainer> {

  private final ResourceLocation id;
  private final NonNullList<Ingredient> ingredients;
  private final ItemStack output;
  private final @Nullable ResourceLocation entityResult;
  private final int entityCount;
  private final boolean requiresSoul;
  private final boolean naturesRitualSerializer;
  private final RitualKind ritualKind;
  private final Optional<Integer> energyCost;

  public static final int INPUT_SLOTS = 8;

  public enum RitualKind {
    NATURE,
    CELESTIAL,
    SUMMONING
  }

  public RitualTableRecipe(
      ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack output) {
    this(id, ingredients, output, null, 0, false, false);
  }

  public RitualTableRecipe(
      ResourceLocation id,
      NonNullList<Ingredient> ingredients,
      ItemStack output,
      @Nullable ResourceLocation entityResult,
      int entityCount,
      boolean requiresSoul,
      boolean naturesRitualSerializer) {
    this(id, ingredients, output, entityResult, entityCount, requiresSoul,
        naturesRitualSerializer,
        requiresSoul || entityResult != null ? RitualKind.SUMMONING : RitualKind.NATURE,
        Optional.empty());
  }

  public RitualTableRecipe(
      ResourceLocation id,
      NonNullList<Ingredient> ingredients,
      ItemStack output,
      @Nullable ResourceLocation entityResult,
      int entityCount,
      boolean requiresSoul,
      boolean naturesRitualSerializer,
      RitualKind ritualKind,
      Optional<Integer> energyCost) {
    this.id = id;
    this.ingredients = ingredients;
    this.output = output;
    this.entityResult = entityResult;
    this.entityCount = entityCount;
    this.requiresSoul = requiresSoul;
    this.naturesRitualSerializer = naturesRitualSerializer;
    this.ritualKind = ritualKind;
    this.energyCost = energyCost;
  }

  public Ingredient centerIngredient() {
    return ingredients.get(0);
  }

  public NonNullList<Ingredient> offerings() {
    NonNullList<Ingredient> offerings = NonNullList.create();
    for (int i = 1; i < ingredients.size(); i++) offerings.add(ingredients.get(i));
    return offerings;
  }

  public boolean requiresSoul() {
    return requiresSoul;
  }

  public RitualKind ritualKind() {
    return ritualKind;
  }

  public Optional<Integer> energyCost() {
    return energyCost;
  }

  public int resolvedEnergyCost() {
    return energyCost.orElseGet(() -> Configuration.NATURES_RITUAL_CROP_REQUIREMENT.get());
  }

  public boolean isEntityResult() {
    return entityResult != null;
  }

  public @Nullable ResourceLocation entityResult() {
    return entityResult;
  }

  public int entityCount() {
    return entityCount;
  }

  public NonNullList<Ingredient> getIngredients() {
    return ingredients;
  }

  @Override
  public boolean matches(SimpleContainer inv, Level level) {
    if (level.isClientSide) return false;
    if (ingredients.isEmpty()) return false;
    return ingredients.get(0).test(inv.getItem(0));
  }

  @Override
  public ItemStack assemble(SimpleContainer inv, RegistryAccess access) {
    return output.copy();
  }

  @Override
  public boolean canCraftInDimensions(int w, int h) {
    return w * h >= ingredients.size();
  }

  @Override
  public ItemStack getResultItem(RegistryAccess access) {
    return output;
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  @Override
  public ResourceLocation getId() {
    return id;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return naturesRitualSerializer ? Serializer.NATURES_RITUAL_INSTANCE : Serializer.INSTANCE;
  }

  @Override
  public RecipeType<?> getType() {
    return Type.INSTANCE;
  }

  public static class Type implements RecipeType<RitualTableRecipe> {
    public static final Type INSTANCE = new Type();
    public static final String ID = "ritual_table";
  }

  public static class Serializer implements RecipeSerializer<RitualTableRecipe> {
    public static final Serializer INSTANCE = new Serializer(false);
    public static final Serializer NATURES_RITUAL_INSTANCE = new Serializer(true);
    private final boolean naturesRitual;

    private Serializer(boolean naturesRitual) {
      this.naturesRitual = naturesRitual;
    }

    @Override
    public RitualTableRecipe fromJson(ResourceLocation id, JsonObject json) {
      NonNullList<Ingredient> list = NonNullList.create();
      boolean legacy = json.has("ingredients");
      if (legacy && (json.has("center") || json.has("offerings"))) {
        throw new IllegalArgumentException(
            "Nature's Ritual cannot combine ingredients with center/offerings");
      }
      if (legacy) {
        JsonArray arr = GsonHelper.getAsJsonArray(json, "ingredients");
        for (int i = 0; i < arr.size(); i++) list.add(Ingredient.fromJson(arr.get(i)));
      } else {
        list.add(Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "center")));
        if (json.has("offerings")) {
          JsonArray arr = GsonHelper.getAsJsonArray(json, "offerings");
          for (int i = 0; i < arr.size(); i++) list.add(Ingredient.fromJson(arr.get(i)));
        }
      }
      if (list.isEmpty()) {
        throw new IllegalArgumentException("RitualTable: ingredients array cannot be empty");
      }
      if (json.has("output") == json.has("result")) {
        throw new IllegalArgumentException("Nature's Ritual requires exactly one output or result");
      }
      ItemStack out = ItemStack.EMPTY;
      ResourceLocation entity = null;
      int entityCount = 0;
      if (json.has("output")) {
        if (json.get("output").isJsonObject()) {
          out = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
        } else {
          ResourceLocation itemId = new ResourceLocation(GsonHelper.getAsString(json, "output"));
          out = new ItemStack(net.minecraft.core.registries.BuiltInRegistries.ITEM.get(itemId));
        }
        if (out.isEmpty()) throw new IllegalArgumentException("Invalid Nature's Ritual output");
      } else {
        JsonObject result = GsonHelper.getAsJsonObject(json, "result");
        if (!"entity".equals(GsonHelper.getAsString(result, "type"))) {
          throw new IllegalArgumentException("Unsupported Nature's Ritual result type");
        }
        entity = new ResourceLocation(GsonHelper.getAsString(result, "entity"));
        entityCount = GsonHelper.getAsInt(result, "count");
        if (entityCount < 1
            || !net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.containsKey(entity)) {
          throw new IllegalArgumentException("Invalid Nature's Ritual entity result: " + entity);
        }
      }
      boolean requiresSoul = GsonHelper.getAsBoolean(json, "requires_soul", false);
      RitualKind ritualKind;
      try {
        ritualKind = RitualKind.valueOf(GsonHelper.getAsString(json, "ritual_kind",
            requiresSoul || entity != null ? "summoning" : "nature").toUpperCase(Locale.ROOT));
      } catch (IllegalArgumentException exception) {
        throw new IllegalArgumentException("Invalid Nature's Ritual ritual_kind", exception);
      }
      Optional<Integer> energyCost = json.has("energy_cost")
          ? Optional.of(GsonHelper.getAsInt(json, "energy_cost")) : Optional.empty();
      if (energyCost.isPresent() && energyCost.get() < 0)
        throw new IllegalArgumentException("Nature's Ritual energy_cost must be nonnegative");
      if (ritualKind == RitualKind.CELESTIAL && energyCost.isEmpty())
        throw new IllegalArgumentException("Celestial rituals require energy_cost");
      return new RitualTableRecipe(
          id,
          list,
          out,
          entity,
          entityCount,
          requiresSoul,
          naturesRitual,
          ritualKind,
          energyCost);
    }

    @Override
    public RitualTableRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
      int count = buf.readVarInt();
      NonNullList<Ingredient> list = NonNullList.withSize(count, Ingredient.EMPTY);
      for (int i = 0; i < count; i++) list.set(i, Ingredient.fromNetwork(buf));
      boolean item = buf.readBoolean();
      ItemStack out = item ? buf.readItem() : ItemStack.EMPTY;
      ResourceLocation entity = item ? null : buf.readResourceLocation();
      int entityCount = item ? 0 : buf.readVarInt();
      boolean requiresSoul = buf.readBoolean();
      RitualKind ritualKind = buf.readEnum(RitualKind.class);
      Optional<Integer> energyCost = buf.readBoolean()
          ? Optional.of(buf.readVarInt()) : Optional.empty();
      return new RitualTableRecipe(
          id, list, out, entity, entityCount, requiresSoul, naturesRitual,
          ritualKind, energyCost);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, RitualTableRecipe recipe) {
      buf.writeVarInt(recipe.ingredients.size());
      for (Ingredient ing : recipe.ingredients) ing.toNetwork(buf);
      buf.writeBoolean(!recipe.isEntityResult());
      if (recipe.isEntityResult()) {
        buf.writeResourceLocation(recipe.entityResult);
        buf.writeVarInt(recipe.entityCount);
      } else {
        buf.writeItem(recipe.output);
      }
      buf.writeBoolean(recipe.requiresSoul);
      buf.writeEnum(recipe.ritualKind);
      buf.writeBoolean(recipe.energyCost.isPresent());
      recipe.energyCost.ifPresent(buf::writeVarInt);
    }
  }
}
