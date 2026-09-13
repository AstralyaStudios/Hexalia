package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.Configuration;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.block.entity.custom.RitualBrazierBlockEntity;
import net.astralya.hexalia.block.entity.custom.RitualTableBlockEntity;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.recipe.RitualTableRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RitualTableBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private static final VoxelShape SHAPE = Shapes.or(
            Shapes.box(0.1875, 0.0, 0.1875, 0.8125, 0.125, 0.8125),
            Shapes.box(0.25, 0.125, 0.25, 0.75, 0.625, 0.75),
            Shapes.box(0.1875, 0.625, 0.1875, 0.8125, 0.6875, 0.8125),
            Shapes.box(0.125, 0.6875, 0.125, 0.875, 0.8125, 0.875)
    );

    private static final class Match {
        final RitualTableRecipe recipe;
        final List<RitualBrazierBlockEntity> usedBraziers;

        Match(RitualTableRecipe recipe, List<RitualBrazierBlockEntity> usedBraziers) {
            this.recipe = recipe;
            this.usedBraziers = usedBraziers;
        }
    }

    public RitualTableBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RitualTableBlockEntity(pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof RitualTableBlockEntity tableBE) {
                Containers.dropContents(level, pos, tableBE);
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }
        super.onRemove(state, level, pos, newState, moved);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }

        if (!(level.getBlockEntity(pos) instanceof RitualTableBlockEntity tableBE)) {
            return InteractionResult.PASS;
        }

        ItemStack stack = player.getItemInHand(hand);

        if (stack.is(ModItems.HEX_FOCUS.get()) && tryStartRitual(level, pos, player, tableBE)) {
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        if (tableBE.isEmpty() && !stack.isEmpty() && !stack.is(ModItems.HEX_FOCUS.get())) {
            addItemToBlock(stack, level, pos, player, tableBE);
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        if (stack.isEmpty() && !tableBE.getItem(0).isEmpty()) {
            removeItemFromBlock(level, pos, player, tableBE);
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
    }

    private boolean tryStartRitual(Level level, BlockPos pos, Player player, RitualTableBlockEntity tableBE) {
        ItemStack tableItem = tableBE.getItem(0);
        if (tableItem.isEmpty()) {
            fail(level, pos, player, "message.hexalia.ritual.missing_ingredients");
            return true;
        }

        Match match = findMatch(level, pos, tableItem, tableBE);
        if (match == null) {
            fail(level, pos, player, "message.hexalia.ritual.wrong_recipe");
            return true;
        }

        for (RitualBrazierBlockEntity brazier : match.usedBraziers) {
            BlockState brazierState = level.getBlockState(brazier.getBlockPos());
            if (!brazierState.hasProperty(RitualBrazierBlock.SALTED) || !brazierState.getValue(RitualBrazierBlock.SALTED)) {
                fail(level, pos, player, "message.hexalia.ritual.missing_salt");
                return true;
            }
        }

        int cropRequirement = Configuration.NATURES_RITUAL_CROP_REQUIREMENT.get();
        List<BlockPos> grownCrops = cropRequirement == 0
                ? List.of()
                : findFullyGrownCrops(level, pos, cropRequirement, 8);
        if (grownCrops.size() < cropRequirement) {
            fail(level, pos, player, "message.hexalia.ritual.invalid_crops");
            return true;
        }

        int duration = match.usedBraziers.size() * 40;

        tableBE.startTransformation(
                match.recipe.itemResult(),
                duration,
                match.usedBraziers,
                match.recipe.getId(),
                match.recipe.requiresSoul(),
                player
        );
        tableBE.setGrownCropPositions(grownCrops);

        play(level, pos);
        puff(level, pos, ParticleTypes.POOF, 5, 10);
        return true;
    }

    @Nullable
    private Match findMatch(Level level, BlockPos tablePos, ItemStack tableItem, RitualTableBlockEntity tableBE) {
        List<RitualTableRecipe> candidates = level.getRecipeManager().getAllRecipesFor(RitualTableRecipe.Type.INSTANCE);

        for (RitualTableRecipe recipe : candidates) {
            if (!recipe.centerIngredient().test(tableItem)) {
                continue;
            }

            List<RitualBrazierBlockEntity> available = new ArrayList<>();
            for (int dx = -8; dx <= 8; dx++) {
                for (int dz = -8; dz <= 8; dz++) {
                    if (dx * dx + dz * dz > 64) continue;
                    BlockPos brazierPos = tablePos.offset(dx, 0, dz);
                    if (level.getBlockEntity(brazierPos) instanceof RitualBrazierBlockEntity brazier
                            && !brazier.getStoredItem().isEmpty()) {
                        available.add(brazier);
                    }
                }
            }
            available.sort(Comparator
                    .comparingDouble((RitualBrazierBlockEntity brazier) -> tablePos.distSqr(brazier.getBlockPos()))
                    .thenComparingLong(brazier -> brazier.getBlockPos().asLong()));

            List<RitualBrazierBlockEntity> pool = new ArrayList<>(available);
            List<RitualBrazierBlockEntity> used = new ArrayList<>();

            boolean ok = true;
            for (Ingredient needed : recipe.offerings()) {
                int foundIndex = -1;

                for (int j = 0; j < pool.size(); j++) {
                    if (needed.test(pool.get(j).getStoredItem())) {
                        foundIndex = j;
                        break;
                    }
                }

                if (foundIndex == -1) {
                    ok = false;
                    break;
                }

                used.add(pool.remove(foundIndex));
            }

            if (ok) {
                return new Match(recipe, used);
            }
        }

        return null;
    }

    private List<BlockPos> findFullyGrownCrops(Level level, BlockPos center, int required, int radius) {
        List<BlockPos> found = new ArrayList<>();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                BlockPos checkPos = center.offset(dx, 0, dz);
                BlockState state = level.getBlockState(checkPos);

                if (state.getBlock() instanceof CropBlock crop && crop.isMaxAge(state)) {
                    found.add(checkPos);
                    if (found.size() >= required) {
                        return found;
                    }
                }
            }
        }

        return found;
    }

    private void addItemToBlock(ItemStack stack, Level level, BlockPos pos, Player player, RitualTableBlockEntity be) {
        if (level.isClientSide()) {
            return;
        }

        ItemStack single = stack.copy();
        single.setCount(1);
        be.setItem(0, single);

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        play(level, pos);
    }

    private void removeItemFromBlock(Level level, BlockPos pos, Player player, RitualTableBlockEntity be) {
        if (level.isClientSide()) {
            return;
        }

        ItemStack onTable = be.getItem(0).copy();
        if (!player.getAbilities().instabuild) {
            if (!player.getInventory().add(onTable)) {
                Containers.dropItemStack(level, pos.getX(), pos.getY() + 1.0D, pos.getZ(), onTable);
            }
        }

        be.clearContent();
        play(level, pos);
    }

    private void fail(Level level, BlockPos pos, Player player, String key) {
        puff(level, pos, ParticleTypes.SMOKE, 8, 12);
        if (!level.isClientSide()) {
            player.displayClientMessage(Component.translatable(key), true);
        }
        level.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 0.4F, 0.6F);
    }

    private void puff(Level level, BlockPos pos, SimpleParticleType type, int min, int max) {
        if (level instanceof ServerLevel server) {
            int count = ThreadLocalRandom.current().nextInt(min, max);
            for (int i = 0; i < count; i++) {
                double x = pos.getX() + 0.5D + ThreadLocalRandom.current().nextDouble(-0.5D, 0.5D);
                double y = pos.getY() + 1.0D + ThreadLocalRandom.current().nextDouble(0.0D, 0.5D);
                double z = pos.getZ() + 0.5D + ThreadLocalRandom.current().nextDouble(-0.5D, 0.5D);
                server.sendParticles(type, x, y, z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    private void play(Level level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundSource.BLOCKS, 0.8F, 0.5F);
        level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.8F, 0.5F);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide() && type == ModBlockEntityTypes.RITUAL_TABLE.get()
                ? (lvl, p, st, be) -> RitualTableBlockEntity.serverTick(lvl, p, st, (RitualTableBlockEntity) be)
                : null;
    }
}
