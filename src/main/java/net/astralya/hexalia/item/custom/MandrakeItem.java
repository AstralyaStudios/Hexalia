package net.astralya.hexalia.item.custom;

import net.astralya.hexalia.Configuration;
import net.astralya.hexalia.compat.accessory.AccessoryLookup;
import net.astralya.hexalia.effect.ModMobEffects;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.sound.ModSoundEvents;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MandrakeItem extends Item {
    public MandrakeItem(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        if (!level.isClientSide && user instanceof Player player) {

            double radius = Configuration.MANDRAKE_SCREAM_RADIUS.get();
            int stunDuration = Configuration.MANDRAKE_STUN_DURATION.get();

            List<Entity> entities = level.getEntities(player, player.getBoundingBox().inflate(radius));
            for (Entity entity : entities) {
                if (entity instanceof LivingEntity livingEntity) {
                    boolean protectedByEarplugs = livingEntity instanceof Player target
                            && (target.getItemBySlot(EquipmentSlot.HEAD).is(ModTags.Items.STUN_IMMUNE_HEADWEAR)
                            || AccessoryLookup.hasEquipped(target, ModItems.EARPLUGS.get()));
                    if (!protectedByEarplugs) {
                        livingEntity.addEffect(new MobEffectInstance(ModMobEffects.STUNNED.get(), stunDuration, 0));
                    }
                }
            }

            level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundEvents.MANDRAKE_SCREAM.get(), SoundSource.PLAYERS, 1.0f, 1.0f);

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        return stack.isEmpty() ? ItemStack.EMPTY : stack;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.hexalia.mandrake").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 32;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

}
