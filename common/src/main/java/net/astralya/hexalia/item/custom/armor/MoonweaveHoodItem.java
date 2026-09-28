package net.astralya.hexalia.item.custom.armor;

import net.astralya.hexalia.client.renderer.item.MoonweaveHoodRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class MoonweaveHoodItem extends HexaliaGeoArmorItem implements MagicResistanceArmor {

  private static final ResourceLocation ARMOR_SET_ID = new ResourceLocation("hexalia", "moonweave");
  private static final float MAGIC_RESISTANCE_BONUS = 0.10f;

  private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

  public MoonweaveHoodItem(ArmorMaterial material, Type type, Properties properties) {
    super(material, type, properties);
    SingletonGeoAnimatable.registerSyncedAnimatable(this);
  }

  @Override
  public ResourceLocation getArmorSetId() {
    return ARMOR_SET_ID;
  }

  @Override
  public ResourceLocation getArmorSetGroupId() {
    return new ResourceLocation("hexalia", "woven");
  }

  @Override
  public float getMagicResistanceBonus() {
    return MAGIC_RESISTANCE_BONUS;
  }

  @Override
  public GeoArmorRenderer<?> createGeoArmorRenderer() {
    return new MoonweaveHoodRenderer();
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
}
