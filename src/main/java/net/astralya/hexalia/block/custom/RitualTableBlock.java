package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.Configuration;
import net.astralya.hexalia.block.entity.ModBlockEntityTypes;
import net.astralya.hexalia.block.entity.custom.RitualBrazierBlockEntity;
import net.astralya.hexalia.block.entity.custom.RitualTableBlockEntity;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.recipe.ModRecipes;
import net.astralya.hexalia.recipe.RitualTableRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.CropBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RitualTableBlock extends BlockWithEntity {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    private static final VoxelShape SHAPE = VoxelShapes.union(
            VoxelShapes.cuboid(0.1875, 0.0, 0.1875, 0.8125, 0.125, 0.8125),
            VoxelShapes.cuboid(0.25, 0.125, 0.25, 0.75, 0.625, 0.75),
            VoxelShapes.cuboid(0.1875, 0.625, 0.1875, 0.8125, 0.6875, 0.8125),
            VoxelShapes.cuboid(0.125, 0.6875, 0.125, 0.875, 0.8125, 0.875)
    );

    public RitualTableBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RitualTableBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, ModBlockEntityTypes.RITUAL_TABLE, RitualTableBlockEntity::serverTick);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof RitualTableBlockEntity table) {
                ItemScatterer.spawn(world, pos, table);
                world.updateComparators(pos, this);
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (hand != Hand.MAIN_HAND) {
            return ActionResult.PASS;
        }

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof RitualTableBlockEntity tableBE)) {
            return ActionResult.PASS;
        }

        ItemStack stack = player.getStackInHand(hand);

        if (stack.isOf(ModItems.HEX_FOCUS) && tryStartRitual(world, pos, player, tableBE)) {
            return ActionResult.SUCCESS;
        }

        if (tableBE.isEmpty() && !stack.isEmpty() && !stack.isOf(ModItems.HEX_FOCUS)) {
            addItemToBlock(stack, world, pos, player, tableBE);
            return ActionResult.SUCCESS;
        }

        if (stack.isEmpty() && !tableBE.getStack(0).isEmpty()) {
            removeItemFromBlock(world, pos, player, tableBE);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    private boolean tryStartRitual(World world, BlockPos pos, PlayerEntity player, RitualTableBlockEntity tableBE) {
        ItemStack tableItem = tableBE.getStack(0);
        if (tableItem.isEmpty()) {
            fail(world, pos, player, "message.hexalia.ritual.missing_ingredients");
            return true;
        }

        MatchResult match = findMatchingRecipe(world, pos, tableItem);
        if (match == null) {
            fail(world, pos, player, "message.hexalia.ritual.wrong_recipe");
            return true;
        }

        for (RitualBrazierBlockEntity brazier : match.usedBraziers) {
            BlockState brazierState = world.getBlockState(brazier.getPos());
            if (!brazierState.contains(RitualBrazierBlock.SALTED) || !brazierState.get(RitualBrazierBlock.SALTED)) {
                fail(world, pos, player, "message.hexalia.ritual.missing_salt");
                return true;
            }
        }

        int cropRequirement = Configuration.NATURES_RITUAL_CROP_REQUIREMENT.get();
        List<BlockPos> grownCrops = cropRequirement == 0
                ? List.of()
                : findFullyGrownCrops(world, pos, cropRequirement, 8);
        if (grownCrops.size() < cropRequirement) {
            fail(world, pos, player, "message.hexalia.ritual.invalid_crops");
            return true;
        }

        int duration = match.usedBraziers.size() * 40;
        tableBE.startTransformation(match.recipe.itemResult(), duration, match.usedBraziers,
                match.recipe.getId(), match.recipe.requiresSoul(), player);
        tableBE.setGrownCropPositions(grownCrops);

        play(world, pos);
        puff(world, pos, ParticleTypes.POOF, 5, 10);
        return true;
    }

    private @Nullable MatchResult findMatchingRecipe(World world, BlockPos tablePos, ItemStack tableItem) {
        SimpleInventory inventory = new SimpleInventory(RitualTableRecipe.INPUT_SLOTS);
        inventory.setStack(0, tableItem.copy());

        List<RitualTableRecipe> candidates = world.getRecipeManager().getAllMatches(ModRecipes.RITUAL_TABLE_TYPE, inventory, world);

        for (RitualTableRecipe recipe : candidates) {
            if (!recipe.centerIngredient().test(tableItem)) {
                continue;
            }

            List<RitualBrazierBlockEntity> availableBraziers = new ArrayList<>();
            for (int dx = -8; dx <= 8; dx++) {
                for (int dz = -8; dz <= 8; dz++) {
                    if (dx * dx + dz * dz > 64) continue;
                    BlockEntity blockEntity = world.getBlockEntity(tablePos.add(dx, 0, dz));
                    if (blockEntity instanceof RitualBrazierBlockEntity brazier && !brazier.getStoredItem().isEmpty()) {
                        availableBraziers.add(brazier);
                    }
                }
            }
            availableBraziers.sort(Comparator
                    .comparingDouble((RitualBrazierBlockEntity brazier) -> tablePos.getSquaredDistance(brazier.getPos()))
                    .thenComparingLong(brazier -> brazier.getPos().asLong()));

            List<RitualBrazierBlockEntity> pool = new ArrayList<>(availableBraziers);
            List<RitualBrazierBlockEntity> used = new ArrayList<>();
            boolean matches = true;

            for (Ingredient ingredient : recipe.offerings()) {
                int index = -1;
                for (int i = 0; i < pool.size(); i++) {
                    if (ingredient.test(pool.get(i).getStoredItem())) {
                        index = i;
                        break;
                    }
                }

                if (index == -1) {
                    matches = false;
                    break;
                }

                used.add(pool.remove(index));
            }

            if (matches) {
                return new MatchResult(recipe, used);
            }
        }

        return null;
    }

    private List<BlockPos> findFullyGrownCrops(World world, BlockPos center, int requiredCount, int radius) {
        List<BlockPos> found = new ArrayList<>();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                BlockPos checkPos = center.add(dx, 0, dz);
                BlockState state = world.getBlockState(checkPos);
                if (state.getBlock() instanceof CropBlock crop && crop.isMature(state)) {
                    found.add(checkPos);
                    if (found.size() >= requiredCount) {
                        return found;
                    }
                }
            }
        }

        return found;
    }

    private void addItemToBlock(ItemStack stack, World world, BlockPos pos, PlayerEntity player, RitualTableBlockEntity table) {
        ItemStack single = stack.copy();
        single.setCount(1);
        table.setStack(0, single);

        if (!player.getAbilities().creativeMode) {
            stack.decrement(1);
        }

        play(world, pos);
    }

    private void removeItemFromBlock(World world, BlockPos pos, PlayerEntity player, RitualTableBlockEntity table) {
        ItemStack onTable = table.getStack(0);

        if (!player.getAbilities().creativeMode) {
            player.setStackInHand(Hand.MAIN_HAND, onTable);
        }

        table.clear();
        play(world, pos);
    }

    private void fail(World world, BlockPos pos, PlayerEntity player, String key) {
        puff(world, pos, ParticleTypes.SMOKE, 8, 12);
        if (!world.isClient) {
            player.sendMessage(Text.translatable(key), true);
        }
        world.playSound(null, pos, SoundEvents.BLOCK_CANDLE_EXTINGUISH, SoundCategory.BLOCKS, 0.4f, 0.6f);
    }

    private void puff(World world, BlockPos pos, DefaultParticleType type, int min, int max) {
        if (world instanceof ServerWorld serverWorld) {
            int count = ThreadLocalRandom.current().nextInt(min, max);
            for (int i = 0; i < count; i++) {
                double x = pos.getX() + 0.5 + ThreadLocalRandom.current().nextDouble(-0.5, 0.5);
                double y = pos.getY() + 1.0 + ThreadLocalRandom.current().nextDouble(0.0, 0.5);
                double z = pos.getZ() + 0.5 + ThreadLocalRandom.current().nextDouble(-0.5, 0.5);
                serverWorld.spawnParticles(type, x, y, z, 1, 0.0, 0.0, 0.0, 0.0);
            }
        }
    }

    private void play(World world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.BLOCKS, 0.8f, 0.5f);
        world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.8f, 0.5f);
    }

    private static final class MatchResult {
        final RitualTableRecipe recipe;
        final List<RitualBrazierBlockEntity> usedBraziers;

        MatchResult(RitualTableRecipe recipe, List<RitualBrazierBlockEntity> usedBraziers) {
            this.recipe = recipe;
            this.usedBraziers = usedBraziers;
        }
    }
}
