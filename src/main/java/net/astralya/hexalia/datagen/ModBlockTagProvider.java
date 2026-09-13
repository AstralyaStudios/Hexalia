package net.astralya.hexalia.datagen;

import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        addMiningTags();
        addPlantTags();
        addFunctionalTags();
        addTreeRelatedTags();
        addCustomModTags();
        addAdditionalTags();
        addSereneSeasonsTags();
    }

    private void addMiningTags() {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(ModBlocks.SALT_LAMP)
                .add(ModBlocks.RUSTIC_OVEN)
                .add(ModBlocks.SMALL_CAULDRON)
                .add(ModBlocks.RITUAL_TABLE)
                .add(ModBlocks.SALT_BLOCK)
                .add(ModBlocks.CELESTIAL_CRYSTAL_BLOCK);

        getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
                .add(ModBlocks.SHELF)
                .add(ModBlocks.CENSER)
                .add(ModBlocks.RITUAL_BRAZIER)
                .add(ModBlocks.NESTING_BLOCK)
                .add(ModBlocks.MORTAR_AND_PESTLE)
                .add(ModBlocks.LOTUS_FLOWER);

        getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.SALT_LAMP)
                .add(ModBlocks.RUSTIC_OVEN)
                .add(ModBlocks.SMALL_CAULDRON)
                .add(ModBlocks.SHELF)
                .add(ModBlocks.CENSER)
                .add(ModBlocks.LOTUS_FLOWER)
                .add(ModBlocks.RITUAL_TABLE)
                .add(ModBlocks.SALT_BLOCK)
                .add(ModBlocks.RITUAL_BRAZIER)
                .add(ModBlocks.NESTING_BLOCK)
                .add(ModBlocks.MORTAR_AND_PESTLE)
                .add(ModBlocks.CELESTIAL_CRYSTAL_BLOCK);
    }

    private void addPlantTags() {
        getOrCreateTagBuilder(BlockTags.FLOWERS)
                .add(ModBlocks.SPIRIT_BLOOM)
                .add(ModBlocks.WITCHWEED)
                .add(ModBlocks.GHOST_FERN)
                .add(ModBlocks.CELESTIAL_BLOOM)
                .add(ModBlocks.WITHERED_CELESTIAL_BLOOM)
                .add(ModBlocks.NIGHTSHADE_BUSH)
                .add(ModBlocks.BEGONIA)
                .add(ModBlocks.LAVENDER)
                .add(ModBlocks.AEGIFLORA)
                .add(ModBlocks.WITHERED_AEGIFLORA)
                .add(ModBlocks.MORPHORA)
                .add(ModBlocks.WINDSONG)
                .add(ModBlocks.LOURDES)
                .add(ModBlocks.GRIMSHADE)
                .add(ModBlocks.CELESTIAL_BLOOM)
                .add(ModBlocks.ASTRYLIS)
                .add(ModBlocks.DAHLIA);

        getOrCreateTagBuilder(BlockTags.SMALL_FLOWERS)
                .add(ModBlocks.SPIRIT_BLOOM)
                .add(ModBlocks.WITCHWEED)
                .add(ModBlocks.GHOST_FERN)
                .add(ModBlocks.CELESTIAL_BLOOM)
                .add(ModBlocks.WITHERED_CELESTIAL_BLOOM)
                .add(ModBlocks.NIGHTSHADE_BUSH)
                .add(ModBlocks.BEGONIA)
                .add(ModBlocks.LAVENDER)
                .add(ModBlocks.AEGIFLORA)
                .add(ModBlocks.WITHERED_AEGIFLORA)
                .add(ModBlocks.MORPHORA)
                .add(ModBlocks.WINDSONG)
                .add(ModBlocks.LOURDES)
                .add(ModBlocks.GRIMSHADE)
                .add(ModBlocks.CELESTIAL_BLOOM)
                .add(ModBlocks.ASTRYLIS)
                .add(ModBlocks.DAHLIA);

        getOrCreateTagBuilder(BlockTags.FROG_PREFER_JUMP_TO)
                .add(ModBlocks.LOTUS_FLOWER);

        getOrCreateTagBuilder(BlockTags.INSIDE_STEP_SOUND_BLOCKS)
                .add(ModBlocks.LOTUS_FLOWER);

        getOrCreateTagBuilder(BlockTags.SWORD_EFFICIENT)
                .add(ModBlocks.LOTUS_FLOWER);

        getOrCreateTagBuilder(BlockTags.CROPS)
                .add(ModBlocks.SUNFIRE_TOMATO_CROP)
                .add(ModBlocks.MANDRAKE_CROP)
                .add(ModBlocks.RABBAGE_CROP)
                .add(ModBlocks.SALTSPROUT)
                .add(ModBlocks.CHILLBERRY_BUSH);

        getOrCreateTagBuilder(BlockTags.SAPLINGS)
                .add(ModBlocks.WILLOW_SAPLING)
                .add(ModBlocks.COTTONWOOD_SAPLING);
    }

    private void addFunctionalTags() {
        getOrCreateTagBuilder(ModTags.Blocks.ATTRACTS_MOTH)
                .add(Blocks.LANTERN)
                .add(Blocks.SEA_LANTERN)
                .add(Blocks.SOUL_LANTERN)
                .add(ModBlocks.SALT_LAMP)
                .add(Blocks.END_ROD)
                .add(Blocks.TORCH);
    }

    private void addTreeRelatedTags() {
        getOrCreateTagBuilder(ModTags.Blocks.COTTONWOOD_LOGS)
                .add(ModBlocks.COTTONWOOD_LOG)
                .add(ModBlocks.STRIPPED_COTTONWOOD_LOG)
                .add(ModBlocks.COTTONWOOD_WOOD)
                .add(ModBlocks.STRIPPED_COTTONWOOD_WOOD);

        getOrCreateTagBuilder(ModTags.Blocks.WILLOW_LOGS)
                .add(ModBlocks.WILLOW_LOG)
                .add(ModBlocks.STRIPPED_WILLOW_LOG)
                .add(ModBlocks.WILLOW_WOOD)
                .add(ModBlocks.STRIPPED_WILLOW_WOOD);

        getOrCreateTagBuilder(ModTags.Blocks.COCOON_LOGS)
                .add(ModBlocks.COTTONWOOD_LOG)
                .add(Blocks.DARK_OAK_LOG);

        getOrCreateTagBuilder(BlockTags.LOGS_THAT_BURN)
                .addTag(ModTags.Blocks.COTTONWOOD_LOGS)
                .addTag(ModTags.Blocks.WILLOW_LOGS);

        getOrCreateTagBuilder(BlockTags.LEAVES)
                .add(ModBlocks.COTTONWOOD_LEAVES)
                .add(ModBlocks.WILLOW_LEAVES);

        getOrCreateTagBuilder(BlockTags.PLANKS)
                .add(ModBlocks.COTTONWOOD_PLANKS)
                .add(ModBlocks.WILLOW_PLANKS);

        getOrCreateTagBuilder(BlockTags.WOODEN_STAIRS)
                .add(ModBlocks.COTTONWOOD_STAIRS)
                .add(ModBlocks.WILLOW_STAIRS);

        getOrCreateTagBuilder(BlockTags.WOODEN_SLABS)
                .add(ModBlocks.COTTONWOOD_SLAB)
                .add(ModBlocks.WILLOW_SLAB);

        getOrCreateTagBuilder(BlockTags.WOODEN_BUTTONS)
                .add(ModBlocks.COTTONWOOD_BUTTON)
                .add(ModBlocks.WILLOW_BUTTON);

        getOrCreateTagBuilder(BlockTags.WOODEN_PRESSURE_PLATES)
                .add(ModBlocks.COTTONWOOD_PRESSURE_PLATE)
                .add(ModBlocks.WILLOW_PRESSURE_PLATE);

        getOrCreateTagBuilder(BlockTags.WOODEN_TRAPDOORS)
                .add(ModBlocks.COTTONWOOD_TRAPDOOR)
                .add(ModBlocks.WILLOW_TRAPDOOR);

        getOrCreateTagBuilder(BlockTags.FENCE_GATES)
                .add(ModBlocks.COTTONWOOD_FENCE_GATE)
                .add(ModBlocks.WILLOW_FENCE_GATE);

        getOrCreateTagBuilder(BlockTags.WOODEN_FENCES)
                .add(ModBlocks.COTTONWOOD_FENCE)
                .add(ModBlocks.WILLOW_FENCE);

        getOrCreateTagBuilder(BlockTags.WOODEN_DOORS)
                .add(ModBlocks.COTTONWOOD_DOOR)
                .add(ModBlocks.WILLOW_DOOR);

        getOrCreateTagBuilder(BlockTags.SIGNS)
                .add(ModBlocks.COTTONWOOD_SIGN)
                .add(ModBlocks.COTTONWOOD_WALL_SIGN)
                .add(ModBlocks.WILLOW_SIGN)
                .add(ModBlocks.WILLOW_WALL_SIGN);

        getOrCreateTagBuilder(BlockTags.ALL_HANGING_SIGNS)
                .add(ModBlocks.COTTONWOOD_HANGING_SIGN)
                .add(ModBlocks.COTTONWOOD_HANGING_WALL_SIGN)
                .add(ModBlocks.WILLOW_HANGING_SIGN)
                .add(ModBlocks.WILLOW_HANGING_WALL_SIGN);
    }

    private void addCustomModTags() {
        getOrCreateTagBuilder(ModTags.Blocks.HERBS)
                .add(ModBlocks.WITCHWEED)
                .add(ModBlocks.SPIRIT_BLOOM)
                .add(ModBlocks.DREAMSHROOM)
                .add(ModBlocks.SIREN_KELP)
                .add(ModBlocks.GHOST_FERN)
                .add(ModBlocks.CELESTIAL_BLOOM)
                .add(ModBlocks.LOTUS_FLOWER);

        getOrCreateTagBuilder(ModTags.Blocks.RESIN_LOGS)
                .add(Blocks.DARK_OAK_LOG)
                .add(Blocks.DARK_OAK_WOOD)
                .add(ModBlocks.COTTONWOOD_LOG)
                .add(ModBlocks.COTTONWOOD_WOOD)
                .add(ModBlocks.WILLOW_LOG)
                .add(ModBlocks.WILLOW_WOOD);

        getOrCreateTagBuilder(ModTags.Blocks.GRAVEBLOOM_PLANTS)
                .add(Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM, Blocks.DANDELION, Blocks.POPPY,
                        Blocks.BLUE_ORCHID, Blocks.ALLIUM, Blocks.AZURE_BLUET, Blocks.RED_TULIP,
                        Blocks.ORANGE_TULIP, Blocks.WHITE_TULIP, Blocks.PINK_TULIP, Blocks.OXEYE_DAISY,
                        Blocks.CORNFLOWER, Blocks.LILY_OF_THE_VALLEY, ModBlocks.WITCHWEED,
                        ModBlocks.SPIRIT_BLOOM, ModBlocks.DREAMSHROOM, ModBlocks.GHOST_FERN,
                        ModBlocks.CELESTIAL_BLOOM);

        getOrCreateTagBuilder(ModTags.Blocks.SALT_BLOCKS)
                .add(ModBlocks.SALT_BLOCK);

        getOrCreateTagBuilder(ModTags.Blocks.SPIRITROOT_BOUND_BLOCKS)
                .add(ModBlocks.SPIRIT_BLOOM);

        getOrCreateTagBuilder(ModTags.Blocks.BOGSHADE_NO_SLOW)
                .add(Blocks.MUD)
                .add(Blocks.SOUL_SAND)
                .add(Blocks.HONEY_BLOCK);
    }

    private void addAdditionalTags() {
        getOrCreateTagBuilder(BlockTags.CLIMBABLE)
                .add(ModBlocks.GALEBERRIES_VINE)
                .add(ModBlocks.GALEBERRIES_VINE_PLANT);

        getOrCreateTagBuilder(BlockTags.CAVE_VINES)
                .add(ModBlocks.GALEBERRIES_VINE)
                .add(ModBlocks.GALEBERRIES_VINE_PLANT);

        getOrCreateTagBuilder(BlockTags.FLOWER_POTS)
                .add(ModBlocks.POTTED_SPIRIT_BLOOM)
                .add(ModBlocks.POTTED_DREAMSHROOM)
                .add(ModBlocks.POTTED_LAVENDER)
                .add(ModBlocks.POTTED_BEGONIA)
                .add(ModBlocks.POTTED_NIGHTSHADE_BUSH)
                .add(ModBlocks.POTTED_ASTRYLIS)
                .add(ModBlocks.POTTED_GRIMSHADE)
                .add(ModBlocks.POTTED_WINDSONG)
                .add(ModBlocks.POTTED_MORPHORA)
                .add(ModBlocks.POTTED_COTTONWOOD_SAPLING)
                .add(ModBlocks.POTTED_WILLOW_SAPLING)
                .add(ModBlocks.POTTED_AEGIFLORA)
                .add(ModBlocks.POTTED_LOURDES)
                .add(ModBlocks.POTTED_WITHERED_AEGIFLORA)
                .add(ModBlocks.POTTED_WITHERED_CELESTIAL_BLOOM)
                .add(ModBlocks.POTTED_DAHLIA);

        getOrCreateTagBuilder(BlockTags.DIRT)
                .add(ModBlocks.INFUSED_DIRT);

        getOrCreateTagBuilder(BlockTags.MUSHROOM_GROW_BLOCK)
                .add(ModBlocks.INFUSED_DIRT);
    }

    private void addSereneSeasonsTags() {
        getOrCreateTagBuilder(ModTags.Compat.SERENE_SEASONS_SPRING_CROPS_BLOCK)
                .add(ModBlocks.RABBAGE_CROP)
                .add(ModBlocks.MANDRAKE_CROP);

        getOrCreateTagBuilder(ModTags.Compat.SERENE_SEASONS_SUMMER_CROPS_BLOCK)
                .add(ModBlocks.RABBAGE_CROP)
                .add(ModBlocks.MANDRAKE_CROP)
                .add(ModBlocks.SUNFIRE_TOMATO_CROP);

        getOrCreateTagBuilder(ModTags.Compat.SERENE_SEASONS_AUTUMN_CROPS_BLOCK)
                .add(ModBlocks.RABBAGE_CROP)
                .add(ModBlocks.SUNFIRE_TOMATO_CROP);

        getOrCreateTagBuilder(ModTags.Compat.SERENE_SEASONS_WINTER_CROPS_BLOCK)
                .add(ModBlocks.RABBAGE_CROP);

        getOrCreateTagBuilder(ModTags.Compat.SERENE_SEASONS_UNBREAKABLE_FERTILE_CROPS)
                .add(ModBlocks.MANDRAKE_CROP);
    }
}
