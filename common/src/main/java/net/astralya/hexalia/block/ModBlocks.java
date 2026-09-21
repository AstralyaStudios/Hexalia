package net.astralya.hexalia.block;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.astralya.hexalia.Hexalia;
import net.astralya.hexalia.block.custom.AegifloraBlock;
import net.astralya.hexalia.block.custom.AstrylisBlock;
import net.astralya.hexalia.block.custom.CandleSkullBlock;
import net.astralya.hexalia.block.custom.CatkinBlock;
import net.astralya.hexalia.block.custom.CelestialBloomBlock;
import net.astralya.hexalia.block.custom.CenserBlock;
import net.astralya.hexalia.block.custom.ChillberryBushBlock;
import net.astralya.hexalia.block.custom.DreamcatcherBlock;
import net.astralya.hexalia.block.custom.DreamshroomBlock;
import net.astralya.hexalia.block.custom.EggClusterBlock;
import net.astralya.hexalia.block.custom.GaleberriesVineBlock;
import net.astralya.hexalia.block.custom.GaleberriesVinePlantBlock;
import net.astralya.hexalia.block.custom.GhostFernBlock;
import net.astralya.hexalia.block.custom.GrimshadeBlock;
import net.astralya.hexalia.block.custom.HerbBlock;
import net.astralya.hexalia.block.custom.HerbJarBlock;
import net.astralya.hexalia.block.custom.HexaliaSaplingBlock;
import net.astralya.hexalia.block.custom.InfusedDirtBlock;
import net.astralya.hexalia.block.custom.InfusedFarmlandBlock;
import net.astralya.hexalia.block.custom.LotusFlowerBlock;
import net.astralya.hexalia.block.custom.LourdesBlock;
import net.astralya.hexalia.block.custom.MandrakeCropBlock;
import net.astralya.hexalia.block.custom.MorphoraBlock;
import net.astralya.hexalia.block.custom.MortarAndPestleBlock;
import net.astralya.hexalia.block.custom.NautiliteBlock;
import net.astralya.hexalia.block.custom.NestingBlock;
import net.astralya.hexalia.block.custom.PaleMushroomBlock;
import net.astralya.hexalia.block.custom.RabbageCropBlock;
import net.astralya.hexalia.block.custom.RitualBrazierBlock;
import net.astralya.hexalia.block.custom.RitualTableBlock;
import net.astralya.hexalia.block.custom.RusticOvenBlock;
import net.astralya.hexalia.block.custom.SaltLampBlock;
import net.astralya.hexalia.block.custom.SaltsproutBlock;
import net.astralya.hexalia.block.custom.ShelfBlock;
import net.astralya.hexalia.block.custom.SilkwormCocoonBlock;
import net.astralya.hexalia.block.custom.SirenKelpBlock;
import net.astralya.hexalia.block.custom.SmallCauldronBlock;
import net.astralya.hexalia.block.custom.SunfireTomatoCropBlock;
import net.astralya.hexalia.block.custom.WildMandrakeBlock;
import net.astralya.hexalia.block.custom.WildSunfireTomatoBlock;
import net.astralya.hexalia.block.custom.WindsongBlock;
import net.astralya.hexalia.block.custom.WitchweedBlock;
import net.astralya.hexalia.block.custom.wood.ModHangingSignBlock;
import net.astralya.hexalia.block.custom.wood.ModStandingSignBlock;
import net.astralya.hexalia.block.custom.wood.ModWallHangingSignBlock;
import net.astralya.hexalia.block.custom.wood.ModWallSignBlock;
import net.astralya.hexalia.effect.ModMobEffects;
import net.astralya.hexalia.util.ModWoodTypes;
import net.astralya.hexalia.worldgen.ModConfiguredFeatures;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;

public final class ModBlocks {
  public static final DeferredRegister<Block> BLOCKS =
      DeferredRegister.create(Hexalia.MOD_ID, Registries.BLOCK);

