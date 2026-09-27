package net.astralya.hexalia.forge.event;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HexaliaMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BloomwrapEventHandler {
    private static final float KNOCKBACK_REDUCTION = 0.8f;
    private static final float REFLECT_FRACTION    = 0.15f;
    private static final int   EFFECT_DURATION     = 60;
    private static final double FLOWER_SCAN_RADIUS = 4.0;

    @SubscribeEvent
    public static void onKnockback(LivingKnockBackEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;
        if (!isWearing(entity, EquipmentSlot.HEAD, ModItems.BLOOMWRAP_HAT.get())) return;
        event.setStrength(event.getStrength() * (1.0f - KNOCKBACK_REDUCTION));
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        if (player.level().isClientSide) return;
        if ((player.tickCount % 20) != 0) return;
        tickBoots(player);
        tickLeggings(player);
    }

    @SubscribeEvent
    public static void onRobesIncomingDamage(LivingDamageEvent event) {
        LivingEntity victim = event.getEntity();
        if (victim.level().isClientSide) return;
        if (!isWearing(victim, EquipmentSlot.CHEST, ModItems.BLOOMWRAP_ROBES.get())) return;
        DamageSource source = event.getSource();
        if (source.is(net.minecraft.world.damagesource.DamageTypes.THORNS)) return;
        if (!(source.getEntity() instanceof LivingEntity attacker)) return;
        if (attacker == victim) return;
        float reflectAmount = event.getAmount() * REFLECT_FRACTION;
        attacker.hurt(victim.level().damageSources().thorns(victim), reflectAmount);
    }

    private static void tickBoots(Player player) {
        if (!isWearing(player, EquipmentSlot.FEET, ModItems.BLOOMWRAP_BOOTS.get())) return;
        BlockPos below = player.blockPosition().below();
        Level level = player.level();
        boolean onNature = level.getBlockState(below).is(BlockTags.DIRT)
                || level.getBlockState(below).is(BlockTags.LEAVES)
                || level.getBlockState(below).is(BlockTags.SMALL_FLOWERS)
                || level.getBlockState(below).is(BlockTags.TALL_FLOWERS);
        if (onNature) {
            player.forceAddEffect(new MobEffectInstance(
                    MobEffects.MOVEMENT_SPEED, EFFECT_DURATION, 0, false, false, true), null);
        }
    }

    private static void tickLeggings(Player player) {
        if (!isWearing(player, EquipmentSlot.LEGS, ModItems.BLOOMWRAP_LEGGINGS.get())) return;
        Level level = player.level();
        BlockPos origin = player.blockPosition();
        AABB scanArea = new AABB(origin).inflate(FLOWER_SCAN_RADIUS);
        boolean nearFlower = BlockPos.betweenClosedStream(
                        BlockPos.containing(scanArea.minX, scanArea.minY, scanArea.minZ),
                        BlockPos.containing(scanArea.maxX, scanArea.maxY, scanArea.maxZ))
                .anyMatch(p -> level.getBlockState(p).is(BlockTags.SMALL_FLOWERS)
                        || level.getBlockState(p).is(BlockTags.TALL_FLOWERS));
        if (nearFlower) {
            player.forceAddEffect(new MobEffectInstance(
                    MobEffects.REGENERATION, EFFECT_DURATION, 0, false, false, true), null);
        }
    }

    private static boolean isWearing(LivingEntity entity, EquipmentSlot slot, net.minecraft.world.item.Item item) {
        ItemStack stack = entity.getItemBySlot(slot);
        return !stack.isEmpty() && stack.getItem() == item;
    }
}
