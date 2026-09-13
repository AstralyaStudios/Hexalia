package net.astralya.hexalia.event;

import java.util.ArrayList;
import java.util.List;
import net.astralya.hexalia.compat.accessory.AccessoryLookup;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockState;

public final class GreenOmenEffects {
    private static final float DOUBLE_DROPS_CHANCE = 0.1F;

    private GreenOmenEffects() {}

    public static List<ItemStack> addBonusDrops(ServerPlayerEntity player, BlockPos pos,
                                                BlockState state, List<ItemStack> drops) {
        if (drops.isEmpty() || player.isCreative() || !state.isIn(BlockTags.CROPS)
                || !AccessoryLookup.hasEquipped(player, ModItems.GREEN_OMEN)
                || player.getRandom().nextFloat() >= DOUBLE_DROPS_CHANCE) return drops;
        List<ItemStack> result = new ArrayList<>(drops.size() * 2);
        result.addAll(drops);
        for (ItemStack drop : drops) if (!drop.isEmpty()) result.add(drop.copy());
        ServerWorld world = player.getServerWorld();
        world.spawnParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5,
                5, .25, .2, .25, 0);
        world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.PLAYERS, .3F, 1.2F);
        return result;
    }
}
