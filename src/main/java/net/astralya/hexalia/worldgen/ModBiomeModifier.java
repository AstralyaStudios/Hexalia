package net.astralya.hexalia.worldgen;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.entity.ModEntities;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.core.HolderSet;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public final class ModBiomeModifier {

    public static final ResourceKey<BiomeModifier> SPAWN_SILK_MOTH = registerKey("spawn_silk_moth");
    public static final ResourceKey<BiomeModifier> SPAWN_CACOFEY = registerKey("spawn_cacofey");

    public static final ResourceKey<BiomeModifier> ADD_SPIRIT_BLOOM = registerKey("add_spirit_bloom");
    public static final ResourceKey<BiomeModifier> ADD_DREAMSHROOM = registerKey("add_dreamshroom");
    public static final ResourceKey<BiomeModifier> ADD_SIREN_KELP = registerKey("add_siren_kelp");
    public static final ResourceKey<BiomeModifier> ADD_GHOST_FERN = registerKey("add_ghost_fern");
    public static final ResourceKey<BiomeModifier> ADD_CELESTIAL_BLOOM = registerKey("add_celestial_bloom");
    public static final ResourceKey<BiomeModifier> ADD_LOTUS_FLOWER = registerKey("add_lotus_flower");
    public static final ResourceKey<BiomeModifier> ADD_WITCHWEED = registerKey("add_witchweed");
    public static final ResourceKey<BiomeModifier> ADD_PALE_MUSHROOM = registerKey("add_pale_mushroom");
    public static final ResourceKey<BiomeModifier> ADD_NIGHTSHADE = registerKey("add_nightshade");
    public static final ResourceKey<BiomeModifier> ADD_SALTSPROUT = registerKey("add_saltsprout");

    public static final ResourceKey<BiomeModifier> ADD_CHILLBERRY = registerKey("add_chillberry");
    public static final ResourceKey<BiomeModifier> ADD_WILD_SUNFIRE_TOMATO = registerKey("add_wild_sunfire_tomato");
    public static final ResourceKey<BiomeModifier> ADD_WILD_MANDRAKE = registerKey("add_wild_mandrake");

    public static final ResourceKey<BiomeModifier> ADD_DARK_OAK_COCOON = registerKey("add_dark_oak_cocoon");
    public static final ResourceKey<BiomeModifier> ADD_WILLOW = registerKey("add_willow");
    public static final ResourceKey<BiomeModifier> ADD_COTTONWOOD = registerKey("add_cottonwood");

    public static final ResourceKey<BiomeModifier> ADD_BEGONIA = registerKey("add_begonia");
    public static final ResourceKey<BiomeModifier> ADD_LAVENDER = registerKey("add_lavender");
    public static final ResourceKey<BiomeModifier> ADD_DAHLIA = registerKey("add_dahlia");

    private ModBiomeModifier() {
    }

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<net.minecraft.world.level.biome.Biome> biomes = context.lookup(Registries.BIOME);

        context.register(
                SPAWN_SILK_MOTH,
                new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                        biomes.getOrThrow(ModTags.Biomes.SILK_MOTH_SPAWNS),
                        List.of(new MobSpawnSettings.SpawnerData(ModEntities.SILK_MOTH_ENTITY.get(), 6, 1, 2))
                )
        );

        context.register(
                SPAWN_CACOFEY,
                new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                        biomes.getOrThrow(ModTags.Biomes.CACOFEY_SPAWNS),
                        List.of(new MobSpawnSettings.SpawnerData(ModEntities.CACOFEY_ENTITY.get(), 8, 1, 2))
                )
        );

        context.register(ADD_SPIRIT_BLOOM, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.HAS_SWAMP_VEGETATION),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.SPIRIT_BLOOM_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_DREAMSHROOM, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.HAS_SHROOMS),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.DREAMSHROOM_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_SIREN_KELP, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.HAS_SIREN_KELP),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.SIREN_KELP_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_CHILLBERRY, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.HAS_COOL_BIOME_VEGETATION),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.CHILLBERRY_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_WILD_SUNFIRE_TOMATO, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.HAS_DRY_BIOME_VEGETATION),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.WILD_SUNFIRE_TOMATO_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_WILD_MANDRAKE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.HAS_FLORAL_VEGETATION),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.WILD_MANDRAKE_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_CELESTIAL_BLOOM, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.HAS_FLORAL_VEGETATION),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.CELESTIAL_BLOOM_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_GHOST_FERN, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.HAS_SHADED_VEGETATION),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.GHOST_FERN_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_LOTUS_FLOWER, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.HAS_SWAMP_VEGETATION),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.LOTUS_FLOWER_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_SALTSPROUT, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.HAS_DRY_BIOME_VEGETATION),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.SALTSPROUT_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_BEGONIA, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.HAS_DECORATIVE_FLOWERS),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.BEGONIA_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_LAVENDER, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.HAS_COOL_BIOME_VEGETATION),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.LAVENDER_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_DAHLIA, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.HAS_FLORAL_VEGETATION),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.DAHLIA_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_WITCHWEED, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.HAS_FLORAL_VEGETATION),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.WITCHWEED_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_PALE_MUSHROOM, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.HAS_SHROOMS),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.PALE_MUSHROOM_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_NIGHTSHADE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.HAS_SHADED_VEGETATION),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.NIGHTSHADE_BUSH_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_DARK_OAK_COCOON, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.HAS_SHADED_VEGETATION),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.DARK_OAK_COCOON_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_COTTONWOOD, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.HAS_SWAMP_VEGETATION),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.COTTONWOOD_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_WILLOW, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.HAS_SWAMP_VEGETATION),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.WILLOW_PLACED)),
                GenerationStep.Decoration.VEGETAL_DECORATION));
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(HexaliaMod.MODID, name));
    }
}
