package net.astralya.hexalia.gameplay.enchantedplant;
import java.util.Map;
import net.astralya.hexalia.block.ModBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
public final class EnchantedPlantActivations {
  static { GrimshadeActivation.register(); }
  private EnchantedPlantActivations() {}
  @FunctionalInterface private interface Activation { boolean activate(ServerWorld world, PlayerEntity player); }
  private static Map<Item, Activation> activations() {
    return Map.of(
        ModBlocks.GRIMSHADE.asItem(), GrimshadeActivation::activate,
        ModBlocks.NAUTILITE.asItem(), NautiliteActivation::activate,
        ModBlocks.WINDSONG.asItem(), WindsongActivation::activate);
  }
  public static boolean tryActivate(ServerWorld world, PlayerEntity player, ItemStack catalyst) {
    Activation activation=activations().get(catalyst.getItem()); return activation != null && activation.activate(world, player);
  }
}
