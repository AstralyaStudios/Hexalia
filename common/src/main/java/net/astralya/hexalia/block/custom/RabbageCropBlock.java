package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class RabbageCropBlock extends CropBlock {

  public static final int MAX_AGE = 3;
  public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 3);

  public RabbageCropBlock(Properties pProperties) {
    super(pProperties);
  }

  @Override
  protected ItemLike getBaseSeedId() {
    return ModItems.RABBAGE_SEEDS.get();
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
  public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
    if (!(pEntity instanceof LivingEntity) || pEntity.getType() == EntityType.BEE) {
      return;
    }
    if (!pLevel.isClientSide && pState.getValue(AGE) >= 2) {
      double deltaX = Math.abs(pEntity.getX() - pEntity.xOld);
      double deltaZ = Math.abs(pEntity.getZ() - pEntity.zOld);
      if (deltaX >= 0.003f || deltaZ >= 0.003f) {
        pEntity.hurt(pLevel.damageSources().cactus(), 0.5f);
      }
    }
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    pBuilder.add(AGE);
  }
}
