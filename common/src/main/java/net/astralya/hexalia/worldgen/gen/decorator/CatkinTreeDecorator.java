package net.astralya.hexalia.worldgen.gen.decorator;

import com.mojang.serialization.Codec;
import java.util.List;
import net.astralya.hexalia.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

public class CatkinTreeDecorator extends TreeDecorator {
  public static final Codec<CatkinTreeDecorator> CODEC = Codec.unit(CatkinTreeDecorator::new);

  public CatkinTreeDecorator() {}

  @Override
  protected TreeDecoratorType<?> type() {
    return ModTreeDecorators.CATKIN.get();
  }

  @Override
  public void place(Context pContext) {
    RandomSource random = pContext.random();
    List<BlockPos> leavesPositions = pContext.leaves();
    int catkinsPlaced = 0;
    int maxCatkins = 3 + random.nextInt(2);

    for (BlockPos pos : leavesPositions) {
      if (catkinsPlaced >= maxCatkins) break;

      BlockPos blockPosBelow = pos.below();
      if (pContext.isAir(blockPosBelow)) {
        pContext.setBlock(blockPosBelow, ModBlocks.COTTONWOOD_CATKIN.get().defaultBlockState());
        catkinsPlaced++;
      }
    }
  }
}
