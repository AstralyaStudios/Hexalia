package net.astralya.hexalia.compat.emi.ritual_table;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.astralya.hexalia.compat.NaturesRitualViewer;
import net.astralya.hexalia.recipe.RitualTableRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public final class RitualTableEmiRecipe implements EmiRecipe {

    private final RitualTableRecipe recipe;
    private final List<EmiIngredient> inputs;
    private final List<EmiStack> outputs;

    public RitualTableEmiRecipe(RitualTableRecipe recipe) {
        this.recipe = recipe;
        this.inputs = getInputs(recipe);
        this.outputs = List.of(EmiStack.of(NaturesRitualViewer.result(recipe)));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return RitualTableEmiCategory.CATEGORY;
    }

    @Override
    public ResourceLocation getId() {
        return this.recipe.getId();
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return this.inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return this.outputs;
    }

    @Override
    public int getDisplayWidth() {
        return NaturesRitualViewer.WIDTH;
    }

    @Override
    public int getDisplayHeight() {
        return NaturesRitualViewer.HEIGHT;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(NaturesRitualViewer.TEXTURE, 0, 0, NaturesRitualViewer.WIDTH, NaturesRitualViewer.HEIGHT, 0, 0);

        for (int i = 0; i < Math.min(this.inputs.size(), NaturesRitualViewer.INPUT_X.length); i++) {
            widgets.addSlot(this.inputs.get(i), NaturesRitualViewer.INPUT_X[i],
                    NaturesRitualViewer.INPUT_Y[i]).drawBack(false);
        }
        if (recipe.requiresSoul()) {
            widgets.addTexture(NaturesRitualViewer.SOUL_ICON, NaturesRitualViewer.SOUL_X, NaturesRitualViewer.SOUL_Y,
                    NaturesRitualViewer.SOUL_SIZE, NaturesRitualViewer.SOUL_SIZE, 0, 0,
                    NaturesRitualViewer.SOUL_SIZE, NaturesRitualViewer.SOUL_SIZE,
                    NaturesRitualViewer.SOUL_SIZE, NaturesRitualViewer.SOUL_SIZE);
            widgets.addTooltipText(List.of(Component.translatable(NaturesRitualViewer.SOUL_TOOLTIP)),
                    NaturesRitualViewer.SOUL_X, NaturesRitualViewer.SOUL_Y, NaturesRitualViewer.SOUL_SIZE, NaturesRitualViewer.SOUL_SIZE);
        }

        widgets.addSlot(this.outputs.get(0), NaturesRitualViewer.OUTPUT_X, NaturesRitualViewer.OUTPUT_Y)
                .drawBack(false)
                .recipeContext(this);
    }

    private static List<EmiIngredient> getInputs(RitualTableRecipe recipe) {
        List<EmiIngredient> list = new ArrayList<>();

        for (int i = 0; i < Math.min(recipe.getIngredients().size(), NaturesRitualViewer.INPUT_X.length); i++) {
            list.add(EmiIngredient.of(recipe.getIngredients().get(i)));
        }

        return list;
    }
}
