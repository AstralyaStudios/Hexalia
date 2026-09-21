package net.astralya.hexalia.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.compat.jei.category.MortarAndPestleRecipeCategory;
import net.astralya.hexalia.compat.jei.category.MutationRecipeCategory;
import net.astralya.hexalia.compat.jei.category.RitualBrazierRecipeCategory;
import net.astralya.hexalia.compat.jei.category.RitualTableRecipeCategory;
import net.astralya.hexalia.compat.jei.category.SmallCauldronRecipeCategory;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.recipe.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

@JeiPlugin
@SuppressWarnings("unused")
public class JEIPlugin implements IModPlugin {

    private static final Identifier ID = Identifier.of(HexaliaMod.MODID, "jei_plugin");

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new SmallCauldronRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new RitualBrazierRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new RitualTableRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new MutationRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new MortarAndPestleRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }


    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SMALL_CAULDRON), SmallCauldronRecipeCategory.SMALL_CAULDRON_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.RITUAL_BRAZIER), RitualBrazierRecipeCategory.RITUAL_BRAZIER_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.RITUAL_TABLE), RitualTableRecipeCategory.RITUAL_TABLE_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModItems.MUTAVIS), MutationRecipeCategory.MUTATION_RECIPE_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModItems.MORTAR_AND_PESTLE), MortarAndPestleRecipeCategory.MORTAR_AND_PESTLE_RECIPE_TYPE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (MinecraftClient.getInstance().world == null) {
            return;
        }

        RecipeManager recipeManager = MinecraftClient.getInstance().world.getRecipeManager();

        List<SmallCauldronRecipe> smallCauldronRecipes = recipeManager.listAllOfType(ModRecipes.SMALL_CAULDRON_TYPE);
        registration.addRecipes(SmallCauldronRecipeCategory.SMALL_CAULDRON_RECIPE_TYPE, smallCauldronRecipes);

        List<RitualBrazierRecipe> ritualBrazierRecipes = recipeManager.listAllOfType(ModRecipes.RITUAL_BRAZIER_TYPE);
        registration.addRecipes(RitualBrazierRecipeCategory.RITUAL_BRAZIER_RECIPE_TYPE, ritualBrazierRecipes);

        List<RitualTableRecipe> ritualTableRecipes = recipeManager.listAllOfType(ModRecipes.RITUAL_TABLE_TYPE);
        registration.addRecipes(RitualTableRecipeCategory.RITUAL_TABLE_RECIPE_TYPE, ritualTableRecipes);

        List<MutationRecipe> mutationRecipes = recipeManager.listAllOfType(ModRecipes.MUTATION_TYPE);
        registration.addRecipes(MutationRecipeCategory.MUTATION_RECIPE_RECIPE_TYPE, mutationRecipes);

        List<MortarAndPestleRecipe> mortarAndPestleRecipes = recipeManager.listAllOfType(ModRecipes.MORTAR_AND_PESTLE_TYPE);
        registration.addRecipes(MortarAndPestleRecipeCategory.MORTAR_AND_PESTLE_RECIPE_TYPE, mortarAndPestleRecipes);

        registration.addIngredientInfo(
                List.of(
                        new ItemStack(ModBlocks.WILD_SUNFIRE_TOMATO),
                        new ItemStack(ModItems.SUNFIRE_TOMATO),
                        new ItemStack(ModItems.SUNFIRE_TOMATO_SEEDS)
                ),
                VanillaTypes.ITEM_STACK,
                Text.translatable("jei.info.wild_sunfire_tomatoes")
        );

        registration.addIngredientInfo(
                List.of(
                        new ItemStack(ModBlocks.WILD_MANDRAKE),
                        new ItemStack(ModItems.MANDRAKE),
                        new ItemStack(ModItems.MANDRAKE_SEEDS)
                ),
                VanillaTypes.ITEM_STACK,
                Text.translatable("jei.info.wild_mandrakes")
        );

        registration.addIngredientInfo(
                List.of(
                        new ItemStack(ModBlocks.CHILLBERRY_BUSH),
                        new ItemStack(ModItems.CHILLBERRIES)
                ),
                VanillaTypes.ITEM_STACK,
                Text.translatable("jei.info.chillberry_bushes")
        );

        registration.addIngredientInfo(
                List.of(new ItemStack(ModBlocks.RITUAL_BRAZIER)),
                VanillaTypes.ITEM_STACK,
                Text.translatable("jei.info.ritual_brazier")
        );

        registration.addIngredientInfo(
                List.of(new ItemStack(ModBlocks.RITUAL_TABLE)),
                VanillaTypes.ITEM_STACK,
                Text.translatable("jei.info.ritual_table")
        );

        registration.addIngredientInfo(
                List.of(new ItemStack(ModItems.LOTUS_BLOSSOM)),
                VanillaTypes.ITEM_STACK,
                Text.translatable("jei.info.lotus_blossom")
        );

        registration.addIngredientInfo(
                List.of(new ItemStack(ModItems.TREE_RESIN)),
                VanillaTypes.ITEM_STACK,
                Text.translatable("jei.info.tree_resin")
        );

        registration.addIngredientInfo(
                List.of(new ItemStack(ModItems.SILKWORM)),
                VanillaTypes.ITEM_STACK,
                Text.translatable("jei.info.silk_fiber")
        );
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        IModPlugin.super.registerGuiHandlers(registration);
    }

    @Override
    public Identifier getPluginUid() {
        return ID;
    }
}