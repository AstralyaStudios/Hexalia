package net.astralya.hexalia.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Set;

public class AthameItem extends Item {

    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    private static final Set<Block> STRIPPABLE_LOGS = Set.of(Blocks.DARK_OAK_LOG, ModBlocks.COTTONWOOD_LOG);
    private static final Map<Block, Block> STRIPPED_BLOCKS =
            Map.of(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG,
                    ModBlocks.COTTONWOOD_LOG, ModBlocks.STRIPPED_COTTONWOOD_LOG);

    public AthameItem(Settings settings) {
        super(settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(
                ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", 4.0, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? attributeModifiers : super.getAttributeModifiers(slot);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);

        if (player == null) {
            return ActionResult.PASS;
        }

        if (player.isSneaking() && state.isIn(BlockTags.PLANKS)) {
            transformPlanksIntoBrazier(world, pos, player, context);
            return ActionResult.SUCCESS;
        }

        if (STRIPPABLE_LOGS.contains(state.getBlock())) {
            stripLog(world, pos, state, player, context);
            return ActionResult.SUCCESS;
        }

        return super.useOnBlock(context);
    }

    private void transformPlanksIntoBrazier(World world, BlockPos pos, PlayerEntity player, ItemUsageContext context) {
        if (!world.isClient) {
            world.playSound(null, pos, SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);

            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);

            Block.dropStack(world, pos, new ItemStack(ModBlocks.RITUAL_BRAZIER));

            handleItemDamage(player, context.getStack(), context);
        }
    }

    private void stripLog(World world, BlockPos pos, BlockState state, PlayerEntity player, ItemUsageContext context) {
        if (!world.isClient) {
            world.playSound(null, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0f, 1.0f);

            Block strippedBlock = getStrippedBlock(state.getBlock());
            world.setBlockState(pos, strippedBlock.getDefaultState().with(PillarBlock.AXIS,
                    state.get(PillarBlock.AXIS)), 3);

            Block.dropStack(world, pos, new ItemStack(ModItems.TREE_RESIN));
            handleItemDamage(player, context.getStack(), context);
        }
    }

    private Block getStrippedBlock(Block originalBlock) {
        return STRIPPED_BLOCKS.getOrDefault(originalBlock, originalBlock);
    }


    private void handleItemDamage(PlayerEntity player, ItemStack stack, ItemUsageContext context) {
        if (!player.isCreative() && stack.isDamageable()) {
            stack.damage(1, player, (p) -> p.sendToolBreakStatus(context.getHand()));
            if (stack.isEmpty()) {
                player.setStackInHand(context.getHand(), ItemStack.EMPTY);
            }
        }
    }
}
