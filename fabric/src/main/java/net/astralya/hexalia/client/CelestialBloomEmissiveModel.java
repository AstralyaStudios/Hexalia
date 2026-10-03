package net.astralya.hexalia.client;

import java.util.function.Supplier;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

/** Renders only the open Bloom's overlay quads at full brightness. */
public final class CelestialBloomEmissiveModel extends ForwardingBakedModel {
  private static final ResourceLocation OVERLAY =
      new ResourceLocation("hexalia", "block/celestial_bloom_open_emissive");
  private final RenderMaterial emissive;

  private CelestialBloomEmissiveModel(BakedModel wrapped) {
    this.wrapped = wrapped;
    emissive = RendererAccess.INSTANCE.getRenderer().materialFinder().emissive(true).find();
  }

  public static void register() {
    ModelLoadingPlugin.register(plugin -> plugin.modifyModelAfterBake().register((model, context) ->
        model != null && OVERLAY.equals(context.id())
            ? new CelestialBloomEmissiveModel(model) : model));
  }

  @Override
  public boolean isVanillaAdapter() {
    return false;
  }

  @Override
  public void emitBlockQuads(
      BlockAndTintGetter level, BlockState state, BlockPos pos,
      Supplier<RandomSource> randomSupplier, RenderContext context) {
    emitFace(state, null, randomSupplier, context);
    for (Direction face : Direction.values()) {
      emitFace(state, face, randomSupplier, context);
    }
  }

  private void emitFace(
      BlockState state, Direction face, Supplier<RandomSource> randomSupplier,
      RenderContext context) {
    for (var quad : wrapped.getQuads(state, face, randomSupplier.get())) {
      context.getEmitter().fromVanilla(quad, emissive, face).emit();
    }
  }
}
