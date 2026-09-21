package net.astralya.hexalia.gameplay.censer;

import net.astralya.hexalia.Configuration;
import net.astralya.hexalia.block.custom.CenserBlock;
import net.astralya.hexalia.block.entity.custom.CenserBlockEntity;
import net.astralya.hexalia.gameplay.censer.effects.ICenserEffect;
import net.astralya.hexalia.gameplay.censer.effects.UndeadVeilEffect;
import net.astralya.hexalia.gameplay.censer.effects.WitheringCalmEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public final class CenserEffectHandler {

    private record ActiveEffect(HerbCombination combo, ICenserEffect effect, int remainingTicks) {
        private ActiveEffect tickDown() {
            return new ActiveEffect(this.combo, this.effect, this.remainingTicks - 1);
        }

        private boolean expired() {
            return this.remainingTicks <= 0;
        }
    }

    private static final Map<Level, Map<BlockPos, ActiveEffect>> ACTIVE_EFFECTS = new WeakHashMap<>();
    private static final Map<Level, Set<BlockPos>> SPATIAL_CACHE = new WeakHashMap<>();

    private CenserEffectHandler() {
    }

    public static void startEffect(Level level, BlockPos pos, HerbCombination combo) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        ICenserEffect effect = CenserEffectRegistry.create(combo);
        if (effect == null) {
            return;
        }

        int duration = Configuration.CENSER_EFFECT_DURATION.get();
        putActive(serverLevel, pos, combo, effect, duration);
        effect.onStart(serverLevel, pos);
        effect.onTick(serverLevel, pos);

        if (serverLevel.getBlockEntity(pos) instanceof CenserBlockEntity censer) {
            censer.setActiveCombination(combo);
            censer.setBurnTime(duration);
        }
    }

    public static void registerActiveEffect(Level level, BlockPos pos, HerbCombination combo, int remainingTime) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        Map<BlockPos, ActiveEffect> map = ACTIVE_EFFECTS.get(serverLevel);
        if (map != null && map.containsKey(pos)) {
            return;
        }

        ICenserEffect effect = CenserEffectRegistry.create(combo);
        if (effect == null) {
            return;
        }

        putActive(serverLevel, pos, combo, effect, remainingTime);
        effect.onStart(serverLevel, pos);
        effect.onTick(serverLevel, pos);
    }

    public static void removeActiveEffect(Level level, BlockPos pos) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        ActiveEffect removed = removeActive(serverLevel, pos);
        if (removed != null) {
            removed.effect().onStop(serverLevel, pos);
        }
    }

    public static void updateEffects(Level level) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        Map<BlockPos, ActiveEffect> map = ACTIVE_EFFECTS.get(serverLevel);
        if (map == null || map.isEmpty()) {
            return;
        }

        Iterator<Map.Entry<BlockPos, ActiveEffect>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<BlockPos, ActiveEffect> entry = iterator.next();
            BlockPos pos = entry.getKey();
            ActiveEffect active = entry.getValue();

            if (!isCenserStillRunning(serverLevel, pos, active.combo())) {
                active.effect().onStop(serverLevel, pos);
                iterator.remove();
                continue;
            }

            ActiveEffect ticked = active.tickDown();
            if (ticked.expired()) {
                ticked.effect().onStop(serverLevel, pos);
                iterator.remove();
            } else {
                entry.setValue(ticked);
                ticked.effect().onTick(serverLevel, pos);
            }
        }
    }

    private static boolean isCenserStillRunning(ServerLevel level, BlockPos pos, HerbCombination expectedCombo) {
        if (!(level.getBlockEntity(pos) instanceof CenserBlockEntity censer)) {
            return false;
        }
        if (!censer.getBlockState().getValue(CenserBlock.LIT)) {
            return false;
        }
        return expectedCombo.equals(censer.getActiveCombination());
    }

    public static boolean isValidCombination(Item item1, Item item2) {
        return CenserEffectRegistry.isValid(item1, item2);
    }

    public static String getMessageKeyForCombination(HerbCombination combo) {
        return CenserEffectRegistry.getMessageKey(combo);
    }

    public static boolean isUndeadVeilActiveInArea(Level level, BlockPos pos) {
        return isSpatialCacheHit(level, pos);
    }

    public static boolean shouldPreventPlayerTarget(Mob mob) {
        Map<BlockPos, ActiveEffect> effects = ACTIVE_EFFECTS.get(mob.level());
        if (effects == null) return false;
        double radiusSq = Math.pow(Configuration.CENSER_EFFECT_RADIUS.get(), 2);
        for (Map.Entry<BlockPos, ActiveEffect> entry : effects.entrySet()) {
            if (mob.blockPosition().distSqr(entry.getKey()) > radiusSq) continue;
            if (entry.getValue().effect() instanceof UndeadVeilEffect
                    && mob.getType().is(net.astralya.hexalia.util.ModTags.EntityTypes.AFFECTED_BY_UNDEAD_VEIL)
                || entry.getValue().effect() instanceof WitheringCalmEffect
                    && mob.getMobType() != MobType.UNDEAD) return true;
        }
        return false;
    }

    public static boolean isEffectActiveInArea(Level level, BlockPos pos, HerbCombination combo) {
        if (level.isClientSide()) {
            return false;
        }

        ICenserEffect effect = CenserEffectRegistry.create(combo);
        if (effect == null) {
            return false;
        }

        if (effect.usesSpatialCache()) {
            return isSpatialCacheHit(level, pos);
        }

        int radius = Configuration.CENSER_EFFECT_RADIUS.get();
        AABB area = new AABB(pos).inflate(radius);

        return BlockPos.betweenClosedStream(
                        new BlockPos((int) area.minX, (int) area.minY, (int) area.minZ),
                        new BlockPos((int) area.maxX, (int) area.maxY, (int) area.maxZ)
                )
                .map(level::getBlockEntity)
                .filter(blockEntity -> blockEntity instanceof CenserBlockEntity)
                .map(blockEntity -> (CenserBlockEntity) blockEntity)
                .anyMatch(censer -> isCenserRunningCombo(censer, combo));
    }

    public static void onUndeadVeilStart(ServerLevel level, BlockPos pos) {
        SPATIAL_CACHE.computeIfAbsent(level, key -> new HashSet<>()).add(pos.immutable());
    }

    public static void onUndeadVeilStop(ServerLevel level, BlockPos pos) {
        Set<BlockPos> set = SPATIAL_CACHE.get(level);
        if (set != null) {
            set.remove(pos);
        }
    }

    private static boolean isSpatialCacheHit(Level level, BlockPos pos) {
        if (level.isClientSide()) {
            return false;
        }

        int radius = Configuration.CENSER_EFFECT_RADIUS.get();
        Set<BlockPos> cached = SPATIAL_CACHE.get(level);
        if (cached == null || cached.isEmpty()) {
            return false;
        }

        double radiusSq = (double) radius * (double) radius;
        for (BlockPos center : cached) {
            if (pos.distSqr(center) <= radiusSq) {
                return true;
            }
        }

        return false;
    }

    private static void putActive(ServerLevel level, BlockPos pos, HerbCombination combo, ICenserEffect effect, int duration) {
        ACTIVE_EFFECTS.computeIfAbsent(level, key -> new HashMap<>())
                .put(pos.immutable(), new ActiveEffect(combo, effect, duration));
    }

    private static ActiveEffect removeActive(ServerLevel level, BlockPos pos) {
        Map<BlockPos, ActiveEffect> map = ACTIVE_EFFECTS.get(level);
        return map != null ? map.remove(pos) : null;
    }

    private static boolean isCenserRunningCombo(CenserBlockEntity censer, HerbCombination combo) {
        if (!censer.getBlockState().getValue(CenserBlock.LIT)) {
            return false;
        }

        ItemStack herb1 = censer.getItem(0);
        ItemStack herb2 = censer.getItem(1);

        if (herb1.isEmpty() || herb2.isEmpty()) {
            return false;
        }

        return combo.equals(new HerbCombination(herb1.getItem(), herb2.getItem()));
    }
}
