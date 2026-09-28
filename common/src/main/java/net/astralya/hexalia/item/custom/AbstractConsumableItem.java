package net.astralya.hexalia.item.custom;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public abstract class AbstractConsumableItem extends Item {

  public AbstractConsumableItem(Properties properties) {
    super(properties);
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
    ItemStack original = stack.copy();
    ItemStack current = super.finishUsingItem(stack, level, user);

    if (user instanceof ServerPlayer sp) {
      CriteriaTriggers.CONSUME_ITEM.trigger(sp, original);
      sp.awardStat(Stats.ITEM_USED.get(this));
    }

    handleEffects(level, user, original);

    ItemStack container = getReturnContainer(original);

    if (!level.isClientSide && user instanceof Player player && !player.getAbilities().instabuild) {
      boolean isFood = original.isEdible();
      if (!isFood) current.shrink(1);

      if (current.isEmpty()) {
        return container;
      }
      if (!container.isEmpty()) {
        if (!player.getInventory().add(container)) {
          player.drop(container, false);
        }
      }
    }

    return current;
  }

  protected abstract void handleEffects(Level level, LivingEntity user, ItemStack consumedStack);

  protected abstract ItemStack getReturnContainer(ItemStack consumedStack);

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    return ItemUtils.startUsingInstantly(level, player, hand);
  }

  @Override
  public int getUseDuration(ItemStack stack) {
    return 32;
  }

  @Override
  public UseAnim getUseAnimation(ItemStack stack) {
    return UseAnim.DRINK;
  }

  @Override
  public void appendHoverText(
      ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
    Component line = getTooltip(stack);
    if (line != null) tooltip.add(line);
  }

  @Nullable protected Component getTooltip(ItemStack stack) {
    return null;
  }
}
