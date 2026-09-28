package net.astralya.hexalia.client.renderer.item;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.item.custom.armor.BloomwrapRobesItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class BloomwrapRobesRenderer extends GeoArmorRenderer<BloomwrapRobesItem> {
  public BloomwrapRobesRenderer() {
    super(
        new DefaultedItemGeoModel<>(
            new ResourceLocation(HexaliaMod.MODID, "armor/bloomwrap_robes")) {
          @Override
          public ResourceLocation getTextureResource(BloomwrapRobesItem animatable) {
            return new ResourceLocation(HexaliaMod.MODID, "textures/armor/bloomwrap.png");
          }

          @Override
          public ResourceLocation getAnimationResource(BloomwrapRobesItem animatable) {
            return new ResourceLocation(
                HexaliaMod.MODID, "animations/bloomwrap_robes.animation.json");
          }
        });
  }
}
