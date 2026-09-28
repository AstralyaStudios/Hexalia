package net.astralya.hexalia.worldgen;

import java.util.List;
import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.block.custom.ChillberryBushBlock;
import net.astralya.hexalia.block.custom.SaltsproutBlock;
import net.astralya.hexalia.worldgen.feature.WildCropConfiguration;
import net.astralya.hexalia.worldgen.gen.decorator.CatkinTreeDecorator;
import net.astralya.hexalia.worldgen.gen.decorator.CocoonTreeDecorator;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.DarkOakFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

public class ModConfiguredFeatures {
  public static final ResourceKey<ConfiguredFeature<?, ?>> SPIRIT_BLOOM =
      registerKey("spirit_bloom");
  public static final ResourceKey<ConfiguredFeature<?, ?>> DREAMSHROOM = registerKey("dreamshroom");
  public static final ResourceKey<ConfiguredFeature<?, ?>> SIREN_KELP = registerKey("siren_kelp");
  public static final ResourceKey<ConfiguredFeature<?, ?>> CHILLBERRY = registerKey("chillberry");
  public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_SUNFIRE_TOMATO =
      registerKey("wild_sunfire_tomato");
  public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_MANDRAKE =
      registerKey("wild_mandrake");
  public static final ResourceKey<ConfiguredFeature<?, ?>> BEGONIA = registerKey("begonia");
  public static final ResourceKey<ConfiguredFeature<?, ?>> LAVENDER = registerKey("lavender");
  public static final ResourceKey<ConfiguredFeature<?, ?>> DAHLIA = registerKey("dahlia");
  public static final ResourceKey<ConfiguredFeature<?, ?>> CELESTIAL_BLOOM =
      registerKey("celestial_bloom");
  public static final ResourceKey<ConfiguredFeature<?, ?>> GHOST_FERN = registerKey("ghost_fern");
  public static final ResourceKey<ConfiguredFeature<?, ?>> SALTSPROUT = registerKey("saltsprout");
  public static final ResourceKey<ConfiguredFeature<?, ?>> COTTONWOOD = registerKey("cottonwood");
  public static final ResourceKey<ConfiguredFeature<?, ?>> COTTONWOOD_COCOON =
      registerKey("cottonwood_cocoon");
  public static final ResourceKey<ConfiguredFeature<?, ?>> WILLOW = registerKey("willow");
  public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_OAK_PLAIN =
      registerKey("dark_oak_plain");
  public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_OAK_COCOON =
      registerKey("dark_oak_cocoon");
  public static final ResourceKey<ConfiguredFeature<?, ?>> LOTUS_FLOWER =
      registerKey("lotus_flower");
  public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MUSHROOM =
      registerKey("pale_mushroom");
  public static final ResourceKey<ConfiguredFeature<?, ?>> WITCHWEED = registerKey("witchweed");
  public static final ResourceKey<ConfiguredFeature<?, ?>> NIGHTSHADE_BUSH =
      registerKey("nightshade_bush");

