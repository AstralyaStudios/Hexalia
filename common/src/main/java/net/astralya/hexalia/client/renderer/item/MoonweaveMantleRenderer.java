package net.astralya.hexalia.client.renderer.item;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.item.custom.armor.MoonweaveMantleItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class MoonweaveMantleRenderer extends GeoArmorRenderer<MoonweaveMantleItem> {

  public MoonweaveMantleRenderer() {
    super(
        new DefaultedItemGeoModel<>(
            new ResourceLocation(HexaliaMod.MODID, "armor/moonweave_mantle")) {
          @Override
          public ResourceLocation getTextureResource(MoonweaveMantleItem animatable) {
            return new ResourceLocation(HexaliaMod.MODID, "textures/armor/moonweave.png");
          }

          @Override
          public ResourceLocation getAnimationResource(MoonweaveMantleItem animatable) {
            return new ResourceLocation(
                HexaliaMod.MODID, "animations/moonweave_mantle.animation.json");
          }
        });
  }
}
