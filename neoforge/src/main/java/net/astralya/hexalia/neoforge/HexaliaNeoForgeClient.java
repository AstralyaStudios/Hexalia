package net.astralya.hexalia.neoforge;

import dev.architectury.platform.Platform;
import io.wispforest.accessories.api.client.AccessoriesRendererRegistry;
import net.astralya.hexalia.Hexalia;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.client.model.PestleModel;
import net.astralya.hexalia.client.renderer.blockentity.CenserBlockEntityRenderer;
import net.astralya.hexalia.client.renderer.blockentity.HerbJarBlockEntityRenderer;
import net.astralya.hexalia.client.renderer.blockentity.MortarAndPestleBlockEntityRenderer;
import net.astralya.hexalia.client.renderer.blockentity.RitualBrazierBlockEntityRenderer;
import net.astralya.hexalia.client.renderer.blockentity.RitualTableBlockEntityRenderer;
import net.astralya.hexalia.client.renderer.blockentity.ShelfBlockEntityRenderer;
import net.astralya.hexalia.client.renderer.blockentity.SmallCauldronBlockEntityRenderer;
import net.astralya.hexalia.client.renderer.entity.CacofeyRenderer;
import net.astralya.hexalia.client.renderer.entity.CinderhewProjectileRenderer;
import net.astralya.hexalia.client.renderer.entity.ModBoatRenderer;
import net.astralya.hexalia.client.renderer.entity.SilkMothRenderer;
import net.astralya.hexalia.client.renderer.entity.ThornArrowRenderer;
import net.astralya.hexalia.client.screen.NestingBlockScreen;
import net.astralya.hexalia.entity.ModEntities;
import net.astralya.hexalia.entity.boat.ModBoatEntity;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.menu.ModMenuTypes;
import net.astralya.hexalia.particle.ModParticleTypes;
import net.astralya.hexalia.particle.custom.CacofeyDustHeldParticle;
import net.astralya.hexalia.particle.custom.CacofeyDustParticle;
import net.astralya.hexalia.particle.custom.InfusedBubbleParticle;
import net.astralya.hexalia.particle.custom.LeavesParticle;
import net.astralya.hexalia.particle.custom.SparkleParticle;
import net.astralya.hexalia.particle.custom.SporeParticle;
import net.astralya.hexalia.util.ModItemProperties;
import net.astralya.hexalia.util.MagicResistanceTooltip;
import net.astralya.hexalia.util.ModWoodTypes;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public final class HexaliaNeoForgeClient {
  private HexaliaNeoForgeClient() {}

  public static void init(IEventBus modEventBus) {
    Hexalia.initClient();
    modEventBus.addListener(HexaliaNeoForgeClient::registerRenderers);
    modEventBus.addListener(HexaliaNeoForgeClient::registerParticles);
    modEventBus.addListener(HexaliaNeoForgeClient::registerLayerDefinitions);
    modEventBus.addListener(HexaliaNeoForgeClient::registerScreens);
    modEventBus.addListener(HexaliaNeoForgeClient::registerBlockColors);
    modEventBus.addListener(HexaliaNeoForgeClient::registerItemColors);
    modEventBus.addListener(HexaliaNeoForgeClient::setupClient);
    NeoForge.EVENT_BUS.addListener(HexaliaNeoForgeClient::addFullSetTooltip);
  }

  private static void addFullSetTooltip(ItemTooltipEvent event) {
    if (event.getEntity() != null) {
      MagicResistanceTooltip.addFullSetLineIfWorn(
          event.getEntity(), event.getItemStack(), event.getToolTip());
    }
  }

  private static void registerAccessoryRenderers() {
    AccessoriesRendererRegistry.registerNoRenderer(ModItems.EARPLUGS.get());
    AccessoriesRendererRegistry.registerNoRenderer(ModItems.SAGE_PENDANT.get());
    AccessoriesRendererRegistry.registerNoRenderer(ModItems.SEAFOAM_TALISMAN.get());
    AccessoriesRendererRegistry.registerNoRenderer(ModItems.MOONWARD_RING.get());
    AccessoriesRendererRegistry.registerNoRenderer(ModItems.WITCHHEART_CLUSTER.get());
    AccessoriesRendererRegistry.registerNoRenderer(ModItems.WYRD_FEATHER.get());
    AccessoriesRendererRegistry.registerNoRenderer(ModItems.GREEN_OMEN.get());
  }

  private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerBlockEntityRenderer(
        ModBlockEntityTypes.RITUAL_TABLE.get(), RitualTableBlockEntityRenderer::new);
    event.registerBlockEntityRenderer(
        ModBlockEntityTypes.RITUAL_BRAZIER.get(), RitualBrazierBlockEntityRenderer::new);
    event.registerBlockEntityRenderer(
        ModBlockEntityTypes.SMALL_CAULDRON.get(), SmallCauldronBlockEntityRenderer::new);
    event.registerBlockEntityRenderer(
        ModBlockEntityTypes.MORTAR_AND_PESTLE.get(), MortarAndPestleBlockEntityRenderer::new);
    event.registerBlockEntityRenderer(ModBlockEntityTypes.CENSER.get(), CenserBlockEntityRenderer::new);
    event.registerBlockEntityRenderer(ModBlockEntityTypes.SHELF.get(), ShelfBlockEntityRenderer::new);
    event.registerBlockEntityRenderer(
        ModBlockEntityTypes.HERB_JAR.get(), HerbJarBlockEntityRenderer::new);
    event.registerBlockEntityRenderer(ModBlockEntityTypes.MOD_SIGN.get(), SignRenderer::new);
    event.registerBlockEntityRenderer(
        ModBlockEntityTypes.MOD_HANGING_SIGN.get(), HangingSignRenderer::new);
    event.registerEntityRenderer(ModEntities.SILK_MOTH.get(), SilkMothRenderer::new);
    event.registerEntityRenderer(ModEntities.CACOFEY.get(), CacofeyRenderer::new);
    event.registerEntityRenderer(
        ModEntities.MOD_BOAT.get(), context -> new ModBoatRenderer(context, false));
    event.registerEntityRenderer(
        ModEntities.MOD_CHEST_BOAT.get(), context -> new ModBoatRenderer(context, true));
    event.registerEntityRenderer(ModEntities.RABBAGE.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(ModEntities.PURIFYING_SAC.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(ModEntities.FOUL_SAC.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(ModEntities.FROST_SAC.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(ModEntities.SEARING_SAC.get(), ThrownItemRenderer::new);
    event.registerEntityRenderer(ModEntities.THORN_ARROW.get(), ThornArrowRenderer::new);
    event.registerEntityRenderer(ModEntities.CINDERHEW.get(), CinderhewProjectileRenderer::new);
  }

  private static void registerParticles(RegisterParticleProvidersEvent event) {
    event.registerSpriteSet(ModParticleTypes.SPORE.get(), SporeParticle.Factory::new);
    event.registerSpriteSet(ModParticleTypes.SPARKLE.get(), SparkleParticle.Factory::new);
    event.registerSpriteSet(ModParticleTypes.LEAVES.get(), LeavesParticle.Factory::new);
    event.registerSpriteSet(
        ModParticleTypes.INFUSED_BUBBLES.get(), InfusedBubbleParticle.Factory::new);
    event.registerSpriteSet(ModParticleTypes.CACOFEY_DUST.get(), CacofeyDustParticle.Factory::new);
    event.registerSpriteSet(
        ModParticleTypes.CACOFEY_DUST_HELD.get(), CacofeyDustHeldParticle.Factory::new);
  }

  private static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
    event.registerLayerDefinition(PestleModel.LAYER_LOCATION, PestleModel::createLayer);
    for (ModBoatEntity.Type type : ModBoatEntity.Type.values()) {
      event.registerLayerDefinition(
          ModBoatRenderer.createBoatModelName(type), BoatModel::createBodyModel);
      event.registerLayerDefinition(
          ModBoatRenderer.createChestBoatModelName(type), ChestBoatModel::createBodyModel);
    }
  }

  private static void registerScreens(RegisterMenuScreensEvent event) {
    event.register(ModMenuTypes.NESTING_BLOCK.get(), NestingBlockScreen::new);
  }

  private static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
    event.register(
        (state, level, pos, tintIndex) ->
            level == null || pos == null
                ? FoliageColor.getDefaultColor()
                : BiomeColors.getAverageFoliageColor(level, pos),
        ModBlocks.COTTONWOOD_LEAVES.get(),
        ModBlocks.WILLOW_LEAVES.get());
  }

  private static void registerItemColors(RegisterColorHandlersEvent.Item event) {
    event.register(
        (stack, tintIndex) -> FoliageColor.getDefaultColor(),
        ModItems.COTTONWOOD_LEAVES.get(),
        ModItems.WILLOW_LEAVES.get());
  }

  private static void setupClient(FMLClientSetupEvent event) {
    event.enqueueWork(
        () -> {
          WoodType.register(ModWoodTypes.COTTONWOOD);
          WoodType.register(ModWoodTypes.WILLOW);
          Sheets.addWoodType(ModWoodTypes.COTTONWOOD);
          Sheets.addWoodType(ModWoodTypes.WILLOW);
          if (Platform.isModLoaded("accessories")) {
            registerAccessoryRenderers();
          }
          registerCutoutBlocks();
          ModItemProperties.register();
        });
  }

  @SuppressWarnings("deprecation")
  private static void registerCutoutBlocks() {
    RenderType cutout = RenderType.cutout();
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.SILKWORM_COCOON.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.RITUAL_BRAZIER.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.SMALL_CAULDRON.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.CENSER.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.HERB_JAR.get(), RenderType.translucent());
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.DREAMCATCHER.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.CANDLE_SKULL.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.WITHER_CANDLE_SKULL.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.MORPHORA.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_MORPHORA.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.GRIMSHADE.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_GRIMSHADE.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.NAUTILITE.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.WINDSONG.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_WINDSONG.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.ASTRYLIS.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_ASTRYLIS.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.LOURDES.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_LOURDES.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.AEGIFLORA.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_AEGIFLORA.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.WITHERED_AEGIFLORA.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_WITHERED_AEGIFLORA.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.BEGONIA.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_BEGONIA.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.LAVENDER.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_LAVENDER.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.DAHLIA.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_DAHLIA.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.NIGHTSHADE_BUSH.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_NIGHTSHADE_BUSH.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.SPIRIT_BLOOM.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_SPIRIT_BLOOM.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.DREAMSHROOM.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_DREAMSHROOM.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.PALE_MUSHROOM.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.SIREN_KELP.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.GHOST_FERN.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_GHOST_FERN.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.CELESTIAL_BLOOM.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_CELESTIAL_BLOOM.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.WITHERED_CELESTIAL_BLOOM.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_WITHERED_CELESTIAL_BLOOM.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.LOTUS_FLOWER.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.WITCHWEED.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.MANDRAKE_CROP.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.SUNFIRE_TOMATO_CROP.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.RABBAGE_CROP.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_MANDRAKE.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_SUNFIRE_TOMATO.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.CHILLBERRY_BUSH.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.SALTSPROUT.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.GALEBERRIES_VINE.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.GALEBERRIES_VINE_PLANT.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.SALT_LAMP.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.COTTONWOOD_CATKIN.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.COTTONWOOD_SAPLING.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_COTTONWOOD_SAPLING.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.COTTONWOOD_TRAPDOOR.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.COTTONWOOD_DOOR.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILLOW_SAPLING.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_WILLOW_SAPLING.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILLOW_TRAPDOOR.get(), cutout);
    ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILLOW_DOOR.get(), cutout);
  }
}
