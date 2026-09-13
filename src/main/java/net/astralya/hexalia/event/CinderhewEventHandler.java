package net.astralya.hexalia.event;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.item.custom.CinderhewItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HexaliaMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CinderhewEventHandler {
    private static final ThreadLocal<Boolean> BREAKING = ThreadLocal.withInitial(() -> false);

    private CinderhewEventHandler() {
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (BREAKING.get() || !(event.getPlayer() instanceof ServerPlayer player) || !player.isShiftKeyDown()) {
            return;
        }
        ItemStack stack = player.getMainHandItem();
        BlockState state = event.getState();
        if (!(stack.getItem() instanceof CinderhewItem) || !state.is(BlockTags.LOGS_THAT_BURN)
                || !(player.level() instanceof ServerLevel level)) {
            return;
        }
        convertLog(level, event.getPos(), state, player, stack);
        event.setCanceled(true);
    }

    private static void convertLog(ServerLevel level, BlockPos pos, BlockState state,
                                   ServerPlayer player, ItemStack stack) {
        BREAKING.set(true);
        try {
            state.getBlock().playerWillDestroy(level, pos, state, player);
            if (!level.removeBlock(pos, false)) {
                return;
            }
            state.getBlock().destroy(level, pos, state);
            if (!player.getAbilities().instabuild) {
                stack.mineBlock(level, state, pos, player);
                player.awardStat(Stats.BLOCK_MINED.get(state.getBlock()));
                player.causeFoodExhaustion(0.005F);
                Block.popResource(level, pos,
                        new ItemStack(Items.CHARCOAL, level.getRandom().nextBoolean() ? 2 : 1));
            }
            level.sendParticles(ParticleTypes.FLAME, pos.getX() + 0.5D, pos.getY() + 0.5D,
                    pos.getZ() + 0.5D, 6, 0.25D, 0.25D, 0.25D, 0.01D);
            level.sendParticles(ParticleTypes.SMOKE, pos.getX() + 0.5D, pos.getY() + 0.5D,
                    pos.getZ() + 0.5D, 5, 0.25D, 0.25D, 0.25D, 0.01D);
            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.45F, 1.2F);
        } finally {
            BREAKING.set(false);
        }
    }
}
