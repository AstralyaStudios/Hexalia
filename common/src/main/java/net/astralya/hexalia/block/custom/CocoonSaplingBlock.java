package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.worldgen.tree.CocoonTreeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class CocoonSaplingBlock extends SaplingBlock {

    private final ResourceKey<ConfiguredFeature<?, ?>> plainKey;
    private final ResourceKey<ConfiguredFeature<?, ?>> cocoonKey;

    public CocoonSaplingBlock(AbstractTreeGrower grower, ResourceKey<ConfiguredFeature<?, ?>> plainKey, ResourceKey<ConfiguredFeature<?, ?>> cocoonKey, Properties properties) {
        super(grower, properties);
        this.plainKey = plainKey;
        this.cocoonKey = cocoonKey;
    }

    @Override
    public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        CocoonTreeHelper.growTree(level, level.getChunkSource().getGenerator(), pos, state, random, plainKey, cocoonKey);
    }
}
