package net.astralya.hexalia.block;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.custom.*;
import net.astralya.hexalia.block.custom.signs.ModHangingSignBlock;
import net.astralya.hexalia.block.custom.signs.ModStandingSignBlock;
import net.astralya.hexalia.block.custom.signs.ModWallHangingSignBlock;
import net.astralya.hexalia.block.custom.signs.ModWallSignBlock;
import net.astralya.hexalia.block.entity.custom.ModFlammableRotatedPillarBlock;
import net.astralya.hexalia.effect.ModMobEffects;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.util.ModWoodTypes;
import net.astralya.hexalia.worldgen.ModConfiguredFeatures;
import net.astralya.hexalia.worldgen.tree.CottonwoodTreeGrower;
import net.astralya.hexalia.worldgen.tree.WillowTreeGrower;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static net.astralya.hexalia.block.custom.CandleSkullBlock.LIT;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, HexaliaMod.MODID);

    public static final RegistryObject<Block> INFUSED_DIRT = registerBlock("infused_dirt",
            () -> new InfusedDirtBlock(BlockBehaviour.Properties.copy(Blocks.DIRT).sound(SoundType.MUD)));
    public static final RegistryObject<Block> INFUSED_FARMLAND = registerBlock("infused_farmland",
            () -> new InfusedFarmlandBlock(BlockBehaviour.Properties.copy(Blocks.DIRT).noOcclusion().randomTicks().sound(SoundType.MUD)));
    public static final RegistryObject<Block> SILKWORM_COCOON = registerBlock("silkworm_cocoon",
            () -> new SilkwormCocoonBlock(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL).noCollission()));
    public static final RegistryObject<Block> EGG_CLUSTER = registerBlock("egg_cluster",
            () -> new EggClusterBlock(BlockBehaviour.Properties.copy(Blocks.MOSS_BLOCK).noOcclusion()));

    public static final RegistryObject<Block> SPIRIT_BLOOM = registerBlock("spirit_bloom",
            () -> new HerbBlock(() -> MobEffects.POISON, 6, BlockBehaviour.Properties.copy(Blocks.POPPY)));
    public static final RegistryObject<Block> POTTED_SPIRIT_BLOOM = BLOCKS.register("potted_spirit_bloom",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, SPIRIT_BLOOM, BlockBehaviour.Properties.copy(Blocks.POTTED_POPPY)));
    public static final RegistryObject<Block> DREAMSHROOM = registerBlock("dreamshroom",
            () -> new DreamshroomBlock(BlockBehaviour.Properties.copy(Blocks.BROWN_MUSHROOM).lightLevel(state -> 4)));
    public static final RegistryObject<Block> SIREN_KELP = BLOCKS.register("siren_kelp",
            () -> new SirenKelpBlock(BlockBehaviour.Properties.copy(Blocks.SEAGRASS)));
    public static final RegistryObject<Block> POTTED_DREAMSHROOM = BLOCKS.register("potted_dreamshroom",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, DREAMSHROOM, BlockBehaviour.Properties.copy(Blocks.POTTED_POPPY)));
    public static final RegistryObject<Block> GHOST_FERN = registerBlock("ghost_fern",
            () -> new GhostFernBlock(() -> MobEffects.INVISIBILITY, 6, BlockBehaviour.Properties.copy(Blocks.POPPY).noCollission()));
    public static final RegistryObject<Block> POTTED_GHOST_FERN = BLOCKS.register("potted_ghost_fern",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, GHOST_FERN, BlockBehaviour.Properties.copy(Blocks.POTTED_FERN)));
    public static final RegistryObject<Block> CELESTIAL_BLOOM = registerBlock("celestial_bloom",
            () -> new CelestialBloomBlock(() -> MobEffects.NIGHT_VISION, 6, BlockBehaviour.Properties.copy(Blocks.POPPY).noCollission().lightLevel(state -> 6)));
    public static final RegistryObject<Block> POTTED_CELESTIAL_BLOOM = BLOCKS.register("potted_celestial_bloom",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, CELESTIAL_BLOOM, BlockBehaviour.Properties.copy(Blocks.POTTED_POPPY)));
    public static final RegistryObject<Block> WITHERED_CELESTIAL_BLOOM = registerBlock("withered_celestial_bloom",
            () -> new CelestialBloomBlock(() -> MobEffects.NIGHT_VISION, 3, BlockBehaviour.Properties.copy(Blocks.POPPY).noCollission().lightLevel(state -> 6)));
    public static final RegistryObject<Block> POTTED_WITHERED_CELESTIAL_BLOOM = BLOCKS.register("potted_withered_celestial_bloom",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, WITHERED_CELESTIAL_BLOOM, BlockBehaviour.Properties.copy(Blocks.POTTED_POPPY)));
    public static final RegistryObject<Block> LOTUS_FLOWER = BLOCKS.register("lotus_flower",
            () -> new LotusFlowerBlock(BlockBehaviour.Properties.copy(Blocks.LILY_PAD).lightLevel(state -> 6)));
    public static final RegistryObject<Block> WITCHWEED = registerBlock("witchweed",
            () -> new WitchweedBlock(() -> MobEffects.POISON, 6, BlockBehaviour.Properties.copy(Blocks.POPPY)));

    public static final RegistryObject<Block> MORPHORA = registerBlock("morphora",
            () -> new MorphoraBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).noCollission()));
    public static final RegistryObject<Block> POTTED_MORPHORA = BLOCKS.register("potted_morphora",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, MORPHORA, BlockBehaviour.Properties.copy(Blocks.POTTED_POPPY)));
    public static final RegistryObject<Block> GRIMSHADE = registerBlock("grimshade",
            () -> new GrimshadeBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).noCollission()));
    public static final RegistryObject<Block> POTTED_GRIMSHADE = BLOCKS.register("potted_grimshade",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, GRIMSHADE, BlockBehaviour.Properties.copy(Blocks.POTTED_POPPY)));
    public static final RegistryObject<Block> NAUTILITE = BLOCKS.register("nautilite",
            () -> new NautiliteBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).noCollission()));
    public static final RegistryObject<Block> WINDSONG = registerBlock("windsong",
            () -> new WindsongBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).noCollission()));
    public static final RegistryObject<Block> POTTED_WINDSONG = BLOCKS.register("potted_windsong",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, WINDSONG, BlockBehaviour.Properties.copy(Blocks.POTTED_POPPY)));
    public static final RegistryObject<Block> ASTRYLIS = registerBlock("astrylis",
            () -> new AstrylisBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).noCollission()));
    public static final RegistryObject<Block> POTTED_ASTRYLIS = BLOCKS.register("potted_astrylis",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ASTRYLIS, BlockBehaviour.Properties.copy(Blocks.POTTED_POPPY)));
    public static final RegistryObject<Block> LOURDES = registerBlock("lourdes",
            () -> new LourdesBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).noCollission()));
    public static final RegistryObject<Block> POTTED_LOURDES = BLOCKS.register("potted_lourdes",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ASTRYLIS, BlockBehaviour.Properties.copy(Blocks.POTTED_POPPY)));
    public static final RegistryObject<Block> AEGIFLORA = registerBlock("aegiflora",
            () -> new AegifloraBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).noCollission()));
    public static final RegistryObject<Block> POTTED_AEGIFLORA = BLOCKS.register("potted_aegiflora",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ASTRYLIS, BlockBehaviour.Properties.copy(Blocks.POTTED_POPPY)));
    public static final RegistryObject<Block> WITHERED_AEGIFLORA = registerBlock("withered_aegiflora",
            () -> new AegifloraBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).noCollission()));
    public static final RegistryObject<Block> POTTED_WITHERED_AEGIFLORA = BLOCKS.register("potted_withered_aegiflora",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, WITHERED_AEGIFLORA, BlockBehaviour.Properties.copy(Blocks.POTTED_POPPY)));

    public static final RegistryObject<Block> BEGONIA = registerBlock("begonia",
            () -> new FlowerBlock(() -> MobEffects.REGENERATION, 6, BlockBehaviour.Properties.copy(Blocks.POPPY)));
    public static final RegistryObject<Block> POTTED_BEGONIA = BLOCKS.register("potted_begonia",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, BEGONIA, BlockBehaviour.Properties.copy(Blocks.POTTED_POPPY)));
    public static final RegistryObject<Block> LAVENDER = registerBlock("lavender",
            () -> new FlowerBlock(() -> MobEffects.LUCK, 6, BlockBehaviour.Properties.copy(Blocks.POPPY)));
    public static final RegistryObject<Block> POTTED_LAVENDER = BLOCKS.register("potted_lavender",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, LAVENDER, BlockBehaviour.Properties.copy(Blocks.POTTED_POPPY)));
    public static final RegistryObject<Block> DAHLIA = registerBlock("dahlia",
            () -> new FlowerBlock(() -> MobEffects.DAMAGE_BOOST, 6, BlockBehaviour.Properties.copy(Blocks.POPPY)));
    public static final RegistryObject<Block> POTTED_DAHLIA = BLOCKS.register("potted_dahlia",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, DAHLIA, BlockBehaviour.Properties.copy(Blocks.POTTED_POPPY)));
    public static final RegistryObject<Block> PALE_MUSHROOM = registerBlock("pale_mushroom",
            () -> new PaleMushroomBlock(BlockBehaviour.Properties.copy(Blocks.BROWN_MUSHROOM).lightLevel(state -> 4)));
    public static final RegistryObject<Block> NIGHTSHADE_BUSH = registerBlock("nightshade_bush",
            () -> new FlowerBlock(() -> MobEffects.POISON, 6, BlockBehaviour.Properties.copy(Blocks.POPPY)));
    public static final RegistryObject<Block> POTTED_NIGHTSHADE_BUSH = BLOCKS.register("potted_nightshade_bush",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, NIGHTSHADE_BUSH, BlockBehaviour.Properties.copy(Blocks.POTTED_POPPY)));

    public static final RegistryObject<Block> MANDRAKE_CROP = BLOCKS.register("mandrake_crop",
            () -> new MandrakeCropBlock(BlockBehaviour.Properties.copy(Blocks.POTATOES)));
    public static final RegistryObject<Block> SUNFIRE_TOMATO_CROP = BLOCKS.register("sunfire_tomato_crop",
            () -> new SunfireTomatoCropBlock(BlockBehaviour.Properties.copy(Blocks.POTATOES)));
    public static final RegistryObject<Block> RABBAGE_CROP = BLOCKS.register("rabbage_crop",
            () -> new RabbageCropBlock(BlockBehaviour.Properties.copy(Blocks.POTATOES)));
    public static final RegistryObject<Block> WILD_MANDRAKE = registerBlock("wild_mandrake",
            () -> new WildMandrakeBlock(ModMobEffects.STUNNED, 6, BlockBehaviour.Properties.copy(Blocks.POPPY)));
    public static final RegistryObject<Block> WILD_SUNFIRE_TOMATO = registerBlock("wild_sunfire_tomato",
            () -> new WildSunfireTomatoBlock(BlockBehaviour.Properties.copy(Blocks.POPPY).lightLevel(state -> 4)));
    public static final RegistryObject<Block> CHILLBERRY_BUSH = BLOCKS.register("chillberry_bush",
            () -> new ChillberryBushBlock(BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH)));
    public static final RegistryObject<Block> SALTSPROUT = BLOCKS.register("saltsprout",
            () -> new SaltsproutBlock(BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH)));
    public static final RegistryObject<Block> GALEBERRIES_VINE = BLOCKS.register("galeberries_vine",
            () -> new GaleberriesVineBlock(BlockBehaviour.Properties.copy(Blocks.CAVE_VINES)));
    public static final RegistryObject<Block> GALEBERRIES_VINE_PLANT = BLOCKS.register("galeberries_vine_plant",
            () -> new GaleberriesVinePlantBlock(BlockBehaviour.Properties.copy(Blocks.CAVE_VINES_PLANT)));

    public static final RegistryObject<Block> SALT_BLOCK = registerBlock("salt_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));
    public static final RegistryObject<Block> SALT_LAMP = BLOCKS.register("salt_lamp",
            () -> new SaltLampBlock(BlockBehaviour.Properties.copy(Blocks.LANTERN).lightLevel(state -> 12)));
    public static final RegistryObject<Block> CELESTIAL_CRYSTAL_BLOCK = registerBlock("celestial_crystal_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK).lightLevel(state -> 6)));

    public static final RegistryObject<Block> SMALL_CAULDRON = BLOCKS.register("small_cauldron",
            () -> new SmallCauldronBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final RegistryObject<Block> SHELF = registerBlock("shelf",
            () -> new ShelfBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICKS).noOcclusion()));
    public static final RegistryObject<Block> RUSTIC_OVEN = registerBlock("rustic_oven",
            () -> new RusticOvenBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICKS).noOcclusion().lightLevel(state -> 10)));
    public static final RegistryObject<Block> RITUAL_TABLE = registerBlock("ritual_table",
            () -> new RitualTableBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICKS).noOcclusion()));
    public static final RegistryObject<Block> RITUAL_BRAZIER = registerBlock("ritual_brazier",
            () -> new RitualBrazierBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> HERB_JAR = BLOCKS.register("herb_jar",
            () -> new HerbJarBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion()));
    public static final RegistryObject<Block> CENSER = registerBlock("censer",
            () -> new CenserBlock(BlockBehaviour.Properties.copy(Blocks.CAMPFIRE).lightLevel(state -> state.getValue(LIT) ? 12 : 0).noOcclusion()));
    public static final RegistryObject<Block> DREAMCATCHER = registerBlock("dreamcatcher",
            () -> new DreamcatcherBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> MORTAR_AND_PESTLE = BLOCKS.register("mortar_and_pestle",
            () -> new MortarAndPestleBlock(BlockBehaviour.Properties.copy(Blocks.MANGROVE_WOOD).noOcclusion()));
    public static final RegistryObject<Block> NESTING_BLOCK = registerBlock("nesting_block",
            () -> new NestingBlock(BlockBehaviour.Properties.copy(Blocks.LOOM).noOcclusion()));

    public static final RegistryObject<Block> CANDLE_SKULL = BLOCKS.register("candle_skull",
            () -> new CandleSkullBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE)
                    .strength(1.0f).lightLevel(state -> state.getValue(LIT) ? 12 : 0)));
    public static final RegistryObject<Block> WITHER_CANDLE_SKULL = BLOCKS.register("wither_candle_skull",
            () -> new CandleSkullBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BLACK)
                    .strength(1.0f).lightLevel(state -> state.getValue(LIT) ? 12 : 0)));

    public static final RegistryObject<Block> COTTONWOOD_CATKIN = registerBlock("cottonwood_catkin",
            () -> new CatkinBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).strength(0.2f).noCollission()));
    public static final RegistryObject<Block> COTTONWOOD_LEAVES = registerBlock("cottonwood_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).strength(0.2f)));
    public static final RegistryObject<Block> COTTONWOOD_SAPLING = registerBlock("cottonwood_sapling",
            () -> new CocoonSaplingBlock(new CottonwoodTreeGrower(), ModConfiguredFeatures.COTTONWOOD, ModConfiguredFeatures.COTTONWOOD_COCOON,
                    BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).strength(0.2f)));
    public static final RegistryObject<Block> POTTED_COTTONWOOD_SAPLING = BLOCKS.register("potted_cottonwood_sapling",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, COTTONWOOD_SAPLING, BlockBehaviour.Properties.copy(Blocks.POTTED_POPPY)));
    public static final RegistryObject<Block> COTTONWOOD_LOG = registerBlock("cottonwood_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> STRIPPED_COTTONWOOD_LOG = registerBlock("stripped_cottonwood_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistryObject<Block> COTTONWOOD_WOOD = registerBlock("cottonwood_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
    public static final RegistryObject<Block> STRIPPED_COTTONWOOD_WOOD = registerBlock("stripped_cottonwood_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));
    public static final RegistryObject<Block> COTTONWOOD_PLANKS = registerBlock("cottonwood_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> COTTONWOOD_STAIRS = registerBlock("cottonwood_stairs",
            () -> new StairBlock(() -> ModBlocks.COTTONWOOD_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)));
    public static final RegistryObject<Block> COTTONWOOD_SLAB = registerBlock("cottonwood_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> COTTONWOOD_BUTTON = registerBlock("cottonwood_button",
            () -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), BlockSetType.OAK, 10, true));
    public static final RegistryObject<Block> COTTONWOOD_PRESSURE_PLATE = registerBlock("cottonwood_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,
                    BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), BlockSetType.OAK));
    public static final RegistryObject<Block> COTTONWOOD_FENCE = registerBlock("cottonwood_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));
    public static final RegistryObject<Block> COTTONWOOD_FENCE_GATE = registerBlock("cottonwood_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), WoodType.OAK));
    public static final RegistryObject<Block> COTTONWOOD_TRAPDOOR = registerBlock("cottonwood_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR).noOcclusion(), BlockSetType.OAK));
    public static final RegistryObject<Block> COTTONWOOD_DOOR = registerBlock("cottonwood_door",
            () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR).noOcclusion(), BlockSetType.OAK));
    public static final RegistryObject<Block> COTTONWOOD_SIGN = BLOCKS.register("cottonwood_sign",
            () -> new ModStandingSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SIGN).noCollission(), ModWoodTypes.COTTONWOOD));
    public static final RegistryObject<Block> COTTONWOOD_WALL_SIGN = BLOCKS.register("cottonwood_wall_sign",
            () -> new ModWallSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WALL_SIGN).noCollission(), ModWoodTypes.COTTONWOOD));
    public static final RegistryObject<Block> COTTONWOOD_HANGING_SIGN = BLOCKS.register("cottonwood_hanging_sign",
            () -> new ModHangingSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), ModWoodTypes.COTTONWOOD));
    public static final RegistryObject<Block> COTTONWOOD_HANGING_WALL_SIGN = BLOCKS.register("cottonwood_hanging_wall_sign",
            () -> new ModWallHangingSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), ModWoodTypes.COTTONWOOD));

    public static final RegistryObject<Block> WILLOW_LEAVES = registerBlock("willow_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).strength(0.2f)));
    public static final RegistryObject<Block> WILLOW_SAPLING = registerBlock("willow_sapling",
            () -> new SaplingBlock(new WillowTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING).strength(0.2f)));
    public static final RegistryObject<Block> POTTED_WILLOW_SAPLING = BLOCKS.register("potted_willow_sapling",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, WILLOW_SAPLING, BlockBehaviour.Properties.copy(Blocks.POTTED_POPPY)));
    public static final RegistryObject<Block> WILLOW_LOG = registerBlock("willow_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> STRIPPED_WILLOW_LOG = registerBlock("stripped_willow_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistryObject<Block> WILLOW_WOOD = registerBlock("willow_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
    public static final RegistryObject<Block> STRIPPED_WILLOW_WOOD = registerBlock("stripped_willow_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));
    public static final RegistryObject<Block> WILLOW_PLANKS = registerBlock("willow_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> WILLOW_STAIRS = registerBlock("willow_stairs",
            () -> new StairBlock(() -> ModBlocks.WILLOW_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)));
    public static final RegistryObject<Block> WILLOW_SLAB = registerBlock("willow_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> WILLOW_BUTTON = registerBlock("willow_button",
            () -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), BlockSetType.OAK, 10, true));
    public static final RegistryObject<Block> WILLOW_PRESSURE_PLATE = registerBlock("willow_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,
                    BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), BlockSetType.OAK));
    public static final RegistryObject<Block> WILLOW_FENCE = registerBlock("willow_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));
    public static final RegistryObject<Block> WILLOW_FENCE_GATE = registerBlock("willow_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), WoodType.OAK));
    public static final RegistryObject<Block> WILLOW_TRAPDOOR = registerBlock("willow_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR).noOcclusion(), BlockSetType.OAK));
    public static final RegistryObject<Block> WILLOW_DOOR = registerBlock("willow_door",
            () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR).noOcclusion(), BlockSetType.OAK));
    public static final RegistryObject<Block> WILLOW_SIGN = BLOCKS.register("willow_sign",
            () -> new ModStandingSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SIGN).noCollission(), ModWoodTypes.WILLOW));
    public static final RegistryObject<Block> WILLOW_WALL_SIGN = BLOCKS.register("willow_wall_sign",
            () -> new ModWallSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WALL_SIGN).noCollission(), ModWoodTypes.WILLOW));
    public static final RegistryObject<Block> WILLOW_HANGING_SIGN = BLOCKS.register("willow_hanging_sign",
            () -> new ModHangingSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), ModWoodTypes.WILLOW));
    public static final RegistryObject<Block> WILLOW_HANGING_WALL_SIGN = BLOCKS.register("willow_hanging_wall_sign",
            () -> new ModWallHangingSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), ModWoodTypes.WILLOW));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
