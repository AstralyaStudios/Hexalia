package net.astralya.hexalia.neoforge.datagen;

import java.util.concurrent.CompletableFuture;
import net.astralya.hexalia.Hexalia;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBiomeTagsProvider extends BiomeTagsProvider {
  public ModBiomeTagsProvider(
      PackOutput output,
      CompletableFuture<HolderLookup.Provider> lookupProvider,
      ExistingFileHelper existingFileHelper) {
    super(output, lookupProvider, Hexalia.MOD_ID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    tag(ModTags.Biomes.HAS_FLORAL_VEGETATION)
        .add(Biomes.MEADOW)
        .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
        .add(Biomes.BIRCH_FOREST)
        .add(Biomes.FLOWER_FOREST)
        .addOptionalTag(ResourceLocation.fromNamespaceAndPath("c", "is_birch_forest"))
        .addOptionalTag(ResourceLocation.fromNamespaceAndPath("c", "is_flower_forest"));

    tag(ModTags.Biomes.HAS_COOL_BIOME_VEGETATION)
        .add(Biomes.TAIGA)
        .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
        .add(Biomes.SNOWY_TAIGA)
        .add(Biomes.SNOWY_PLAINS);

    tag(ModTags.Biomes.HAS_DRY_BIOME_VEGETATION)
        .add(Biomes.DESERT)
        .add(Biomes.BADLANDS)
        .add(Biomes.WINDSWEPT_SAVANNA)
        .add(Biomes.SAVANNA)
        .addOptionalTag(ResourceLocation.fromNamespaceAndPath("c", "is_desert"));

    tag(ModTags.Biomes.HAS_SHROOMS)
        .add(Biomes.OLD_GROWTH_PINE_TAIGA)
        .add(Biomes.MUSHROOM_FIELDS)
        .addOptionalTag(ResourceLocation.fromNamespaceAndPath("c", "is_mushroom"));

    tag(ModTags.Biomes.HAS_SIREN_KELP).addTag(BiomeTags.IS_OCEAN);

    tag(ModTags.Biomes.HAS_SWAMP_VEGETATION)
        .add(Biomes.MANGROVE_SWAMP)
        .add(Biomes.SWAMP)
        .addOptionalTag(ResourceLocation.fromNamespaceAndPath("c", "is_swamp"));

    tag(ModTags.Biomes.HAS_SHADED_VEGETATION).add(Biomes.DARK_FOREST);

    tag(ModTags.Biomes.HAS_DECORATIVE_FLOWERS)
        .add(Biomes.SUNFLOWER_PLAINS)
        .add(Biomes.PLAINS)
        .addOptionalTag(ResourceLocation.fromNamespaceAndPath("c", "is_plains"));

    tag(ModTags.Biomes.SILK_MOTH_SPAWNS).addTag(BiomeTags.IS_FOREST);

    tag(ModTags.Biomes.CACOFEY_SPAWNS).addTag(BiomeTags.IS_JUNGLE);
  }
}
