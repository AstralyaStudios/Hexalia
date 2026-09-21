package net.astralya.hexalia.compat.emi.small_cauldron;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.util.Identifier;

public final class SmallCauldronEmiCategory {
    public static final Identifier ID = new Identifier(HexaliaMod.MODID, "small_cauldron");
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(ID, EmiStack.of(ModItems.SMALL_CAULDRON));

    private SmallCauldronEmiCategory() {
    }
}