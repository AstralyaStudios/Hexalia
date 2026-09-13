package net.astralya.hexalia.fabric.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import java.util.ArrayList;
import java.util.List;
import net.astralya.hexalia.event.GreenOmenEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Block.class)
public abstract class BlockMixin {
  @ModifyExpressionValue(
      method =
          "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)V",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/world/level/block/Block;getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;"))
  private static List<ItemStack> hexalia$applyGreenOmen(
      List<ItemStack> drops,
      BlockState state,
      Level level,
      BlockPos pos,
      BlockEntity blockEntity,
      Entity breaker,
      ItemStack tool) {
    if (!(breaker instanceof ServerPlayer player)) {
      return drops;
    }

    List<ItemStack> bonusDrops = GreenOmenEffects.createBonusDrops(player, pos, state, drops);
    if (bonusDrops.isEmpty()) {
      return drops;
    }
    List<ItemStack> doubledDrops = new ArrayList<>(drops.size() + bonusDrops.size());
    doubledDrops.addAll(drops);
    doubledDrops.addAll(bonusDrops);
    return doubledDrops;
  }
}
