package net.astralya.hexalia.util.fabric;

import net.astralya.hexalia.HexaliaMod;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public final class ModWoodTypesImpl {
    public static WoodType create(String name) {
        return WoodTypeBuilder.copyOf(WoodType.OAK)
                .register(new ResourceLocation(HexaliaMod.MODID, name), BlockSetType.OAK);
    }
}
