package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.block.entity.custom.HerbJarBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HerbJarBlock extends BlockWithEntity {
    public static final String STORAGE_KEY = "HerbJar";
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty HAS_CONTENTS = BooleanProperty.of("has_contents");
    private static final VoxelShape SHAPE = VoxelShapes.union(
            Block.createCuboidShape(4, 0, 4, 12, 11, 12),
            Block.createCuboidShape(5, 11, 5, 11, 12, 11));

    public HerbJarBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(HAS_CONTENTS, false));
    }

    @Override public BlockRenderType getRenderType(BlockState state) { return BlockRenderType.MODEL; }
    @Override public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
    @Override public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
    @Override public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new HerbJarBlockEntity(pos, state);
    }
    @Override public BlockState getPlacementState(ItemPlacementContext context) {
        return getDefaultState().with(FACING, context.getHorizontalPlayerFacing().getOpposite());
    }
    @Override protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, HAS_CONTENTS);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player,
                              Hand hand, BlockHitResult hit) {
        if (!(world.getBlockEntity(pos) instanceof HerbJarBlockEntity jar)) return ActionResult.PASS;
        ItemStack held = player.getStackInHand(hand);
        if (!held.isEmpty()) {
            if (!jar.canInsert(held)) return ActionResult.PASS;
            if (!world.isClient && jar.insertOne(held)) {
                if (!player.getAbilities().creativeMode) held.decrement(1);
                world.playSound(null, pos, SoundEvents.ENTITY_ITEM_FRAME_ADD_ITEM, SoundCategory.BLOCKS, 1, 1);
            }
            return ActionResult.success(world.isClient);
        }
        if (jar.isEmpty()) return ActionResult.PASS;
        if (!world.isClient) {
            ItemStack extracted = jar.extractOne();
            if (!extracted.isEmpty() && !player.giveItemStack(extracted)) {
                player.dropItem(extracted, false);
            }
            world.playSound(null, pos, SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM, SoundCategory.BLOCKS, 1, 1);
        }
        return ActionResult.success(world.isClient);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onPlaced(world, pos, state, placer, stack);
        if (!world.isClient && world.getBlockEntity(pos) instanceof HerbJarBlockEntity jar) {
            NbtCompound storage = getStorageNbt(stack);
            if (storage != null) jar.restoreStorageNbt(storage);
        }
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        ItemStack drop = new ItemStack(this);
        BlockEntity blockEntity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);
        if (blockEntity instanceof HerbJarBlockEntity jar && !jar.isEmpty()) {
            putStorageNbt(drop, jar.createStorageNbt());
        }
        return List.of(drop);
    }

    public static void putStorageNbt(ItemStack jar, NbtCompound storage) {
        jar.getOrCreateNbt().put(STORAGE_KEY, storage.copy());
    }

    public static @Nullable NbtCompound getStorageNbt(ItemStack jar) {
        NbtCompound nbt = jar.getNbt();
        return nbt != null && nbt.contains(STORAGE_KEY, NbtCompound.COMPOUND_TYPE)
                ? nbt.getCompound(STORAGE_KEY) : null;
    }

    public static StoredSummary summarize(NbtCompound storage) {
        net.minecraft.util.collection.DefaultedList<ItemStack> items =
                net.minecraft.util.collection.DefaultedList.ofSize(HerbJarBlockEntity.SIZE, ItemStack.EMPTY);
        Inventories.readNbt(storage, items);
        ItemStack identity = ItemStack.EMPTY;
        int count = 0;
        for (ItemStack stack : items) {
            if (stack.isEmpty()) continue;
            if (identity.isEmpty()) identity = stack.copyWithCount(1);
            if (ItemStack.canCombine(identity, stack)) count += Math.min(1, stack.getCount());
            if (count == HerbJarBlockEntity.SIZE) break;
        }
        return new StoredSummary(identity, count);
    }

    public record StoredSummary(ItemStack stack, int count) {}
}
