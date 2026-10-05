package net.astralya.hexalia.integration.accessories;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class AccessoriesIntegration {
  private static BiPredicate<Player, Item> equippedLookup = (player, item) -> false;
  private static BiFunction<Player, Item, ItemStack> equippedStackLookup =
      (player, item) -> ItemStack.EMPTY;

  private AccessoriesIntegration() {}

  public static boolean isEquipped(Player player, Item item) {
    return equippedLookup.test(player, item);
  }

  public static boolean isActive(Player player, Item item) {
    return player.getOffhandItem().is(item) || isEquipped(player, item);
  }

  public static ItemStack getEquippedStack(Player player, Item item) {
    return equippedStackLookup.apply(player, item);
  }

  public static boolean isWearingEarplugs(Player player) {
    return player.getItemBySlot(EquipmentSlot.HEAD).is(ModTags.Items.STUN_IMMUNE_HEADWEAR)
        || isActive(player, ModItems.EARPLUGS.get());
  }

  static void setEquippedLookup(BiPredicate<Player, Item> lookup) {
    equippedLookup = lookup;
  }

  static void setEquippedStackLookup(BiFunction<Player, Item, ItemStack> lookup) {
    equippedStackLookup = lookup;
  }

  public static void addEquippedStackLookup(BiFunction<Player, Item, ItemStack> lookup) {
    BiFunction<Player, Item, ItemStack> previous = equippedStackLookup;
    equippedStackLookup = (player, item) -> {
      ItemStack found = previous.apply(player, item);
      return found.isEmpty() ? lookup.apply(player, item) : found;
    };
    equippedLookup = (player, item) -> !equippedStackLookup.apply(player, item).isEmpty();
  }
}
