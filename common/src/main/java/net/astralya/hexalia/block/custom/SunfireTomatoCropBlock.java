package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class SunfireTomatoCropBlock extends CropBlock {
  public static final int MAX_AGE = 3;
  public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 3);

  public SunfireTomatoCropBlock(Properties pProperties) {
    super(pProperties);
  }

  @Override
  protected ItemLike getBaseSeedId() {
    return ModItems.SUNFIRE_TOMATO_SEEDS.get();
  }

  @Override
  protected IntegerProperty getAgeProperty() {
    return AGE;
  }

  @Override
  public int getMaxAge() {
    return MAX_AGE;
  }

  @Override
  public InteractionResult use(
      BlockState pState,
      Level pLevel,
      BlockPos pPos,
      Player pPlayer,
      InteractionHand pHand,
      BlockHitResult pHit) {
    int i = pState.getValue(AGE);
    if (i != 3) {
      return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }
    if (pPlayer.getItemInHand(pHand).is(Items.BONE_MEAL)) {
      return InteractionResult.PASS;
    }
    int j = 1 + pLevel.random.nextInt(1);
    SunfireTomatoCropBlock.popResource(
        pLevel, pPos, new ItemStack(ModItems.SUNFIRE_TOMATO.get(), j + 1));
    pLevel.playSound(
        null,
        pPos,
        SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
        SoundSource.BLOCKS,
        1.0f,
        0.8f + pLevel.random.nextFloat() * 0.4f);

    BlockState newState = pState.setValue(AGE, 1);
    pLevel.setBlock(pPos, newState, Block.UPDATE_ALL);
    pLevel.gameEvent(GameEvent.BLOCK_CHANGE, pPos, GameEvent.Context.of(pPlayer, newState));
    return InteractionResult.sidedSuccess(pLevel.isClientSide);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    pBuilder.add(AGE);
  }
}
