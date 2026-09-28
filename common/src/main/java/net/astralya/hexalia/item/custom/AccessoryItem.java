package net.astralya.hexalia.item.custom;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class AccessoryItem extends Item {
  private final String loreKey;

  public AccessoryItem(Properties properties, String id) {
    super(properties.stacksTo(1));
    loreKey = "tooltip.hexalia.accessory." + id;
  }

  @Override
  public void appendHoverText(
      ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
    tooltip.add(Component.translatable(loreKey).withStyle(ChatFormatting.GRAY));
  }
}
