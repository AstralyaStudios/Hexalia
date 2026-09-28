package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.block.entity.custom.AstrylisBlockEntity;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.particle.ModParticleType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class AstrylisBlock extends EnchantedPlantBlock implements EntityBlock {
  public AstrylisBlock(Properties properties) {
    super(properties);
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    if (level.isClientSide) {
      BlockEntity blockEntity = level.getBlockEntity(pos);
      if (blockEntity instanceof AstrylisBlockEntity astrylis && astrylis.isActive()) {
        float progress = astrylis.getProgress();
        int particleCount = Math.max(2, (int) (6 * (1.0f - progress * 0.5f)));
        for (int i = 0; i < particleCount; i++) {
          double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.3;
          double y = pos.getY() + 0.7 + random.nextDouble() * 0.3;
          double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.3;
          level.addParticle(ModParticleType.SPARKLE.get(), x, y, z, 0, 0.01, 0);
        }
      }
    }
  }

  @Override
  public InteractionResult use(
      BlockState state,
      Level level,
      BlockPos pos,
      Player player,
      InteractionHand hand,
      BlockHitResult hitResult) {
    ItemStack itemStack = player.getItemInHand(hand);
    BlockEntity blockEntity = level.getBlockEntity(pos);
    if (blockEntity instanceof AstrylisBlockEntity astrylis) {
      if (itemStack.isEmpty()) {
        if (!level.isClientSide && !astrylis.isActive()) {
          player.displayClientMessage(
              Component.translatable("message.hexalia.astrylis.inactive"), true);
        }
        return InteractionResult.SUCCESS;
      }
      if (itemStack.getItem() == ModItems.CELESTIAL_CRYSTAL.get()) {
        if (!astrylis.isActive()) {
          if (!level.isClientSide) {
            astrylis.activate(level.getGameTime());
            level.playSound(
                null, pos, SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.BLOCKS, 1.0f, 1.0f);
            if (!player.isCreative()) {
              itemStack.shrink(1);
            }
            player.displayClientMessage(
                Component.translatable("message.hexalia.astrylis.activation"), true);
          }
          return InteractionResult.SUCCESS;
        }
      }
    }
    return InteractionResult.PASS;
  }

  @Override
  public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new AstrylisBlockEntity(blockPos, blockState);
  }

  @Override
  public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
      Level level, BlockState state, BlockEntityType<T> blockEntityType) {
    return level.isClientSide
        ? null
        : (level1, pos, state1, blockEntity) -> {
          if (blockEntity instanceof AstrylisBlockEntity astrylis) {
            AstrylisBlockEntity.tick(level1, pos, state1, astrylis);
          }
        };
  }
}
