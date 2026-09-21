package net.astralya.hexalia.mixin;

import net.astralya.hexalia.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SugarCaneBlock.class)
public class SugarCaneBlockMixin {
    @Inject(at = @At("TAIL"), method = "canPlaceAt", cancellable = true)
    private void canPlaceSugarCaneOnInfusedFarmland(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState blockState = world.getBlockState(pos.down());
        if (blockState.isOf(ModBlocks.INFUSED_FARMLAND)) {
            cir.setReturnValue(true);
        }
    }
}
