package net.astralya.hexalia.event;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.client.model.PestleModel;
import net.astralya.hexalia.client.renderer.entity.CacofeyRenderer;
import net.astralya.hexalia.client.renderer.entity.CinderhewProjectileRenderer;
import net.astralya.hexalia.client.renderer.entity.ModBoatRenderer;
import net.astralya.hexalia.client.renderer.entity.SilkMothRenderer;
import net.astralya.hexalia.client.renderer.entity.ThornArrowRenderer;
import net.astralya.hexalia.client.screen.NestingBlockScreen;
import net.astralya.hexalia.entity.ModEntities;
import net.astralya.hexalia.entity.layers.ModModelLayers;
import net.astralya.hexalia.menu.ModMenuTypes;
import net.astralya.hexalia.util.ModItemProperties;
import net.astralya.hexalia.util.ModWoodTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = HexaliaMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(ModMenuTypes.NESTING_BLOCK_MENU.get(), NestingBlockScreen::new);

        EntityRenderers.register(ModEntities.RABBAGE.get(), ThrownItemRenderer::new);
        EntityRenderers.register(ModEntities.PURIFYING_SAC.get(), ThrownItemRenderer::new);
        EntityRenderers.register(ModEntities.FOUL_SAC.get(), ThrownItemRenderer::new);
        EntityRenderers.register(ModEntities.FROST_SAC.get(), ThrownItemRenderer::new);
        EntityRenderers.register(ModEntities.SEARING_SAC.get(), ThrownItemRenderer::new);
        EntityRenderers.register(ModEntities.MOD_BOAT.get(), context -> new ModBoatRenderer(context, false));
        EntityRenderers.register(ModEntities.MOD_CHEST_BOAT.get(), context -> new ModBoatRenderer(context, true));
        EntityRenderers.register(ModEntities.THORN_ARROW.get(), ThornArrowRenderer::new);
        EntityRenderers.register(ModEntities.CINDERHEW.get(), CinderhewProjectileRenderer::new);
        EntityRenderers.register(ModEntities.SILK_MOTH_ENTITY.get(), SilkMothRenderer::new);
        EntityRenderers.register(ModEntities.CACOFEY_ENTITY.get(), CacofeyRenderer::new);

        Sheets.addWoodType(ModWoodTypes.COTTONWOOD);
        Sheets.addWoodType(ModWoodTypes.WILLOW);

        event.enqueueWork(ModItemProperties::addCustomItemProperties);
        event.enqueueWork(() -> ItemBlockRenderTypes.setRenderLayer(ModBlocks.HERB_JAR.get(), RenderType.translucent()));
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(PestleModel.LAYER_LOCATION, PestleModel::createLayer);
        event.registerLayerDefinition(ModModelLayers.COTTONWOOD_BOAT_LAYER, BoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayers.COTTONWOOD_CHEST_BOAT_LAYER, ChestBoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayers.WILLOW_BOAT_LAYER, BoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayers.WILLOW_CHEST_BOAT_LAYER, ChestBoatModel::createBodyModel);
    }

}
