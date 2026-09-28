package net.astralya.hexalia.item.custom;

import java.util.List;
import java.util.function.BiFunction;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ThrownSacItem extends Item {

  private final BiFunction<Level, Player, ? extends ThrowableItemProjectile> projectileFactory;
  private final boolean requireSneakToThrow;

  public ThrownSacItem(
      Properties props,
      BiFunction<Level, Player, ? extends ThrowableItemProjectile> projectileFactory) {
    this(props, projectileFactory, false);
  }

  public ThrownSacItem(
      Properties props,
      BiFunction<Level, Player, ? extends ThrowableItemProjectile> projectileFactory,
      boolean requireSneakToThrow) {
    super(props);
    this.projectileFactory = projectileFactory;
    this.requireSneakToThrow = requireSneakToThrow;
  }

  @Override
  public void appendHoverText(
      ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
    tooltip.add(
        Component.translatable("tooltip.hexalia.throwable")
            .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);

    boolean shouldThrow = !requireSneakToThrow || player.isCrouching();
    if (shouldThrow) {
      if (!level.isClientSide) {
        var proj = projectileFactory.apply(level, player);
        proj.setItem(stack.copyWithCount(1));
        proj.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.5F, 1.0F);
        level.addFreshEntity(proj);

        level.playSound(
            null,
            player.getX(),
            player.getY(),
            player.getZ(),
            SoundEvents.SPLASH_POTION_THROW,
            SoundSource.PLAYERS,
            0.5F,
            0.8F + level.getRandom().nextFloat() * 0.4F);

        if (!player.getAbilities().instabuild) {
          stack.shrink(1);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
      }
      return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    return ItemUtils.startUsingInstantly(level, player, hand);
  }
}
