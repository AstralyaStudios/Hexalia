package net.astralya.hexalia.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import java.util.List;
import net.astralya.hexalia.compat.HexaliaRecipeGuiLayout;
import net.astralya.hexalia.compat.NaturesRitualViewerIndicator;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public final class HexaliaEmiRecipe implements EmiRecipe {
  private final EmiRecipeCategory category;
  private final HexaliaRecipeGuiLayout layout;
  private final ResourceLocation id;
  private final List<EmiIngredient> inputs;
  private final List<EmiStack> outputs;
  private final List<Component> recipeTooltips;
  private final List<Component> outputTooltips;
  private final boolean drawRitualBrazierFocus;
  private final boolean showRitualTableFocusTooltip;
  private final boolean showMutationTooltip;
  private final boolean showSoulIndicator;

  public HexaliaEmiRecipe(
      EmiRecipeCategory category,
      HexaliaRecipeGuiLayout layout,
      ResourceLocation id,
      List<EmiIngredient> inputs,
      EmiStack output) {
    this(category, layout, id, inputs, output, List.of(), List.of());
  }

  public HexaliaEmiRecipe(
      EmiRecipeCategory category,
      HexaliaRecipeGuiLayout layout,
      ResourceLocation id,
      List<EmiIngredient> inputs,
      EmiStack output,
      List<Component> recipeTooltips,
      List<Component> outputTooltips) {
    this(
        category,
        layout,
        id,
        inputs,
        output,
        recipeTooltips,
        outputTooltips,
        false,
        false,
        false,
        false);
  }

  public HexaliaEmiRecipe(
      EmiRecipeCategory category,
      HexaliaRecipeGuiLayout layout,
      ResourceLocation id,
      List<EmiIngredient> inputs,
      EmiStack output,
      List<Component> recipeTooltips,
      List<Component> outputTooltips,
      boolean drawRitualBrazierFocus,
      boolean showRitualTableFocusTooltip,
      boolean showMutationTooltip,
      boolean showSoulIndicator) {
    this.category = category;
    this.layout = layout;
    this.id = id;
    this.inputs = List.copyOf(inputs);
    this.outputs = List.of(output);
    this.recipeTooltips = List.copyOf(recipeTooltips);
    this.outputTooltips = List.copyOf(outputTooltips);
    this.drawRitualBrazierFocus = drawRitualBrazierFocus;
    this.showRitualTableFocusTooltip = showRitualTableFocusTooltip;
    this.showMutationTooltip = showMutationTooltip;
    this.showSoulIndicator = showSoulIndicator;
  }

  @Override
  public EmiRecipeCategory getCategory() {
    return category;
  }

  @Override
  public @Nullable ResourceLocation getId() {
    return id;
  }

  @Override
  public List<EmiIngredient> getInputs() {
    return inputs;
  }

  @Override
  public List<EmiStack> getOutputs() {
    return outputs;
  }

  @Override
  public int getDisplayWidth() {
    return layout.width();
  }

  @Override
  public int getDisplayHeight() {
    return layout.height();
  }

  @Override
  public void addWidgets(WidgetHolder widgets) {
    widgets.addTexture(
        layout.texture(),
        0,
        0,
        layout.width(),
        layout.height(),
        layout.textureU(),
        layout.textureV(),
        layout.textureWidth(),
        layout.textureHeight(),
        256,
        256);

    if (showSoulIndicator) {
      widgets.addTexture(
          NaturesRitualViewerIndicator.TEXTURE,
          NaturesRitualViewerIndicator.X,
          NaturesRitualViewerIndicator.Y,
          NaturesRitualViewerIndicator.WIDTH,
          NaturesRitualViewerIndicator.HEIGHT,
          0,
          0,
          NaturesRitualViewerIndicator.WIDTH,
          NaturesRitualViewerIndicator.HEIGHT,
          NaturesRitualViewerIndicator.WIDTH,
          NaturesRitualViewerIndicator.HEIGHT);
      widgets.addTooltipText(
          List.of(Component.translatable(NaturesRitualViewerIndicator.TOOLTIP_KEY)),
          NaturesRitualViewerIndicator.X,
          NaturesRitualViewerIndicator.Y,
          NaturesRitualViewerIndicator.WIDTH,
          NaturesRitualViewerIndicator.HEIGHT);
    }

    if (!recipeTooltips.isEmpty()) {
      widgets.addTooltipText(recipeTooltips, 0, 0, layout.width(), layout.height());
    }

    if (drawRitualBrazierFocus) {
      widgets.addTexture(layout.texture(), 50, 30, 16, 16, 118, 0);
      widgets.addTooltipText(
          List.of(Component.translatable("tooltip.hexalia.hex_focus_gui")), 3, 53, 16, 16);
    }

    if (showRitualTableFocusTooltip) {
      widgets.addTooltipText(
          List.of(Component.translatable("tooltip.hexalia.hex_focus_gui")), 3, 62, 16, 16);
    }

    if (showMutationTooltip) {
      widgets.addTooltipText(
          List.of(Component.translatable("tooltip.hexalia.mutation")), 4, 55, 16, 16);
    }

    for (int index = 0; index < inputs.size(); index++) {
      widgets
          .addSlot(inputs.get(index), layout.inputX(index), layout.inputY(index))
          .drawBack(false);
    }

    if (!outputs.isEmpty()) {
      var outputSlot =
          widgets
              .addSlot(outputs.get(0), layout.outputX(), layout.outputY())
              .drawBack(false)
              .recipeContext(this);
      for (Component tooltip : outputTooltips) {
        outputSlot.appendTooltip(tooltip);
      }
    }
  }
}
