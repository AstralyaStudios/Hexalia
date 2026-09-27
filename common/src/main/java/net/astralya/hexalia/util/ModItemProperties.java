package net.astralya.hexalia.util;

import dev.architectury.registry.item.ItemPropertiesRegistry;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.item.data.SpiritrootTetherData;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ModItemProperties {
    public static void addCustomItemProperties() {
        registerBottledMoth();
        registerThornbow();
        registerSpiritrootTether();
    }

    private static void registerBottledMoth() {
        register(
                (Item) ModItems.BOTTLED_MOTH.get(),
                new ResourceLocation("hexalia", "variant"),
                (stack, level, entity, seed) -> {
                    CompoundTag tag = stack.getTag();
                    if (tag == null) return 0.0F;
                    if (tag.contains("SilkMothVariant")) return tag.getInt("SilkMothVariant");
                    if (tag.contains("variantId")) return tag.getInt("variantId");
                    CompoundTag mothData = tag.getCompound("MothData");
                    return mothData.contains("VariantId") ? mothData.getInt("VariantId") : 0.0F;
                }
        );
    }

    private static void registerThornbow() {
        register(
                (Item) ModItems.THORNBOW.get(),
                new ResourceLocation("pull"),
                (stack, level, entity, seed) -> {
                    if (entity == null) return 0.0F;
                    if (entity.getUseItem() != stack) return 0.0F;
                    return (float) (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
                }
        );
        register(
                (Item) ModItems.THORNBOW.get(),
                new ResourceLocation("pulling"),
                (stack, level, entity, seed) -> {
                    if (entity == null) return 0.0F;
                    if (!entity.isUsingItem()) return 0.0F;
                    if (entity.getUseItem() != stack) return 0.0F;
                    return 1.0F;
                }
        );
    }

    private static void registerSpiritrootTether() {
        register(
                (Item) ModItems.SPIRITROOT_TETHER.get(),
                new ResourceLocation("hexalia", "bound"),
                (stack, level, entity, seed) -> {
                    CompoundTag tag = stack.getTag();
                    if (SpiritrootTetherData.load(stack).hasMob()) return 1.0F;
                    if (tag == null) return 0.0F;
                    if (tag.contains("boundMobUUID")) return 1.0F;
                    return tag.getCompound("SpiritrootTetherData").getBoolean("HasMob") ? 1.0F : 0.0F;
                }
        );
    }

    private static void register(Item item, ResourceLocation id, ClampedItemPropertyFunction property) {
        ItemPropertiesRegistry.register(item, id, property);
    }
}
