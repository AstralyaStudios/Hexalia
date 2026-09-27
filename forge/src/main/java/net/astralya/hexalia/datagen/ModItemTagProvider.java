package net.astralya.hexalia.datagen;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture,
                              CompletableFuture<TagLookup<Block>> lookupCompletableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, completableFuture, lookupCompletableFuture, HexaliaMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        addVanillaTags();
        addCustomTags();
        addTreeRelatedTags();
        addFoodTags();
        addSignAndBoatTags();
        addSereneSeasonsTags();
    }

    private void addVanillaTags() {
        // Animal food
        tag(ItemTags.FOX_FOOD)
                .add(ModItems.CHILLBERRIES.get())
                .add(ModItems.GALEBERRIES.get());

        // Plants
        tag(ItemTags.FLOWERS)
                .add(ModBlocks.SPIRIT_BLOOM.get().asItem())
                .add(ModBlocks.WITCHWEED.get().asItem())
                .add(ModBlocks.GHOST_FERN.get().asItem())
                .add(ModBlocks.CELESTIAL_BLOOM.get().asItem())
                .add(ModBlocks.WITHERED_CELESTIAL_BLOOM.get().asItem())
                .add(ModBlocks.NIGHTSHADE_BUSH.get().asItem())
                .add(ModBlocks.BEGONIA.get().asItem())
                .add(ModBlocks.LAVENDER.get().asItem())
                .add(ModBlocks.AEGIFLORA.get().asItem())
                .add(ModBlocks.WITHERED_AEGIFLORA.get().asItem())
                .add(ModBlocks.MORPHORA.get().asItem())
                .add(ModBlocks.WINDSONG.get().asItem())
                .add(ModBlocks.LOURDES.get().asItem())
                .add(ModBlocks.GRIMSHADE.get().asItem())
                .add(ModBlocks.CELESTIAL_BLOOM.get().asItem())
                .add(ModBlocks.ASTRYLIS.get().asItem())
                .add(ModBlocks.DAHLIA.get().asItem());

        // Small flowers
        tag(ItemTags.SMALL_FLOWERS)
                .add(ModBlocks.SPIRIT_BLOOM.get().asItem())
                .add(ModBlocks.WITCHWEED.get().asItem())
                .add(ModBlocks.GHOST_FERN.get().asItem())
                .add(ModBlocks.CELESTIAL_BLOOM.get().asItem())
                .add(ModBlocks.WITHERED_CELESTIAL_BLOOM.get().asItem())
                .add(ModBlocks.NIGHTSHADE_BUSH.get().asItem())
                .add(ModBlocks.BEGONIA.get().asItem())
                .add(ModBlocks.LAVENDER.get().asItem())
                .add(ModBlocks.AEGIFLORA.get().asItem())
                .add(ModBlocks.WITHERED_AEGIFLORA.get().asItem())
                .add(ModBlocks.MORPHORA.get().asItem())
                .add(ModBlocks.WINDSONG.get().asItem())
                .add(ModBlocks.LOURDES.get().asItem())
                .add(ModBlocks.GRIMSHADE.get().asItem())
                .add(ModBlocks.CELESTIAL_BLOOM.get().asItem())
                .add(ModBlocks.ASTRYLIS.get().asItem())
                .add(ModBlocks.DAHLIA.get().asItem());

        // Villager related
        tag(ItemTags.VILLAGER_PLANTABLE_SEEDS)
                .add(ModItems.RABBAGE_SEEDS.get())
                .add(ModItems.SUNFIRE_TOMATO_SEEDS.get())
                .add(ModItems.MANDRAKE_SEEDS.get());
    }
    private void addToolsAndEnchantmentsTags() {
        // Tools
        tag(ItemTags.SHOVELS)
                .add(ModItems.ROOTSHAPER.get());

        tag(ItemTags.PICKAXES)
                .add(ModItems.ROOTSHAPER.get());

        tag(ItemTags.SWORDS)
                .add(ModItems.KELPWEAVE_BLADE.get());


    }

    private void addCustomTags() {
        // Herbs and magical components
        tag(ModTags.Items.HERBS)
                .add(ModBlocks.SPIRIT_BLOOM.get().asItem())
                .add(ModItems.SIREN_KELP.get())
                .add(ModBlocks.DREAMSHROOM.get().asItem())
                .add(ModBlocks.GHOST_FERN.get().asItem())
                .add(ModBlocks.WITCHWEED.get().asItem())
                .add(ModBlocks.CELESTIAL_BLOOM.get().asItem());

        tag(ModTags.Items.CRUSHED_HERBS)
                .add(ModItems.SPIRIT_POWDER.get().asItem())
                .add(ModItems.SIREN_PASTE.get())
                .add(ModItems.DREAM_PASTE.get())
                .add(ModItems.GHOST_POWDER.get());

        // Brews
        tag(ModTags.Items.BREWS)
                .add(ModItems.BREW_OF_HOMESTEAD.get())
                .add(ModItems.BREW_OF_BLOODLUST.get())
                .add(ModItems.BREW_OF_SLIMEWALKER.get())
                .add(ModItems.BREW_OF_SPIKESKIN.get())
                .add(ModItems.BREW_OF_SIPHON.get())
                .add(ModItems.BREW_OF_DAYBLOOM.get())
                .add(ModItems.BREW_OF_ARACHNID_GRACE.get())
                .add(ModItems.BREW_OF_HOLLOW_SILENCE.get())
                .add(ModItems.RUSTIC_BOTTLE.get());

        // Minerals
        tag(ModTags.Items.SALT)
                .add(ModItems.SALT.get());

        tag(ModTags.Items.SALT_BLOCKS)
                .add(ModBlocks.SALT_BLOCK.get().asItem());

        // Fungi
        tag(ModTags.Items.MUSHROOMS)
                .add(ModBlocks.DREAMSHROOM.get().asItem())
                .add(ModBlocks.PALE_MUSHROOM.get().asItem());

        // OFF
        tag(ModTags.Items.OFFHAND_EQUIPMENT)
                .add(ModItems.HEX_FOCUS.get())
                .addTag(ModTags.Items.SALT);

        // Tulips
        tag(ModTags.Items.TULIPS)
                .add(Blocks.ORANGE_TULIP.asItem())
                .add(Blocks.PINK_TULIP.asItem())
                .add(Blocks.RED_TULIP.asItem())
                .add(Blocks.WHITE_TULIP.asItem());

        // Stun Immune Headwear
        tag(ModTags.Items.STUN_IMMUNE_HEADWEAR)
                .add(ModItems.EARPLUGS.get())
                .add(ModItems.BLOOMWRAP_HAT.get());
    }

    private void addTreeRelatedTags() {
        // Log types
        tag(ModTags.Items.COTTONWOOD_LOGS)
                .add(ModBlocks.COTTONWOOD_LOG.get().asItem())
                .add(ModBlocks.STRIPPED_COTTONWOOD_LOG.get().asItem())
                .add(ModBlocks.COTTONWOOD_WOOD.get().asItem())
                .add(ModBlocks.STRIPPED_COTTONWOOD_WOOD.get().asItem());

        tag(ModTags.Items.WILLOW_LOGS)
                .add(ModBlocks.WILLOW_LOG.get().asItem())
                .add(ModBlocks.STRIPPED_WILLOW_LOG.get().asItem())
                .add(ModBlocks.WILLOW_WOOD.get().asItem())
                .add(ModBlocks.STRIPPED_WILLOW_WOOD.get().asItem());

        // Vanilla wood categories
        tag(ItemTags.LOGS_THAT_BURN)
                .addTag(ModTags.Items.COTTONWOOD_LOGS)
                .addTag(ModTags.Items.WILLOW_LOGS);

        tag(ItemTags.SAPLINGS)
                .add(ModBlocks.COTTONWOOD_SAPLING.get().asItem())
                .add(ModBlocks.WILLOW_SAPLING.get().asItem());

        tag(ItemTags.LEAVES)
                .add(ModBlocks.COTTONWOOD_LEAVES.get().asItem())
                .add(ModBlocks.WILLOW_LEAVES.get().asItem());

        // Wooden items
        tag(ItemTags.PLANKS)
                .add(ModBlocks.COTTONWOOD_PLANKS.get().asItem())
                .add(ModBlocks.WILLOW_PLANKS.get().asItem());

        tag(ItemTags.WOODEN_STAIRS)
                .add(ModBlocks.COTTONWOOD_STAIRS.get().asItem())
                .add(ModBlocks.WILLOW_STAIRS.get().asItem());

        tag(ItemTags.WOODEN_SLABS)
                .add(ModBlocks.COTTONWOOD_SLAB.get().asItem())
                .add(ModBlocks.WILLOW_SLAB.get().asItem());

        tag(ItemTags.WOODEN_DOORS)
                .add(ModBlocks.COTTONWOOD_DOOR.get().asItem())
                .add(ModBlocks.WILLOW_DOOR.get().asItem());

        tag(ItemTags.WOODEN_BUTTONS)
                .add(ModBlocks.COTTONWOOD_BUTTON.get().asItem())
                .add(ModBlocks.WILLOW_BUTTON.get().asItem());

        tag(ItemTags.WOODEN_PRESSURE_PLATES)
                .add(ModBlocks.COTTONWOOD_PRESSURE_PLATE.get().asItem())
                .add(ModBlocks.WILLOW_PRESSURE_PLATE.get().asItem());

        tag(ItemTags.WOODEN_TRAPDOORS)
                .add(ModBlocks.COTTONWOOD_TRAPDOOR.get().asItem())
                .add(ModBlocks.WILLOW_TRAPDOOR.get().asItem());

        tag(ItemTags.FENCE_GATES)
                .add(ModBlocks.COTTONWOOD_FENCE_GATE.get().asItem())
                .add(ModBlocks.WILLOW_FENCE_GATE.get().asItem());

        tag(ItemTags.WOODEN_FENCES)
                .add(ModBlocks.COTTONWOOD_FENCE.get().asItem())
                .add(ModBlocks.WILLOW_FENCE.get().asItem());
    }

    private void addFoodTags() {
        // Crops
        tag(ModTags.Items.CROPS)
                .add(ModItems.SALTSPROUT.get());

        tag(ModTags.Items.CROPS_TOMATO)
                .add(ModItems.SUNFIRE_TOMATO.get());

        // Specific food types
        tag(ModTags.Items.BERRIES)
                .add(ModItems.CHILLBERRIES.get())
                .add(ModItems.GALEBERRIES.get());

        tag(ModTags.Items.VEGETABLES)
                .add(ModItems.SUNFIRE_TOMATO.get())
                .add(Items.CARROT)
                .add(Items.POTATO);

        tag(ModTags.Items.VEGETABLES_TOMATO)
                .add(ModItems.SUNFIRE_TOMATO.get());

        // Standard food categories
        tag(ModTags.Items.COOKED_MEATS)
                .add(Items.COOKED_BEEF)
                .add(Items.COOKED_CHICKEN)
                .add(Items.COOKED_MUTTON)
                .add(Items.COOKED_PORKCHOP)
                .add(Items.COOKED_RABBIT)
                .add(Items.COOKED_COD)
                .add(Items.COOKED_SALMON);

        tag(ModTags.Items.BREAD)
                .add(Items.BREAD);

        tag(ModTags.Items.BERRIES)
                .add(ModItems.CHILLBERRIES.get())
                .add(ModItems.GALEBERRIES.get());

        tag(ModTags.Items.SEEDS)
                .add(ModItems.MANDRAKE_SEEDS.get())
                .add(ModItems.SUNFIRE_TOMATO_SEEDS.get())
                .add(ModItems.RABBAGE_SEEDS.get());
    }

    private void addSignAndBoatTags() {
        // Boats
        tag(ItemTags.BOATS)
                .add(ModItems.COTTONWOOD_BOAT.get())
                .add(ModItems.WILLOW_BOAT.get());

        tag(ItemTags.CHEST_BOATS)
                .add(ModItems.COTTONWOOD_CHEST_BOAT.get())
                .add(ModItems.WILLOW_CHEST_BOAT.get());

        // Signs
        tag(ItemTags.SIGNS)
                .add(ModItems.COTTONWOOD_SIGN.get())
                .add(ModItems.WILLOW_SIGN.get());

        tag(ItemTags.HANGING_SIGNS)
                .add(ModItems.COTTONWOOD_HANGING_SIGN.get())
                .add(ModItems.WILLOW_HANGING_SIGN.get());
    }

    private void addSereneSeasonsTags() {
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

        tag(ModTags.Compat.SERENE_SEASONS_WINTER_CROPS)
                .add(ModItems.RABBAGE_SEEDS.get());
    }

    @Override
    public String getName() {
        return "Item Tags";
    }
}
