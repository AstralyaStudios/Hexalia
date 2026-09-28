package net.astralya.hexalia.client.renderer.item;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.item.custom.armor.SilkweaveMantleItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class SilkweaveMantleRenderer extends GeoArmorRenderer<SilkweaveMantleItem> {
  public SilkweaveMantleRenderer() {
    super(
        new DefaultedItemGeoModel<>(
            new ResourceLocation(HexaliaMod.MODID, "armor/silkweave_mantle")) {
          @Override
          public ResourceLocation getTextureResource(SilkweaveMantleItem animatable) {
            return new ResourceLocation(HexaliaMod.MODID, "textures/armor/silkweave.png");
          }

          @Override
          public ResourceLocation getAnimationResource(SilkweaveMantleItem animatable) {
            return new ResourceLocation(
                HexaliaMod.MODID, "animations/silkweave_mantle.animation.json");
          }
        });
  }
}
