package net.astralya.hexalia.block.entity;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.block.entity.custom.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, HexaliaMod.MODID);

    // Block Entities
    public static final RegistryObject<BlockEntityType<SmallCauldronBlockEntity>> SMALL_CAULDRON = BLOCK_ENTITY_TYPE.register("small_cauldron",
            () -> BlockEntityType.Builder.of(SmallCauldronBlockEntity::new, ModBlocks.SMALL_CAULDRON.get()).build(null));
    public static final RegistryObject<BlockEntityType<RitualTableBlockEntity>> RITUAL_TABLE = BLOCK_ENTITY_TYPE.register("ritual_table",
            () -> BlockEntityType.Builder.of(RitualTableBlockEntity::new, ModBlocks.RITUAL_TABLE.get()).build(null));
    public static final RegistryObject<BlockEntityType<RitualBrazierBlockEntity>> RITUAL_BRAZIER = BLOCK_ENTITY_TYPE.register("ritual_brazier",
            () -> BlockEntityType.Builder.of(RitualBrazierBlockEntity::new, ModBlocks.RITUAL_BRAZIER.get()).build(null));
    public static final RegistryObject<BlockEntityType<HerbJarBlockEntity>> HERB_JAR = BLOCK_ENTITY_TYPE.register("herb_jar",
            () -> BlockEntityType.Builder.of(HerbJarBlockEntity::new, ModBlocks.HERB_JAR.get()).build(null));
    public static final RegistryObject<BlockEntityType<ShelfBlockEntity>> SHELF = BLOCK_ENTITY_TYPE.register("shelf",
            () -> BlockEntityType.Builder.of(ShelfBlockEntity::new, ModBlocks.SHELF.get()).build(null));
    public static final RegistryObject<BlockEntityType<CenserBlockEntity>> CENSER = BLOCK_ENTITY_TYPE.register("censer",
            () -> BlockEntityType.Builder.of(CenserBlockEntity::new, ModBlocks.CENSER.get()).build(null));
    public static final RegistryObject<BlockEntityType<MortarAndPestleBlockEntity>> MORTAR_AND_PESTLE = BLOCK_ENTITY_TYPE.register("mortar_and_pestle",
            () -> BlockEntityType.Builder.of(MortarAndPestleBlockEntity::new, ModBlocks.MORTAR_AND_PESTLE.get()).build(null));
    public static final RegistryObject<BlockEntityType<EggClusterBlockEntity>> EGG_CLUSTER = BLOCK_ENTITY_TYPE.register("egg_cluster",
            () -> BlockEntityType.Builder.of(EggClusterBlockEntity::new, ModBlocks.EGG_CLUSTER.get()).build(null));
    public static final RegistryObject<BlockEntityType<NestingBlockEntity>> NESTING_BLOCK = BLOCK_ENTITY_TYPE.register("nesting_block",
            () -> BlockEntityType.Builder.of(NestingBlockEntity::new, ModBlocks.NESTING_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<DreamcatcherBlockEntity>> DREAMCATCHER = BLOCK_ENTITY_TYPE.register("dreamcatcher",
            () -> BlockEntityType.Builder.of(DreamcatcherBlockEntity::new, ModBlocks.DREAMCATCHER.get()).build(null));

    // Enchanted Plants
    public static final RegistryObject<BlockEntityType<NautiliteBlockEntity>> NAUTILITE = BLOCK_ENTITY_TYPE.register("nautilite",
            () -> BlockEntityType.Builder.of(NautiliteBlockEntity::new, ModBlocks.NAUTILITE.get()).build(null));
    public static final RegistryObject<BlockEntityType<AstrylisBlockEntity>> ASTRYLIS = BLOCK_ENTITY_TYPE.register("astrylis",
            () -> BlockEntityType.Builder.of(AstrylisBlockEntity::new, ModBlocks.ASTRYLIS.get()).build(null));
    public static final RegistryObject<BlockEntityType<WindsongBlockEntity>> WINDSONG = BLOCK_ENTITY_TYPE.register("windsong",
            () -> BlockEntityType.Builder.of(WindsongBlockEntity::new, ModBlocks.WINDSONG.get()).build(null));
    public static final RegistryObject<BlockEntityType<GrimshadeBlockEntity>> GRIMSHADE = BLOCK_ENTITY_TYPE.register("grimshade",
            () -> BlockEntityType.Builder.of(GrimshadeBlockEntity::new, ModBlocks.GRIMSHADE.get()).build(null));
    public static final RegistryObject<BlockEntityType<LourdesBlockEntity>> LOURDES = BLOCK_ENTITY_TYPE.register("lourdes",
            () -> BlockEntityType.Builder.of(LourdesBlockEntity::new, ModBlocks.LOURDES.get()).build(null));
    public static final RegistryObject<BlockEntityType<AegifloraBlockEntity>> AEGIFLORA = BLOCK_ENTITY_TYPE.register("aegiflora",
            () -> BlockEntityType.Builder.of(AegifloraBlockEntity::new, ModBlocks.AEGIFLORA.get(), ModBlocks.WITHERED_AEGIFLORA.get()).build(null));

    // Mod Signs
    public static final RegistryObject<BlockEntityType<ModSignBlockEntity>> MOD_SIGN =
            BLOCK_ENTITY_TYPE.register("mod_sign", () ->
                    BlockEntityType.Builder.of(ModSignBlockEntity::new, ModBlocks.COTTONWOOD_SIGN.get(), ModBlocks.COTTONWOOD_WALL_SIGN.get(),
                            ModBlocks.WILLOW_SIGN.get(), ModBlocks.WILLOW_WALL_SIGN.get()).build(null));

    public static final RegistryObject<BlockEntityType<HangingSignBlockEntity>> MOD_HANGING_SIGN =
            BLOCK_ENTITY_TYPE.register("mod_hanging_sign", () ->
                    BlockEntityType.Builder.of(HangingSignBlockEntity::new, ModBlocks.COTTONWOOD_HANGING_SIGN.get(), ModBlocks.COTTONWOOD_HANGING_WALL_SIGN.get(),
                            ModBlocks.WILLOW_HANGING_SIGN.get(), ModBlocks.WILLOW_HANGING_WALL_SIGN.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPE.register(eventBus);
    }
}
