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
import net.astralya.hexalia.event.GravebloomEventHandler;
import net.astralya.hexalia.event.CinderhewEventHandler;
import net.astralya.hexalia.event.AccessoryEffectHandler;
import net.astralya.hexalia.event.NaturesRitualSoulEvents;
import net.astralya.hexalia.gameplay.censer.CenserServerTickHandler;
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

public class HexaliaMod implements ModInitializer {

	public static final String MODID = "hexalia";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		registerCore();
		registerContent();
		registerGameplay();
		registerAttributes();
	}

	private static void registerCore() {
		ModRegistries.registerModStuff();
		Configuration.register();
	}

	private static void registerContent() {
		ModItems.registerModItems();
		ModCreativeModeTabs.registerItemGroups();
		ModMobEffects.register();
		ModBlocks.registerModBlocks();
		ModBlockProperties.register();
		ModParticleType.registerParticles();
		ModSoundEvents.registerSounds();
		ModBlockEntityTypes.registerBlockEntities();
		ModEntities.registerModEntities();
		ModRecipes.registerRecipes();
		ModMenuTypes.registerScreenHandlers();
		ModArmorMaterials.registerModArmorMaterials();
		ModWorldGeneration.generateModWorldGeneration();
		ModFeatures.register();
		ModLootTableModifiers.register();
		ModBoats.registerBoats();
	}

	private static void registerGameplay() {
		InfusedDirtBlock.init();
		ModVanillaBehaviors.register();
		CenserServerTickHandler.register();
		RootshaperEventHandler.register();
		GravebloomEventHandler.register();
		CinderhewEventHandler.register();
		AccessoryEffectHandler.register();
		NaturesRitualSoulEvents.register();
	}

	private static void registerAttributes() {
		FabricDefaultAttributeRegistry.register(ModEntities.SILK_MOTH_ENTITY, SilkMothEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.CACOFEY_ENTITY, CacofeyEntity.setAttributes());
	}
}
