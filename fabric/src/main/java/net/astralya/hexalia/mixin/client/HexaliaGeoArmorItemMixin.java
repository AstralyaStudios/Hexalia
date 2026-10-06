package net.astralya.hexalia.mixin.client;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.astralya.hexalia.item.custom.armor.HexaliaGeoArmorItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

@Mixin(HexaliaGeoArmorItem.class)
public abstract class HexaliaGeoArmorItemMixin implements GeoItem {
    @Unique private Supplier<Object> hexalia$renderProvider;

    @Shadow(remap = false)
    public abstract Object createGeoArmorRenderer();

    @Overwrite(remap = false)
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private GeoArmorRenderer<?> renderer;

            @Override
            @SuppressWarnings("unchecked")
            public HumanoidModel<LivingEntity> getHumanoidArmorModel(
                    LivingEntity livingEntity,
                    ItemStack stack,
                    EquipmentSlot slot,
                    HumanoidModel<LivingEntity> original) {
                if (this.renderer == null) {
                    this.renderer = (GeoArmorRenderer<?>) HexaliaGeoArmorItemMixin.this.createGeoArmorRenderer();
                }

                this.renderer.prepForRender(livingEntity, stack, slot, original);
                return (HumanoidModel<LivingEntity>) this.renderer;
            }
        });
    }

    @Overwrite(remap = false)
    public Supplier<Object> getRenderProvider() {
        if (this.hexalia$renderProvider == null) {
            this.hexalia$renderProvider = GeoItem.makeRenderer(this);
        }

        return this.hexalia$renderProvider;
    }
}
