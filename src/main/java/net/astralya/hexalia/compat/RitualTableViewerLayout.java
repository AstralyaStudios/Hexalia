package net.astralya.hexalia.compat;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.item.custom.CustomModelSpawnEggItem;
import net.astralya.hexalia.recipe.RitualTableRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;

public final class RitualTableViewerLayout {
    public static final int WIDTH = 118;
    public static final int HEIGHT = 80;
    public static final int[][] INPUTS = {
            {27, 30}, {3, 6}, {27, 6}, {51, 6}, {3, 30},
            {51, 30}, {3, 54}, {27, 54}, {51, 54}
    };
    public static final int OUTPUT_X = 88;
    public static final int OUTPUT_Y = 30;
    public static final ResourceLocation SOUL_TEXTURE =
            new ResourceLocation(HexaliaMod.MODID, "textures/gui/category/soul_required.png");
    public static final int SOUL_X = 72;
    public static final int SOUL_Y = 4;
    public static final int SOUL_SIZE = 10;
    public static final String SOUL_TOOLTIP = "tooltip.hexalia.requires_soul";

    private RitualTableViewerLayout() {}

    public static ItemStack displayResult(RitualTableRecipe recipe) {
        ItemStack itemResult = recipe.itemResult();
        if (!itemResult.isEmpty()) return itemResult;
        EntityType<?> type = recipe.entityResult()
                .flatMap(result -> BuiltInRegistries.ENTITY_TYPE.getOptional(result.entityId()))
                .orElse(null);
        if (type == null) return new ItemStack(Items.BARRIER);
        SpawnEggItem vanillaEgg = SpawnEggItem.byId(type);
        if (vanillaEgg != null) return vanillaEgg.getDefaultInstance();
        for (Item item : BuiltInRegistries.ITEM) {
            ItemStack stack = new ItemStack(item);
            if (item instanceof CustomModelSpawnEggItem egg && egg.spawnsEntity(stack, type)) return stack;
        }
        return new ItemStack(Items.BARRIER);
    }
}
