package net.astralya.hexalia.item.custom.armor;

import java.util.List;
import java.util.function.Consumer;
import net.astralya.hexalia.Hexalia;
import net.astralya.hexalia.util.MagicResistanceHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

public class HexaliaGeoArmorItem extends ArmorItem implements GeoItem {
  private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
  private final ResourceLocation modelResource;
  private final ResourceLocation textureResource;
  private final ResourceLocation animationResource;

  public HexaliaGeoArmorItem(
      Holder<ArmorMaterial> material,
      Type type,
      Properties properties,
      String name,
      String texture) {
    this(material, type, properties, name, texture, "animations/" + name + ".animation.json");
  }

  public HexaliaGeoArmorItem(
      Holder<ArmorMaterial> material,
      Type type,
      Properties properties,
      String name,
      String texture,
      String animationPath) {
    super(material, type, properties);
    modelResource =
        ResourceLocation.fromNamespaceAndPath(
            Hexalia.MOD_ID, "geo/item/armor/" + name + ".geo.json");
    textureResource =
        ResourceLocation.fromNamespaceAndPath(Hexalia.MOD_ID, "textures/armor/" + texture + ".png");
    animationResource = ResourceLocation.fromNamespaceAndPath(Hexalia.MOD_ID, animationPath);
    SingletonGeoAnimatable.registerSyncedAnimatable(this);
  }

  public ResourceLocation modelResource() {
    return modelResource;
  }

  public ResourceLocation textureResource() {
    return textureResource;
  }

  public ResourceLocation animationResource() {
    return animationResource;
  }

  @Override
  public void appendHoverText(
      ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
    float magicResistance = MagicResistanceHelper.getMagicResistancePct(stack);
    if (magicResistance > 0.0F) {
      tooltip.add(
          Component.translatable(
                  "tooltip.hexalia.magic_resistance",
                  MagicResistanceHelper.formatPercent(magicResistance))
              .withStyle(ChatFormatting.BLUE));
    }
  }

  @Override
  public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
    consumer.accept(createClientRenderProvider());
  }

  private GeoRenderProvider createClientRenderProvider() {
    try {
      Class<?> rendererClass =
          Class.forName("net.astralya.hexalia.client.renderer.item.HexaliaArmorRenderer");
      return (GeoRenderProvider)
          rendererClass
              .getMethod("createRenderProvider", HexaliaGeoArmorItem.class)
              .invoke(null, this);
    } catch (ReflectiveOperationException exception) {
      throw new IllegalStateException("Unable to create Hexalia armor renderer", exception);
    }
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
