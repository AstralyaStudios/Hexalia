package net.astralya.hexalia.item.custom;

import java.util.List;
import net.astralya.hexalia.HexaliaConfig;
import net.astralya.hexalia.effect.ModMobEffects;
import net.astralya.hexalia.integration.accessories.AccessoriesIntegration;
import net.astralya.hexalia.sound.ModSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class MandrakeItem extends Item {
  public MandrakeItem(Properties properties) {
    super(properties);
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
    if (!level.isClientSide && user instanceof Player player) {
      List<Entity> entities =
          level.getEntities(
              player, player.getBoundingBox().inflate(HexaliaConfig.mandrakeScreamRadius()));
      for (Entity entity : entities) {
        if (entity instanceof LivingEntity livingEntity
            && (!(livingEntity instanceof Player target)
                || !AccessoriesIntegration.isWearingEarplugs(target))) {
          livingEntity.addEffect(
              new MobEffectInstance(
                  ModMobEffects.holder(ModMobEffects.STUNNED),
                  HexaliaConfig.mandrakeStunDuration() * 20,
                  0));
        }
      }
      level.playSound(
          null,
          player.getX(),
          player.getY(),
          player.getZ(),
          ModSoundEvents.MANDRAKE_SCREAM.get(),
          SoundSource.PLAYERS,
          1.0F,
          1.0F);
      if (!player.getAbilities().instabuild) {
        stack.shrink(1);
      }
    }
    return stack.isEmpty() ? ItemStack.EMPTY : stack;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    return ItemUtils.startUsingInstantly(level, player, hand);
  }

  @Override
  public UseAnim getUseAnimation(ItemStack stack) {
    return UseAnim.BOW;
  }

  @Override
  public int getUseDuration(ItemStack stack, LivingEntity user) {
    return 32;
  }
}
