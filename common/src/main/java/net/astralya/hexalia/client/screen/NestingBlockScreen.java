package net.astralya.hexalia.client.screen;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.menu.NestingBlockMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class NestingBlockScreen extends AbstractContainerScreen<NestingBlockMenu> {

  private static final ResourceLocation TEXTURE =
      new ResourceLocation(HexaliaMod.MODID, "textures/gui/container/small_container.png");

  public NestingBlockScreen(NestingBlockMenu menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
    this.imageWidth = 176;
    this.imageHeight = 150;
    this.inventoryLabelY = 40;
  }

  @Override
  protected void init() {
    super.init();
    this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
  }

  @Override
  protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
    graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
  }

  @Override
  public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
    this.renderBackground(graphics);
    super.render(graphics, mouseX, mouseY, partialTick);
    this.renderTooltip(graphics, mouseX, mouseY);
  }
}
