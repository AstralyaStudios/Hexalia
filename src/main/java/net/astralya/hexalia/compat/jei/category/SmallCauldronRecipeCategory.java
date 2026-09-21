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
import net.astralya.hexalia.recipe.SmallCauldronRecipe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SmallCauldronRecipeCategory implements IRecipeCategory<SmallCauldronRecipe> {

    public static final Identifier UID = Identifier.of(HexaliaMod.MODID, "small_cauldron");
    public static final Identifier TEXTURE = Identifier.of(HexaliaMod.MODID, "textures/gui/category/small_cauldron_gui.png");

    public static final RecipeType<SmallCauldronRecipe> SMALL_CAULDRON_RECIPE_TYPE =
            new RecipeType<>(UID, SmallCauldronRecipe.class);

    private static final int TEX_U = 14;
    private static final int TEX_V = 19;
    private static final int WIDTH = 89;
    private static final int HEIGHT = 42;

    private final IDrawable background;
    private final IDrawable icon;

    public SmallCauldronRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, TEX_U, TEX_V, WIDTH, HEIGHT);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.SMALL_CAULDRON));
    }

    @Override
    public @NotNull RecipeType<SmallCauldronRecipe> getRecipeType() {
        return SMALL_CAULDRON_RECIPE_TYPE;
    }

    @Override
    public @NotNull Text getTitle() {
        return Text.translatable("container.hexalia.small_cauldron");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
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
    public void draw(@NotNull SmallCauldronRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull DrawContext drawContext, double mouseX, double mouseY) {
        background.draw(drawContext, 0, 0);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SmallCauldronRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 25, 1).addIngredients(recipe.getIngredients().get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 25).addIngredients(recipe.getIngredients().get(2));
        builder.addSlot(RecipeIngredientRole.INPUT, 25, 25).addIngredients(recipe.getIngredients().get(3));

        if (MinecraftClient.getInstance().world != null) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 70, 12)
                    .addItemStack(recipe.getOutput(MinecraftClient.getInstance().world.getRegistryManager()));
        }
    }

    @Override
    public void getTooltip(@NotNull ITooltipBuilder tooltip, @NotNull SmallCauldronRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
    }
}