package net.astralya.hexalia.item.custom;

import net.astralya.hexalia.block.custom.HerbJarBlock;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HerbJarBlockItem extends BlockItem {
    public HerbJarBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound storage = HerbJarBlock.getStorageNbt(stack);
        if (storage == null) return;
        HerbJarBlock.StoredSummary summary = HerbJarBlock.summarize(storage);
        if (!summary.stack().isEmpty() && summary.count() > 0) {
            tooltip.add(Text.translatable("tooltip.hexalia.herb_jar.stores",
                    summary.stack().getName(), summary.count()).formatted(Formatting.GRAY));
        }
    }
}
