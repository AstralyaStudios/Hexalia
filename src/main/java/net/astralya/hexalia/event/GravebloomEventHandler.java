package net.astralya.hexalia.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.astralya.hexalia.effect.ModMobEffects;
import net.astralya.hexalia.util.ModTags;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

public final class GravebloomEventHandler {
    private static final int PATCH_RADIUS = 2;

    private GravebloomEventHandler() {
    }

    public static void register() {
        ServerLivingEntityEvents.AFTER_DEATH.register((slain, source) -> {
            if (!(slain.getWorld() instanceof ServerWorld world)
                    || !(slain instanceof HostileEntity)
                    || !(source.getAttacker() instanceof PlayerEntity player)
                    || !player.hasStatusEffect(ModMobEffects.GRAVEBLOOM)) {
                return;
            }
            bloom(world, slain.getBlockPos());
        });
    }

    private static void bloom(ServerWorld world, BlockPos origin) {
        net.minecraft.util.math.random.Random random = world.getRandom();
        List<BlockPos> candidates = new ArrayList<>();
        for (int x = -PATCH_RADIUS; x <= PATCH_RADIUS; x++) {
            for (int z = -PATCH_RADIUS; z <= PATCH_RADIUS; z++) {
                if (Math.abs(x) == PATCH_RADIUS && Math.abs(z) == PATCH_RADIUS) {
                    continue;
                }
                BlockPos ground = findGround(world, origin.add(x, 0, z));
                if (ground != null) {
                    candidates.add(ground);
                }
            }
        }
        Collections.shuffle(candidates, new Random(random.nextLong()));

        List<BlockPos> moss = new ArrayList<>();
        for (BlockPos pos : candidates) {
            double distance = Math.sqrt(origin.getSquaredDistance(pos));
            float chance = distance < 1.5D ? 0.9F : 0.55F;
            if (random.nextFloat() < chance && world.setBlockState(pos, Blocks.MOSS_BLOCK.getDefaultState(), 3)) {
                moss.add(pos.toImmutable());
            }
        }

        int plants = placeSprouts(world, moss, 2 + random.nextInt(3));
        if (!moss.isEmpty() || plants > 0) {
            world.spawnParticles(ParticleTypes.FALLING_SPORE_BLOSSOM,
                    origin.getX() + 0.5D, origin.getY() + 0.8D, origin.getZ() + 0.5D,
                    8 + random.nextInt(8), 1.35D, 0.45D, 1.35D, 0.01D);
            world.playSound(null, origin, SoundEvents.BLOCK_MOSS_PLACE, SoundCategory.BLOCKS,
                    0.55F, 0.9F + random.nextFloat() * 0.2F);
        }
    }

    private static BlockPos findGround(ServerWorld world, BlockPos column) {
        for (int yOffset = 1; yOffset >= -2; yOffset--) {
            BlockPos pos = column.add(0, yOffset, 0);
            if (world.getBlockState(pos).isIn(BlockTags.MOSS_REPLACEABLE)
                    && world.getBlockEntity(pos) == null
                    && world.getBlockState(pos.up()).getFluidState().isEmpty()
                    && world.getBlockState(pos.up()).isReplaceable()) {
                return pos;
            }
        }
        return null;
    }

    private static int placeSprouts(ServerWorld world, List<BlockPos> moss, int targetCount) {
        if (moss.isEmpty()) {
            return 0;
        }
        RegistryEntryLookup<Block> blocks = world.getRegistryManager().getWrapperOrThrow(RegistryKeys.BLOCK);
        RegistryEntryList.Named<Block> gravebloomPlants = blocks.getOptional(ModTags.Blocks.GRAVEBLOOM_PLANTS).orElse(null);
        RegistryEntryList.Named<Block> herbs = blocks.getOptional(ModTags.Blocks.HERBS).orElse(null);
        if (gravebloomPlants == null || gravebloomPlants.size() == 0 || herbs == null || herbs.size() == 0) {
            return 0;
        }

        Set<BlockPos> used = new HashSet<>();
        List<RegistryEntry<Block>> herbPlants = new ArrayList<>();
        herbs.forEach(herbPlants::add);
        if (!tryPlaceSprout(world, moss, herbPlants, used)) {
            return 0;
        }
        List<RegistryEntry<Block>> plants = new ArrayList<>();
        gravebloomPlants.forEach(plants::add);
        int placed = 1;
        while (placed < targetCount && tryPlaceSprout(world, moss, plants, used)) {
            placed++;
        }
        return placed;
    }

    private static boolean tryPlaceSprout(ServerWorld world, List<BlockPos> moss,
                                            List<RegistryEntry<Block>> pool, Set<BlockPos> used) {
        Random random = new Random(world.getRandom().nextLong());
        List<BlockPos> targets = new ArrayList<>(moss);
        List<RegistryEntry<Block>> plants = new ArrayList<>(pool);
        Collections.shuffle(targets, random);
        Collections.shuffle(plants, random);
        for (BlockPos ground : targets) {
            BlockPos target = ground.up();
            if (!used.contains(target) && world.getBlockState(target).isReplaceable()) {
                for (RegistryEntry<Block> selected : plants) {
                    BlockState state = selected.value().getDefaultState();
                    if (state.canPlaceAt(world, target) && world.setBlockState(target, state, 3)) {
                        used.add(target);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
