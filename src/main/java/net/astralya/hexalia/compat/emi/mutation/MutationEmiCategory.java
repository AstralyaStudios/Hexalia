package net.astralya.hexalia.compat.emi.mutation;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.resources.ResourceLocation;

public final class MutationEmiCategory {
    public static final ResourceLocation ID = new ResourceLocation(HexaliaMod.MODID, "mutation");
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(ID, EmiStack.of(ModItems.MUTAVIS.get()));

    private MutationEmiCategory() {
    }
}