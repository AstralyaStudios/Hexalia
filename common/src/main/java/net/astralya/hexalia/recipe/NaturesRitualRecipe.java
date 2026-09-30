package net.astralya.hexalia.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import net.astralya.hexalia.HexaliaConfig;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record NaturesRitualRecipe(
    Ingredient centerIngredient,
    NonNullList<Ingredient> offerings,
    RitualResult result,
    boolean requiresSoul,
    RitualKind ritualKind,
    Optional<Integer> energyCost)
    implements Recipe<NaturesRitualRecipeInput> {
  public enum RitualKind {
    NATURE,
    CELESTIAL,
    SUMMONING
  }

  public sealed interface RitualResult permits ItemResult, EntityResult {}

  public record ItemResult(ItemStack stack) implements RitualResult {
    public ItemResult {
      stack = stack.copy();
    }
  }

  public record EntityResult(ResourceLocation entity, int count) implements RitualResult {
    public EntityResult {
      if (count < 1) throw new IllegalArgumentException("Entity result count must be at least one");
    }
  }

  public NaturesRitualRecipe {
    if (energyCost.isPresent() && energyCost.get() < 0)
      throw new IllegalArgumentException("Ritual energy cost cannot be negative");
    if (ritualKind == RitualKind.CELESTIAL && energyCost.isEmpty())
      throw new IllegalArgumentException("Celestial rituals require energy_cost");
    if ((ritualKind == RitualKind.SUMMONING) != requiresSoul
        || (ritualKind == RitualKind.SUMMONING) != (result instanceof EntityResult))
      throw new IllegalArgumentException("Summoning requires a soul and entity result");
    NonNullList<Ingredient> copy = NonNullList.create();
    copy.addAll(offerings);
    offerings = copy;
  }

  public NaturesRitualRecipe(
      Ingredient center, NonNullList<Ingredient> offerings, ItemStack output) {
    this(center, offerings, new ItemResult(output), false, RitualKind.NATURE, Optional.empty());
  }

  public NaturesRitualRecipe(NonNullList<Ingredient> ingredients, ItemStack output) {
    this(
        center(ingredients),
        offeringList(ingredients),
        new ItemResult(output),
        false,
        RitualKind.NATURE,
        Optional.empty());
  }

  public int resolvedEnergyCost() {
    return energyCost.orElseGet(HexaliaConfig::naturesRitualCropRequirement);
  }

  private static Ingredient center(List<Ingredient> ingredients) {
    if (ingredients.isEmpty())
      throw new IllegalArgumentException("Nature's Ritual requires a center ingredient");
    return ingredients.getFirst();
  }

  private static NonNullList<Ingredient> offeringList(List<Ingredient> ingredients) {
    NonNullList<Ingredient> result = NonNullList.create();
    if (ingredients.size() > 1) result.addAll(ingredients.subList(1, ingredients.size()));
    return result;
  }

  public boolean isItemResult() {
    return result instanceof ItemResult;
  }

  public boolean isEntityResult() {
    return result instanceof EntityResult;
  }

  public ItemStack itemResult() {
    return result instanceof ItemResult item ? item.stack().copy() : ItemStack.EMPTY;
  }

  public Optional<EntityResult> entityResult() {
    return result instanceof EntityResult entity ? Optional.of(entity) : Optional.empty();
  }

  public ItemStack output() {
    return itemResult();
  }

  public NonNullList<Ingredient> ingredients() {
    NonNullList<Ingredient> result = NonNullList.create();
    result.add(centerIngredient);
    result.addAll(offerings);
    return result;
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return ingredients();
  }

  @Override
  public boolean matches(NaturesRitualRecipeInput input, Level level) {
    return !level.isClientSide() && centerIngredient.test(input.getItem(0));
  }

  @Override
  public ItemStack assemble(NaturesRitualRecipeInput input, HolderLookup.Provider registries) {
    return itemResult();
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width * height >= offerings.size() + 1;
  }

  @Override
  public ItemStack getResultItem(HolderLookup.Provider registries) {
    return itemResult();
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModRecipeTypes.NATURES_RITUAL_SERIALIZER.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipeTypes.NATURES_RITUAL.get();
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  private record EntityJson(String type, ResourceLocation entity, int count) {
    private static final Codec<EntityJson> CODEC =
        RecordCodecBuilder.create(
            instance ->
                instance
                    .group(
                        Codec.STRING.fieldOf("type").forGetter(EntityJson::type),
                        ResourceLocation.CODEC.fieldOf("entity").forGetter(EntityJson::entity),
                        Codec.INT.fieldOf("count").forGetter(EntityJson::count))
                    .apply(instance, EntityJson::new));
  }

  private record Serialized(
      Optional<List<Ingredient>> ingredients,
      Optional<Ingredient> center,
      Optional<List<Ingredient>> offerings,
      Optional<ItemStack> output,
      Optional<EntityJson> result,
      boolean requiresSoul,
      Optional<RitualKind> ritualKind,
      Optional<Integer> energyCost) {
    private static Serialized fromRecipe(NaturesRitualRecipe recipe) {
      Optional<ItemStack> item =
          recipe.result instanceof ItemResult value ? Optional.of(value.stack()) : Optional.empty();
      Optional<EntityJson> entity =
          recipe.result instanceof EntityResult value
              ? Optional.of(new EntityJson("entity", value.entity(), value.count()))
              : Optional.empty();
      return new Serialized(
          Optional.empty(),
          Optional.of(recipe.centerIngredient),
          Optional.of(List.copyOf(recipe.offerings)),
          item,
          entity,
          recipe.requiresSoul,
          Optional.of(recipe.ritualKind),
          recipe.energyCost);
    }
  }

  public static class Serializer implements RecipeSerializer<NaturesRitualRecipe> {
    private static final MapCodec<Serialized> RAW_CODEC =
        RecordCodecBuilder.mapCodec(
            instance ->
                instance
                    .group(
                        Ingredient.CODEC_NONEMPTY
                            .listOf()
                            .optionalFieldOf("ingredients")
                            .forGetter(Serialized::ingredients),
                        Ingredient.CODEC_NONEMPTY
                            .optionalFieldOf("center")
                            .forGetter(Serialized::center),
                        Ingredient.CODEC_NONEMPTY
                            .listOf()
                            .optionalFieldOf("offerings")
                            .forGetter(Serialized::offerings),
                        BuiltInRegistries.ITEM
                            .byNameCodec()
                            .xmap(ItemStack::new, ItemStack::getItem)
                            .optionalFieldOf("output")
                            .forGetter(Serialized::output),
                        EntityJson.CODEC.optionalFieldOf("result").forGetter(Serialized::result),
                        Codec.BOOL
                            .optionalFieldOf("requires_soul", false)
                            .forGetter(Serialized::requiresSoul),
                        Codec.STRING
                            .comapFlatMap(
                                value -> {
                                  try {
                                    return DataResult.success(
                                        RitualKind.valueOf(
                                            value.toUpperCase(java.util.Locale.ROOT)));
                                  } catch (IllegalArgumentException exception) {
                                    return DataResult.error(() -> "Unknown ritual_kind: " + value);
                                  }
                                },
                                value -> value.name().toLowerCase(java.util.Locale.ROOT))
                            .optionalFieldOf("ritual_kind")
                            .forGetter(Serialized::ritualKind),
                        Codec.INT.optionalFieldOf("energy_cost").forGetter(Serialized::energyCost))
                    .apply(instance, Serialized::new));
    public static final MapCodec<NaturesRitualRecipe> CODEC =
        RAW_CODEC.flatXmap(
            Serializer::decode, recipe -> DataResult.success(Serialized.fromRecipe(recipe)));
    public static final StreamCodec<RegistryFriendlyByteBuf, NaturesRitualRecipe> STREAM_CODEC =
        StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

    private static DataResult<NaturesRitualRecipe> decode(Serialized value) {
      boolean legacy = value.ingredients.isPresent();
      boolean explicit = value.center.isPresent() || value.offerings.isPresent();
      if (legacy && explicit)
        return DataResult.error(
            () -> "Nature's Ritual cannot define both ingredients and center/offerings");
      if (!legacy && value.center.isEmpty())
        return DataResult.error(() -> "Nature's Ritual requires either ingredients or center");
      if (value.output.isPresent() == value.result.isPresent())
        return DataResult.error(
            () -> "Nature's Ritual requires exactly one item output or entity result");
      RitualResult result;
      if (value.output.isPresent()) result = new ItemResult(value.output.orElseThrow());
      else {
        EntityJson entity = value.result.orElseThrow();
        if (!entity.type.equals("entity"))
          return DataResult.error(() -> "Unsupported Nature's Ritual result type: " + entity.type);
        if (entity.count < 1)
          return DataResult.error(() -> "Entity result count must be at least one");
        result = new EntityResult(entity.entity, entity.count);
      }
      Ingredient center;
      NonNullList<Ingredient> offerings = NonNullList.create();
      if (legacy) {
        List<Ingredient> all = value.ingredients.orElseThrow();
        if (all.isEmpty())
          return DataResult.error(
              () -> "Nature's Ritual ingredients must contain a center ingredient");
        center = all.getFirst();
        offerings.addAll(all.subList(1, all.size()));
      } else {
        center = value.center.orElseThrow();
        value.offerings.ifPresent(offerings::addAll);
      }
      try {
        return DataResult.success(
            new NaturesRitualRecipe(
                center,
                offerings,
                result,
                value.requiresSoul,
                value.ritualKind.orElse(
                    value.requiresSoul ? RitualKind.SUMMONING : RitualKind.NATURE),
                value.energyCost));
      } catch (IllegalArgumentException exception) {
        return DataResult.error(exception::getMessage);
      }
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, NaturesRitualRecipe recipe) {
      Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.centerIngredient);
      buffer.writeVarInt(recipe.offerings.size());
      recipe.offerings.forEach(value -> Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, value));
      buffer.writeBoolean(recipe.isItemResult());
      if (recipe.result instanceof ItemResult item)
        ItemStack.STREAM_CODEC.encode(buffer, item.stack());
      else {
        EntityResult entity = (EntityResult) recipe.result;
        buffer.writeResourceLocation(entity.entity());
        buffer.writeVarInt(entity.count());
      }
      buffer.writeBoolean(recipe.requiresSoul);
      buffer.writeEnum(recipe.ritualKind);
      buffer.writeBoolean(recipe.energyCost.isPresent());
      recipe.energyCost.ifPresent(buffer::writeVarInt);
    }

    private static NaturesRitualRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
      Ingredient center = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
      NonNullList<Ingredient> offerings =
          NonNullList.withSize(buffer.readVarInt(), Ingredient.EMPTY);
      offerings.replaceAll(ignored -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
      RitualResult result =
          buffer.readBoolean()
              ? new ItemResult(ItemStack.STREAM_CODEC.decode(buffer))
              : new EntityResult(buffer.readResourceLocation(), buffer.readVarInt());
      boolean soul = buffer.readBoolean();
      RitualKind kind = buffer.readEnum(RitualKind.class);
      Optional<Integer> cost =
          buffer.readBoolean() ? Optional.of(buffer.readVarInt()) : Optional.empty();
      return new NaturesRitualRecipe(center, offerings, result, soul, kind, cost);
    }

    @Override
    public MapCodec<NaturesRitualRecipe> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, NaturesRitualRecipe> streamCodec() {
      return STREAM_CODEC;
    }
  }
}
