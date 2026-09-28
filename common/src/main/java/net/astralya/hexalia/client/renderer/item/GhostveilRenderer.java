package net.astralya.hexalia.client.renderer.item;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.item.custom.armor.GhostveilItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class GhostveilRenderer extends GeoArmorRenderer<GhostveilItem> {

  public GhostveilRenderer() {
    super(
        new DefaultedItemGeoModel<>(new ResourceLocation(HexaliaMod.MODID, "armor/ghostveil")) {
          @Override
          public ResourceLocation getTextureResource(GhostveilItem animatable) {
            return new ResourceLocation(HexaliaMod.MODID, "textures/armor/ghostveil.png");
          }

          @Override
          public ResourceLocation getAnimationResource(GhostveilItem animatable) {
            return new ResourceLocation(HexaliaMod.MODID, "animations/ghostveil.animation.json");
          }
        });
  }
}
