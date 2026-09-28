package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.block.entity.custom.AegifloraBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AegifloraBlock extends EnchantedPlantBlock implements EntityBlock {

  public AegifloraBlock(Properties pProperties) {
    super(pProperties);
  }

  @Override
  public RenderShape getRenderShape(BlockState pState) {
    return RenderShape.MODEL;
  }

  @Override
  public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
    return new AegifloraBlockEntity(pPos, pState);
  }
}
