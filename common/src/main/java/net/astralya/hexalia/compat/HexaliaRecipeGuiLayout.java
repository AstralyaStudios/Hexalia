package net.astralya.hexalia.compat;

import net.astralya.hexalia.Hexalia;
import net.minecraft.resources.ResourceLocation;

public enum HexaliaRecipeGuiLayout {
  MORTAR_AND_PESTLE(
      texture("mortar_gui.png"),
      118,
      80,
      0,
      0,
      118,
      80,
      new int[][] {{27, 30}, {3, 30}, {51, 30}},
      new int[] {88, 30}),
  SMALL_CAULDRON(
      texture("category/small_cauldron_gui.png"),
      89,
      42,
      14,
      19,
      89,
      42,
      new int[][] {{0, 0}, {24, 0}, {0, 24}, {24, 24}},
      new int[] {69, 11}),
  NATURES_RITUAL(
      texture("ritual_table_gui.png"),
      118,
      80,
      0,
      0,
      118,
      80,
      new int[][] {
        {27, 30}, {3, 6}, {27, 6}, {51, 6}, {3, 30}, {51, 30}, {3, 54}, {27, 54}, {51, 54}
      },
      new int[] {88, 30}),
  CELESTIAL_INFUSION(
      texture("ritual_brazier_gui.png"),
      118,
      80,
      0,
      0,
      118,
      80,
      new int[][] {{27, 30}},
      new int[] {88, 30}),
  MUTATION(
      texture("mutation_gui.png"),
      118,
      80,
      0,
      0,
      118,
      80,
      new int[][] {{47, 31}},
      new int[] {88, 30});

  private final ResourceLocation texture;
  private final int width;
  private final int height;
  private final int textureU;
  private final int textureV;
  private final int textureWidth;
  private final int textureHeight;
  private final int[][] inputSlots;
  private final int[] outputSlot;

  HexaliaRecipeGuiLayout(
      ResourceLocation texture,
      int width,
      int height,
      int textureU,
      int textureV,
      int textureWidth,
      int textureHeight,
      int[][] inputSlots,
      int[] outputSlot) {
    this.texture = texture;
    this.width = width;
    this.height = height;
    this.textureU = textureU;
    this.textureV = textureV;
    this.textureWidth = textureWidth;
    this.textureHeight = textureHeight;
    this.inputSlots = inputSlots;
    this.outputSlot = outputSlot;
  }

  public ResourceLocation texture() {
    return texture;
  }

  public int width() {
    return width;
  }

  public int height() {
    return height;
  }

  public int textureU() {
    return textureU;
  }

  public int textureV() {
    return textureV;
  }

  public int textureWidth() {
    return textureWidth;
  }

  public int textureHeight() {
    return textureHeight;
  }

  public int inputX(int index) {
    return inputSlots[Math.min(index, inputSlots.length - 1)][0];
  }

  public int inputY(int index) {
    return inputSlots[Math.min(index, inputSlots.length - 1)][1];
  }

  public int inputCount() {
    return inputSlots.length;
  }

  public int outputX() {
    return outputSlot[0];
  }

  public int outputY() {
    return outputSlot[1];
  }

  private static ResourceLocation texture(String path) {
    return ResourceLocation.fromNamespaceAndPath(Hexalia.MOD_ID, "textures/gui/" + path);
  }
}
