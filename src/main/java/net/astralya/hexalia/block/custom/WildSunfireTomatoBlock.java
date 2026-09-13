package net.astralya.hexalia.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.server.world.ServerWorld;

import java.util.concurrent.ThreadLocalRandom;

public class WildSunfireTomatoBlock extends PlantBlock implements Fertilizable {
    protected static final VoxelShape SHAPE = Block.createCuboidShape(5.0, 0.0, 5.0, 11.0, 10.0, 11.0);
    public WildSunfireTomatoBlock(Settings settings) {
        super(settings);
    }

    @Override public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean client) { return true; }
    @Override public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) { return random.nextFloat() < 0.8F; }
    @Override public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) { WildCropSpreading.spread(world, random, pos, state); }

    @Override public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        if (!stack.isOf(Items.SHEARS)) return ActionResult.PASS;
        if (!world.isClient) {
            createFireParticles(world, pos);
            dropStack(world, pos, new ItemStack(this));
            world.removeBlock(pos, false);
            stack.damage(1, player, p -> p.sendToolBreakStatus(hand));
        }
        return ActionResult.success(world.isClient);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        createFireParticles(world, pos);
        super.onBreak(world, pos, state, player);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Vec3d vec3d = state.getModelOffset(world, pos);
        return SHAPE.offset(vec3d.x, vec3d.y, vec3d.z);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        LivingEntity livingEntity;
        if (world.isClient || world.getDifficulty() == Difficulty.PEACEFUL) {
            return;
        }
        if (entity instanceof LivingEntity && !(livingEntity = (LivingEntity)entity).isInvulnerableTo(world.getDamageSources().onFire())) {
            livingEntity.setOnFireFor(3);
        }
    }

    public static void createFireParticles(World world, BlockPos pos) {
        final double maxHorizontalOffset = 0.5;

        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < 8; i++) {
            double x = pos.getX() + 0.5;
            double y = pos.getY() + random.nextDouble(0.33);
            double z = pos.getZ() + 0.5;
            z += random.nextDouble(-maxHorizontalOffset, maxHorizontalOffset);
            x += random.nextDouble(-maxHorizontalOffset, maxHorizontalOffset);
            world.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.05D, 0.0D);
        }
    }
}
