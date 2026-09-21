package net.astralya.hexalia.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public final class FireStarterHelper {
  private FireStarterHelper() {}

  public static boolean isFireStarter(ItemStack stack) {
    return stack.is(Items.FLINT_AND_STEEL)
        || stack.is(Items.FIRE_CHARGE)
        || canLightWithNeoForge(stack);
  }

  public static void consumeFireStarter(
      ServerLevel level, @Nullable Player player, @Nullable InteractionHand hand, ItemStack stack) {
    if (player != null && player.getAbilities().instabuild) {
      return;
    }
    if (stack.is(Items.FIRE_CHARGE)) {
      stack.shrink(1);
      return;
    }
    if (stack.isDamageableItem()) {
      if (player != null && hand != null) {
        stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
      } else {
        stack.hurtAndBreak(1, level, null, item -> {});
      }
    }
  }

  private static boolean canLightWithNeoForge(ItemStack stack) {
    try {
      Class<?> abilitiesClass = Class.forName("net.neoforged.neoforge.common.ItemAbilities");
      Field firestarterField = abilitiesClass.getField("FIRESTARTER_LIGHT");
      Object firestarterAbility = firestarterField.get(null);
      Method canPerformAction =
          stack.getClass().getMethod("canPerformAction", firestarterAbility.getClass());
      return Boolean.TRUE.equals(canPerformAction.invoke(stack, firestarterAbility));
    } catch (ReflectiveOperationException | LinkageError ignored) {
      return false;
    }
  }
}
