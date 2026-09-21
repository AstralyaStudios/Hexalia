package net.astralya.hexalia.block.custom;

import com.mojang.serialization.MapCodec;
import java.util.EnumMap;
import java.util.Map;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.block.entity.custom.CenserBlockEntity;
import net.astralya.hexalia.gameplay.censer.CenserEffectHandler;
import net.astralya.hexalia.gameplay.censer.HerbCombination;
import net.astralya.hexalia.util.FireStarterHelper;
import net.astralya.hexalia.util.ItemInteractionHelper;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CenserBlock extends BaseEntityBlock {
  public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
  public static final BooleanProperty LIT = BlockStateProperties.LIT;
  public static final MapCodec<CenserBlock> CODEC = simpleCodec(CenserBlock::new);

  private static final VoxelShape SHAPE_NORTH =
      Shapes.or(
          Shapes.box(0.0625, 0.0, 0.0, 0.3125, 0.25, 1.0),
          Shapes.box(0.6875, 0.0, 0.0, 0.9375, 0.25, 1.0),
          Shapes.box(0.3125, 0.0, 0.0, 0.6875, 0.0625, 1.0),
          Shapes.box(0.0, 0.25, 0.0625, 1.0, 0.375, 0.9375),
          Shapes.box(0.125, 0.375, 0.1875, 0.25, 0.5, 0.8125),
          Shapes.box(0.75, 0.375, 0.1875, 0.875, 0.5, 0.8125),
          Shapes.box(0.25, 0.375, 0.6875, 0.75, 0.5, 0.8125),
          Shapes.box(0.25, 0.375, 0.1875, 0.75, 0.5, 0.3125));
  private static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);

  static {
    SHAPES.put(Direction.NORTH, SHAPE_NORTH);
    SHAPES.put(Direction.SOUTH, rotateShape(Direction.NORTH, Direction.SOUTH, SHAPE_NORTH));
    SHAPES.put(Direction.EAST, rotateShape(Direction.NORTH, Direction.EAST, SHAPE_NORTH));
    SHAPES.put(Direction.WEST, rotateShape(Direction.NORTH, Direction.WEST, SHAPE_NORTH));
  }

  public CenserBlock(Properties properties) {
    super(properties);
    registerDefaultState(
        defaultBlockState().setValue(FACING, Direction.NORTH).setValue(LIT, false));
  }

  @Override
  protected MapCodec<? extends BaseEntityBlock> codec() {
    return CODEC;
  }

  @Override
  public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
    return defaultBlockState()
        .setValue(FACING, context.getHorizontalDirection().getOpposite())
        .setValue(LIT, false);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING, LIT);
  }

  @Override
  protected VoxelShape getShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return SHAPES.getOrDefault(state.getValue(FACING), SHAPE_NORTH);
  }

  @Override
  protected VoxelShape getCollisionShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return SHAPES.getOrDefault(state.getValue(FACING), SHAPE_NORTH);
  }

  @Override
  protected RenderShape getRenderShape(BlockState state) {
    return RenderShape.MODEL;
  }

  @Override
  protected InteractionResult useWithoutItem(
      BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
    if (!(level.getBlockEntity(pos) instanceof CenserBlockEntity censer)) {
      return InteractionResult.PASS;
    }
    return ItemInteractionHelper.tryExtractOneItem(level, pos, player, censer);
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
    if (!(level.getBlockEntity(pos) instanceof CenserBlockEntity censer)) {
      return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
    if (FireStarterHelper.isFireStarter(stack) && !state.getValue(LIT)) {
      return tryLight(state, level, pos, player, hand, stack, censer);
    }
    if (stack.getItem() instanceof ShovelItem && state.getValue(LIT)) {
      return tryExtinguish(state, level, pos, censer);
    }
    if (!state.getValue(LIT)) {
      if (!stack.is(ModTags.Items.HERBS)) {
        if (level.isClientSide()) {
          player.displayClientMessage(Component.translatable("message.hexalia.invalid_item"), true);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
      }
      return ItemInteractionHelper.tryInsertOneItem(
          level, pos, player, hand, censer, item -> item.is(ModTags.Items.HERBS));
    }
    return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
  }

  private ItemInteractionResult tryLight(
      BlockState state,
      Level level,
      BlockPos pos,
      Player player,
      InteractionHand hand,
      ItemStack stack,
      CenserBlockEntity censer) {
    if (!censer.hasTwoHerbs()) {
      if (level.isClientSide()) {
        player.displayClientMessage(
            Component.translatable("message.hexalia.censer_not_full"), true);
      }
      return ItemInteractionResult.FAIL;
    }
    HerbCombination combination = censer.getStoredCombination();
    if (!CenserEffectHandler.isValidCombination(combination)) {
      if (level.isClientSide()) {
        player.displayClientMessage(
            Component.translatable("message.hexalia.invalid_herb_combination"), true);
      }
      return ItemInteractionResult.FAIL;
    }
    if (level.isClientSide()) {
      return ItemInteractionResult.SUCCESS;
    }

    censer.clearItems();
    level.setBlockAndUpdate(pos, state.setValue(LIT, true));
    censer.setActiveCombination(combination);
    censer.setBurnTime(CenserEffectHandler.duration());
    sendEffectActivationMessage(level, pos, combination, player);
    CenserEffectHandler.applyEffect(level, pos, combination);
    FireStarterHelper.consumeFireStarter((ServerLevel) level, player, hand, stack);
    level.playSound(
        null,
        pos,
        SoundEvents.FLINTANDSTEEL_USE,
        SoundSource.BLOCKS,
        1.0F,
        level.random.nextFloat() * 0.4F + 0.8F);
    return ItemInteractionResult.SUCCESS;
  }

  public static boolean tryLightFromDispenser(
      ServerLevel level, BlockPos pos, BlockState state, ItemStack stack) {
    if (state.getValue(LIT) || !(level.getBlockEntity(pos) instanceof CenserBlockEntity censer)) {
      return false;
    }
    if (!censer.hasTwoHerbs()) {
      return false;
    }
    HerbCombination combination = censer.getStoredCombination();
    if (!CenserEffectHandler.isValidCombination(combination)) {
      return false;
    }
    censer.clearItems();
    level.setBlockAndUpdate(pos, state.setValue(LIT, true));
    censer.setActiveCombination(combination);
    censer.setBurnTime(CenserEffectHandler.duration());
    CenserEffectHandler.applyEffect(level, pos, combination);
    FireStarterHelper.consumeFireStarter(level, null, null, stack);
    level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
    return true;
  }

  private ItemInteractionResult tryExtinguish(
      BlockState state, Level level, BlockPos pos, CenserBlockEntity censer) {
    if (level.isClientSide()) {
      return ItemInteractionResult.SUCCESS;
    }
    level.setBlockAndUpdate(pos, state.setValue(LIT, false));
    censer.setBurnTime(0);
    censer.setActiveCombination(null);
    censer.clearItems();
    level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 1.0F);
    return ItemInteractionResult.SUCCESS;
  }

  private void sendEffectActivationMessage(
      Level level, BlockPos pos, HerbCombination combination, Player activatingPlayer) {
    String key = CenserEffectHandler.getMessageKeyForCombination(combination);
    AABB area = new AABB(pos).inflate(CenserEffectHandler.radius());
    for (Player player : level.getEntitiesOfClass(Player.class, area)) {
      if (player instanceof ServerPlayer serverPlayer) {
        serverPlayer.displayClientMessage(Component.translatable(key), true);
      }
    }
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    if (!state.getValue(LIT)) {
      return;
    }
    if (random.nextInt(10) == 0) {
      level.playLocalSound(
          pos.getX() + 0.5,
          pos.getY() + 0.5,
          pos.getZ() + 0.5,
          SoundEvents.CAMPFIRE_CRACKLE,
          SoundSource.BLOCKS,
          0.5F + random.nextFloat(),
          random.nextFloat() * 0.7F + 0.6F,
          false);
    }
    level.addAlwaysVisibleParticle(
        ParticleTypes.CAMPFIRE_COSY_SMOKE,
        true,
        pos.getX() + 0.5 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
        pos.getY() + random.nextDouble() + random.nextDouble(),
        pos.getZ() + 0.5 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
        0.0,
        0.07,
        0.0);
    level.addParticle(
        ParticleTypes.SMOKE,
        pos.getX() + 0.5 + random.nextDouble() / 4.0 * (random.nextBoolean() ? 1 : -1),
        pos.getY() + 0.4,
        pos.getZ() + 0.5 + random.nextDouble() / 4.0 * (random.nextBoolean() ? 1 : -1),
        0.0,
        0.005,
        0.0);
  }

  @Override
  protected void onRemove(
      BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
    if (!state.is(newState.getBlock())
        && level.getBlockEntity(pos) instanceof CenserBlockEntity censer) {
      censer.dropContents(level);
      level.updateNeighbourForOutputSignal(pos, this);
    }
    super.onRemove(state, level, pos, newState, moved);
  }

  @Override
  public BlockState rotate(BlockState state, Rotation rotation) {
    return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
  }

  @Override
  public BlockState mirror(BlockState state, Mirror mirror) {
    return state.rotate(mirror.getRotation(state.getValue(FACING)));
  }

  @Override
  public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new CenserBlockEntity(pos, state);
  }

  @Override
  public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(
      Level level, BlockState state, BlockEntityType<T> type) {
    return !level.isClientSide() && type == ModBlockEntityTypes.CENSER.get()
        ? (tickLevel, tickPos, tickState, blockEntity) ->
            ((CenserBlockEntity) blockEntity).tick(tickLevel, tickPos, tickState)
        : null;
  }

  private static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
    VoxelShape[] buffer = {shape, Shapes.empty()};
    int steps = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
    for (int index = 0; index < steps; index++) {
      buffer[0].forAllBoxes(
          (x1, y1, z1, x2, y2, z2) ->
              buffer[1] = Shapes.or(buffer[1], Shapes.box(1 - z2, y1, x1, 1 - z1, y2, x2)));
      buffer[0] = buffer[1];
      buffer[1] = Shapes.empty();
    }
    return buffer[0];
  }
}
