package net.astralya.hexalia.event;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.client.renderer.blockentity.*;
import net.astralya.hexalia.particle.ModParticleType;
import net.astralya.hexalia.particle.custom.*;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HexaliaMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetUpEvents {
    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticleType.SPORE.get(), SporeParticle.Factory::new);
        event.registerSpriteSet(ModParticleType.GHOST.get(), GhostParticle.Factory::new);
        event.registerSpriteSet(ModParticleType.INFUSED_BUBBLES.get(), InfusedBubbleParticle.Provider::new);
        event.registerSpriteSet(ModParticleType.LEAVES.get(), LeavesParticle.Provider::new);
        event.registerSpriteSet(ModParticleType.SPARKLE.get(), SparkleParticle.Factory::new);
        event.registerSpriteSet(ModParticleType.CACOFEY_DUST.get(), CacofeyDustParticle.Factory::new);
        event.registerSpriteSet(ModParticleType.CACOFEY_DUST_HELD.get(), CacofeyDustParticle.Factory::new);
    }

    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntityTypes.RITUAL_TABLE.get(), RitualTableBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypes.RITUAL_BRAZIER.get(), RitualBrazierBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypes.HERB_JAR.get(), HerbJarBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypes.SHELF.get(), ShelfBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypes.CENSER.get(), CenserBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypes.MORTAR_AND_PESTLE.get(), MortarAndPestleBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypes.SMALL_CAULDRON.get(), SmallCauldronBlockEntityRenderer::new);

        event.registerBlockEntityRenderer(ModBlockEntityTypes.MOD_SIGN.get(), SignRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypes.MOD_HANGING_SIGN.get(), HangingSignRenderer::new);
    }

    @SubscribeEvent
    public static void registerColoredBlock(RegisterColorHandlersEvent.Block event) {
        event.register((pState, pLevel, pPos, pTintIndex) -> pLevel != null && pPos != null
        ? BiomeColors.getAverageFoliageColor(pLevel, pPos) : FoliageColor.getDefaultColor(),
                ModBlocks.COTTONWOOD_LEAVES.get(), ModBlocks.WILLOW_LEAVES.get());
    }

    @SubscribeEvent
    public static void registerColoredItems(RegisterColorHandlersEvent.Item event) {
        event.register((pStack, pTintIndex) -> {
            BlockState state = ((BlockItem)pStack.getItem()).getBlock().defaultBlockState();
            return event.getBlockColors().getColor(state, null, null, pTintIndex);
        }, ModBlocks.COTTONWOOD_LEAVES.get(), ModBlocks.WILLOW_LEAVES.get());
    }
}