  public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
    registerFunctionalPlants(context);
    registerDecorativePlants(context);
    registerTrees(context);
  }

  private static void registerFunctionalPlants(BootstapContext<ConfiguredFeature<?, ?>> context) {
    register(
        context,
        SPIRIT_BLOOM,
        ModFeatures.WILD_CROP.get(),
        wildCropConfig(ModBlocks.SPIRIT_BLOOM.get(), 2, 3, 1, onDirt()));
    register(
        context,
        DREAMSHROOM,
        ModFeatures.WILD_CROP.get(),
        wildCropConfig(ModBlocks.DREAMSHROOM.get(), 2, 3, 1, onDirtOrMycelium()));
    register(
        context,
        SIREN_KELP,
        ModFeatures.WILD_CROP.get(),
        WildCropConfiguration.forWater(
            BlockStateProvider.simple(ModBlocks.SIREN_KELP.get()), 2, 3, 1, onOceanFloor()));
    register(
        context,
        CHILLBERRY,
        ModFeatures.WILD_CROP.get(),
        wildCropConfig(
            BlockStateProvider.simple(
                ModBlocks.CHILLBERRY_BUSH
                    .get()
                    .defaultBlockState()
                    .setValue(ChillberryBushBlock.AGE, 3)),
            30,
            8,
            4,
            onDirt()));
    register(
        context,
        WILD_SUNFIRE_TOMATO,
        ModFeatures.WILD_CROP.get(),
        wildCropConfig(ModBlocks.WILD_SUNFIRE_TOMATO.get(), 3, 7, 3, onDirt()));
    register(
        context,
        WILD_MANDRAKE,
        ModFeatures.WILD_CROP.get(),
        wildCropConfig(ModBlocks.WILD_MANDRAKE.get(), 3, 7, 3, onDirt()));
    register(
        context,
        CELESTIAL_BLOOM,
        ModFeatures.WILD_CROP.get(),
        wildCropConfig(ModBlocks.CELESTIAL_BLOOM.get(), 1, 7, 3, onDirt()));
    register(
        context,
        GHOST_FERN,
        ModFeatures.WILD_CROP.get(),
        wildCropConfig(ModBlocks.GHOST_FERN.get(), 1, 7, 3, onDirt()));
    register(
        context,
        SALTSPROUT,
        ModFeatures.WILD_CROP.get(),
        wildCropConfig(
            BlockStateProvider.simple(
                ModBlocks.SALTSPROUT.get().defaultBlockState().setValue(SaltsproutBlock.AGE, 2)),
            8,
            7,
            3,
            onSand()));
  }

  private static void registerDecorativePlants(BootstapContext<ConfiguredFeature<?, ?>> context) {
    register(
        context,
        BEGONIA,
        ModFeatures.WILD_CROP.get(),
        wildCropConfig(ModBlocks.BEGONIA.get(), 3, 7, 3, onDirt()));
    register(
        context,
        LAVENDER,
        ModFeatures.WILD_CROP.get(),
        wildCropConfig(ModBlocks.LAVENDER.get(), 15, 7, 5, onDirt()));
    register(
        context,
        DAHLIA,
        ModFeatures.WILD_CROP.get(),
        wildCropConfig(ModBlocks.DAHLIA.get(), 15, 7, 5, onDirt()));
    register(
        context,
        LOTUS_FLOWER,
        ModFeatures.WILD_CROP.get(),
        wildCropConfig(
            ModBlocks.LOTUS_FLOWER.get(),
            64,
            6,
            2,
            BlockPredicate.matchesBlocks(List.of(Blocks.WATER))));
    register(
        context,
        PALE_MUSHROOM,
        ModFeatures.WILD_CROP.get(),
        wildCropConfig(
            ModBlocks.PALE_MUSHROOM.get(),
            2,
            2,
            3,
            BlockPredicate.matchesBlocks(List.of(Blocks.MYCELIUM))));
    register(
        context,
        WITCHWEED,
        ModFeatures.WILD_CROP.get(),
        wildCropConfig(ModBlocks.WITCHWEED.get(), 20, 10, 5, onDirt()));
    register(
        context,
        NIGHTSHADE_BUSH,
        ModFeatures.WILD_CROP.get(),
        wildCropConfig(ModBlocks.NIGHTSHADE_BUSH.get(), 3, 7, 3, onDirt()));
  }

  private static void registerTrees(BootstapContext<ConfiguredFeature<?, ?>> context) {
    register(
        context,
        DARK_OAK_PLAIN,
        Feature.TREE,
        new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(Blocks.DARK_OAK_LOG),
                new DarkOakTrunkPlacer(5, 2, 1),
                BlockStateProvider.simple(Blocks.DARK_OAK_LEAVES),
                new DarkOakFoliagePlacer(ConstantInt.of(1), ConstantInt.of(0)) {},
                new TwoLayersFeatureSize(1, 0, 1))
            .build());

    register(
        context,
        DARK_OAK_COCOON,
        Feature.TREE,
        new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(Blocks.DARK_OAK_LOG),
                new DarkOakTrunkPlacer(5, 2, 1),
                BlockStateProvider.simple(Blocks.DARK_OAK_LEAVES),
                new DarkOakFoliagePlacer(ConstantInt.of(1), ConstantInt.of(0)) {},
                new TwoLayersFeatureSize(1, 0, 1))
            .decorators(List.of(new CocoonTreeDecorator()))
            .build());

    register(
        context,
        COTTONWOOD,
        Feature.TREE,
        new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.COTTONWOOD_LOG.get()),
                new StraightTrunkPlacer(6, 2, 1),
                BlockStateProvider.simple(ModBlocks.COTTONWOOD_LEAVES.get()),
                new FancyFoliagePlacer(ConstantInt.of(3), ConstantInt.of(1), 3) {},
                new TwoLayersFeatureSize(1, 0, 2))
            .decorators(List.of(new CatkinTreeDecorator()))
            .build());

    register(
        context,
        COTTONWOOD_COCOON,
        Feature.TREE,
        new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.COTTONWOOD_LOG.get()),
                new StraightTrunkPlacer(6, 2, 1),
                BlockStateProvider.simple(ModBlocks.COTTONWOOD_LEAVES.get()),
                new FancyFoliagePlacer(ConstantInt.of(3), ConstantInt.of(1), 3) {},
                new TwoLayersFeatureSize(1, 0, 2))
            .decorators(List.of(new CatkinTreeDecorator(), new CocoonTreeDecorator()))
            .build());

    register(
        context,
        WILLOW,
        Feature.TREE,
        new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.WILLOW_LOG.get()),
                new StraightTrunkPlacer(4, 2, 1),
                BlockStateProvider.simple(ModBlocks.WILLOW_LEAVES.get()),
                new BlobFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), 5),
                new TwoLayersFeatureSize(1, 0, 2))
            .ignoreVines()
            .build());
  }

  private static BlockPredicate onDirt() {
    return BlockPredicate.matchesTag(BlockTags.DIRT);
  }

  private static BlockPredicate onSand() {
    return BlockPredicate.matchesTag(BlockTags.SAND);
  }

  private static BlockPredicate onDirtOrMycelium() {
    return BlockPredicate.anyOf(
        BlockPredicate.matchesTag(BlockTags.DIRT),
        BlockPredicate.matchesBlocks(List.of(Blocks.MYCELIUM)));
  }

  private static BlockPredicate onOceanFloor() {
    return BlockPredicate.anyOf(
        BlockPredicate.matchesTag(BlockTags.DIRT),
        BlockPredicate.matchesTag(BlockTags.SAND),
        BlockPredicate.matchesBlocks(List.of(Blocks.GRAVEL, Blocks.CLAY)));
  }

  private static WildCropConfiguration wildCropConfig(
      Block block, int tries, int xzSpread, int ySpread, BlockPredicate groundPredicate) {
    return WildCropConfiguration.forLand(
        BlockStateProvider.simple(block), tries, xzSpread, ySpread, groundPredicate);
  }

  private static WildCropConfiguration wildCropConfig(
      BlockStateProvider provider,
      int tries,
      int xzSpread,
      int ySpread,
      BlockPredicate groundPredicate) {
    return WildCropConfiguration.forLand(provider, tries, xzSpread, ySpread, groundPredicate);
  }

  private static RandomPatchConfiguration patchConfig(
      Block block, int tries, int xzSpread, int ySpread) {
    return new RandomPatchConfiguration(
        tries,
        xzSpread,
        ySpread,
        PlacementUtils.onlyWhenEmpty(
            Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(block))));
  }

  private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
    return ResourceKey.create(
        Registries.CONFIGURED_FEATURE, new ResourceLocation(HexaliaMod.MODID, name));
  }

  private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
      BootstapContext<ConfiguredFeature<?, ?>> context,
      ResourceKey<ConfiguredFeature<?, ?>> key,
      F feature,
      FC config) {
    context.register(key, new ConfiguredFeature<>(feature, config));
  }
}
