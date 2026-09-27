package net.astralya.hexalia.mixin;

import net.astralya.hexalia.block.ModBlocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SugarCaneBlock.class)
public class SugarCaneBlockMixin {
    @Inject(at = @At("TAIL"), method = "canSurvive", cancellable = true)
    private void canPlaceSugarCaneOnInfusedFarmland(BlockState state, LevelReader world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState blockState = world.getBlockState(pos.below());
        if (blockState.is(ModBlocks.INFUSED_FARMLAND.get())) {
            cir.setReturnValue(true);
        }
    }
}

