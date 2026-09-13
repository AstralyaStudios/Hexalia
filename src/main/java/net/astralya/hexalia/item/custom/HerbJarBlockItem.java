package net.astralya.hexalia.item.custom;

import net.astralya.hexalia.block.custom.HerbJarBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HerbJarBlockItem extends BlockItem {
    public HerbJarBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        CompoundTag storage = HerbJarBlock.getStorageNbt(stack);
        if (storage == null) return;
        HerbJarBlock.StoredSummary summary = HerbJarBlock.summarize(storage);
        if (!summary.stack().isEmpty() && summary.count() > 0) {
            tooltip.add(Component.translatable("tooltip.hexalia.herb_jar.stores",
                    summary.stack().getHoverName(), summary.count()).withStyle(ChatFormatting.GRAY));
        }
    }
}
