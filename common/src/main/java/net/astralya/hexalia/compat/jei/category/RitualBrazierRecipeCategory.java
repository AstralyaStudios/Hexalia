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
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.recipe.RitualBrazierRecipe;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RitualBrazierRecipeCategory implements IRecipeCategory<RitualBrazierRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(HexaliaMod.MODID, "ritual_brazier");
    public static final ResourceLocation TEXTURE = new ResourceLocation(HexaliaMod.MODID, "textures/gui/ritual_brazier_gui.png");
    public static final RecipeType<RitualBrazierRecipe> RITUAL_BRAZIER_RECIPE_TYPE =
            new RecipeType<>(UID, RitualBrazierRecipe.class);

    private static final int WIDTH = 118;
    private static final int HEIGHT = 80;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable hexIcon;

    public RitualBrazierRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, WIDTH, HEIGHT);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.RITUAL_BRAZIER.get()));
        this.hexIcon = helper.createDrawable(TEXTURE, 0, 0, 16, 16);
    }

    @Override
    public RecipeType<RitualBrazierRecipe> getRecipeType() {
        return RITUAL_BRAZIER_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.hexalia.ritual_brazier");
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
    public void draw(RitualBrazierRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics, 0, 0);
        hexIcon.draw(guiGraphics, 28, 7);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RitualBrazierRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 28, 31)
                .addIngredients(recipe.getInput());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 89, 31)
                .addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, RitualBrazierRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (mouseX >= 4 && mouseX < 20 && mouseY >= 55 && mouseY < 71) {
            tooltip.add(Component.translatable("tooltip.hexalia.hex_focus_gui"));
        }
    }
}

