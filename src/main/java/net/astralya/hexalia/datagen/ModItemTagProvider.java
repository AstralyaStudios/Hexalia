package net.astralya.hexalia.datagen;

import java.util.concurrent.CompletableFuture;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {

    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        addVanillaTags();
        addToolsAndEnchantmentsTags();
        addCustomTags();
        addTreeRelatedTags();
        addFoodTags();
        addSignAndBoatTags();
        addSereneSeasonsTags();
    }

    private void addVanillaTags() {
        getOrCreateTagBuilder(ItemTags.FOX_FOOD)
                .add(ModItems.CHILLBERRIES)
                .add(ModItems.GALEBERRIES);

        getOrCreateTagBuilder(ItemTags.FLOWERS)
                .add(ModBlocks.SPIRIT_BLOOM.asItem())
                .add(ModBlocks.WITCHWEED.asItem())
                .add(ModBlocks.GHOST_FERN.asItem())
                .add(ModBlocks.CELESTIAL_BLOOM.asItem())
                .add(ModBlocks.WITHERED_CELESTIAL_BLOOM.asItem())
                .add(ModBlocks.NIGHTSHADE_BUSH.asItem())
                .add(ModBlocks.BEGONIA.asItem())
                .add(ModBlocks.LAVENDER.asItem())
                .add(ModBlocks.AEGIFLORA.asItem())
                .add(ModBlocks.WITHERED_AEGIFLORA.asItem())
                .add(ModBlocks.MORPHORA.asItem())
                .add(ModBlocks.WINDSONG.asItem())
                .add(ModBlocks.LOURDES.asItem())
                .add(ModBlocks.GRIMSHADE.asItem())
                .add(ModBlocks.CELESTIAL_BLOOM.asItem())
                .add(ModBlocks.ASTRYLIS.asItem())
                .add(ModBlocks.DAHLIA.asItem());

        getOrCreateTagBuilder(ItemTags.SMALL_FLOWERS)
                .add(ModBlocks.SPIRIT_BLOOM.asItem())
                .add(ModBlocks.WITCHWEED.asItem())
                .add(ModBlocks.GHOST_FERN.asItem())
                .add(ModBlocks.CELESTIAL_BLOOM.asItem())
                .add(ModBlocks.WITHERED_CELESTIAL_BLOOM.asItem())
                .add(ModBlocks.NIGHTSHADE_BUSH.asItem())
                .add(ModBlocks.BEGONIA.asItem())
                .add(ModBlocks.LAVENDER.asItem())
                .add(ModBlocks.AEGIFLORA.asItem())
                .add(ModBlocks.WITHERED_AEGIFLORA.asItem())
                .add(ModBlocks.MORPHORA.asItem())
                .add(ModBlocks.WINDSONG.asItem())
                .add(ModBlocks.LOURDES.asItem())
                .add(ModBlocks.GRIMSHADE.asItem())
                .add(ModBlocks.CELESTIAL_BLOOM.asItem())
                .add(ModBlocks.ASTRYLIS.asItem())
                .add(ModBlocks.DAHLIA.asItem());

        getOrCreateTagBuilder(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .add(ModItems.RABBAGE_SEEDS)
                .add(ModItems.SUNFIRE_TOMATO_SEEDS)
                .add(ModItems.MANDRAKE_SEEDS);
    }

    private void addToolsAndEnchantmentsTags() {
        getOrCreateTagBuilder(ItemTags.SHOVELS)
                .add(ModItems.ROOTSHAPER);

        getOrCreateTagBuilder(ItemTags.PICKAXES)
                .add(ModItems.ROOTSHAPER);

        getOrCreateTagBuilder(ItemTags.SWORDS)
                .add(ModItems.KELPWEAVE_BLADE);
    }

    private void addCustomTags() {
        getOrCreateTagBuilder(ModTags.Items.HERBS)
                .add(ModBlocks.WITCHWEED.asItem())
                .add(ModBlocks.SPIRIT_BLOOM.asItem())
                .add(ModBlocks.DREAMSHROOM.asItem())
                .add(ModItems.SIREN_KELP)
                .add(ModBlocks.GHOST_FERN.asItem())
                .add(ModBlocks.CELESTIAL_BLOOM.asItem())
                .add(ModItems.LOTUS_FLOWER);

        getOrCreateTagBuilder(ModTags.Items.CRUSHED_HERBS)
                .add(ModItems.DREAM_PASTE)
                .add(ModItems.SIREN_PASTE)
                .add(ModItems.SPIRIT_POWDER)
                .add(ModItems.GHOST_POWDER);

        getOrCreateTagBuilder(ModTags.Items.HERB_JAR_STORABLE)
                .addTag(ModTags.Items.HERBS)
                .addTag(ModTags.Items.CRUSHED_HERBS);

        getOrCreateTagBuilder(ModTags.Items.BREWS)
                .add(ModItems.BREW_OF_HOMESTEAD)
                .add(ModItems.BREW_OF_BLOODLUST)
                .add(ModItems.BREW_OF_SLIMEWALKER)
                .add(ModItems.BREW_OF_SPIKESKIN)
                .add(ModItems.BREW_OF_SIPHON)
                .add(ModItems.BREW_OF_DAYBLOOM)
                .add(ModItems.BREW_OF_GRAVEBLOOM)
                .add(ModItems.BREW_OF_ARACHNID_GRACE)
                .add(ModItems.BREW_OF_HOLLOW_SILENCE)
                .add(ModItems.RUSTIC_BOTTLE);

        getOrCreateTagBuilder(ModTags.Items.SALT_DUSTS)
                .add(ModItems.SALT);

        getOrCreateTagBuilder(ModTags.Items.SALT_BLOCKS)
                .add(ModBlocks.SALT_BLOCK.asItem());

        getOrCreateTagBuilder(ModTags.Items.MUSHROOMS)
                .add(ModBlocks.DREAMSHROOM.asItem())
                .add(ModBlocks.PALE_MUSHROOM.asItem());

        getOrCreateTagBuilder(ModTags.Items.OFFHAND_EQUIPMENT)
                .add(ModItems.HEX_FOCUS.asItem())
                .add(ModItems.SALT.asItem());

        getOrCreateTagBuilder(ModTags.Items.TULIPS)
                .add(Blocks.ORANGE_TULIP.asItem())
                .add(Blocks.PINK_TULIP.asItem())
                .add(Blocks.RED_TULIP.asItem())
                .add(Blocks.WHITE_TULIP.asItem());

        getOrCreateTagBuilder(ModTags.Items.STUN_IMMUNE_HEADWEAR)
                .add(ModItems.EARPLUGS)
                .add(ModItems.BLOOMWRAP_HAT);
    }

    private void addTreeRelatedTags() {
        getOrCreateTagBuilder(ModTags.Items.COTTONWOOD_LOGS)
                .add(ModBlocks.COTTONWOOD_LOG.asItem())
                .add(ModBlocks.STRIPPED_COTTONWOOD_LOG.asItem())
                .add(ModBlocks.COTTONWOOD_WOOD.asItem())
                .add(ModBlocks.STRIPPED_COTTONWOOD_WOOD.asItem());

        getOrCreateTagBuilder(ModTags.Items.WILLOW_LOGS)
                .add(ModBlocks.WILLOW_LOG.asItem())
                .add(ModBlocks.STRIPPED_WILLOW_LOG.asItem())
                .add(ModBlocks.WILLOW_WOOD.asItem())
                .add(ModBlocks.STRIPPED_WILLOW_WOOD.asItem());

        getOrCreateTagBuilder(ItemTags.LOGS_THAT_BURN)
                .addTag(ModTags.Items.COTTONWOOD_LOGS)
                .addTag(ModTags.Items.WILLOW_LOGS);

        getOrCreateTagBuilder(ItemTags.SAPLINGS)
                .add(ModBlocks.COTTONWOOD_SAPLING.asItem())
                .add(ModBlocks.WILLOW_SAPLING.asItem());

        getOrCreateTagBuilder(ItemTags.LEAVES)
                .add(ModBlocks.COTTONWOOD_LEAVES.asItem())
                .add(ModBlocks.WILLOW_LEAVES.asItem());

        getOrCreateTagBuilder(ItemTags.PLANKS)
                .add(ModBlocks.COTTONWOOD_PLANKS.asItem())
                .add(ModBlocks.WILLOW_PLANKS.asItem());

        getOrCreateTagBuilder(ItemTags.WOODEN_STAIRS)
                .add(ModBlocks.COTTONWOOD_STAIRS.asItem())
                .add(ModBlocks.WILLOW_STAIRS.asItem());

        getOrCreateTagBuilder(ItemTags.WOODEN_SLABS)
                .add(ModBlocks.COTTONWOOD_SLAB.asItem())
                .add(ModBlocks.WILLOW_SLAB.asItem());

        getOrCreateTagBuilder(ItemTags.WOODEN_DOORS)
                .add(ModBlocks.COTTONWOOD_DOOR.asItem(), ModBlocks.WILLOW_DOOR.asItem());

        getOrCreateTagBuilder(ItemTags.WOODEN_BUTTONS)
                .add(ModBlocks.COTTONWOOD_BUTTON.asItem(), ModBlocks.COTTONWOOD_BUTTON.asItem());

        getOrCreateTagBuilder(ItemTags.WOODEN_PRESSURE_PLATES)
                .add(ModBlocks.COTTONWOOD_PRESSURE_PLATE.asItem(), ModBlocks.WILLOW_PRESSURE_PLATE.asItem());

        getOrCreateTagBuilder(ItemTags.WOODEN_TRAPDOORS)
                .add(ModBlocks.COTTONWOOD_TRAPDOOR.asItem(), ModBlocks.WILLOW_TRAPDOOR.asItem());

        getOrCreateTagBuilder(ItemTags.FENCE_GATES)
                .add(ModBlocks.COTTONWOOD_FENCE_GATE.asItem(), ModBlocks.WILLOW_FENCE_GATE.asItem());

        getOrCreateTagBuilder(ItemTags.WOODEN_FENCES)
                .add(ModBlocks.COTTONWOOD_FENCE.asItem(), ModBlocks.WILLOW_FENCE.asItem());
    }

    private void addFoodTags() {
        getOrCreateTagBuilder(ModTags.Items.FOODS)
                .add(ModItems.SIREN_KELP)
                .add(ModItems.CHILLBERRIES)
                .add(ModItems.SUNFIRE_TOMATO)
                .add(ModItems.GALEBERRIES)
                .add(ModItems.SALTSPROUT)
                .add(ModItems.CHILLBERRY_PIE)
                .add(ModItems.MANDRAKE_STEW)
                .add(ModItems.GALEBERRIES_COOKIE);

        getOrCreateTagBuilder(ModTags.Items.CROPS)
                .add(ModItems.SALTSPROUT);

        getOrCreateTagBuilder(ModTags.Items.CROPS_TOMATO)
                .add(ModItems.SUNFIRE_TOMATO);

        getOrCreateTagBuilder(ModTags.Items.FOODS_BERRIES)
                .add(ModItems.CHILLBERRIES)
                .add(ModItems.GALEBERRIES);

        getOrCreateTagBuilder(ModTags.Items.FOODS_VEGETABLES)
                .add(ModItems.SUNFIRE_TOMATO)
                .add(Items.CARROT)
                .add(Items.POTATO);

        getOrCreateTagBuilder(ModTags.Items.FOODS_VEGETABLES_TOMATO)
                .add(ModItems.SUNFIRE_TOMATO);

        getOrCreateTagBuilder(ModTags.Items.FOODS_COOKED_MEATS)
                .add(Items.COOKED_BEEF)
                .add(Items.COOKED_CHICKEN)
                .add(Items.COOKED_MUTTON)
                .add(Items.COOKED_PORKCHOP)
                .add(Items.COOKED_RABBIT)
                .add(Items.COOKED_COD)
                .add(Items.COOKED_SALMON);

        getOrCreateTagBuilder(ModTags.Items.FOODS_BREADS)
                .add(Items.BREAD);

        getOrCreateTagBuilder(ModTags.Items.BERRIES)
                .add(ModItems.CHILLBERRIES);

        getOrCreateTagBuilder(ModTags.Items.SEEDS)
                .add(ModItems.SUNFIRE_TOMATO_SEEDS)
                .add(ModItems.RABBAGE_SEEDS)
                .add(ModItems.MANDRAKE_SEEDS);
    }

    private void addSignAndBoatTags() {
        getOrCreateTagBuilder(ItemTags.BOATS)
                .add(ModItems.COTTONWOOD_BOAT)
                .add(ModItems.WILLOW_BOAT);

        getOrCreateTagBuilder(ItemTags.CHEST_BOATS)
                .add(ModItems.COTTONWOOD_CHEST_BOAT)
                .add(ModItems.WILLOW_CHEST_BOAT);

        getOrCreateTagBuilder(ItemTags.SIGNS)
                .add(ModItems.COTTONWOOD_SIGN)
                .add(ModItems.WILLOW_SIGN);

        getOrCreateTagBuilder(ItemTags.HANGING_SIGNS)
                .add(ModItems.COTTONWOOD_HANGING_SIGN)
                .add(ModItems.WILLOW_HANGING_SIGN);
    }

    private void addSereneSeasonsTags() {
        getOrCreateTagBuilder(ModTags.Compat.SERENE_SEASONS_SPRING_CROPS)
                .add(ModItems.RABBAGE_SEEDS)
                .add(ModItems.MANDRAKE_SEEDS);

        getOrCreateTagBuilder(ModTags.Compat.SERENE_SEASONS_SUMMER_CROPS)
                .add(ModItems.RABBAGE_SEEDS)
                .add(ModItems.MANDRAKE_SEEDS)
                .add(ModItems.SUNFIRE_TOMATO_SEEDS);

        getOrCreateTagBuilder(ModTags.Compat.SERENE_SEASONS_AUTUMN_CROPS)
                .add(ModItems.RABBAGE_SEEDS)
                .add(ModItems.SUNFIRE_TOMATO_SEEDS);

        getOrCreateTagBuilder(ModTags.Compat.SERENE_SEASONS_WINTER_CROPS)
                .add(ModItems.RABBAGE_SEEDS);
    }
}
