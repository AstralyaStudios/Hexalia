package net.astralya.hexalia.neoforge.datagen;

import net.astralya.hexalia.Hexalia;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.effect.ModMobEffects;
import net.astralya.hexalia.entity.ModEntities;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public final class ModLanguageProvider extends LanguageProvider {
  public ModLanguageProvider(PackOutput output) {
    super(output, Hexalia.MOD_ID, "en_us");
  }

  @Override
  protected void addTranslations() {
    addCreativeTabTranslations();

    addBlockTranslations();
    addWoodTranslations();
    addItemTranslations();
    addEntityTranslations();

    addContainerTranslations();
    addTooltipTranslations();
    addMessageTranslations();

    addAdvancementTranslations();
    addEffectTranslations();

    addJeiTranslations();
    addEmiTranslations();

    addTagTranslations();
    addMiscTranslations();
  }

  private void addCreativeTabTranslations() {
    add("itemGroup.hexalia", "Hexalia");
  }

  private void addBlockTranslations() {
    add(ModBlocks.INFUSED_DIRT.get(), "Infused Dirt");
    add(ModBlocks.INFUSED_FARMLAND.get(), "Infused Farmland");

    add(ModBlocks.SILKWORM_COCOON.get(), "Silkworm Cocoon");
    add(ModBlocks.EGG_CLUSTER.get(), "Egg Cluster");

    add(ModBlocks.SPIRIT_BLOOM.get(), "Spirit Bloom");
    add(ModBlocks.POTTED_SPIRIT_BLOOM.get(), "Potted Spirit Bloom");

    add(ModBlocks.DREAMSHROOM.get(), "Dreamshroom");
    add(ModBlocks.POTTED_DREAMSHROOM.get(), "Potted Dreamshroom");

    add(ModBlocks.SIREN_KELP.get(), "Siren Kelp");

    add(ModBlocks.GHOST_FERN.get(), "Ghost Fern");
    add(ModBlocks.POTTED_GHOST_FERN.get(), "Potted Ghost Fern");

    add(ModBlocks.CELESTIAL_BLOOM.get(), "Celestial Bloom");
    add(ModBlocks.POTTED_CELESTIAL_BLOOM.get(), "Potted Celestial Bloom");

    add(ModBlocks.WITHERED_CELESTIAL_BLOOM.get(), "Withered Celestial Bloom");
    add(ModBlocks.POTTED_WITHERED_CELESTIAL_BLOOM.get(), "Potted Withered Celestial Bloom");

    add(ModBlocks.LOTUS_FLOWER.get(), "Lotus Flower");

    add(ModBlocks.WITCHWEED.get(), "Witchweed");

    add(ModBlocks.MORPHORA.get(), "Morphora");
    add(ModBlocks.POTTED_MORPHORA.get(), "Potted Morphora");

    add(ModBlocks.GRIMSHADE.get(), "Grimshade");
    add(ModBlocks.POTTED_GRIMSHADE.get(), "Potted Grimshade");

    add(ModBlocks.NAUTILITE.get(), "Nautilite");

    add(ModBlocks.WINDSONG.get(), "Windsong");
    add(ModBlocks.POTTED_WINDSONG.get(), "Potted Windsong");

    add(ModBlocks.ASTRYLIS.get(), "Astrylis");
    add(ModBlocks.POTTED_ASTRYLIS.get(), "Potted Astrylis");

    add(ModBlocks.LOURDES.get(), "Lourdes");
    add(ModBlocks.POTTED_LOURDES.get(), "Potted Lourdes");

    add(ModBlocks.AEGIFLORA.get(), "Aegiflora");
    add(ModBlocks.POTTED_AEGIFLORA.get(), "Potted Aegiflora");

    add(ModBlocks.WITHERED_AEGIFLORA.get(), "Withered Aegiflora");
    add(ModBlocks.POTTED_WITHERED_AEGIFLORA.get(), "Potted Withered Aegiflora");

    add(ModBlocks.BEGONIA.get(), "Begonia");
    add(ModBlocks.POTTED_BEGONIA.get(), "Potted Begonia");

    add(ModBlocks.LAVENDER.get(), "Lavender");
    add(ModBlocks.POTTED_LAVENDER.get(), "Potted Lavender");

    add(ModBlocks.DAHLIA.get(), "Dahlia");
    add(ModBlocks.POTTED_DAHLIA.get(), "Potted Dahlia");

    add(ModBlocks.PALE_MUSHROOM.get(), "Pale Mushroom");

    add(ModBlocks.NIGHTSHADE_BUSH.get(), "Nightshade Bush");
    add(ModBlocks.POTTED_NIGHTSHADE_BUSH.get(), "Potted Nightshade Bush");

    add(ModBlocks.MANDRAKE_CROP.get(), "Mandrake");
    add(ModBlocks.SUNFIRE_TOMATO_CROP.get(), "Sunfire Tomato");
    add(ModBlocks.RABBAGE_CROP.get(), "Rabbage");

    add(ModBlocks.WILD_MANDRAKE.get(), "Wild Mandrake");
    add(ModBlocks.WILD_SUNFIRE_TOMATO.get(), "Wild Sunfire Tomato");

    add(ModBlocks.CHILLBERRY_BUSH.get(), "Chillberry Bush");

    add(ModBlocks.SALTSPROUT.get(), "Saltsprout");

    add(ModBlocks.GALEBERRIES_VINE.get(), "Galeberries Vine");
    add(ModBlocks.GALEBERRIES_VINE_PLANT.get(), "Galeberries Vine Plant");

    add(ModBlocks.SALT_BLOCK.get(), "Salt Block");
    add(ModBlocks.SALT_LAMP.get(), "Salt Lamp");

    add(ModBlocks.CELESTIAL_CRYSTAL_BLOCK.get(), "Celestial Crystal Block");

    add(ModBlocks.SMALL_CAULDRON.get(), "Small Cauldron");

    add(ModBlocks.SHELF.get(), "Shelf");
    add(ModBlocks.HERB_JAR.get(), "Herb Jar");
    add(ModBlocks.RUSTIC_OVEN.get(), "Rustic Oven");

    add(ModBlocks.RITUAL_TABLE.get(), "Ritual Table");
    add(ModBlocks.RITUAL_BRAZIER.get(), "Ritual Brazier");
    add(ModBlocks.CENSER.get(), "Censer");

    add(ModBlocks.DREAMCATCHER.get(), "Dreamcatcher");

    add(ModBlocks.MORTAR_AND_PESTLE.get(), "Mortar and Pestle");

    add(ModBlocks.NESTING_BLOCK.get(), "Nesting Block");

    add(ModBlocks.CANDLE_SKULL.get(), "Candle Skull");
    add(ModBlocks.WITHER_CANDLE_SKULL.get(), "Wither Candle Skull");
  }

  private void addWoodTranslations() {
    add(ModBlocks.COTTONWOOD_LEAVES.get(), "Cottonwood Leaves");
    add(ModBlocks.COTTONWOOD_CATKIN.get(), "Cottonwood Catkin");

    add(ModBlocks.COTTONWOOD_LOG.get(), "Cottonwood Log");
    add(ModBlocks.COTTONWOOD_WOOD.get(), "Cottonwood Wood");

    add(ModBlocks.STRIPPED_COTTONWOOD_LOG.get(), "Stripped Cottonwood Log");
    add(ModBlocks.STRIPPED_COTTONWOOD_WOOD.get(), "Stripped Cottonwood Wood");

    add(ModBlocks.COTTONWOOD_PLANKS.get(), "Cottonwood Planks");

    add(ModBlocks.COTTONWOOD_SAPLING.get(), "Cottonwood Sapling");

    add(ModBlocks.COTTONWOOD_STAIRS.get(), "Cottonwood Stairs");
    add(ModBlocks.COTTONWOOD_SLAB.get(), "Cottonwood Slab");

    add(ModBlocks.COTTONWOOD_BUTTON.get(), "Cottonwood Button");
    add(ModBlocks.COTTONWOOD_PRESSURE_PLATE.get(), "Cottonwood Pressure Plate");

    add(ModBlocks.COTTONWOOD_FENCE.get(), "Cottonwood Fence");
    add(ModBlocks.COTTONWOOD_FENCE_GATE.get(), "Cottonwood Fence Gate");

    add(ModBlocks.COTTONWOOD_TRAPDOOR.get(), "Cottonwood Trapdoor");
    add(ModBlocks.COTTONWOOD_DOOR.get(), "Cottonwood Door");

    add(ModBlocks.COTTONWOOD_SIGN.get(), "Cottonwood Sign");
    add(ModBlocks.COTTONWOOD_HANGING_SIGN.get(), "Cottonwood Hanging Sign");

    add(ModBlocks.WILLOW_LEAVES.get(), "Willow Leaves");

    add(ModBlocks.WILLOW_LOG.get(), "Willow Log");
    add(ModBlocks.WILLOW_WOOD.get(), "Willow Wood");

    add(ModBlocks.STRIPPED_WILLOW_LOG.get(), "Stripped Willow Log");
    add(ModBlocks.STRIPPED_WILLOW_WOOD.get(), "Stripped Willow Wood");

    add(ModBlocks.WILLOW_PLANKS.get(), "Willow Planks");

    add(ModBlocks.WILLOW_SAPLING.get(), "Willow Sapling");

    add(ModBlocks.WILLOW_STAIRS.get(), "Willow Stairs");
    add(ModBlocks.WILLOW_SLAB.get(), "Willow Slab");

    add(ModBlocks.WILLOW_BUTTON.get(), "Willow Button");
    add(ModBlocks.WILLOW_PRESSURE_PLATE.get(), "Willow Pressure Plate");

    add(ModBlocks.WILLOW_FENCE.get(), "Willow Fence");
    add(ModBlocks.WILLOW_FENCE_GATE.get(), "Willow Fence Gate");

    add(ModBlocks.WILLOW_TRAPDOOR.get(), "Willow Trapdoor");
    add(ModBlocks.WILLOW_DOOR.get(), "Willow Door");

    add(ModBlocks.WILLOW_SIGN.get(), "Willow Sign");
    add(ModBlocks.WILLOW_HANGING_SIGN.get(), "Willow Hanging Sign");
  }

  private void addItemTranslations() {
    add(ModItems.HEARTSEED.get(), "Heartseed");
    add(ModItems.SALT.get(), "Salt");
    add(ModItems.TREE_RESIN.get(), "Tree Resin");

    add(ModItems.SILK_FIBER.get(), "Silk Fiber");
    add(ModItems.SILKWORM.get(), "Silkworm");

    add(ModItems.CELESTIAL_CRYSTAL.get(), "Celestial Crystal");

    add(ModItems.FIRE_NODE.get(), "Fire Node");
    add(ModItems.WATER_NODE.get(), "Water Node");
    add(ModItems.AIR_NODE.get(), "Air Node");
    add(ModItems.EARTH_NODE.get(), "Earth Node");

    add(ModItems.ANCIENT_SEED.get(), "Ancient Seed");

    add(ModItems.SUNFIRE_TOMATO.get(), "Sunfire Tomato");
    add(ModItems.SUNFIRE_TOMATO_SEEDS.get(), "Sunfire Tomato Seeds");

    add(ModItems.MANDRAKE.get(), "Mandrake");
    add(ModItems.MANDRAKE_SEEDS.get(), "Mandrake Seeds");

    add(ModItems.RABBAGE.get(), "Rabbage");
    add(ModItems.RABBAGE_SEEDS.get(), "Rabbage Seeds");

    add(ModItems.CHILLBERRIES.get(), "Chillberries");

    add(ModItems.GALEBERRIES.get(), "Galeberries");

    add(ModItems.LOTUS_BLOSSOM.get(), "Lotus Blossom");

    add(ModItems.SPIRIT_POWDER.get(), "Spirit Powder");
    add(ModItems.SIREN_PASTE.get(), "Siren Paste");
    add(ModItems.DREAM_PASTE.get(), "Dream Paste");
    add(ModItems.GHOST_POWDER.get(), "Ghost Powder");

    add(ModItems.FRAGRANT_NECTAR.get(), "Fragrant Nectar");

    add(ModItems.SPICY_SANDWICH.get(), "Spicy Sandwich");
    add(ModItems.CHILLBERRY_PIE.get(), "Chillberry Pie");
    add(ModItems.MANDRAKE_STEW.get(), "Mandrake Stew");
    add(ModItems.GALEBERRIES_COOKIE.get(), "Galeberries Cookie");

    add(ModItems.HEX_FOCUS.get(), "Hex Focus");
    add(ModItems.ATHAME.get(), "Athame");

    add(ModItems.PURIFYING_SAC.get(), "Purifying Sac");
    add(ModItems.FOUL_SAC.get(), "Foul Sac");
    add(ModItems.FROST_SAC.get(), "Frost Sac");
    add(ModItems.SEARING_SAC.get(), "Searing Sac");

    add(ModItems.SAGE_PENDANT.get(), "Sage Pendant");
    add(ModItems.SEAFOAM_TALISMAN.get(), "Seafoam Talisman");
    add(ModItems.MOONWARD_RING.get(), "Moonward Ring");
    add(ModItems.WITCHHEART_CLUSTER.get(), "Witchheart Cluster");
    add(ModItems.WYRD_FEATHER.get(), "Wyrd Feather");
    add(ModItems.GREEN_OMEN.get(), "Green Omen");

    add(ModItems.SILK_IDOL.get(), "Silk Idol");
    add(ModItems.CLARITY_IDOL.get(), "Clarity Idol");
    add(ModItems.RAINFALL_IDOL.get(), "Rainfall Idol");
    add(ModItems.TEMPEST_IDOL.get(), "Tempest Idol");
    add(ModItems.PURITY_IDOL.get(), "Purity Idol");

    add(ModItems.MUTAVIS.get(), "Mutavis");

    add(ModItems.BRIAR_SICKLE.get(), "Briar Sickle");
    add(ModItems.ROOTSHAPER.get(), "Rootshaper Pickaxe");

    add(ModItems.SPIRITROOT_TETHER.get(), "Spiritroot Tether");

    add(ModItems.LADLE.get(), "Ladle");

    add(ModItems.KELPWEAVE_BLADE.get(), "Kelpweave Blade");
    add(ModItems.CINDERHEW.get(), "Cinderhew");
    add(ModItems.THORNBOW.get(), "Thornbow");

    add(ModItems.SILKWEAVE_HOOD.get(), "Silkweave Hood");
    add(ModItems.SILKWEAVE_MANTLE.get(), "Silkweave Mantle");
    add(ModItems.SILKWEAVE_BINDINGS.get(), "Silkweave Bindings");
    add(ModItems.SILKWEAVE_FOOTWRAPS.get(), "Silkweave Footwraps");

    add(ModItems.MOONWEAVE_HOOD.get(), "Moonweave Hood");
    add(ModItems.MOONWEAVE_MANTLE.get(), "Moonweave Mantle");
    add(ModItems.MOONWEAVE_BINDINGS.get(), "Moonweave Bindings");
    add(ModItems.MOONWEAVE_FOOTWRAPS.get(), "Moonweave Footwraps");

    add(ModItems.BLOOMWRAP_HAT.get(), "Bloomwrap Hat");
    add(ModItems.BLOOMWRAP_ROBES.get(), "Bloomwrap Robes");
    add(ModItems.BLOOMWRAP_LEGGINGS.get(), "Bloomwrap Leggings");
    add(ModItems.BLOOMWRAP_BOOTS.get(), "Bloomwrap Boots");

    add(ModItems.GHOSTVEIL.get(), "Ghostveil");
    add(ModItems.EARPLUGS.get(), "Earplugs");
    add(ModItems.BOGSHADE_BOOTS.get(), "Bogshade Boots");

    add(ModItems.RUSTIC_BOTTLE.get(), "Rustic Bottle");

    add(ModItems.BREW_OF_BLOODLUST.get(), "Brew of Bloodlust");
    add(ModItems.BREW_OF_SLIMEWALKER.get(), "Brew of Slimewalker");
    add(ModItems.BREW_OF_SPIKESKIN.get(), "Brew of Spikeskin");
    add(ModItems.BREW_OF_SIPHON.get(), "Brew of Siphon");
    add(ModItems.BREW_OF_DAYBLOOM.get(), "Brew of Daybloom");
    add(ModItems.BREW_OF_GRAVEBLOOM.get(), "Brew of Gravebloom");
    add(ModItems.BREW_OF_ARACHNID_GRACE.get(), "Brew of Arachnid Grace");
    add(ModItems.BREW_OF_HOMESTEAD.get(), "Brew of Homestead");
    add(ModItems.BREW_OF_HOLLOW_SILENCE.get(), "Brew of Hollow Silence");

    add(ModItems.BRAMBLEGUARD_SALVE.get(), "Brambleguard Salve");
    add(ModItems.MENDERS_SALVE.get(), "Mender's Salve");

    add(ModItems.BOTTLED_MOTH.get(), "Bottled Silk Moth");

    add(ModItems.SILK_MOTH_SPAWN_EGG.get(), "Silk Moth Spawn Egg");
    add(ModItems.CACOFEY_SPAWN_EGG.get(), "Cacofey Spawn Egg");

    add(ModItems.COTTONWOOD_BOAT.get(), "Cottonwood Boat");
    add(ModItems.COTTONWOOD_CHEST_BOAT.get(), "Cottonwood Chest Boat");

    add(ModItems.WILLOW_BOAT.get(), "Willow Boat");
    add(ModItems.WILLOW_CHEST_BOAT.get(), "Willow Chest Boat");

    add(ModItems.VERDANT_GRIMOIRE.get(), "Verdant Grimoire");
  }

  private void addEntityTranslations() {
    add(ModEntities.SILK_MOTH.get(), "Silk Moth");
    add(ModEntities.CACOFEY.get(), "Cacofey");
    add(ModEntities.THORN_ARROW.get(), "Thorn Arrow");
  }

  private void addContainerTranslations() {
    add("container.hexalia.nesting_block", "Nesting Block");
    add("container.hexalia.small_cauldron", "Small Cauldron");
  }

  private void addTooltipTranslations() {
    add("tooltip.hexalia.heat", "A heat source is required");

    add("tooltip.hexalia.hex_focus_gui", "Right-click with a Hex Focus");
    add("tooltip.hexalia.mutation", "Right-click with a Mutavis");
    add("tooltip.hexalia.requires_soul", "Requires a soul sacrifice.");

    add("tooltip.hexalia.homestead_brew", "Teleports to your spawn");
    add("tooltip.hexalia.siphon_brew", "Siphon (4:00)");
    add("tooltip.hexalia.slimewalker_brew", "Slimewalker (4:00)");
    add("tooltip.hexalia.bloodlust_brew", "Bloodlust (4:00)");
    add("tooltip.hexalia.spikeskin_brew", "Spikeskin (4:00)");
    add("tooltip.hexalia.daybloom", "Daybloom (4:00)");
    add("tooltip.hexalia.gravebloom", "Gravebloom (1:30)");
    add("tooltip.hexalia.arachnid_grace", "Arachnid Grace (4:00)");
    add("tooltip.hexalia.hollow_silence", "Hollow Silence (4:00)");

    add("tooltip.hexalia.menders_salve", "Regeneration (1:30)");
    add("tooltip.hexalia.brambleguard_salve", "Brambleguard (1:30)");

    add("tooltip.hexalia.hold_shift", "Hold SHIFT for more information");

    add("tooltip.hexalia.accessory.earplugs", "Protects against a Mandrake's stunning cry.");
    add("tooltip.hexalia.accessory.sage_pendant", "Increases experience gained.");
    add("tooltip.hexalia.accessory.seafoam_talisman", "Helps you breathe while underwater.");
    add("tooltip.hexalia.accessory.moonward_ring", "Protects against Darkness and Blindness.");
    add("tooltip.hexalia.accessory.witchheart_cluster", "Grants two extra hearts.");
    add("tooltip.hexalia.accessory.wyrd_feather", "10% chance to avoid incoming attacks.");
    add("tooltip.hexalia.accessory.green_omen", "10% chance to double crop drops.");

    add("tooltip.hexalia.bogshade_boots", "Full Set Bonus:");
    add("tooltip.hexalia.bogged_armor_2", "Poison Immunity and Water Breathing");

    add("tooltip.hexalia.ghostveil", "Shrouds the wearer to avoid mob detection");

    add("tooltip.hexalia.mandrake", "Stuns in an area on usage");

    add("tooltip.hexalia.purifying_sac", "Removes negative status effects");

    add("tooltip.hexalia.throwable", "Throwable");

    add("tooltip.hexalia.bottled_moth", "Contains: %s");

    add("tooltip.hexalia.enchanted_plant", "Enchanted Plant");

    add("tooltip.hexalia.shelf", "Can store up to 6 brews or potions");
    add("tooltip.hexalia.herb_jar.stores", "Stores: %s × %s");

    add("tooltip.hexalia.thornbow.no_arrows", "Consumes no arrows.");
    add("tooltip.hexalia.thornbow.bleeding", "Thorn shots inflict bleeding.");

    add("tooltip.hexalia.magic_resistance", "+%s Magic Resistance");
    add("tooltip.hexalia.magic_resistance_full_set", "Full Set Bonus: +%s Magic Resistance");

    add("tooltip.hexalia.magic_resist_piece", "Magic Resistance: +%s");
    add("tooltip.hexalia.magic_resist_full_set", "Full Set: +%s Magic Resistance");

    add("tooltip.hexalia.spiritroot_tether", "Bound Spirit: %s");
    add("tooltip.hexalia.spiritroot_tether.bound", "Bound to: %s, %s, %s");
    add("tooltip.hexalia.spiritroot_tether.unknown", "Unknown");

    add("tooltip.hexalia.rootshaper.mode_3x3_active", "3×3 Mining: Active");
    add("tooltip.hexalia.rootshaper.mode_3x3_hint", "Sneak while mining to enable 3×3 mode");
    add("tooltip.hexalia.cinderhew.fire_damage", "+2 Fire Damage");
  }

  private void addMessageTranslations() {
    add("message.hexalia.ritual.invalid_crops", "Natural energy is weak; fully grown crops are missing.");
    add("message.hexalia.natures_ritual.invalid_crops", "Natural energy is weak; fully grown crops are missing.");
    add("message.hexalia.natures_ritual.missing_ingredients", "The ritual table lacks its central ingredient.");
    add("message.hexalia.ritual.missing_salt", "The braziers lack the salt to channel energy.");
    add("message.hexalia.natures_ritual.missing_salt", "The braziers lack the salt to channel energy.");
    add("message.hexalia.ritual.wrong_recipe", "The ritual ingredients resonate incorrectly.");
    add("message.hexalia.natures_ritual.wrong_recipe", "The ritual ingredients resonate incorrectly.");
    add("message.hexalia.ritual.stopped_ritual", "The ritual falters as an item is removed.");
    add("message.hexalia.natures_ritual.stopped_ritual", "The ritual falters as an item is removed.");
    add("message.hexalia.natures_ritual.awaiting_soul", "The ritual awaits a soul sacrifice.");

    add("message.hexalia.rainfall_idol", "The skies darken as rain begins to fall...");
    add("message.hexalia.clarity_idol", "The clouds part, revealing a clear sky...");
    add("message.hexalia.tempest_idol", "The skies roar as thunder begins to clash...");

    add(
            "message.hexalia.astrylis.activation",
            "Celestial energy seeps into the crops and saplings, charging them softly.");

    add(
            "message.hexalia.astrylis.inactive",
            "A Celestial Crystal is required to activate the plant.");

    add(
            "message.hexalia.grimshade.peaceful",
            "Grimshade cannot be activated in Peaceful difficulty.");

    add(
            "message.hexalia.ritual_brazier.invalid_item",
            "The item placed on the brazier cannot be imbued.");

    add(
            "message.hexalia.ritual_brazier.no_celestial_blooms",
            "There are not enough Celestial Blooms nearby.");

    add(
            "message.hexalia.ritual_brazier.no_sky",
            "The infusion requires an open view of the sky.");

    add(
            "message.hexalia.celestial_infusion.invalid_item",
            "The item placed on the brazier cannot be infused.");

    add(
            "message.hexalia.celestial_infusion.no_celestial_blooms",
            "There are not enough Celestial Blooms nearby.");

    add(
            "message.hexalia.celestial_infusion.no_sky",
            "The infusion requires an open view of the sky.");

    add(
            "message.hexalia.censer_not_full",
            "The censer must be filled with herbs before lighting!");

    add(
            "message.hexalia.invalid_herb_combination",
            "This combination of herbs does not work.");

    add(
            "message.hexalia.censer.tidewarden",
            "A gentle tide lifts the spirit, carrying it beyond harm");

    add(
            "message.hexalia.censer.undead_veil",
            "Unnatural stillness quiets the restless dead");

    add(
            "message.hexalia.censer.ethereal_grazing",
            "A pale tranquility settles over the herd, and life renews itself without need");

    add(
            "message.hexalia.censer.miners_respite",
            "Metals whisper secrets of perfect balance");

    add(
            "message.hexalia.censer.tides_memory",
            "The water stirs with old memories, offering what the tide once carried");

    add(
            "message.hexalia.censer.phantom_drift",
            "Objects waver like ghosts, vanishing only to reappear where they belong");

    add(
            "message.hexalia.censer.withering_calm",
            "A quiet malaise settles over the land, and even decay feels strangely serene");

    add(
            "message.hexalia.censer.hollow_aura",
            "A hollow hush devours vitality, and blessings falter in its presence");

    add(
            "message.hexalia.censer.blighted_bloom",
            "A blighted bloom seeps into the soil, and fungus quietly takes hold");

    add(
            "message.hexalia.censer.tidal_pull",
            "A heavy current swirls around the censer, drawing bodies and burdens inward");

    add("message.hexalia.invalid_item", "This item is not valid for this block.");

    add(
            "message.hexalia.patchouli_missing",
            "Patchouli is required to open the Verdant Grimoire.");

    add(
            "message.hexalia.lourdes.activation",
            "The Lourdes flower blooms with purifying warmth.");

    add(
            "message.hexalia.lourdes.inactive",
            "The flower requires a Lotus Blossom to activate.");

    add(
            "message.hexalia.aegiflora.prevented",
            "Aegiflora drinks the blast before it can take form.");

    add(
            "message.hexalia.aegiflora.prevented.withered",
            "Aegiflora drinks the blast, and its petals wither in sacrifice.");

    add(
            "message.hexalia.aegiflora.prevented.dead",
            "Aegiflora drinks the last blast and crumbles into silence.");

    add("message.hexalia.spiritroot_tether.bound", "Tether bound.");

    add(
            "message.hexalia.spiritroot_tether.invalid_bind_block",
            "This block cannot be used as a Spiritroot anchor.");

    add("message.hexalia.spiritroot_tether.captured", "Spirit bound.");

    add(
            "message.hexalia.spiritroot_tether.already_occupied",
            "The tether is already bound to a spirit.");

    add(
            "message.hexalia.spiritroot_tether.not_bound",
            "The tether is not bound to a location.");

    add(
            "message.hexalia.spiritroot_tether.invalid_dimension",
            "That location cannot be reached.");

    add(
            "message.hexalia.spiritroot_tether.recall_failed",
            "The tether fails to recall the spirit.");

    add(
            "message.hexalia.spiritroot_tether.sent_to_anchor",
            "Spirit returned to its anchor.");

    add("message.hexalia.spiritroot_tether.released", "Spirit released.");

    add(
            "message.hexalia.spiritroot_tether.cannot_capture",
            "This spirit cannot be bound.");

    add("message.hexalia.cacofey.stay", "Cacofey is staying put.");
    add("message.hexalia.cacofey.follow", "Cacofey is following you.");
    add("message.hexalia.cacofey.wander", "Cacofey is wandering.");

    add(
            "message.hexalia.cacofey.attuned",
            "Hex Focus attuned to %s - right-click a container.");

    add(
            "message.hexalia.cacofey.anchored",
            "%s will harvest crops around this container.");

    add(
            "message.hexalia.cacofey.invalid_container",
            "That's not a valid container.");

    add(
            "message.hexalia.dreamcatcher_full",
            "The dreamcatcher cannot hold any more fuel.");
  }

  private void addAdvancementTranslations() {
    add("advancements.hexalia.root.title", "Hexalia");
    add("advancements.hexalia.root.description", "Obtain a Hex Focus.");

    add("advancements.hexalia.salt_of_the_craft.title", "Salt of the Craft");
    add(
            "advancements.hexalia.salt_of_the_craft.description",
            "Obtain Salt, the basis of all rituals.");

    add("advancements.hexalia.crush_course.title", "Crush Course");
    add(
            "advancements.hexalia.crush_course.description",
            "Obtain a Mortar & Pestle to crush herbs.");

    add("advancements.hexalia.knife_to_tree_you.title", "Bark & Dagger");
    add(
            "advancements.hexalia.knife_to_tree_you.description",
            "Obtain an Athame to collect Resin or Lotus Blossoms.");

    add("advancements.hexalia.table_manners.title", "Ritual Etiquette");
    add(
            "advancements.hexalia.table_manners.description",
            "Set the table... for the spirits.");

    add("advancements.hexalia.star_power.title", "Starry, Starry Might");
    add(
            "advancements.hexalia.star_power.description",
            "Perform a Celestial Infusion to obtain a Celestial Crystal.");

    add("advancements.hexalia.essence_collector.title", "Elemental, My Dear");
    add(
            "advancements.hexalia.essence_collector.description",
            "Obtain all of the Elemental Nodes.");

    add("advancements.hexalia.change_of_plans.title", "Crafty Mutations");
    add(
            "advancements.hexalia.change_of_plans.description",
            "Obtain a Mutavis and rewrite nature's rules.");

    add("advancements.hexalia.ring_of_change.title", "Crop Circles");
    add(
            "advancements.hexalia.ring_of_change.description",
            "Obtain a Morphora and watch the land rearrange itself.");

    add("advancements.hexalia.small_beginnings.title", "Cauldron Calamity");
    add(
            "advancements.hexalia.small_beginnings.description",
            "Craft a Small Cauldron and stir up some trouble.");

    add("advancements.hexalia.brewbie_award.title", "First Sip's the Deepest");
    add(
            "advancements.hexalia.brewbie_award.description",
            "Obtain a Rustic Bottle to start brewing.");

    add("advancements.hexalia.powder_and_pouch.title", "Bag of Tricks");
    add(
            "advancements.hexalia.powder_and_pouch.description",
            "Craft a throwable Foul Sac. Kaboom, but make it floral.");

    add("advancements.hexalia.silken_beginnings.title", "Thread Lightly");
    add(
            "advancements.hexalia.silken_beginnings.description",
            "Weave together your first Silk Idol.");

    add("advancements.hexalia.pure_intentions.title", "Exorcise in Style");
    add(
            "advancements.hexalia.pure_intentions.description",
            "Use a Purity Idol to remove curses from enchanted items.");

    add("advancements.hexalia.kelp_yourself.title", "Sea What You Did There");
    add(
            "advancements.hexalia.kelp_yourself.description",
            "Obtain the Kelpweave Blade - kelp your enemies!");

    add("advancements.hexalia.wise_investment.title", "Pendant Pending");
    add(
            "advancements.hexalia.wise_investment.description",
            "Obtain the Sage Pendant and profit in XP.");

    add("advancements.hexalia.herb_nerd.title", "Leaf It to Me");
    add(
            "advancements.hexalia.herb_nerd.description",
            "Collect every Magical Herb out there.");

    add("advancements.hexalia.seasoned_farmer.title", "Crop Top");
    add(
            "advancements.hexalia.seasoned_farmer.description",
            "Harvest the full lineup of Magical Crops.");

    add("advancements.hexalia.master_or_not.title", "Weather or Not");
    add(
            "advancements.hexalia.master_or_not.description",
            "Use a Rainfall, Clarity, and Tempest Idol. Forecast: spooky.");

    add("advancements.hexalia.brewed_awakening.title", "Espresso Patronum");
    add(
            "advancements.hexalia.brewed_awakening.description",
            "Drink every Hexalia Brew - magic beans not included.");
  }

  private void addEffectTranslations() {
    add(ModMobEffects.BLOODLUST.get(), "Bloodlust");
    add(
            ModMobEffects.BLOODLUST.get().getDescriptionId() + ".description",
            "Increases strength and restores a portion of damage dealt to foes, but the Regeneration effect is disabled.");

    add(ModMobEffects.STUNNED.get(), "Stunned");
    add(
            ModMobEffects.STUNNED.get().getDescriptionId() + ".description",
            "Immobilizes the target, preventing movement for a short duration.");

    add(ModMobEffects.OVERFED.get(), "Overfed");
    add(
            ModMobEffects.OVERFED.get().getDescriptionId() + ".description",
            "Prevents saturation from decreasing, but reduces movement speed while active.");

    add(ModMobEffects.SLIMEWALKER.get(), "Slimewalker");
    add(
            ModMobEffects.SLIMEWALKER.get().getDescriptionId() + ".description",
            "Negates fall damage and causes the user to bounce when landing. While grounded, movement is slowed.");

    add(ModMobEffects.SPIKESKIN.get(), "Spikeskin");
    add(
            ModMobEffects.SPIKESKIN.get().getDescriptionId() + ".description",
            "Increases armor and reflects a portion of incoming damage, but reduces movement speed.");

    add(ModMobEffects.SIPHON.get(), "Siphon");
    add(
            ModMobEffects.SIPHON.get().getDescriptionId() + ".description",
            "Increases mining speed and attracts nearby items, but breaking blocks increases exhaustion.");

    add(ModMobEffects.BLEEDING.get(), "Bleeding");
    add(
            ModMobEffects.BLEEDING.get().getDescriptionId() + ".description",
            "Deals damage over time, similar to Poison.");

    add(ModMobEffects.DAYBLOOM.get(), "Daybloom");
    add(
            ModMobEffects.DAYBLOOM.get().getDescriptionId() + ".description",
            "Grants regeneration and increased speed during daylight, but darkness harms the user.");

    add(ModMobEffects.GRAVEBLOOM.get(), "Gravebloom");
    add(
            ModMobEffects.GRAVEBLOOM.get().getDescriptionId() + ".description",
            "Hostile mobs slain by the user briefly bloom the surrounding terrain with moss and magical plants.");

    add(ModMobEffects.ARACHNID_GRACE.get(), "Arachnid Grace");
    add(
            ModMobEffects.ARACHNID_GRACE.get().getDescriptionId() + ".description",
            "Allows vertical wall climbing and grants poison immunity. Cobwebs do not slow the user, but water weakens them.");

    add(ModMobEffects.BRAMBLEGUARD.get(), "Brambleguard");
    add(
            ModMobEffects.BRAMBLEGUARD.get().getDescriptionId() + ".description",
            "Grants increased magical and physical resistance per level. Removes and prevents Bleeding while active.");

    add(ModMobEffects.HOLLOW_SILENCE.get(), "Hollow Silence");
    add(
            ModMobEffects.HOLLOW_SILENCE.get().getDescriptionId() + ".description",
            "Silences the user's presence, allowing safe movement near sound-sensitive entities, but periodically clouds vision.");
  }

  private void addJeiTranslations() {
    add("jei.hexalia.category.mortar_and_pestle", "Mortar & Pestle");
    add("jei.hexalia.category.small_cauldron", "Small Cauldron Brewing");
    add("jei.hexalia.category.natures_ritual", "Nature's Ritual");
    add("jei.hexalia.category.celestial_infusion", "Celestial Infusion");
    add("jei.hexalia.category.mutation", "Mutation");

    add("jei.hexalia.tooltip.brew_time", "Brew Time: %s ticks");
    add("jei.hexalia.tooltip.experience", "Experience: %s");

    add(
            "jei.hexalia.tooltip.requires_hex_focus",
            "Requires activation with a Hex Focus.");

    add(
            "jei.hexalia.tooltip.requires_salted_brazier",
            "Requires a salted Ritual Brazier.");

    add(
            "jei.hexalia.tooltip.requires_salted_braziers",
            "Used Ritual Braziers must be salted.");

    add(
            "jei.hexalia.tooltip.requires_celestial_blooms",
            "Requires nearby Celestial Blooms.");

    add(
            "jei.hexalia.tooltip.requires_open_sky",
            "Requires an open view of the sky.");

    add(
            "jei.hexalia.tooltip.requires_mature_crops",
            "Requires mature crops nearby.");

    add(
            "jei.info.wild_sunfire_tomatoes",
            "Wild Sunfire Tomatoes can be found in savannas.");

    add(
            "jei.info.wild_mandrakes",
            "Wild Mandrakes can be found in forests.");

    add(
            "jei.info.chillberry_bushes",
            "Chillberries can be found in taigas.");

    add(
            "jei.info.ritual_brazier",
            "Can be obtained after right-clicking any planks block with an Athame while crouching.");

    add(
            "jei.info.ritual_table",
            "Can be obtained after right-clicking Deepslate or Cobbled Deepslate with a Hex Focus.");

    add(
            "jei.info.lotus_blossom",
            "Can be obtained after right-clicking Lotus Flowers with an Athame.");

    add(
            "jei.info.tree_resin",
            "Can be obtained after right-clicking Cottonwood Logs or Dark Oak Logs with an Athame.");

    add(
            "jei.info.silk_fiber",
            "Can be obtained after breeding Silk Moths.");
  }

  private void addEmiTranslations() {
    add("emi.category.hexalia.mutation", "Mutation");
    add("emi.category.hexalia.mortar_and_pestle", "Mortar and Pestle");
    add("emi.category.hexalia.ritual_brazier", "Ritual Brazier");
    add("emi.category.hexalia.ritual_table", "Ritual Table");
    add("emi.category.hexalia.small_cauldron", "Small Cauldron");
  }

  private void addTagTranslations() {
    add("tag.item.hexalia.herbs", "Herbs");
    add("tag.item.hexalia.crushed_herbs", "Crushed Herbs");
    add("tag.item.hexalia.brews", "Brews");

    add("tag.item.hexalia.cottonwood_logs", "Cottonwood Logs");
    add("tag.item.hexalia.willow_logs", "Willow Logs");

    add("tag.item.hexalia.offhand_equipment", "Offhand Equipment");
    add("tag.item.hexalia.tulips", "Tulips");

    add("tag.item.hexalia.stun_immune_headwear", "Stun-Immune Headwear");

    add("tag.item.c.foods", "Foods");
    add("tag.item.c.foods.bread", "Bread");
    add("tag.item.c.crops", "Crops");
    add("tag.item.c.foods.berry", "Berries");
    add("tag.item.c.foods.cooked_meat", "Cooked Meats");
    add("tag.item.c.foods.soup", "Soups");
    add("tag.item.c.foods.pie", "Pies");
    add("tag.item.c.foods.food_poisoning", "Food Poisoning Foods");
    add("tag.item.c.foods.vegetable", "Vegetables");

    add("tag.item.c.salt", "Salt");
    add("tag.item.c.seeds", "Seeds");
    add("tag.item.c.mushrooms", "Mushrooms");
    add("tag.item.c.salt_blocks", "Salt Blocks");

    add("tag.item.sereneseasons.autumn_crops", "Autumn Crops");
    add("tag.item.sereneseasons.spring_crops", "Spring Crops");
    add("tag.item.sereneseasons.summer_crops", "Summer Crops");
    add("tag.item.sereneseasons.winter_crops", "Winter Crops");

    add("tag.block.hexalia.heating_block", "Heating Blocks");
    add("tag.block.hexalia.attracts_moth", "Attracts Moths");

    add("tag.block.hexalia.cottonwood_logs", "Cottonwood Logs");
    add("tag.block.hexalia.willow_logs", "Willow Logs");

    add("tag.block.hexalia.spiritroot_bound_blocks", "Spiritroot Bound Blocks");
    add("tag.block.hexalia.bogshade_no_slow", "Bogshade No-Slow Blocks");

    add("tag.block.c.salt_blocks", "Salt Blocks");

    add("tag.block.sereneseasons.autumn_crops", "Autumn Crops");
    add("tag.block.sereneseasons.spring_crops", "Spring Crops");
    add("tag.block.sereneseasons.summer_crops", "Summer Crops");
    add("tag.block.sereneseasons.winter_crops", "Winter Crops");

    add(
            "tag.block.sereneseasons.unbreakable_infertile_crops",
            "Unbreakable Infertile Crops");

    add(
            "tag.entity_type.hexalia.spiritroot_uncapturable",
            "Spiritroot Uncapturable");

    add(
            "tag.entity_type.hexalia.undead_veil_immune",
            "Undead Veil Immune");
  }

  private void addMiscTranslations() {
    add(
            "item.hexalia.verdant_grimoire.landing_text",
            "A detailed book is your trusted tool in matters of witchcraft and plant care.");

    add("sounds.hexalia.mandrake_scream", "Mandrake screamed");
    add("sounds.hexalia.ritual_success", "Ritual completed");
    add("sounds.hexalia.conversion", "Block converted");
    add("sounds.hexalia.sac_impact", "Sac hits the ground");
    add("sounds.hexalia.cacofey_giggle", "Cacofey giggling");
  }
}
