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
import net.astralya.hexalia.compat.NaturesRitualViewer;
import net.astralya.hexalia.recipe.RitualTableRecipe;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RitualTableRecipeCategory implements IRecipeCategory<RitualTableRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(HexaliaMod.MODID, "ritual_table");
    public static final ResourceLocation TEXTURE = new ResourceLocation(HexaliaMod.MODID, "textures/gui/ritual_table_gui.png");

    public static final RecipeType<RitualTableRecipe> RITUAL_TABLE_RECIPE_TYPE =
            new RecipeType<>(UID, RitualTableRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable soulIcon;

    private static final int WIDTH = NaturesRitualViewer.WIDTH;
    private static final int HEIGHT = NaturesRitualViewer.HEIGHT;

    public RitualTableRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, WIDTH, HEIGHT);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(ModBlocks.RITUAL_TABLE.get()));
        this.soulIcon = helper.createDrawable(NaturesRitualViewer.SOUL_ICON, 0, 0, NaturesRitualViewer.SOUL_SIZE, NaturesRitualViewer.SOUL_SIZE);
    }

    @Override
    public RecipeType<RitualTableRecipe> getRecipeType() {
        return RITUAL_TABLE_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.hexalia.ritual_table");
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
    public void draw(RitualTableRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics, 0, 0);
        if (recipe.requiresSoul()) soulIcon.draw(guiGraphics, NaturesRitualViewer.SOUL_X, NaturesRitualViewer.SOUL_Y);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RitualTableRecipe recipe, IFocusGroup focuses) {
        List<Ingredient> ingredients = recipe.getIngredients();

        if (!ingredients.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, NaturesRitualViewer.INPUT_X[0], NaturesRitualViewer.INPUT_Y[0])
                    .addIngredients(ingredients.get(0));
        }

        for (int i = 1; i < ingredients.size() && i < NaturesRitualViewer.INPUT_X.length; i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, NaturesRitualViewer.INPUT_X[i],
                            NaturesRitualViewer.INPUT_Y[i])
                    .addIngredients(ingredients.get(i));
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, NaturesRitualViewer.OUTPUT_X, NaturesRitualViewer.OUTPUT_Y)
                .addItemStack(NaturesRitualViewer.result(recipe));
    }


    @Override
    public void getTooltip(ITooltipBuilder tooltip, RitualTableRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (recipe.requiresSoul() && mouseX >= NaturesRitualViewer.SOUL_X && mouseX < NaturesRitualViewer.SOUL_X + NaturesRitualViewer.SOUL_SIZE && mouseY >= NaturesRitualViewer.SOUL_Y && mouseY < NaturesRitualViewer.SOUL_Y + NaturesRitualViewer.SOUL_SIZE) {
            tooltip.add(Component.translatable(NaturesRitualViewer.SOUL_TOOLTIP));
        }
    }
}
