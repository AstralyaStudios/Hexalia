package net.astralya.hexalia.util.forge;

import net.astralya.hexalia.HexaliaMod;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public final class ModWoodTypesImpl {
    public static WoodType create(String name) {
        return WoodType.register(new WoodType(HexaliaMod.MODID + ":" + name, BlockSetType.OAK));
    }
}

