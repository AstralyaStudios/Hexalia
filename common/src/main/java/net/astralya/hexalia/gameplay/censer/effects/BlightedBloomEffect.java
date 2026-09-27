package net.astralya.hexalia.gameplay.censer.effects;

import net.astralya.hexalia.Configuration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BlightedBloomEffect implements ICenserEffect {

    private static final int PULSE_INTERVAL = 32;
    private static final int MIN_CONVERSIONS = 2;
    private static final int MAX_CONVERSIONS = 4;
    private static final float MOOSHROOM_CHANCE = 0.10F;
    private static final double SPREAD_BIAS = 3.0D;

    private int tickCounter = 0;

    @Override
    public String getMessageKey() {
        return "message.hexalia.censer.blighted_bloom";
    }

    @Override
    public void onTick(ServerLevel level, BlockPos pos) {
        int radius = Configuration.CENSER_EFFECT_RADIUS.get();
        spawnAmbientSpores(level, pos, radius);

        this.tickCounter++;
        if (this.tickCounter < PULSE_INTERVAL) {
            return;
        }
        this.tickCounter = 0;

        AABB area = new AABB(pos).inflate(radius);
        convertBlocks(level, pos, radius);
        trySpawnMushrooms(level, pos, radius);
        tryConvertCows(level, area);
    }

    private static void spawnAmbientSpores(ServerLevel level, BlockPos pos, int radius) {
        for (int i = 0; i < 3; i++) {
            double x = pos.getX() + (level.random.nextDouble() * 2.0D - 1.0D) * radius;
            double y = pos.getY() + level.random.nextDouble() * (radius * 0.5D);
            double z = pos.getZ() + (level.random.nextDouble() * 2.0D - 1.0D) * radius;
            level.sendParticles(ParticleTypes.MYCELIUM, x, y, z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    private static void convertBlocks(ServerLevel level, BlockPos center, int radius) {
        List<BlockPos> candidates = new ArrayList<>();
        BlockPos min = new BlockPos(center.getX() - radius, center.getY() - radius, center.getZ() - radius);
        BlockPos max = new BlockPos(center.getX() + radius, center.getY() + radius, center.getZ() + radius);

        for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
            BlockState state = level.getBlockState(pos);
            if ((state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT)) && isOnSpreadFrontier(level, pos, center)) {
                candidates.add(pos.immutable());
            }
        }

        if (candidates.isEmpty()) {
            for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
                BlockState state = level.getBlockState(pos);
                if (state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT)) {
                    candidates.add(pos.immutable());
                }
            }
        }

        if (candidates.isEmpty()) {
            return;
        }

        candidates.sort(Comparator.comparingDouble(pos -> pos.distSqr(center)));
        int count = MIN_CONVERSIONS + level.random.nextInt(MAX_CONVERSIONS - MIN_CONVERSIONS + 1);
        count = Math.min(count, candidates.size());

        for (int i = 0; i < count; i++) {
            BlockPos target = pickBiasedNearest(candidates, level, center, radius);
            if (target == null) {
                break;
            }
            candidates.remove(target);
            level.setBlockAndUpdate(target, Blocks.MYCELIUM.defaultBlockState());
            spawnConversionParticles(level, target);
        }
    }

    private static boolean isOnSpreadFrontier(ServerLevel level, BlockPos pos, BlockPos center) {
        if (pos.closerThan(center, 2.0D)) {
            return true;
        }

        for (BlockPos neighbor : neighbors(pos)) {
            if (level.getBlockState(neighbor).is(Blocks.MYCELIUM)) {
                return true;
            }
        }

        return false;
    }

    private static Iterable<BlockPos> neighbors(BlockPos pos) {
        List<BlockPos> result = new ArrayList<>(6);
        result.add(pos.north());
        result.add(pos.south());
        result.add(pos.east());
        result.add(pos.west());
        result.add(pos.above());
        result.add(pos.below());
        return result;
    }

    private static BlockPos pickBiasedNearest(List<BlockPos> candidates, ServerLevel level, BlockPos center, int radius) {
        if (candidates.isEmpty()) {
            return null;
        }

        double totalWeight = 0.0D;
        double[] weights = new double[candidates.size()];
        double maxDist = radius * radius;

        for (int i = 0; i < candidates.size(); i++) {
            double distSq = candidates.get(i).distSqr(center);
            weights[i] = Math.exp(-SPREAD_BIAS * distSq / Math.max(maxDist, 1.0D));
            totalWeight += weights[i];
        }

        double roll = level.random.nextDouble() * totalWeight;
        double cumulative = 0.0D;

        for (int i = 0; i < candidates.size(); i++) {
            cumulative += weights[i];
            if (roll <= cumulative) {
                return candidates.get(i);
            }
        }

        return candidates.get(0);
    }

    private static void trySpawnMushrooms(ServerLevel level, BlockPos center, int radius) {
        if (level.random.nextFloat() > 0.5F) {
            return;
        }

        BlockPos min = new BlockPos(center.getX() - radius, center.getY() - radius, center.getZ() - radius);
        BlockPos max = new BlockPos(center.getX() + radius, center.getY() + radius, center.getZ() + radius);

        List<BlockPos> surfaces = new ArrayList<>();
        for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
            BlockState below = level.getBlockState(pos.below());
            BlockState current = level.getBlockState(pos);
            if ((below.is(Blocks.MYCELIUM) || below.is(Blocks.GRASS_BLOCK) || below.is(Blocks.DIRT)) && current.isAir()) {
                surfaces.add(pos.immutable());
            }
        }

        if (surfaces.isEmpty()) {
            return;
        }

        surfaces.sort(Comparator.comparingDouble(pos -> pos.distSqr(center)));
        BlockPos target = pickBiasedNearest(surfaces, level, center, radius);
        if (target == null) {
            return;
        }

        BlockState mushroom = level.random.nextBoolean()
                ? Blocks.RED_MUSHROOM.defaultBlockState()
                : Blocks.BROWN_MUSHROOM.defaultBlockState();

        BlockPos ground = target.below();
        if (!level.getBlockState(ground).is(Blocks.MYCELIUM)) {
            level.setBlockAndUpdate(ground, Blocks.MYCELIUM.defaultBlockState());
            spawnConversionParticles(level, ground);
        }

        if (mushroom.canSurvive(level, target)) {
            level.setBlockAndUpdate(target, mushroom);
            level.playSound(null, target, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 0.4F, 0.8F + level.random.nextFloat() * 0.4F);
            spawnConversionParticles(level, target);
        }
    }

    private static void tryConvertCows(ServerLevel level, AABB area) {
        List<Cow> cows = level.getEntitiesOfClass(Cow.class, area, entity -> !(entity instanceof MushroomCow));

        for (Cow cow : cows) {
            if (level.random.nextFloat() > MOOSHROOM_CHANCE) {
                continue;
            }

            MushroomCow mooshroom = new MushroomCow(EntityType.MOOSHROOM, level);
            mooshroom.moveTo(cow.getX(), cow.getY(), cow.getZ(), cow.getYRot(), cow.getXRot());
            mooshroom.setHealth(cow.getHealth());
            mooshroom.setBaby(cow.isBaby());

            if (level.random.nextBoolean()) {
                mooshroom.setVariant(MushroomCow.MushroomType.BROWN);
            }

            level.addFreshEntity(mooshroom);
            cow.discard();

            level.sendParticles(ParticleTypes.MYCELIUM, cow.getX(), cow.getY() + 1.0D, cow.getZ(), 20, 0.5D, 0.5D, 0.5D, 0.0D);
            level.playSound(null, cow.blockPosition(), SoundEvents.MOOSHROOM_CONVERT, SoundSource.NEUTRAL, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
        }
    }

    private static void spawnConversionParticles(ServerLevel level, BlockPos pos) {
        level.sendParticles(ParticleTypes.MYCELIUM, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 8, 0.4D, 0.1D, 0.4D, 0.0D);
        level.sendParticles(ParticleTypes.ENTITY_EFFECT, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 4, 0.2D, 0.2D, 0.2D, 0.05D);
    }
}
