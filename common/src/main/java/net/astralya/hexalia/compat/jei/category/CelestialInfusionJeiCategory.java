package net.astralya.hexalia.compat.jei.category;

import java.util.List;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import net.astralya.hexalia.compat.HexaliaRecipeGuiLayout;
import net.astralya.hexalia.compat.RitualEnergyViewerIndicator;
import net.astralya.hexalia.compat.jei.HexaliaJeiRecipeTypes;
import net.astralya.hexalia.compat.jei.util.JeiLayoutHelper;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.recipe.NaturesRitualRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public final class CelestialInfusionJeiCategory
    extends AbstractHexaliaJeiCategory<NaturesRitualRecipe> {
  public CelestialInfusionJeiCategory(IGuiHelper guiHelper) {
    super(
        guiHelper,
        HexaliaJeiRecipeTypes.CELESTIAL_INFUSION,
        "jei.hexalia.category.celestial_infusion",
        ModItems.RITUAL_TABLE.get(),
        HexaliaRecipeGuiLayout.NATURES_RITUAL);
  }

  @Override
  public int getHeight() {
    return RitualEnergyViewerIndicator.Y + RitualEnergyViewerIndicator.HEIGHT;
  }

  @Override
  public void setRecipe(
      IRecipeLayoutBuilder builder, NaturesRitualRecipe recipe, IFocusGroup focuses) {
    HexaliaRecipeGuiLayout layout = HexaliaRecipeGuiLayout.NATURES_RITUAL;
    JeiLayoutHelper.addInput(
        builder, recipe.centerIngredient(), layout.inputX(0), layout.inputY(0));
    for (int index = 0; index < Math.min(recipe.offerings().size(), 8); index++)
      JeiLayoutHelper.addInput(
          builder,
          recipe.offerings().get(index),
          layout.inputX(index + 1),
          layout.inputY(index + 1));
    var energy = RitualEnergyViewerIndicator.of(recipe);
    builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 4, RitualEnergyViewerIndicator.Y + 2).addItemStacks(energy.icons());
    JeiLayoutHelper.addOutput(builder, recipe.itemResult(), layout);
  }

  @Override
  public void getTooltip(
      ITooltipBuilder tooltipBuilder,
      NaturesRitualRecipe recipe,
      IRecipeSlotsView recipeSlotsView,
      double mouseX,
      double mouseY) {
    if (mouseX >= 0 && mouseX <= getWidth() && mouseY >= 0 && mouseY <= getHeight()) {
      tooltipBuilder.addAll(
          List.of(
              Component.translatable("jei.hexalia.tooltip.requires_hex_focus")
                  .withStyle(ChatFormatting.GRAY),
              Component.translatable("jei.hexalia.tooltip.requires_salted_brazier")
                  .withStyle(ChatFormatting.GRAY),
              Component.translatable("message.hexalia.celestial_infusion.requires_night")
                  .withStyle(ChatFormatting.GRAY)));
    }
    if (mouseY >= RitualEnergyViewerIndicator.Y)
      tooltipBuilder.addAll(RitualEnergyViewerIndicator.of(recipe).tooltip());
  }

  @Override
  public void draw(NaturesRitualRecipe recipe, IRecipeSlotsView recipeSlotsView, net.minecraft.client.gui.GuiGraphics guiGraphics, double mouseX, double mouseY) {
    super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
    drawEnergyLabel(guiGraphics, recipe);
  }

  private static void drawEnergyLabel(net.minecraft.client.gui.GuiGraphics guiGraphics, NaturesRitualRecipe recipe) {
    var font = net.minecraft.client.Minecraft.getInstance().font;
    guiGraphics.fill(4, 81, 114, 82, 0xFF706C77);
    guiGraphics.drawWordWrap(font, RitualEnergyViewerIndicator.of(recipe).label(), 24, 86, 90, 0xA9A29C);
  }
}
