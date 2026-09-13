package net.astralya.hexalia.item.custom;

import java.util.List;
import net.astralya.hexalia.entity.ModEntities;
import net.astralya.hexalia.entity.custom.projectile.CinderhewProjectile;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class CinderhewItem extends AxeItem {
  private static final float FIRE_DAMAGE = 2.0F;
  private static final int THROW_COOLDOWN_TICKS = 100;

  public CinderhewItem(Tier tier, Properties properties) {
    super(tier, properties);
  }

  @Override
  public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
    if (!attacker.level().isClientSide()) {
      int invulnerableTime = target.invulnerableTime;
      target.invulnerableTime = 0;
      target.hurt(target.damageSources().onFire(), FIRE_DAMAGE);
      target.invulnerableTime = Math.max(target.invulnerableTime, invulnerableTime);
      if (!target.fireImmune()) {
        target.igniteForSeconds(2.0F);
      }
    }
    return super.hurtEnemy(stack, target, attacker);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);
    if (player.getCooldowns().isOnCooldown(this)) {
      return InteractionResultHolder.fail(stack);
    }

    if (!level.isClientSide()) {
      ItemStack thrownStack = stack.copy();
      player.setItemInHand(hand, ItemStack.EMPTY);
      CinderhewProjectile projectile =
          new CinderhewProjectile(ModEntities.CINDERHEW.get(), level, player, thrownStack);
      projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.6F, 1.0F);
      level.addFreshEntity(projectile);
      player.getCooldowns().addCooldown(this, THROW_COOLDOWN_TICKS);
      player.awardStat(Stats.ITEM_USED.get(this));
      level.playSound(
          null,
          player.getX(),
          player.getY(),
          player.getZ(),
          SoundEvents.TRIDENT_THROW.value(),
          SoundSource.PLAYERS,
          0.7F,
          0.9F + level.getRandom().nextFloat() * 0.2F);
      return InteractionResultHolder.success(ItemStack.EMPTY);
    }

    return InteractionResultHolder.sidedSuccess(stack, true);
  }

  @Override
  public void appendHoverText(
      ItemStack stack,
      TooltipContext context,
      List<Component> tooltipComponents,
      TooltipFlag flag) {
    tooltipComponents.add(
        Component.translatable("tooltip.hexalia.cinderhew.fire_damage")
            .withStyle(ChatFormatting.GOLD));
    tooltipComponents.add(
        Component.translatable("tooltip.hexalia.throwable")
            .withStyle(ChatFormatting.GRAY)
            .withStyle(ChatFormatting.ITALIC));
  }
}
