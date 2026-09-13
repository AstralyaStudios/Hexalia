package net.astralya.hexalia.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.effect.ModMobEffects;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HexaliaMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GravebloomEventHandler {
    private static final int PATCH_RADIUS = 2;

    private GravebloomEventHandler() {
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getEntity().level() instanceof ServerLevel level)
                || !(event.getEntity() instanceof Monster)
                || !(event.getSource().getEntity() instanceof Player player)
                || !player.hasEffect(ModMobEffects.GRAVEBLOOM.get())) {
            return;
        }
        bloom(level, event.getEntity().blockPosition());
    }

    private static void bloom(ServerLevel level, BlockPos origin) {
        RandomSource random = level.getRandom();
        List<BlockPos> candidates = new ArrayList<>();
        for (int x = -PATCH_RADIUS; x <= PATCH_RADIUS; x++) {
            for (int z = -PATCH_RADIUS; z <= PATCH_RADIUS; z++) {
                if (Math.abs(x) == PATCH_RADIUS && Math.abs(z) == PATCH_RADIUS) {
                    continue;
                }
                BlockPos ground = findGround(level, origin.offset(x, 0, z));
                if (ground != null) {
                    candidates.add(ground);
                }
            }
        }
        Collections.shuffle(candidates, new Random(random.nextLong()));

        List<BlockPos> moss = new ArrayList<>();
        for (BlockPos pos : candidates) {
            double distance = Math.sqrt(origin.distSqr(pos));
            float chance = distance < 1.5D ? 0.9F : 0.55F;
            if (random.nextFloat() < chance && level.setBlock(pos, Blocks.MOSS_BLOCK.defaultBlockState(), 3)) {
                moss.add(pos.immutable());
            }
        }

        int plants = placeSprouts(level, moss, 2 + random.nextInt(3));
        if (!moss.isEmpty() || plants > 0) {
            level.sendParticles(ParticleTypes.FALLING_SPORE_BLOSSOM,
                    origin.getX() + 0.5D, origin.getY() + 0.8D, origin.getZ() + 0.5D,
                    8 + random.nextInt(8), 1.35D, 0.45D, 1.35D, 0.01D);
            level.playSound(null, origin, SoundEvents.MOSS_PLACE, SoundSource.BLOCKS,
                    0.55F, 0.9F + random.nextFloat() * 0.2F);
        }
    }

    private static BlockPos findGround(ServerLevel level, BlockPos column) {
        for (int yOffset = 1; yOffset >= -2; yOffset--) {
            BlockPos pos = column.offset(0, yOffset, 0);
            if (level.getBlockState(pos).is(BlockTags.MOSS_REPLACEABLE)
                    && level.getBlockEntity(pos) == null
                    && level.getBlockState(pos.above()).getFluidState().isEmpty()
                    && level.getBlockState(pos.above()).canBeReplaced()) {
                return pos;
            }
        }
        return null;
    }

    private static int placeSprouts(ServerLevel level, List<BlockPos> moss, int targetCount) {
        if (moss.isEmpty()) {
            return 0;
        }
        HolderSet.Named<Block> gravebloomPlants = level.registryAccess().registryOrThrow(Registries.BLOCK)
                .getTag(ModTags.Blocks.GRAVEBLOOM_PLANTS).orElse(null);
        HolderSet.Named<Block> herbs = level.registryAccess().registryOrThrow(Registries.BLOCK)
                .getTag(ModTags.Blocks.HERBS).orElse(null);
        if (gravebloomPlants == null || gravebloomPlants.size() == 0 || herbs == null || herbs.size() == 0) {
            return 0;
        }

        Set<BlockPos> used = new HashSet<>();
        List<Holder<Block>> herbPlants = new ArrayList<>();
        herbs.forEach(herbPlants::add);
        if (!tryPlaceSprout(level, moss, herbPlants, used)) {
            return 0;
        }
        List<Holder<Block>> plants = new ArrayList<>();
        gravebloomPlants.forEach(plants::add);
        int placed = 1;
        while (placed < targetCount && tryPlaceSprout(level, moss, plants, used)) {
            placed++;
        }
        return placed;
    }

    private static boolean tryPlaceSprout(ServerLevel level, List<BlockPos> moss,
                                            List<Holder<Block>> pool, Set<BlockPos> used) {
        Random random = new Random(level.getRandom().nextLong());
        List<BlockPos> targets = new ArrayList<>(moss);
        List<Holder<Block>> plants = new ArrayList<>(pool);
        Collections.shuffle(targets, random);
        Collections.shuffle(plants, random);
        for (BlockPos ground : targets) {
            BlockPos target = ground.above();
            if (!used.contains(target) && level.getBlockState(target).canBeReplaced()) {
                for (Holder<Block> selected : plants) {
                    BlockState state = selected.value().defaultBlockState();
                    if (state.canSurvive(level, target) && level.setBlock(target, state, 3)) {
                        used.add(target);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
