package net.astralya.hexalia.mixin.fabric;

import java.util.ArrayList;
import java.util.List;
import net.astralya.hexalia.event.GreenOmenEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Block.class)
public abstract class GreenOmenBlockMixin {
    @Redirect(method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;"))
    private static List<ItemStack> hexalia$greenOmenDrops(BlockState state, ServerLevel serverLevel, BlockPos pos,
            BlockEntity blockEntity, Entity breaker, ItemStack tool) {
        List<ItemStack> drops = Block.getDrops(state, serverLevel, pos, blockEntity, breaker, tool);
        if (!(breaker instanceof ServerPlayer player)) return drops;
        List<ItemStack> bonus = GreenOmenEffects.createBonusDrops(player, pos, state, drops);
        if (bonus.isEmpty()) return drops;
        List<ItemStack> result = new ArrayList<>(drops.size() + bonus.size());
        result.addAll(drops);
        result.addAll(bonus);
        return result;
    }
}