  public static final RegistrySupplier<Block> INFUSED_DIRT =
      BLOCKS.register(
          "infused_dirt",
          () -> new InfusedDirtBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT)));

  public static final RegistrySupplier<Block> INFUSED_FARMLAND =
      BLOCKS.register(
          "infused_farmland",
          () ->
              new InfusedFarmlandBlock(
                  BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND)
                      .randomTicks()
                      .sound(SoundType.ROOTED_DIRT)));

  public static final RegistrySupplier<Block> SILKWORM_COCOON =
      BLOCKS.register(
          "silkworm_cocoon",
          () -> new SilkwormCocoonBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)));

  public static final RegistrySupplier<Block> EGG_CLUSTER =
      BLOCKS.register(
          "egg_cluster",
          () ->
              new EggClusterBlock(
                  BlockBehaviour.Properties.ofFullCopy(Blocks.MOSS_BLOCK).noOcclusion()));

  public static final RegistrySupplier<Block> RITUAL_TABLE =
      BLOCKS.register(
          "ritual_table",
          () ->
              new RitualTableBlock(
                  BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICKS).noOcclusion()));

  public static final RegistrySupplier<Block> RITUAL_BRAZIER =
      BLOCKS.register(
          "ritual_brazier",
          () ->
              new RitualBrazierBlock(
                  BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
                      .sound(SoundType.WOOD)
                      .noOcclusion()));

  public static final RegistrySupplier<Block> SMALL_CAULDRON =
      BLOCKS.register(
          "small_cauldron",
          () ->
              new SmallCauldronBlock(
                  BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON)
                      .noOcclusion()
                      .lightLevel(state -> state.getValue(SmallCauldronBlock.LIT) ? 12 : 0)));

  public static final RegistrySupplier<Block> MORTAR_AND_PESTLE =
      BLOCKS.register(
          "mortar_and_pestle",
          () ->
              new MortarAndPestleBlock(
                  BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noOcclusion()));

  public static final RegistrySupplier<Block> CENSER =
      BLOCKS.register(
          "censer",
          () ->
              new CenserBlock(
                  BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
                      .sound(SoundType.WOOD)
                      .noOcclusion()
                      .lightLevel(state -> state.getValue(CenserBlock.LIT) ? 12 : 0)));

  public static final RegistrySupplier<Block> NESTING_BLOCK =
      BLOCKS.register(
          "nesting_block",
          () -> new NestingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LOOM).noOcclusion()));

  public static final RegistrySupplier<Block> SHELF =
      BLOCKS.register(
          "shelf",
          () ->
              new ShelfBlock(
                  BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICKS).noOcclusion()));

  public static final RegistrySupplier<Block> HERB_JAR =
      BLOCKS.register(
          "herb_jar",
          () -> new HerbJarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noOcclusion()));

  public static final RegistrySupplier<Block> DREAMCATCHER =
      BLOCKS.register(
          "dreamcatcher",
          () -> new DreamcatcherBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));

  public static final RegistrySupplier<Block> CANDLE_SKULL =
      BLOCKS.register(
          "candle_skull",
          () ->
              new CandleSkullBlock(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.TERRACOTTA_WHITE)
                      .strength(1.0F)
                      .lightLevel(state -> state.getValue(CandleSkullBlock.LIT) ? 12 : 0)));

  public static final RegistrySupplier<Block> WITHER_CANDLE_SKULL =
      BLOCKS.register(
          "wither_candle_skull",
          () ->
              new CandleSkullBlock(
                  BlockBehaviour.Properties.of()
                      .mapColor(MapColor.TERRACOTTA_BLACK)
                      .strength(1.0F)
                      .lightLevel(state -> state.getValue(CandleSkullBlock.LIT) ? 12 : 0)));

  public static final RegistrySupplier<Block> MORPHORA =
      BLOCKS.register(
          "morphora", () -> new MorphoraBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY)));

  public static final RegistrySupplier<Block> POTTED_MORPHORA =
      BLOCKS.register(
          "potted_morphora",
          () ->
              new FlowerPotBlock(
                  MORPHORA.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_POPPY)));

  public static final RegistrySupplier<Block> GRIMSHADE =
      BLOCKS.register(
          "grimshade",
          () ->
              new GrimshadeBlock(
                  BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY).noCollission()));

  public static final RegistrySupplier<Block> POTTED_GRIMSHADE =
      potted("potted_grimshade", GRIMSHADE);

  public static final RegistrySupplier<Block> NAUTILITE =
      BLOCKS.register(
          "nautilite",
          () -> new NautiliteBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SEAGRASS)));

  public static final RegistrySupplier<Block> WINDSONG =
      BLOCKS.register(
          "windsong",
          () ->
              new WindsongBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY).noCollission()));

  public static final RegistrySupplier<Block> POTTED_WINDSONG = potted("potted_windsong", WINDSONG);

  public static final RegistrySupplier<Block> ASTRYLIS =
      BLOCKS.register(
          "astrylis",
          () ->
              new AstrylisBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY).noCollission()));

  public static final RegistrySupplier<Block> POTTED_ASTRYLIS = potted("potted_astrylis", ASTRYLIS);

  public static final RegistrySupplier<Block> LOURDES =
      BLOCKS.register(
          "lourdes",
          () ->
              new LourdesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY).noCollission()));

  public static final RegistrySupplier<Block> POTTED_LOURDES = potted("potted_lourdes", LOURDES);

  public static final RegistrySupplier<Block> AEGIFLORA =
      BLOCKS.register(
          "aegiflora",
          () ->
              new AegifloraBlock(
                  BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY).noCollission()));

  public static final RegistrySupplier<Block> POTTED_AEGIFLORA =
      potted("potted_aegiflora", AEGIFLORA);

  public static final RegistrySupplier<Block> WITHERED_AEGIFLORA =
      BLOCKS.register(
          "withered_aegiflora",
          () ->
              new AegifloraBlock(
                  BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY).noCollission()));

  public static final RegistrySupplier<Block> POTTED_WITHERED_AEGIFLORA =
      potted("potted_withered_aegiflora", WITHERED_AEGIFLORA);

  public static final RegistrySupplier<Block> BEGONIA =
      BLOCKS.register(
          "begonia",
          () ->
              new FlowerBlock(
                  MobEffects.REGENERATION, 6, BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY)));

  public static final RegistrySupplier<Block> POTTED_BEGONIA = potted("potted_begonia", BEGONIA);

  public static final RegistrySupplier<Block> LAVENDER =
      BLOCKS.register(
          "lavender",
          () ->
              new FlowerBlock(
                  MobEffects.LUCK, 6, BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY)));

  public static final RegistrySupplier<Block> POTTED_LAVENDER = potted("potted_lavender", LAVENDER);

  public static final RegistrySupplier<Block> DAHLIA =
      BLOCKS.register(
          "dahlia",
          () ->
              new FlowerBlock(
                  MobEffects.DAMAGE_BOOST, 6, BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY)));

  public static final RegistrySupplier<Block> POTTED_DAHLIA = potted("potted_dahlia", DAHLIA);

  public static final RegistrySupplier<Block> NIGHTSHADE_BUSH =
      BLOCKS.register(
          "nightshade_bush",
          () ->
              new FlowerBlock(
                  MobEffects.POISON, 6, BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY)));

  public static final RegistrySupplier<Block> POTTED_NIGHTSHADE_BUSH =
      potted("potted_nightshade_bush", NIGHTSHADE_BUSH);

  public static final RegistrySupplier<Block> SPIRIT_BLOOM =
      BLOCKS.register(
          "spirit_bloom",
          () ->
              new HerbBlock(
                  MobEffects.POISON, 6, BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY)));

  public static final RegistrySupplier<Block> POTTED_SPIRIT_BLOOM =
      potted("potted_spirit_bloom", SPIRIT_BLOOM);

  public static final RegistrySupplier<Block> DREAMSHROOM =
      BLOCKS.register(
          "dreamshroom",
          () ->
              new DreamshroomBlock(
                  BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_MUSHROOM)
                      .lightLevel(state -> 4)));

  public static final RegistrySupplier<Block> POTTED_DREAMSHROOM =
      potted(
          "potted_dreamshroom",
          DREAMSHROOM,
          BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_POPPY).lightLevel(state -> 4));

  public static final RegistrySupplier<Block> PALE_MUSHROOM =
      BLOCKS.register(
          "pale_mushroom",
          () ->
              new PaleMushroomBlock(
                  BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_MUSHROOM)
                      .lightLevel(state -> 4)));

  public static final RegistrySupplier<Block> SIREN_KELP =
      BLOCKS.register(
          "siren_kelp",
          () -> new SirenKelpBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SEAGRASS)));

  public static final RegistrySupplier<Block> GHOST_FERN =
      BLOCKS.register(
          "ghost_fern",
          () ->
              new GhostFernBlock(
                  MobEffects.INVISIBILITY, 6, BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY)));

  public static final RegistrySupplier<Block> POTTED_GHOST_FERN =
      potted("potted_ghost_fern", GHOST_FERN);

  public static final RegistrySupplier<Block> CELESTIAL_BLOOM =
      BLOCKS.register(
          "celestial_bloom",
          () ->
              new CelestialBloomBlock(
                  MobEffects.NIGHT_VISION,
                  6,
                  BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY).lightLevel(state -> 6)));

  public static final RegistrySupplier<Block> POTTED_CELESTIAL_BLOOM =
      potted(
          "potted_celestial_bloom",
          CELESTIAL_BLOOM,
          BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_POPPY).lightLevel(state -> 6));

  public static final RegistrySupplier<Block> WITHERED_CELESTIAL_BLOOM =
      BLOCKS.register(
          "withered_celestial_bloom",
          () ->
              new CelestialBloomBlock(
                  MobEffects.NIGHT_VISION,
                  3,
                  BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY).lightLevel(state -> 6)));

  public static final RegistrySupplier<Block> POTTED_WITHERED_CELESTIAL_BLOOM =
      potted(
          "potted_withered_celestial_bloom",
          WITHERED_CELESTIAL_BLOOM,
          BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_POPPY).lightLevel(state -> 6));

  public static final RegistrySupplier<Block> LOTUS_FLOWER =
      BLOCKS.register(
          "lotus_flower",
          () ->
              new LotusFlowerBlock(
                  BlockBehaviour.Properties.ofFullCopy(Blocks.LILY_PAD).lightLevel(state -> 6)));

  public static final RegistrySupplier<Block> WITCHWEED =
      BLOCKS.register(
          "witchweed",
          () ->
              new WitchweedBlock(
                  MobEffects.POISON,
                  6,
                  BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY)
                      .dynamicShape()
                      .noCollission()));

  public static final RegistrySupplier<Block> MANDRAKE_CROP =
      BLOCKS.register(
          "mandrake_crop",
          () -> new MandrakeCropBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.POTATOES)));

  public static final RegistrySupplier<Block> SUNFIRE_TOMATO_CROP =
      BLOCKS.register(
          "sunfire_tomato_crop",
          () -> new SunfireTomatoCropBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.POTATOES)));

  public static final RegistrySupplier<Block> RABBAGE_CROP =
      BLOCKS.register(
          "rabbage_crop",
          () -> new RabbageCropBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.POTATOES)));

  public static final RegistrySupplier<Block> WILD_MANDRAKE =
      BLOCKS.register(
          "wild_mandrake",
          () ->
              new WildMandrakeBlock(
                  BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModMobEffects.STUNNED.get()),
                  6,
                  BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY)
                      .dynamicShape()
                      .noCollission()));

  public static final RegistrySupplier<Block> WILD_SUNFIRE_TOMATO =
      BLOCKS.register(
          "wild_sunfire_tomato",
          () ->
              new WildSunfireTomatoBlock(
                  BlockBehaviour.Properties.ofFullCopy(Blocks.CORNFLOWER)
                      .lightLevel(state -> 4)
                      .dynamicShape()
                      .noCollission()));

  public static final RegistrySupplier<Block> CHILLBERRY_BUSH =
      BLOCKS.register(
          "chillberry_bush",
          () ->
              new ChillberryBushBlock(
                  BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH).randomTicks()));

  public static final RegistrySupplier<Block> SALTSPROUT =
      BLOCKS.register(
          "saltsprout",
          () ->
              new SaltsproutBlock(
                  BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH).randomTicks()));

  public static final RegistrySupplier<Block> GALEBERRIES_VINE =
      BLOCKS.register(
          "galeberries_vine",
          () -> new GaleberriesVineBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAVE_VINES)));

  public static final RegistrySupplier<Block> GALEBERRIES_VINE_PLANT =
      BLOCKS.register(
          "galeberries_vine_plant",
          () ->
              new GaleberriesVinePlantBlock(
                  BlockBehaviour.Properties.ofFullCopy(Blocks.CAVE_VINES_PLANT)));

  public static final RegistrySupplier<Block> SALT_BLOCK =
      BLOCKS.register(
          "salt_block",
          () ->
              new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).sound(SoundType.SAND)));

  public static final RegistrySupplier<Block> SALT_LAMP =
      BLOCKS.register(
          "salt_lamp",
          () ->
              new SaltLampBlock(
                  BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN).lightLevel(state -> 12)));

  public static final RegistrySupplier<Block> CELESTIAL_CRYSTAL_BLOCK =
      BLOCKS.register(
          "celestial_crystal_block",
          () ->
              new Block(
                  BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK)
                      .lightLevel(state -> 6)));

  public static final RegistrySupplier<Block> RUSTIC_OVEN =
      BLOCKS.register(
          "rustic_oven",
          () ->
              new RusticOvenBlock(
                  BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICKS)
                      .noOcclusion()
                      .lightLevel(state -> 10)));

  public static final RegistrySupplier<Block> COTTONWOOD_CATKIN =
      BLOCKS.register(
          "cottonwood_catkin",
          () ->
              new CatkinBlock(
                  BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)
                      .strength(0.2F)
                      .noCollission()
                      .noOcclusion()));

  public static final RegistrySupplier<Block> COTTONWOOD_LEAVES =
      BLOCKS.register(
          "cottonwood_leaves",
          () ->
              new LeavesBlock(
                  BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)
                      .strength(0.2F)
                      .noOcclusion()));

  public static final RegistrySupplier<Block> COTTONWOOD_SAPLING =
      BLOCKS.register(
          "cottonwood_sapling",
          () ->
              new HexaliaSaplingBlock(
                  "cottonwood",
                  ModConfiguredFeatures.COTTONWOOD,
                  BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING).strength(0.2F)));

  public static final RegistrySupplier<Block> POTTED_COTTONWOOD_SAPLING =
      potted("potted_cottonwood_sapling", COTTONWOOD_SAPLING);

  public static final RegistrySupplier<Block> COTTONWOOD_LOG =
      log("cottonwood_log", Blocks.OAK_LOG);

  public static final RegistrySupplier<Block> STRIPPED_COTTONWOOD_LOG =
      log("stripped_cottonwood_log", Blocks.STRIPPED_OAK_LOG);

  public static final RegistrySupplier<Block> COTTONWOOD_WOOD =
      log("cottonwood_wood", Blocks.OAK_WOOD);

  public static final RegistrySupplier<Block> STRIPPED_COTTONWOOD_WOOD =
      log("stripped_cottonwood_wood", Blocks.STRIPPED_OAK_WOOD);

  public static final RegistrySupplier<Block> COTTONWOOD_PLANKS =
      BLOCKS.register(
          "cottonwood_planks",
          () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));

  public static final RegistrySupplier<Block> COTTONWOOD_STAIRS =
      BLOCKS.register(
          "cottonwood_stairs",
          () ->
              new StairBlock(
                  COTTONWOOD_PLANKS.get().defaultBlockState(),
                  BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));

  public static final RegistrySupplier<Block> COTTONWOOD_SLAB =
      BLOCKS.register(
          "cottonwood_slab",
          () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));

  public static final RegistrySupplier<Block> COTTONWOOD_BUTTON =
      BLOCKS.register(
          "cottonwood_button",
          () ->
              new ButtonBlock(
                  BlockSetType.OAK, 10, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)));

  public static final RegistrySupplier<Block> COTTONWOOD_PRESSURE_PLATE =
      BLOCKS.register(
          "cottonwood_pressure_plate",
          () ->
              new PressurePlateBlock(
                  BlockSetType.OAK,
                  BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE)));

  public static final RegistrySupplier<Block> COTTONWOOD_FENCE =
      BLOCKS.register(
          "cottonwood_fence",
          () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE)));

  public static final RegistrySupplier<Block> COTTONWOOD_FENCE_GATE =
      BLOCKS.register(
          "cottonwood_fence_gate",
          () ->
              new FenceGateBlock(
                  ModWoodTypes.COTTONWOOD,
                  BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE)));

  public static final RegistrySupplier<Block> COTTONWOOD_TRAPDOOR =
      BLOCKS.register(
          "cottonwood_trapdoor",
          () ->
              new TrapDoorBlock(
                  BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR)));

  public static final RegistrySupplier<Block> COTTONWOOD_DOOR =
      BLOCKS.register(
          "cottonwood_door",
          () ->
              new DoorBlock(
                  BlockSetType.OAK,
                  BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR).noOcclusion()));

  public static final RegistrySupplier<Block> COTTONWOOD_SIGN =
      BLOCKS.register(
          "cottonwood_sign",
          () ->
              new ModStandingSignBlock(
                  ModWoodTypes.COTTONWOOD,
                  BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN).noOcclusion()));

  public static final RegistrySupplier<Block> COTTONWOOD_WALL_SIGN =
      BLOCKS.register(
          "cottonwood_wall_sign",
          () ->
              new ModWallSignBlock(
                  ModWoodTypes.COTTONWOOD,
                  BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_SIGN).noOcclusion()));

  public static final RegistrySupplier<Block> COTTONWOOD_HANGING_SIGN =
      BLOCKS.register(
          "cottonwood_hanging_sign",
          () ->
              new ModHangingSignBlock(
                  ModWoodTypes.COTTONWOOD,
                  BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_HANGING_SIGN).noOcclusion()));

  public static final RegistrySupplier<Block> COTTONWOOD_HANGING_WALL_SIGN =
      BLOCKS.register(
          "cottonwood_hanging_wall_sign",
          () ->
              new ModWallHangingSignBlock(
                  ModWoodTypes.COTTONWOOD,
                  BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN)
                      .noOcclusion()));

  public static final RegistrySupplier<Block> WILLOW_LEAVES =
      BLOCKS.register(
          "willow_leaves",
          () ->
              new LeavesBlock(
                  BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)
                      .strength(0.2F)
                      .noOcclusion()));

  public static final RegistrySupplier<Block> WILLOW_SAPLING =
      BLOCKS.register(
          "willow_sapling",
          () ->
              new HexaliaSaplingBlock(
                  "willow",
                  ModConfiguredFeatures.WILLOW,
                  BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING).strength(0.2F)));

  public static final RegistrySupplier<Block> POTTED_WILLOW_SAPLING =
      potted("potted_willow_sapling", WILLOW_SAPLING);

  public static final RegistrySupplier<Block> WILLOW_LOG = log("willow_log", Blocks.OAK_LOG);

  public static final RegistrySupplier<Block> STRIPPED_WILLOW_LOG =
      log("stripped_willow_log", Blocks.STRIPPED_OAK_LOG);

  public static final RegistrySupplier<Block> WILLOW_WOOD = log("willow_wood", Blocks.OAK_WOOD);

  public static final RegistrySupplier<Block> STRIPPED_WILLOW_WOOD =
      log("stripped_willow_wood", Blocks.STRIPPED_OAK_WOOD);

  public static final RegistrySupplier<Block> WILLOW_PLANKS =
      BLOCKS.register(
          "willow_planks",
          () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));

  public static final RegistrySupplier<Block> WILLOW_STAIRS =
      BLOCKS.register(
          "willow_stairs",
          () ->
              new StairBlock(
                  WILLOW_PLANKS.get().defaultBlockState(),
                  BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));

  public static final RegistrySupplier<Block> WILLOW_SLAB =
      BLOCKS.register(
          "willow_slab",
          () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));

  public static final RegistrySupplier<Block> WILLOW_BUTTON =
      BLOCKS.register(
          "willow_button",
          () ->
              new ButtonBlock(
                  BlockSetType.OAK, 10, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)));

  public static final RegistrySupplier<Block> WILLOW_PRESSURE_PLATE =
      BLOCKS.register(
          "willow_pressure_plate",
          () ->
              new PressurePlateBlock(
                  BlockSetType.OAK,
                  BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE)));

  public static final RegistrySupplier<Block> WILLOW_FENCE =
      BLOCKS.register(
          "willow_fence",
          () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE)));

  public static final RegistrySupplier<Block> WILLOW_FENCE_GATE =
      BLOCKS.register(
          "willow_fence_gate",
          () ->
              new FenceGateBlock(
                  ModWoodTypes.WILLOW,
                  BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE)));

  public static final RegistrySupplier<Block> WILLOW_TRAPDOOR =
      BLOCKS.register(
          "willow_trapdoor",
          () ->
              new TrapDoorBlock(
                  BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR)));

  public static final RegistrySupplier<Block> WILLOW_DOOR =
      BLOCKS.register(
          "willow_door",
          () ->
              new DoorBlock(
                  BlockSetType.OAK,
                  BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR).noOcclusion()));

  public static final RegistrySupplier<Block> WILLOW_SIGN =
      BLOCKS.register(
          "willow_sign",
          () ->
              new ModStandingSignBlock(
                  ModWoodTypes.WILLOW,
                  BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN).noOcclusion()));

  public static final RegistrySupplier<Block> WILLOW_WALL_SIGN =
      BLOCKS.register(
          "willow_wall_sign",
          () ->
              new ModWallSignBlock(
                  ModWoodTypes.WILLOW,
                  BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_SIGN).noOcclusion()));

  public static final RegistrySupplier<Block> WILLOW_HANGING_SIGN =
      BLOCKS.register(
          "willow_hanging_sign",
          () ->
              new ModHangingSignBlock(
                  ModWoodTypes.WILLOW,
                  BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_HANGING_SIGN).noOcclusion()));

  public static final RegistrySupplier<Block> WILLOW_HANGING_WALL_SIGN =
      BLOCKS.register(
          "willow_hanging_wall_sign",
          () ->
              new ModWallHangingSignBlock(
                  ModWoodTypes.WILLOW,
                  BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN)
                      .noOcclusion()));

  private ModBlocks() {}

  private static RegistrySupplier<Block> potted(String name, RegistrySupplier<Block> flower) {
    return potted(name, flower, BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_POPPY));
  }

  private static RegistrySupplier<Block> potted(
      String name, RegistrySupplier<Block> flower, BlockBehaviour.Properties properties) {
    return BLOCKS.register(name, () -> new FlowerPotBlock(flower.get(), properties));
  }

  private static RegistrySupplier<Block> log(String name, Block base) {
    return BLOCKS.register(
        name, () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(base)));
  }

  public static void init() {
    BLOCKS.register();
  }
}
