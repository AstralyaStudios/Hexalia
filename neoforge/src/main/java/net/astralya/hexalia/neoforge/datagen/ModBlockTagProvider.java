package net.astralya.hexalia.neoforge.datagen;

import java.util.concurrent.CompletableFuture;
import net.astralya.hexalia.Hexalia;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public final class ModBlockTagProvider extends BlockTagsProvider {
  public ModBlockTagProvider(
      PackOutput output,
      CompletableFuture<HolderLookup.Provider> lookupProvider,
      ExistingFileHelper existingFileHelper) {
    super(output, lookupProvider, Hexalia.MOD_ID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider lookupProvider) {
    tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .add(ModBlocks.SMALL_CAULDRON.get())
        .add(ModBlocks.RITUAL_TABLE.get())
        .add(ModBlocks.CANDLE_SKULL.get())
        .add(ModBlocks.WITHER_CANDLE_SKULL.get())
        .add(ModBlocks.SALT_LAMP.get())
        .add(ModBlocks.SALT_BLOCK.get())
        .add(ModBlocks.CELESTIAL_CRYSTAL_BLOCK.get())
        .add(ModBlocks.RUSTIC_OVEN.get());

    tag(BlockTags.MINEABLE_WITH_AXE)
        .add(ModBlocks.CENSER.get())
        .add(ModBlocks.SHELF.get())
        .add(ModBlocks.DREAMCATCHER.get())
        .add(ModBlocks.RITUAL_BRAZIER.get())
        .add(ModBlocks.MORTAR_AND_PESTLE.get())
        .add(ModBlocks.LOTUS_FLOWER.get())
        .add(ModBlocks.NESTING_BLOCK.get())
        .add(
            ModBlocks.COTTONWOOD_LOG.get(),
            ModBlocks.STRIPPED_COTTONWOOD_LOG.get(),
            ModBlocks.COTTONWOOD_WOOD.get(),
            ModBlocks.STRIPPED_COTTONWOOD_WOOD.get(),
            ModBlocks.COTTONWOOD_PLANKS.get(),
            ModBlocks.COTTONWOOD_STAIRS.get(),
            ModBlocks.COTTONWOOD_SLAB.get(),
            ModBlocks.COTTONWOOD_BUTTON.get(),
            ModBlocks.COTTONWOOD_PRESSURE_PLATE.get(),
            ModBlocks.COTTONWOOD_FENCE.get(),
            ModBlocks.COTTONWOOD_FENCE_GATE.get(),
            ModBlocks.COTTONWOOD_TRAPDOOR.get(),
            ModBlocks.COTTONWOOD_DOOR.get(),
            ModBlocks.COTTONWOOD_SIGN.get(),
            ModBlocks.COTTONWOOD_WALL_SIGN.get(),
            ModBlocks.COTTONWOOD_HANGING_SIGN.get(),
            ModBlocks.COTTONWOOD_HANGING_WALL_SIGN.get(),
            ModBlocks.WILLOW_LOG.get(),
            ModBlocks.STRIPPED_WILLOW_LOG.get(),
            ModBlocks.WILLOW_WOOD.get(),
            ModBlocks.STRIPPED_WILLOW_WOOD.get(),
            ModBlocks.WILLOW_PLANKS.get(),
            ModBlocks.WILLOW_STAIRS.get(),
            ModBlocks.WILLOW_SLAB.get(),
            ModBlocks.WILLOW_BUTTON.get(),
            ModBlocks.WILLOW_PRESSURE_PLATE.get(),
            ModBlocks.WILLOW_FENCE.get(),
            ModBlocks.WILLOW_FENCE_GATE.get(),
            ModBlocks.WILLOW_TRAPDOOR.get(),
            ModBlocks.WILLOW_DOOR.get(),
            ModBlocks.WILLOW_SIGN.get(),
            ModBlocks.WILLOW_WALL_SIGN.get(),
            ModBlocks.WILLOW_HANGING_SIGN.get(),
            ModBlocks.WILLOW_HANGING_WALL_SIGN.get());

    tag(BlockTags.NEEDS_STONE_TOOL)
        .add(ModBlocks.SMALL_CAULDRON.get())
        .add(ModBlocks.CENSER.get())
        .add(ModBlocks.SHELF.get())
        .add(ModBlocks.LOTUS_FLOWER.get())
        .add(ModBlocks.RITUAL_TABLE.get())
        .add(ModBlocks.RITUAL_BRAZIER.get())
        .add(ModBlocks.MORTAR_AND_PESTLE.get())
        .add(ModBlocks.NESTING_BLOCK.get())
        .add(ModBlocks.SALT_LAMP.get())
        .add(ModBlocks.SALT_BLOCK.get())
        .add(ModBlocks.CELESTIAL_CRYSTAL_BLOCK.get())
        .add(ModBlocks.RUSTIC_OVEN.get());

    tag(BlockTags.FLOWERS)
        .add(ModBlocks.SPIRIT_BLOOM.get())
        .add(ModBlocks.WITCHWEED.get())
        .add(ModBlocks.GHOST_FERN.get())
        .add(ModBlocks.CELESTIAL_BLOOM.get())
        .add(ModBlocks.WITHERED_CELESTIAL_BLOOM.get())
        .add(ModBlocks.MORPHORA.get())
        .add(ModBlocks.GRIMSHADE.get())
        .add(ModBlocks.NAUTILITE.get())
        .add(ModBlocks.WINDSONG.get())
        .add(ModBlocks.ASTRYLIS.get())
        .add(ModBlocks.LOURDES.get())
        .add(ModBlocks.AEGIFLORA.get())
        .add(ModBlocks.WITHERED_AEGIFLORA.get())
        .add(ModBlocks.NIGHTSHADE_BUSH.get())
        .add(ModBlocks.BEGONIA.get())
        .add(ModBlocks.LAVENDER.get())
        .add(ModBlocks.DAHLIA.get());

    tag(BlockTags.SMALL_FLOWERS)
        .add(ModBlocks.SPIRIT_BLOOM.get())
        .add(ModBlocks.WITCHWEED.get())
        .add(ModBlocks.GHOST_FERN.get())
        .add(ModBlocks.CELESTIAL_BLOOM.get())
        .add(ModBlocks.WITHERED_CELESTIAL_BLOOM.get())
        .add(ModBlocks.MORPHORA.get())
        .add(ModBlocks.GRIMSHADE.get())
        .add(ModBlocks.NAUTILITE.get())
        .add(ModBlocks.WINDSONG.get())
        .add(ModBlocks.ASTRYLIS.get())
        .add(ModBlocks.LOURDES.get())
        .add(ModBlocks.AEGIFLORA.get())
        .add(ModBlocks.WITHERED_AEGIFLORA.get())
        .add(ModBlocks.NIGHTSHADE_BUSH.get())
        .add(ModBlocks.BEGONIA.get())
        .add(ModBlocks.LAVENDER.get())
        .add(ModBlocks.DAHLIA.get());

    tag(BlockTags.FROG_PREFER_JUMP_TO).add(ModBlocks.LOTUS_FLOWER.get());
    tag(BlockTags.INSIDE_STEP_SOUND_BLOCKS).add(ModBlocks.LOTUS_FLOWER.get());
    tag(BlockTags.SWORD_EFFICIENT).add(ModBlocks.LOTUS_FLOWER.get());

    tag(BlockTags.CROPS)
        .add(ModBlocks.SUNFIRE_TOMATO_CROP.get())
        .add(ModBlocks.MANDRAKE_CROP.get())
        .add(ModBlocks.RABBAGE_CROP.get())
        .add(ModBlocks.SALTSPROUT.get())
        .add(ModBlocks.CHILLBERRY_BUSH.get());

    tag(BlockTags.FLOWER_POTS)
        .add(ModBlocks.POTTED_SPIRIT_BLOOM.get())
        .add(ModBlocks.POTTED_DREAMSHROOM.get())
        .add(ModBlocks.POTTED_GHOST_FERN.get())
        .add(ModBlocks.POTTED_CELESTIAL_BLOOM.get())
        .add(ModBlocks.POTTED_WITHERED_CELESTIAL_BLOOM.get())
        .add(ModBlocks.POTTED_MORPHORA.get())
        .add(ModBlocks.POTTED_GRIMSHADE.get())
        .add(ModBlocks.POTTED_WINDSONG.get())
        .add(ModBlocks.POTTED_ASTRYLIS.get())
        .add(ModBlocks.POTTED_LOURDES.get())
        .add(ModBlocks.POTTED_AEGIFLORA.get())
        .add(ModBlocks.POTTED_WITHERED_AEGIFLORA.get())
        .add(ModBlocks.POTTED_NIGHTSHADE_BUSH.get())
        .add(ModBlocks.POTTED_BEGONIA.get())
        .add(ModBlocks.POTTED_LAVENDER.get())
        .add(ModBlocks.POTTED_DAHLIA.get())
        .add(ModBlocks.POTTED_COTTONWOOD_SAPLING.get())
        .add(ModBlocks.POTTED_WILLOW_SAPLING.get());

    tag(ModTags.Blocks.CROPS)
        .add(ModBlocks.SUNFIRE_TOMATO_CROP.get())
        .add(ModBlocks.MANDRAKE_CROP.get())
        .add(ModBlocks.RABBAGE_CROP.get())
        .add(ModBlocks.SALTSPROUT.get())
        .add(ModBlocks.CHILLBERRY_BUSH.get());

    tag(ModTags.Blocks.SALT_BLOCKS).add(ModBlocks.SALT_BLOCK.get());
    tag(ModTags.Blocks.BOGSHADE_NO_SLOW)
        .add(
            net.minecraft.world.level.block.Blocks.MUD,
            net.minecraft.world.level.block.Blocks.SOUL_SAND,
            net.minecraft.world.level.block.Blocks.HONEY_BLOCK);
    tag(ModTags.Blocks.RESIN_LOGS)
        .add(
            Blocks.DARK_OAK_LOG,
            ModBlocks.COTTONWOOD_LOG.get(),
            ModBlocks.COTTONWOOD_WOOD.get(),
            ModBlocks.WILLOW_LOG.get(),
            ModBlocks.WILLOW_WOOD.get());
    tag(ModTags.Blocks.COTTONWOOD_LOGS)
        .add(
            ModBlocks.COTTONWOOD_LOG.get(),
            ModBlocks.STRIPPED_COTTONWOOD_LOG.get(),
            ModBlocks.COTTONWOOD_WOOD.get(),
            ModBlocks.STRIPPED_COTTONWOOD_WOOD.get());
    tag(ModTags.Blocks.WILLOW_LOGS)
        .add(
            ModBlocks.WILLOW_LOG.get(),
            ModBlocks.STRIPPED_WILLOW_LOG.get(),
            ModBlocks.WILLOW_WOOD.get(),
            ModBlocks.STRIPPED_WILLOW_WOOD.get());
    tag(BlockTags.LOGS_THAT_BURN)
        .addTag(ModTags.Blocks.COTTONWOOD_LOGS)
        .addTag(ModTags.Blocks.WILLOW_LOGS);
    tag(BlockTags.PLANKS).add(ModBlocks.COTTONWOOD_PLANKS.get(), ModBlocks.WILLOW_PLANKS.get());
    tag(BlockTags.WOODEN_STAIRS).add(ModBlocks.COTTONWOOD_STAIRS.get(), ModBlocks.WILLOW_STAIRS.get());
    tag(BlockTags.WOODEN_SLABS).add(ModBlocks.COTTONWOOD_SLAB.get(), ModBlocks.WILLOW_SLAB.get());
    tag(BlockTags.WOODEN_BUTTONS).add(ModBlocks.COTTONWOOD_BUTTON.get(), ModBlocks.WILLOW_BUTTON.get());
    tag(BlockTags.WOODEN_PRESSURE_PLATES)
        .add(ModBlocks.COTTONWOOD_PRESSURE_PLATE.get(), ModBlocks.WILLOW_PRESSURE_PLATE.get());
    tag(BlockTags.WOODEN_FENCES).add(ModBlocks.COTTONWOOD_FENCE.get(), ModBlocks.WILLOW_FENCE.get());
    tag(BlockTags.FENCE_GATES)
        .add(ModBlocks.COTTONWOOD_FENCE_GATE.get(), ModBlocks.WILLOW_FENCE_GATE.get());
    tag(BlockTags.WOODEN_DOORS).add(ModBlocks.COTTONWOOD_DOOR.get(), ModBlocks.WILLOW_DOOR.get());
    tag(BlockTags.WOODEN_TRAPDOORS)
        .add(ModBlocks.COTTONWOOD_TRAPDOOR.get(), ModBlocks.WILLOW_TRAPDOOR.get());
    tag(BlockTags.SAPLINGS).add(ModBlocks.COTTONWOOD_SAPLING.get(), ModBlocks.WILLOW_SAPLING.get());
    tag(BlockTags.LEAVES).add(ModBlocks.COTTONWOOD_LEAVES.get(), ModBlocks.WILLOW_LEAVES.get());
    tag(BlockTags.STANDING_SIGNS).add(ModBlocks.COTTONWOOD_SIGN.get(), ModBlocks.WILLOW_SIGN.get());
    tag(BlockTags.WALL_SIGNS)
        .add(ModBlocks.COTTONWOOD_WALL_SIGN.get(), ModBlocks.WILLOW_WALL_SIGN.get());
    tag(BlockTags.CEILING_HANGING_SIGNS)
        .add(ModBlocks.COTTONWOOD_HANGING_SIGN.get(), ModBlocks.WILLOW_HANGING_SIGN.get());
    tag(BlockTags.WALL_HANGING_SIGNS)
        .add(
            ModBlocks.COTTONWOOD_HANGING_WALL_SIGN.get(),
            ModBlocks.WILLOW_HANGING_WALL_SIGN.get());
    tag(BlockTags.ALL_SIGNS)
        .add(
            ModBlocks.COTTONWOOD_SIGN.get(),
            ModBlocks.COTTONWOOD_WALL_SIGN.get(),
            ModBlocks.WILLOW_SIGN.get(),
            ModBlocks.WILLOW_WALL_SIGN.get());
    tag(BlockTags.ALL_HANGING_SIGNS)
        .add(
            ModBlocks.COTTONWOOD_HANGING_SIGN.get(),
            ModBlocks.COTTONWOOD_HANGING_WALL_SIGN.get(),
            ModBlocks.WILLOW_HANGING_SIGN.get(),
            ModBlocks.WILLOW_HANGING_WALL_SIGN.get());
    tag(ModTags.Blocks.SPIRITROOT_BOUND_BLOCKS).add(ModBlocks.SPIRIT_BLOOM.get());
    tag(ModTags.Blocks.HERBS)
        .add(ModBlocks.WITCHWEED.get())
        .add(ModBlocks.SPIRIT_BLOOM.get())
        .add(ModBlocks.DREAMSHROOM.get())
        .add(ModBlocks.SIREN_KELP.get())
        .add(ModBlocks.GHOST_FERN.get())
        .add(ModBlocks.CELESTIAL_BLOOM.get())
        .add(ModBlocks.LOTUS_FLOWER.get());
    tag(ModTags.Blocks.GRAVEBLOOM_PLANTS)
        .add(
            Blocks.BROWN_MUSHROOM,
            Blocks.RED_MUSHROOM,
            Blocks.DANDELION,
            Blocks.POPPY,
            Blocks.BLUE_ORCHID,
            Blocks.ALLIUM,
            Blocks.AZURE_BLUET,
            Blocks.RED_TULIP,
            Blocks.ORANGE_TULIP,
            Blocks.WHITE_TULIP,
            Blocks.PINK_TULIP,
            Blocks.OXEYE_DAISY,
            Blocks.CORNFLOWER,
            Blocks.LILY_OF_THE_VALLEY,
            ModBlocks.WITCHWEED.get(),
            ModBlocks.SPIRIT_BLOOM.get(),
            ModBlocks.DREAMSHROOM.get(),
            ModBlocks.GHOST_FERN.get(),
            ModBlocks.CELESTIAL_BLOOM.get());
    tag(ModTags.Blocks.ATTRACTS_MOTH)
        .add(net.minecraft.world.level.block.Blocks.LANTERN)
        .add(net.minecraft.world.level.block.Blocks.SEA_LANTERN)
        .add(net.minecraft.world.level.block.Blocks.SOUL_LANTERN)
        .add(ModBlocks.SALT_LAMP.get())
        .add(ModBlocks.GHOST_FERN.get())
        .add(net.minecraft.world.level.block.Blocks.END_ROD)
        .add(net.minecraft.world.level.block.Blocks.TORCH);

    tag(BlockTags.DIRT).add(ModBlocks.INFUSED_DIRT.get());
    tag(BlockTags.MUSHROOM_GROW_BLOCK).add(ModBlocks.INFUSED_DIRT.get());
    tag(BlockTags.CLIMBABLE)
        .add(ModBlocks.GALEBERRIES_VINE.get())
        .add(ModBlocks.GALEBERRIES_VINE_PLANT.get());
    tag(BlockTags.CAVE_VINES)
        .add(ModBlocks.GALEBERRIES_VINE.get())
        .add(ModBlocks.GALEBERRIES_VINE_PLANT.get());

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
    tag(ModTags.Compat.SERENE_SEASONS_WINTER_CROPS_BLOCK).add(ModBlocks.RABBAGE_CROP.get());
    tag(ModTags.Compat.SERENE_SEASONS_UNBREAKABLE_FERTILE_CROPS)
        .add(ModBlocks.MANDRAKE_CROP.get());
  }
}
