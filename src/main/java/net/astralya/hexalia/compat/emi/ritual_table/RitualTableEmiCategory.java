package net.astralya.hexalia.compat.emi.ritual_table;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.util.Identifier;

public final class RitualTableEmiCategory {
    public static final Identifier ID = new Identifier(HexaliaMod.MODID, "ritual_table");
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(ID, EmiStack.of(ModItems.RITUAL_TABLE));

    private RitualTableEmiCategory() {
    }
}