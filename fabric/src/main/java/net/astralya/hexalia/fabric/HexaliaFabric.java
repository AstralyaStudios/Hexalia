package net.astralya.hexalia.fabric;

import net.astralya.hexalia.Hexalia;
import net.astralya.hexalia.entity.ModEntities;
import net.astralya.hexalia.entity.custom.CacofeyEntity;
import net.astralya.hexalia.entity.custom.SilkMothEntity;
import net.astralya.hexalia.fabric.event.FabricAccessoryLootEvents;
import net.astralya.hexalia.fabric.worldgen.FabricEntitySpawns;
import net.astralya.hexalia.fabric.worldgen.FabricNaturalGeneration;
import net.astralya.hexalia.util.ModVanillaBehaviors;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public final class HexaliaFabric implements ModInitializer {
  @Override
  public void onInitialize() {
    HexaliaFabricConfig.init();
    Hexalia.init();
    FabricDefaultAttributeRegistry.register(ModEntities.SILK_MOTH.get(), SilkMothEntity.setAttributes());
    FabricDefaultAttributeRegistry.register(ModEntities.CACOFEY.get(), CacofeyEntity.setAttributes());
    FabricEntitySpawns.register();
    FabricNaturalGeneration.register();
    FabricAccessoryLootEvents.register();
    ModVanillaBehaviors.register();
  }
}
