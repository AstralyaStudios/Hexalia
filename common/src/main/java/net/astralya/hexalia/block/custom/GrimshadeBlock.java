package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.block.entity.custom.GrimshadeBlockEntity;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class GrimshadeBlock extends EnchantedPlantBlock implements EntityBlock {

    public GrimshadeBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return super.mayPlaceOn(state, level, pos)
                || state.is(Blocks.NETHERRACK)
                || state.is(Blocks.SOUL_SAND)
                || state.is(Blocks.SOUL_SOIL);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);
        if (held.getItem() != ModItems.HEX_FOCUS.get()) return InteractionResult.PASS;
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof GrimshadeBlockEntity grim && !grim.isActive()) {
                playActivationEffects((ServerLevel) level, pos);
                doOneShotConversions((ServerLevel) level, pos);
                grim.activate();
            }
        }
        return InteractionResult.SUCCESS;
    }

    private void playActivationEffects(ServerLevel level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.WITHER_AMBIENT, SoundSource.BLOCKS, 1.0f, 1.0f);
        level.playSound(null, pos, SoundEvents.SOUL_ESCAPE,   SoundSource.BLOCKS, 0.8f, 1.0f);
        level.sendParticles(ParticleTypes.SOUL,  pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5, 24, 0.35, 0.35, 0.35, 0.02);
        level.sendParticles(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5, 16, 0.35, 0.35, 0.35, 0.01);
    }

    private void doOneShotConversions(ServerLevel level, BlockPos pos) {
        AABB area = new AABB(pos).inflate(2.5);

        for (Entity e : level.getEntitiesOfClass(Skeleton.class, area)) {
            WitherSkeleton wither = EntityType.WITHER_SKELETON.create(level);
            wither.moveTo(e.getX(), e.getY(), e.getZ(), e.getYRot(), e.getXRot());
            e.discard();
            level.addFreshEntity(wither);
        }

        final int[] skulls = {0};
        for (ItemEntity it : level.getEntitiesOfClass(ItemEntity.class, area,
                ent -> ent.getItem().is(net.minecraft.world.item.Items.SKELETON_SKULL) && skulls[0] < 3)) {
            it.setItem(new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.WITHER_SKELETON_SKULL, it.getItem().getCount()));
            skulls[0]++;
        }

        int minX = (int) Math.floor(area.minX);
        int minY = (int) Math.floor(area.minY);
        int minZ = (int) Math.floor(area.minZ);
        int maxX = (int) Math.floor(area.maxX);
        int maxY = (int) Math.floor(area.maxY);
        int maxZ = (int) Math.floor(area.maxZ);

        for (BlockPos p : BlockPos.betweenClosed(minX, minY, minZ, maxX, maxY, maxZ)) {
            if (skulls[0] >= 3) break;
            BlockState s = level.getBlockState(p);

            if (s.is(Blocks.SKELETON_SKULL)) {
                BlockState t = copyCommonProperties(s, Blocks.WITHER_SKELETON_SKULL.defaultBlockState());
                level.setBlock(p, t, 3);
                skulls[0]++;
            } else if (s.is(Blocks.SKELETON_WALL_SKULL)) {
                BlockState t = Blocks.WITHER_SKELETON_WALL_SKULL.defaultBlockState();
                t = copyCommonProperties(s, t);
                if (s.hasProperty(WallSkullBlock.FACING) && t.hasProperty(WallSkullBlock.FACING)) {
                    t = t.setValue(WallSkullBlock.FACING, s.getValue(WallSkullBlock.FACING));
                }
                level.setBlock(p, t, 3);
                skulls[0]++;
            } else if (s.is(ModBlocks.CANDLE_SKULL.get())) {
                BlockState t = copyCommonProperties(s, ModBlocks.WITHER_CANDLE_SKULL.get().defaultBlockState());
                level.setBlock(p, t, 3);
                skulls[0]++;
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static BlockState copyCommonProperties(BlockState from, BlockState to) {
        for (var prop : from.getProperties()) {
            if (to.hasProperty(prop)) {
                to = to.setValue((net.minecraft.world.level.block.state.properties.Property) prop, from.getValue(prop));
            }
        }
        return to;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return super.getStateForPlacement(ctx);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GrimshadeBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : (lvl, p, s, be) -> {
            if (be instanceof GrimshadeBlockEntity g) {
                GrimshadeBlockEntity.tick(lvl, p, s, g);
            }
        };
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide) return;
        if (!(entity instanceof net.minecraft.world.entity.LivingEntity) || entity instanceof Player) return;
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof GrimshadeBlockEntity g && g.isActive()) {
            g.applyCollisionPing((net.minecraft.world.entity.LivingEntity) entity);
        }
    }
}
