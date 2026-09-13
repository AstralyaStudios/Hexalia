package net.astralya.hexalia.event;

import java.util.ArrayList;
import java.util.List;
import net.astralya.hexalia.integration.accessories.AccessoriesIntegration;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public final class GreenOmenEffects {
  private static final float DOUBLE_DROPS_CHANCE = 0.1F;

  private GreenOmenEffects() {}

  public static List<ItemStack> createBonusDrops(
      ServerPlayer player, BlockPos pos, BlockState state, List<ItemStack> drops) {
    if (drops.isEmpty()
        || player.isCreative()
        || !state.is(ModTags.Blocks.CROPS)
        || !AccessoriesIntegration.isEquipped(player, ModItems.GREEN_OMEN.get())
        || player.getRandom().nextFloat() >= DOUBLE_DROPS_CHANCE) {
      return List.of();
    }

    List<ItemStack> bonusDrops = new ArrayList<>(drops.size());
    for (ItemStack drop : drops) {
      if (!drop.isEmpty()) {
        bonusDrops.add(drop.copy());
      }
    }
    playFeedback(player, pos);
    return bonusDrops;
  }

  private static void playFeedback(ServerPlayer player, BlockPos pos) {
    ServerLevel level = player.serverLevel();
    level.sendParticles(
        ParticleTypes.HAPPY_VILLAGER,
        pos.getX() + 0.5,
        pos.getY() + 0.5,
        pos.getZ() + 0.5,
        5,
        0.25,
        0.2,
        0.25,
        0.0);
    level.playSound(
        null,
        pos,
        SoundEvents.AMETHYST_BLOCK_CHIME,
        SoundSource.PLAYERS,
        0.3F,
        1.2F);
  }
}
