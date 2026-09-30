package net.astralya.hexalia.compat.jei.category;

import java.util.List;
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
import net.astralya.hexalia.compat.NaturesRitualViewer;
import net.astralya.hexalia.recipe.RitualTableRecipe;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RitualTableRecipeCategory implements IRecipeCategory<RitualTableRecipe> {

  private final RitualTableRecipe.RitualKind kind;
  private final RecipeType<RitualTableRecipe> recipeType;
  public static final ResourceLocation TEXTURE =
      new ResourceLocation(HexaliaMod.MODID, "textures/gui/ritual_table_gui.png");


  private final IDrawable background;
  private final IDrawable icon;
  private final IDrawable soulIcon;

  private static final int WIDTH = NaturesRitualViewer.WIDTH;
  private static final int HEIGHT = NaturesRitualViewer.HEIGHT;

  public RitualTableRecipeCategory(IGuiHelper helper, RitualTableRecipe.RitualKind kind) {
    this.kind = kind;
    this.recipeType = new RecipeType<>(NaturesRitualViewer.categoryId(kind), RitualTableRecipe.class);
    this.background = helper.createDrawable(TEXTURE, 0, 0, WIDTH, HEIGHT);
    this.icon =
        helper.createDrawableIngredient(
            VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.RITUAL_TABLE.get()));
    this.soulIcon =
        helper.createDrawable(
            NaturesRitualViewer.SOUL_ICON,
            0,
            0,
            NaturesRitualViewer.SOUL_SIZE,
            NaturesRitualViewer.SOUL_SIZE);
  }

  @Override
  public RecipeType<RitualTableRecipe> getRecipeType() {
    return recipeType;
  }

  @Override
  public Component getTitle() {
    return NaturesRitualViewer.categoryTitle(kind);
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
    return NaturesRitualViewer.ENERGY_Y + NaturesRitualViewer.ENERGY_HEIGHT;
  }

  @Override
  public void draw(
      RitualTableRecipe recipe,
      IRecipeSlotsView recipeSlotsView,
      GuiGraphics guiGraphics,
      double mouseX,
      double mouseY) {
    background.draw(guiGraphics, 0, 0);
    guiGraphics.fill(4, 81, 114, 82, 0xFF706C77);
    guiGraphics.drawString(net.minecraft.client.Minecraft.getInstance().font,
        NaturesRitualViewer.energyFirstLine(recipe), 24, 86, 0xA9A29C, false);
    guiGraphics.drawString(net.minecraft.client.Minecraft.getInstance().font,
        NaturesRitualViewer.energySecondLine(recipe), 24, 97, 0xA9A29C, false);
    if (recipe.requiresSoul())
      soulIcon.draw(guiGraphics, NaturesRitualViewer.SOUL_X, NaturesRitualViewer.SOUL_Y);
  }

  @Override
  public void setRecipe(
      IRecipeLayoutBuilder builder, RitualTableRecipe recipe, IFocusGroup focuses) {
    List<Ingredient> ingredients = recipe.getIngredients();

    if (!ingredients.isEmpty()) {
      builder
          .addSlot(
              RecipeIngredientRole.INPUT,
              NaturesRitualViewer.INPUT_X[0],
              NaturesRitualViewer.INPUT_Y[0])
          .addIngredients(ingredients.get(0));
    }

    for (int i = 1; i < ingredients.size() && i < NaturesRitualViewer.INPUT_X.length; i++) {
      builder
          .addSlot(
              RecipeIngredientRole.INPUT,
              NaturesRitualViewer.INPUT_X[i],
              NaturesRitualViewer.INPUT_Y[i])
          .addIngredients(ingredients.get(i));
    }

    builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 4, NaturesRitualViewer.ENERGY_Y + 2)
        .addItemStacks(NaturesRitualViewer.energyIcons(recipe));

    builder
        .addSlot(
            RecipeIngredientRole.OUTPUT, NaturesRitualViewer.OUTPUT_X, NaturesRitualViewer.OUTPUT_Y)
        .addItemStack(NaturesRitualViewer.result(recipe));
  }

  @Override
  public void getTooltip(
      ITooltipBuilder tooltip,
      RitualTableRecipe recipe,
      IRecipeSlotsView recipeSlotsView,
      double mouseX,
      double mouseY) {
    if (mouseY >= NaturesRitualViewer.ENERGY_Y)
      NaturesRitualViewer.energyTooltip(recipe).forEach(tooltip::add);
    if (recipe.requiresSoul()
        && mouseX >= NaturesRitualViewer.SOUL_X
        && mouseX < NaturesRitualViewer.SOUL_X + NaturesRitualViewer.SOUL_SIZE
        && mouseY >= NaturesRitualViewer.SOUL_Y
        && mouseY < NaturesRitualViewer.SOUL_Y + NaturesRitualViewer.SOUL_SIZE) {
      tooltip.add(Component.translatable(NaturesRitualViewer.SOUL_TOOLTIP));
    }
  }
}
