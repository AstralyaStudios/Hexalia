package net.astralya.hexalia.mixin;

import net.astralya.hexalia.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SugarCaneBlock.class)
public class SugarCanePlanting {

    @Inject(at = @At("TAIL"), method = "canSurvive", cancellable = true)
    private void canPlaceSugarCaneOnInfusedFarmland(BlockState pState, LevelReader pLevel, BlockPos pPos, CallbackInfoReturnable<Boolean> cir) {
        BlockState blockState = pLevel.getBlockState(pPos.below());
        if (blockState.is(ModBlocks.INFUSED_FARMLAND.get())) {
            cir.setReturnValue(true);
        }
    }
}
