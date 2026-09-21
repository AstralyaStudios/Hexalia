package net.astralya.hexalia.compat.rei.small_cauldron;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.astralya.hexalia.recipe.SmallCauldronRecipe;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class SmallCauldronDisplay extends BasicDisplay {

    @SuppressWarnings("unused")
    public SmallCauldronDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    public SmallCauldronDisplay(SmallCauldronRecipe recipe) {
        super(
                getInputList(recipe),
                List.of(EntryIngredient.of(EntryStacks.of(recipe.getResultItem(null).copy())))
        );
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return SmallCauldronCategory.SMALL_CAULDRON;
    }

    private static List<EntryIngredient> getInputList(SmallCauldronRecipe recipe) {
        if (recipe == null) {
            return Collections.emptyList();
        }

        List<EntryIngredient> list = new ArrayList<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
            list.add(EntryIngredients.ofIngredient(ingredient));
        }
        return list;
    }
}