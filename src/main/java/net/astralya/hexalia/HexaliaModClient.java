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
import net.astralya.hexalia.particle.custom.InfusedBubbleParticle;
import net.astralya.hexalia.particle.custom.LeavesParticle;
import net.astralya.hexalia.particle.custom.SparkleParticle;
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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.block.entity.HangingSignBlockEntityRenderer;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.color.world.FoliageColors;

public class HexaliaModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        registerModelLayers();
        registerBlockRenderLayers();
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
                PestleModel.LAYER_LOCATION, PestleModel::createModelData);
    }

    private static void registerBlockRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putBlocks(
                RenderLayer.getCutout(),
                ModBlocks.SPIRIT_BLOOM,
                ModBlocks.POTTED_SPIRIT_BLOOM,
                ModBlocks.DREAMSHROOM,
                ModBlocks.POTTED_DREAMSHROOM,
                ModBlocks.SIREN_KELP,
                ModBlocks.MANDRAKE_CROP,
                ModBlocks.CHILLBERRY_BUSH,
                ModBlocks.SUNFIRE_TOMATO_CROP,
                ModBlocks.WILD_MANDRAKE,
                ModBlocks.WILD_SUNFIRE_TOMATO,
                ModBlocks.RABBAGE_CROP,
                ModBlocks.DREAMCATCHER,
                ModBlocks.CANDLE_SKULL,
                ModBlocks.SALT_LAMP,
                ModBlocks.SILKWORM_COCOON,
                ModBlocks.EGG_CLUSTER,
                ModBlocks.LOTUS_FLOWER,
                ModBlocks.PALE_MUSHROOM,
                ModBlocks.WITCHWEED,
                ModBlocks.GHOST_FERN,
                ModBlocks.NIGHTSHADE_BUSH,
                ModBlocks.POTTED_NIGHTSHADE_BUSH,
                ModBlocks.SALTSPROUT,
                ModBlocks.GALEBERRIES_VINE,
                ModBlocks.GALEBERRIES_VINE_PLANT,
                ModBlocks.GRIMSHADE,
                ModBlocks.POTTED_GRIMSHADE,
                ModBlocks.BEGONIA,
                ModBlocks.POTTED_BEGONIA,
                ModBlocks.POTTED_MORPHORA,
                ModBlocks.RITUAL_BRAZIER,
                ModBlocks.HERB_JAR,
                ModBlocks.MORPHORA,
                ModBlocks.LAVENDER,
                ModBlocks.POTTED_LAVENDER,
                ModBlocks.NAUTILITE,
                ModBlocks.WINDSONG,
                ModBlocks.ASTRYLIS,
                ModBlocks.POTTED_WINDSONG,
                ModBlocks.POTTED_ASTRYLIS,
                ModBlocks.WITHER_CANDLE_SKULL,
                ModBlocks.DAHLIA,
                ModBlocks.POTTED_DAHLIA,
                ModBlocks.CELESTIAL_BLOOM,
                ModBlocks.POTTED_CELESTIAL_BLOOM,
                ModBlocks.WITHERED_CELESTIAL_BLOOM,
                ModBlocks.POTTED_WITHERED_CELESTIAL_BLOOM,
                ModBlocks.POTTED_GHOST_FERN,
                ModBlocks.LOURDES,
                ModBlocks.POTTED_LOURDES,
                ModBlocks.AEGIFLORA,
                ModBlocks.POTTED_AEGIFLORA,
                ModBlocks.WITHERED_AEGIFLORA,
                ModBlocks.POTTED_WITHERED_AEGIFLORA,
                ModBlocks.NESTING_BLOCK,
                ModBlocks.COTTONWOOD_SAPLING,
                ModBlocks.POTTED_COTTONWOOD_SAPLING,
                ModBlocks.WILLOW_SAPLING,
                ModBlocks.POTTED_WILLOW_SAPLING,
                ModBlocks.COTTONWOOD_TRAPDOOR,
                ModBlocks.COTTONWOOD_DOOR,
                ModBlocks.COTTONWOOD_CATKIN,
                ModBlocks.WILLOW_TRAPDOOR,
                ModBlocks.WILLOW_DOOR
        );
    }

    private static void registerParticles() {
        ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();
        registry.register(ModParticleType.SPORE, SporeParticle.Factory::new);
        registry.register(ModParticleType.INFUSED_BUBBLE, InfusedBubbleParticle.Factory::new);
        registry.register(ModParticleType.GHOST, GhostParticle.Factory::new);
        registry.register(ModParticleType.LEAVES, LeavesParticle.Factory::new);
        registry.register(ModParticleType.SPARKLE, SparkleParticle.Factory::new);
        registry.register(ModParticleType.CACOFEY_DUST, CacofeyDustParticle.Factory::new);
        registry.register(ModParticleType.CACOFEY_DUST_HELD, CacofeyDustHeldParticle.Factory::new);
    }

    private static void registerColorProviders() {
        ColorProviderRegistry.BLOCK.register(
                (state, world, pos, tintIndex) -> world != null && pos != null
                        ? BiomeColors.getFoliageColor(world, pos)
                        : FoliageColors.getDefaultColor(),
                ModBlocks.COTTONWOOD_LEAVES,
                ModBlocks.WILLOW_LEAVES
        );

        ColorProviderRegistry.ITEM.register(
                (stack, tintIndex) -> FoliageColors.getDefaultColor(),
                ModBlocks.COTTONWOOD_LEAVES,
                ModBlocks.WILLOW_LEAVES
        );
    }

    private static void registerWoodTypes() {
        TexturedRenderLayers.SIGN_TYPE_TEXTURES.put(ModWoodTypes.WILLOW_WOOD_TYPE, TexturedRenderLayers.getSignTextureId(ModWoodTypes.WILLOW_WOOD_TYPE));
        TexturedRenderLayers.SIGN_TYPE_TEXTURES.put(ModWoodTypes.COTTONWOOD_WOOD_TYPE, TexturedRenderLayers.getSignTextureId(ModWoodTypes.COTTONWOOD_WOOD_TYPE));
        TerraformBoatClientHelper.registerModelLayers(ModBoats.WILLOW_BOAT_ID, false);
        TerraformBoatClientHelper.registerModelLayers(ModBoats.COTTONWOOD_BOAT_ID, false);
    }

    private static void registerBlockEntityRenderers() {
        BlockEntityRendererFactories.register(ModBlockEntityTypes.MOD_SIGN, SignBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntityTypes.MOD_HANGING_SIGN, HangingSignBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntityTypes.RITUAL_BRAZIER, RitualBrazierBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntityTypes.HERB_JAR, HerbJarBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntityTypes.SHELF, ShelfBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntityTypes.CENSER, CenserBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntityTypes.RITUAL_TABLE, RitualTableBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntityTypes.MORTAR_AND_PESTLE, MortarAndPestleBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntityTypes.SMALL_CAULDRON, SmallCauldronBlockEntityRenderer::new);
    }

    private static void registerEntityRenderers() {
        EntityRendererRegistry.register(ModEntities.RABBAGE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.PURIFYING_SAC, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.FOUL_SAC, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.FROST_SAC, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.SEARING_SAC, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.THORN_ARROW, ThornArrowRenderer::new);
        EntityRendererRegistry.register(ModEntities.CINDERHEW, CinderhewProjectileRenderer::new);
        EntityRendererRegistry.register(ModEntities.SILK_MOTH_ENTITY, SilkMothRenderer::new);
        EntityRendererRegistry.register(ModEntities.CACOFEY_ENTITY, CacofeyRenderer::new);
    }

    private static void registerScreens() {
        HandledScreens.register(ModMenuTypes.NESTING_BLOCK_MENU, NestingBlockScreen::new);
    }

    private static void registerTooltips() {
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            if (!MagicResistanceTooltip.hasMagicResist(stack)) {
                return;
            }
            MagicResistanceTooltip.addPieceLine(stack, lines);
            PlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null) {
                MagicResistanceTooltip.addFullSetLineIfWorn(player, stack, lines);
            }
        });
    }

    private static void registerItemProperties() {
        ModItemProperties.addCustomItemProperties();
    }
}
