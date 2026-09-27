package net.astralya.hexalia.client.renderer.entity;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.entity.custom.projectile.ThornArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ThornArrowRenderer extends ArrowRenderer<ThornArrowEntity> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(HexaliaMod.MODID, "textures/entity/projectiles/thorn_arrow.png");

    public ThornArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(ThornArrowEntity entity) {
        return TEXTURE;
    }
}

