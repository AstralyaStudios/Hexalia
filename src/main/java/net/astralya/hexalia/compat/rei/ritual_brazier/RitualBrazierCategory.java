package net.astralya.hexalia.compat.rei.ritual_brazier;

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
import net.astralya.hexalia.compat.rei.HexaliaREIClientPlugin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class RitualBrazierCategory implements DisplayCategory<RitualBrazierDisplay> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(HexaliaMod.MODID, "textures/gui/ritual_brazier_gui.png");
    public static final CategoryIdentifier<RitualBrazierDisplay> RITUAL_BRAZIER =
            CategoryIdentifier.of(HexaliaMod.MODID, "ritual_brazier");

    @Override
    public CategoryIdentifier<? extends RitualBrazierDisplay> getCategoryIdentifier() {
        return RITUAL_BRAZIER;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.hexalia.ritual_brazier");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModBlocks.RITUAL_BRAZIER.get());
    }

    @Override
    public List<Widget> setupDisplay(RitualBrazierDisplay display, Rectangle bounds) {
        Point origin = bounds.getLocation();
        List<Widget> widgets = new ArrayList<>();

        widgets.add(Widgets.createRecipeBase(bounds));

        int guiWidth = 118;
        int guiHeight = 80;

        Rectangle startPoint = HexaliaREIClientPlugin.centeredIntoRecipeBase(origin, guiWidth, guiHeight);
        widgets.add(Widgets.createTexturedWidget(TEXTURE, startPoint.x, startPoint.y, 0, 0, guiWidth, guiHeight));
        widgets.add(Widgets.createTexturedWidget(TEXTURE, startPoint.x + 28, startPoint.y + 7, 0, 0, 16, 16));

        List<EntryIngredient> inputs = display.getInputEntries();
        if (inputs != null && !inputs.isEmpty()) {
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 28, startPoint.y + 31))
                    .entries(inputs.get(0))
                    .markInput()
                    .disableBackground());
        }

        List<EntryIngredient> outputs = display.getOutputEntries();
        if (outputs != null && !outputs.isEmpty()) {
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 89, startPoint.y + 31))
                    .entries(outputs.get(0))
                    .markOutput()
                    .disableBackground());
        }

        widgets.add(Widgets.createTooltip(
                new Rectangle(startPoint.x + 4, startPoint.y + 55, 16, 16),
                Component.translatable("tooltip.hexalia.hex_focus_gui")
        ));

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 80;
    }
}