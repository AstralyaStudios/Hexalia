package net.astralya.hexalia.util;

import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.world.level.block.ComposterBlock;

public class ModRegistries {
    public static void registerCompostable() {
        ComposterBlock.COMPOSTABLES.put(ModItems.MANDRAKE_SEEDS.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ModItems.SUNFIRE_TOMATO_SEEDS.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ModItems.RABBAGE_SEEDS.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ModItems.CHILLBERRIES.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ModItems.LOTUS_FLOWER.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.COTTONWOOD_LEAVES.get().asItem(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.WILLOW_LEAVES.get().asItem(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.COTTONWOOD_SAPLING.get().asItem(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.WILLOW_SAPLING.get().asItem(), 0.3F);

        ComposterBlock.COMPOSTABLES.put(ModBlocks.SPIRIT_BLOOM.get().asItem(), 0.5F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.DREAMSHROOM.get().asItem(), 0.5F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.CELESTIAL_BLOOM.get().asItem(), 0.5F);
        ComposterBlock.COMPOSTABLES.put(ModItems.SIREN_KELP.get(), 0.5F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.BEGONIA.get().asItem(), 0.5F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.LAVENDER.get().asItem(), 0.5F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.DAHLIA.get().asItem(), 0.5F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.PALE_MUSHROOM.get().asItem(), 0.5F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.WITCHWEED.get().asItem(), 0.5F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.GHOST_FERN.get().asItem(), 0.5F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.NIGHTSHADE_BUSH.get().asItem(), 0.5F);

        ComposterBlock.COMPOSTABLES.put(ModItems.MANDRAKE.get(), 0.6F);
        ComposterBlock.COMPOSTABLES.put(ModItems.SUNFIRE_TOMATO.get(), 0.6F);
        ComposterBlock.COMPOSTABLES.put(ModItems.RABBAGE.get(), 0.6F);
        ComposterBlock.COMPOSTABLES.put(ModItems.SALTSPROUT.get(), 0.6F);

        ComposterBlock.COMPOSTABLES.put(ModBlocks.MORPHORA.get(), 0.8F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.GRIMSHADE.get(), 0.8F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.NAUTILITE.get(), 0.8F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.WINDSONG.get(), 0.8F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.NAUTILITE.get(), 0.8F);
    }
}

