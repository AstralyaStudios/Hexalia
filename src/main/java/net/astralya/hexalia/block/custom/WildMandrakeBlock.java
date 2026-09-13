package net.astralya.hexalia.block.custom;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class WildMandrakeBlock extends FlowerBlock implements BonemealableBlock {
    public WildMandrakeBlock(Supplier<MobEffect> effect, int duration, Properties properties) { super(effect, duration, properties); }
    @Override public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean client) { return true; }
    @Override public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return random.nextFloat() < 0.8F; }
    @Override public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) { WildCropSpreading.spread(level, random, pos, state); }
    @Override public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.is(Items.SHEARS)) return InteractionResult.PASS;
        if (!level.isClientSide) {
            popResource(level, pos, new ItemStack(this));
            level.removeBlock(pos, false);
            stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
