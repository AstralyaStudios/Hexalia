package net.astralya.hexalia;

import com.terraformersmc.terraform.boat.api.client.TerraformBoatClientHelper;
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
import net.astralya.hexalia.client.renderer.entity.SilkMothRenderer;
import net.astralya.hexalia.client.renderer.entity.ThornArrowRenderer;
import net.astralya.hexalia.client.screen.NestingBlockScreen;
import net.astralya.hexalia.entity.ModBoats;
import net.astralya.hexalia.entity.ModEntities;
import net.astralya.hexalia.menu.ModMenuTypes;
import net.astralya.hexalia.particle.ModParticleType;
import net.astralya.hexalia.particle.custom.CacofeyDustHeldParticle;
import net.astralya.hexalia.particle.custom.CacofeyDustParticle;
import net.astralya.hexalia.particle.custom.GhostParticle;
import net.astralya.hexalia.particle.custom.HexMoteParticle;
import net.astralya.hexalia.particle.custom.InfusedBubbleParticle;
import net.astralya.hexalia.particle.custom.LeavesParticle;
import net.astralya.hexalia.particle.custom.SparkleParticle;
import net.astralya.hexalia.particle.custom.RitualGlyphParticle;
import net.astralya.hexalia.particle.custom.SporeParticle;
import net.astralya.hexalia.util.MagicResistanceTooltip;
import net.astralya.hexalia.util.ModItemProperties;
import net.astralya.hexalia.util.ModWoodTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.FoliageColor;

