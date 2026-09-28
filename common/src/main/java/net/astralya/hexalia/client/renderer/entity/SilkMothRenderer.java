package net.astralya.hexalia.client.renderer.entity;

import com.google.common.collect.Maps;
import java.util.Map;
import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.client.model.entity.SilkMothModel;
import net.astralya.hexalia.entity.custom.SilkMothEntity;
import net.astralya.hexalia.entity.custom.variant.SilkMothVariant;
import net.minecraft.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SilkMothRenderer extends GeoEntityRenderer<SilkMothEntity> {

  private static final Map<SilkMothVariant, ResourceLocation> LOCATION_BY_VARIANT =
      Util.make(
          Maps.newEnumMap(SilkMothVariant.class),
          map -> {
            map.put(
                SilkMothVariant.DEFAULT,
                new ResourceLocation(HexaliaMod.MODID, "textures/entity/silk_moth_default.png"));
            map.put(
                SilkMothVariant.BLUE,
                new ResourceLocation(HexaliaMod.MODID, "textures/entity/silk_moth_blue.png"));
            map.put(
                SilkMothVariant.PINK,
                new ResourceLocation(HexaliaMod.MODID, "textures/entity/silk_moth_pink.png"));
            map.put(
                SilkMothVariant.BLACK,
                new ResourceLocation(HexaliaMod.MODID, "textures/entity/silk_moth_black.png"));
          });

  public SilkMothRenderer(EntityRendererProvider.Context renderManager) {
    super(renderManager, new SilkMothModel());
  }

  @Override
  public ResourceLocation getTextureLocation(SilkMothEntity animatable) {
    return LOCATION_BY_VARIANT.get(animatable.getVariant());
  }
}
