package net.astralya.hexalia.client;

import net.minecraft.client.Minecraft;

public final class RootshaperTooltip {
  private RootshaperTooltip() {}

  public static boolean isSneaking() {
    return Minecraft.getInstance().player != null
        && Minecraft.getInstance().player.isShiftKeyDown();
  }
}
