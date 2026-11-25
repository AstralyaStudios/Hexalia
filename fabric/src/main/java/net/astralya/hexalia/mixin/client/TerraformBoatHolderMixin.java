package net.astralya.hexalia.mixin.client;

import com.terraformersmc.terraform.boat.api.TerraformBoatType;
import com.terraformersmc.terraform.boat.api.TerraformBoatTypeRegistry;
import com.terraformersmc.terraform.boat.impl.entity.TerraformBoatHolder;
import net.astralya.hexalia.entity.ModBoats;
import net.astralya.hexalia.entity.custom.boat.ModBoatEntity;
import net.astralya.hexalia.entity.custom.boat.ModChestBoatEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ModBoatEntity.class, ModChestBoatEntity.class})
public abstract class TerraformBoatHolderMixin implements TerraformBoatHolder {
    @Override
    public TerraformBoatType getTerraformBoat() {
        ModBoatEntity.Type variant = (Object) this instanceof ModBoatEntity boat
                ? boat.getModVariant() : ((ModChestBoatEntity) (Object) this).getModVariant();
        return TerraformBoatTypeRegistry.INSTANCE.get(variant == ModBoatEntity.Type.WILLOW
                ? ModBoats.WILLOW_BOAT_ID : ModBoats.COTTONWOOD_BOAT_ID);
    }

    @Override
    public void setTerraformBoat(TerraformBoatType boatType) {
        ModBoatEntity.Type variant = boatType == TerraformBoatTypeRegistry.INSTANCE.get(ModBoats.WILLOW_BOAT_ID)
                ? ModBoatEntity.Type.WILLOW : ModBoatEntity.Type.COTTONWOOD;
        if ((Object) this instanceof ModBoatEntity boat) {
            boat.setVariant(variant);
        } else {
            ((ModChestBoatEntity) (Object) this).setVariant(variant);
        }
    }
}
