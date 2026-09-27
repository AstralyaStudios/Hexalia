package net.astralya.hexalia.entity;

import com.terraformersmc.terraform.boat.api.TerraformBoatType;
import com.terraformersmc.terraform.boat.api.TerraformBoatTypeRegistry;
import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class ModBoats {
    public static final ResourceLocation COTTONWOOD_BOAT_ID = new ResourceLocation(HexaliaMod.MODID, "cottonwood_boat");
    public static final ResourceLocation COTTONWOOD_CHEST_BOAT_ID = new ResourceLocation(HexaliaMod.MODID, "cottonwood_chest_boat");

    public static final ResourceLocation WILLOW_BOAT_ID = new ResourceLocation(HexaliaMod.MODID, "willow_boat");
    public static final ResourceLocation WILLOW_CHEST_BOAT_ID = new ResourceLocation(HexaliaMod.MODID, "willow_chest_boat");

    public static final ResourceKey<TerraformBoatType> COTTONWOOD_BOAT_KEY = TerraformBoatTypeRegistry.createKey(COTTONWOOD_BOAT_ID);
    public static final ResourceKey<TerraformBoatType> WILLOW_BOAT_KEY = TerraformBoatTypeRegistry.createKey(WILLOW_BOAT_ID);

    public static void registerBoats() {
        TerraformBoatType cottonwoodBoat = new TerraformBoatType.Builder()
                .item(ModItems.COTTONWOOD_BOAT.get())
                .chestItem(ModItems.COTTONWOOD_CHEST_BOAT.get())
                .planks(ModBlocks.COTTONWOOD_PLANKS.get().asItem())
                .build();

        TerraformBoatType willowBoat = new TerraformBoatType.Builder()
                .item(ModItems.WILLOW_BOAT.get())
                .chestItem(ModItems.WILLOW_CHEST_BOAT.get())
                .planks(ModBlocks.WILLOW_PLANKS.get().asItem())
                .build();

        Registry.register(TerraformBoatTypeRegistry.INSTANCE, COTTONWOOD_BOAT_KEY, cottonwoodBoat);
        Registry.register(TerraformBoatTypeRegistry.INSTANCE, WILLOW_BOAT_KEY, willowBoat);
    }
}


