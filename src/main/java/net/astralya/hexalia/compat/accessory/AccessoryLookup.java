package net.astralya.hexalia.compat.accessory;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class AccessoryLookup {
    private AccessoryLookup() {
    }

    public static boolean hasEquipped(PlayerEntity player, Item item) {
        return !getEquippedStack(player, item).isEmpty();
    }

    public static ItemStack getEquippedStack(PlayerEntity player, Item item) {
        return FabricLoader.getInstance().isModLoaded("trinkets")
                ? TrinketsCompat.getEquippedStack(player, item)
                : ItemStack.EMPTY;
    }
}
