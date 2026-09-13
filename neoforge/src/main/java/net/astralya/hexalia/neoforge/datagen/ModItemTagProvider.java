package net.astralya.hexalia.neoforge.datagen;

import java.util.concurrent.CompletableFuture;
import net.astralya.hexalia.Hexalia;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public final class ModItemTagProvider extends ItemTagsProvider {
  public ModItemTagProvider(
      PackOutput output,
      CompletableFuture<HolderLookup.Provider> lookupProvider,
      CompletableFuture<TagLookup<Block>> blockTags,
      ExistingFileHelper existingFileHelper) {
    super(output, lookupProvider, blockTags, Hexalia.MOD_ID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider lookupProvider) {
    tag(accessoriesTag("hat")).add(ModItems.EARPLUGS.get());
    tag(accessoriesTag("necklace"))
        .add(ModItems.SAGE_PENDANT.get(), ModItems.SEAFOAM_TALISMAN.get());
    tag(accessoriesTag("ring")).add(ModItems.MOONWARD_RING.get(), ModItems.GREEN_OMEN.get());
    tag(accessoriesTag("charm"))
        .add(
            ModItems.WITCHHEART_CLUSTER.get(), ModItems.WYRD_FEATHER.get());

    tag(ItemTags.FLOWERS)
        .add(ModBlocks.SPIRIT_BLOOM.get().asItem())
        .add(ModBlocks.WITCHWEED.get().asItem())
        .add(ModBlocks.GHOST_FERN.get().asItem())
        .add(ModBlocks.CELESTIAL_BLOOM.get().asItem())
        .add(ModBlocks.WITHERED_CELESTIAL_BLOOM.get().asItem())
        .add(ModBlocks.MORPHORA.get().asItem())
        .add(ModBlocks.GRIMSHADE.get().asItem())
        .add(ModBlocks.NAUTILITE.get().asItem())
        .add(ModBlocks.WINDSONG.get().asItem())
        .add(ModBlocks.ASTRYLIS.get().asItem())
        .add(ModBlocks.LOURDES.get().asItem())
        .add(ModBlocks.AEGIFLORA.get().asItem())
        .add(ModBlocks.WITHERED_AEGIFLORA.get().asItem())
        .add(ModBlocks.NIGHTSHADE_BUSH.get().asItem())
        .add(ModBlocks.BEGONIA.get().asItem())
        .add(ModBlocks.LAVENDER.get().asItem())
        .add(ModBlocks.DAHLIA.get().asItem());

    tag(ModTags.Items.COTTONWOOD_LOGS)
        .add(
            ModItems.COTTONWOOD_LOG.get(),
            ModItems.STRIPPED_COTTONWOOD_LOG.get(),
            ModItems.COTTONWOOD_WOOD.get(),
            ModItems.STRIPPED_COTTONWOOD_WOOD.get());
    tag(ModTags.Items.WILLOW_LOGS)
        .add(
            ModItems.WILLOW_LOG.get(),
            ModItems.STRIPPED_WILLOW_LOG.get(),
            ModItems.WILLOW_WOOD.get(),
            ModItems.STRIPPED_WILLOW_WOOD.get());
    tag(ItemTags.LOGS_THAT_BURN)
        .addTag(ModTags.Items.COTTONWOOD_LOGS)
        .addTag(ModTags.Items.WILLOW_LOGS);
    tag(ItemTags.PLANKS).add(ModItems.COTTONWOOD_PLANKS.get(), ModItems.WILLOW_PLANKS.get());
    tag(ItemTags.WOODEN_STAIRS).add(ModItems.COTTONWOOD_STAIRS.get(), ModItems.WILLOW_STAIRS.get());
    tag(ItemTags.WOODEN_SLABS).add(ModItems.COTTONWOOD_SLAB.get(), ModItems.WILLOW_SLAB.get());
    tag(ItemTags.WOODEN_BUTTONS).add(ModItems.COTTONWOOD_BUTTON.get(), ModItems.WILLOW_BUTTON.get());
    tag(ItemTags.WOODEN_PRESSURE_PLATES)
        .add(ModItems.COTTONWOOD_PRESSURE_PLATE.get(), ModItems.WILLOW_PRESSURE_PLATE.get());
    tag(ItemTags.WOODEN_FENCES).add(ModItems.COTTONWOOD_FENCE.get(), ModItems.WILLOW_FENCE.get());
    tag(ItemTags.FENCE_GATES)
        .add(ModItems.COTTONWOOD_FENCE_GATE.get(), ModItems.WILLOW_FENCE_GATE.get());
    tag(ItemTags.WOODEN_DOORS).add(ModItems.COTTONWOOD_DOOR.get(), ModItems.WILLOW_DOOR.get());
    tag(ItemTags.WOODEN_TRAPDOORS)
        .add(ModItems.COTTONWOOD_TRAPDOOR.get(), ModItems.WILLOW_TRAPDOOR.get());
    tag(ItemTags.SAPLINGS).add(ModItems.COTTONWOOD_SAPLING.get(), ModItems.WILLOW_SAPLING.get());
    tag(ItemTags.LEAVES).add(ModItems.COTTONWOOD_LEAVES.get(), ModItems.WILLOW_LEAVES.get());
    tag(ItemTags.SIGNS).add(ModItems.COTTONWOOD_SIGN.get(), ModItems.WILLOW_SIGN.get());
    tag(ItemTags.HANGING_SIGNS)
        .add(ModItems.COTTONWOOD_HANGING_SIGN.get(), ModItems.WILLOW_HANGING_SIGN.get());
    tag(ItemTags.BOATS).add(ModItems.COTTONWOOD_BOAT.get(), ModItems.WILLOW_BOAT.get());
    tag(ItemTags.CHEST_BOATS)
        .add(ModItems.COTTONWOOD_CHEST_BOAT.get(), ModItems.WILLOW_CHEST_BOAT.get());

    tag(ItemTags.SMALL_FLOWERS)
        .add(ModBlocks.SPIRIT_BLOOM.get().asItem())
        .add(ModBlocks.WITCHWEED.get().asItem())
        .add(ModBlocks.GHOST_FERN.get().asItem())
        .add(ModBlocks.CELESTIAL_BLOOM.get().asItem())
        .add(ModBlocks.WITHERED_CELESTIAL_BLOOM.get().asItem())
        .add(ModBlocks.MORPHORA.get().asItem())
        .add(ModBlocks.GRIMSHADE.get().asItem())
        .add(ModBlocks.NAUTILITE.get().asItem())
        .add(ModBlocks.WINDSONG.get().asItem())
        .add(ModBlocks.ASTRYLIS.get().asItem())
        .add(ModBlocks.LOURDES.get().asItem())
        .add(ModBlocks.AEGIFLORA.get().asItem())
        .add(ModBlocks.WITHERED_AEGIFLORA.get().asItem())
        .add(ModBlocks.NIGHTSHADE_BUSH.get().asItem())
        .add(ModBlocks.BEGONIA.get().asItem())
        .add(ModBlocks.LAVENDER.get().asItem())
        .add(ModBlocks.DAHLIA.get().asItem());

    tag(ItemTags.VILLAGER_PLANTABLE_SEEDS)
        .add(ModItems.RABBAGE_SEEDS.get())
        .add(ModItems.SUNFIRE_TOMATO_SEEDS.get())
        .add(ModItems.MANDRAKE_SEEDS.get());

    tag(ItemTags.SHOVELS).add(ModItems.ROOTSHAPER.get());
    tag(ItemTags.PICKAXES).add(ModItems.ROOTSHAPER.get());
    tag(ItemTags.SWORDS).add(ModItems.KELPWEAVE_BLADE.get());
    tag(ItemTags.AXES).add(ModItems.CINDERHEW.get());
    tag(Tags.Items.RANGED_WEAPON_TOOLS).add(ModItems.THORNBOW.get());
    tag(Tags.Items.MELEE_WEAPON_TOOLS)
        .add(ModItems.CINDERHEW.get())
        .add(ModItems.KELPWEAVE_BLADE.get());

    tag(ItemTags.HEAD_ARMOR)
        .add(ModItems.EARPLUGS.get())
        .add(ModItems.SILKWEAVE_HOOD.get())
        .add(ModItems.MOONWEAVE_HOOD.get())
        .add(ModItems.BLOOMWRAP_HAT.get());
    tag(ItemTags.CHEST_ARMOR)
        .add(ModItems.GHOSTVEIL.get())
        .add(ModItems.SILKWEAVE_MANTLE.get())
        .add(ModItems.MOONWEAVE_MANTLE.get())
        .add(ModItems.BLOOMWRAP_ROBES.get());
    tag(ItemTags.LEG_ARMOR)
        .add(ModItems.SILKWEAVE_BINDINGS.get())
        .add(ModItems.MOONWEAVE_BINDINGS.get())
        .add(ModItems.BLOOMWRAP_LEGGINGS.get());
    tag(ItemTags.FOOT_ARMOR)
        .add(ModItems.BOGSHADE_BOOTS.get())
        .add(ModItems.SILKWEAVE_FOOTWRAPS.get())
        .add(ModItems.MOONWEAVE_FOOTWRAPS.get())
        .add(ModItems.BLOOMWRAP_BOOTS.get());

    tag(ItemTags.MINING_ENCHANTABLE)
        .add(ModItems.ROOTSHAPER.get())
        .add(ModItems.CINDERHEW.get())
        .add(ModItems.BRIAR_SICKLE.get());
    tag(ItemTags.DURABILITY_ENCHANTABLE)
        .add(ModItems.ATHAME.get())
        .add(ModItems.ROOTSHAPER.get())
        .add(ModItems.KELPWEAVE_BLADE.get())
        .add(ModItems.CINDERHEW.get())
        .add(ModItems.BRIAR_SICKLE.get())
        .add(ModItems.SPIRITROOT_TETHER.get())
        .add(ModItems.SAGE_PENDANT.get())
        .add(ModItems.PURIFYING_SAC.get())
        .add(ModItems.THORNBOW.get())
        .add(ModItems.EARPLUGS.get())
        .add(ModItems.GHOSTVEIL.get())
        .add(ModItems.BOGSHADE_BOOTS.get())
        .add(ModItems.SILKWEAVE_HOOD.get())
        .add(ModItems.SILKWEAVE_MANTLE.get())
        .add(ModItems.SILKWEAVE_BINDINGS.get())
        .add(ModItems.SILKWEAVE_FOOTWRAPS.get())
        .add(ModItems.MOONWEAVE_HOOD.get())
        .add(ModItems.MOONWEAVE_MANTLE.get())
        .add(ModItems.MOONWEAVE_BINDINGS.get())
        .add(ModItems.MOONWEAVE_FOOTWRAPS.get())
        .add(ModItems.BLOOMWRAP_HAT.get())
        .add(ModItems.BLOOMWRAP_ROBES.get())
        .add(ModItems.BLOOMWRAP_LEGGINGS.get())
        .add(ModItems.BLOOMWRAP_BOOTS.get());
    tag(ItemTags.SWORD_ENCHANTABLE).add(ModItems.KELPWEAVE_BLADE.get());
    tag(ItemTags.MINING_LOOT_ENCHANTABLE).add(ModItems.CINDERHEW.get());
    tag(ItemTags.BOW_ENCHANTABLE).add(ModItems.THORNBOW.get());
    tag(ItemTags.HEAD_ARMOR_ENCHANTABLE)
        .add(ModItems.EARPLUGS.get())
        .add(ModItems.SILKWEAVE_HOOD.get())
        .add(ModItems.MOONWEAVE_HOOD.get())
        .add(ModItems.BLOOMWRAP_HAT.get());
    tag(ModTags.Items.STUN_IMMUNE_HEADWEAR)
        .add(ModItems.EARPLUGS.get())
        .add(ModItems.BLOOMWRAP_HAT.get());
    tag(ItemTags.CHEST_ARMOR_ENCHANTABLE)
        .add(ModItems.GHOSTVEIL.get())
        .add(ModItems.SILKWEAVE_MANTLE.get())
        .add(ModItems.MOONWEAVE_MANTLE.get())
        .add(ModItems.BLOOMWRAP_ROBES.get());
    tag(ItemTags.LEG_ARMOR_ENCHANTABLE)
        .add(ModItems.SILKWEAVE_BINDINGS.get())
        .add(ModItems.MOONWEAVE_BINDINGS.get())
        .add(ModItems.BLOOMWRAP_LEGGINGS.get());
    tag(ItemTags.FOOT_ARMOR_ENCHANTABLE)
        .add(ModItems.BOGSHADE_BOOTS.get())
        .add(ModItems.SILKWEAVE_FOOTWRAPS.get())
        .add(ModItems.MOONWEAVE_FOOTWRAPS.get())
        .add(ModItems.BLOOMWRAP_BOOTS.get());

    tag(ModTags.Items.HERBS)
        .add(ModBlocks.WITCHWEED.get().asItem())
        .add(ModBlocks.SPIRIT_BLOOM.get().asItem())
        .add(ModBlocks.DREAMSHROOM.get().asItem())
        .add(ModItems.SIREN_KELP.get())
        .add(ModBlocks.GHOST_FERN.get().asItem())
        .add(ModBlocks.CELESTIAL_BLOOM.get().asItem())
        .add(ModItems.LOTUS_FLOWER.get());
    tag(ModTags.Items.HERB_JAR_STORABLE)
        .addTag(ModTags.Items.HERBS)
        .addTag(ModTags.Items.CRUSHED_HERBS);

    tag(ModTags.Items.CRUSHED_HERBS)
        .add(ModItems.SPIRIT_POWDER.get())
        .add(ModItems.SIREN_PASTE.get())
        .add(ModItems.DREAM_PASTE.get())
        .add(ModItems.GHOST_POWDER.get());

    tag(ModTags.Items.BREWS)
        .add(ModItems.BREW_OF_HOMESTEAD.get())
        .add(ModItems.BREW_OF_BLOODLUST.get())
        .add(ModItems.BREW_OF_SLIMEWALKER.get())
        .add(ModItems.BREW_OF_SPIKESKIN.get())
        .add(ModItems.BREW_OF_SIPHON.get())
        .add(ModItems.BREW_OF_DAYBLOOM.get())
        .add(ModItems.BREW_OF_GRAVEBLOOM.get())
        .add(ModItems.BREW_OF_ARACHNID_GRACE.get())
        .add(ModItems.BREW_OF_HOLLOW_SILENCE.get())
        .add(ModItems.RUSTIC_BOTTLE.get());

    tag(ModTags.Items.SALT).add(ModItems.SALT.get());
    tag(ModTags.Items.SALT_BLOCKS).add(ModItems.SALT_BLOCK.get());
    tag(ModTags.Items.MUSHROOMS)
        .add(ModBlocks.DREAMSHROOM.get().asItem())
        .add(ModBlocks.PALE_MUSHROOM.get().asItem());
    tag(ModTags.Items.OFFHAND_EQUIPMENT)
        .add(ModItems.HEX_FOCUS.get())
        .add(ModItems.ATHAME.get())
        .add(ModItems.SAGE_PENDANT.get())
        .addTag(ModTags.Items.SALT);

    tag(ModTags.Items.TULIPS)
        .add(Blocks.ORANGE_TULIP.asItem())
        .add(Blocks.PINK_TULIP.asItem())
        .add(Blocks.RED_TULIP.asItem())
        .add(Blocks.WHITE_TULIP.asItem());

    tag(ModTags.Items.CROPS)
        .add(ModItems.MANDRAKE.get())
        .add(ModItems.SUNFIRE_TOMATO.get())
        .add(ModItems.RABBAGE.get())
        .add(ModItems.SALTSPROUT.get());

    tag(ModTags.Items.SEEDS)
        .add(ModItems.MANDRAKE_SEEDS.get())
        .add(ModItems.SUNFIRE_TOMATO_SEEDS.get())
        .add(ModItems.RABBAGE_SEEDS.get());

    tag(ModTags.Items.FOODS)
        .add(ModItems.CHILLBERRIES.get())
        .add(ModItems.SUNFIRE_TOMATO.get())
        .add(ModItems.SALTSPROUT.get())
        .add(ModItems.GALEBERRIES.get())
        .add(ModItems.SPICY_SANDWICH.get())
        .add(ModItems.CHILLBERRY_PIE.get())
        .add(ModItems.MANDRAKE_STEW.get())
        .add(ModItems.GALEBERRIES_COOKIE.get());

    tag(ModTags.Items.FOODS_BERRY).add(ModItems.CHILLBERRIES.get()).add(ModItems.GALEBERRIES.get());
    tag(ModTags.Items.FOODS_SOUP).add(ModItems.MANDRAKE_STEW.get());
    tag(ModTags.Items.FOODS_PIE).add(ModItems.CHILLBERRY_PIE.get());
    tag(ModTags.Items.FOODS_FOOD_POISONING).add(ModItems.SALTSPROUT.get());

    tag(ItemTags.FOX_FOOD).add(ModItems.CHILLBERRIES.get()).add(ModItems.GALEBERRIES.get());

    tag(ModTags.Items.FOODS_BREAD).add(Items.BREAD);

    tag(ModTags.Items.FOODS_COOKED_MEAT)
        .add(Items.COOKED_BEEF)
        .add(Items.COOKED_CHICKEN)
        .add(Items.COOKED_MUTTON)
        .add(Items.COOKED_PORKCHOP)
        .add(Items.COOKED_RABBIT)
        .add(Items.COOKED_COD)
        .add(Items.COOKED_SALMON);

    tag(ModTags.Items.FOODS_VEGETABLE)
        .add(ModItems.SUNFIRE_TOMATO.get())
        .add(ModItems.RABBAGE.get())
        .add(Items.CARROT)
        .add(Items.POTATO);

    tag(ModTags.Compat.SERENE_SEASONS_SPRING_CROPS)
        .add(ModItems.RABBAGE_SEEDS.get())
        .add(ModItems.MANDRAKE_SEEDS.get());
    tag(ModTags.Compat.SERENE_SEASONS_SUMMER_CROPS)
        .add(ModItems.RABBAGE_SEEDS.get())
        .add(ModItems.MANDRAKE_SEEDS.get())
        .add(ModItems.SUNFIRE_TOMATO_SEEDS.get());
    tag(ModTags.Compat.SERENE_SEASONS_AUTUMN_CROPS)
        .add(ModItems.RABBAGE_SEEDS.get())
        .add(ModItems.SUNFIRE_TOMATO_SEEDS.get());
    tag(ModTags.Compat.SERENE_SEASONS_WINTER_CROPS).add(ModItems.RABBAGE_SEEDS.get());
  }

  private static TagKey<Item> accessoriesTag(String path) {
    return TagKey.create(
        Registries.ITEM, ResourceLocation.fromNamespaceAndPath("accessories", path));
  }
}
