package net.astralya.hexalia.datagen;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagsProvider extends BiomeTagsProvider {
    public ModBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, HexaliaMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        // Vegetation
        this.tag(ModTags.Biomes.HAS_SHROOMS)
                .add(Biomes.OLD_GROWTH_PINE_TAIGA)
                .add(Biomes.MUSHROOM_FIELDS)
                .addOptionalTag(new ResourceLocation("forge", "is_mushroom"));

        this.tag(ModTags.Biomes.HAS_SIREN_KELP)
                .addTag(BiomeTags.IS_OCEAN);

        this.tag(ModTags.Biomes.HAS_DECORATIVE_FLOWERS)
                .add(Biomes.SUNFLOWER_PLAINS)
                .add(Biomes.PLAINS);

        this.tag(ModTags.Biomes.HAS_SWAMP_VEGETATION)
                .add(Biomes.MANGROVE_SWAMP)
                .add(Biomes.SWAMP)
                .addOptionalTag(new ResourceLocation("forge", "is_swamp"));

        this.tag(ModTags.Biomes.HAS_FLORAL_VEGETATION)
                .add(Biomes.MEADOW)
                .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
                .add(Biomes.BIRCH_FOREST)
                .add(Biomes.FLOWER_FOREST);

        this.tag(ModTags.Biomes.HAS_SHADED_VEGETATION)
                .add(Biomes.DARK_FOREST);

        this.tag(ModTags.Biomes.HAS_COOL_BIOME_VEGETATION)
                .add(Biomes.TAIGA)
                .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
                .add(Biomes.SNOWY_TAIGA)
                .add(Biomes.SNOWY_PLAINS);

        this.tag(ModTags.Biomes.HAS_DRY_BIOME_VEGETATION)
                .add(Biomes.DESERT)
                .add(Biomes.BADLANDS)
                .add(Biomes.WINDSWEPT_SAVANNA)
                .add(Biomes.SAVANNA)
                .addOptionalTag(new ResourceLocation("forge", "is_desert"));

        // Entities
        this.tag(ModTags.Biomes.SILK_MOTH_SPAWNS)
                .addTag(BiomeTags.IS_FOREST);
        this.tag(ModTags.Biomes.CACOFEY_SPAWNS)
                .addTag(BiomeTags.IS_JUNGLE);
    }
}
