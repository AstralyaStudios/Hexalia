package net.astralya.hexalia.worldgen;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;

import java.util.List;

public class ModPlacedFeatures {

    public static final ResourceKey<PlacedFeature> SPIRIT_BLOOM_PLACED = registerKey("spirit_bloom_placed");
    public static final ResourceKey<PlacedFeature> DREAMSHROOM_PLACED = registerKey("dreamshroom_placed");
    public static final ResourceKey<PlacedFeature> SIREN_KELP_PLACED = registerKey("siren_kelp_placed");
    public static final ResourceKey<PlacedFeature> CHILLBERRY_PLACED = registerKey("chillberry_placed");
    public static final ResourceKey<PlacedFeature> WILD_SUNFIRE_TOMATO_PLACED = registerKey("wild_sunfire_tomato_placed");
    public static final ResourceKey<PlacedFeature> WILD_MANDRAKE_PLACED = registerKey("wild_mandrake_placed");
    public static final ResourceKey<PlacedFeature> GHOST_FERN_PLACED = registerKey("ghost_fern_placed");
    public static final ResourceKey<PlacedFeature> CELESTIAL_BLOOM_PLACED = registerKey("celestial_bloom_placed");
    public static final ResourceKey<PlacedFeature> SALTSPROUT_PLACED = registerKey("saltsprout_placed");
    public static final ResourceKey<PlacedFeature> BEGONIA_PLACED = registerKey("begonia_placed");
    public static final ResourceKey<PlacedFeature> LAVENDER_PLACED = registerKey("lavender_placed");
    public static final ResourceKey<PlacedFeature> DAHLIA_PLACED = registerKey("dahlia_placed");
    public static final ResourceKey<PlacedFeature> LOTUS_FLOWER_PLACED = registerKey("lotus_flower_placed");
    public static final ResourceKey<PlacedFeature> PALE_MUSHROOM_PLACED = registerKey("pale_mushroom_placed");
    public static final ResourceKey<PlacedFeature> WITCHWEED_PLACED = registerKey("witchweed_placed");
    public static final ResourceKey<PlacedFeature> NIGHTSHADE_BUSH_PLACED = registerKey("nightshade_bush_placed");
    public static final ResourceKey<PlacedFeature> COTTONWOOD_PLACED = registerKey("cottonwood_placed");
    public static final ResourceKey<PlacedFeature> WILLOW_PLACED = registerKey("willow_placed");
    public static final ResourceKey<PlacedFeature> DARK_OAK_COCOON_PLACED = registerKey("dark_oak_cocoon_placed");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configured = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, SPIRIT_BLOOM_PLACED, configured.getOrThrow(ModConfiguredFeatures.SPIRIT_BLOOM), rarityPatch(4));
        register(context, DREAMSHROOM_PLACED, configured.getOrThrow(ModConfiguredFeatures.DREAMSHROOM), rarityPatch(4));
        register(context, SIREN_KELP_PLACED, configured.getOrThrow(ModConfiguredFeatures.SIREN_KELP), oceanFloorPatch(4));
        register(context, GHOST_FERN_PLACED, configured.getOrThrow(ModConfiguredFeatures.GHOST_FERN), rarityPatch(4));
        register(context, CELESTIAL_BLOOM_PLACED, configured.getOrThrow(ModConfiguredFeatures.CELESTIAL_BLOOM), rarityPatch(4));
        register(context, WITCHWEED_PLACED, configured.getOrThrow(ModConfiguredFeatures.WITCHWEED), rarityPatch(4));
        register(context, CHILLBERRY_PLACED, configured.getOrThrow(ModConfiguredFeatures.CHILLBERRY), rarityPatch(6));
        register(context, BEGONIA_PLACED, configured.getOrThrow(ModConfiguredFeatures.BEGONIA), rarityPatch(6));
        register(context, LAVENDER_PLACED, configured.getOrThrow(ModConfiguredFeatures.LAVENDER), rarityPatch(6));
        register(context, DAHLIA_PLACED, configured.getOrThrow(ModConfiguredFeatures.DAHLIA), rarityPatch(6));
        register(context, PALE_MUSHROOM_PLACED, configured.getOrThrow(ModConfiguredFeatures.PALE_MUSHROOM), rarityPatch(6));
        register(context, WILD_SUNFIRE_TOMATO_PLACED, configured.getOrThrow(ModConfiguredFeatures.WILD_SUNFIRE_TOMATO), rarityPatch(8));
        register(context, NIGHTSHADE_BUSH_PLACED, configured.getOrThrow(ModConfiguredFeatures.NIGHTSHADE_BUSH), rarityPatch(8));
        register(context, WILD_MANDRAKE_PLACED, configured.getOrThrow(ModConfiguredFeatures.WILD_MANDRAKE), rarityPatch(10));
        register(context, SALTSPROUT_PLACED, configured.getOrThrow(ModConfiguredFeatures.SALTSPROUT), rarityPatch(20));
        register(context, LOTUS_FLOWER_PLACED, configured.getOrThrow(ModConfiguredFeatures.LOTUS_FLOWER), rarityPatch(30));

        register(context, COTTONWOOD_PLACED,
                configured.getOrThrow(ModConfiguredFeatures.COTTONWOOD),
                rareTreePlacement(ModBlocks.COTTONWOOD_SAPLING.get(), 25));

        register(context, WILLOW_PLACED,
                configured.getOrThrow(ModConfiguredFeatures.WILLOW),
                rareTreePlacement(ModBlocks.WILLOW_SAPLING.get(), 25));

        register(context, DARK_OAK_COCOON_PLACED,
                configured.getOrThrow(ModConfiguredFeatures.DARK_OAK_COCOON),
                rareTreePlacement(Blocks.DARK_OAK_SAPLING, 25));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(HexaliaMod.MODID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration, List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

    private static List<PlacementModifier> rarityPatch(int rarity) {
        return List.of(
                RarityFilter.onAverageOnceEvery(rarity),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome()
        );
    }

    private static List<PlacementModifier> oceanFloorPatch(int rarity) {
        return List.of(
                RarityFilter.onAverageOnceEvery(rarity),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                BiomeFilter.biome()
        );
    }

    private static List<PlacementModifier> rareTreePlacement(Block sapling, int rarity) {
        return List.of(
                RarityFilter.onAverageOnceEvery(rarity),
                InSquarePlacement.spread(),
                SurfaceWaterDepthFilter.forMaxDepth(0),
                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                BiomeFilter.biome(),
                BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(sapling.defaultBlockState(), BlockPos.ZERO))
        );
    }
}
