package net.astralya.hexalia.neoforge.datagen;

import net.astralya.hexalia.Hexalia;
import net.astralya.hexalia.entity.ModEntities;
import net.astralya.hexalia.util.ModTags;
import net.astralya.hexalia.worldgen.ModPlacedFeatures;
import java.util.List;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModBiomeModifier {
  public static final ResourceKey<BiomeModifier> SPAWN_SILK_MOTH = registerKey("spawn_silk_moth");
  public static final ResourceKey<BiomeModifier> SPAWN_CACOFEY = registerKey("spawn_cacofey");

  public static final ResourceKey<BiomeModifier> ADD_CHILLBERRY = registerKey("add_chillberry");
  public static final ResourceKey<BiomeModifier> ADD_WILD_SUNFIRE_TOMATO =
      registerKey("add_wild_sunfire_tomato");
  public static final ResourceKey<BiomeModifier> ADD_WILD_MANDRAKE =
      registerKey("add_wild_mandrake");
  public static final ResourceKey<BiomeModifier> ADD_SPIRIT_BLOOM =
      registerKey("add_spirit_bloom");
  public static final ResourceKey<BiomeModifier> ADD_DREAMSHROOM = registerKey("add_dreamshroom");
  public static final ResourceKey<BiomeModifier> ADD_SIREN_KELP = registerKey("add_siren_kelp");
  public static final ResourceKey<BiomeModifier> ADD_GHOST_FERN = registerKey("add_ghost_fern");
  public static final ResourceKey<BiomeModifier> ADD_CELESTIAL_BLOOM =
      registerKey("add_celestial_bloom");
  public static final ResourceKey<BiomeModifier> ADD_SALTSPROUT = registerKey("add_saltsprout");
  public static final ResourceKey<BiomeModifier> ADD_LOTUS_FLOWER =
      registerKey("add_lotus_flower");
  public static final ResourceKey<BiomeModifier> ADD_WITCHWEED = registerKey("add_witchweed");
  public static final ResourceKey<BiomeModifier> ADD_PALE_MUSHROOM =
      registerKey("add_pale_mushroom");
  public static final ResourceKey<BiomeModifier> ADD_NIGHTSHADE = registerKey("add_nightshade");
  public static final ResourceKey<BiomeModifier> ADD_BEGONIA = registerKey("add_begonia");
  public static final ResourceKey<BiomeModifier> ADD_LAVENDER = registerKey("add_lavender");
  public static final ResourceKey<BiomeModifier> ADD_DAHLIA = registerKey("add_dahlia");
  public static final ResourceKey<BiomeModifier> ADD_DARK_OAK_COCOON =
      registerKey("add_dark_oak_cocoon");
  public static final ResourceKey<BiomeModifier> ADD_WILLOW = registerKey("add_willow");
  public static final ResourceKey<BiomeModifier> ADD_COTTONWOOD = registerKey("add_cottonwood");

  private ModBiomeModifier() {}

  public static void bootstrap(BootstrapContext<BiomeModifier> context) {
    var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
    var biomes = context.lookup(Registries.BIOME);

    context.register(
        SPAWN_SILK_MOTH,
        new BiomeModifiers.AddSpawnsBiomeModifier(
            biomes.getOrThrow(ModTags.Biomes.SILK_MOTH_SPAWNS),
            List.of(new MobSpawnSettings.SpawnerData(ModEntities.SILK_MOTH.get(), 6, 1, 2))));

    context.register(
        SPAWN_CACOFEY,
        new BiomeModifiers.AddSpawnsBiomeModifier(
            biomes.getOrThrow(ModTags.Biomes.CACOFEY_SPAWNS),
            List.of(new MobSpawnSettings.SpawnerData(ModEntities.CACOFEY.get(), 8, 1, 2))));

    context.register(
        ADD_CHILLBERRY,
        new BiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(ModTags.Biomes.HAS_COOL_BIOME_VEGETATION),
            HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.CHILLBERRY_PLACED)),
            GenerationStep.Decoration.VEGETAL_DECORATION));

    context.register(
        ADD_WILD_SUNFIRE_TOMATO,
        new BiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(ModTags.Biomes.HAS_DRY_BIOME_VEGETATION),
            HolderSet.direct(
                placedFeatures.getOrThrow(ModPlacedFeatures.WILD_SUNFIRE_TOMATO_PLACED)),
            GenerationStep.Decoration.VEGETAL_DECORATION));

    context.register(
        ADD_WILD_MANDRAKE,
        new BiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(ModTags.Biomes.HAS_FLORAL_VEGETATION),
            HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.WILD_MANDRAKE_PLACED)),
            GenerationStep.Decoration.VEGETAL_DECORATION));

    context.register(
        ADD_SPIRIT_BLOOM,
        new BiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(ModTags.Biomes.HAS_SWAMP_VEGETATION),
            HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.SPIRIT_BLOOM_PLACED)),
            GenerationStep.Decoration.VEGETAL_DECORATION));

    context.register(
        ADD_DREAMSHROOM,
        new BiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(ModTags.Biomes.HAS_SHROOMS),
            HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.DREAMSHROOM_PLACED)),
            GenerationStep.Decoration.VEGETAL_DECORATION));

    context.register(
        ADD_SIREN_KELP,
        new BiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(ModTags.Biomes.HAS_SIREN_KELP),
            HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.SIREN_KELP_PLACED)),
            GenerationStep.Decoration.VEGETAL_DECORATION));

    context.register(
        ADD_CELESTIAL_BLOOM,
        new BiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(ModTags.Biomes.HAS_FLORAL_VEGETATION),
            HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.CELESTIAL_BLOOM_PLACED)),
            GenerationStep.Decoration.VEGETAL_DECORATION));

    context.register(
        ADD_GHOST_FERN,
        new BiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(ModTags.Biomes.HAS_SHADED_VEGETATION),
            HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.GHOST_FERN_PLACED)),
            GenerationStep.Decoration.VEGETAL_DECORATION));

    context.register(
        ADD_SALTSPROUT,
        new BiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(ModTags.Biomes.HAS_DRY_BIOME_VEGETATION),
            HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.SALTSPROUT_PLACED)),
            GenerationStep.Decoration.VEGETAL_DECORATION));

    context.register(
        ADD_LOTUS_FLOWER,
        new BiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(ModTags.Biomes.HAS_SWAMP_VEGETATION),
            HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.LOTUS_FLOWER_PLACED)),
            GenerationStep.Decoration.VEGETAL_DECORATION));

    context.register(
        ADD_BEGONIA,
        new BiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(ModTags.Biomes.HAS_DECORATIVE_FLOWERS),
            HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.BEGONIA_PLACED)),
            GenerationStep.Decoration.VEGETAL_DECORATION));

    context.register(
        ADD_LAVENDER,
        new BiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(ModTags.Biomes.HAS_COOL_BIOME_VEGETATION),
            HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.LAVENDER_PLACED)),
            GenerationStep.Decoration.VEGETAL_DECORATION));

    context.register(
        ADD_DAHLIA,
        new BiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(ModTags.Biomes.HAS_FLORAL_VEGETATION),
            HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.DAHLIA_PLACED)),
            GenerationStep.Decoration.VEGETAL_DECORATION));

    context.register(
        ADD_WITCHWEED,
        new BiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(ModTags.Biomes.HAS_FLORAL_VEGETATION),
            HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.WITCHWEED_PLACED)),
            GenerationStep.Decoration.VEGETAL_DECORATION));

    context.register(
        ADD_PALE_MUSHROOM,
        new BiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(ModTags.Biomes.HAS_SHROOMS),
            HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.PALE_MUSHROOM_PLACED)),
            GenerationStep.Decoration.VEGETAL_DECORATION));

    context.register(
        ADD_NIGHTSHADE,
        new BiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(ModTags.Biomes.HAS_SHADED_VEGETATION),
            HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.NIGHTSHADE_BUSH_PLACED)),
            GenerationStep.Decoration.VEGETAL_DECORATION));

    context.register(
        ADD_DARK_OAK_COCOON,
        new BiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(ModTags.Biomes.HAS_SHADED_VEGETATION),
            HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.DARK_OAK_COCOON_PLACED)),
            GenerationStep.Decoration.VEGETAL_DECORATION));

    context.register(
        ADD_COTTONWOOD,
        new BiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(ModTags.Biomes.HAS_SWAMP_VEGETATION),
            HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.COTTONWOOD_PLACED)),
            GenerationStep.Decoration.VEGETAL_DECORATION));

    context.register(
        ADD_WILLOW,
        new BiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(ModTags.Biomes.HAS_SWAMP_VEGETATION),
            HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.WILLOW_PLACED)),
            GenerationStep.Decoration.VEGETAL_DECORATION));
  }

  private static ResourceKey<BiomeModifier> registerKey(String name) {
    return ResourceKey.create(
        NeoForgeRegistries.Keys.BIOME_MODIFIERS,
        ResourceLocation.fromNamespaceAndPath(Hexalia.MOD_ID, name));
  }
}
