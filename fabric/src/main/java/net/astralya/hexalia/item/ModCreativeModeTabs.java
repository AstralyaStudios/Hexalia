package net.astralya.hexalia.item;

import net.astralya.hexalia.util.ModUtil;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.ModBlocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ModCreativeModeTabs {

    public static final CreativeModeTab HEXALIA = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            new ResourceLocation(HexaliaMod.MODID, "hexalia"),
            FabricItemGroup.builder().title(Component.translatable("itemGroup.hexalia"))
                    .icon(() -> new ItemStack(ModItems.HEX_FOCUS.get())).displayItems((displayContext, entries) -> {
                        acceptHerbsAndFlora(entries);
                        acceptProcessedIngredients(entries);
                        acceptSeedsAndCrops(entries);
                        acceptFood(entries);
                        acceptBrewsAndAlchemy(entries);
                        acceptMagicComponents(entries);
                        acceptToolsAndRelics(entries);
                        acceptWearables(entries);
                        acceptFunctionalBlocks(entries);
                        acceptDecor(entries);
                        acceptRareItems(entries);
                        acceptCottonwood(entries);
                        acceptWillow(entries);
                        acceptSpawnEggs(entries);
                    }).build());

    private static void acceptHerbsAndFlora(CreativeModeTab.Output entries) {
        entries.accept(ModBlocks.SPIRIT_BLOOM.get());
        entries.accept(ModBlocks.DREAMSHROOM.get());
        entries.accept(ModItems.SIREN_KELP.get());
        entries.accept(ModBlocks.GHOST_FERN.get());
        entries.accept(ModBlocks.CELESTIAL_BLOOM.get());
        entries.accept(ModItems.LOTUS_FLOWER.get());
        entries.accept(ModItems.LOTUS_BLOSSOM.get());
        entries.accept(ModBlocks.WITCHWEED.get());
        entries.accept(ModBlocks.MORPHORA.get());
        entries.accept(ModBlocks.GRIMSHADE.get());
        entries.accept(ModItems.NAUTILITE.get());
        entries.accept(ModBlocks.WINDSONG.get());
        entries.accept(ModBlocks.ASTRYLIS.get());
        entries.accept(ModBlocks.LOURDES.get());
        entries.accept(ModBlocks.AEGIFLORA.get());
        entries.accept(ModBlocks.BEGONIA.get());
        entries.accept(ModBlocks.LAVENDER.get());
        entries.accept(ModBlocks.DAHLIA.get());
        entries.accept(ModBlocks.PALE_MUSHROOM.get());
        entries.accept(ModBlocks.NIGHTSHADE_BUSH.get());
    }

    private static void acceptProcessedIngredients(CreativeModeTab.Output entries) {
        entries.accept(ModItems.SPIRIT_POWDER.get());
        entries.accept(ModItems.DREAM_PASTE.get());
        entries.accept(ModItems.SIREN_PASTE.get());
        entries.accept(ModItems.GHOST_POWDER.get());
        entries.accept(ModItems.FRAGRANT_NECTAR.get());
    }

    private static void acceptSeedsAndCrops(CreativeModeTab.Output entries) {
        entries.accept(ModItems.MANDRAKE_SEEDS.get());
        entries.accept(ModItems.SUNFIRE_TOMATO_SEEDS.get());
        entries.accept(ModItems.RABBAGE_SEEDS.get());
        entries.accept(ModBlocks.WILD_MANDRAKE.get());
        entries.accept(ModBlocks.WILD_SUNFIRE_TOMATO.get());
        entries.accept(ModItems.MANDRAKE.get());
        entries.accept(ModItems.SUNFIRE_TOMATO.get());
        entries.accept(ModItems.CHILLBERRIES.get());
        entries.accept(ModItems.RABBAGE.get());
        entries.accept(ModItems.SALTSPROUT.get());
        entries.accept(ModItems.GALEBERRIES.get());
    }

    private static void acceptFood(CreativeModeTab.Output entries) {
        entries.accept(ModItems.MANDRAKE_STEW.get());
        entries.accept(ModItems.SPICY_SANDWICH.get());
        entries.accept(ModItems.CHILLBERRY_PIE.get());
        entries.accept(ModItems.GALEBERRIES_COOKIE.get());
    }

    private static void acceptBrewsAndAlchemy(CreativeModeTab.Output entries) {
        entries.accept(ModItems.RUSTIC_BOTTLE.get());
        entries.accept(ModItems.BREW_OF_SPIKESKIN.get());
        entries.accept(ModItems.BREW_OF_BLOODLUST.get());
        entries.accept(ModItems.BREW_OF_SLIMEWALKER.get());
        entries.accept(ModItems.BREW_OF_HOMESTEAD.get());
        entries.accept(ModItems.BREW_OF_SIPHON.get());
        entries.accept(ModItems.BREW_OF_DAYBLOOM.get());
        entries.accept(ModItems.BREW_OF_GRAVEBLOOM.get());
        entries.accept(ModItems.BREW_OF_ARACHNID_GRACE.get());
        entries.accept(ModItems.BREW_OF_HOLLOW_SILENCE.get());
        entries.accept(ModItems.BRAMBLEGUARD_SALVE.get());
        entries.accept(ModItems.MENDERS_SALVE.get());
        entries.accept(ModItems.SALT.get());
        entries.accept(ModBlocks.SALT_BLOCK.get());
        entries.accept(ModItems.PURIFYING_SAC.get());
        entries.accept(ModItems.FOUL_SAC.get());
        entries.accept(ModItems.FROST_SAC.get());
        entries.accept(ModItems.SEARING_SAC.get());
    }

    private static void acceptMagicComponents(CreativeModeTab.Output entries) {
        entries.accept(ModItems.FIRE_NODE.get());
        entries.accept(ModItems.WATER_NODE.get());
        entries.accept(ModItems.AIR_NODE.get());
        entries.accept(ModItems.EARTH_NODE.get());
        entries.accept(ModItems.TREE_RESIN.get());
        entries.accept(ModItems.CELESTIAL_CRYSTAL.get());
        entries.accept(ModBlocks.CELESTIAL_CRYSTAL_BLOCK.get());
        entries.accept(ModItems.SILK_FIBER.get());
        entries.accept(ModItems.SILKWORM.get());
    }

    private static void acceptToolsAndRelics(CreativeModeTab.Output entries) {
        entries.accept(ModItems.MORTAR_AND_PESTLE.get());
        entries.accept(ModItems.ATHAME.get());
        entries.accept(ModItems.HEX_FOCUS.get());
        entries.accept(ModItems.LADLE.get());
        entries.accept(ModItems.SILK_IDOL.get());
        entries.accept(ModItems.RAINFALL_IDOL.get());
        entries.accept(ModItems.CLARITY_IDOL.get());
        entries.accept(ModItems.TEMPEST_IDOL.get());
        entries.accept(ModItems.PURITY_IDOL.get());
        entries.accept(ModItems.MUTAVIS.get());
        entries.accept(ModItems.SPIRITROOT_TETHER.get());
        if (dev.architectury.platform.Platform.isModLoaded("patchouli")) {
            entries.accept(ModItems.VERDANT_GRIMOIRE.get());
        }
    }

    private static void acceptWearables(CreativeModeTab.Output entries) {
        entries.accept(ModItems.EARPLUGS.get());
        entries.accept(ModItems.SAGE_PENDANT.get());
        entries.accept(ModItems.SEAFOAM_TALISMAN.get());
        entries.accept(ModItems.MOONWARD_RING.get());
        entries.accept(ModItems.GREEN_OMEN.get());
        entries.accept(ModItems.WITCHHEART_CLUSTER.get());
        entries.accept(ModItems.WYRD_FEATHER.get());
        entries.accept(ModItems.GHOSTVEIL.get());
        entries.accept(ModItems.BOGSHADE_BOOTS.get());
        entries.accept(ModItems.SILKWEAVE_HOOD.get());
        entries.accept(ModItems.SILKWEAVE_MANTLE.get());
        entries.accept(ModItems.SILKWEAVE_BINDINGS.get());
        entries.accept(ModItems.SILKWEAVE_FOOTWRAPS.get());
        entries.accept(ModItems.BLOOMWRAP_HAT.get());
        entries.accept(ModItems.BLOOMWRAP_ROBES.get());
        entries.accept(ModItems.BLOOMWRAP_LEGGINGS.get());
        entries.accept(ModItems.BLOOMWRAP_BOOTS.get());
        entries.accept(ModItems.MOONWEAVE_HOOD.get());
        entries.accept(ModItems.MOONWEAVE_MANTLE.get());
        entries.accept(ModItems.MOONWEAVE_BINDINGS.get());
        entries.accept(ModItems.MOONWEAVE_FOOTWRAPS.get());
    }

    private static void acceptFunctionalBlocks(CreativeModeTab.Output entries) {
        entries.accept(ModItems.SMALL_CAULDRON.get());
        entries.accept(ModBlocks.SHELF.get());
        entries.accept(ModBlocks.HERB_JAR.get());
        entries.accept(ModBlocks.RITUAL_TABLE.get());
        entries.accept(ModBlocks.INFUSED_DIRT.get());
        entries.accept(ModBlocks.INFUSED_FARMLAND.get());
        entries.accept(ModBlocks.RITUAL_BRAZIER.get());
        entries.accept(ModBlocks.CENSER.get());
        entries.accept(ModBlocks.DREAMCATCHER.get());
        entries.accept(ModBlocks.NESTING_BLOCK.get());
    }

    private static void acceptDecor(CreativeModeTab.Output entries) {
        entries.accept(ModItems.CANDLE_SKULL.get());
        entries.accept(ModItems.WITHER_CANDLE_SKULL.get());
        entries.accept(ModItems.SALT_LAMP.get());
    }

    private static void acceptRareItems(CreativeModeTab.Output entries) {
        entries.accept(ModItems.HEARTSEED.get());
        entries.accept(ModItems.ANCIENT_SEED.get());
        entries.accept(ModItems.KELPWEAVE_BLADE.get());
        entries.accept(ModItems.ROOTSHAPER.get());
        entries.accept(ModItems.CINDERHEW.get());
        entries.accept(ModItems.THORNBOW.get());
        entries.accept(ModItems.BRIAR_SICKLE.get());
    }

    private static void acceptCottonwood(CreativeModeTab.Output entries) {
        entries.accept(ModBlocks.COTTONWOOD_SAPLING.get());
        entries.accept(ModBlocks.COTTONWOOD_LEAVES.get());
        entries.accept(ModBlocks.COTTONWOOD_LOG.get());
        entries.accept(ModBlocks.COTTONWOOD_WOOD.get());
        entries.accept(ModBlocks.STRIPPED_COTTONWOOD_LOG.get());
        entries.accept(ModBlocks.STRIPPED_COTTONWOOD_WOOD.get());
        entries.accept(ModBlocks.COTTONWOOD_PLANKS.get());
        entries.accept(ModBlocks.COTTONWOOD_STAIRS.get());
        entries.accept(ModBlocks.COTTONWOOD_SLAB.get());
        entries.accept(ModBlocks.COTTONWOOD_FENCE.get());
        entries.accept(ModBlocks.COTTONWOOD_FENCE_GATE.get());
        entries.accept(ModBlocks.COTTONWOOD_DOOR.get());
        entries.accept(ModBlocks.COTTONWOOD_TRAPDOOR.get());
        entries.accept(ModBlocks.COTTONWOOD_PRESSURE_PLATE.get());
        entries.accept(ModBlocks.COTTONWOOD_BUTTON.get());
        entries.accept(ModItems.COTTONWOOD_SIGN.get());
        entries.accept(ModItems.COTTONWOOD_HANGING_SIGN.get());
        entries.accept(ModItems.COTTONWOOD_BOAT.get());
        entries.accept(ModItems.COTTONWOOD_CHEST_BOAT.get());
    }

    private static void acceptWillow(CreativeModeTab.Output entries) {
        entries.accept(ModBlocks.WILLOW_SAPLING.get());
        entries.accept(ModBlocks.WILLOW_LEAVES.get());
        entries.accept(ModBlocks.WILLOW_LOG.get());
        entries.accept(ModBlocks.WILLOW_WOOD.get());
        entries.accept(ModBlocks.STRIPPED_WILLOW_LOG.get());
        entries.accept(ModBlocks.STRIPPED_WILLOW_WOOD.get());
        entries.accept(ModBlocks.WILLOW_PLANKS.get());
        entries.accept(ModBlocks.WILLOW_STAIRS.get());
        entries.accept(ModBlocks.WILLOW_SLAB.get());
        entries.accept(ModBlocks.WILLOW_FENCE.get());
        entries.accept(ModBlocks.WILLOW_FENCE_GATE.get());
        entries.accept(ModBlocks.WILLOW_DOOR.get());
        entries.accept(ModBlocks.WILLOW_TRAPDOOR.get());
        entries.accept(ModBlocks.WILLOW_PRESSURE_PLATE.get());
        entries.accept(ModBlocks.WILLOW_BUTTON.get());
        entries.accept(ModItems.WILLOW_SIGN.get());
        entries.accept(ModItems.WILLOW_HANGING_SIGN.get());
        entries.accept(ModItems.WILLOW_BOAT.get());
        entries.accept(ModItems.WILLOW_CHEST_BOAT.get());
    }

    private static void acceptSpawnEggs(CreativeModeTab.Output entries) {
        entries.accept(ModItems.SILK_MOTH_SPAWN_EGG.get());
        entries.accept(ModItems.CACOFEY_SPAWN_EGG.get());
    }

    public static void registerCreativeModeTabs() {
        HexaliaMod.LOGGER.info("Registering Item Group for " + HexaliaMod.MODID);
    }
}
