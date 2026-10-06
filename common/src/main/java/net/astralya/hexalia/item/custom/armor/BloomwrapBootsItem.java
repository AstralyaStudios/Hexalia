package net.astralya.hexalia.item.custom.armor;

import net.minecraft.world.item.ArmorMaterial;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class BloomwrapBootsItem extends HexaliaGeoArmorItem {
  private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

  public BloomwrapBootsItem(ArmorMaterial material, Type type, Properties properties) {
    super(material, type, properties);
    SingletonGeoAnimatable.registerSyncedAnimatable(this);
  }

  @Override
  public Object createGeoArmorRenderer() {
    return createClientRenderer("BloomwrapBootsRenderer");
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
