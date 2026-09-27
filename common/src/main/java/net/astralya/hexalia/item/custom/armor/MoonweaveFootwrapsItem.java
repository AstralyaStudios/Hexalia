package net.astralya.hexalia.item.custom.armor;

import net.astralya.hexalia.client.renderer.item.MoonweaveFootwrapsRenderer;
import net.minecraft.client.model.HumanoidModel;
import java.util.function.Consumer;

import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;


public class MoonweaveFootwrapsItem extends HexaliaGeoArmorItem implements MagicResistanceArmor {

    private static final ResourceLocation ARMOR_SET_ID = new ResourceLocation("hexalia", "moonweave");
    private static final float MAGIC_RESISTANCE_BONUS = 0.10f;

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public MoonweaveFootwrapsItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public ResourceLocation getArmorSetId() {
        return ARMOR_SET_ID;
    }

    @Override
    public ResourceLocation getArmorSetGroupId() {
        return new ResourceLocation("hexalia", "woven");
    }

    @Override
    public float getMagicResistanceBonus() {
        return MAGIC_RESISTANCE_BONUS;
    }

    @Override
    public GeoArmorRenderer<?> createGeoArmorRenderer() {
        return new MoonweaveFootwrapsRenderer();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> {
            state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
