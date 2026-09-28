package net.astralya.hexalia.client.renderer.item;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.item.custom.armor.BloomwrapBootsItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class BloomwrapBootsRenderer extends GeoArmorRenderer<BloomwrapBootsItem> {
  public BloomwrapBootsRenderer() {
    super(
        new DefaultedItemGeoModel<>(
            new ResourceLocation(HexaliaMod.MODID, "armor/bloomwrap_boots")) {
          @Override
          public ResourceLocation getTextureResource(BloomwrapBootsItem animatable) {
            return new ResourceLocation(HexaliaMod.MODID, "textures/armor/bloomwrap.png");
          }

          @Override
          public ResourceLocation getAnimationResource(BloomwrapBootsItem animatable) {
            return new ResourceLocation(
                HexaliaMod.MODID, "animations/bloomwrap_boots.animation.json");
          }
        });
  }
}
