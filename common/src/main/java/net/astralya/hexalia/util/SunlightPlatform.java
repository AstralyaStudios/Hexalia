package net.astralya.hexalia.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.biome.Biome;

public final class SunlightPlatform {
    private SunlightPlatform() {}

    @ExpectPlatform
    public static float getDownfall(Biome biome) {
        throw new AssertionError();
    }
}
