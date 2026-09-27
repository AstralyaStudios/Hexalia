package net.astralya.hexalia.worldgen.tree;

import net.astralya.hexalia.block.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.core.BlockPos;

import java.util.Optional;

public class CocoonTreeHelper {
    private static final int   SCAN_RADIUS   = 8;
    private static final float COCOON_CHANCE = 0.25f;

    private CocoonTreeHelper() {}

    public static boolean growTree(ServerLevel level, ChunkGenerator generator, BlockPos pos,
                                   BlockState saplingState, RandomSource random,
                                   ResourceKey<ConfiguredFeature<?, ?>> plainKey,
                                   ResourceKey<ConfiguredFeature<?, ?>> cocoonKey) {
        ResourceKey<ConfiguredFeature<?, ?>> key =
                hasNearbyGhostFern(level, pos) && random.nextFloat() < COCOON_CHANCE
                        ? cocoonKey
                        : plainKey;

        Registry<ConfiguredFeature<?, ?>> registry =
                level.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE);
        Optional<? extends Holder<ConfiguredFeature<?, ?>>> optional =
                registry.getHolder(key);

        if (optional.isEmpty()) return false;

        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 4);
        if (optional.get().value().place(level, generator, random, pos)) {
            return true;
        }
        level.setBlock(pos, saplingState, 4);
        return false;
    }

    public static boolean hasNearbyGhostFern(ServerLevel level, BlockPos pos) {
        for (BlockPos candidate : BlockPos.betweenClosed(
                pos.offset(-SCAN_RADIUS, -2, -SCAN_RADIUS),
                pos.offset( SCAN_RADIUS,  2,  SCAN_RADIUS))) {
            if (level.getBlockState(candidate).is(ModBlocks.GHOST_FERN.get())) {
                return true;
            }
        }
        return false;
    }
}
