package net.astralya.hexalia.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModUtil {
    public static Iterable<ServerPlayer> tracking(ServerLevel level, BlockPos pos) {
        return level.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false);
    }

    public static void spawnItemEntity(Level level, ItemStack stack, double x, double y, double z,
                                       double xMotion, double yMotion, double zMotion) {
        ItemEntity entity = new ItemEntity(level, x, y, z, stack);
        entity.setDeltaMovement(xMotion, yMotion, zMotion);
        level.addFreshEntity(entity);
    }

    public static void removeHarmfulEffects(LivingEntity entity) {
        List<MobEffect> toRemove = new ArrayList<>();
        for (Map.Entry<MobEffect, MobEffectInstance> e : entity.getActiveEffectsMap().entrySet()) {
            MobEffectInstance inst = e.getValue();
            if (inst != null && inst.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                toRemove.add(e.getKey());
            }
        }
        for (MobEffect eff : toRemove) {
            entity.removeEffect(eff);
        }
    }
}
