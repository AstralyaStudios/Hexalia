package net.astralya.hexalia.block.custom;

import com.mojang.serialization.MapCodec;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import net.astralya.hexalia.gameplay.mutation.MutationProcess;
import net.astralya.hexalia.item.custom.MutavisItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MorphoraBlock extends BushBlock {
  public static final MapCodec<MorphoraBlock> CODEC = simpleCodec(MorphoraBlock::new);
  private static final int MUTATION_RADIUS = 6;
  private static final int MUTATION_HEIGHT = 3;
  private static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 10.0, 11.0);

  public MorphoraBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected MapCodec<? extends BushBlock> codec() {
    return CODEC;
  }

  @Override
  protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
    return state.is(Blocks.MAGMA_BLOCK) || state.isFaceSturdy(level, pos, Direction.UP);
  }

  @Override
  protected VoxelShape getShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    Vec3 offset = state.getOffset(level, pos);
    return SHAPE.move(offset.x, offset.y, offset.z);
  }

  public boolean tryActivateWithMutavis(
      ServerLevel level, BlockPos morphoraPos, ItemStack mutavisStack, Player player) {
    List<MutationProcess.Target> targets = new ArrayList<>();
    for (int x = -MUTATION_RADIUS; x <= MUTATION_RADIUS; x++) {
      for (int y = -MUTATION_HEIGHT; y <= MUTATION_HEIGHT; y++) {
        for (int z = -MUTATION_RADIUS; z <= MUTATION_RADIUS; z++) {
          BlockPos targetPos = morphoraPos.offset(x, y, z);
          if (!level.hasChunkAt(targetPos)) continue;
          if (!targetPos.equals(morphoraPos)) {
            MutationProcess.Target target = MutationProcess.findTarget(level, targetPos);
            if (target != null) targets.add(target);
          }
        }
      }
    }
    if (targets.isEmpty()) return false;
    List<MutationProcess.Target> funded = new ArrayList<>();
    List<BlockPos> unfunded = new ArrayList<>();
    Set<BlockPos> reservedGrass = new HashSet<>();
    for (MutationProcess.Target target : targets) {
      BlockPos grass = findLocalGrass(level, target.pos(), reservedGrass);
      if (grass != null) {
        reservedGrass.add(grass);
        funded.add(target.withGrass(grass));
      } else {
        unfunded.add(target.pos());
      }
    }
    if (player == null || !player.getAbilities().instabuild) {
      mutavisStack.shrink(1);
    }
    MutationProcess.startMorphora(level, morphoraPos, funded, unfunded);
    MutavisItem.playApplicationSound(level, morphoraPos);
    level.playSound(null, morphoraPos, SoundEvents.MOSS_PLACE, SoundSource.BLOCKS, 0.75F,
        1.0F + level.random.nextFloat() * 0.15F);
    return true;
  }

  private static BlockPos findLocalGrass(ServerLevel level, BlockPos target, Set<BlockPos> reserved) {
    BlockPos below = target.below();
    if (isAvailableGrass(level, below, reserved)) return below;
    for (int y = -1; y <= 0; y++) {
      for (int x = -1; x <= 1; x++) {
        for (int z = -1; z <= 1; z++) {
          if (x == 0 && z == 0) continue;
          BlockPos candidate = target.offset(x, y, z);
          if (isAvailableGrass(level, candidate, reserved)) return candidate;
        }
      }
    }
    return null;
  }

  private static boolean isAvailableGrass(ServerLevel level, BlockPos pos, Set<BlockPos> reserved) {
    return !reserved.contains(pos) && level.hasChunkAt(pos) && level.getBlockState(pos).is(Blocks.GRASS_BLOCK);
  }
}
