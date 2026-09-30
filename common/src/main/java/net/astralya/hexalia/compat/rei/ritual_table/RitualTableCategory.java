package net.astralya.hexalia.compat.rei.ritual_table;

import java.util.LinkedList;
import java.util.List;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.compat.NaturesRitualViewer;
import net.astralya.hexalia.compat.rei.HexaliaREIClientPlugin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public final class RitualTableCategory implements DisplayCategory<RitualTableDisplay> {

  public static final ResourceLocation TEXTURE =
      new ResourceLocation(HexaliaMod.MODID, "textures/gui/ritual_table_gui.png");
  private final net.astralya.hexalia.recipe.RitualTableRecipe.RitualKind kind;

  public RitualTableCategory(net.astralya.hexalia.recipe.RitualTableRecipe.RitualKind kind) {
    this.kind = kind;
  }

  @Override
  public CategoryIdentifier<? extends RitualTableDisplay> getCategoryIdentifier() {
    return CategoryIdentifier.of(NaturesRitualViewer.categoryId(kind));
  }

  @Override
  public Component getTitle() {
    return NaturesRitualViewer.categoryTitle(kind);
  }

  @Override
  public Renderer getIcon() {
    return EntryStacks.of(ModBlocks.RITUAL_TABLE.get());
  }

  @Override
  public List<Widget> setupDisplay(RitualTableDisplay display, Rectangle bounds) {
    Point origin = bounds.getLocation();
    List<Widget> widgets = new LinkedList<>();
    widgets.add(Widgets.createRecipeBase(bounds));

    int guiWidth = NaturesRitualViewer.WIDTH;
    int guiHeight = NaturesRitualViewer.HEIGHT;

    Rectangle startPoint =
        HexaliaREIClientPlugin.centeredIntoRecipeBase(origin, guiWidth, guiHeight);
    widgets.add(
        Widgets.createTexturedWidget(
            TEXTURE, startPoint.x, startPoint.y, 0, 0, guiWidth, guiHeight));

    List<EntryIngredient> ingredientEntries = display.getInputEntries();

    if (ingredientEntries != null) {
      for (int i = 0;
          i < Math.min(ingredientEntries.size(), NaturesRitualViewer.INPUT_X.length);
          i++) {
        widgets.add(
            Widgets.createSlot(
                    new Point(
                        startPoint.x + NaturesRitualViewer.INPUT_X[i],
                        startPoint.y + NaturesRitualViewer.INPUT_Y[i]))
                .entries(ingredientEntries.get(i))
                .markInput()
                .disableBackground());
      }
    }

    List<EntryIngredient> outputs = display.getOutputEntries();
    if (outputs != null && !outputs.isEmpty()) {
      widgets.add(
          Widgets.createSlot(
                  new Point(
                      startPoint.x + NaturesRitualViewer.OUTPUT_X,
                      startPoint.y + NaturesRitualViewer.OUTPUT_Y))
              .entries(outputs.get(0))
              .markOutput()
              .disableBackground());
    }

    widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + NaturesRitualViewer.ENERGY_Y + 2))
        .entries(display.energyEntries()).disableBackground());
    widgets.add(Widgets.createLabel(new Point(startPoint.x + 24, startPoint.y + 86), display.energyFirstLine())
        .color(0xA9A29C).noShadow());
    widgets.add(Widgets.createLabel(new Point(startPoint.x + 24, startPoint.y + 97), display.energySecondLine())
        .color(0xA9A29C).noShadow());
    widgets.add(Widgets.createTooltip(new Rectangle(startPoint.x + 4,
        startPoint.y + NaturesRitualViewer.ENERGY_Y, 110, 28), display.energyTooltip()));

    if (display.requiresSoul()) {
      widgets.add(
          Widgets.createTexturedWidget(
              NaturesRitualViewer.SOUL_ICON,
              startPoint.x + NaturesRitualViewer.SOUL_X,
              startPoint.y + NaturesRitualViewer.SOUL_Y,
              0,
              0,
              NaturesRitualViewer.SOUL_SIZE,
              NaturesRitualViewer.SOUL_SIZE));
      widgets.add(
          Widgets.createTooltip(
              new Rectangle(
                  startPoint.x + NaturesRitualViewer.SOUL_X,
                  startPoint.y + NaturesRitualViewer.SOUL_Y,
                  NaturesRitualViewer.SOUL_SIZE,
                  NaturesRitualViewer.SOUL_SIZE),
              Component.translatable(NaturesRitualViewer.SOUL_TOOLTIP)));
    }

    return widgets;
  }

  @Override
  public int getDisplayHeight() {
    return NaturesRitualViewer.ENERGY_Y + NaturesRitualViewer.ENERGY_HEIGHT;
  }
}
