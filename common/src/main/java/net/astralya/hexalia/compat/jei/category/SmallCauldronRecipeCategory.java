package net.astralya.hexalia.compat.jei.category;

import javax.annotation.ParametersAreNonnullByDefault;
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
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SmallCauldronRecipeCategory implements IRecipeCategory<SmallCauldronRecipe> {

  public static final ResourceLocation UID =
      new ResourceLocation(HexaliaMod.MODID, "small_cauldron");
  public static final ResourceLocation TEXTURE =
      new ResourceLocation(HexaliaMod.MODID, "textures/gui/category/small_cauldron_gui.png");

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
    this.icon =
        helper.createDrawableIngredient(
            VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.SMALL_CAULDRON.get()));
  }

  @Override
  public RecipeType<SmallCauldronRecipe> getRecipeType() {
    return SMALL_CAULDRON_RECIPE_TYPE;
  }

  @Override
  public Component getTitle() {
    return Component.translatable("container.hexalia.small_cauldron");
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
  public void draw(
      SmallCauldronRecipe recipe,
      IRecipeSlotsView recipeSlotsView,
      GuiGraphics guiGraphics,
      double mouseX,
      double mouseY) {
    background.draw(guiGraphics, 0, 0);
  }

  @Override
  public void setRecipe(
      IRecipeLayoutBuilder builder, SmallCauldronRecipe recipe, IFocusGroup focuses) {
    builder
        .addSlot(RecipeIngredientRole.INPUT, 1, 1)
        .addIngredients(recipe.getIngredients().get(0));
    builder
        .addSlot(RecipeIngredientRole.INPUT, 25, 1)
        .addIngredients(recipe.getIngredients().get(1));
    builder
        .addSlot(RecipeIngredientRole.INPUT, 1, 25)
        .addIngredients(recipe.getIngredients().get(2));
    builder
        .addSlot(RecipeIngredientRole.INPUT, 25, 25)
        .addIngredients(recipe.getIngredients().get(3));

    builder.addSlot(RecipeIngredientRole.OUTPUT, 70, 12).addItemStack(recipe.getResultItem(null));
  }

  @Override
  public void getTooltip(
      ITooltipBuilder tooltip,
      SmallCauldronRecipe recipe,
      IRecipeSlotsView recipeSlotsView,
      double mouseX,
      double mouseY) {}
}
