package net.astralya.hexalia.compat.accessory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;

public final class AccessoryLookup {
    private AccessoryLookup() {
    }

    public static boolean hasEquipped(Player player, Item item) {
        return !getEquippedStack(player, item).isEmpty();
    }

    public static ItemStack getEquippedStack(Player player, Item item) {
        return ModList.get().isLoaded("curios")
                ? CuriosCompat.getEquippedStack(player, item)
                : ItemStack.EMPTY;
    }
}
