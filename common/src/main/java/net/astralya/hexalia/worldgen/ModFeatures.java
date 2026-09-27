package net.astralya.hexalia.worldgen;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.worldgen.feature.WildCropConfiguration;
import net.astralya.hexalia.worldgen.feature.WildCropFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(HexaliaMod.MODID, Registries.FEATURE);

    public static final RegistrySupplier<WildCropFeature> WILD_CROP =
            FEATURES.register("wild_crop", () -> new WildCropFeature(WildCropConfiguration.CODEC));

    public static void register() {
        FEATURES.register();
    }
}
