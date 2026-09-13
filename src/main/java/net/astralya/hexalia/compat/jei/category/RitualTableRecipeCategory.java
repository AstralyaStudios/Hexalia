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
import net.astralya.hexalia.compat.RitualTableViewerLayout;
import net.astralya.hexalia.recipe.RitualTableRecipe;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RitualTableRecipeCategory implements IRecipeCategory<RitualTableRecipe> {
    public static final Identifier UID = Identifier.of(HexaliaMod.MODID, "ritual_table");
    public static final Identifier TEXTURE = Identifier.of(HexaliaMod.MODID, "textures/gui/ritual_table_gui.png");
    public static final RecipeType<RitualTableRecipe> RITUAL_TABLE_RECIPE_TYPE =
            new RecipeType<>(UID, RitualTableRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable soulIndicator;

    public RitualTableRecipeCategory(IGuiHelper helper) {
        background = helper.createDrawable(TEXTURE, 0, 0, RitualTableViewerLayout.WIDTH, RitualTableViewerLayout.HEIGHT);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.RITUAL_TABLE));
        soulIndicator = helper.createDrawable(RitualTableViewerLayout.SOUL_TEXTURE, 0, 0,
                RitualTableViewerLayout.SOUL_SIZE, RitualTableViewerLayout.SOUL_SIZE);
    }

    @Override public @NotNull RecipeType<RitualTableRecipe> getRecipeType() { return RITUAL_TABLE_RECIPE_TYPE; }
    @Override public @NotNull Text getTitle() { return Text.translatable("block.hexalia.ritual_table"); }
    @Override public IDrawable getBackground() { return background; }
    @Override public @Nullable IDrawable getIcon() { return icon; }
    @Override public int getWidth() { return RitualTableViewerLayout.WIDTH; }
    @Override public int getHeight() { return RitualTableViewerLayout.HEIGHT; }

    @Override public void draw(@NotNull RitualTableRecipe recipe, @NotNull IRecipeSlotsView slots,
                               @NotNull DrawContext context, double mouseX, double mouseY) {
        background.draw(context, 0, 0);
        if (recipe.requiresSoul()) soulIndicator.draw(context, RitualTableViewerLayout.SOUL_X, RitualTableViewerLayout.SOUL_Y);
    }

    @Override public void setRecipe(@NotNull IRecipeLayoutBuilder builder, RitualTableRecipe recipe,
                                    @NotNull IFocusGroup focuses) {
        int[] center = RitualTableViewerLayout.INPUTS[0];
        builder.addSlot(RecipeIngredientRole.INPUT, center[0] + 1, center[1] + 1)
                .addIngredients(recipe.centerIngredient());
        for (int index = 0; index < Math.min(recipe.offerings().size(), 8); index++) {
            int[] position = RitualTableViewerLayout.INPUTS[index + 1];
            builder.addSlot(RecipeIngredientRole.INPUT, position[0] + 1, position[1] + 1)
                    .addIngredients(recipe.offerings().get(index));
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT,
                RitualTableViewerLayout.OUTPUT_X + 1, RitualTableViewerLayout.OUTPUT_Y + 1)
                .addItemStack(RitualTableViewerLayout.displayResult(recipe));
    }

    @Override public void getTooltip(@NotNull ITooltipBuilder tooltip, @NotNull RitualTableRecipe recipe,
                                     @NotNull IRecipeSlotsView slots, double mouseX, double mouseY) {
        if (recipe.requiresSoul() && mouseX >= RitualTableViewerLayout.SOUL_X
                && mouseX < RitualTableViewerLayout.SOUL_X + RitualTableViewerLayout.SOUL_SIZE
                && mouseY >= RitualTableViewerLayout.SOUL_Y
                && mouseY < RitualTableViewerLayout.SOUL_Y + RitualTableViewerLayout.SOUL_SIZE) {
            tooltip.add(Text.translatable(RitualTableViewerLayout.SOUL_TOOLTIP));
        }
    }
}
