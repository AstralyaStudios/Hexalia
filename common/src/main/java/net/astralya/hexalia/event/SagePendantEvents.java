package net.astralya.hexalia.event;

import net.astralya.hexalia.integration.accessories.AccessoriesIntegration;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.item.custom.SagePendantItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class SagePendantEvents {
  private SagePendantEvents() {}

  public static void storeExperience(Player player, int value) {
    if (player.level().isClientSide || value <= 0) return;
    ItemStack pendant = getSagePendant(player);
    if (!pendant.isEmpty()) SagePendantItem.storeBonus(pendant, value);
  }

  private static ItemStack getSagePendant(Player player) {
    ItemStack offhand = player.getOffhandItem();
    if (offhand.is(ModItems.SAGE_PENDANT.get())) return offhand;
    return AccessoriesIntegration.getEquippedStack(player, ModItems.SAGE_PENDANT.get());
  }
}
