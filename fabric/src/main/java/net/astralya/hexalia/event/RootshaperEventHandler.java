package net.astralya.hexalia.event;

import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.item.custom.RootshaperItem;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class RootshaperEventHandler {

    private static final ThreadLocal<Boolean> BREAKING = ThreadLocal.withInitial(() -> false);

    public static void register() {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            if (BREAKING.get()) {
                return true;
            }

            if (!(player instanceof ServerPlayer serverPlayer)) {
                return true;
            }

            if (!serverPlayer.isShiftKeyDown()) {
                return true;
            }

            ItemStack stack = serverPlayer.getMainHandItem();
            if (!(stack.getItem() instanceof RootshaperItem)) {
                return true;
            }

            Direction face = getPlayerFacing(serverPlayer);

            BREAKING.set(true);
            try {
                for (BlockPos adjacent : get3x3Positions(pos, face)) {
                    if (adjacent.equals(pos)) {
                        continue;
                    }

                    BlockState adjacentState = world.getBlockState(adjacent);
                    if (adjacentState.isAir() || adjacentState.getDestroySpeed(world, adjacent) < 0.0F) {
                        continue;
                    }

                    if (!canRootshaperBreak(adjacentState)) {
                        continue;
                    }

                    boolean broke = serverPlayer.gameMode.destroyBlock(adjacent);
                    if (broke && !serverPlayer.isCreative()) {
                        stack.hurtAndBreak(1, serverPlayer, entity -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                        if (stack.isEmpty()) {
                            break;
                        }
                    }
                }
            } finally {
                BREAKING.set(false);
            }

            return true;
        });
    }

    private static boolean canRootshaperBreak(BlockState state) {
        return state.is(BlockTags.MINEABLE_WITH_PICKAXE) || state.is(BlockTags.MINEABLE_WITH_SHOVEL);
    }

    public static void onLeftClickBlock(ServerPlayer player, BlockPos pos, Level world) {
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModItems.ROOTSHAPER.get())) {
            return;
        }

        BlockState state = world.getBlockState(pos);
        int newMode = RootshaperItem.computeMode(state);
        if (newMode != RootshaperItem.getMode(stack)) {
            RootshaperItem.setMode(stack, newMode);
        }
    }

    private static Direction getPlayerFacing(ServerPlayer player) {
        Vec3 look = player.getLookAngle();
        double ax = Math.abs(look.x);
        double ay = Math.abs(look.y);
        double az = Math.abs(look.z);

        if (ay > ax && ay > az) {
            return look.y > 0.0D ? Direction.UP : Direction.DOWN;
        }

        if (ax > az) {
            return look.x > 0.0D ? Direction.EAST : Direction.WEST;
        }

        return look.z > 0.0D ? Direction.SOUTH : Direction.NORTH;
    }

    private static List<BlockPos> get3x3Positions(BlockPos center, Direction face) {
        List<BlockPos> positions = new ArrayList<>();
        Direction[] axes = getPerpendicularAxes(face);
        Direction a = axes[0];
        Direction b = axes[1];

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                positions.add(center.relative(a, i).relative(b, j));
            }
        }

        return positions;
    }

    private static Direction[] getPerpendicularAxes(Direction face) {
        return switch (face) {
            case UP, DOWN -> new Direction[]{Direction.NORTH, Direction.EAST};
            case NORTH, SOUTH -> new Direction[]{Direction.EAST, Direction.UP};
            case EAST, WEST -> new Direction[]{Direction.NORTH, Direction.UP};
        };
    }
}

