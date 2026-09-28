package net.astralya.hexalia.client.renderer.entity;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.client.renderer.layer.CacofeyHeldItemLayer;
import net.astralya.hexalia.entity.custom.CacofeyEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CacofeyRenderer extends GeoEntityRenderer<CacofeyEntity> {

  public CacofeyRenderer(EntityRendererProvider.Context context) {
    super(context, new CacofeyModel());
    this.addRenderLayer(new CacofeyHeldItemLayer(this));
  }

  public static class CacofeyModel extends GeoModel<CacofeyEntity> {

    @Override
    public ResourceLocation getModelResource(CacofeyEntity entity) {
      return new ResourceLocation(HexaliaMod.MODID, "geo/entity/cacofey.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CacofeyEntity entity) {
      return new ResourceLocation(HexaliaMod.MODID, "textures/entity/cacofey.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CacofeyEntity entity) {
      return new ResourceLocation(HexaliaMod.MODID, "animations/entity/cacofey.animation.json");
    }
  }
}
