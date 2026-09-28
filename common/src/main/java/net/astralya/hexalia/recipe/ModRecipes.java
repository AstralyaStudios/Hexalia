package net.astralya.hexalia.recipe;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.astralya.hexalia.HexaliaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ModRecipes {

  public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
      DeferredRegister.create(HexaliaMod.MODID, Registries.RECIPE_SERIALIZER);

  public static final RegistrySupplier<RecipeSerializer<SmallCauldronRecipe>>
      SMALL_CAULDRON_SERIALIZER =
          SERIALIZERS.register("small_cauldron", () -> SmallCauldronRecipe.Serializer.INSTANCE);

  public static final RegistrySupplier<RecipeSerializer<RitualTableRecipe>>
      RITUAL_TABLE_SERIALIZER =
          SERIALIZERS.register("ritual_table", () -> RitualTableRecipe.Serializer.INSTANCE);
  public static final RegistrySupplier<RecipeSerializer<RitualTableRecipe>>
      NATURES_RITUAL_SERIALIZER =
          SERIALIZERS.register(
              "natures_ritual", () -> RitualTableRecipe.Serializer.NATURES_RITUAL_INSTANCE);

  public static final RegistrySupplier<RecipeSerializer<RitualBrazierRecipe>>
      RITUAL_BRAZIER_SERIALIZER =
          SERIALIZERS.register("ritual_brazier", () -> RitualBrazierRecipe.Serializer.INSTANCE);

  public static final RegistrySupplier<RecipeSerializer<MutationRecipe>> MUTATION_SERIALIZER =
      SERIALIZERS.register("mutation", () -> MutationRecipe.Serializer.INSTANCE);

  public static final RegistrySupplier<RecipeSerializer<MortarAndPestleRecipe>>
      MORTAR_AND_PESTLE_SERIALIZER =
          SERIALIZERS.register(
              "mortar_and_pestle", () -> MortarAndPestleRecipe.Serializer.INSTANCE);

  public static void register() {
    SERIALIZERS.register();
  }
}
