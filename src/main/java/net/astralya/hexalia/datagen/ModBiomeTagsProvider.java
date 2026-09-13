package net.astralya.hexalia.datagen;

import java.util.concurrent.CompletableFuture;
import net.astralya.hexalia.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public class ModBiomeTagsProvider extends FabricTagProvider<Biome> {

    public ModBiomeTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.BIOME, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries) {
        getOrCreateTagBuilder(ModTags.Biomes.HAS_SHROOMS)
                .add(BiomeKeys.OLD_GROWTH_PINE_TAIGA)
                .add(BiomeKeys.MUSHROOM_FIELDS)
                .addOptionalTag(new Identifier("c", "mushroom"));

        getOrCreateTagBuilder(ModTags.Biomes.HAS_SIREN_KELP)
                .add(BiomeKeys.OCEAN)
                .add(BiomeKeys.DEEP_OCEAN)
                .add(BiomeKeys.COLD_OCEAN)
                .add(BiomeKeys.DEEP_COLD_OCEAN)
                .add(BiomeKeys.LUKEWARM_OCEAN)
                .add(BiomeKeys.DEEP_LUKEWARM_OCEAN)
                .add(BiomeKeys.WARM_OCEAN)
                .add(BiomeKeys.FROZEN_OCEAN)
                .add(BiomeKeys.DEEP_FROZEN_OCEAN);

        getOrCreateTagBuilder(ModTags.Biomes.HAS_DECORATIVE_FLOWERS)
                .add(BiomeKeys.SUNFLOWER_PLAINS)
                .add(BiomeKeys.PLAINS)
                .addOptionalTag(new Identifier("c", "plains"));

        getOrCreateTagBuilder(ModTags.Biomes.HAS_SWAMP_VEGETATION)
                .add(BiomeKeys.MANGROVE_SWAMP)
                .add(BiomeKeys.SWAMP)
                .addOptionalTag(new Identifier("c", "swamp"));

        getOrCreateTagBuilder(ModTags.Biomes.HAS_FLORAL_VEGETATION)
                .add(BiomeKeys.MEADOW)
                .add(BiomeKeys.OLD_GROWTH_BIRCH_FOREST)
                .add(BiomeKeys.BIRCH_FOREST)
                .add(BiomeKeys.FLOWER_FOREST)
                .addOptionalTag(new Identifier("c", "birch_forest"))
                .addOptionalTag(new Identifier("c", "flower_forests"));

        getOrCreateTagBuilder(ModTags.Biomes.HAS_SHADED_VEGETATION)
                .add(BiomeKeys.DARK_FOREST);

        getOrCreateTagBuilder(ModTags.Biomes.HAS_COOL_BIOME_VEGETATION)
                .add(BiomeKeys.TAIGA)
                .add(BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA)
                .add(BiomeKeys.SNOWY_TAIGA)
                .add(BiomeKeys.SNOWY_PLAINS);

        getOrCreateTagBuilder(ModTags.Biomes.HAS_DRY_BIOME_VEGETATION)
                .add(BiomeKeys.DESERT)
                .add(BiomeKeys.BADLANDS)
                .add(BiomeKeys.WINDSWEPT_SAVANNA)
                .add(BiomeKeys.SAVANNA)
                .addOptionalTag(new Identifier("c", "desert"));

        getOrCreateTagBuilder(ModTags.Biomes.SILK_MOTH_SPAWNS)
                .add(BiomeKeys.FOREST)
                .add(BiomeKeys.FLOWER_FOREST)
                .add(BiomeKeys.BIRCH_FOREST)
                .add(BiomeKeys.OLD_GROWTH_BIRCH_FOREST)
                .add(BiomeKeys.DARK_FOREST)
                .add(BiomeKeys.GROVE);

        getOrCreateTagBuilder(ModTags.Biomes.CACOFEY_SPAWNS)
                .add(BiomeKeys.JUNGLE)
                .add(BiomeKeys.BAMBOO_JUNGLE)
                .add(BiomeKeys.SPARSE_JUNGLE);
    }
}
