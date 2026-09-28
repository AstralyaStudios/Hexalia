package net.astralya.hexalia.item.custom;

import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.block.custom.SilkwormCocoonBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class SilkwormItem extends Item {

  public SilkwormItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Level level = context.getLevel();
    BlockPos clickedPos = context.getClickedPos();
    BlockState clickedState = level.getBlockState(clickedPos);

    if (!clickedState.is(BlockTags.LOGS)) {
      return InteractionResult.PASS;
    }

    Direction face = context.getClickedFace();
    if (face.getAxis().isVertical()) {
      return InteractionResult.FAIL;
    }

    BlockPos placePos = clickedPos.relative(face);
    BlockState placeState = level.getBlockState(placePos);

    if (!placeState.isAir() && !placeState.canBeReplaced((BlockPlaceContext) context)) {
      return InteractionResult.FAIL;
    }

    BlockState cocoonState =
        ModBlocks.SILKWORM_COCOON
            .get()
            .defaultBlockState()
            .setValue(SilkwormCocoonBlock.FACING, face);

    if (!cocoonState.canSurvive(level, placePos)) {
      return InteractionResult.FAIL;
    }

    if (level.isClientSide) {
      return InteractionResult.SUCCESS;
    }

    level.setBlock(placePos, cocoonState, 3);
    level.gameEvent(context.getPlayer(), GameEvent.BLOCK_PLACE, placePos);
    level.playSound(null, placePos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 0.8F, 1.0F);

    Player player = context.getPlayer();
    if (player == null || !player.getAbilities().instabuild) {
      context.getItemInHand().shrink(1);
    }

    return InteractionResult.CONSUME;
  }
}
