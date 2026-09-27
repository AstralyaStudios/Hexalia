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
import net.astralya.hexalia.compat.NaturesRitualViewer;
import net.astralya.hexalia.compat.rei.HexaliaREIClientPlugin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedList;
import java.util.List;

public final class RitualTableCategory implements DisplayCategory<RitualTableDisplay> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(HexaliaMod.MODID, "textures/gui/ritual_table_gui.png");
    public static final CategoryIdentifier<RitualTableDisplay> RITUAL_TABLE =
            CategoryIdentifier.of(HexaliaMod.MODID, "ritual_table");

    @Override
    public CategoryIdentifier<? extends RitualTableDisplay> getCategoryIdentifier() {
        return RITUAL_TABLE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.hexalia.ritual_table");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModBlocks.RITUAL_TABLE.get());
    }

    @Override
    public List<Widget> setupDisplay(RitualTableDisplay display, Rectangle bounds) {
        Point origin = bounds.getLocation();
        List<Widget> widgets = new LinkedList<>();
        widgets.add(Widgets.createRecipeBase(bounds));

        int guiWidth = NaturesRitualViewer.WIDTH;
        int guiHeight = NaturesRitualViewer.HEIGHT;

        Rectangle startPoint = HexaliaREIClientPlugin.centeredIntoRecipeBase(origin, guiWidth, guiHeight);
        widgets.add(Widgets.createTexturedWidget(TEXTURE, startPoint.x, startPoint.y, 0, 0, guiWidth, guiHeight));

        List<EntryIngredient> ingredientEntries = display.getInputEntries();

        if (ingredientEntries != null) {
            for (int i = 0; i < Math.min(ingredientEntries.size(), NaturesRitualViewer.INPUT_X.length); i++) {
                widgets.add(Widgets.createSlot(new Point(startPoint.x + NaturesRitualViewer.INPUT_X[i],
                                startPoint.y + NaturesRitualViewer.INPUT_Y[i]))
                        .entries(ingredientEntries.get(i)).markInput().disableBackground());
            }
        }

        List<EntryIngredient> outputs = display.getOutputEntries();
        if (outputs != null && !outputs.isEmpty()) {
            widgets.add(Widgets.createSlot(new Point(startPoint.x + NaturesRitualViewer.OUTPUT_X, startPoint.y + NaturesRitualViewer.OUTPUT_Y))
                    .entries(outputs.get(0))
                    .markOutput()
                    .disableBackground());
        }

        if (display.requiresSoul()) {
            widgets.add(Widgets.createTexturedWidget(NaturesRitualViewer.SOUL_ICON,
                    startPoint.x + NaturesRitualViewer.SOUL_X, startPoint.y + NaturesRitualViewer.SOUL_Y, 0, 0, NaturesRitualViewer.SOUL_SIZE, NaturesRitualViewer.SOUL_SIZE));
            widgets.add(Widgets.createTooltip(new Rectangle(startPoint.x + NaturesRitualViewer.SOUL_X, startPoint.y + NaturesRitualViewer.SOUL_Y, NaturesRitualViewer.SOUL_SIZE, NaturesRitualViewer.SOUL_SIZE),
                    Component.translatable(NaturesRitualViewer.SOUL_TOOLTIP)));
        }

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return NaturesRitualViewer.HEIGHT;
    }
}
