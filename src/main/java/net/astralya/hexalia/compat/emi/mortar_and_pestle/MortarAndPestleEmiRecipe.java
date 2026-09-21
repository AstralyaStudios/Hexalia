package net.astralya.hexalia.compat.emi.mortar_and_pestle;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.recipe.MortarAndPestleRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public final class MortarAndPestleEmiRecipe implements EmiRecipe {

    public static final ResourceLocation TEXTURE = new ResourceLocation(HexaliaMod.MODID, "textures/gui/mortar_gui.png");

    private final MortarAndPestleRecipe recipe;
    private final List<EmiIngredient> inputs;
    private final List<EmiStack> outputs;

    public MortarAndPestleEmiRecipe(MortarAndPestleRecipe recipe) {
        this.recipe = recipe;
        this.inputs = getInputs(recipe);
        this.outputs = List.of(EmiStack.of(recipe.getResultItem(null).copy()));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return MortarAndPestleEmiCategory.CATEGORY;
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

        if (!this.inputs.isEmpty()) {
            widgets.addSlot(this.inputs.get(0), 27, 30)
                    .drawBack(false);
        }

        if (this.inputs.size() > 1) {
            widgets.addSlot(this.inputs.get(1), 3, 30)
                    .drawBack(false);
        }

        if (this.inputs.size() > 2) {
            widgets.addSlot(this.inputs.get(2), 51, 30)
                    .drawBack(false);
        }

        widgets.addSlot(this.outputs.get(0), 88, 30)
                .drawBack(false)
                .recipeContext(this);
    }

    private static List<EmiIngredient> getInputs(MortarAndPestleRecipe recipe) {
        List<EmiIngredient> list = new ArrayList<>();

        for (Ingredient ingredient : recipe.getIngredients()) {
            list.add(EmiIngredient.of(ingredient));
        }

        return list;
    }
}