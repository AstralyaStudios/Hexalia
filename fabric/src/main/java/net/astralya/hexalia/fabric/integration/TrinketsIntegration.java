package net.astralya.hexalia.fabric.integration;

import dev.emi.trinkets.api.TrinketsApi;
import net.astralya.hexalia.integration.accessories.AccessoriesIntegration;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class TrinketsIntegration {
    private TrinketsIntegration() {}

    public static void register() {
        AccessoriesIntegration.initialize(
                (player, item) -> !getEquippedStack(player, item).isEmpty(),
                TrinketsIntegration::getEquippedStack);
    }

    private static ItemStack getEquippedStack(Player player, Item item) {
        return TrinketsApi.getTrinketComponent(player)
                .flatMap(component -> component.getAllEquipped().stream()
                        .map(pair -> pair.getB())
                        .filter(stack -> stack.is(item))
                        .findFirst())
                .orElse(ItemStack.EMPTY);
    }
}
