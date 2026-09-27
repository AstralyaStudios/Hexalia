package net.astralya.hexalia.item.custom;

import net.astralya.hexalia.entity.custom.projectile.PurifyingSacProjectile;
import net.astralya.hexalia.util.ModUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PurifyingSacItem extends Item {
    public PurifyingSacItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isCrouching()) {
            if (!level.isClientSide) {
                PurifyingSacProjectile proj = new PurifyingSacProjectile(level, player);
                proj.setItem(stack.copyWithCount(1));
                proj.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.5F, 1.0F);
                level.addFreshEntity(proj);

                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.SPLASH_POTION_THROW, SoundSource.PLAYERS, 0.5F,
                        0.8F + level.getRandom().nextFloat() * 0.4F);

                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                player.awardStat(Stats.ITEM_USED.get(this));
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }

        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        if (user instanceof ServerPlayer sp) {
            sp.awardStat(Stats.ITEM_USED.get(this));
            CriteriaTriggers.CONSUME_ITEM.trigger(sp, stack);
        }

        if (user instanceof Player player && !player.getAbilities().instabuild) {
            EquipmentSlot slot = stack.equals(player.getItemBySlot(EquipmentSlot.OFFHAND))
                    ? EquipmentSlot.OFFHAND
                    : EquipmentSlot.MAINHAND;
            stack.hurtAndBreak(1, user, e -> e.broadcastBreakEvent(slot));
        }

        level.playSound(null, user.getX(), user.getY(), user.getZ(),
                SoundEvents.BONE_MEAL_USE, SoundSource.PLAYERS, 0.5F, 1.0F);

        if (!level.isClientSide) {
            ModUtil.removeHarmfulEffects(user);
        }

        return stack;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.hexalia.purifying_sac").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.hexalia.throwable")
                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }
}
