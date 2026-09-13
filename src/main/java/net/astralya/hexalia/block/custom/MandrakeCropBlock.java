package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.compat.accessory.AccessoryLookup;
import net.astralya.hexalia.effect.ModMobEffects;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.sound.ModSoundEvents;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class MandrakeCropBlock extends CropBlock {

    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 3);

    public MandrakeCropBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.MANDRAKE_SEEDS.get();
    }

    @Override
    protected IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }


    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
        if (!pLevel.isClientSide() && pState.getValue(AGE) == MAX_AGE) {
            if (!(pPlayer.getItemBySlot(EquipmentSlot.HEAD).is(ModTags.Items.STUN_IMMUNE_HEADWEAR))
                    && !AccessoryLookup.hasEquipped(pPlayer, ModItems.EARPLUGS.get()) && !(pPlayer.getAbilities().instabuild)) {
                pPlayer.addEffect(new MobEffectInstance(ModMobEffects.STUNNED.get(), 60, 0));
                pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), ModSoundEvents.MANDRAKE_SCREAM.get(),
                        SoundSource.PLAYERS, 1.0f, 1.0f);
            }
        }
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AGE);
    }
}
