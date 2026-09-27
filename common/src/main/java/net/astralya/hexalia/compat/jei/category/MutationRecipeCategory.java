package net.astralya.hexalia.compat.jei.category;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.recipe.MutationRecipe;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault

public class MutationRecipeCategory implements IRecipeCategory<MutationRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(HexaliaMod.MODID, "mutation");
    public static final ResourceLocation TEXTURE = new ResourceLocation(HexaliaMod.MODID, "textures/gui/mutation_gui.png");

    public static final RecipeType<MutationRecipe> MUTATION_RECIPE_RECIPE_TYPE =
            new RecipeType<>(UID, MutationRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable hexIcon;

    private static final int WIDTH = 118;
    private static final int HEIGHT = 80;

    public MutationRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, WIDTH, HEIGHT);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(ModItems.MUTAVIS.get()));
        this.hexIcon = helper.createDrawable(TEXTURE, 0, 0, 16, 16);
    }

    @Override
    public RecipeType<MutationRecipe> getRecipeType() {
        return MUTATION_RECIPE_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("item.hexalia.mutavis");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public void draw(MutationRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics, 0, 0);
        hexIcon.draw(guiGraphics, 28, 7);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MutationRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 48, 32)
                .addIngredients(recipe.getIngredients().get(0));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 89, 31)
                .addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, MutationRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (mouseX >= 4 && mouseX < 20 && mouseY >= 55 && mouseY < 71) {
            tooltip.add(Component.translatable("tooltip.hexalia.mutation"));
        }
    }
}

