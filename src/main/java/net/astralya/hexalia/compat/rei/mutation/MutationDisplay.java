package net.astralya.hexalia.compat.rei.mutation;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.astralya.hexalia.recipe.MutationRecipe;

import java.util.Collections;
import java.util.List;

public final class MutationDisplay extends BasicDisplay {

    @SuppressWarnings("unused")
    public MutationDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    public MutationDisplay(MutationRecipe recipe) {
        super(
                recipe.getIngredients().isEmpty()
                        ? Collections.emptyList()
                        : List.of(EntryIngredients.ofIngredient(recipe.getIngredients().get(0))),
                List.of(EntryIngredient.of(EntryStacks.of(recipe.getResultItem(null).copy())))
        );
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return MutationCategory.MUTATION;
    }
}