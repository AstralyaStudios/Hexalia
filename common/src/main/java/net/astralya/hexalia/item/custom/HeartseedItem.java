package net.astralya.hexalia.item.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class HeartseedItem extends Item {
  public HeartseedItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult interactLivingEntity(
      ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
    if (!(target instanceof TamableAnimal animal) || !animal.isAlive() || animal.isTame()) {
      return InteractionResult.PASS;
    }

    Level level = animal.level();
    if (!level.isClientSide) {
      animal.tame(player);
      if (!player.getAbilities().instabuild) {
        stack.shrink(1);
      }
      level.broadcastEntityEvent(animal, (byte) 7);
      level.playSound(
          null,
          animal.blockPosition(),
          SoundEvents.AMETHYST_BLOCK_CHIME,
          SoundSource.NEUTRAL,
          0.6F,
          1.2F);
    }
    return InteractionResult.sidedSuccess(level.isClientSide);
  }
}
