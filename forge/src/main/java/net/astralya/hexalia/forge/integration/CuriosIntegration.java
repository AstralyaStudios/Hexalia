package net.astralya.hexalia.forge.integration;

import net.astralya.hexalia.integration.accessories.AccessoriesIntegration;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

public final class CuriosIntegration {
    private CuriosIntegration() {}

    public static void register() {
        AccessoriesIntegration.initialize(
                (player, item) -> CuriosApi.getCuriosInventory(player)
                        .map(handler -> handler.isEquipped(item)).orElse(false),
                (player, item) -> CuriosApi.getCuriosInventory(player)
                        .resolve()
                        .flatMap(handler -> handler.findFirstCurio(item))
                        .map(result -> result.stack())
                        .orElse(ItemStack.EMPTY));
    }
}
