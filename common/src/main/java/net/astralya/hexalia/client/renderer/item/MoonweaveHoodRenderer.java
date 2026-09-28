package net.astralya.hexalia.client.renderer.item;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.item.custom.armor.MoonweaveHoodItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class MoonweaveHoodRenderer extends GeoArmorRenderer<MoonweaveHoodItem> {

  public MoonweaveHoodRenderer() {
    super(
        new DefaultedItemGeoModel<>(
            new ResourceLocation(HexaliaMod.MODID, "armor/moonweave_hood")) {
          @Override
          public ResourceLocation getTextureResource(MoonweaveHoodItem animatable) {
            return new ResourceLocation(HexaliaMod.MODID, "textures/armor/moonweave.png");
          }

          @Override
          public ResourceLocation getAnimationResource(MoonweaveHoodItem animatable) {
            return new ResourceLocation(
                HexaliaMod.MODID, "animations/moonweave_hood.animation.json");
          }
        });
  }
}
