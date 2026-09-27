package net.astralya.hexalia.worldgen.gen;

import net.astralya.hexalia.util.ModTags;
import net.astralya.hexalia.worldgen.ModPlacedFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.level.levelgen.GenerationStep;

public final class ModNaturalGeneration {

    private ModNaturalGeneration() {
    }

    public static void registerNaturalGeneration() {
        addVegetation();
        addTrees();
    }

    private static void addVegetation() {
        BiomeModifications.addFeature(
                BiomeSelectors.tag(ModTags.Biomes.HAS_SWAMP_VEGETATION),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.SPIRIT_BLOOM_PLACED
        );
        BiomeModifications.addFeature(
                BiomeSelectors.tag(ModTags.Biomes.HAS_SHROOMS),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.DREAMSHROOM_PLACED
        );
        BiomeModifications.addFeature(
                BiomeSelectors.tag(ModTags.Biomes.HAS_SIREN_KELP),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.SIREN_KELP_PLACED
        );
        BiomeModifications.addFeature(
                BiomeSelectors.tag(ModTags.Biomes.HAS_COOL_BIOME_VEGETATION),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.CHILLBERRY_PLACED
        );
        BiomeModifications.addFeature(
                BiomeSelectors.tag(ModTags.Biomes.HAS_DRY_BIOME_VEGETATION),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.WILD_SUNFIRE_TOMATO_PLACED
        );
        BiomeModifications.addFeature(
                BiomeSelectors.tag(ModTags.Biomes.HAS_FLORAL_VEGETATION),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.WILD_MANDRAKE_PLACED
        );
        BiomeModifications.addFeature(
                BiomeSelectors.tag(ModTags.Biomes.HAS_FLORAL_VEGETATION),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.CELESTIAL_BLOOM_PLACED
        );
        BiomeModifications.addFeature(
                BiomeSelectors.tag(ModTags.Biomes.HAS_SHADED_VEGETATION),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.GHOST_FERN_PLACED
        );
        BiomeModifications.addFeature(
                BiomeSelectors.tag(ModTags.Biomes.HAS_SWAMP_VEGETATION),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.LOTUS_FLOWER_PLACED
        );
        BiomeModifications.addFeature(
                BiomeSelectors.tag(ModTags.Biomes.HAS_DRY_BIOME_VEGETATION),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.SALTSPROUT_PLACED
        );
        BiomeModifications.addFeature(
                BiomeSelectors.tag(ModTags.Biomes.HAS_DECORATIVE_FLOWERS),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.BEGONIA_PLACED
        );
        BiomeModifications.addFeature(
                BiomeSelectors.tag(ModTags.Biomes.HAS_COOL_BIOME_VEGETATION),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.LAVENDER_PLACED
        );
        BiomeModifications.addFeature(
                BiomeSelectors.tag(ModTags.Biomes.HAS_FLORAL_VEGETATION),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.DAHLIA_PLACED
        );
        BiomeModifications.addFeature(
                BiomeSelectors.tag(ModTags.Biomes.HAS_FLORAL_VEGETATION),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.WITCHWEED_PLACED
        );
        BiomeModifications.addFeature(
                BiomeSelectors.tag(ModTags.Biomes.HAS_SHROOMS),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.PALE_MUSHROOM_PLACED
        );
        BiomeModifications.addFeature(
                BiomeSelectors.tag(ModTags.Biomes.HAS_SHADED_VEGETATION),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.NIGHTSHADE_BUSH_PLACED
        );
    }

    private static void addTrees() {
        BiomeModifications.addFeature(
                BiomeSelectors.tag(ModTags.Biomes.HAS_SHADED_VEGETATION),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.DARK_OAK_COCOON_PLACED
        );
        BiomeModifications.addFeature(
                BiomeSelectors.tag(ModTags.Biomes.HAS_SWAMP_VEGETATION),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.COTTONWOOD_PLACED
        );
        BiomeModifications.addFeature(
                BiomeSelectors.tag(ModTags.Biomes.HAS_SWAMP_VEGETATION),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.WILLOW_PLACED
        );
    }
}

