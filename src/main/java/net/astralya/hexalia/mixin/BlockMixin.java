package net.astralya.hexalia.mixin;

import java.util.List;
import net.astralya.hexalia.event.GreenOmenEffects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Redirect(method = "dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;"))
    private static List<ItemStack> hexalia$greenOmen(BlockState state, ServerWorld world, BlockPos pos,
                                                      BlockEntity blockEntity, Entity breaker, ItemStack tool) {
        List<ItemStack> drops = Block.getDroppedStacks(state, world, pos, blockEntity, breaker, tool);
        return breaker instanceof ServerPlayerEntity player
                ? GreenOmenEffects.addBonusDrops(player, pos, state, drops) : drops;
    }
}
