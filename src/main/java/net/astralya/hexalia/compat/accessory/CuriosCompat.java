package net.astralya.hexalia.compat.accessory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

final class CuriosCompat {
    private CuriosCompat() {
    }

    static boolean hasEquipped(Player player, Item item) {
        return !getEquippedStack(player, item).isEmpty();
    }

    static ItemStack getEquippedStack(Player player, Item item) {
        return CuriosApi.getCuriosInventory(player).resolve()
                .map(handler -> {
                    var equipped = handler.getEquippedCurios();
                    for (int slot = 0; slot < equipped.getSlots(); slot++) {
                        ItemStack stack = equipped.getStackInSlot(slot);
                        if (stack.is(item)) return stack;
                    }
                    return ItemStack.EMPTY;
                })
                .orElse(ItemStack.EMPTY);
    }
}
