package net.astralya.hexalia.item.custom;

import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.entity.custom.CacofeyEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;
import java.util.UUID;

public class HexFocusItem extends Item {
    @Override
    public net.minecraft.util.TypedActionResult<ItemStack> use(
            net.minecraft.world.World level, net.minecraft.entity.player.PlayerEntity player,
            net.minecraft.util.Hand hand) {
        ItemStack focus = player.getStackInHand(hand);
        if (hand != net.minecraft.util.Hand.MAIN_HAND
                || player.getItemCooldownManager().isCoolingDown(this)) {
            return net.minecraft.util.TypedActionResult.pass(focus);
        }
        ItemStack catalyst = player.getOffHandStack();
        if (level instanceof net.minecraft.server.world.ServerWorld serverLevel
                && net.astralya.hexalia.gameplay.enchantedplant.EnchantedPlantActivations
                        .tryActivate(serverLevel, player, catalyst)) {
            if (!player.getAbilities().creativeMode) catalyst.decrement(1);
            player.getItemCooldownManager().set(this, 20);
            return net.minecraft.util.TypedActionResult.success(focus);
        }
        return net.minecraft.util.TypedActionResult.pass(focus);
    }


    private static final int BLOCK_BREAK_EVENT_ID = 2001;
    private static final Set<Block> VALID_BLOCKS = Set.of(Blocks.COBBLED_DEEPSLATE, Blocks.DEEPSLATE);
    private static final String TAG_CACOFEY_UUID = "cacofey_uuid";

    public HexFocusItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);

        if (player == null || world.isClient) return ActionResult.PASS;
        if (player.getItemCooldownManager().isCoolingDown(this)) return ActionResult.FAIL;

        if (hasAttunedCacofey(player.getMainHandStack())) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof Inventory) {
                UUID uuid = getAttunedUUID(player.getMainHandStack());
                ServerWorld serverWorld = (ServerWorld) world;
                if (serverWorld.getEntity(uuid) instanceof CacofeyEntity cacofey) {
                    cacofey.setAnchorPos(pos);
                    clearAttunedUUID(player.getMainHandStack());
                    player.sendMessage(
                            Text.translatable("message.hexalia.cacofey.anchored", cacofey.getName()), true);
                    world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.NEUTRAL, 1.0f, 1.2f);
                    return ActionResult.SUCCESS;
                }
            } else {
                player.sendMessage(
                        Text.translatable("message.hexalia.cacofey.invalid_container"), true);
                return ActionResult.FAIL;
            }
        }

        if (VALID_BLOCKS.contains(state.getBlock())) {
            world.syncWorldEvent(BLOCK_BREAK_EVENT_ID, pos, Block.getRawIdFromState(state));
            world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_HIT, SoundCategory.BLOCKS, 1.0f, 1.0f);
            world.setBlockState(pos, ModBlocks.RITUAL_TABLE.getDefaultState());
            player.getItemCooldownManager().set(this, 60);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    public static void attuneToEntity(ItemStack stack, UUID uuid) {
        NbtCompound tag = stack.getOrCreateNbt();
        tag.putUuid(TAG_CACOFEY_UUID, uuid);
    }

    public static boolean hasAttunedCacofey(ItemStack stack) {
        NbtCompound tag = stack.getNbt();
        if (tag == null) return false;
        return tag.containsUuid(TAG_CACOFEY_UUID);
    }

    public static UUID getAttunedUUID(ItemStack stack) {
        NbtCompound tag = stack.getNbt();
        if (tag == null) return null;
        return tag.containsUuid(TAG_CACOFEY_UUID) ? tag.getUuid(TAG_CACOFEY_UUID) : null;
    }

    private static void clearAttunedUUID(ItemStack stack) {
        NbtCompound tag = stack.getNbt();
        if (tag == null) return;
        tag.remove(TAG_CACOFEY_UUID);
    }
}
