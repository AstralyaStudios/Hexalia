package net.astralya.hexalia.item.custom.armor;

import java.util.UUID;
import net.astralya.hexalia.client.renderer.item.BogshadeBootsRenderer;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class BogshadeBootsItem extends HexaliaGeoArmorItem {

  private static final double NO_SLOW_BOOST = 0.25D;
  private static final UUID SLOW_BLOCK_MOVE_SPEED_MODIFIER_UUID =
      UUID.fromString("b3341177-5f7d-4ea3-99db-8c7bce180e54");

  private static final AttributeModifier SLOW_BLOCK_MOVE_SPEED_MODIFIER =
      new AttributeModifier(
          SLOW_BLOCK_MOVE_SPEED_MODIFIER_UUID,
          "bogshade_slow_block_move_speed",
          NO_SLOW_BOOST,
          AttributeModifier.Operation.MULTIPLY_TOTAL);

  private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

  public BogshadeBootsItem(ArmorMaterial material, Type type, Properties properties) {
    super(material, type, properties);
    SingletonGeoAnimatable.registerSyncedAnimatable(this);
  }

  @Override
  public GeoArmorRenderer<?> createGeoArmorRenderer() {
    return new BogshadeBootsRenderer();
  }

  @Override
  public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    controllers.add(
        new AnimationController<>(
            this,
            "controller",
            0,
            state -> {
              state
                  .getController()
                  .setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
              return PlayState.CONTINUE;
            }));
  }

  @Override
  public AnimatableInstanceCache getAnimatableInstanceCache() {
    return cache;
  }

  @Override
  public void inventoryTick(
      ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
    if (!(entity instanceof Player player) || level.isClientSide) {
      super.inventoryTick(stack, level, entity, slot, selected);
      return;
    }

    boolean wearing = player.getItemBySlot(EquipmentSlot.FEET).is(this);
    if (!wearing) {
      removeAll(player);
      super.inventoryTick(stack, level, entity, slot, selected);
      return;
    }

    boolean inWater = player.isInWaterOrBubble();
    if (inWater) {
      applySwimSpeed(player);
    } else {
      removeSwimSpeed(player);
    }

    boolean onNoSlow = !inWater && player.onGround() && isOnNoSlowBlock(player);
    if (onNoSlow) {
      applySlowBlockMoveSpeed(player);
    } else {
      removeSlowBlockMoveSpeed(player);
    }

    super.inventoryTick(stack, level, entity, slot, selected);
  }

  private static void applySwimSpeed(Player player) {
    var velocity = player.getDeltaMovement();
    double scale = player.isSprinting() ? 1.15D : 1.08D;
    double verticalMovement = velocity.y;
    if (player.isInWater()
        && !player.isSwimming()
        && player.getFluidHeight(FluidTags.WATER) <= 1.25D
        && verticalMovement > 0.0D) {
      verticalMovement = Math.min(verticalMovement + 0.04D, 0.3D);
    }
    player.setDeltaMovement(velocity.x * scale, verticalMovement, velocity.z * scale);
    player.hurtMarked = true;
  }

  private static void removeSwimSpeed(Player player) {}

  private static void applySlowBlockMoveSpeed(Player player) {
    AttributeInstance movement = player.getAttribute(Attributes.MOVEMENT_SPEED);
    if (movement == null) {
      return;
    }

    if (movement.getModifier(SLOW_BLOCK_MOVE_SPEED_MODIFIER_UUID) == null) {
      movement.addTransientModifier(SLOW_BLOCK_MOVE_SPEED_MODIFIER);
    }
  }

  private static void removeSlowBlockMoveSpeed(Player player) {
    AttributeInstance movement = player.getAttribute(Attributes.MOVEMENT_SPEED);
    if (movement == null) {
      return;
    }

    if (movement.getModifier(SLOW_BLOCK_MOVE_SPEED_MODIFIER_UUID) != null) {
      movement.removeModifier(SLOW_BLOCK_MOVE_SPEED_MODIFIER_UUID);
    }
  }

  private static boolean isOnNoSlowBlock(Player player) {
    BlockPos below = player.blockPosition().below();
    return player.level().getBlockState(below).is(ModTags.Blocks.BOGSHADE_NO_SLOW);
  }

  private static void removeAll(Player player) {
    removeSwimSpeed(player);
    removeSlowBlockMoveSpeed(player);
  }
}
