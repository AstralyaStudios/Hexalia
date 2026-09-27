package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.block.entity.custom.MortarAndPestleBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class MortarAndPestleBlock extends BaseEntityBlock {

    private static final VoxelShape SHAPE = Shapes.or(
            Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.0625, 0.8125),
            Shapes.box(0.6875, 0.0625, 0.3125, 0.8125, 0.25, 0.6875),
            Shapes.box(0.1875, 0.0625, 0.3125, 0.3125, 0.25, 0.6875),
            Shapes.box(0.1875, 0.0625, 0.1875, 0.8125, 0.25, 0.3125),
            Shapes.box(0.1875, 0.0625, 0.6875, 0.8125, 0.25, 0.8125)
    );

    public MortarAndPestleBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MortarAndPestleBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntityTypes.MORTAR_AND_PESTLE.get(), MortarAndPestleBlockEntity::tick);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof MortarAndPestleBlockEntity mortar) {
                mortar.drops();
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);

        if (!stack.isEmpty() && !player.isShiftKeyDown()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (!(be instanceof MortarAndPestleBlockEntity mortar)) {
                return InteractionResult.PASS;
            }

            if (level.isClientSide) {
                return mortar.canInsertOne(stack) ? InteractionResult.SUCCESS : InteractionResult.PASS;
            }

            if (!mortar.canInsertOne(stack)) {
                return InteractionResult.PASS;
            }

            boolean inserted = mortar.insertOneIntoNextEmpty(stack);
            if (!inserted) {
                return InteractionResult.PASS;
            }

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.8F, 1.2F);
            return InteractionResult.CONSUME;
        }

        if (!player.getMainHandItem().isEmpty()) {
            return InteractionResult.PASS;
        }

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof MortarAndPestleBlockEntity mortar)) {
            return InteractionResult.PASS;
        }

        if (!player.isShiftKeyDown()) {
            if (level.isClientSide) {
                return (mortar.hasOutput() || mortar.hasAnyInputs()) ? InteractionResult.SUCCESS : InteractionResult.PASS;
            }

            ItemStack extracted = mortar.hasOutput() ? mortar.takeOutputOne() : mortar.extractOneInput();
            if (extracted.isEmpty()) {
                return InteractionResult.PASS;
            }

            if (!player.getInventory().add(extracted)) {
                ItemEntity entity = new ItemEntity(
                        level,
                        pos.getX() + 0.5,
                        pos.getY() + 1.0,
                        pos.getZ() + 0.5,
                        extracted
                );
                level.addFreshEntity(entity);
            }

            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.8F, 1.2F);
            return InteractionResult.CONSUME;
        }

        if (level.isClientSide) {
            return mortar.canStartSpin() ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }

        if (mortar.startSpin()) {
            level.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }
}
