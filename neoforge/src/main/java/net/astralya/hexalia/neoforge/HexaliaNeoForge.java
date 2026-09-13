package net.astralya.hexalia.neoforge;

import net.astralya.hexalia.Hexalia;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.block.entity.custom.MortarAndPestleBlockEntity;
import net.astralya.hexalia.entity.ModEntities;
import net.astralya.hexalia.entity.custom.CacofeyEntity;
import net.astralya.hexalia.entity.custom.SilkMothEntity;
import net.astralya.hexalia.neoforge.event.NeoForgeArmorEvents;
import net.astralya.hexalia.neoforge.event.NeoForgeGreenOmenEvents;
import net.astralya.hexalia.neoforge.event.NeoForgeSagePendantEvents;
import net.astralya.hexalia.util.ModVanillaBehaviors;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;

@Mod(Hexalia.MOD_ID)
public final class HexaliaNeoForge {
  public HexaliaNeoForge(IEventBus modEventBus, ModContainer modContainer) {
    HexaliaNeoForgeConfig.init(modContainer, modEventBus);
    Hexalia.init();
    NeoForgeArmorEvents.register();
    NeoForgeGreenOmenEvents.register();
    NeoForgeSagePendantEvents.register();
    modEventBus.addListener(HexaliaNeoForge::commonSetup);
    modEventBus.addListener(HexaliaNeoForge::registerAttributes);
    modEventBus.addListener(HexaliaNeoForge::registerCapabilities);
    modEventBus.addListener(HexaliaNeoForge::registerSpawnPlacements);
    if (FMLEnvironment.dist == Dist.CLIENT) {
      HexaliaNeoForgeClient.init(modEventBus);
    }
  }

  private static void commonSetup(FMLCommonSetupEvent event) {
    event.enqueueWork(ModVanillaBehaviors::register);
  }

  private static void registerAttributes(EntityAttributeCreationEvent event) {
    event.put(ModEntities.SILK_MOTH.get(), SilkMothEntity.setAttributes());
    event.put(ModEntities.CACOFEY.get(), CacofeyEntity.setAttributes());
  }

  private static void registerCapabilities(RegisterCapabilitiesEvent event) {
    event.registerBlockEntity(
        Capabilities.ItemHandler.BLOCK,
        ModBlockEntityTypes.MORTAR_AND_PESTLE.get(),
        (mortar, side) -> new SidedInvWrapper(mortar, side));
    event.registerBlockEntity(
        Capabilities.ItemHandler.BLOCK,
        ModBlockEntityTypes.HERB_JAR.get(),
        (herbJar, side) -> new SidedInvWrapper(herbJar, side));
  }

  private static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
    event.register(
        ModEntities.SILK_MOTH.get(),
        SpawnPlacementTypes.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        Animal::checkAnimalSpawnRules,
        RegisterSpawnPlacementsEvent.Operation.REPLACE);
    event.register(
        ModEntities.CACOFEY.get(),
        SpawnPlacementTypes.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        Animal::checkAnimalSpawnRules,
        RegisterSpawnPlacementsEvent.Operation.REPLACE);
  }
}
