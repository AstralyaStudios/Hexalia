package net.astralya.hexalia.util.forge;

import net.minecraft.world.level.biome.Biome;

public final class SunlightPlatformImpl {
    private SunlightPlatformImpl() {}

    public static float getDownfall(Biome biome) {
        return biome.getModifiedClimateSettings().downfall();
    }
}

