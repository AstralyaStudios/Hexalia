package net.astralya.hexalia.compat.rei.ritual_table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.astralya.hexalia.compat.NaturesRitualViewer;
import net.astralya.hexalia.recipe.RitualTableRecipe;

public final class RitualTableDisplay extends BasicDisplay {
  private boolean requiresSoul;
  private RitualTableRecipe.RitualKind kind;
  private int energyCost;
  private List<EntryIngredient> energyEntries;

  @SuppressWarnings("unused")
  public RitualTableDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
    super(inputs, outputs);
    this.requiresSoul = false;
    this.kind = RitualTableRecipe.RitualKind.NATURE;
    this.energyCost = 0;
    this.energyEntries = List.of();
  }

  public RitualTableDisplay(RitualTableRecipe recipe) {
    super(
        getInputList(recipe),
        List.of(EntryIngredient.of(EntryStacks.of(NaturesRitualViewer.result(recipe)))));
    this.requiresSoul = recipe.requiresSoul();
    this.kind = recipe.ritualKind();
    this.energyCost = recipe.resolvedEnergyCost();
    this.energyEntries = List.of(EntryIngredient.of(
        NaturesRitualViewer.energyIcons(recipe).stream().map(EntryStacks::of).toList()));
  }

  public boolean requiresSoul() {
    return requiresSoul;
  }

  public EntryIngredient energyEntries() { return energyEntries.get(0); }

  public net.minecraft.network.chat.Component energyLabel() {
    return net.minecraft.network.chat.Component.translatable(
        "jei.hexalia.energy." + (kind == RitualTableRecipe.RitualKind.CELESTIAL ? "bloom" : "crop")
            + "_label", energyCost);
  }

  public net.minecraft.network.chat.Component energyFirstLine() {
    return net.minecraft.network.chat.Component.translatable(
        "jei.hexalia.energy." + (kind == RitualTableRecipe.RitualKind.CELESTIAL ? "bloom" : "crop")
            + "_line_1", energyCost);
  }

  public net.minecraft.network.chat.Component energySecondLine() {
    return net.minecraft.network.chat.Component.translatable(
        "jei.hexalia.energy." + (kind == RitualTableRecipe.RitualKind.CELESTIAL ? "bloom" : "crop")
            + "_line_2");
  }

  public List<net.minecraft.network.chat.Component> energyTooltip() {
    String prefix = "jei.hexalia.energy." + (kind == RitualTableRecipe.RitualKind.CELESTIAL ? "bloom" : "crop");
    return List.of(net.minecraft.network.chat.Component.translatable(prefix + "_title"),
        net.minecraft.network.chat.Component.translatable(prefix + "_requirement", energyCost),
        net.minecraft.network.chat.Component.translatable(prefix + "_effect"));
  }

  @Override
  public CategoryIdentifier<?> getCategoryIdentifier() {
    return CategoryIdentifier.of(NaturesRitualViewer.categoryId(kind));
  }

  private static List<EntryIngredient> getInputList(RitualTableRecipe recipe) {
    if (recipe == null) {
      return Collections.emptyList();
    }

    List<EntryIngredient> list = new ArrayList<>();
    for (int i = 0;
        i < Math.min(recipe.getIngredients().size(), NaturesRitualViewer.INPUT_X.length);
        i++) {
      list.add(EntryIngredients.ofIngredient(recipe.getIngredients().get(i)));
    }
    return list;
  }
}
