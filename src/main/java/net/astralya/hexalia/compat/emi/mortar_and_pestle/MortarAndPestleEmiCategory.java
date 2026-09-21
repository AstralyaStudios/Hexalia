package net.astralya.hexalia.compat.emi.mortar_and_pestle;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.resources.ResourceLocation;

public final class MortarAndPestleEmiCategory {
    public static final ResourceLocation ID = new ResourceLocation(HexaliaMod.MODID, "mortar_and_pestle");
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(ID, EmiStack.of(ModItems.MORTAR_AND_PESTLE.get()));

    private MortarAndPestleEmiCategory() {
    }
}