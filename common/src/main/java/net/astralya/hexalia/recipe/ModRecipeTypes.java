package net.astralya.hexalia.recipe;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.astralya.hexalia.Hexalia;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public final class ModRecipeTypes {
  public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
      DeferredRegister.create(Hexalia.MOD_ID, Registries.RECIPE_SERIALIZER);
  public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
      DeferredRegister.create(Hexalia.MOD_ID, Registries.RECIPE_TYPE);

  public static final RegistrySupplier<RecipeSerializer<NaturesRitualRecipe>>
      NATURES_RITUAL_SERIALIZER =
          RECIPE_SERIALIZERS.register("natures_ritual", NaturesRitualRecipe.Serializer::new);

  public static final RegistrySupplier<RecipeSerializer<NaturesRitualRecipe>>
      LEGACY_RITUAL_TABLE_SERIALIZER =
          RECIPE_SERIALIZERS.register("ritual_table", NaturesRitualRecipe.Serializer::new);

  public static final RegistrySupplier<RecipeType<NaturesRitualRecipe>> NATURES_RITUAL =
      RECIPE_TYPES.register(
          "natures_ritual",
          () ->
              new RecipeType<>() {
                @Override
                public String toString() {
                  return Hexalia.MOD_ID + ":natures_ritual";
                }
              });

  public static final RegistrySupplier<RecipeSerializer<SmallCauldronRecipe>>
      SMALL_CAULDRON_SERIALIZER =
          RECIPE_SERIALIZERS.register("small_cauldron", SmallCauldronRecipe.Serializer::new);

  public static final RegistrySupplier<RecipeType<SmallCauldronRecipe>> SMALL_CAULDRON =
      RECIPE_TYPES.register(
          "small_cauldron",
          () ->
              new RecipeType<>() {
                @Override
                public String toString() {
                  return Hexalia.MOD_ID + ":small_cauldron";
                }
              });

  public static final RegistrySupplier<RecipeSerializer<MortarAndPestleRecipe>>
      MORTAR_AND_PESTLE_SERIALIZER =
          RECIPE_SERIALIZERS.register("mortar_and_pestle", MortarAndPestleRecipe.Serializer::new);

  public static final RegistrySupplier<RecipeType<MortarAndPestleRecipe>> MORTAR_AND_PESTLE =
      RECIPE_TYPES.register(
          "mortar_and_pestle",
          () ->
              new RecipeType<>() {
                @Override
                public String toString() {
                  return Hexalia.MOD_ID + ":mortar_and_pestle";
                }
              });

  public static final RegistrySupplier<RecipeSerializer<MutationRecipe>> MUTATION_SERIALIZER =
      RECIPE_SERIALIZERS.register("mutation", MutationRecipe.Serializer::new);

  public static final RegistrySupplier<RecipeType<MutationRecipe>> MUTATION =
      RECIPE_TYPES.register(
          "mutation",
          () ->
              new RecipeType<>() {
                @Override
                public String toString() {
                  return Hexalia.MOD_ID + ":mutation";
                }
              });

  private ModRecipeTypes() {}

  public static void init() {
    RECIPE_SERIALIZERS.register();
    RECIPE_TYPES.register();
  }
}
