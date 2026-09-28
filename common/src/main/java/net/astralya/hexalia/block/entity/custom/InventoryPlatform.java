package net.astralya.hexalia.block.entity.custom;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public final class InventoryPlatform {
  private InventoryPlatform() {}

  @ExpectPlatform
  public static @Nullable ItemStack insertAbove(Level level, BlockPos pos, ItemStack stack) {
    throw new AssertionError();
  }

  @ExpectPlatform
  public static @Nullable ItemStack insertAny(Level level, BlockPos pos, ItemStack stack) {
    throw new AssertionError();
  }
}
