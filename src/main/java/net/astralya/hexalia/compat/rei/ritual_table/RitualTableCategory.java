package net.astralya.hexalia.compat.rei.ritual_table;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.ModBlocks;
import net.astralya.hexalia.compat.RitualTableViewerLayout;
import net.astralya.hexalia.compat.rei.HexaliaREIClientPlugin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import java.util.LinkedList;
import java.util.List;

public final class RitualTableCategory implements DisplayCategory<RitualTableDisplay> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(HexaliaMod.MODID, "textures/gui/ritual_table_gui.png");
    public static final CategoryIdentifier<RitualTableDisplay> RITUAL_TABLE =
            CategoryIdentifier.of(HexaliaMod.MODID, "ritual_table");

    @Override public CategoryIdentifier<? extends RitualTableDisplay> getCategoryIdentifier() { return RITUAL_TABLE; }
    @Override public Component getTitle() { return Component.translatable("block.hexalia.ritual_table"); }
    @Override public Renderer getIcon() { return EntryStacks.of(ModBlocks.RITUAL_TABLE.get()); }

    @Override public List<Widget> setupDisplay(RitualTableDisplay display, Rectangle bounds) {
        List<Widget> widgets = new LinkedList<>();
        widgets.add(Widgets.createRecipeBase(bounds));
        Rectangle panel = HexaliaREIClientPlugin.centeredIntoRecipeBase(
                bounds.getLocation(), RitualTableViewerLayout.WIDTH, RitualTableViewerLayout.HEIGHT);
        widgets.add(Widgets.createTexturedWidget(TEXTURE, panel.x, panel.y, 0, 0,
                RitualTableViewerLayout.WIDTH, RitualTableViewerLayout.HEIGHT));
        List<EntryIngredient> inputs = display.getInputEntries();
        for (int index = 0; index < inputs.size(); index++) {
            int[] position = RitualTableViewerLayout.INPUTS[index];
            widgets.add(Widgets.createSlot(new Point(panel.x + position[0] + 1, panel.y + position[1] + 1))
                    .entries(inputs.get(index)).markInput().disableBackground());
        }
        if (!display.getOutputEntries().isEmpty()) {
            widgets.add(Widgets.createSlot(new Point(panel.x + RitualTableViewerLayout.OUTPUT_X + 1,
                    panel.y + RitualTableViewerLayout.OUTPUT_Y + 1))
                    .entries(display.getOutputEntries().get(0)).markOutput().disableBackground());
        }
        if (display.requiresSoul()) {
            Rectangle soul = new Rectangle(panel.x + RitualTableViewerLayout.SOUL_X,
                    panel.y + RitualTableViewerLayout.SOUL_Y,
                    RitualTableViewerLayout.SOUL_SIZE, RitualTableViewerLayout.SOUL_SIZE);
            widgets.add(Widgets.createTexturedWidget(RitualTableViewerLayout.SOUL_TEXTURE, soul,
                    0, 0, RitualTableViewerLayout.SOUL_SIZE, RitualTableViewerLayout.SOUL_SIZE,
                    RitualTableViewerLayout.SOUL_SIZE, RitualTableViewerLayout.SOUL_SIZE));
            widgets.add(Widgets.createTooltip(soul, Component.translatable(RitualTableViewerLayout.SOUL_TOOLTIP)));
        }
        return widgets;
    }

    @Override public int getDisplayHeight() { return RitualTableViewerLayout.HEIGHT; }
}
