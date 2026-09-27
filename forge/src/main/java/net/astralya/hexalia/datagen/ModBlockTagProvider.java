package net.astralya.hexalia.datagen;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, HexaliaMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        addMiningTags();
        addPlantTags();
        addFunctionalTags();
        addTreeRelatedTags();
        addCustomModTags();
        addAdditionalTags();
        addSereneSeasonsTags();
    }

    private void addMiningTags() {
        // Pickaxe mineable blocks
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.SALT_LAMP.get())
                .add(ModBlocks.RUSTIC_OVEN.get())
                .add(ModBlocks.SMALL_CAULDRON.get())
                .add(ModBlocks.RITUAL_TABLE.get())
                .add(ModBlocks.SALT_BLOCK.get())
                .add(ModBlocks.CELESTIAL_CRYSTAL_BLOCK.get());

        // Axe mineable blocks
        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.SHELF.get())
                .add(ModBlocks.CENSER.get())
                .add(ModBlocks.RITUAL_BRAZIER.get())
                .add(ModBlocks.NESTING_BLOCK.get())
                .add(ModBlocks.MORTAR_AND_PESTLE.get())
                .add(ModBlocks.LOTUS_FLOWER.get());

        // Tool requirements
        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.SALT_LAMP.get())
                .add(ModBlocks.RUSTIC_OVEN.get())
                .add(ModBlocks.SMALL_CAULDRON.get())
                .add(ModBlocks.SHELF.get())
                .add(ModBlocks.CENSER.get())
                .add(ModBlocks.LOTUS_FLOWER.get())
                .add(ModBlocks.RITUAL_TABLE.get())
                .add(ModBlocks.SALT_BLOCK.get())
                .add(ModBlocks.RITUAL_BRAZIER.get())
                .add(ModBlocks.NESTING_BLOCK.get())
                .add(ModBlocks.MORTAR_AND_PESTLE.get())
                .add(ModBlocks.CELESTIAL_CRYSTAL_BLOCK.get());
    }

    private void addPlantTags() {
        // Flower tags
        tag(BlockTags.FLOWERS)
                .add(ModBlocks.SPIRIT_BLOOM.get())
                .add(ModBlocks.WITCHWEED.get())
                .add(ModBlocks.GHOST_FERN.get())
                .add(ModBlocks.CELESTIAL_BLOOM.get())
                .add(ModBlocks.WITHERED_CELESTIAL_BLOOM.get())
                .add(ModBlocks.NIGHTSHADE_BUSH.get())
                .add(ModBlocks.BEGONIA.get())
                .add(ModBlocks.LAVENDER.get())
                .add(ModBlocks.AEGIFLORA.get())
                .add(ModBlocks.WITHERED_AEGIFLORA.get())
                .add(ModBlocks.MORPHORA.get())
                .add(ModBlocks.WINDSONG.get())
                .add(ModBlocks.LOURDES.get())
                .add(ModBlocks.GRIMSHADE.get())
                .add(ModBlocks.CELESTIAL_BLOOM.get())
                .add(ModBlocks.ASTRYLIS.get())
                .add(ModBlocks.DAHLIA.get());

        // Small flowers
        tag(BlockTags.SMALL_FLOWERS)
                .add(ModBlocks.SPIRIT_BLOOM.get())
                .add(ModBlocks.WITCHWEED.get())
                .add(ModBlocks.GHOST_FERN.get())
                .add(ModBlocks.CELESTIAL_BLOOM.get())
                .add(ModBlocks.WITHERED_CELESTIAL_BLOOM.get())
                .add(ModBlocks.NIGHTSHADE_BUSH.get())
                .add(ModBlocks.BEGONIA.get())
                .add(ModBlocks.LAVENDER.get())
                .add(ModBlocks.AEGIFLORA.get())
                .add(ModBlocks.WITHERED_AEGIFLORA.get())
                .add(ModBlocks.MORPHORA.get())
                .add(ModBlocks.WINDSONG.get())
                .add(ModBlocks.LOURDES.get())
                .add(ModBlocks.GRIMSHADE.get())
                .add(ModBlocks.CELESTIAL_BLOOM.get())
                .add(ModBlocks.ASTRYLIS.get())
                .add(ModBlocks.DAHLIA.get());

        // Special plant properties
        tag(BlockTags.FROG_PREFER_JUMP_TO)
                .add(ModBlocks.LOTUS_FLOWER.get());

        tag(BlockTags.INSIDE_STEP_SOUND_BLOCKS)
                .add(ModBlocks.LOTUS_FLOWER.get());

        tag(BlockTags.SWORD_EFFICIENT)
                .add(ModBlocks.LOTUS_FLOWER.get());

        // Crops
        tag(BlockTags.CROPS)
                .add(ModBlocks.SUNFIRE_TOMATO_CROP.get())
                .add(ModBlocks.MANDRAKE_CROP.get())
                .add(ModBlocks.RABBAGE_CROP.get())
                .add(ModBlocks.SALTSPROUT.get())
                .add(ModBlocks.CHILLBERRY_BUSH.get());

        // Saplings
        tag(BlockTags.SAPLINGS)
                .add(ModBlocks.WILLOW_SAPLING.get())
                .add(ModBlocks.COTTONWOOD_SAPLING.get());
    }

    private void addFunctionalTags() {
        tag(ModTags.Blocks.ATTRACTS_MOTH)
                .add(Blocks.LANTERN)
                .add(Blocks.SEA_LANTERN)
                .add(Blocks.SOUL_LANTERN)
                .add(ModBlocks.SALT_LAMP.get())
                .add(ModBlocks.GHOST_FERN.get())
                .add(Blocks.END_ROD)
                .add(Blocks.TORCH);
    }

    private void addTreeRelatedTags() {
        // Cottonwood
        tag(ModTags.Blocks.COTTONWOOD_LOGS)
                .add(ModBlocks.COTTONWOOD_LOG.get())
                .add(ModBlocks.STRIPPED_COTTONWOOD_LOG.get())
                .add(ModBlocks.COTTONWOOD_WOOD.get())
                .add(ModBlocks.STRIPPED_COTTONWOOD_WOOD.get());

        // Willow
        tag(ModTags.Blocks.WILLOW_LOGS)
                .add(ModBlocks.WILLOW_LOG.get())
                .add(ModBlocks.STRIPPED_WILLOW_LOG.get())
                .add(ModBlocks.WILLOW_WOOD.get())
                .add(ModBlocks.STRIPPED_WILLOW_WOOD.get());

        // Vanilla wood categories
        tag(BlockTags.LOGS_THAT_BURN)
                .addTag(ModTags.Blocks.COTTONWOOD_LOGS)
                .addTag(ModTags.Blocks.WILLOW_LOGS);

        tag(BlockTags.LEAVES)
                .add(ModBlocks.COTTONWOOD_LEAVES.get())
                .add(ModBlocks.WILLOW_LEAVES.get());

        // Wooden items
        tag(BlockTags.PLANKS)
                .add(ModBlocks.COTTONWOOD_PLANKS.get())
                .add(ModBlocks.WILLOW_PLANKS.get());

        tag(BlockTags.WOODEN_STAIRS)
                .add(ModBlocks.COTTONWOOD_STAIRS.get())
                .add(ModBlocks.WILLOW_STAIRS.get());

        tag(BlockTags.WOODEN_SLABS)
                .add(ModBlocks.COTTONWOOD_SLAB.get())
                .add(ModBlocks.WILLOW_SLAB.get());

        tag(BlockTags.WOODEN_BUTTONS)
                .add(ModBlocks.COTTONWOOD_BUTTON.get())
                .add(ModBlocks.WILLOW_BUTTON.get());

        tag(BlockTags.WOODEN_PRESSURE_PLATES)
                .add(ModBlocks.COTTONWOOD_PRESSURE_PLATE.get())
                .add(ModBlocks.WILLOW_PRESSURE_PLATE.get());

        tag(BlockTags.WOODEN_TRAPDOORS)
                .add(ModBlocks.COTTONWOOD_TRAPDOOR.get())
                .add(ModBlocks.WILLOW_TRAPDOOR.get());

        tag(BlockTags.FENCE_GATES)
                .add(ModBlocks.COTTONWOOD_FENCE_GATE.get())
                .add(ModBlocks.WILLOW_FENCE_GATE.get());

        tag(BlockTags.WOODEN_FENCES)
                .add(ModBlocks.COTTONWOOD_FENCE.get())
                .add(ModBlocks.WILLOW_FENCE.get());

        tag(BlockTags.WOODEN_DOORS)
                .add(ModBlocks.COTTONWOOD_DOOR.get())
                .add(ModBlocks.WILLOW_DOOR.get());

        // Signs
        tag(BlockTags.SIGNS)
                .add(ModBlocks.COTTONWOOD_SIGN.get())
                .add(ModBlocks.COTTONWOOD_WALL_SIGN.get())
                .add(ModBlocks.WILLOW_SIGN.get())
                .add(ModBlocks.WILLOW_WALL_SIGN.get());

        tag(BlockTags.ALL_HANGING_SIGNS)
                .add(ModBlocks.COTTONWOOD_HANGING_SIGN.get())
                .add(ModBlocks.COTTONWOOD_HANGING_WALL_SIGN.get())
                .add(ModBlocks.WILLOW_HANGING_SIGN.get())
                .add(ModBlocks.WILLOW_HANGING_WALL_SIGN.get());
    }

    private void addCustomModTags() {
        tag(ModTags.Blocks.SALT_BLOCKS)
                .add(ModBlocks.SALT_BLOCK.get());

        tag(ModTags.Blocks.SPIRITROOT_BOUND_BLOCKS)
                .add(ModBlocks.SPIRIT_BLOOM.get());

        tag(ModTags.Blocks.BOGSHADE_NO_SLOW)
                .add(Blocks.MUD)
                .add(Blocks.SOUL_SAND)
                .add(Blocks.HONEY_BLOCK)        ;
    }

    private void addAdditionalTags() {
        // Climbable vines
        tag(BlockTags.CLIMBABLE)
                .add(ModBlocks.GALEBERRIES_VINE.get())
                .add(ModBlocks.GALEBERRIES_VINE_PLANT.get());

        // Cave vines
        tag(BlockTags.CAVE_VINES)
                .add(ModBlocks.GALEBERRIES_VINE.get())
                .add(ModBlocks.GALEBERRIES_VINE_PLANT.get());

        // Flower pots
        tag(BlockTags.FLOWER_POTS)
                .add(ModBlocks.POTTED_SPIRIT_BLOOM.get())
                .add(ModBlocks.POTTED_DREAMSHROOM.get())
                .add(ModBlocks.POTTED_LAVENDER.get())
                .add(ModBlocks.POTTED_BEGONIA.get())
                .add(ModBlocks.POTTED_NIGHTSHADE_BUSH.get())
                .add(ModBlocks.POTTED_ASTRYLIS.get())
                .add(ModBlocks.POTTED_GRIMSHADE.get())
                .add(ModBlocks.POTTED_WINDSONG.get())
                .add(ModBlocks.POTTED_MORPHORA.get())
                .add(ModBlocks.POTTED_COTTONWOOD_SAPLING.get())
                .add(ModBlocks.POTTED_WILLOW_SAPLING.get())
                .add(ModBlocks.POTTED_AEGIFLORA.get())
                .add(ModBlocks.POTTED_LOURDES.get())
                .add(ModBlocks.POTTED_WITHERED_AEGIFLORA.get())
                .add(ModBlocks.POTTED_WITHERED_CELESTIAL_BLOOM.get())
                .add(ModBlocks.POTTED_DAHLIA.get());

        // Dirt
        tag(BlockTags.DIRT)
                .add(ModBlocks.INFUSED_DIRT.get());

        // Mushroom growth
        tag(BlockTags.MUSHROOM_GROW_BLOCK)
                .add(ModBlocks.INFUSED_DIRT.get());
    }

    private void addSereneSeasonsTags() {
        tag(ModTags.Compat.SERENE_SEASONS_SPRING_CROPS_BLOCK)
                .add(ModBlocks.RABBAGE_CROP.get())
                .add(ModBlocks.MANDRAKE_CROP.get());

        tag(ModTags.Compat.SERENE_SEASONS_SUMMER_CROPS_BLOCK)
                .add(ModBlocks.RABBAGE_CROP.get())
                .add(ModBlocks.MANDRAKE_CROP.get())
                .add(ModBlocks.SUNFIRE_TOMATO_CROP.get());

        tag(ModTags.Compat.SERENE_SEASONS_AUTUMN_CROPS_BLOCK)
                .add(ModBlocks.RABBAGE_CROP.get())
                .add(ModBlocks.SUNFIRE_TOMATO_CROP.get());

        tag(ModTags.Compat.SERENE_SEASONS_WINTER_CROPS_BLOCK)
                .add(ModBlocks.RABBAGE_CROP.get());

        tag(ModTags.Compat.SERENE_SEASONS_UNBREAKABLE_FERTILE_CROPS)
                .add(ModBlocks.MANDRAKE_CROP.get());
    }
}
