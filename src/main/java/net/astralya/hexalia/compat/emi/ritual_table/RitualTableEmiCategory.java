package net.astralya.hexalia.compat.emi.ritual_table;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.ModBlocks;
import net.minecraft.resources.ResourceLocation;

public final class RitualTableEmiCategory {
    public static final ResourceLocation ID = new ResourceLocation(HexaliaMod.MODID, "ritual_table");
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(ID, EmiStack.of(ModBlocks.RITUAL_TABLE.get()));

    private RitualTableEmiCategory() {
    }
}