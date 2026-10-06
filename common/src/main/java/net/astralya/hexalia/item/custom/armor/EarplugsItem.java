package net.astralya.hexalia.item.custom.armor;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class EarplugsItem extends HexaliaGeoArmorItem {

  private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

  public EarplugsItem(ArmorMaterial material, Type type, Properties properties) {
    super(material, type, properties);
    SingletonGeoAnimatable.registerSyncedAnimatable(this);
  }

  @Override
  public void appendHoverText(
      ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
    super.appendHoverText(stack, level, tooltip, flag);
    tooltip.add(
        Component.translatable("tooltip.hexalia.accessory.earplugs")
            .withStyle(ChatFormatting.GRAY));
  }

  @Override
  public Object createGeoArmorRenderer() {
    return createClientRenderer("EarplugsRenderer");
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
