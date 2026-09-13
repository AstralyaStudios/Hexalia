package net.astralya.hexalia.item.custom;

import java.util.List;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.entity.LivingEntity;
import net.astralya.hexalia.entity.ModEntities;
import net.astralya.hexalia.entity.custom.projectile.CinderhewProjectile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public final class CinderhewItem extends AxeItem {
    private static final float FIRE_DAMAGE = 2.0F;
    private static final int THROW_COOLDOWN_TICKS = 100;

    public CinderhewItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.getWorld().isClient) {
            int timeUntilRegen = target.timeUntilRegen;
            target.timeUntilRegen = 0;
            target.damage(target.getDamageSources().onFire(), FIRE_DAMAGE);
            target.timeUntilRegen = Math.max(target.timeUntilRegen, timeUntilRegen);
            if (!target.isFireImmune()) {
                target.setOnFireFor(2);
            }
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (player.getItemCooldownManager().isCoolingDown(this)) return TypedActionResult.fail(stack);
        if (!world.isClient) {
            ItemStack thrownStack = stack.copy();
            player.setStackInHand(hand, ItemStack.EMPTY);
            CinderhewProjectile projectile = new CinderhewProjectile(ModEntities.CINDERHEW, world, player, thrownStack);
            projectile.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 1.6F, 1.0F);
            world.spawnEntity(projectile);
            player.getItemCooldownManager().set(this, THROW_COOLDOWN_TICKS);
            player.incrementStat(Stats.USED.getOrCreateStat(this));
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_TRIDENT_THROW,
                    SoundCategory.PLAYERS, 0.7F, 0.9F + world.getRandom().nextFloat() * 0.2F);
            return TypedActionResult.success(ItemStack.EMPTY);
        }
        return TypedActionResult.success(stack, true);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.hexalia.cinderhew.fire_damage").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("tooltip.hexalia.throwable").formatted(Formatting.GRAY, Formatting.ITALIC));
    }
}
