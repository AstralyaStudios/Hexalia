package net.astralya.hexalia.event;

import java.util.ArrayList;
import java.util.List;
import net.astralya.hexalia.compat.accessory.AccessoryLookup;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public final class GreenOmenEffects {
    private static final float DOUBLE_DROPS_CHANCE = 0.1F;

    private GreenOmenEffects() {}

    public static List<ItemStack> addBonusDrops(ServerPlayer player, BlockPos pos,
                                                BlockState state, List<ItemStack> drops) {
        if (drops.isEmpty() || player.isCreative() || !state.is(BlockTags.CROPS)
                || !AccessoryLookup.hasEquipped(player, ModItems.GREEN_OMEN.get())
                || player.getRandom().nextFloat() >= DOUBLE_DROPS_CHANCE) return drops;
        List<ItemStack> result = new ArrayList<>(drops.size() * 2);
        result.addAll(drops);
        for (ItemStack drop : drops) if (!drop.isEmpty()) result.add(drop.copy());
        ServerLevel level = player.serverLevel();
        level.sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5,
                5, .25, .2, .25, 0);
        level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, .3F, 1.2F);
        return result;
    }
}
