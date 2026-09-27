package net.astralya.hexalia.event;

import net.astralya.hexalia.item.ModItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;

public class BloomwrapEventHandler {

    private static final float KNOCKBACK_REDUCTION = 0.8F;
    private static final int EFFECT_DURATION = 60;
    private static final double FLOWER_SCAN_RADIUS = 4.0D;

    public static void onPlayerTick(Player player) {
        if (player.level().isClientSide) {
            return;
        }

        if (player.tickCount % 20 != 0) {
            return;
        }

        tickBoots(player);
        tickLeggings(player);
    }

    public static boolean onKnockback(LivingEntity entity, double strength) {
        if (entity.level().isClientSide) {
            return false;
        }

        return isWearing(entity, EquipmentSlot.HEAD, ModItems.BLOOMWRAP_HAT.get());
    }

    public static float getReducedKnockback(float strength) {
        return strength * (1.0F - KNOCKBACK_REDUCTION);
    }

    private static void tickBoots(Player player) {
        if (!isWearing(player, EquipmentSlot.FEET, ModItems.BLOOMWRAP_BOOTS.get())) {
            return;
        }

        Level world = player.level();
        BlockPos below = player.blockPosition().below();
        boolean onNature = world.getBlockState(below).is(BlockTags.DIRT)
                || world.getBlockState(below).is(BlockTags.LEAVES)
                || world.getBlockState(below).is(BlockTags.SMALL_FLOWERS)
                || world.getBlockState(below).is(BlockTags.TALL_FLOWERS);

        if (onNature) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, EFFECT_DURATION, 0, false, false, true));
        }
    }

    private static void tickLeggings(Player player) {
        if (!isWearing(player, EquipmentSlot.LEGS, ModItems.BLOOMWRAP_LEGGINGS.get())) {
            return;
        }

        Level world = player.level();
        BlockPos origin = player.blockPosition();
        AABB scanArea = new AABB(origin).inflate(FLOWER_SCAN_RADIUS);
        boolean nearFlower = BlockPos.betweenClosedStream(
                BlockPos.containing(scanArea.minX, scanArea.minY, scanArea.minZ),
                BlockPos.containing(scanArea.maxX, scanArea.maxY, scanArea.maxZ)
        ).anyMatch(pos -> {
            var state = world.getBlockState(pos);
            return state.is(BlockTags.SMALL_FLOWERS) || state.is(BlockTags.TALL_FLOWERS);
        });

        if (nearFlower) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, EFFECT_DURATION, 0, false, false, true));
        }
    }

    private static boolean isWearing(LivingEntity entity, EquipmentSlot slot, Item item) {
        ItemStack stack = entity.getItemBySlot(slot);
        return !stack.isEmpty() && stack.is(item);
    }
}

