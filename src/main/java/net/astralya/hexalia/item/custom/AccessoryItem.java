package net.astralya.hexalia.item.custom;

import java.util.List;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AccessoryItem extends Item {
    private final String tooltipKey;

    public AccessoryItem(Settings settings, String name) {
        super(settings);
        this.tooltipKey = "tooltip.hexalia.accessory." + name;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable(tooltipKey).formatted(Formatting.GRAY));
    }
}
