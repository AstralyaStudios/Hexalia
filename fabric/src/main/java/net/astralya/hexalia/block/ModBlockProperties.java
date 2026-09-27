package net.astralya.hexalia.block;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;

public final class ModBlockProperties {

    private ModBlockProperties() {
    }

    public static void register() {
        registerStrippables();
        registerFlammables();
    }

    private static void registerStrippables() {
        StrippableBlockRegistry.register(ModBlocks.COTTONWOOD_LOG.get(), ModBlocks.STRIPPED_COTTONWOOD_LOG.get());
        StrippableBlockRegistry.register(ModBlocks.COTTONWOOD_WOOD.get(), ModBlocks.STRIPPED_COTTONWOOD_WOOD.get());
        StrippableBlockRegistry.register(ModBlocks.WILLOW_LOG.get(), ModBlocks.STRIPPED_WILLOW_LOG.get());
        StrippableBlockRegistry.register(ModBlocks.WILLOW_WOOD.get(), ModBlocks.STRIPPED_WILLOW_WOOD.get());
    }

    public static void registerFlammables() {
        FlammableBlockRegistry instance = FlammableBlockRegistry.getDefaultInstance();

        // Blocks
        instance.add(ModBlocks.NESTING_BLOCK.get(), 5, 20);
        instance.add(ModBlocks.RITUAL_BRAZIER.get(), 5, 20);
        instance.add(ModBlocks.DREAMCATCHER.get(), 5, 20);
        instance.add(ModBlocks.SILKWORM_COCOON.get(), 60, 100);

        // Leaves
        instance.add(ModBlocks.COTTONWOOD_CATKIN.get(), 30, 60);
        instance.add(ModBlocks.COTTONWOOD_LEAVES.get(), 30, 60);
        instance.add(ModBlocks.WILLOW_LEAVES.get(), 30, 60);

        // Saplings
        instance.add(ModBlocks.COTTONWOOD_SAPLING.get(), 60, 100);
        instance.add(ModBlocks.WILLOW_SAPLING.get(), 60, 100);

        // Cottonwood Logs and Wood
        instance.add(ModBlocks.COTTONWOOD_LOG.get(), 5, 5);
        instance.add(ModBlocks.COTTONWOOD_WOOD.get(), 5, 5);
        instance.add(ModBlocks.STRIPPED_COTTONWOOD_LOG.get(), 5, 5);
        instance.add(ModBlocks.STRIPPED_COTTONWOOD_WOOD.get(), 5, 5);

        // Cottonwood Planks and Variants
        instance.add(ModBlocks.COTTONWOOD_PLANKS.get(), 5, 20);
        instance.add(ModBlocks.COTTONWOOD_STAIRS.get(), 5, 20);
        instance.add(ModBlocks.COTTONWOOD_SLAB.get(), 5, 20);
        instance.add(ModBlocks.COTTONWOOD_FENCE.get(), 5, 20);
        instance.add(ModBlocks.COTTONWOOD_FENCE_GATE.get(), 5, 20);
        instance.add(ModBlocks.COTTONWOOD_TRAPDOOR.get(), 5, 20);
        instance.add(ModBlocks.COTTONWOOD_DOOR.get(), 5, 20);
        instance.add(ModBlocks.COTTONWOOD_BUTTON.get(), 5, 20);
        instance.add(ModBlocks.COTTONWOOD_PRESSURE_PLATE.get(), 5, 20);

        // Cottonwood Signs
        instance.add(ModBlocks.COTTONWOOD_SIGN.get(), 5, 20);
        instance.add(ModBlocks.COTTONWOOD_WALL_SIGN.get(), 5, 20);
        instance.add(ModBlocks.COTTONWOOD_HANGING_SIGN.get(), 5, 20);
        instance.add(ModBlocks.COTTONWOOD_HANGING_WALL_SIGN.get(), 5, 20);

        // Willow Logs and Wood
        instance.add(ModBlocks.WILLOW_LOG.get(), 5, 5);
        instance.add(ModBlocks.WILLOW_WOOD.get(), 5, 5);
        instance.add(ModBlocks.STRIPPED_WILLOW_LOG.get(), 5, 5);
        instance.add(ModBlocks.STRIPPED_WILLOW_WOOD.get(), 5, 5);

        // Willow Planks and Variants
        instance.add(ModBlocks.WILLOW_PLANKS.get(), 5, 20);
        instance.add(ModBlocks.WILLOW_STAIRS.get(), 5, 20);
        instance.add(ModBlocks.WILLOW_SLAB.get(), 5, 20);
        instance.add(ModBlocks.WILLOW_FENCE.get(), 5, 20);
        instance.add(ModBlocks.WILLOW_FENCE_GATE.get(), 5, 20);
        instance.add(ModBlocks.WILLOW_TRAPDOOR.get(), 5, 20);
        instance.add(ModBlocks.WILLOW_DOOR.get(), 5, 20);
        instance.add(ModBlocks.WILLOW_BUTTON.get(), 5, 20);
        instance.add(ModBlocks.WILLOW_PRESSURE_PLATE.get(), 5, 20);

        // Willow Signs
        instance.add(ModBlocks.WILLOW_SIGN.get(), 5, 20);
        instance.add(ModBlocks.WILLOW_WALL_SIGN.get(), 5, 20);
        instance.add(ModBlocks.WILLOW_HANGING_SIGN.get(), 5, 20);
        instance.add(ModBlocks.WILLOW_HANGING_WALL_SIGN.get(), 5, 20);

        // Herbs and Plants
        instance.add(ModBlocks.SPIRIT_BLOOM.get(), 60, 100);
        instance.add(ModBlocks.GHOST_FERN.get(), 60, 100);
        instance.add(ModBlocks.CELESTIAL_BLOOM.get(), 60, 100);
        instance.add(ModBlocks.WITHERED_CELESTIAL_BLOOM.get(), 60, 100);
        instance.add(ModBlocks.WILD_MANDRAKE.get(), 60, 100);
        instance.add(ModBlocks.CHILLBERRY_BUSH.get(), 60, 100);

        // Enchanted Plants
        instance.add(ModBlocks.MORPHORA.get(), 60, 100);
        instance.add(ModBlocks.GRIMSHADE.get(), 60, 100);
        instance.add(ModBlocks.WINDSONG.get(), 60, 100);
        instance.add(ModBlocks.ASTRYLIS.get(), 60, 100);
        instance.add(ModBlocks.LOURDES.get(), 60, 100);
        instance.add(ModBlocks.AEGIFLORA.get(), 60, 100);
        instance.add(ModBlocks.WITHERED_AEGIFLORA.get(), 60, 100);

        // Decorative Flowers
        instance.add(ModBlocks.BEGONIA.get(), 60, 100);
        instance.add(ModBlocks.LAVENDER.get(), 60, 100);
        instance.add(ModBlocks.DAHLIA.get(), 60, 100);
        instance.add(ModBlocks.WITCHWEED.get(), 60, 100);
        instance.add(ModBlocks.NIGHTSHADE_BUSH.get(), 60, 100);

        // Vines and Decorative Blocks
        instance.add(ModBlocks.GALEBERRIES_VINE.get(), 60, 100);
        instance.add(ModBlocks.GALEBERRIES_VINE_PLANT.get(), 60, 100);
    }
}
