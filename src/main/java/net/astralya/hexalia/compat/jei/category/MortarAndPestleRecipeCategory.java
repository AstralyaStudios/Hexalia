package net.astralya.hexalia.compat.jei.category;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.recipe.MortarAndPestleRecipe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@MethodsReturnNonnullByDefault
public class MortarAndPestleRecipeCategory implements IRecipeCategory<MortarAndPestleRecipe> {

    public static final Identifier UID = Identifier.of(HexaliaMod.MODID, "mortar_and_pestle");
    public static final Identifier TEXTURE = Identifier.of(HexaliaMod.MODID, "textures/gui/mortar_gui.png");

    public static final RecipeType<MortarAndPestleRecipe> MORTAR_AND_PESTLE_RECIPE_TYPE =
            new RecipeType<>(UID, MortarAndPestleRecipe.class);

    private static final int WIDTH = 118;
    private static final int HEIGHT = 80;

    private final IDrawable background;
    private final IDrawable icon;

    public MortarAndPestleRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, WIDTH, HEIGHT);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.MORTAR_AND_PESTLE));
    }

    @Override
    public RecipeType<MortarAndPestleRecipe> getRecipeType() {
        return MORTAR_AND_PESTLE_RECIPE_TYPE;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("block.hexalia.mortar_and_pestle");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
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
    public void draw(@NotNull MortarAndPestleRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull DrawContext drawContext, double mouseX, double mouseY) {
        background.draw(drawContext, 0, 0);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, MortarAndPestleRecipe recipe, @NotNull IFocusGroup focuses) {
        if (!recipe.getIngredients().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 28, 31).addIngredients(recipe.getIngredients().get(0));
        }
        if (recipe.getIngredients().size() > 1) {
            builder.addSlot(RecipeIngredientRole.INPUT, 4, 31).addIngredients(recipe.getIngredients().get(1));
        }
        if (recipe.getIngredients().size() > 2) {
            builder.addSlot(RecipeIngredientRole.INPUT, 52, 31).addIngredients(recipe.getIngredients().get(2));
        }

        if (MinecraftClient.getInstance().world != null) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 89, 31)
                    .addItemStack(recipe.getOutput(MinecraftClient.getInstance().world.getRegistryManager()));
        }
    }
}