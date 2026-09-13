package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.effect.ModMobEffects;
import net.astralya.hexalia.integration.accessories.AccessoriesIntegration;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.sound.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
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
  public static final IntegerProperty AGE = IntegerProperty.create("age", 0, MAX_AGE);

  public MandrakeCropBlock(Properties properties) {
    super(properties);
    registerDefaultState(stateDefinition.any().setValue(AGE, 0));
  }

  @Override
  protected ItemLike getBaseSeedId() {
    return ModItems.MANDRAKE_SEEDS.get();
  }

  @Override
  public IntegerProperty getAgeProperty() {
    return AGE;
  }

  @Override
  public int getMaxAge() {
    return MAX_AGE;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(AGE);
  }

  @Override
  public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
    BlockState result = super.playerWillDestroy(level, pos, state, player);
    if (!level.isClientSide()
        && state.getValue(AGE) == MAX_AGE
        && !player.getAbilities().instabuild
        && !AccessoriesIntegration.isWearingEarplugs(player)) {
      player.addEffect(new MobEffectInstance(ModMobEffects.holder(ModMobEffects.STUNNED), 60, 0));
      level.playSound(
          null,
          player.getX(),
          player.getY(),
          player.getZ(),
          ModSoundEvents.MANDRAKE_SCREAM.get(),
          SoundSource.PLAYERS,
          1.0F,
          1.0F);
    }
    return result;
  }
}
