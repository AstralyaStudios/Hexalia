package net.astralya.hexalia.compat.emi.mortar_and_pestle;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.util.Identifier;

public final class MortarAndPestleEmiCategory {
    public static final Identifier ID = new Identifier(HexaliaMod.MODID, "mortar_and_pestle");
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(ID, EmiStack.of(ModItems.MORTAR_AND_PESTLE));

    private MortarAndPestleEmiCategory() {
    }
}