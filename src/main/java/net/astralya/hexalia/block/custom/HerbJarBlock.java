package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.block.entity.custom.HerbJarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HerbJarBlock extends BaseEntityBlock {
    public static final String STORAGE_KEY = "HerbJar";
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty HAS_CONTENTS = BooleanProperty.create("has_contents");
    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(4, 0, 4, 12, 11, 12),
            Block.box(5, 11, 5, 11, 12, 11));

    public HerbJarBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HAS_CONTENTS, false));
    }

    @Override public RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }
    @Override public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    @Override public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    @Override public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HerbJarBlockEntity(pos, state);
    }
    @Override public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HAS_CONTENTS);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof HerbJarBlockEntity jar)) return InteractionResult.PASS;
        ItemStack held = player.getItemInHand(hand);
        if (!held.isEmpty()) {
            if (!jar.canInsert(held)) return InteractionResult.PASS;
            if (!level.isClientSide && jar.insertOne(held)) {
                if (!player.isCreative()) held.shrink(1);
                level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1, 1);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        if (jar.isEmpty()) return InteractionResult.PASS;
        if (!level.isClientSide) {
            ItemStack extracted = jar.extractOne();
            if (!extracted.isEmpty() && !player.getInventory().add(extracted)) {
                player.drop(extracted, false);
            }
            level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1, 1);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof HerbJarBlockEntity jar) {
            CompoundTag storage = getStorageNbt(stack);
            if (storage != null) jar.restoreStorageNbt(storage);
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        ItemStack drop = new ItemStack(this);
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof HerbJarBlockEntity jar && !jar.isEmpty()) {
            putStorageNbt(drop, jar.createStorageNbt());
        }
        return List.of(drop);
    }

    public static void putStorageNbt(ItemStack jar, CompoundTag storage) {
        jar.getOrCreateTag().put(STORAGE_KEY, storage.copy());
    }

    public static @Nullable CompoundTag getStorageNbt(ItemStack jar) {
        CompoundTag tag = jar.getTag();
        return tag != null && tag.contains(STORAGE_KEY, Tag.TAG_COMPOUND)
                ? tag.getCompound(STORAGE_KEY) : null;
    }

    public static StoredSummary summarize(CompoundTag storage) {
        net.minecraft.core.NonNullList<ItemStack> items =
                net.minecraft.core.NonNullList.withSize(HerbJarBlockEntity.SIZE, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(storage, items);
        ItemStack identity = ItemStack.EMPTY;
        int count = 0;
        for (ItemStack stack : items) {
            if (stack.isEmpty()) continue;
            if (identity.isEmpty()) identity = stack.copyWithCount(1);
            if (ItemStack.isSameItemSameTags(identity, stack)) count += Math.min(1, stack.getCount());
            if (count == HerbJarBlockEntity.SIZE) break;
        }
        return new StoredSummary(identity, count);
    }

    public record StoredSummary(ItemStack stack, int count) {}
}
