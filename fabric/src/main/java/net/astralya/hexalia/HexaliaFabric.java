package net.astralya.hexalia;

import net.astralya.hexalia.block.ModBlockProperties;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.block.custom.InfusedDirtBlock;
import net.astralya.hexalia.effect.ModMobEffects;
import net.astralya.hexalia.entity.ModBoats;
import net.astralya.hexalia.entity.ModEntities;
import net.astralya.hexalia.entity.custom.CacofeyEntity;
import net.astralya.hexalia.entity.custom.SilkMothEntity;
import net.astralya.hexalia.event.RootshaperEventHandler;
import net.astralya.hexalia.event.ModGameEvents;
import net.astralya.hexalia.gameplay.censer.CenserServerTickHandler;
import net.astralya.hexalia.gameplay.WindsongServerTickHandler;
import net.astralya.hexalia.item.ModCreativeModeTabs;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.loot.ModLootTableModifiers;
import net.astralya.hexalia.menu.ModMenuTypes;
import net.astralya.hexalia.particle.ModParticleType;
import net.astralya.hexalia.recipe.ModRecipes;
import net.astralya.hexalia.sound.ModSoundEvents;
import net.astralya.hexalia.util.ModArmorMaterials;
import net.astralya.hexalia.util.ModRegistries;
import net.astralya.hexalia.util.ModVanillaBehaviors;
import net.astralya.hexalia.worldgen.ModFeatures;
import net.astralya.hexalia.worldgen.gen.ModWorldGeneration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HexaliaFabric implements ModInitializer {

	@Override
	public void onInitialize() {
		registerCore();
		registerContent();
		registerGameplay();
		registerAttributes();
	}

	private static void registerCore() {
		HexaliaMod.init();
		if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("trinkets")) {
			net.astralya.hexalia.fabric.integration.TrinketsIntegration.register();
		}
		if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("accessories")) {
			net.astralya.hexalia.integration.accessories.AccessoriesCompat.register();
		}
		FabricConfiguration.register();
	}

	private static void registerContent() {
		ModCreativeModeTabs.registerCreativeModeTabs();
		ModBlockProperties.register();
		ModWorldGeneration.generateModLevelGeneration();
		ModLootTableModifiers.register();
		ModBoats.registerBoats();
	}

	private static void registerGameplay() {
		ModGameEvents.register();
		ModVanillaBehaviors.register();
		CenserServerTickHandler.register();
		WindsongServerTickHandler.register();
		RootshaperEventHandler.register();
	}

	private static void registerAttributes() {
		FabricDefaultAttributeRegistry.register(ModEntities.SILK_MOTH_ENTITY.get(), SilkMothEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.CACOFEY_ENTITY.get(), CacofeyEntity.setAttributes());
	}
}
