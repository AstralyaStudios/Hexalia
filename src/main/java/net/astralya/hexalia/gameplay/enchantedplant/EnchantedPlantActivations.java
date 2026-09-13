package net.astralya.hexalia.gameplay.enchantedplant;

import java.util.Map;
import net.astralya.hexalia.block.ModBlocks;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class EnchantedPlantActivations {
  static {
    GrimshadeActivation.register();
  }
  private EnchantedPlantActivations() {}
  @FunctionalInterface private interface Activation { boolean activate(ServerLevel level, Player player); }
  private static Map<Item, Activation> activations() {
    return Map.of(
        ModBlocks.GRIMSHADE.get().asItem(), GrimshadeActivation::activate,
        ModBlocks.NAUTILITE.get().asItem(), NautiliteActivation::activate,
        ModBlocks.WINDSONG.get().asItem(), WindsongActivation::activate);
  }
  public static boolean tryActivate(ServerLevel level, Player player, ItemStack catalyst) {
    Activation activation = activations().get(catalyst.getItem());
    return activation != null && activation.activate(level, player);
  }
}
