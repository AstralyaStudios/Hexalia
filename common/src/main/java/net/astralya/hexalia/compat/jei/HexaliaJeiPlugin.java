package net.astralya.hexalia.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.astralya.hexalia.Hexalia;
import net.astralya.hexalia.compat.jei.category.CelestialInfusionJeiCategory;
import net.astralya.hexalia.compat.jei.category.MortarAndPestleJeiCategory;
import net.astralya.hexalia.compat.jei.category.MutationJeiCategory;
import net.astralya.hexalia.compat.jei.category.NaturesRitualJeiCategory;
import net.astralya.hexalia.compat.jei.category.SmallCauldronJeiCategory;
import net.astralya.hexalia.compat.jei.util.JeiRecipeLookup;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.recipe.ModRecipeTypes;
import net.astralya.hexalia.recipe.NaturesRitualRecipe;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public final class HexaliaJeiPlugin implements IModPlugin {
  public static final ResourceLocation UID =
      ResourceLocation.fromNamespaceAndPath(Hexalia.MOD_ID, "jei_plugin");

  @Override
  public ResourceLocation getPluginUid() {
    return UID;
  }

  @Override
  public void registerCategories(IRecipeCategoryRegistration registration) {
    IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
    registration.addRecipeCategories(
        new MortarAndPestleJeiCategory(guiHelper),
        new SmallCauldronJeiCategory(guiHelper),
        new NaturesRitualJeiCategory(guiHelper),
        new CelestialInfusionJeiCategory(guiHelper),
        new MutationJeiCategory(guiHelper));
  }

  @Override
  public void registerRecipes(IRecipeRegistration registration) {
    registration.addRecipes(
        HexaliaJeiRecipeTypes.MORTAR_AND_PESTLE,
        JeiRecipeLookup.getRecipes(ModRecipeTypes.MORTAR_AND_PESTLE.get()));
    registration.addRecipes(
        HexaliaJeiRecipeTypes.SMALL_CAULDRON,
        JeiRecipeLookup.getRecipes(ModRecipeTypes.SMALL_CAULDRON.get()));
    registration.addRecipes(
        HexaliaJeiRecipeTypes.NATURES_RITUAL,
        JeiRecipeLookup.getRecipes(ModRecipeTypes.NATURES_RITUAL.get()).stream()
            .filter(recipe -> recipe.ritualKind() != NaturesRitualRecipe.RitualKind.CELESTIAL)
            .toList());
    registration.addRecipes(
        HexaliaJeiRecipeTypes.CELESTIAL_INFUSION,
        JeiRecipeLookup.getRecipes(ModRecipeTypes.NATURES_RITUAL.get()).stream()
            .filter(recipe -> recipe.ritualKind() == NaturesRitualRecipe.RitualKind.CELESTIAL)
            .toList());
    registration.addRecipes(
        HexaliaJeiRecipeTypes.MUTATION, JeiRecipeLookup.getRecipes(ModRecipeTypes.MUTATION.get()));
  }

  @Override
  public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
    registration.addRecipeCatalyst(
        ModItems.MORTAR_AND_PESTLE.get(), HexaliaJeiRecipeTypes.MORTAR_AND_PESTLE);
    registration.addRecipeCatalyst(
        ModItems.SMALL_CAULDRON.get(), HexaliaJeiRecipeTypes.SMALL_CAULDRON);
    registration.addRecipeCatalyst(
        ModItems.RITUAL_TABLE.get(), HexaliaJeiRecipeTypes.NATURES_RITUAL);
    registration.addRecipeCatalyst(
        ModItems.RITUAL_BRAZIER.get(), HexaliaJeiRecipeTypes.NATURES_RITUAL);
    registration.addRecipeCatalyst(ModItems.HEX_FOCUS.get(), HexaliaJeiRecipeTypes.NATURES_RITUAL);
    registration.addRecipeCatalyst(
        ModItems.RITUAL_TABLE.get(), HexaliaJeiRecipeTypes.CELESTIAL_INFUSION);
    registration.addRecipeCatalyst(
        ModItems.CELESTIAL_CRYSTAL.get(), HexaliaJeiRecipeTypes.CELESTIAL_INFUSION);
    registration.addRecipeCatalyst(
        ModItems.HEX_FOCUS.get(), HexaliaJeiRecipeTypes.CELESTIAL_INFUSION);
    registration.addRecipeCatalyst(ModItems.MUTAVIS.get(), HexaliaJeiRecipeTypes.MUTATION);
  }
}
