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

  @SuppressWarnings("unused")
  public RitualTableDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
    super(inputs, outputs);
    this.requiresSoul = false;
  }

  public RitualTableDisplay(RitualTableRecipe recipe) {
    super(
        getInputList(recipe),
        List.of(EntryIngredient.of(EntryStacks.of(NaturesRitualViewer.result(recipe)))));
    this.requiresSoul = recipe.requiresSoul();
  }

  public boolean requiresSoul() {
    return requiresSoul;
  }

  @Override
  public CategoryIdentifier<?> getCategoryIdentifier() {
    return RitualTableCategory.RITUAL_TABLE;
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
