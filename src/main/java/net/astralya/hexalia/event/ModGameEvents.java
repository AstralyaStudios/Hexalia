package net.astralya.hexalia.event;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.effect.ModMobEffects;
import net.astralya.hexalia.compat.accessory.AccessoryLookup;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.item.custom.armor.GhostveilItem;
import net.astralya.hexalia.util.MagicResistanceHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HexaliaMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModGameEvents {
    private static final double GHOSTVEIL_FORGET_DISTANCE       = 16.0D;
    private static final double GHOSTVEIL_SNEAK_FORGET_DISTANCE = 24.0D;
    private static final double GHOSTVEIL_MIN_DETECT_DISTANCE   = 6.0D;

    @SubscribeEvent
    public static void onExperiencePickup(PlayerXpEvent.PickupXp event) {
        Player player = event.getEntity();
        ItemStack pendant = player.getOffhandItem();
        if (!pendant.is(ModItems.SAGE_PENDANT.get())) {
            pendant = AccessoryLookup.getEquippedStack(player, ModItems.SAGE_PENDANT.get());
        }
        if (!pendant.isEmpty()) {
            ExperienceOrb orb = event.getOrb();
            int baseXp = orb.value;
            int bonus = (int) Math.floor(baseXp * 2.0);
            orb.value += bonus;
            if (!player.level().isClientSide && !player.isCreative() && pendant.isDamageableItem()) {
                if (player instanceof ServerPlayer serverPlayer && player.level() instanceof ServerLevel serverLevel) {
                    pendant.hurtAndBreak(1, serverPlayer, p -> p.broadcastBreakEvent(EquipmentSlot.OFFHAND));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBrambleguardIncomingDamage(LivingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance inst = entity.getEffect(ModMobEffects.BRAMBLEGUARD.get());
        if (inst == null) return;
        int level = inst.getAmplifier() + 1;
        float reduction = getBrambleguardReduction(event.getSource(), level);
        if (reduction <= 0.0f) return;
        event.setAmount(event.getAmount() * (1.0f - reduction));
    }

    private static float getBrambleguardReduction(DamageSource source, int level) {
        if (isMagicDamage(source)) return clamp01(0.10f * level);
        return clamp01(0.05f * level);
    }

    private static float clamp01(float v) {
        if (v < 0.0f) return 0.0f;
        return Math.min(v, 1.0f);
    }

    @SubscribeEvent
    public static void onSiphonBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player.level().isClientSide) return;
        if (player.isCreative()) return;
        MobEffectInstance inst = player.getEffect(ModMobEffects.SIPHON.get());
        if (inst == null) return;
        int amp = inst.getAmplifier();
        float extraExhaustion = 0.025F * (amp + 1);
        player.causeFoodExhaustion(extraExhaustion);
    }

    @SubscribeEvent
    public static void onHollowSilenceDarknessTick(net.minecraftforge.event.TickEvent.PlayerTickEvent event) {
        if (event.phase != net.minecraftforge.event.TickEvent.Phase.END) return;
        Player player = event.player;
        if (player.level().isClientSide) return;
        if (player.getEffect(ModMobEffects.HOLLOW_SILENCE.get()) == null) return;
        if ((player.tickCount % 40) != 0) return;
        player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60, 0, true, false, true));
    }

    @SubscribeEvent
    public static void onArmorMagicResistanceIncomingDamage(LivingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;
        if (!isMagicDamage(event.getSource())) return;
        float resist = MagicResistanceHelper.getMagicResistancePct(entity);
        if (resist <= 0.0f) return;
        event.setAmount(event.getAmount() * (1.0f - resist));
    }

    private static boolean isMagicDamage(DamageSource source) {
        return source.is(DamageTypes.MAGIC)
                || source.is(DamageTypes.INDIRECT_MAGIC)
                || source.is(DamageTypes.WITHER)
                || source.is(DamageTypes.DRAGON_BREATH);
    }

    @SubscribeEvent
    public static void onGhostveilChangeTarget(LivingChangeTargetEvent event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Mob mob)) return;
        if (mob.level().isClientSide) return;
        LivingEntity target = event.getNewTarget();
        if (!(target instanceof Player player)) return;
        if (!GhostveilItem.isWornBy(player)) return;
        double forgetDistance = player.isCrouching() ? GHOSTVEIL_SNEAK_FORGET_DISTANCE : GHOSTVEIL_FORGET_DISTANCE;
        double distSqr = mob.distanceToSqr(player);
        if (distSqr <= (GHOSTVEIL_MIN_DETECT_DISTANCE * GHOSTVEIL_MIN_DETECT_DISTANCE)) return;
        if (distSqr <= (forgetDistance * forgetDistance)) {
            event.setNewTarget(null);
        }
    }
}
