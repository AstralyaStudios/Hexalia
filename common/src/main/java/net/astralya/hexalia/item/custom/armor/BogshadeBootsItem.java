package net.astralya.hexalia.item.custom.armor;

import net.astralya.hexalia.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BogshadeBootsItem extends HexaliaGeoArmorItem {
  public BogshadeBootsItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
    super(material, type, properties, "bogshade_boots", "bogshade_boots");
  }

  @Override
  public void inventoryTick(
      ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
    if (!(entity instanceof Player player)) {
      super.inventoryTick(stack, level, entity, slot, selected);
      return;
    }
    if (!player.getItemBySlot(EquipmentSlot.FEET).is(this)) {
      super.inventoryTick(stack, level, entity, slot, selected);
      return;
    }
    if (isWading(player)) {
      improveWadingMovement(player);
    } else if (!level.isClientSide() && player.onGround() && isOnNoSlowBlock(player)) {
      player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 0, false, false, true));
    }
    super.inventoryTick(stack, level, entity, slot, selected);
  }

  private static boolean isWading(Player player) {
    return player.isInWater()
        && !player.isSwimming()
        && player.getFluidHeight(FluidTags.WATER) <= 1.25D;
  }

  private static void improveWadingMovement(Player player) {
    Vec3 movement = player.getDeltaMovement();
    double horizontalBoost = player.onGround() ? 1.18D : 1.1D;
    double verticalMovement = movement.y;
    if (verticalMovement > 0.0D) {
      verticalMovement = Math.min(verticalMovement + 0.04D, 0.3D);
    }
    player.setDeltaMovement(
        movement.x * horizontalBoost, verticalMovement, movement.z * horizontalBoost);
  }

  private static boolean isOnNoSlowBlock(Player player) {
    BlockPos below = player.blockPosition().below();
    return player.level().getBlockState(below).is(ModTags.Blocks.BOGSHADE_NO_SLOW);
  }
}
