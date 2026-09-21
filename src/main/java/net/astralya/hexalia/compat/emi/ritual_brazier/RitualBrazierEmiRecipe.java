package net.astralya.hexalia.compat.emi.ritual_brazier;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.recipe.RitualBrazierRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public final class RitualBrazierEmiRecipe implements EmiRecipe {

    public static final ResourceLocation TEXTURE = new ResourceLocation(HexaliaMod.MODID, "textures/gui/ritual_brazier_gui.png");

    private final RitualBrazierRecipe recipe;
    private final List<EmiIngredient> inputs;
    private final List<EmiStack> outputs;

    public RitualBrazierEmiRecipe(RitualBrazierRecipe recipe) {
        this.recipe = recipe;
        this.inputs = List.of(EmiIngredient.of(recipe.getInput()));
        this.outputs = List.of(EmiStack.of(recipe.getResultItem(null).copy()));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return RitualBrazierEmiCategory.CATEGORY;
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
        return 118;
    }

    @Override
    public int getDisplayHeight() {
        return 80;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(TEXTURE, 0, 0, 118, 80, 0, 0);

        widgets.addSlot(this.inputs.get(0), 27, 30)
                .drawBack(false);

        widgets.addSlot(this.outputs.get(0), 88, 30)
                .drawBack(false)
                .recipeContext(this);

        widgets.addTexture(TEXTURE, 50, 30, 16, 16, 118, 0);

        widgets.addTooltipText(
                List.of(Component.translatable("tooltip.hexalia.hex_focus_gui")),
                3,
                53,
                16,
                16
        );
    }
}