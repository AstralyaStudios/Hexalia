package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.block.entity.custom.WindsongBlockEntity;
import net.astralya.hexalia.gameplay.enchantedplant.WindsongActivation;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.sound.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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

    public WindsongBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(ACTIVE, false));
    }

    @Override
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pGetter, BlockPos pPos) {
        return (pState.isFaceSturdy(pGetter, pPos, Direction.UP) && !pState.is(Blocks.MAGMA_BLOCK));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.getItemInHand(pHand).getItem() == ModItems.HEX_FOCUS.get()) {
            if (!pLevel.isClientSide) {
                BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
                if (blockEntity instanceof WindsongBlockEntity windsongBlockEntity && !windsongBlockEntity.isActive()) {
                    WindsongActivation.activatePlaced((net.minecraft.server.level.ServerLevel) pLevel, pPos);
                    pLevel.removeBlock(pPos, false);
                    pLevel.playSound(null, pPos, ModSoundEvents.WIND_BURST.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(ACTIVE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new WindsongBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : (level, pos, state, blockEntity) -> {
            if (blockEntity instanceof WindsongBlockEntity windsong) {
                windsong.tick(level, pos, state);
            }
        };
    }
}
