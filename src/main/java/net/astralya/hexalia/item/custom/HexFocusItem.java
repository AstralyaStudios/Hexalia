package net.astralya.hexalia.item.custom;

import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.entity.custom.CacofeyEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;
import java.util.UUID;

public class HexFocusItem extends Item {
    @Override
    public net.minecraft.world.InteractionResultHolder<ItemStack> use(
            Level level, Player player, net.minecraft.world.InteractionHand hand) {
        ItemStack focus = player.getItemInHand(hand);
        if (hand != net.minecraft.world.InteractionHand.MAIN_HAND
                || player.getCooldowns().isOnCooldown(this)) {
            return net.minecraft.world.InteractionResultHolder.pass(focus);
        }
        ItemStack catalyst = player.getOffhandItem();
        if (level instanceof ServerLevel serverLevel
                && net.astralya.hexalia.gameplay.enchantedplant.EnchantedPlantActivations
                        .tryActivate(serverLevel, player, catalyst)) {
            if (!player.getAbilities().instabuild) catalyst.shrink(1);
            player.getCooldowns().addCooldown(this, 20);
            return net.minecraft.world.InteractionResultHolder.success(focus);
        }
        return net.minecraft.world.InteractionResultHolder.pass(focus);
    }

    private static final int BLOCK_BREAK_EVENT_ID = 2001;
    private static final Set<Block> VALID_BLOCKS = Set.of(Blocks.COBBLED_DEEPSLATE, Blocks.DEEPSLATE);
    private static final String TAG_CACOFEY_UUID = "cacofey_uuid";

    public HexFocusItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        if (player == null || level.isClientSide()) return InteractionResult.PASS;
        if (player.getCooldowns().isOnCooldown(this)) return InteractionResult.FAIL;
        if (hasAttunedCacofey(player.getMainHandItem())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof Container) {
                UUID uuid = getAttunedUUID(player.getMainHandItem());
                ServerLevel serverLevel = (ServerLevel) level;
                if (serverLevel.getEntity(uuid) instanceof CacofeyEntity cacofey) {
                    cacofey.setAnchorPos(pos);
                    clearAttunedUUID(player.getMainHandItem());
                    player.displayClientMessage(
                            Component.translatable("message.hexalia.cacofey.anchored", cacofey.getName()), true);
                    level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.NEUTRAL, 1.0f, 1.2f);
                    return InteractionResult.SUCCESS;
                }
            } else {
                player.displayClientMessage(
                        Component.translatable("message.hexalia.cacofey.invalid_container"), true);
                return InteractionResult.FAIL;
            }
        }
        if (VALID_BLOCKS.contains(state.getBlock())) {
            level.levelEvent(BLOCK_BREAK_EVENT_ID, pos, Block.getId(state));
            level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.BLOCKS, 1.0f, 1.0f);
            level.setBlockAndUpdate(pos, ModBlocks.RITUAL_TABLE.get().defaultBlockState());
            player.getCooldowns().addCooldown(this, 60);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public static void attuneToEntity(ItemStack stack, UUID uuid) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putUUID(TAG_CACOFEY_UUID, uuid);
    }

    public static boolean hasAttunedCacofey(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.hasUUID(TAG_CACOFEY_UUID);
    }

    public static UUID getAttunedUUID(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.hasUUID(TAG_CACOFEY_UUID)) return null;
        return tag.getUUID(TAG_CACOFEY_UUID);
    }

    private static void clearAttunedUUID(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return;
        tag.remove(TAG_CACOFEY_UUID);
        if (tag.isEmpty()) {
            stack.setTag(null);
        }
    }
}
