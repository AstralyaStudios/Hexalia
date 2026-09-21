package net.astralya.hexalia.compat.jei.category;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@MethodsReturnNonnullByDefault
public class MutationRecipeCategory implements IRecipeCategory<MutationRecipe> {

    public static final Identifier UID = Identifier.of(HexaliaMod.MODID, "mutation");
    public static final Identifier TEXTURE = Identifier.of(HexaliaMod.MODID, "textures/gui/mutation_gui.png");

    public static final RecipeType<MutationRecipe> MUTATION_RECIPE_RECIPE_TYPE =
            new RecipeType<>(UID, MutationRecipe.class);

    private static final int WIDTH = 118;
    private static final int HEIGHT = 80;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable hexIcon;

    public MutationRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, WIDTH, HEIGHT);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.MUTAVIS));
        this.hexIcon = helper.createDrawable(TEXTURE, 0, 0, 16, 16);
    }

    @Override
    public RecipeType<MutationRecipe> getRecipeType() {
        return MUTATION_RECIPE_RECIPE_TYPE;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("item.hexalia.mutavis");
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
    public void draw(@NotNull MutationRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull DrawContext drawContext, double mouseX, double mouseY) {
        background.draw(drawContext, 0, 0);
        hexIcon.draw(drawContext, 28, 7);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MutationRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 48, 32)
                .addIngredients(recipe.getIngredients().get(0));

        if (MinecraftClient.getInstance().world != null) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 89, 31)
                    .addItemStack(recipe.getOutput(MinecraftClient.getInstance().world.getRegistryManager()));
        }
    }

    @Override
    public void getTooltip(@NotNull ITooltipBuilder tooltip, @NotNull MutationRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (mouseX >= 4 && mouseX < 20 && mouseY >= 55 && mouseY < 71) {
            tooltip.add(Text.translatable("tooltip.hexalia.mutation"));
        }
    }
}