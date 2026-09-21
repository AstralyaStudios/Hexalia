package net.astralya.hexalia.compat.rei.ritual_brazier;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.astralya.hexalia.recipe.RitualBrazierRecipe;

import java.util.Collections;
import java.util.List;

public class RitualBrazierDisplay extends BasicDisplay {

    @SuppressWarnings("unused")
    public RitualBrazierDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    public RitualBrazierDisplay(RitualBrazierRecipe recipe) {
        super(getInputList(recipe), getOutputList(recipe));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return RitualBrazierCategory.RITUAL_BRAZIER;
    }

    private static List<EntryIngredient> getInputList(RitualBrazierRecipe recipe) {
        if (recipe == null) {
            return Collections.emptyList();
        }

        return List.of(EntryIngredients.ofIngredient(recipe.getInput()));
    }

    private static List<EntryIngredient> getOutputList(RitualBrazierRecipe recipe) {
        if (recipe == null) {
            return Collections.emptyList();
        }

        return List.of(EntryIngredient.of(EntryStacks.of(recipe.getResultItem(null).copy())));
    }
}