package net.astralya.hexalia.compat.rei.mutation;

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
import net.astralya.hexalia.compat.rei.HexaliaREIClientPlugin;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedList;
import java.util.List;

public final class MutationCategory implements DisplayCategory<MutationDisplay> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(HexaliaMod.MODID, "textures/gui/mutation_gui.png");
    public static final CategoryIdentifier<MutationDisplay> MUTATION =
            CategoryIdentifier.of(HexaliaMod.MODID, "mutation");

    @Override
    public CategoryIdentifier<? extends MutationDisplay> getCategoryIdentifier() {
        return MUTATION;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("item.hexalia.mutavis");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModItems.MUTAVIS.get());
    }

    @Override
    public List<Widget> setupDisplay(MutationDisplay display, Rectangle bounds) {
        Point origin = bounds.getLocation();
        List<Widget> widgets = new LinkedList<>();
        widgets.add(Widgets.createRecipeBase(bounds));

        int guiWidth = 118;
        int guiHeight = 80;

        Rectangle startPoint = HexaliaREIClientPlugin.centeredIntoRecipeBase(origin, guiWidth, guiHeight);
        widgets.add(Widgets.createTexturedWidget(TEXTURE, startPoint.x, startPoint.y, 0, 0, guiWidth, guiHeight));
        widgets.add(Widgets.createTexturedWidget(TEXTURE, startPoint.x + 28, startPoint.y + 7, 0, 0, 16, 16));

        List<EntryIngredient> inputs = display.getInputEntries();
        if (inputs != null && !inputs.isEmpty()) {
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 48, startPoint.y + 32))
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
                Component.translatable("tooltip.hexalia.mutation")
        ));

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 80;
    }
}