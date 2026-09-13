package net.astralya.hexalia.compat.accessory;

import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

final class TrinketsCompat {
    private TrinketsCompat() {
    }

    static boolean hasEquipped(PlayerEntity player, Item item) {
        return !getEquippedStack(player, item).isEmpty();
    }

    static ItemStack getEquippedStack(PlayerEntity player, Item item) {
        return TrinketsApi.getTrinketComponent(player)
                .flatMap(component -> component.getAllEquipped().stream()
                        .filter(pair -> pair.getRight().isOf(item))
                        .map(pair -> pair.getRight())
                        .findFirst())
                .orElse(ItemStack.EMPTY);
    }
}
