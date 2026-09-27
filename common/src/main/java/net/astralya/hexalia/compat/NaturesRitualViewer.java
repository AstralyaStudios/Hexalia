package net.astralya.hexalia.compat;

import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.recipe.RitualTableRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;

public final class NaturesRitualViewer {
    public static final ResourceLocation SOUL_ICON = new ResourceLocation("hexalia", "textures/gui/category/soul_required.png");
    public static final ResourceLocation TEXTURE = new ResourceLocation("hexalia", "textures/gui/ritual_table_gui.png");
    public static final int WIDTH = 118;
    public static final int HEIGHT = 80;
    public static final int[] INPUT_X = {27, 3, 27, 51, 3, 51, 3, 27, 51};
    public static final int[] INPUT_Y = {30, 6, 6, 6, 30, 30, 54, 54, 54};
    public static final int OUTPUT_X = 88;
    public static final int OUTPUT_Y = 30;
    public static final int SOUL_X = 72;
    public static final int SOUL_Y = 4;
    public static final int SOUL_SIZE = 10;
    public static final String SOUL_TOOLTIP = "tooltip.hexalia.requires_soul";

    private NaturesRitualViewer() {}

    public static ItemStack result(RitualTableRecipe recipe) {
        if (!recipe.isEntityResult()) return recipe.getResultItem(null).copy();
        ResourceLocation id = recipe.entityResult();
        if (id != null && "hexalia".equals(id.getNamespace())) {
            if ("silk_moth".equals(id.getPath())) return new ItemStack(ModItems.SILK_MOTH_SPAWN_EGG.get());
            if ("cacofey".equals(id.getPath())) return new ItemStack(ModItems.CACOFEY_SPAWN_EGG.get());
        }
        EntityType<?> type = id == null ? null : BuiltInRegistries.ENTITY_TYPE.getOptional(id).orElse(null);
        SpawnEggItem egg = type == null ? null : SpawnEggItem.byId(type);
        return egg == null ? ItemStack.EMPTY : egg.getDefaultInstance();
    }
}
