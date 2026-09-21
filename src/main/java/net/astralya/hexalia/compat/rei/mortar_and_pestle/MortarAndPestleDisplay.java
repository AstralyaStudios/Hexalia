package net.astralya.hexalia.compat.rei.mortar_and_pestle;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.astralya.hexalia.recipe.MortarAndPestleRecipe;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MortarAndPestleDisplay extends BasicDisplay {

    @SuppressWarnings("unused")
    public MortarAndPestleDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    public MortarAndPestleDisplay(MortarAndPestleRecipe recipe) {
        super(
                getInputList(recipe),
                List.of(EntryIngredient.of(EntryStacks.of(recipe.getResultItem(null).copy())))
        );
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return MortarAndPestleCategory.MORTAR_AND_PESTLE;
    }

    private static List<EntryIngredient> getInputList(MortarAndPestleRecipe recipe) {
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