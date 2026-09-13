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
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RitualTableRecipeCategory implements IRecipeCategory<RitualTableRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(HexaliaMod.MODID, "ritual_table");
    public static final ResourceLocation TEXTURE = new ResourceLocation(HexaliaMod.MODID, "textures/gui/ritual_table_gui.png");
    public static final RecipeType<RitualTableRecipe> RITUAL_TABLE_RECIPE_TYPE =
            new RecipeType<>(UID, RitualTableRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable soulIndicator;

    public RitualTableRecipeCategory(IGuiHelper helper) {
        background = helper.createDrawable(TEXTURE, 0, 0, RitualTableViewerLayout.WIDTH, RitualTableViewerLayout.HEIGHT);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.RITUAL_TABLE.get()));
        soulIndicator = helper.createDrawable(RitualTableViewerLayout.SOUL_TEXTURE, 0, 0,
                RitualTableViewerLayout.SOUL_SIZE, RitualTableViewerLayout.SOUL_SIZE);
    }

    @Override public RecipeType<RitualTableRecipe> getRecipeType() { return RITUAL_TABLE_RECIPE_TYPE; }
    @Override public Component getTitle() { return Component.translatable("block.hexalia.ritual_table"); }
    @Override public @Nullable IDrawable getIcon() { return icon; }
    @Override public int getWidth() { return RitualTableViewerLayout.WIDTH; }
    @Override public int getHeight() { return RitualTableViewerLayout.HEIGHT; }

    @Override public void draw(RitualTableRecipe recipe, IRecipeSlotsView slots, GuiGraphics graphics,
                               double mouseX, double mouseY) {
        background.draw(graphics, 0, 0);
        if (recipe.requiresSoul()) soulIndicator.draw(graphics, RitualTableViewerLayout.SOUL_X, RitualTableViewerLayout.SOUL_Y);
    }

    @Override public void setRecipe(IRecipeLayoutBuilder builder, RitualTableRecipe recipe, IFocusGroup focuses) {
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

    @Override public void getTooltip(ITooltipBuilder tooltip, RitualTableRecipe recipe,
                                     IRecipeSlotsView slots, double mouseX, double mouseY) {
        if (recipe.requiresSoul() && mouseX >= RitualTableViewerLayout.SOUL_X
                && mouseX < RitualTableViewerLayout.SOUL_X + RitualTableViewerLayout.SOUL_SIZE
                && mouseY >= RitualTableViewerLayout.SOUL_Y
                && mouseY < RitualTableViewerLayout.SOUL_Y + RitualTableViewerLayout.SOUL_SIZE) {
            tooltip.add(Component.translatable(RitualTableViewerLayout.SOUL_TOOLTIP));
        }
    }
}
