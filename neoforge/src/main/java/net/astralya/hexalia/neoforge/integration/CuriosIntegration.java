package net.astralya.hexalia.neoforge.integration;

import net.astralya.hexalia.integration.accessories.AccessoriesIntegration;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

public final class CuriosIntegration {
  private CuriosIntegration() {}

  public static void register() {
    AccessoriesIntegration.addEquippedStackLookup((player, item) ->
        CuriosApi.getCuriosInventory(player)
            .flatMap(handler -> handler.findFirstCurio(item))
            .map(result -> result.stack())
            .orElse(ItemStack.EMPTY));
  }
}
