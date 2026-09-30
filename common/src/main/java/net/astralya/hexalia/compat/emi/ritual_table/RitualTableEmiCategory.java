package net.astralya.hexalia.compat.emi.ritual_table;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.ModBlocks;
import net.minecraft.resources.ResourceLocation;

public final class RitualTableEmiCategory {
  public static final EmiRecipeCategory NATURE = category(net.astralya.hexalia.recipe.RitualTableRecipe.RitualKind.NATURE);
  public static final EmiRecipeCategory CELESTIAL = category(net.astralya.hexalia.recipe.RitualTableRecipe.RitualKind.CELESTIAL);
  public static final EmiRecipeCategory SUMMONING = category(net.astralya.hexalia.recipe.RitualTableRecipe.RitualKind.SUMMONING);

  public static EmiRecipeCategory category(net.astralya.hexalia.recipe.RitualTableRecipe.RitualKind kind) {
    return new EmiRecipeCategory(net.astralya.hexalia.compat.NaturesRitualViewer.categoryId(kind),
        EmiStack.of(ModBlocks.RITUAL_TABLE.get()));
  }

  private RitualTableEmiCategory() {}
}
