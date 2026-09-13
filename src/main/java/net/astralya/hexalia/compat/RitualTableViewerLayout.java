package net.astralya.hexalia.compat;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.item.custom.CustomModelSpawnEggItem;
import net.astralya.hexalia.recipe.RitualTableRecipe;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public final class RitualTableViewerLayout {
    public static final int WIDTH = 118;
    public static final int HEIGHT = 80;
    public static final int[][] INPUTS = {
            {27, 30}, {3, 6}, {27, 6}, {51, 6}, {3, 30},
            {51, 30}, {3, 54}, {27, 54}, {51, 54}
    };
    public static final int OUTPUT_X = 88;
    public static final int OUTPUT_Y = 30;
    public static final Identifier SOUL_TEXTURE =
            new Identifier(HexaliaMod.MODID, "textures/gui/category/soul_required.png");
    public static final int SOUL_X = 72;
    public static final int SOUL_Y = 4;
    public static final int SOUL_SIZE = 10;
    public static final String SOUL_TOOLTIP = "tooltip.hexalia.requires_soul";

    private RitualTableViewerLayout() {}

    public static ItemStack displayResult(RitualTableRecipe recipe) {
        ItemStack itemResult = recipe.itemResult();
        if (!itemResult.isEmpty()) return itemResult;
        EntityType<?> type = recipe.entityResult()
                .flatMap(result -> Registries.ENTITY_TYPE.getOrEmpty(result.entityId()))
                .orElse(null);
        if (type == null) return new ItemStack(Items.BARRIER);
        SpawnEggItem vanillaEgg = SpawnEggItem.forEntity(type);
        if (vanillaEgg != null) return vanillaEgg.getDefaultStack();
        for (Item item : Registries.ITEM) {
            ItemStack stack = new ItemStack(item);
            if (item instanceof CustomModelSpawnEggItem egg && egg.spawnsEntity(stack, type)) return stack;
        }
        return new ItemStack(Items.BARRIER);
    }
}
