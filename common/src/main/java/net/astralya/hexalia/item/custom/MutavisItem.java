package net.astralya.hexalia.item.custom;

import net.astralya.hexalia.block.custom.MorphoraBlock;
import net.astralya.hexalia.gameplay.mutation.MutationProcess;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class MutavisItem extends Item {
  public MutavisItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Level level = context.getLevel();
    if (level.isClientSide()) {
      return InteractionResult.SUCCESS;
    }
    BlockPos pos = context.getClickedPos();
    ItemStack stack = context.getItemInHand();
    ServerLevel server = (ServerLevel) level;
    if (server.getBlockState(pos).getBlock() instanceof MorphoraBlock morphora
        && morphora.tryActivateWithMutavis(server, pos, stack, context.getPlayer())) {
      return InteractionResult.CONSUME;
    }
    return tryMutate(server, pos, stack, context.getPlayer())
        ? InteractionResult.CONSUME
        : InteractionResult.PASS;
  }

  public boolean tryMutate(ServerLevel level, BlockPos pos, ItemStack mutavisStack, Player player) {
    MutationProcess.Target target = MutationProcess.findTarget(level, pos);
    if (target == null) return false;
    if (player == null || !player.getAbilities().instabuild) {
      mutavisStack.shrink(1);
    }
    MutationProcess.startDirect(level, target);
    playApplicationSound(level, pos);
    return true;
  }

  public static void playApplicationSound(ServerLevel level, BlockPos pos) {
    level.playSound(null, pos, SoundEvents.HONEY_BLOCK_PLACE, SoundSource.BLOCKS, 0.9F,
        0.85F + level.random.nextFloat() * 0.15F);
  }
}
