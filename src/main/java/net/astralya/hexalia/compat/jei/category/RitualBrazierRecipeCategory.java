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
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.recipe.RitualBrazierRecipe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@MethodsReturnNonnullByDefault
public class RitualBrazierRecipeCategory implements IRecipeCategory<RitualBrazierRecipe> {

    public static final Identifier UID = Identifier.of(HexaliaMod.MODID, "ritual_brazier");
    public static final Identifier TEXTURE = Identifier.of(HexaliaMod.MODID, "textures/gui/ritual_brazier_gui.png");

    public static final RecipeType<RitualBrazierRecipe> RITUAL_BRAZIER_RECIPE_TYPE =
            new RecipeType<>(UID, RitualBrazierRecipe.class);

    private static final int WIDTH = 118;
    private static final int HEIGHT = 80;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable hexIcon;

    public RitualBrazierRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, WIDTH, HEIGHT);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.RITUAL_BRAZIER));
        this.hexIcon = helper.createDrawable(TEXTURE, 0, 0, 16, 16);
    }

    @Override
    public RecipeType<RitualBrazierRecipe> getRecipeType() {
        return RITUAL_BRAZIER_RECIPE_TYPE;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("block.hexalia.ritual_brazier");
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
    public void draw(@NotNull RitualBrazierRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull DrawContext drawContext, double mouseX, double mouseY) {
        background.draw(drawContext, 0, 0);
        hexIcon.draw(drawContext, 28, 7);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RitualBrazierRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 28, 31)
                .addIngredients(recipe.getIngredients().get(0));

        if (MinecraftClient.getInstance().world != null) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 89, 31)
                    .addItemStack(recipe.getOutput(MinecraftClient.getInstance().world.getRegistryManager()));
        }
    }

    @Override
    public void getTooltip(@NotNull ITooltipBuilder tooltip, @NotNull RitualBrazierRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (mouseX >= 4 && mouseX < 20 && mouseY >= 55 && mouseY < 71) {
            tooltip.add(Text.translatable("tooltip.hexalia.hex_focus_gui"));
        }
    }
}