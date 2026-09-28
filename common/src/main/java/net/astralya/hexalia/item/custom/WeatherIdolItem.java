package net.astralya.hexalia.item.custom;

import net.astralya.hexalia.item.ModItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class WeatherIdolItem extends Item {

  public WeatherIdolItem(Properties pProperties) {
    super(pProperties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    player.startUsingItem(hand);
    return InteractionResultHolder.consume(player.getItemInHand(hand));
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
    if (!level.isClientSide && livingEntity instanceof Player player) {
      if (level instanceof ServerLevel serverLevel) {
        if (stack.getItem() == ModItems.RAINFALL_IDOL.get()) {
          serverLevel.setWeatherParameters(0, 6000, true, false);
          player.displayClientMessage(
              Component.translatable("message.hexalia.rainfall_idol"), true);
        } else if (stack.getItem() == ModItems.CLARITY_IDOL.get()) {
          serverLevel.setWeatherParameters(6000, 0, false, false);
          player.displayClientMessage(Component.translatable("message.hexalia.clarity_idol"), true);
        } else if (stack.getItem() == ModItems.TEMPEST_IDOL.get()) {
          serverLevel.setWeatherParameters(0, 6000, true, true);
          player.displayClientMessage(Component.translatable("message.hexalia.tempest_idol"), true);
        }
      }
      if (!player.isCreative()) {
        stack.shrink(1);
      }
    }

    if (livingEntity instanceof ServerPlayer serverPlayer) {
      serverPlayer.awardStat(Stats.ITEM_USED.get(this));
      CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
    }

    return stack.isEmpty() ? ItemStack.EMPTY : stack;
  }

  @Override
  public UseAnim getUseAnimation(ItemStack pStack) {
    return UseAnim.BOW;
  }

  @Override
  public int getUseDuration(ItemStack pStack) {
    return 32;
  }
}
