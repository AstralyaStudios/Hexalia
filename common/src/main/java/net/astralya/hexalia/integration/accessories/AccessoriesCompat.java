package net.astralya.hexalia.integration.accessories;

import io.wispforest.accessories.api.AccessoriesCapability;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class AccessoriesCompat {
  private AccessoriesCompat() {}

  public static void init() {
    AccessoriesIntegration.setEquippedLookup(AccessoriesCompat::isEquipped);
    AccessoriesIntegration.setEquippedStackLookup(AccessoriesCompat::getEquippedStack);
  }

  private static boolean isEquipped(Player player, Item item) {
    AccessoriesCapability capability = AccessoriesCapability.get(player);
    return capability != null && capability.isEquipped(item);
  }

  private static ItemStack getEquippedStack(Player player, Item item) {
    AccessoriesCapability capability = AccessoriesCapability.get(player);
    if (capability == null) return ItemStack.EMPTY;
    return capability.getEquipped(item).stream()
        .findFirst()
        .map(entry -> entry.stack())
        .orElse(ItemStack.EMPTY);
  }
}
