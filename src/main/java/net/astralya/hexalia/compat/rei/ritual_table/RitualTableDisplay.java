package net.astralya.hexalia.compat.rei.ritual_table;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.astralya.hexalia.compat.RitualTableViewerLayout;
import net.astralya.hexalia.recipe.RitualTableRecipe;
import java.util.ArrayList;
import java.util.List;

public final class RitualTableDisplay extends BasicDisplay {
    private final boolean requiresSoul;

    public RitualTableDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
        requiresSoul = false;
    }

    public RitualTableDisplay(RitualTableRecipe recipe) {
        super(inputs(recipe), List.of(EntryIngredient.of(EntryStacks.of(RitualTableViewerLayout.displayResult(recipe)))));
        requiresSoul = recipe.requiresSoul();
    }

    @Override public CategoryIdentifier<?> getCategoryIdentifier() { return RitualTableCategory.RITUAL_TABLE; }
    public boolean requiresSoul() { return requiresSoul; }

    private static List<EntryIngredient> inputs(RitualTableRecipe recipe) {
        List<EntryIngredient> inputs = new ArrayList<>();
        inputs.add(EntryIngredients.ofIngredient(recipe.centerIngredient()));
        recipe.offerings().stream().limit(8).map(EntryIngredients::ofIngredient).forEach(inputs::add);
        return inputs;
    }
}
