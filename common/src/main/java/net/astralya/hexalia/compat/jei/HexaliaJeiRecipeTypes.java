package net.astralya.hexalia.compat.jei;

import mezz.jei.api.recipe.RecipeType;
import net.astralya.hexalia.Hexalia;
import net.astralya.hexalia.recipe.MortarAndPestleRecipe;
import net.astralya.hexalia.recipe.MutationRecipe;
import net.astralya.hexalia.recipe.NaturesRitualRecipe;
import net.astralya.hexalia.recipe.SmallCauldronRecipe;

public final class HexaliaJeiRecipeTypes {
  public static final RecipeType<MortarAndPestleRecipe> MORTAR_AND_PESTLE =
      RecipeType.create(Hexalia.MOD_ID, "mortar_and_pestle", MortarAndPestleRecipe.class);
  public static final RecipeType<SmallCauldronRecipe> SMALL_CAULDRON =
      RecipeType.create(Hexalia.MOD_ID, "small_cauldron", SmallCauldronRecipe.class);
  public static final RecipeType<NaturesRitualRecipe> NATURES_RITUAL =
      RecipeType.create(Hexalia.MOD_ID, "natures_ritual", NaturesRitualRecipe.class);
  public static final RecipeType<NaturesRitualRecipe> CELESTIAL_INFUSION =
      RecipeType.create(Hexalia.MOD_ID, "celestial_infusion", NaturesRitualRecipe.class);
  public static final RecipeType<MutationRecipe> MUTATION =
      RecipeType.create(Hexalia.MOD_ID, "mutation", MutationRecipe.class);

  private HexaliaJeiRecipeTypes() {}
}
