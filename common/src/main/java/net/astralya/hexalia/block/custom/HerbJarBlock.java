package net.astralya.hexalia.block.custom;

import java.util.List;
import net.astralya.hexalia.block.entity.custom.HerbJarBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
    private static final String DATA_KEY = "HerbJar";
    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(4, 0, 4, 12, 11, 12), Block.box(5, 11, 5, 11, 12, 11));

    public HerbJarBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(HAS_CONTENTS, false));
    }

    @Override public RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }
    @Override public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
    @Override public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
    @Override public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new HerbJarBlockEntity(pos, state); }
    @Override public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HAS_CONTENTS);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        CompoundTag data = stack.getTagElement(DATA_KEY);
        if (data == null) return;
        int count = 0;
        ItemStack stored = ItemStack.EMPTY;
        net.minecraft.core.NonNullList<ItemStack> items = net.minecraft.core.NonNullList.withSize(HerbJarBlockEntity.SIZE, ItemStack.EMPTY);
        net.minecraft.world.ContainerHelper.loadAllItems(data, items);
        for (ItemStack item : items) {
            if (!item.isEmpty()) {
                if (stored.isEmpty()) stored = item;
                count += item.getCount();
            }
        }
        if (!stored.isEmpty()) tooltip.add(Component.translatable("tooltip.hexalia.herb_jar.stores", stored.getHoverName(), count)
                .withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof HerbJarBlockEntity jar)) return InteractionResult.PASS;
        ItemStack held = player.getItemInHand(hand);
        if (!held.isEmpty()) {
            if (!jar.canInsert(held)) return InteractionResult.PASS;
            if (level.isClientSide) return InteractionResult.SUCCESS;
            int inserted = jar.insert(held);
            if (inserted == 0) return InteractionResult.PASS;
            if (!player.isCreative()) held.shrink(inserted);
            level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            return InteractionResult.CONSUME;
        }
        if (jar.isEmpty()) return InteractionResult.PASS;
        if (level.isClientSide) return InteractionResult.SUCCESS;
        ItemStack extracted = jar.extractLastStack();
        if (extracted.isEmpty()) return InteractionResult.PASS;
        if (!player.getInventory().add(extracted)) {
            Containers.dropItemStack(level, player.getX(), player.getY(), player.getZ(), extracted);
        }
        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        return InteractionResult.CONSUME;
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state,
                              @Nullable BlockEntity blockEntity, ItemStack tool) {
        if (player.isCreative()) return;
        if (level instanceof ServerLevel && blockEntity instanceof HerbJarBlockEntity jar) {
            ItemStack drop = new ItemStack(this);
            if (!jar.isEmpty()) drop.addTagElement(DATA_KEY, jar.createData());
            popResource(level, pos, drop);
            return;
        }
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof HerbJarBlockEntity jar) {
            CompoundTag data = stack.getTagElement(DATA_KEY);
            if (data != null) jar.restoreData(data);
        }
    }
}
