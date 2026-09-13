package net.astralya.hexalia.item.custom;

import java.util.List;
import net.astralya.hexalia.entity.ModEntities;
import net.astralya.hexalia.entity.custom.projectile.ThornArrowEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ThornbowItem extends BowItem {
  private static final int EXTRA_DURABILITY_COST_PER_SHOT = 1;

  public ThornbowItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);
    player.startUsingItem(hand);
    return InteractionResultHolder.consume(stack);
  }

  @Override
  public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeLeft) {
    if (!(livingEntity instanceof Player player)) {
      return;
    }

    int used = getUseDuration(stack, livingEntity) - timeLeft;
    float power = BowItem.getPowerForTime(used);

    if (power < 0.1F) {
      return;
    }

    if (level.isClientSide) {
      return;
    }

    ThornArrowEntity projectile =
        new ThornArrowEntity(ModEntities.THORN_ARROW.get(), level, player, stack);

    projectile.shootFromRotation(
        player, player.getXRot(), player.getYRot(), 0.0F, power * 3.0F, 1.0F);

    if (power == 1.0F) {
      projectile.setCritArrow(true);
    }

    level.addFreshEntity(projectile);

    level.playSound(
        null,
        player.getX(),
        player.getY(),
        player.getZ(),
        SoundEvents.ARROW_SHOOT,
        SoundSource.PLAYERS,
        1.0F,
        1.0F);

    int damage = 1 + EXTRA_DURABILITY_COST_PER_SHOT;
    stack.hurtAndBreak(damage, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));
  }

  @Override
  public boolean isEnchantable(ItemStack stack) {
    return true;
  }

  @Override
  public int getEnchantmentValue() {
    return 1;
  }

  @Override
  public void appendHoverText(
      ItemStack stack,
      TooltipContext context,
      List<Component> tooltipComponents,
      TooltipFlag tooltipFlag) {
    tooltipComponents.add(
        Component.translatable("tooltip.hexalia.thornbow.no_arrows")
            .withStyle(ChatFormatting.GRAY));
    tooltipComponents.add(
        Component.translatable("tooltip.hexalia.thornbow.bleeding").withStyle(ChatFormatting.GRAY));
  }
}
