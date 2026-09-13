package net.astralya.hexalia.neoforge.datagen;

import net.astralya.hexalia.Hexalia;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public final class ModItemModelProvider extends ItemModelProvider {
  public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
    super(output, Hexalia.MOD_ID, existingFileHelper);
  }

  @Override
  protected void registerModels() {
    basicItem(ModItems.HEX_FOCUS.get());
    basicItem(ModItems.SALT.get());
    basicItem(ModItems.TREE_RESIN.get());
    basicItem(ModItems.SILK_FIBER.get());
    basicItem(ModItems.SILKWORM.get());
    basicItem(ModItems.FRAGRANT_NECTAR.get());
    basicItem(ModItems.ANCIENT_SEED.get());
    basicItem(ModItems.SIREN_PASTE.get());
    basicItem(ModItems.DREAM_PASTE.get());
    basicItem(ModItems.CELESTIAL_CRYSTAL.get());
    basicItem(ModItems.VERDANT_GRIMOIRE.get());
    basicItem(ModItems.FIRE_NODE.get());
    basicItem(ModItems.WATER_NODE.get());
    basicItem(ModItems.AIR_NODE.get());
    basicItem(ModItems.EARTH_NODE.get());
    basicItem(ModItems.LADLE.get());
    handheldItem(ModItems.ATHAME.get());
    handheldItem(ModItems.KELPWEAVE_BLADE.get());
    handheldItem(ModItems.CINDERHEW.get());
    handheldItem(ModItems.BRIAR_SICKLE.get());
    basicItem(ModItems.SAGE_PENDANT.get());
    basicItem(ModItems.SEAFOAM_TALISMAN.get());
    basicItem(ModItems.MOONWARD_RING.get());
    basicItem(ModItems.WITCHHEART_CLUSTER.get());
    basicItem(ModItems.WYRD_FEATHER.get());
    basicItem(ModItems.GREEN_OMEN.get());
    basicItem(ModItems.SILK_MOTH_SPAWN_EGG.get());
    basicItem(ModItems.CACOFEY_SPAWN_EGG.get());
    basicItem(ModItems.GHOSTVEIL.get());
    basicItem(ModItems.EARPLUGS.get());
    basicItem(ModItems.BOGSHADE_BOOTS.get());
    basicItem(ModItems.SILKWEAVE_HOOD.get());
    basicItem(ModItems.SILKWEAVE_MANTLE.get());
    basicItem(ModItems.SILKWEAVE_BINDINGS.get());
    basicItem(ModItems.SILKWEAVE_FOOTWRAPS.get());
    basicItem(ModItems.MOONWEAVE_HOOD.get());
    basicItem(ModItems.MOONWEAVE_MANTLE.get());
    basicItem(ModItems.MOONWEAVE_BINDINGS.get());
    basicItem(ModItems.MOONWEAVE_FOOTWRAPS.get());
    basicItem(ModItems.BLOOMWRAP_HAT.get());
    basicItem(ModItems.BLOOMWRAP_ROBES.get());
    basicItem(ModItems.BLOOMWRAP_LEGGINGS.get());
    basicItem(ModItems.BLOOMWRAP_BOOTS.get());
    basicItem(ModItems.SILK_IDOL.get());
    basicItem(ModItems.CLARITY_IDOL.get());
    basicItem(ModItems.RAINFALL_IDOL.get());
    basicItem(ModItems.TEMPEST_IDOL.get());
    basicItem(ModItems.PURITY_IDOL.get());
    basicItem(ModItems.RUSTIC_BOTTLE.get());
    basicItem(ModItems.HEARTSEED.get());
    basicItem(ModItems.BREW_OF_SPIKESKIN.get());
    basicItem(ModItems.BREW_OF_BLOODLUST.get());
    basicItem(ModItems.BREW_OF_SLIMEWALKER.get());
    basicItem(ModItems.BREW_OF_SIPHON.get());
    basicItem(ModItems.BREW_OF_DAYBLOOM.get());
    basicItem(ModItems.BREW_OF_GRAVEBLOOM.get());
    basicItem(ModItems.BREW_OF_ARACHNID_GRACE.get());
    basicItem(ModItems.BREW_OF_HOMESTEAD.get());
    basicItem(ModItems.BREW_OF_HOLLOW_SILENCE.get());
    basicItem(ModItems.BRAMBLEGUARD_SALVE.get());
    basicItem(ModItems.MENDERS_SALVE.get());
    basicItem(ModItems.LOTUS_BLOSSOM.get());
    basicItem(ModItems.SPIRIT_POWDER.get());
    basicItem(ModItems.GHOST_POWDER.get());
    basicItem(ModItems.LOTUS_FLOWER.get());
    basicItem(ModItems.SIREN_KELP.get());
    blockTextureItem(ModItems.MORPHORA.get());
    blockTextureItem(ModItems.GRIMSHADE.get());
    basicItem(ModItems.NAUTILITE.get());
    blockTextureItem(ModItems.WINDSONG.get());
    blockTextureItem(ModItems.ASTRYLIS.get());
    blockTextureItem(ModItems.LOURDES.get());
    blockTextureItem(ModItems.AEGIFLORA.get());
    blockTextureItem(ModItems.WITHERED_AEGIFLORA.get());
    blockTextureItem(ModItems.BEGONIA.get());
    blockTextureItem(ModItems.LAVENDER.get());
    blockTextureItem(ModItems.DAHLIA.get());
    blockTextureItem(ModItems.NIGHTSHADE_BUSH.get());
    blockTextureItem(ModItems.SPIRIT_BLOOM.get());
    blockTextureItem(ModItems.DREAMSHROOM.get());
    blockTextureItem(ModItems.GHOST_FERN.get());
    blockTextureItem(ModItems.CELESTIAL_BLOOM.get());
    blockTextureItem(ModItems.WITCHWEED.get());
    blockTextureItem(ModItems.WITHERED_CELESTIAL_BLOOM.get());
    basicItem(ModItems.MUTAVIS.get());
    basicItem(ModItems.MORTAR_AND_PESTLE.get());
    basicItem(ModItems.MANDRAKE.get());
    basicItem(ModItems.MANDRAKE_SEEDS.get());
    basicItem(ModItems.SUNFIRE_TOMATO.get());
    basicItem(ModItems.SUNFIRE_TOMATO_SEEDS.get());
    basicItem(ModItems.RABBAGE.get());
    basicItem(ModItems.RABBAGE_SEEDS.get());
    basicItem(ModItems.PURIFYING_SAC.get());
    basicItem(ModItems.FOUL_SAC.get());
    basicItem(ModItems.FROST_SAC.get());
    basicItem(ModItems.SEARING_SAC.get());
    basicItem(ModItems.CHILLBERRIES.get());
    basicItem(ModItems.GALEBERRIES.get());
    basicItem(ModItems.SPICY_SANDWICH.get());
    basicItem(ModItems.CHILLBERRY_PIE.get());
    basicItem(ModItems.MANDRAKE_STEW.get());
    basicItem(ModItems.GALEBERRIES_COOKIE.get());
    basicItem(ModItems.COTTONWOOD_BOAT.get());
    basicItem(ModItems.COTTONWOOD_CHEST_BOAT.get());
    basicItem(ModItems.WILLOW_BOAT.get());
    basicItem(ModItems.WILLOW_CHEST_BOAT.get());
    basicItem(ModItems.SALTSPROUT.get());
    basicItem(ModItems.SALT_LAMP.get());
    basicItem(ModItems.CANDLE_SKULL.get());
    basicItem(ModItems.WITHER_CANDLE_SKULL.get());
    blockTextureItem(ModItems.COTTONWOOD_CATKIN.get());
    blockTextureItem(ModItems.COTTONWOOD_SAPLING.get());
    basicItem(ModItems.COTTONWOOD_DOOR.get());
    blockTextureItem(ModItems.WILLOW_SAPLING.get());
    basicItem(ModItems.WILLOW_DOOR.get());
  }

  private void blockTextureItem(Item item) {
    String name = BuiltInRegistries.ITEM.getKey(item).getPath();
    getBuilder(name)
        .parent(getExistingFile(mcLoc("item/generated")))
        .texture("layer0", modLoc("block/" + name));
  }
}
