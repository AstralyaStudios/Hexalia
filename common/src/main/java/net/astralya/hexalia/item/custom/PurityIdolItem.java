package net.astralya.hexalia.item.custom;

import java.util.Map;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

public class PurityIdolItem extends WeatherIdolItem {

  public PurityIdolItem(Properties pProperties) {
    super(pProperties);
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
    if (!level.isClientSide) {
      ItemStack main = user.getMainHandItem();
      ItemStack off = user.getOffhandItem();
      ItemStack target = (stack == main) ? off : main;

      boolean removed = removeCurses(target);

      if (removed) {
        level.playSound(
            null,
            user.getX(),
            user.getY(),
            user.getZ(),
            SoundEvents.ENCHANTMENT_TABLE_USE,
            SoundSource.PLAYERS,
            1.0F,
            1.2F);
        if (user instanceof Player player && !player.getAbilities().instabuild) {
          stack.shrink(1);
        }
      }
    }

    if (user instanceof ServerPlayer serverPlayer) {
      serverPlayer.awardStat(Stats.ITEM_USED.get(this));
      CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
    }

    return stack;
  }

  private boolean removeCurses(ItemStack stack) {
    if (stack.isEmpty()) return false;

    Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
    if (enchants.isEmpty()) return false;

    int before = enchants.size();
    enchants.entrySet().removeIf(e -> e.getKey().isCurse());
    int after = enchants.size();

    if (after < before) {
      EnchantmentHelper.setEnchantments(enchants, stack);
      return true;
    }
    return false;
  }
}
