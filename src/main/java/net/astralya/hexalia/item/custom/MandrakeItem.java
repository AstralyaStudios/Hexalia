package net.astralya.hexalia.item.custom;

import net.astralya.hexalia.Configuration;
import net.astralya.hexalia.compat.accessory.AccessoryLookup;
import net.astralya.hexalia.effect.ModMobEffects;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.sound.ModSoundEvents;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MandrakeItem extends Item {

    public MandrakeItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient && user instanceof PlayerEntity player) {
            double radius = Math.max(0.0, Configuration.MANDRAKE_SCREAM_RADIUS.get());
            int stunDuration = Math.max(1, Configuration.MANDRAKE_STUN_DURATION.get());

            List<Entity> entities = world.getOtherEntities(player, player.getBoundingBox().expand(radius));
            for (Entity e : entities) {
                if (e instanceof LivingEntity living) {
                    boolean protectedByEarplugs = living instanceof PlayerEntity target
                            && (target.getEquippedStack(EquipmentSlot.HEAD).isIn(ModTags.Items.STUN_IMMUNE_HEADWEAR)
                            || AccessoryLookup.hasEquipped(target, ModItems.EARPLUGS));

                    if (!protectedByEarplugs) {
                        living.addStatusEffect(new StatusEffectInstance(ModMobEffects.STUNNED, stunDuration * 20, 0));
                    }
                }
            }

            world.playSound(null, player.getX(), player.getY(), player.getZ(),
                    ModSoundEvents.MANDRAKE_SCREAM, SoundCategory.PLAYERS, 1.0f, 1.0f);

            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
            }
        }
        return stack.isEmpty() ? ItemStack.EMPTY : stack;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.hexalia.mandrake").formatted(Formatting.GRAY));
    }
}
