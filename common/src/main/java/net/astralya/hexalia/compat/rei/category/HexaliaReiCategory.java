package net.astralya.hexalia.compat.rei.category;

import java.util.ArrayList;
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
import net.astralya.hexalia.compat.HexaliaRecipeGuiLayout;
import net.astralya.hexalia.compat.NaturesRitualViewerIndicator;
import net.astralya.hexalia.compat.rei.HexaliaReiDisplay;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ItemLike;

public final class HexaliaReiCategory implements DisplayCategory<HexaliaReiDisplay> {
  private final CategoryIdentifier<HexaliaReiDisplay> category;
  private final Component title;
  private final Renderer icon;
  private final HexaliaRecipeGuiLayout layout;

  public HexaliaReiCategory(
      CategoryIdentifier<HexaliaReiDisplay> category,
      String titleKey,
      ItemLike iconItem,
      HexaliaRecipeGuiLayout layout) {
    this.category = category;
    this.title = Component.translatable(titleKey);
    this.icon = EntryStacks.of(iconItem);
    this.layout = layout;
  }

  @Override
  public CategoryIdentifier<? extends HexaliaReiDisplay> getCategoryIdentifier() {
    return category;
  }

  @Override
  public Component getTitle() {
    return title;
  }

  @Override
  public Renderer getIcon() {
    return icon;
  }

  @Override
  public int getDisplayWidth(HexaliaReiDisplay display) {
    return layout.width();
  }

  @Override
  public int getDisplayHeight() {
    return layout.height();
  }

  @Override
  public int getMaximumDisplaysPerPage() {
    return 1;
  }

  @Override
  public List<Widget> setupDisplay(HexaliaReiDisplay display, Rectangle bounds) {
    List<Widget> widgets = new ArrayList<>();
    widgets.add(
        Widgets.createTexturedWidget(
            layout.texture(),
            bounds,
            layout.textureU(),
            layout.textureV(),
            layout.textureWidth(),
            layout.textureHeight(),
            256,
            256));

    switch (display.getLayout()) {
      case MORTAR_AND_PESTLE -> addMortarAndPestle(widgets, display, bounds);
      case SMALL_CAULDRON -> addSmallCauldron(widgets, display, bounds);
      case NATURES_RITUAL -> addNaturesRitual(widgets, display, bounds);
      case CELESTIAL_INFUSION, MUTATION -> addSimpleInputOutput(widgets, display, bounds);
    }

    if (!display.getRecipeTooltips().isEmpty()) {
      widgets.add(Widgets.createTooltip(bounds, display.getRecipeTooltips()));
    }
    return widgets;
  }

  private static void addMortarAndPestle(
      List<Widget> widgets, HexaliaReiDisplay display, Rectangle bounds) {
    addInputs(widgets, display, bounds, HexaliaRecipeGuiLayout.MORTAR_AND_PESTLE);
    addOutput(widgets, display, bounds, HexaliaRecipeGuiLayout.MORTAR_AND_PESTLE);
  }

  private static void addSmallCauldron(
      List<Widget> widgets, HexaliaReiDisplay display, Rectangle bounds) {
    addInputs(widgets, display, bounds, HexaliaRecipeGuiLayout.SMALL_CAULDRON);
    addOutput(widgets, display, bounds, HexaliaRecipeGuiLayout.SMALL_CAULDRON);
  }

  private static void addNaturesRitual(
      List<Widget> widgets, HexaliaReiDisplay display, Rectangle bounds) {
    addInputs(widgets, display, bounds, HexaliaRecipeGuiLayout.NATURES_RITUAL);
    addOutput(widgets, display, bounds, HexaliaRecipeGuiLayout.NATURES_RITUAL);
    if (display.showSoulIndicator()) {
      widgets.add(
          Widgets.createTexturedWidget(
              NaturesRitualViewerIndicator.TEXTURE,
              new Rectangle(
                  bounds.x + NaturesRitualViewerIndicator.X,
                  bounds.y + NaturesRitualViewerIndicator.Y,
                  NaturesRitualViewerIndicator.WIDTH,
                  NaturesRitualViewerIndicator.HEIGHT),
              0,
              0,
              NaturesRitualViewerIndicator.WIDTH,
              NaturesRitualViewerIndicator.HEIGHT,
              NaturesRitualViewerIndicator.WIDTH,
              NaturesRitualViewerIndicator.HEIGHT));
      widgets.add(
          Widgets.createTooltip(
              new Rectangle(
                  bounds.x + NaturesRitualViewerIndicator.X,
                  bounds.y + NaturesRitualViewerIndicator.Y,
                  NaturesRitualViewerIndicator.WIDTH,
                  NaturesRitualViewerIndicator.HEIGHT),
              List.of(Component.translatable(NaturesRitualViewerIndicator.TOOLTIP_KEY))));
    }
  }

  private static void addSimpleInputOutput(
      List<Widget> widgets, HexaliaReiDisplay display, Rectangle bounds) {
    HexaliaRecipeGuiLayout layout =
        display.getLayout() == HexaliaReiDisplay.Layout.CELESTIAL_INFUSION
            ? HexaliaRecipeGuiLayout.CELESTIAL_INFUSION
            : HexaliaRecipeGuiLayout.MUTATION;
    addInputs(widgets, display, bounds, layout);
    addOutput(widgets, display, bounds, layout);
  }

  private static void addInputs(
      List<Widget> widgets,
      HexaliaReiDisplay display,
      Rectangle bounds,
      HexaliaRecipeGuiLayout layout) {
    List<EntryIngredient> inputs = display.getInputEntries();
    for (int index = 0; index < inputs.size(); index++) {
      addInput(widgets, inputs.get(index), bounds, layout.inputX(index), layout.inputY(index));
    }
  }

  private static void addInput(
      List<Widget> widgets, EntryIngredient input, Rectangle bounds, int x, int y) {
    widgets.add(
        Widgets.createSlot(point(bounds, x + 1, y + 1))
            .entries(input)
            .markInput()
            .disableBackground());
  }

  private static void addOutput(
      List<Widget> widgets,
      HexaliaReiDisplay display,
      Rectangle bounds,
      HexaliaRecipeGuiLayout layout) {
    if (display.getOutputEntries().isEmpty()) {
      return;
    }

    widgets.add(
        Widgets.createSlot(point(bounds, layout.outputX() + 1, layout.outputY() + 1))
            .entries(display.getOutputEntries().get(0))
            .markOutput()
            .disableBackground());
  }

  private static Point point(Rectangle bounds, int x, int y) {
    return new Point(bounds.x + x, bounds.y + y);
  }
}
