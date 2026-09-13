package net.astralya.hexalia.compat.emi.ritual_table;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.compat.RitualTableViewerLayout;
import net.astralya.hexalia.recipe.RitualTableRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import java.util.ArrayList;
import java.util.List;

public final class RitualTableEmiRecipe implements EmiRecipe {
    public static final ResourceLocation TEXTURE = new ResourceLocation(HexaliaMod.MODID, "textures/gui/ritual_table_gui.png");
    private final RitualTableRecipe recipe;
    private final List<EmiIngredient> inputs;
    private final List<EmiStack> outputs;

    public RitualTableEmiRecipe(RitualTableRecipe recipe) {
        this.recipe = recipe;
        inputs = new ArrayList<>();
        inputs.add(EmiIngredient.of(recipe.centerIngredient()));
        recipe.offerings().stream().limit(8).map(EmiIngredient::of).forEach(inputs::add);
        outputs = List.of(EmiStack.of(RitualTableViewerLayout.displayResult(recipe)));
    }

    @Override public EmiRecipeCategory getCategory() { return RitualTableEmiCategory.CATEGORY; }
    @Override public ResourceLocation getId() { return recipe.getId(); }
    @Override public List<EmiIngredient> getInputs() { return inputs; }
    @Override public List<EmiStack> getOutputs() { return outputs; }
    @Override public int getDisplayWidth() { return RitualTableViewerLayout.WIDTH; }
    @Override public int getDisplayHeight() { return RitualTableViewerLayout.HEIGHT; }

    @Override public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(TEXTURE, 0, 0, RitualTableViewerLayout.WIDTH, RitualTableViewerLayout.HEIGHT, 0, 0);
        for (int index = 0; index < inputs.size(); index++) {
            int[] position = RitualTableViewerLayout.INPUTS[index];
            widgets.addSlot(inputs.get(index), position[0], position[1]).drawBack(false);
        }
        widgets.addSlot(outputs.get(0), RitualTableViewerLayout.OUTPUT_X, RitualTableViewerLayout.OUTPUT_Y)
                .drawBack(false).recipeContext(this);
        if (recipe.requiresSoul()) {
            widgets.addTexture(RitualTableViewerLayout.SOUL_TEXTURE,
                    RitualTableViewerLayout.SOUL_X, RitualTableViewerLayout.SOUL_Y,
                    RitualTableViewerLayout.SOUL_SIZE, RitualTableViewerLayout.SOUL_SIZE, 0, 0);
            widgets.addTooltipText(List.of(Component.translatable(RitualTableViewerLayout.SOUL_TOOLTIP)),
                    RitualTableViewerLayout.SOUL_X, RitualTableViewerLayout.SOUL_Y,
                    RitualTableViewerLayout.SOUL_SIZE, RitualTableViewerLayout.SOUL_SIZE);
        }
    }
}
