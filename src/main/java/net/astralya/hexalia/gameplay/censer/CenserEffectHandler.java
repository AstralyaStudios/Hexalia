package net.astralya.hexalia.gameplay.censer;

import net.astralya.hexalia.Configuration;
import net.astralya.hexalia.block.custom.CenserBlock;
import net.astralya.hexalia.block.entity.custom.CenserBlockEntity;
import net.astralya.hexalia.gameplay.censer.effects.ICenserEffect;
import net.astralya.hexalia.gameplay.censer.effects.UndeadVeilEffect;
import net.astralya.hexalia.gameplay.censer.effects.WitheringCalmEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public final class CenserEffectHandler {

    private static final Map<World, Map<BlockPos, ActiveEffect>> ACTIVE_EFFECTS = new WeakHashMap<>();
    private static final Map<World, Set<BlockPos>> SPATIAL_CACHE = new WeakHashMap<>();

    private CenserEffectHandler() {
    }

    public static void startEffect(World world, BlockPos pos, HerbCombination combo) {
        if (!(world instanceof ServerWorld serverWorld)) {
            return;
        }

        ICenserEffect effect = CenserEffectRegistry.create(combo);
        if (effect == null) {
            return;
        }

        int duration = Configuration.CENSER_EFFECT_DURATION.get();
        putActive(serverWorld, pos, combo, effect, duration);
        effect.onStart(serverWorld, pos);
        effect.onTick(serverWorld, pos);

        if (serverWorld.getBlockEntity(pos) instanceof CenserBlockEntity censer) {
            censer.setActiveCombination(combo);
            censer.setBurnTime(duration);
        }
    }

    public static void registerActiveEffect(World world, BlockPos pos, HerbCombination combo, int remainingTime) {
        if (!(world instanceof ServerWorld serverWorld)) {
            return;
        }

        Map<BlockPos, ActiveEffect> map = ACTIVE_EFFECTS.get(serverWorld);
        if (map != null && map.containsKey(pos)) {
            return;
        }

        ICenserEffect effect = CenserEffectRegistry.create(combo);
        if (effect == null) {
            return;
        }

        putActive(serverWorld, pos, combo, effect, remainingTime);
        effect.onStart(serverWorld, pos);
        effect.onTick(serverWorld, pos);
    }

    public static void removeActiveEffect(World world, BlockPos pos) {
        if (!(world instanceof ServerWorld serverWorld)) {
            return;
        }

        ActiveEffect removed = removeActive(serverWorld, pos);
        if (removed != null) {
            removed.effect().onStop(serverWorld, pos);
        }
    }

    public static void updateEffects(World world) {
        if (!(world instanceof ServerWorld serverWorld)) {
            return;
        }

        Map<BlockPos, ActiveEffect> map = ACTIVE_EFFECTS.get(serverWorld);
        if (map == null || map.isEmpty()) {
            return;
        }

        Iterator<Map.Entry<BlockPos, ActiveEffect>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<BlockPos, ActiveEffect> entry = iterator.next();
            BlockPos pos = entry.getKey();
            ActiveEffect active = entry.getValue();

            if (!isCenserStillRunning(serverWorld, pos, active.combo())) {
                active.effect().onStop(serverWorld, pos);
                iterator.remove();
                continue;
            }

            ActiveEffect ticked = active.tickDown();
            if (ticked.expired()) {
                ticked.effect().onStop(serverWorld, pos);
                iterator.remove();
            } else {
                entry.setValue(ticked);
                ticked.effect().onTick(serverWorld, pos);
            }
        }
    }

    public static boolean isValidCombination(Item item1, Item item2) {
        return CenserEffectRegistry.isValid(item1, item2);
    }

    public static String getMessageKeyForCombination(HerbCombination combo) {
        return CenserEffectRegistry.getMessageKey(combo);
    }

    public static boolean isUndeadVeilActiveInArea(World world, BlockPos pos) {
        return isSpatialCacheHit(world, pos);
    }

    public static boolean shouldPreventPlayerTarget(MobEntity mob) {
        Map<BlockPos, ActiveEffect> effects = ACTIVE_EFFECTS.get(mob.getWorld());
        if (effects == null) return false;
        double radiusSq = Math.pow(Configuration.CENSER_EFFECT_RADIUS.get(), 2);
        for (Map.Entry<BlockPos, ActiveEffect> entry : effects.entrySet()) {
            if (mob.getBlockPos().getSquaredDistance(entry.getKey()) > radiusSq) continue;
            if (entry.getValue().effect() instanceof UndeadVeilEffect
                    && mob.getType().isIn(net.astralya.hexalia.util.ModTags.EntityTypes.AFFECTED_BY_UNDEAD_VEIL)
                || entry.getValue().effect() instanceof WitheringCalmEffect
                    && mob.getGroup() != EntityGroup.UNDEAD) return true;
        }
        return false;
    }

    public static boolean isEffectActiveInArea(World world, BlockPos pos, HerbCombination combo) {
        if (world.isClient()) {
            return false;
        }

        ICenserEffect effect = CenserEffectRegistry.create(combo);
        if (effect == null) {
            return false;
        }

        if (effect.usesSpatialCache()) {
            return isSpatialCacheHit(world, pos);
        }

        int radius = Configuration.CENSER_EFFECT_RADIUS.get();
        Box area = new Box(pos).expand(radius);

        return BlockPos.stream(
                        BlockPos.ofFloored(area.minX, area.minY, area.minZ),
                        BlockPos.ofFloored(area.maxX, area.maxY, area.maxZ)
                )
                .map(world::getBlockEntity)
                .filter(CenserBlockEntity.class::isInstance)
                .map(CenserBlockEntity.class::cast)
                .anyMatch(censer -> isCenserRunningCombo(censer, combo));
    }

    public static void onUndeadVeilStart(ServerWorld world, BlockPos pos) {
        SPATIAL_CACHE.computeIfAbsent(world, key -> new HashSet<>()).add(pos.toImmutable());
    }

    public static void onUndeadVeilStop(ServerWorld world, BlockPos pos) {
        Set<BlockPos> set = SPATIAL_CACHE.get(world);
        if (set != null) {
            set.remove(pos);
        }
    }

    private static boolean isCenserStillRunning(ServerWorld world, BlockPos pos, HerbCombination expectedCombo) {
        if (!(world.getBlockEntity(pos) instanceof CenserBlockEntity censer)) {
            return false;
        }

        if (!censer.getCachedState().get(CenserBlock.LIT)) {
            return false;
        }

        return expectedCombo.equals(censer.getActiveCombination());
    }

    private static boolean isSpatialCacheHit(World world, BlockPos pos) {
        if (world.isClient()) {
            return false;
        }

        Set<BlockPos> cached = SPATIAL_CACHE.get(world);
        if (cached == null || cached.isEmpty()) {
            return false;
        }

        int radius = Configuration.CENSER_EFFECT_RADIUS.get();
        double radiusSq = (double) radius * radius;

        for (BlockPos center : cached) {
            if (pos.getSquaredDistance(center) <= radiusSq) {
                return true;
            }
        }

        return false;
    }

    private static void putActive(ServerWorld world, BlockPos pos, HerbCombination combo, ICenserEffect effect, int duration) {
        ACTIVE_EFFECTS.computeIfAbsent(world, key -> new HashMap<>())
                .put(pos.toImmutable(), new ActiveEffect(combo, effect, duration));
    }

    private static ActiveEffect removeActive(ServerWorld world, BlockPos pos) {
        Map<BlockPos, ActiveEffect> map = ACTIVE_EFFECTS.get(world);
        return map != null ? map.remove(pos) : null;
    }

    private static boolean isCenserRunningCombo(CenserBlockEntity censer, HerbCombination combo) {
        if (!censer.getCachedState().get(CenserBlock.LIT)) {
            return false;
        }

        ItemStack herb1 = censer.getStack(0);
        ItemStack herb2 = censer.getStack(1);

        if (herb1.isEmpty() || herb2.isEmpty()) {
            return false;
        }

        return combo.equals(new HerbCombination(herb1.getItem(), herb2.getItem()));
    }

    private record ActiveEffect(HerbCombination combo, ICenserEffect effect, int remainingTicks) {

        private ActiveEffect tickDown() {
            return new ActiveEffect(combo, effect, remainingTicks - 1);
        }

        private boolean expired() {
            return remainingTicks <= 0;
        }
    }
}
