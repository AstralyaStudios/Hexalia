package net.astralya.hexalia.effect.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;

public class OverfedEffect extends MobEffect {
    public OverfedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if(!pLivingEntity.getCommandSenderWorld().isClientSide() && pLivingEntity instanceof Player player) {
            FoodData foodData = player.getFoodData();
            boolean isPlayerHealing = player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION)
                    && player.isHurt() && foodData.getFoodLevel() >= 18;
            if(!isPlayerHealing) {
                float exhaustion = foodData.getExhaustionLevel();
                float reduce = Math.min(exhaustion, 4.0f);
                if (exhaustion > .0f) {
                    player.causeFoodExhaustion(-reduce);
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
