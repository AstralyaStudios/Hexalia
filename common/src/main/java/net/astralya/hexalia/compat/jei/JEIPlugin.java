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
import net.astralya.hexalia.recipe.MortarAndPestleRecipe;
import net.astralya.hexalia.recipe.MutationRecipe;
import net.astralya.hexalia.recipe.RitualBrazierRecipe;
import net.astralya.hexalia.recipe.RitualTableRecipe;
import net.astralya.hexalia.recipe.SmallCauldronRecipe;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@JeiPlugin
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("unused")
public class JEIPlugin implements IModPlugin {

    private static final ResourceLocation ID = new ResourceLocation(HexaliaMod.MODID, "jei_plugin");

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
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SMALL_CAULDRON.get()), SmallCauldronRecipeCategory.SMALL_CAULDRON_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.RITUAL_BRAZIER.get()), RitualBrazierRecipeCategory.RITUAL_BRAZIER_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.RITUAL_TABLE.get()), RitualTableRecipeCategory.RITUAL_TABLE_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModItems.MUTAVIS.get()), MutationRecipeCategory.MUTATION_RECIPE_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModItems.MORTAR_AND_PESTLE.get()), MortarAndPestleRecipeCategory.MORTAR_AND_PESTLE_RECIPE_TYPE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (Minecraft.getInstance().level == null) {
            return;
        }

        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<SmallCauldronRecipe> smallCauldronRecipes = recipeManager.getAllRecipesFor(SmallCauldronRecipe.Type.INSTANCE);
        registration.addRecipes(SmallCauldronRecipeCategory.SMALL_CAULDRON_RECIPE_TYPE, smallCauldronRecipes);

        List<RitualBrazierRecipe> ritualBrazierRecipes = recipeManager.getAllRecipesFor(RitualBrazierRecipe.Type.INSTANCE);
        registration.addRecipes(RitualBrazierRecipeCategory.RITUAL_BRAZIER_RECIPE_TYPE, ritualBrazierRecipes);

        List<RitualTableRecipe> ritualTableRecipes = recipeManager.getAllRecipesFor(RitualTableRecipe.Type.INSTANCE);
        registration.addRecipes(RitualTableRecipeCategory.RITUAL_TABLE_RECIPE_TYPE, ritualTableRecipes);

        List<MutationRecipe> mutationRecipes = recipeManager.getAllRecipesFor(MutationRecipe.Type.INSTANCE);
        registration.addRecipes(MutationRecipeCategory.MUTATION_RECIPE_RECIPE_TYPE, mutationRecipes);

        List<MortarAndPestleRecipe> mortarAndPestleRecipes = recipeManager.getAllRecipesFor(MortarAndPestleRecipe.Type.INSTANCE);
        registration.addRecipes(MortarAndPestleRecipeCategory.MORTAR_AND_PESTLE_RECIPE_TYPE, mortarAndPestleRecipes);

        registration.addIngredientInfo(
                List.of(
                        new ItemStack(ModBlocks.WILD_SUNFIRE_TOMATO.get()),
                        new ItemStack(ModItems.SUNFIRE_TOMATO.get()),
                        new ItemStack(ModItems.SUNFIRE_TOMATO_SEEDS.get())
                ),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.info.wild_sunfire_tomatoes")
        );

        registration.addIngredientInfo(
                List.of(
                        new ItemStack(ModBlocks.WILD_MANDRAKE.get()),
                        new ItemStack(ModItems.MANDRAKE.get()),
                        new ItemStack(ModItems.MANDRAKE_SEEDS.get())
                ),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.info.wild_mandrakes")
        );

        registration.addIngredientInfo(
                List.of(
                        new ItemStack(ModBlocks.CHILLBERRY_BUSH.get()),
                        new ItemStack(ModItems.CHILLBERRIES.get())
                ),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.info.chillberry_bushes")
        );

        registration.addIngredientInfo(
                List.of(new ItemStack(ModBlocks.RITUAL_BRAZIER.get())),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.info.ritual_brazier")
        );

        registration.addIngredientInfo(
                List.of(new ItemStack(ModBlocks.RITUAL_TABLE.get())),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.info.ritual_table")
        );

        registration.addIngredientInfo(
                List.of(new ItemStack(ModItems.LOTUS_BLOSSOM.get())),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.info.lotus_blossom")
        );

        registration.addIngredientInfo(
                List.of(new ItemStack(ModItems.TREE_RESIN.get())),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.info.tree_resin")
        );

        registration.addIngredientInfo(
                List.of(new ItemStack(ModItems.SILKWORM.get())),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.info.silk_fiber")
        );
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        IModPlugin.super.registerGuiHandlers(registration);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }
}
