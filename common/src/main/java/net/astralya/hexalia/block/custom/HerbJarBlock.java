package net.astralya.hexalia.block.custom;

import com.mojang.serialization.MapCodec;
import java.util.List;
import net.astralya.hexalia.block.entity.custom.HerbJarBlockEntity;
import net.astralya.hexalia.component.ModComponents;
import net.astralya.hexalia.component.item.HerbJarData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class HerbJarBlock extends BaseEntityBlock {
  public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
  public static final BooleanProperty HAS_CONTENTS = BooleanProperty.create("has_contents");
  public static final MapCodec<HerbJarBlock> CODEC = simpleCodec(HerbJarBlock::new);
  private static final VoxelShape SHAPE =
      Shapes.or(
          Block.box(4.0, 0.0, 4.0, 12.0, 11.0, 12.0), Block.box(5.0, 11.0, 5.0, 11.0, 12.0, 11.0));

  public HerbJarBlock(Properties properties) {
    super(properties);
    registerDefaultState(
        defaultBlockState().setValue(FACING, Direction.NORTH).setValue(HAS_CONTENTS, false));
  }

  @Override
  protected MapCodec<? extends BaseEntityBlock> codec() {
    return CODEC;
  }

  @Override
  protected RenderShape getRenderShape(BlockState state) {
    return RenderShape.MODEL;
  }

  @Override
  protected VoxelShape getShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return SHAPE;
  }

  @Override
  protected VoxelShape getCollisionShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return SHAPE;
  }

  @Override
  public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new HerbJarBlockEntity(pos, state);
  }

  @Override
  public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
    return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING, HAS_CONTENTS);
  }

  @Override
  public void appendHoverText(
      ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
    HerbJarData data = stack.get(ModComponents.HERB_JAR.get());
    if (data == null || data.isEmpty()) {
      return;
    }

    ItemStack storedItem = ItemStack.EMPTY;
    int storedCount = 0;
    for (int slot = 0; slot < HerbJarData.SIZE; slot++) {
      ItemStack slotItem = data.getItem(slot);
      if (!slotItem.isEmpty()) {
        if (storedItem.isEmpty()) {
          storedItem = slotItem;
        }
        storedCount += slotItem.getCount();
      }
    }
    if (!storedItem.isEmpty()) {
      tooltip.add(
          Component.translatable(
                  "tooltip.hexalia.herb_jar.stores", storedItem.getHoverName(), storedCount)
              .withStyle(ChatFormatting.GRAY));
    }
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
    if (!(level.getBlockEntity(pos) instanceof HerbJarBlockEntity herbJar)
        || !herbJar.canInsert(stack)) {
      return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
    if (level.isClientSide()) {
      return ItemInteractionResult.SUCCESS;
    }

    int inserted = herbJar.insert(stack);
    if (inserted == 0) {
      return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
    if (!player.isCreative()) {
      stack.shrink(inserted);
    }
    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
    level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
    return ItemInteractionResult.CONSUME;
  }

  @Override
  protected InteractionResult useWithoutItem(
      BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
    if (!(level.getBlockEntity(pos) instanceof HerbJarBlockEntity herbJar) || herbJar.isEmpty()) {
      return InteractionResult.PASS;
    }
    if (level.isClientSide()) {
      return InteractionResult.SUCCESS;
    }

    ItemStack extracted = herbJar.extractLastStack();
    if (extracted.isEmpty()) {
      return InteractionResult.PASS;
    }
    if (!player.getInventory().add(extracted)) {
      Containers.dropItemStack(level, player.getX(), player.getY(), player.getZ(), extracted);
    }
    level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
    level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
    return InteractionResult.CONSUME;
  }

  @Override
  public void playerDestroy(
      Level level,
      Player player,
      BlockPos pos,
      BlockState state,
      @Nullable BlockEntity blockEntity,
      ItemStack tool) {
    if (player.isCreative()) {
      return;
    }
    if (level instanceof ServerLevel && blockEntity instanceof HerbJarBlockEntity herbJar) {
      ItemStack drop = new ItemStack(this);
      HerbJarData data = herbJar.createData();
      if (!data.isEmpty()) {
        drop.set(ModComponents.HERB_JAR.get(), data);
      }
      popResource(level, pos, drop);
      return;
    }
    super.playerDestroy(level, player, pos, state, blockEntity, tool);
  }

  @Override
  public void setPlacedBy(
      Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
    super.setPlacedBy(level, pos, state, placer, stack);
    if (!level.isClientSide() && level.getBlockEntity(pos) instanceof HerbJarBlockEntity herbJar) {
      HerbJarData data = stack.get(ModComponents.HERB_JAR.get());
      if (data != null) {
        herbJar.restoreData(data);
      }
    }
  }
}
