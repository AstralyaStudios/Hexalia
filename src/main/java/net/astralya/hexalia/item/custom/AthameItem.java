package net.astralya.hexalia.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.Set;

public class AthameItem extends Item {
    private final Multimap<Attribute, AttributeModifier> attributeModifiers;
    private static final Set<Block> STRIPPABLE_LOGS = Set.of(Blocks.DARK_OAK_LOG, ModBlocks.COTTONWOOD_LOG.get());
    private static final Map<Block, Block> STRIPPED_BLOCKS = Map.of(
            Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG,
            ModBlocks.COTTONWOOD_LOG.get(), ModBlocks.STRIPPED_COTTONWOOD_LOG.get()
    );

    public AthameItem(Properties pProperties) {
        super(pProperties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(
                BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 4.0, AttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? attributeModifiers : super.getDefaultAttributeModifiers(slot);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        Player player = pContext.getPlayer();
        BlockPos blockPos = pContext.getClickedPos();
        BlockState state = level.getBlockState(blockPos);

        if (player == null) {
            return InteractionResult.PASS;
        }

        if (player.isShiftKeyDown() && state.is(BlockTags.PLANKS)) {
            transformPlanksIntoBrazier(level, blockPos, player, pContext);
            return InteractionResult.SUCCESS;
        }

        if (STRIPPABLE_LOGS.contains(state.getBlock())) {
            stripLog(level, blockPos, state, player, pContext);
            return InteractionResult.SUCCESS;
        }

        return super.useOn(pContext);
    }

    private void transformPlanksIntoBrazier(Level level, BlockPos blockPos, Player player, UseOnContext pContext) {
        if (!level.isClientSide) {
            level.playSound(null, blockPos, SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);

            level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);

            Block.popResource(level, blockPos, new ItemStack(ModBlocks.RITUAL_BRAZIER.get()));

            handleItemDamage(player, pContext.getItemInHand(), pContext);
        }
    }

    private void stripLog(Level level, BlockPos blockPos, BlockState state, Player player, UseOnContext pContext) {
        if (!level.isClientSide) {
            level.playSound(null, blockPos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0f, 1.0f);

            Block strippedBlock = getStrippedBlock(state.getBlock());
            level.setBlock(blockPos, strippedBlock.defaultBlockState()
                    .setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS)), 3);

            Block.popResource(level, blockPos, new ItemStack(ModItems.TREE_RESIN.get()));

            handleItemDamage(player, pContext.getItemInHand(), pContext);
        }
    }

    private Block getStrippedBlock(Block originalBlock) {
        return STRIPPED_BLOCKS.getOrDefault(originalBlock, originalBlock);
    }

    private void handleItemDamage(Player player, ItemStack stack, UseOnContext pContext) {
        if (!player.isCreative() && stack.isDamageableItem()) {
            stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(pContext.getHand()));
            if (stack.isEmpty()) {
                player.setItemInHand(pContext.getHand(), ItemStack.EMPTY);
            }
        }
    }
}
