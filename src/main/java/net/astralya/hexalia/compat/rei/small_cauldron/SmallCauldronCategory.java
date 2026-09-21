package net.astralya.hexalia.compat.rei.small_cauldron;

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

import java.util.LinkedList;
import java.util.List;

public final class SmallCauldronCategory implements DisplayCategory<SmallCauldronDisplay> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(HexaliaMod.MODID, "textures/gui/category/small_cauldron_gui.png");
    public static final CategoryIdentifier<SmallCauldronDisplay> SMALL_CAULDRON =
            CategoryIdentifier.of(HexaliaMod.MODID, "small_cauldron");

    @Override
    public CategoryIdentifier<? extends SmallCauldronDisplay> getCategoryIdentifier() {
        return SMALL_CAULDRON;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("container.hexalia.small_cauldron");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModBlocks.SMALL_CAULDRON.get());
    }

    @Override
    public List<Widget> setupDisplay(SmallCauldronDisplay display, Rectangle bounds) {
        Point origin = bounds.getLocation();
        List<Widget> widgets = new LinkedList<>();
        widgets.add(Widgets.createRecipeBase(bounds));

        int guiWidth = 89;
        int guiHeight = 42;

        Rectangle startPoint = HexaliaREIClientPlugin.centeredIntoRecipeBase(origin, guiWidth, guiHeight);
        widgets.add(Widgets.createTexturedWidget(TEXTURE, startPoint.x, startPoint.y, 14, 19, guiWidth, guiHeight));

        List<EntryIngredient> inputs = display.getInputEntries();
        if (inputs != null) {
            if (!inputs.isEmpty()) {
                widgets.add(Widgets.createSlot(new Point(startPoint.x + 1, startPoint.y + 1))
                        .entries(inputs.get(0))
                        .markInput()
                        .disableBackground());
            }
            if (inputs.size() >= 2) {
                widgets.add(Widgets.createSlot(new Point(startPoint.x + 25, startPoint.y + 1))
                        .entries(inputs.get(1))
                        .markInput()
                        .disableBackground());
            }
            if (inputs.size() >= 3) {
                widgets.add(Widgets.createSlot(new Point(startPoint.x + 1, startPoint.y + 25))
                        .entries(inputs.get(2))
                        .markInput()
                        .disableBackground());
            }
            if (inputs.size() >= 4) {
                widgets.add(Widgets.createSlot(new Point(startPoint.x + 25, startPoint.y + 25))
                        .entries(inputs.get(3))
                        .markInput()
                        .disableBackground());
            }
        }

        List<EntryIngredient> outputs = display.getOutputEntries();
        if (outputs != null && !outputs.isEmpty()) {
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 70, startPoint.y + 12))
                    .entries(outputs.get(0))
                    .markOutput()
                    .disableBackground());
        }

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 62;
    }
}