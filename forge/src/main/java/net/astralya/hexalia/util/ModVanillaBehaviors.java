package net.astralya.hexalia.forge.util;

import net.astralya.hexalia.block.custom.MorphoraBlock;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.item.custom.MutavisItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayerFactory;

public final class ModVanillaBehaviors {

    private ModVanillaBehaviors() {
    }

    public static void register() {
        registerDispenserBehaviors();
    }

    private static void registerDispenserBehaviors() {
        DispenserBlock.registerBehavior(ModItems.MUTAVIS.get(), new DefaultDispenseItemBehavior() {
            @Override
            protected ItemStack execute(BlockSource source, ItemStack stack) {
                ServerLevel level = source.getLevel();

                Direction facing = source.getBlockState().getValue(DispenserBlock.FACING);
                BlockPos targetPos = source.getPos().relative(facing);
                BlockState targetState = level.getBlockState(targetPos);

                if (stack.getItem() instanceof MutavisItem mutavisItem) {
                    if (mutavisItem.tryMutate(level, targetPos, stack, null)) {
                        return stack;
                    }
                }

                if (targetState.getBlock() instanceof MorphoraBlock morphoraBlock) {
                    if (morphoraBlock.tryActivateWithMutavis(level, targetPos, stack, null)) {
                        return stack;
                    }
                }

                return super.execute(source, stack);
            }
        });

        registerUseOnBehavior(ModItems.CELESTIAL_CRYSTAL.get());
        registerUseOnBehavior(ModItems.HEX_FOCUS.get());
        registerUseOnBehavior(ModItems.LOTUS_BLOSSOM.get());
        registerUseOnBehavior(Items.FLINT_AND_STEEL);
        registerUseOnBehavior(Items.FIRE_CHARGE);
    }

    private static void registerUseOnBehavior(ItemLike item) {
        DispenserBlock.registerBehavior(item, new OptionalDispenseItemBehavior() {
            @Override
            protected ItemStack execute(BlockSource source, ItemStack stack) {
                ServerLevel level = source.getLevel();

                Direction facing = source.getBlockState().getValue(DispenserBlock.FACING);
                BlockPos dispenserPos = source.getPos();
                BlockPos targetPos = dispenserPos.relative(facing);

                Player fakePlayer = FakePlayerFactory.getMinecraft(level);
                fakePlayer.setPos(dispenserPos.getX() + 0.5D, dispenserPos.getY() + 0.5D, dispenserPos.getZ() + 0.5D);
                fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, stack);

                Direction hitFace = facing.getOpposite();
                Vec3 hitLocation = Vec3.atCenterOf(targetPos)
                        .add(hitFace.getStepX() * 0.5D, hitFace.getStepY() * 0.5D, hitFace.getStepZ() * 0.5D);

                BlockHitResult hit = new BlockHitResult(hitLocation, hitFace, targetPos, false);

                BlockState targetState = level.getBlockState(targetPos);
                InteractionResult blockResult = targetState.use(level, fakePlayer, InteractionHand.MAIN_HAND, hit);

                boolean success = blockResult.consumesAction();

                if (!success && blockResult == InteractionResult.PASS) {
                    UseOnContext context = new UseOnContext(level, fakePlayer, InteractionHand.MAIN_HAND, stack, hit);
                    InteractionResult itemResult = stack.useOn(context);
                    success = itemResult.consumesAction();
                }

                setSuccess(success);
                return stack;
            }
        });
    }
}
