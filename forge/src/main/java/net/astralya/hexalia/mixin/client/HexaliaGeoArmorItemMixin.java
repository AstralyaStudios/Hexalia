package net.astralya.hexalia.mixin.client;

import java.util.function.Consumer;
import net.astralya.hexalia.item.custom.armor.HexaliaGeoArmorItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

@Mixin(HexaliaGeoArmorItem.class)
public abstract class HexaliaGeoArmorItemMixin {
    @Shadow(remap = false)
    public abstract GeoArmorRenderer<?> createGeoArmorRenderer();

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<?> renderer;

            @Override
            @SuppressWarnings("unchecked")
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(
                    LivingEntity livingEntity,
                    ItemStack stack,
                    EquipmentSlot slot,
                    HumanoidModel<?> original) {
                if (this.renderer == null) {
                    this.renderer = HexaliaGeoArmorItemMixin.this.createGeoArmorRenderer();
                }

                this.renderer.prepForRender(livingEntity, stack, slot, original);
                return (HumanoidModel<LivingEntity>) this.renderer;
            }
        });
    }
}
