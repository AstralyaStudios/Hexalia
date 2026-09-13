package net.astralya.hexalia.block.entity;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.astralya.hexalia.Hexalia;
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
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class ModBlockEntityTypes {
  public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
      DeferredRegister.create(Hexalia.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

  public static final RegistrySupplier<BlockEntityType<RitualTableBlockEntity>> RITUAL_TABLE =
      BLOCK_ENTITY_TYPES.register(
          "ritual_table",
          () ->
              BlockEntityType.Builder.of(RitualTableBlockEntity::new, ModBlocks.RITUAL_TABLE.get())
                  .build(null));

  public static final RegistrySupplier<BlockEntityType<RitualBrazierBlockEntity>> RITUAL_BRAZIER =
      BLOCK_ENTITY_TYPES.register(
          "ritual_brazier",
          () ->
              BlockEntityType.Builder.of(
                      RitualBrazierBlockEntity::new, ModBlocks.RITUAL_BRAZIER.get())
                  .build(null));

  public static final RegistrySupplier<BlockEntityType<SmallCauldronBlockEntity>> SMALL_CAULDRON =
      BLOCK_ENTITY_TYPES.register(
          "small_cauldron",
          () ->
              BlockEntityType.Builder.of(
                      SmallCauldronBlockEntity::new, ModBlocks.SMALL_CAULDRON.get())
                  .build(null));

  public static final RegistrySupplier<BlockEntityType<MortarAndPestleBlockEntity>>
      MORTAR_AND_PESTLE =
          BLOCK_ENTITY_TYPES.register(
              "mortar_and_pestle",
              () ->
                  BlockEntityType.Builder.of(
                          MortarAndPestleBlockEntity::new, ModBlocks.MORTAR_AND_PESTLE.get())
                      .build(null));

  public static final RegistrySupplier<BlockEntityType<CenserBlockEntity>> CENSER =
      BLOCK_ENTITY_TYPES.register(
          "censer",
          () ->
              BlockEntityType.Builder.of(CenserBlockEntity::new, ModBlocks.CENSER.get())
                  .build(null));

  public static final RegistrySupplier<BlockEntityType<EggClusterBlockEntity>> EGG_CLUSTER =
      BLOCK_ENTITY_TYPES.register(
          "egg_cluster",
          () ->
              BlockEntityType.Builder.of(EggClusterBlockEntity::new, ModBlocks.EGG_CLUSTER.get())
                  .build(null));

  public static final RegistrySupplier<BlockEntityType<NestingBlockEntity>> NESTING_BLOCK =
      BLOCK_ENTITY_TYPES.register(
          "nesting_block",
          () ->
              BlockEntityType.Builder.of(NestingBlockEntity::new, ModBlocks.NESTING_BLOCK.get())
                  .build(null));

  public static final RegistrySupplier<BlockEntityType<ShelfBlockEntity>> SHELF =
      BLOCK_ENTITY_TYPES.register(
          "shelf",
          () ->
              BlockEntityType.Builder.of(ShelfBlockEntity::new, ModBlocks.SHELF.get()).build(null));

  public static final RegistrySupplier<BlockEntityType<HerbJarBlockEntity>> HERB_JAR =
      BLOCK_ENTITY_TYPES.register(
          "herb_jar",
          () ->
              BlockEntityType.Builder.of(HerbJarBlockEntity::new, ModBlocks.HERB_JAR.get())
                  .build(null));

  public static final RegistrySupplier<BlockEntityType<DreamcatcherBlockEntity>> DREAMCATCHER =
      BLOCK_ENTITY_TYPES.register(
          "dreamcatcher",
          () ->
              BlockEntityType.Builder.of(DreamcatcherBlockEntity::new, ModBlocks.DREAMCATCHER.get())
                  .build(null));

  public static final RegistrySupplier<BlockEntityType<NautiliteBlockEntity>> NAUTILITE =
      BLOCK_ENTITY_TYPES.register(
          "nautilite",
          () ->
              BlockEntityType.Builder.of(NautiliteBlockEntity::new, ModBlocks.NAUTILITE.get())
                  .build(null));

  public static final RegistrySupplier<BlockEntityType<AstrylisBlockEntity>> ASTRYLIS =
      BLOCK_ENTITY_TYPES.register(
          "astrylis",
          () ->
              BlockEntityType.Builder.of(AstrylisBlockEntity::new, ModBlocks.ASTRYLIS.get())
                  .build(null));

  public static final RegistrySupplier<BlockEntityType<WindsongBlockEntity>> WINDSONG =
      BLOCK_ENTITY_TYPES.register(
          "windsong",
          () ->
              BlockEntityType.Builder.of(WindsongBlockEntity::new, ModBlocks.WINDSONG.get())
                  .build(null));

  public static final RegistrySupplier<BlockEntityType<GrimshadeBlockEntity>> GRIMSHADE =
      BLOCK_ENTITY_TYPES.register(
          "grimshade",
          () ->
              BlockEntityType.Builder.of(GrimshadeBlockEntity::new, ModBlocks.GRIMSHADE.get())
                  .build(null));

  public static final RegistrySupplier<BlockEntityType<LourdesBlockEntity>> LOURDES =
      BLOCK_ENTITY_TYPES.register(
          "lourdes",
          () ->
              BlockEntityType.Builder.of(LourdesBlockEntity::new, ModBlocks.LOURDES.get())
                  .build(null));

  public static final RegistrySupplier<BlockEntityType<AegifloraBlockEntity>> AEGIFLORA =
      BLOCK_ENTITY_TYPES.register(
          "aegiflora",
          () ->
              BlockEntityType.Builder.of(
                      AegifloraBlockEntity::new,
                      ModBlocks.AEGIFLORA.get(),
                      ModBlocks.WITHERED_AEGIFLORA.get())
                  .build(null));

  public static final RegistrySupplier<BlockEntityType<ModSignBlockEntity>> MOD_SIGN =
      BLOCK_ENTITY_TYPES.register(
          "sign",
          () ->
              BlockEntityType.Builder.of(
                      ModSignBlockEntity::new,
                      ModBlocks.COTTONWOOD_SIGN.get(),
                      ModBlocks.COTTONWOOD_WALL_SIGN.get(),
                      ModBlocks.WILLOW_SIGN.get(),
                      ModBlocks.WILLOW_WALL_SIGN.get())
                  .build(null));

  public static final RegistrySupplier<BlockEntityType<ModHangingSignBlockEntity>>
      MOD_HANGING_SIGN =
          BLOCK_ENTITY_TYPES.register(
              "hanging_sign",
              () ->
                  BlockEntityType.Builder.of(
                          ModHangingSignBlockEntity::new,
                          ModBlocks.COTTONWOOD_HANGING_SIGN.get(),
                          ModBlocks.COTTONWOOD_HANGING_WALL_SIGN.get(),
                          ModBlocks.WILLOW_HANGING_SIGN.get(),
                          ModBlocks.WILLOW_HANGING_WALL_SIGN.get())
                      .build(null));

  private ModBlockEntityTypes() {}

  public static void init() {
    BLOCK_ENTITY_TYPES.register();
  }
}
