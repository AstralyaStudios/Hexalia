package net.astralya.hexalia.block.entity;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.block.entity.custom.AegifloraBlockEntity;
import net.astralya.hexalia.block.entity.custom.AstrylisBlockEntity;
import net.astralya.hexalia.block.entity.custom.CenserBlockEntity;
import net.astralya.hexalia.block.entity.custom.DreamcatcherBlockEntity;
import net.astralya.hexalia.block.entity.custom.EggClusterBlockEntity;
import net.astralya.hexalia.block.entity.custom.GrimshadeBlockEntity;
import net.astralya.hexalia.block.entity.custom.HerbJarBlockEntity;
import net.astralya.hexalia.block.entity.custom.LourdesBlockEntity;
import net.astralya.hexalia.block.entity.custom.MortarAndPestleBlockEntity;
import net.astralya.hexalia.block.entity.custom.NautiliteBlockEntity;
import net.astralya.hexalia.block.entity.custom.NestingBlockEntity;
import net.astralya.hexalia.block.entity.custom.RitualBrazierBlockEntity;
import net.astralya.hexalia.block.entity.custom.RitualTableBlockEntity;
import net.astralya.hexalia.block.entity.custom.ShelfBlockEntity;
import net.astralya.hexalia.block.entity.custom.SmallCauldronBlockEntity;
import net.astralya.hexalia.block.entity.custom.WindsongBlockEntity;
import net.astralya.hexalia.block.entity.wood.ModHangingSignBlockEntity;
import net.astralya.hexalia.block.entity.wood.ModSignBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntityTypes {
    public static final BlockEntityType<SmallCauldronBlockEntity> SMALL_CAULDRON =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    new Identifier(HexaliaMod.MODID, "small_cauldron"),
                    FabricBlockEntityTypeBuilder.create(SmallCauldronBlockEntity::new, ModBlocks.SMALL_CAULDRON).build(null)
            );

    public static final BlockEntityType<RitualTableBlockEntity> RITUAL_TABLE =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    new Identifier(HexaliaMod.MODID, "ritual_table"),
                    FabricBlockEntityTypeBuilder.create(RitualTableBlockEntity::new, ModBlocks.RITUAL_TABLE).build(null)
            );

    public static final BlockEntityType<RitualBrazierBlockEntity> RITUAL_BRAZIER =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    new Identifier(HexaliaMod.MODID, "ritual_brazier"),
                    FabricBlockEntityTypeBuilder.create(RitualBrazierBlockEntity::new, ModBlocks.RITUAL_BRAZIER).build(null)
            );

    public static final BlockEntityType<HerbJarBlockEntity> HERB_JAR =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    new Identifier(HexaliaMod.MODID, "herb_jar"),
                    FabricBlockEntityTypeBuilder.create(HerbJarBlockEntity::new, ModBlocks.HERB_JAR).build(null)
            );

    public static final BlockEntityType<ShelfBlockEntity> SHELF =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    new Identifier(HexaliaMod.MODID, "shelf"),
                    FabricBlockEntityTypeBuilder.create(ShelfBlockEntity::new, ModBlocks.SHELF).build(null)
            );

    public static final BlockEntityType<CenserBlockEntity> CENSER =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    new Identifier(HexaliaMod.MODID, "censer"),
                    FabricBlockEntityTypeBuilder.create(CenserBlockEntity::new, ModBlocks.CENSER).build(null)
            );

    public static final BlockEntityType<MortarAndPestleBlockEntity> MORTAR_AND_PESTLE =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    new Identifier(HexaliaMod.MODID, "mortar_and_pestle"),
                    FabricBlockEntityTypeBuilder.create(MortarAndPestleBlockEntity::new, ModBlocks.MORTAR_AND_PESTLE).build(null)
            );

    public static final BlockEntityType<EggClusterBlockEntity> EGG_CLUSTER =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    new Identifier(HexaliaMod.MODID, "egg_cluster"),
                    FabricBlockEntityTypeBuilder.create(EggClusterBlockEntity::new, ModBlocks.EGG_CLUSTER).build(null)
            );

    public static final BlockEntityType<NestingBlockEntity> NESTING_BLOCK =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    new Identifier(HexaliaMod.MODID, "nesting_block"),
                    FabricBlockEntityTypeBuilder.create(NestingBlockEntity::new, ModBlocks.NESTING_BLOCK).build(null)
            );

    public static final BlockEntityType<DreamcatcherBlockEntity> DREAMCATCHER =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    new Identifier(HexaliaMod.MODID, "dreamcatcher"),
                    FabricBlockEntityTypeBuilder.create(DreamcatcherBlockEntity::new, ModBlocks.DREAMCATCHER).build(null)
            );

    public static final BlockEntityType<NautiliteBlockEntity> NAUTILITE =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    new Identifier(HexaliaMod.MODID, "nautilite"),
                    FabricBlockEntityTypeBuilder.create(NautiliteBlockEntity::new, ModBlocks.NAUTILITE).build(null)
            );

    public static final BlockEntityType<AstrylisBlockEntity> ASTRYLIS =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    new Identifier(HexaliaMod.MODID, "astrylis"),
                    FabricBlockEntityTypeBuilder.create(AstrylisBlockEntity::new, ModBlocks.ASTRYLIS).build(null)
            );

    public static final BlockEntityType<WindsongBlockEntity> WINDSONG =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    new Identifier(HexaliaMod.MODID, "windsong"),
                    FabricBlockEntityTypeBuilder.create(WindsongBlockEntity::new, ModBlocks.WINDSONG).build(null)
            );

    public static final BlockEntityType<GrimshadeBlockEntity> GRIMSHADE =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    new Identifier(HexaliaMod.MODID, "grimshade"),
                    FabricBlockEntityTypeBuilder.create(GrimshadeBlockEntity::new, ModBlocks.GRIMSHADE).build(null)
            );

    public static final BlockEntityType<LourdesBlockEntity> LOURDES =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    new Identifier(HexaliaMod.MODID, "lourdes"),
                    FabricBlockEntityTypeBuilder.create(LourdesBlockEntity::new, ModBlocks.LOURDES).build(null)
            );

    public static final BlockEntityType<AegifloraBlockEntity> AEGIFLORA =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    new Identifier(HexaliaMod.MODID, "aegiflora"),
                    FabricBlockEntityTypeBuilder.create(AegifloraBlockEntity::new, ModBlocks.AEGIFLORA, ModBlocks.WITHERED_AEGIFLORA).build(null)
            );

    public static final BlockEntityType<ModSignBlockEntity> MOD_SIGN =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    new Identifier(HexaliaMod.MODID, "mod_sign"),
                    FabricBlockEntityTypeBuilder.create(
                            ModSignBlockEntity::new,
                            ModBlocks.WILLOW_SIGN,
                            ModBlocks.WILLOW_WALL_SIGN,
                            ModBlocks.COTTONWOOD_SIGN,
                            ModBlocks.COTTONWOOD_WALL_SIGN
                    ).build(null)
            );

    public static final BlockEntityType<ModHangingSignBlockEntity> MOD_HANGING_SIGN =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    new Identifier(HexaliaMod.MODID, "mod_hanging_sign"),
                    FabricBlockEntityTypeBuilder.create(
                            ModHangingSignBlockEntity::new,
                            ModBlocks.WILLOW_HANGING_SIGN,
                            ModBlocks.WILLOW_HANGING_WALL_SIGN,
                            ModBlocks.COTTONWOOD_HANGING_SIGN,
                            ModBlocks.COTTONWOOD_HANGING_WALL_SIGN
                    ).build(null)
            );

    public static void registerBlockEntities() {
        HexaliaMod.LOGGER.info("Registering Block Entities for " + HexaliaMod.MODID);
    }
}
