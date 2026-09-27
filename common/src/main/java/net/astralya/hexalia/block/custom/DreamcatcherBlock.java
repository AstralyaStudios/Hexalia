package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.Configuration;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.block.entity.custom.DreamcatcherBlockEntity;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DreamcatcherBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape NORTH_SHAPE = Shapes.box(0.125, 0.0625, 0.9375, 0.875, 0.875, 1);
    private static final VoxelShape SOUTH_SHAPE = Shapes.box(0.125, 0.0625, 0, 0.875, 0.875, 0.0625);
    private static final VoxelShape WEST_SHAPE = Shapes.box(0.9375, 0.0625, 0.125, 1, 0.875, 0.875);
    private static final VoxelShape EAST_SHAPE = Shapes.box(0, 0.0625, 0.125, 0.0625, 0.875, 0.875);

    private static final int PHANTOM_CHECK_INTERVAL = 20;

    public DreamcatcherBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction.getOpposite() == state.getValue(FACING) && !state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.relative(state.getValue(FACING).getOpposite())).isSolid();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            case EAST -> EAST_SHAPE;
            default -> NORTH_SHAPE;
        };
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof DreamcatcherBlockEntity be)) {
            return InteractionResult.PASS;
        }

        ItemStack held = player.getItemInHand(hand);

        if (held.isEmpty() && player.isShiftKeyDown()) {
            if (!level.isClientSide()) {
                ItemStack returned = be.tryExtractFuel(player);
                if (!returned.isEmpty()) {
                    if (!player.getInventory().add(returned)) {
                        player.drop(returned, false);
                    }
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 0.5f, 1.0f);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        if (held.is(ModItems.FIRE_NODE.get())) {
            if (!level.isClientSide()) {
                InteractionResult result = be.tryInsertFuel(player, held);
                if (result == InteractionResult.SUCCESS || result == InteractionResult.CONSUME) {
                    level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 0.6f, 0.8f + level.random.nextFloat() * 0.4f);
                } else if (result == InteractionResult.FAIL) {
                    player.displayClientMessage(Component.translatable("message.hexalia.dreamcatcher_full"), true);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!level.isClientSide()) {
            return;
        }

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof DreamcatcherBlockEntity dreamcatcher && dreamcatcher.hasFuel()) {
            dreamcatcher.spawnActiveParticles(level, pos, random);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DreamcatcherBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        }
        return createTickerHelper(type, ModBlockEntityTypes.DREAMCATCHER.get(), (lvl, p, st, be) -> {
            DreamcatcherBlockEntity.tick(lvl, p, st, be);
            if (lvl.getGameTime() % PHANTOM_CHECK_INTERVAL == 0) {
                tickPhantoms((ServerLevel) lvl, p, be);
            }
        });
    }

    private static void tickPhantoms(ServerLevel level, BlockPos pos, DreamcatcherBlockEntity be) {
        if (!be.hasFuel() || !level.isNight()) {
            return;
        }

        int radius = Configuration.DREAMCATCHER_RADIUS.get();
        AABB area = new AABB(pos).inflate(radius);
        List<Phantom> phantoms = level.getEntitiesOfClass(Phantom.class, area);
        if (phantoms.isEmpty()) {
            return;
        }

        for (Phantom phantom : phantoms) {
            phantom.setSecondsOnFire(Configuration.PHANTOM_IGNITE_DURATION.get() / 20);
            phantom.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1, false, false, false));
            spawnIgniteParticles(level, phantom.position());
        }

        level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 0.4f, 1.2f + level.random.nextFloat() * 0.3f);
    }

    private static void spawnIgniteParticles(ServerLevel level, Vec3 pos) {
        level.sendParticles(ParticleTypes.FLAME, pos.x, pos.y + 1.0, pos.z, 8, 0.3, 0.5, 0.3, 0.05);
        level.sendParticles(ParticleTypes.SMOKE, pos.x, pos.y + 1.0, pos.z, 4, 0.2, 0.3, 0.2, 0.02);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
