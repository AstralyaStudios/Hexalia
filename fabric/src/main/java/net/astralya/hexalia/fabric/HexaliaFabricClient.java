package net.astralya.hexalia.fabric;

import net.astralya.hexalia.Hexalia;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.client.model.PestleModel;
import net.astralya.hexalia.item.ModItems;
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
import net.astralya.hexalia.fabric.mixin.WoodTypeInvoker;
import dev.architectury.platform.Platform;
import io.wispforest.accessories.api.client.AccessoriesRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public final class HexaliaFabricClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    Hexalia.initClient();
    if (Platform.isModLoaded("accessories")) {
      registerAccessoryRenderers();
    }
    registerLeafColors();
    registerCutoutBlocks();
    registerWoodTypes();
    BlockEntityRenderers.register(
        ModBlockEntityTypes.RITUAL_TABLE.get(), RitualTableBlockEntityRenderer::new);
    BlockEntityRenderers.register(
        ModBlockEntityTypes.RITUAL_BRAZIER.get(), RitualBrazierBlockEntityRenderer::new);
    BlockEntityRenderers.register(
        ModBlockEntityTypes.SMALL_CAULDRON.get(), SmallCauldronBlockEntityRenderer::new);
    BlockEntityRenderers.register(
        ModBlockEntityTypes.MORTAR_AND_PESTLE.get(), MortarAndPestleBlockEntityRenderer::new);
    BlockEntityRenderers.register(ModBlockEntityTypes.CENSER.get(), CenserBlockEntityRenderer::new);
    BlockEntityRenderers.register(ModBlockEntityTypes.SHELF.get(), ShelfBlockEntityRenderer::new);
    BlockEntityRenderers.register(
        ModBlockEntityTypes.HERB_JAR.get(), HerbJarBlockEntityRenderer::new);
    BlockEntityRenderers.register(ModBlockEntityTypes.MOD_SIGN.get(), SignRenderer::new);
    BlockEntityRenderers.register(
        ModBlockEntityTypes.MOD_HANGING_SIGN.get(), HangingSignRenderer::new);
    EntityRendererRegistry.register(ModEntities.SILK_MOTH.get(), SilkMothRenderer::new);
    EntityRendererRegistry.register(ModEntities.CACOFEY.get(), CacofeyRenderer::new);
    EntityRendererRegistry.register(
        ModEntities.MOD_BOAT.get(), context -> new ModBoatRenderer(context, false));
    EntityRendererRegistry.register(
        ModEntities.MOD_CHEST_BOAT.get(), context -> new ModBoatRenderer(context, true));
    EntityRendererRegistry.register(ModEntities.RABBAGE.get(), ThrownItemRenderer::new);
    EntityRendererRegistry.register(ModEntities.PURIFYING_SAC.get(), ThrownItemRenderer::new);
    EntityRendererRegistry.register(ModEntities.FOUL_SAC.get(), ThrownItemRenderer::new);
    EntityRendererRegistry.register(ModEntities.FROST_SAC.get(), ThrownItemRenderer::new);
    EntityRendererRegistry.register(ModEntities.SEARING_SAC.get(), ThrownItemRenderer::new);
    EntityRendererRegistry.register(ModEntities.THORN_ARROW.get(), ThornArrowRenderer::new);
    EntityRendererRegistry.register(ModEntities.CINDERHEW.get(), CinderhewProjectileRenderer::new);
    MenuScreens.register(ModMenuTypes.NESTING_BLOCK.get(), NestingBlockScreen::new);
    ParticleFactoryRegistry particleFactories = ParticleFactoryRegistry.getInstance();
    particleFactories.register(ModParticleTypes.SPORE.get(), SporeParticle.Factory::new);
    particleFactories.register(ModParticleTypes.SPARKLE.get(), SparkleParticle.Factory::new);
    particleFactories.register(ModParticleTypes.LEAVES.get(), LeavesParticle.Factory::new);
    particleFactories.register(
        ModParticleTypes.INFUSED_BUBBLES.get(), InfusedBubbleParticle.Factory::new);
    particleFactories.register(
        ModParticleTypes.CACOFEY_DUST.get(), CacofeyDustParticle.Factory::new);
    particleFactories.register(
        ModParticleTypes.CACOFEY_DUST_HELD.get(), CacofeyDustHeldParticle.Factory::new);
    ModItemProperties.register();
    registerTooltips();
    registerBoatLayers();
  }

  private static void registerTooltips() {
    ItemTooltipCallback.EVENT.register(
        (stack, context, type, tooltip) -> {
          if (Minecraft.getInstance().player != null) {
            MagicResistanceTooltip.addFullSetLineIfWorn(
                Minecraft.getInstance().player, stack, tooltip);
          }
        });
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

  private static void registerWoodTypes() {
    WoodTypeInvoker.hexalia$register(ModWoodTypes.COTTONWOOD);
    WoodTypeInvoker.hexalia$register(ModWoodTypes.WILLOW);
    Sheets.SIGN_MATERIALS.put(
        ModWoodTypes.COTTONWOOD, Sheets.getSignMaterial(ModWoodTypes.COTTONWOOD));
    Sheets.SIGN_MATERIALS.put(ModWoodTypes.WILLOW, Sheets.getSignMaterial(ModWoodTypes.WILLOW));
    Sheets.HANGING_SIGN_MATERIALS.put(
        ModWoodTypes.COTTONWOOD, Sheets.getHangingSignMaterial(ModWoodTypes.COTTONWOOD));
    Sheets.HANGING_SIGN_MATERIALS.put(
        ModWoodTypes.WILLOW, Sheets.getHangingSignMaterial(ModWoodTypes.WILLOW));
  }

  private static void registerLeafColors() {
    ColorProviderRegistry.BLOCK.register(
        (state, level, pos, tintIndex) ->
            level == null || pos == null
                ? FoliageColor.getDefaultColor()
                : BiomeColors.getAverageFoliageColor(level, pos),
        ModBlocks.COTTONWOOD_LEAVES.get(),
        ModBlocks.WILLOW_LEAVES.get());
    ColorProviderRegistry.ITEM.register(
        (stack, tintIndex) -> FoliageColor.getDefaultColor(),
        ModItems.COTTONWOOD_LEAVES.get(),
        ModItems.WILLOW_LEAVES.get());
  }

  private static void registerCutoutBlocks() {
    RenderType cutout = RenderType.cutout();
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SILKWORM_COCOON.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RITUAL_BRAZIER.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SMALL_CAULDRON.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CENSER.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.HERB_JAR.get(), RenderType.translucent());
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DREAMCATCHER.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CANDLE_SKULL.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WITHER_CANDLE_SKULL.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MORPHORA.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POTTED_MORPHORA.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GRIMSHADE.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POTTED_GRIMSHADE.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.NAUTILITE.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WINDSONG.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POTTED_WINDSONG.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ASTRYLIS.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POTTED_ASTRYLIS.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOURDES.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POTTED_LOURDES.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.AEGIFLORA.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POTTED_AEGIFLORA.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WITHERED_AEGIFLORA.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POTTED_WITHERED_AEGIFLORA.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BEGONIA.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POTTED_BEGONIA.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LAVENDER.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POTTED_LAVENDER.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DAHLIA.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POTTED_DAHLIA.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.NIGHTSHADE_BUSH.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POTTED_NIGHTSHADE_BUSH.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SPIRIT_BLOOM.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POTTED_SPIRIT_BLOOM.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DREAMSHROOM.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POTTED_DREAMSHROOM.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PALE_MUSHROOM.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SIREN_KELP.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GHOST_FERN.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POTTED_GHOST_FERN.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CELESTIAL_BLOOM.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POTTED_CELESTIAL_BLOOM.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WITHERED_CELESTIAL_BLOOM.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POTTED_WITHERED_CELESTIAL_BLOOM.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOTUS_FLOWER.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WITCHWEED.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MANDRAKE_CROP.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SUNFIRE_TOMATO_CROP.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RABBAGE_CROP.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WILD_MANDRAKE.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WILD_SUNFIRE_TOMATO.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CHILLBERRY_BUSH.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SALTSPROUT.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GALEBERRIES_VINE.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GALEBERRIES_VINE_PLANT.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SALT_LAMP.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.COTTONWOOD_CATKIN.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.COTTONWOOD_SAPLING.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POTTED_COTTONWOOD_SAPLING.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.COTTONWOOD_TRAPDOOR.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.COTTONWOOD_DOOR.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WILLOW_SAPLING.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POTTED_WILLOW_SAPLING.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WILLOW_TRAPDOOR.get(), cutout);
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WILLOW_DOOR.get(), cutout);
  }

  private static void registerBoatLayers() {
    EntityModelLayerRegistry.registerModelLayer(
        PestleModel.LAYER_LOCATION, PestleModel::createLayer);
    for (ModBoatEntity.Type type : ModBoatEntity.Type.values()) {
      EntityModelLayerRegistry.registerModelLayer(
          ModBoatRenderer.createBoatModelName(type), BoatModel::createBodyModel);
      EntityModelLayerRegistry.registerModelLayer(
          ModBoatRenderer.createChestBoatModelName(type), ChestBoatModel::createBodyModel);
    }
  }
}
