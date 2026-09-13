package net.astralya.hexalia.block.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.FlowerBlock;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class WildMandrakeBlock extends FlowerBlock implements Fertilizable {
    public WildMandrakeBlock(StatusEffect effect, int duration, Settings settings) { super(effect, duration, settings); }
    @Override public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean client) { return true; }
    @Override public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) { return random.nextFloat() < 0.8F; }
    @Override public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) { WildCropSpreading.spread(world, random, pos, state); }

    @Override public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        if (!stack.isOf(Items.SHEARS)) return ActionResult.PASS;
        if (!world.isClient) {
            dropStack(world, pos, new ItemStack(this));
            world.removeBlock(pos, false);
            stack.damage(1, player, p -> p.sendToolBreakStatus(hand));
        }
        return ActionResult.success(world.isClient);
    }
}
