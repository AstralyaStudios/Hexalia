package net.astralya.hexalia.item.custom.armor;

import net.astralya.hexalia.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BogshadeBootsItem extends HexaliaGeoArmorItem {
  public BogshadeBootsItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
    super(material, type, properties, "bogshade_boots", "bogshade_boots");
  }

  @Override
  public void inventoryTick(
      ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
    if (!(entity instanceof Player player) || level.isClientSide()) {
      super.inventoryTick(stack, level, entity, slot, selected);
      return;
    }
    if (!player.getItemBySlot(EquipmentSlot.FEET).is(this)) {
      super.inventoryTick(stack, level, entity, slot, selected);
      return;
    }
    if (player.isInWaterOrBubble()) {
      player.addEffect(
          new MobEffectInstance(MobEffects.WATER_BREATHING, 60, 0, false, false, true));
      player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 60, 0, false, false, true));
    } else if (player.onGround() && isOnNoSlowBlock(player)) {
      player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 0, false, false, true));
    }
    super.inventoryTick(stack, level, entity, slot, selected);
  }

  private static boolean isOnNoSlowBlock(Player player) {
    BlockPos below = player.blockPosition().below();
    return player.level().getBlockState(below).is(ModTags.Blocks.BOGSHADE_NO_SLOW);
  }
}
