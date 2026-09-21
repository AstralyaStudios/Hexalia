package net.astralya.hexalia.compat.emi.mutation;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.util.Identifier;

public final class MutationEmiCategory {
    public static final Identifier ID = new Identifier(HexaliaMod.MODID, "mutation");
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(ID, EmiStack.of(ModItems.MUTAVIS));

    private MutationEmiCategory() {
    }
}