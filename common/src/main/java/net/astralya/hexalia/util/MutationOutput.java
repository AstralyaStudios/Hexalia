package net.astralya.hexalia.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public final class MutationOutput {
  private MutationOutput() {}

  public static boolean apply(ServerLevel level, BlockPos pos, ItemStack result) {
    if (result.isEmpty()) {
      return false;
    }
    if (result.getItem() instanceof BlockItem) {
      Block block = Block.byItem(result.getItem());
      return level.setBlock(pos, block.defaultBlockState(), Block.UPDATE_ALL);
    }
    return level.addFreshEntity(
        new ItemEntity(
            level, pos.getX() + 0.5, pos.getY() + 0.25, pos.getZ() + 0.5, result.copy()));
  }
}
