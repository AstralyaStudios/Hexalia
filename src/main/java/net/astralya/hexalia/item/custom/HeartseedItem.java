package net.astralya.hexalia.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public final class HeartseedItem extends Item {
    public HeartseedItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
        if (!(target instanceof TameableEntity animal) || !animal.isAlive() || animal.isTamed()) {
            return ActionResult.PASS;
        }

        if (!animal.getWorld().isClient) {
            animal.setOwner(player);
            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
            }
            animal.getWorld().sendEntityStatus(animal, (byte) 7);
            animal.getWorld().playSound(null, animal.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME,
                    SoundCategory.NEUTRAL, 0.6F, 1.2F);
        }
        return ActionResult.success(animal.getWorld().isClient);
    }
}
