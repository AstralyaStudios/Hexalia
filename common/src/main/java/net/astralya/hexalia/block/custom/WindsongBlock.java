package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.block.entity.custom.WindsongBlockEntity;
import net.astralya.hexalia.gameplay.enchantedplant.WindsongActivation;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class WindsongBlock extends EnchantedPlantBlock implements EntityBlock {
  public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

  public WindsongBlock(Properties properties) {
    super(properties);
    registerDefaultState(defaultBlockState().setValue(ACTIVE, false));
  }

  @Override
  protected ItemInteractionResult useItemOn(
      ItemStack stack,
      BlockState state,
      Level level,
      BlockPos pos,
      Player player,
      InteractionHand hand,
      BlockHitResult hitResult) {
    if (player.getItemInHand(hand).getItem() != ModItems.HEX_FOCUS.get()) {
      return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
    if (!level.isClientSide
        && level.getBlockEntity(pos) instanceof WindsongBlockEntity windsong
        && !windsong.isActive()) {
      WindsongActivation.activatePlaced((ServerLevel) level, pos);
      level.removeBlock(pos, false);
      level.playSound(
          null, pos, SoundEvents.WIND_CHARGE_BURST.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
    }
    return ItemInteractionResult.SUCCESS;
  }

  @Override
  protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
    return state.isFaceSturdy(level, pos, Direction.UP) && !state.is(Blocks.MAGMA_BLOCK);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(ACTIVE);
  }

  @Override
  public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new WindsongBlockEntity(pos, state);
  }

  @Override
  public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
      Level level, BlockState state, BlockEntityType<T> type) {
    return level.isClientSide
        ? null
        : (tickerLevel, tickerPos, tickerState, blockEntity) -> {
          if (blockEntity instanceof WindsongBlockEntity windsong) {
            WindsongBlockEntity.tick(tickerLevel, tickerPos, tickerState, windsong);
          }
        };
  }
}
