package net.astralya.hexalia.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class BriarSickleItem extends ShearsItem {

    public static final float ATTACK_DAMAGE_BONUS = 1.0F;
    public static final float ATTACK_SPEED = -2.0F;

    private static final UUID ATTACK_DAMAGE_MODIFIER_UUID = UUID.fromString("cfa3c2d4-5bde-4d45-a2c0-0b4d89a83c91");
    private static final UUID ATTACK_SPEED_MODIFIER_UUID = UUID.fromString("6f0d0f5f-2d67-4d1d-9317-5c7be8b7a87f");

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public BriarSickleItem(Properties properties) {
        super(properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(ATTACK_DAMAGE_MODIFIER_UUID, "Weapon modifier", ATTACK_DAMAGE_BONUS, AttributeModifier.Operation.ADDITION)
        );
        builder.put(
                Attributes.ATTACK_SPEED,
                new AttributeModifier(ATTACK_SPEED_MODIFIER_UUID, "Weapon modifier", ATTACK_SPEED, AttributeModifier.Operation.ADDITION)
        );
        this.defaultModifiers = builder.build();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.is(BlockTags.LEAVES)) {
            return 15.0F;
        }

        if (state.is(BlockTags.FLOWERS) || state.is(BlockTags.REPLACEABLE_BY_TREES)) {
            return 10.0F;
        }

        return super.getDestroySpeed(stack, state);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }

        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.PASS;
        }

        ItemStack stack = context.getItemInHand();
        BlockPos center = context.getClickedPos();

        boolean clearedAny = false;
        boolean shearingMode = player.isShiftKeyDown();

        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-1, 0, -1), center.offset(1, 0, 1))) {
            BlockState state = level.getBlockState(pos);
            if (!isClearablePlant(state)) {
                continue;
            }

            if (shearingMode) {
                Block.dropResources(state, serverLevel, pos, level.getBlockEntity(pos), player, stack);
                level.removeBlock(pos, false);
                serverLevel.levelEvent(null, 2001, pos, Block.getId(state));
            } else {
                level.destroyBlock(pos, true, player);
            }

            clearedAny = true;
            if (stack.isEmpty()) {
                break;
            }
        }

        if (!clearedAny) {
            return InteractionResult.PASS;
        }

        level.playSound(
                null,
                player.blockPosition(),
                SoundEvents.PLAYER_ATTACK_SWEEP,
                SoundSource.PLAYERS,
                0.8F,
                1.0F
        );

        player.swing(context.getHand(), true);
        stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(
                context.getHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND
        ));
        return InteractionResult.CONSUME;
    }

    private static boolean isClearablePlant(BlockState state) {
        Block block = state.getBlock();
        return block == Blocks.GRASS
                || block == Blocks.TALL_GRASS
                || block == Blocks.FERN
                || block == Blocks.LARGE_FERN
                || state.is(BlockTags.FLOWERS)
                || state.is(BlockTags.SAPLINGS)
                || state.is(BlockTags.LEAVES);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }
}
