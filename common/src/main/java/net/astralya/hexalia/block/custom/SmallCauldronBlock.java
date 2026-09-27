package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.block.entity.custom.SmallCauldronBlockEntity;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class SmallCauldronBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public SmallCauldronBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false)
                .setValue(LIT, false));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        FluidState fluidState = level.getFluidState(pos);
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER)
                .setValue(LIT, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, LIT);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        SmallCauldronBlockEntity cauldron = getCauldron(level, pos);
        if (cauldron == null) return;
        if (cauldron.isSpoiled()) {
            spawnSpoiledEffects(level, pos, random);
            return;
        }
        if (!state.getValue(LIT)) return;
        if (cauldron.getLiquidFill01() > 0.0F && !cauldron.isCooking() && !cauldron.hasMixture()) {
            spawnLitBaseEffects(level, pos, random);
            return;
        }
        if (cauldron.isCooking()) {
            spawnCookingBrewEffects(level, pos, random);
            return;
        }
        if (!cauldron.hasMixture()) return;
        if (cauldron.isOvercooked()) {
            spawnOvercookedEffects(level, pos, random);
        } else {
            spawnFreshMixtureEffects(level, pos, random, cauldron.getMixtureBaseColor());
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SmallCauldronBlockEntity(pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.is(newState.getBlock())) {
            super.onRemove(state, level, pos, newState, movedByPiston);
            return;
        }
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof SmallCauldronBlockEntity cauldron) {
            if (level instanceof ServerLevel) {
                cauldron.dropAll(level);
            }
            level.updateNeighbourForOutputSignal(pos, this);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        SmallCauldronBlockEntity cauldron = getCauldron(level, pos);
        if (cauldron == null) return InteractionResult.PASS;

        if (!stack.isEmpty()) {
            InteractionResult result;
            result = tryStirWithLadle(stack, state, level, pos, player, hand, cauldron);
            if (result != null) return result;
        result = tryIgniteWithFireStarter(stack, state, level, pos, player, hand);
            if (result != null) return result;
            result = tryRusticBottle(stack, level, player, hand, cauldron);
            if (result != null) return result;
            result = tryLotusCleanse(stack, level, player, hand, cauldron);
            if (result != null) return result;
            result = tryWaterContainer(stack, level, player, hand, cauldron);
            if (result != null) return result;
            return tryInsertIngredient(stack, level, pos, player, cauldron);
        }

        if (level.isClientSide) {
            return cauldron.canExtractOneIngredient() ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
        ItemStack extracted = cauldron.extractOneIngredient();
        if (extracted.isEmpty()) return InteractionResult.PASS;
        if (!player.getInventory().add(extracted)) {
            ItemEntity entity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, extracted);
            level.addFreshEntity(entity);
        }
        level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.8F, 1.2F);
        return InteractionResult.CONSUME;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) return null;
        return createTickerHelper(blockEntityType, ModBlockEntityTypes.SMALL_CAULDRON.get(),
                (level1, pos, state1, blockEntity) -> blockEntity.tick(level1, pos, state1));
    }

    private @Nullable SmallCauldronBlockEntity getCauldron(Level level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        return be instanceof SmallCauldronBlockEntity cauldron ? cauldron : null;
    }

    private @Nullable InteractionResult tryStirWithLadle(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, SmallCauldronBlockEntity cauldron) {
        if (hand != InteractionHand.MAIN_HAND || !stack.is(ModItems.LADLE.get())) return null;
        if (level.isClientSide) {
            return cauldron.canStir(state, player) ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
        if (!cauldron.tryStir(state, player)) return InteractionResult.PASS;
        cauldron.triggerStirAnimation();
        level.playSound(null, pos, SoundEvents.AMBIENT_UNDERWATER_EXIT, SoundSource.BLOCKS, 0.9F, 1.05F);
        if (level instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.SPLASH, pos.getX() + 0.5, pos.getY() + 1.02, pos.getZ() + 0.5, 6, 0.10, 0.02, 0.10, 0.0);
        }
        return InteractionResult.CONSUME;
    }

    private @Nullable InteractionResult tryIgniteWithFireStarter(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand) {
        if (!isFireStarter(stack)) return null;
        if (state.getValue(WATERLOGGED) || state.getValue(LIT)) return InteractionResult.PASS;
        if (level.isClientSide) return InteractionResult.SUCCESS;
        level.setBlock(pos, state.setValue(LIT, true), 3);
        consumeFireStarter(stack, player, hand);
        level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
        return InteractionResult.CONSUME;
    }

    private boolean isFireStarter(ItemStack stack) {
        return stack.is(Items.FLINT_AND_STEEL)
                || stack.is(Items.FIRE_CHARGE);
    }

    private void consumeFireStarter(ItemStack stack, Player player, InteractionHand hand) {
        if (stack.is(Items.FIRE_CHARGE)) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return;
        }

        stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND));
    }

    private @Nullable InteractionResult tryRusticBottle(ItemStack stack, Level level, Player player, InteractionHand hand, SmallCauldronBlockEntity cauldron) {
        if (!cauldron.isRusticBottle(stack)) return null;
        if (level.isClientSide) {
            return cauldron.canScoopMixtureWithRusticBottle() ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
        return cauldron.tryScoopBottlePublic(player, hand, stack) ? InteractionResult.CONSUME : InteractionResult.PASS;
    }

    private @Nullable InteractionResult tryLotusCleanse(ItemStack stack, Level level, Player player, InteractionHand hand, SmallCauldronBlockEntity cauldron) {
        if (!cauldron.isLotusBlossom(stack)) return null;
        if (level.isClientSide) {
            return cauldron.canCleanseSpoiledWithLotus() ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
        return cauldron.tryCleanseSpoiledPublic(player, hand, stack) ? InteractionResult.CONSUME : InteractionResult.PASS;
    }

    private @Nullable InteractionResult tryWaterContainer(ItemStack stack, Level level, Player player, InteractionHand hand, SmallCauldronBlockEntity cauldron) {
        if (!cauldron.isWaterContainer(stack)) return null;
        if (level.isClientSide) {
            return cauldron.canUseWaterContainer(stack) ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
        return cauldron.tryFillWithWaterPublic(player, hand, stack) ? InteractionResult.CONSUME : InteractionResult.PASS;
    }

    private InteractionResult tryInsertIngredient(ItemStack stack, Level level, BlockPos pos, Player player, SmallCauldronBlockEntity cauldron) {
        if (!cauldron.canInsertOne(stack)) return InteractionResult.PASS;
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (!cauldron.insertOneIntoCauldron(stack)) return InteractionResult.PASS;
        if (!player.getAbilities().instabuild) stack.shrink(1);
        level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.8F, 1.2F);
        return InteractionResult.CONSUME;
    }

    private void spawnLitBaseEffects(Level level, BlockPos pos, RandomSource random) {
        double x = pos.getX() + 0.5;
        double y = pos.getY();
        double z = pos.getZ() + 0.5;
        if (random.nextInt(2) == 0) {
            level.addParticle(ParticleTypes.BUBBLE_POP,
                    x + (random.nextDouble() - 0.5) * 0.18, y + 1.01, z + (random.nextDouble() - 0.5) * 0.18,
                    random.nextGaussian() * 0.01, random.nextGaussian() * 0.03 + 0.03, random.nextGaussian() * 0.01);
        }
        if (random.nextInt(6) == 0) {
            level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    x + (random.nextDouble() - 0.5) * 0.18, y + 1.05, z + (random.nextDouble() - 0.5) * 0.18,
                    0.0, 0.02 + random.nextDouble() * 0.015, 0.0);
        }
    }

    private void spawnFreshMixtureEffects(Level level, BlockPos pos, RandomSource random, int rgb) {
        double x = pos.getX() + 0.5;
        double y = pos.getY();
        double z = pos.getZ() + 0.5;
        if (random.nextInt(10) == 0) {
            level.addParticle(ParticleTypes.WITCH,
                    x + (random.nextDouble() - 0.5) * 0.18, y + 1.03, z + (random.nextDouble() - 0.5) * 0.18,
                    0.0, 0.02, 0.0);
        }
        if (random.nextInt(6) != 0) return;
        float cr = ((rgb >> 16) & 0xFF) / 255.0F;
        float cg = ((rgb >> 8) & 0xFF) / 255.0F;
        float cb = (rgb & 0xFF) / 255.0F;
        level.addParticle(ParticleTypes.ENTITY_EFFECT,
                x + (random.nextDouble() - 0.5) * 0.22, y + 1.05 + random.nextDouble() * 0.08, z + (random.nextDouble() - 0.5) * 0.22,
                cr, cg, cb);
    }

    private void spawnOvercookedEffects(Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(2) != 0) return;
        double x = pos.getX() + 0.5;
        double y = pos.getY();
        double z = pos.getZ() + 0.5;
        level.addParticle(ParticleTypes.SMOKE,
                x + (random.nextDouble() - 0.5) * 0.22, y + 1.06, z + (random.nextDouble() - 0.5) * 0.22,
                0.0, 0.06 + random.nextDouble() * 0.03, 0.0);
    }

    private void spawnSpoiledEffects(Level level, BlockPos pos, RandomSource random) {
        int bursts = 1 + random.nextInt(2);
        for (int i = 0; i < bursts; i++) {
            double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 3.0;
            double y = pos.getY() + 1.0 + random.nextDouble() * 0.6;
            double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 3.0;
            level.addParticle(ParticleTypes.ENTITY_EFFECT, x, y, z, 0.0, 1.0, 0.0);
            if (random.nextInt(3) == 0) {
                level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.03 + random.nextDouble() * 0.03, 0.0);
            }
        }
    }

    private void spawnCookingBrewEffects(Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(2) != 0) return;
        double x = pos.getX() + 0.5;
        double y = pos.getY();
        double z = pos.getZ() + 0.5;
        int rolls = 1 + random.nextInt(2);
        for (int i = 0; i < rolls; i++) {
            float r = random.nextFloat();
            float g = random.nextFloat();
            float b = random.nextFloat();
            level.addParticle(ParticleTypes.ENTITY_EFFECT,
                    x + (random.nextDouble() - 0.5) * 0.24, y + 1.03 + random.nextDouble() * 0.10, z + (random.nextDouble() - 0.5) * 0.24,
                    r, g, b);
        }
    }
}