public class HexaliaModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        registerModelLayers();
        registerBlockRenderTypes();
        registerParticles();
        registerColorProviders();
        registerWoodTypes();
        registerBlockEntityRenderers();
        registerEntityRenderers();
        registerScreens();
        registerTooltips();
        registerItemProperties();
    }

    private static void registerModelLayers() {
        EntityModelLayerRegistry.registerModelLayer(
                PestleModel.LAYER_LOCATION, PestleModel::createLayer);
    }

    private static void registerBlockRenderTypes() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.HERB_JAR.get(), RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlocks(
                RenderType.cutout(),
                ModBlocks.SPIRIT_BLOOM.get(),
                ModBlocks.POTTED_SPIRIT_BLOOM.get(),
                ModBlocks.DREAMSHROOM.get(),
                ModBlocks.POTTED_DREAMSHROOM.get(),
                ModBlocks.SIREN_KELP.get(),
                ModBlocks.MANDRAKE_CROP.get(),
                ModBlocks.CHILLBERRY_BUSH.get(),
                ModBlocks.SUNFIRE_TOMATO_CROP.get(),
                ModBlocks.WILD_MANDRAKE.get(),
                ModBlocks.WILD_SUNFIRE_TOMATO.get(),
                ModBlocks.RABBAGE_CROP.get(),
                ModBlocks.DREAMCATCHER.get(),
                ModBlocks.CANDLE_SKULL.get(),
                ModBlocks.SALT_LAMP.get(),
                ModBlocks.SILKWORM_COCOON.get(),
                ModBlocks.EGG_CLUSTER.get(),
                ModBlocks.LOTUS_FLOWER.get(),
                ModBlocks.PALE_MUSHROOM.get(),
                ModBlocks.WITCHWEED.get(),
                ModBlocks.GHOST_FERN.get(),
                ModBlocks.NIGHTSHADE_BUSH.get(),
                ModBlocks.POTTED_NIGHTSHADE_BUSH.get(),
                ModBlocks.SALTSPROUT.get(),
                ModBlocks.GALEBERRIES_VINE.get(),
                ModBlocks.GALEBERRIES_VINE_PLANT.get(),
                ModBlocks.GRIMSHADE.get(),
                ModBlocks.POTTED_GRIMSHADE.get(),
                ModBlocks.BEGONIA.get(),
                ModBlocks.POTTED_BEGONIA.get(),
                ModBlocks.POTTED_MORPHORA.get(),
                ModBlocks.RITUAL_BRAZIER.get(),
                ModBlocks.MORPHORA.get(),
                ModBlocks.LAVENDER.get(),
                ModBlocks.POTTED_LAVENDER.get(),
                ModBlocks.NAUTILITE.get(),
                ModBlocks.WINDSONG.get(),
                ModBlocks.ASTRYLIS.get(),
                ModBlocks.POTTED_WINDSONG.get(),
                ModBlocks.POTTED_ASTRYLIS.get(),
                ModBlocks.WITHER_CANDLE_SKULL.get(),
                ModBlocks.DAHLIA.get(),
                ModBlocks.POTTED_DAHLIA.get(),
                ModBlocks.CELESTIAL_BLOOM.get(),
                ModBlocks.POTTED_CELESTIAL_BLOOM.get(),
                ModBlocks.WITHERED_CELESTIAL_BLOOM.get(),
                ModBlocks.POTTED_WITHERED_CELESTIAL_BLOOM.get(),
                ModBlocks.POTTED_GHOST_FERN.get(),
                ModBlocks.LOURDES.get(),
                ModBlocks.POTTED_LOURDES.get(),
                ModBlocks.AEGIFLORA.get(),
                ModBlocks.POTTED_AEGIFLORA.get(),
                ModBlocks.WITHERED_AEGIFLORA.get(),
                ModBlocks.POTTED_WITHERED_AEGIFLORA.get(),
                ModBlocks.NESTING_BLOCK.get(),
                ModBlocks.COTTONWOOD_SAPLING.get(),
                ModBlocks.POTTED_COTTONWOOD_SAPLING.get(),
                ModBlocks.WILLOW_SAPLING.get(),
                ModBlocks.POTTED_WILLOW_SAPLING.get(),
                ModBlocks.COTTONWOOD_TRAPDOOR.get(),
                ModBlocks.COTTONWOOD_DOOR.get(),
                ModBlocks.COTTONWOOD_CATKIN.get(),
                ModBlocks.WILLOW_TRAPDOOR.get(),
                ModBlocks.WILLOW_DOOR.get()
        );
    }

    private static void registerParticles() {
        ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();
        registry.register(ModParticleType.SPORE.get(), SporeParticle.Factory::new);
        registry.register(ModParticleType.INFUSED_BUBBLES.get(), InfusedBubbleParticle.Provider::new);
        registry.register(ModParticleType.GHOST.get(), GhostParticle.Factory::new);
        registry.register(ModParticleType.LEAVES.get(), LeavesParticle.Factory::new);
        registry.register(ModParticleType.SPARKLE.get(), SparkleParticle.Factory::new);
        registry.register(ModParticleType.CACOFEY_DUST.get(), CacofeyDustParticle.Factory::new);
        registry.register(ModParticleType.HEX_MOTES.get(), HexMoteParticle.Factory::new);
        registry.register(ModParticleType.RITUAL_GLYPH.get(), RitualGlyphParticle.Factory::new);
        registry.register(ModParticleType.CELESTIAL_GLYPH.get(), RitualGlyphParticle.Factory::new);
        registry.register(ModParticleType.SUMMONING_GLYPH.get(), RitualGlyphParticle.Factory::new);
        registry.register(ModParticleType.CACOFEY_DUST_HELD.get(), CacofeyDustHeldParticle.Factory::new);
    }

    private static void registerColorProviders() {
        ColorProviderRegistry.BLOCK.register(
                (state, world, pos, tintIndex) -> world != null && pos != null
                        ? BiomeColors.getAverageFoliageColor(world, pos)
                        : FoliageColor.getDefaultColor(),
                ModBlocks.COTTONWOOD_LEAVES.get(),
                ModBlocks.WILLOW_LEAVES.get()
        );

        ColorProviderRegistry.ITEM.register(
                (stack, tintIndex) -> FoliageColor.getDefaultColor(),
                ModBlocks.COTTONWOOD_LEAVES.get(),
                ModBlocks.WILLOW_LEAVES.get()
        );
    }

    private static void registerWoodTypes() {
        TerraformBoatClientHelper.registerModelLayers(ModBoats.WILLOW_BOAT_ID, false);
        TerraformBoatClientHelper.registerModelLayers(ModBoats.COTTONWOOD_BOAT_ID, false);
    }

    private static void registerBlockEntityRenderers() {
        BlockEntityRenderers.register(ModBlockEntityTypes.MOD_SIGN.get(), SignRenderer::new);
        BlockEntityRenderers.register(ModBlockEntityTypes.MOD_HANGING_SIGN.get(), HangingSignRenderer::new);
        BlockEntityRenderers.register(ModBlockEntityTypes.RITUAL_BRAZIER.get(), RitualBrazierBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntityTypes.SHELF.get(), ShelfBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntityTypes.HERB_JAR.get(), HerbJarBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntityTypes.CENSER.get(), CenserBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntityTypes.RITUAL_TABLE.get(), RitualTableBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntityTypes.MORTAR_AND_PESTLE.get(), MortarAndPestleBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntityTypes.SMALL_CAULDRON.get(), SmallCauldronBlockEntityRenderer::new);
    }

    private static void registerEntityRenderers() {
        EntityRendererRegistry.register(ModEntities.RABBAGE.get(), ThrownItemRenderer::new);
        EntityRendererRegistry.register(ModEntities.PURIFYING_SAC.get(), ThrownItemRenderer::new);
        EntityRendererRegistry.register(ModEntities.FOUL_SAC.get(), ThrownItemRenderer::new);
        EntityRendererRegistry.register(ModEntities.FROST_SAC.get(), ThrownItemRenderer::new);
        EntityRendererRegistry.register(ModEntities.SEARING_SAC.get(), ThrownItemRenderer::new);
        EntityRendererRegistry.register(ModEntities.THORN_ARROW.get(), ThornArrowRenderer::new);
        EntityRendererRegistry.register(ModEntities.CINDERHEW.get(), CinderhewProjectileRenderer::new);
        EntityRendererRegistry.register(ModEntities.SILK_MOTH_ENTITY.get(), SilkMothRenderer::new);
        EntityRendererRegistry.register(ModEntities.CACOFEY_ENTITY.get(), CacofeyRenderer::new);
    }

    private static void registerScreens() {
        MenuScreens.register(ModMenuTypes.NESTING_BLOCK_MENU.get(), NestingBlockScreen::new);
    }

    private static void registerTooltips() {
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            if (!MagicResistanceTooltip.hasMagicResist(stack)) {
                return;
            }
            MagicResistanceTooltip.addPieceLine(stack, lines);
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                MagicResistanceTooltip.addFullSetLineIfWorn(player, stack, lines);
            }
        });
    }

    private static void registerItemProperties() {
        ModItemProperties.addCustomItemProperties();
    }
}
