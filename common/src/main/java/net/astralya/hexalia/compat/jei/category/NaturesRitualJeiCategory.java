package net.astralya.hexalia.compat.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.gui.widgets.IRecipeWidget;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import net.astralya.hexalia.compat.HexaliaRecipeGuiLayout;
import net.astralya.hexalia.compat.NaturesRitualViewerIndicator;
import net.astralya.hexalia.compat.jei.HexaliaJeiRecipeTypes;
import net.astralya.hexalia.compat.jei.util.JeiLayoutHelper;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.recipe.NaturesRitualRecipe;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;

public final class NaturesRitualJeiCategory
    extends AbstractHexaliaJeiCategory<NaturesRitualRecipe> {
  private final IDrawableStatic background;
  private final IDrawableStatic soulIndicator;

  public NaturesRitualJeiCategory(IGuiHelper guiHelper) {
    super(
        guiHelper,
        HexaliaJeiRecipeTypes.NATURES_RITUAL,
        "jei.hexalia.category.natures_ritual",
        ModItems.RITUAL_TABLE.get(),
        HexaliaRecipeGuiLayout.NATURES_RITUAL);
    HexaliaRecipeGuiLayout layout = HexaliaRecipeGuiLayout.NATURES_RITUAL;
    background =
        guiHelper.createDrawable(
            layout.texture(),
            layout.textureU(),
            layout.textureV(),
            layout.textureWidth(),
            layout.textureHeight());
    soulIndicator =
        guiHelper.createDrawable(
            NaturesRitualViewerIndicator.TEXTURE,
            0,
            0,
            NaturesRitualViewerIndicator.WIDTH,
            NaturesRitualViewerIndicator.HEIGHT);
  }

  @Override
  public void setRecipe(
      IRecipeLayoutBuilder builder, NaturesRitualRecipe recipe, IFocusGroup focuses) {
    HexaliaRecipeGuiLayout layout = HexaliaRecipeGuiLayout.NATURES_RITUAL;
    JeiLayoutHelper.addNamedInput(
        builder, recipe.centerIngredient(), layout.inputX(0), layout.inputY(0), "ritual_table");

    for (int index = 0; index < Math.min(recipe.offerings().size(), 8); index++) {
      JeiLayoutHelper.addNamedInput(
          builder,
          recipe.offerings().get(index),
          layout.inputX(index + 1),
          layout.inputY(index + 1),
          "ritual_brazier_" + (index + 1));
    }
    JeiLayoutHelper.addOutput(builder, displayResult(recipe), layout);
  }

  private static ItemStack displayResult(NaturesRitualRecipe recipe) {
    if (recipe.isItemResult()) return recipe.itemResult();
    EntityType<?> type =
        recipe.entityResult()
            .flatMap(result -> BuiltInRegistries.ENTITY_TYPE.getOptional(result.entity()))
            .orElse(null);
    SpawnEggItem spawnEgg = type == null ? null : SpawnEggItem.byId(type);
    return spawnEgg == null ? ItemStack.EMPTY : spawnEgg.getDefaultInstance();
  }

  @Override
  public void draw(
      NaturesRitualRecipe recipe,
      IRecipeSlotsView recipeSlotsView,
      GuiGraphics guiGraphics,
      double mouseX,
      double mouseY) {
    background.draw(guiGraphics, 0, 0);
  }

  @Override
  public void createRecipeExtras(
      IRecipeExtrasBuilder builder, NaturesRitualRecipe recipe, IFocusGroup focuses) {
    if (recipe.requiresSoul()) {
      builder.addDrawable(
          soulIndicator, NaturesRitualViewerIndicator.X, NaturesRitualViewerIndicator.Y);
      builder.addWidget(new SoulIndicatorTooltip());
    }
  }

  private static final class SoulIndicatorTooltip implements IRecipeWidget {
    @Override
    public ScreenPosition getPosition() {
      return new ScreenPosition(
          NaturesRitualViewerIndicator.X, NaturesRitualViewerIndicator.Y);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, double mouseX, double mouseY) {
      if (mouseX >= 0
          && mouseX < NaturesRitualViewerIndicator.WIDTH
          && mouseY >= 0
          && mouseY < NaturesRitualViewerIndicator.HEIGHT) {
        tooltip.add(Component.translatable(NaturesRitualViewerIndicator.TOOLTIP_KEY));
      }
    }
  }
}
