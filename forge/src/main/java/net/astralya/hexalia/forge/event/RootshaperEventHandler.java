package net.astralya.hexalia.forge.event;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.item.custom.RootshaperItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = HexaliaMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RootshaperEventHandler {
    private static final ThreadLocal<Boolean> BREAKING = ThreadLocal.withInitial(() -> false);

    @SubscribeEvent
    public static void onRootshaperLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getLevel().isClientSide()) return;
        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(ModItems.ROOTSHAPER.get())) return;
        BlockState state = event.getLevel().getBlockState(event.getPos());
        int newMode = RootshaperItem.computeMode(state);
        if (newMode != RootshaperItem.getMode(stack)) {
            RootshaperItem.setMode(stack, newMode);
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (BREAKING.get()) return;
        if (!(event.getPlayer() instanceof ServerPlayer player)) return;
        if (!player.isShiftKeyDown()) return;
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof RootshaperItem)) return;
        BlockPos center = event.getPos();
        Level level = (Level) event.getLevel();
        Direction face = getPlayerFacing(player);
        BREAKING.set(true);
        try {
            for (BlockPos pos : get3x3Positions(center, face)) {
                if (pos.equals(center)) continue;
                BlockState state = level.getBlockState(pos);
                if (state.isAir() || state.getDestroySpeed(level, pos) < 0) continue;
                if (!stack.isCorrectToolForDrops(state)) continue;
                player.gameMode.destroyBlock(pos);
            }
        } finally {
            BREAKING.set(false);
        }
    }

    private static Direction getPlayerFacing(ServerPlayer player) {
        Vec3 look = player.getLookAngle();
        double ax = Math.abs(look.x);
        double ay = Math.abs(look.y);
        double az = Math.abs(look.z);
        if (ay > ax && ay > az) {
            return look.y > 0 ? Direction.UP : Direction.DOWN;
        } else if (ax > az) {
            return look.x > 0 ? Direction.EAST : Direction.WEST;
        } else {
            return look.z > 0 ? Direction.SOUTH : Direction.NORTH;
        }
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
            case UP, DOWN     -> new Direction[]{ Direction.NORTH, Direction.EAST };
            case NORTH, SOUTH -> new Direction[]{ Direction.EAST,  Direction.UP   };
            case EAST, WEST   -> new Direction[]{ Direction.NORTH, Direction.UP   };
        };
    }
}