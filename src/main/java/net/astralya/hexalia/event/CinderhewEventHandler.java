package net.astralya.hexalia.event;

import net.astralya.hexalia.item.custom.CinderhewItem;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;

public final class CinderhewEventHandler {
    private static final ThreadLocal<Boolean> BREAKING = ThreadLocal.withInitial(() -> false);

    private CinderhewEventHandler() {
    }

    public static void register() {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            if (BREAKING.get() || !(world instanceof ServerWorld serverWorld)
                    || !(player instanceof ServerPlayerEntity serverPlayer) || !serverPlayer.isSneaking()) {
                return true;
            }
            ItemStack stack = serverPlayer.getMainHandStack();
            if (!(stack.getItem() instanceof CinderhewItem) || !state.isIn(BlockTags.LOGS_THAT_BURN)) {
                return true;
            }
            convertLog(serverWorld, pos, state, serverPlayer, stack);
            return false;
        });
    }

    private static void convertLog(ServerWorld world, BlockPos pos, BlockState state,
                                   ServerPlayerEntity player, ItemStack stack) {
        BREAKING.set(true);
        try {
            state.getBlock().onBreak(world, pos, state, player);
            if (!world.removeBlock(pos, false)) {
                return;
            }
            state.getBlock().onBroken(world, pos, state);
            if (!player.isCreative()) {
                stack.postMine(world, state, pos, player);
                player.incrementStat(Stats.MINED.getOrCreateStat(state.getBlock()));
                player.addExhaustion(0.005F);
                Block.dropStack(world, pos, new ItemStack(Items.CHARCOAL, world.getRandom().nextBoolean() ? 2 : 1));
            }
            world.spawnParticles(ParticleTypes.FLAME, pos.getX() + 0.5D, pos.getY() + 0.5D,
                    pos.getZ() + 0.5D, 6, 0.25D, 0.25D, 0.25D, 0.01D);
            world.spawnParticles(ParticleTypes.SMOKE, pos.getX() + 0.5D, pos.getY() + 0.5D,
                    pos.getZ() + 0.5D, 5, 0.25D, 0.25D, 0.25D, 0.01D);
            world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.45F, 1.2F);
        } finally {
            BREAKING.set(false);
        }
    }
}
