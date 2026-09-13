package net.astralya.hexalia.compat;

import net.astralya.hexalia.Hexalia;
import net.minecraft.resources.ResourceLocation;

public final class NaturesRitualViewerIndicator {
  public static final ResourceLocation TEXTURE =
      ResourceLocation.fromNamespaceAndPath(
          Hexalia.MOD_ID, "textures/gui/category/soul_required.png");
  public static final int X = 72;
  public static final int Y = 4;
  public static final int WIDTH = 10;
  public static final int HEIGHT = 10;
  public static final String TOOLTIP_KEY = "tooltip.hexalia.requires_soul";

  private NaturesRitualViewerIndicator() {}
}
