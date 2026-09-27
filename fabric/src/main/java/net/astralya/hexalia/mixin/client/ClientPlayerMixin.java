package net.astralya.hexalia.mixin.client;

import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.item.custom.RootshaperItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(MultiPlayerGameMode.class)
public class ClientPlayerMixin {

    @Inject(method = "startDestroyBlock", at = @At("HEAD"))
    private void hexalia$rootshaperModeSync(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        Minecraft client = Minecraft.getInstance();
        LocalPlayer player = client.player;
        if (player == null) {
            return;
        }

        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModItems.ROOTSHAPER.get())) {
            return;
        }

        BlockState state = client.level == null ? null : client.level.getBlockState(pos);
        if (state == null) {
            return;
        }

        int newMode = RootshaperItem.computeMode(state);
        int oldMode = RootshaperItem.getMode(stack);
        if (newMode != oldMode) {
            RootshaperItem.setMode(stack, newMode);
            RootshaperItem.playMorphSound(player.level(), player);
        }
    }
}
