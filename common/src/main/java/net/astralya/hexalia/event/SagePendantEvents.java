package net.astralya.hexalia.event;

import net.astralya.hexalia.integration.accessories.AccessoriesIntegration;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class SagePendantEvents {
  private SagePendantEvents() {}

  public static boolean hasSagePendant(Player player) {
    return !getSagePendant(player).isEmpty();
  }

  public static int boostedExperience(int value) {
    return value + (int) Math.floor(value * 2.0);
  }

  public static void damagePendant(Player player) {
    ItemStack pendant = getSagePendant(player);
    if (player.level().isClientSide || player.isCreative() || !pendant.isDamageableItem()) {
      return;
    }

    if (player instanceof ServerPlayer serverPlayer
        && player.level() instanceof ServerLevel serverLevel) {
      pendant.hurtAndBreak(
          1,
          serverLevel,
          serverPlayer,
          brokenStack -> serverPlayer.onEquippedItemBroken(brokenStack, EquipmentSlot.OFFHAND));
    }
  }

  private static ItemStack getSagePendant(Player player) {
    ItemStack offhand = player.getOffhandItem();
    if (offhand.is(ModItems.SAGE_PENDANT.get())) return offhand;
    return AccessoriesIntegration.getEquippedStack(player, ModItems.SAGE_PENDANT.get());
  }
}
