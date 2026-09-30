package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.block.entity.custom.RitualBrazierBlockEntity;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class RitualBrazierBlock extends BaseEntityBlock {

  public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
  public static final BooleanProperty SALTED = BooleanProperty.create("salted");

  protected static final VoxelShape SHAPE = Shapes.or(Block.box(3.0, 0.0, 3.0, 13.0, 1.0, 13.0));

  public RitualBrazierBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(
        this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(SALTED, false));
  }

  @Nullable @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return this.defaultBlockState()
        .setValue(FACING, context.getHorizontalDirection().getOpposite())
        .setValue(SALTED, false);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING, SALTED);
  }

  @Override
  public VoxelShape getShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return SHAPE;
  }

  @Override
  public RenderShape getRenderShape(BlockState state) {
    return RenderShape.MODEL;
  }

  @Nullable @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new RitualBrazierBlockEntity(pos, state);
  }

  @Override
  public void onRemove(
      BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
    if (state.getBlock() != newState.getBlock()) {
      BlockEntity be = level.getBlockEntity(pos);
      if (be instanceof RitualBrazierBlockEntity brazier) {
        Containers.dropItemStack(
            level, pos.getX(), pos.getY(), pos.getZ(), brazier.getStoredItem());
        if (state.getValue(SALTED)) {
          Containers.dropItemStack(
              level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.SALT.get()));
        }
      }
    }
    super.onRemove(state, level, pos, newState, movedByPiston);
  }

  @Override
  public InteractionResult use(
      BlockState state,
      Level level,
      BlockPos pos,
      Player player,
      InteractionHand hand,
      BlockHitResult hit) {
    BlockEntity tileEntity = level.getBlockEntity(pos);
    if (!(tileEntity instanceof RitualBrazierBlockEntity brazier)) {
      return InteractionResult.PASS;
    }

    ItemStack heldStack = player.getItemInHand(hand);
    ItemStack offhandStack = player.getOffhandItem();

    if (hand == InteractionHand.MAIN_HAND && !state.getValue(SALTED)) {
      if (heldStack.is(ModTags.Items.SALT)) {
        if (!level.isClientSide()) {
          level.setBlock(pos, state.setValue(SALTED, true), Block.UPDATE_ALL);
          if (!player.getAbilities().instabuild) {
            heldStack.shrink(1);
          }
          level.playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
      }
    }

    if (brazier.isEmpty()) {
      if (!offhandStack.isEmpty()) {
        if (hand == InteractionHand.MAIN_HAND
            && !offhandStack.is(ModTags.Items.OFFHAND_EQUIPMENT)
            && !(heldStack.getItem() instanceof BlockItem)) {
          return InteractionResult.PASS;
        }
        if (hand == InteractionHand.OFF_HAND && offhandStack.is(ModTags.Items.OFFHAND_EQUIPMENT)) {
          return InteractionResult.PASS;
        }
      }

      if (heldStack.isEmpty()) {
        return InteractionResult.PASS;
      } else if (brazier.addItem(player.getAbilities().instabuild ? heldStack.copy() : heldStack)) {
        playItemSound(level, pos);
        return InteractionResult.sidedSuccess(level.isClientSide());
      }
    } else if (!heldStack.isEmpty() || !offhandStack.isEmpty()) {
      ItemStack focusStack =
          heldStack.is(ModItems.HEX_FOCUS.get())
              ? heldStack
              : offhandStack.is(ModItems.HEX_FOCUS.get()) ? offhandStack : ItemStack.EMPTY;

      if (!focusStack.isEmpty()) return InteractionResult.PASS;
    } else if (hand == InteractionHand.MAIN_HAND) {
      if (!level.isClientSide()) {
        ItemStack removed = brazier.removeItem();
        if (!player.getAbilities().instabuild) {
          if (!player.getInventory().add(removed)) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), removed);
          }
        }
        playItemSound(level, pos);
      }
      return InteractionResult.sidedSuccess(level.isClientSide());
    }

    return InteractionResult.PASS;
  }

  private void playItemSound(Level level, BlockPos pos) {
    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.25f, 0.25f);
  }

  @Nullable @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
      Level level, BlockState state, BlockEntityType<T> type) {
    if (level.isClientSide()) {
      return null;
    }
    return createTickerHelper(
        type, ModBlockEntityTypes.RITUAL_BRAZIER.get(), RitualBrazierBlockEntity::serverTick);
  }
}
