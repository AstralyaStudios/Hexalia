package net.astralya.hexalia.component.item;

import com.mojang.serialization.Codec;
import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

public record HerbJarData(ItemContainerContents contents) {
  public static final int SIZE = 8;
  public static final HerbJarData EMPTY = new HerbJarData(ItemContainerContents.EMPTY);
  public static final Codec<HerbJarData> CODEC =
      ItemContainerContents.CODEC.xmap(HerbJarData::new, HerbJarData::contents);
  public static final StreamCodec<RegistryFriendlyByteBuf, HerbJarData> STREAM_CODEC =
      StreamCodec.composite(
          ItemContainerContents.STREAM_CODEC, HerbJarData::contents, HerbJarData::new);

  public HerbJarData {
    contents = normalize(contents);
  }

  public static HerbJarData fromItems(List<ItemStack> items) {
    return new HerbJarData(ItemContainerContents.fromItems(items));
  }

  public ItemStack getItem(int slot) {
    if (slot < 0 || slot >= SIZE) return ItemStack.EMPTY;
    NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);
    contents.copyInto(items);
    return items.get(slot).copy();
  }

  public boolean isEmpty() {
    return contents.nonEmptyStream().findAny().isEmpty();
  }

  private static ItemContainerContents normalize(ItemContainerContents contents) {
    NonNullList<ItemStack> source = NonNullList.withSize(SIZE, ItemStack.EMPTY);
    NonNullList<ItemStack> normalized = NonNullList.withSize(SIZE, ItemStack.EMPTY);
    contents.copyInto(source);
    int slot = 0;
    for (ItemStack stack : source) {
      for (int count = 0; count < stack.getCount() && slot < SIZE; count++) {
        normalized.set(slot++, stack.copyWithCount(1));
      }
    }
    return ItemContainerContents.fromItems(normalized);
  }
}
