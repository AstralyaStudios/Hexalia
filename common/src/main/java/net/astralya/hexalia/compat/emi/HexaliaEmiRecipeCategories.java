package net.astralya.hexalia.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.astralya.hexalia.Hexalia;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.resources.ResourceLocation;

public final class HexaliaEmiRecipeCategories {
  public static final EmiRecipeCategory MORTAR_AND_PESTLE =
      category("mortar_and_pestle", EmiStack.of(ModItems.MORTAR_AND_PESTLE.get()));
  public static final EmiRecipeCategory SMALL_CAULDRON =
      category("small_cauldron", EmiStack.of(ModItems.SMALL_CAULDRON.get()));
  public static final EmiRecipeCategory NATURES_RITUAL =
      category("ritual_table", EmiStack.of(ModItems.RITUAL_TABLE.get()));
  public static final EmiRecipeCategory CELESTIAL_INFUSION =
      category("celestial_infusion", EmiStack.of(ModItems.RITUAL_TABLE.get()));
  public static final EmiRecipeCategory MUTATION =
      category("mutation", EmiStack.of(ModItems.MUTAVIS.get()));

  private HexaliaEmiRecipeCategories() {}

  private static EmiRecipeCategory category(String path, EmiStack icon) {
    return new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath(Hexalia.MOD_ID, path), icon);
  }
}
