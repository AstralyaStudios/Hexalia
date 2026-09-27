package net.astralya.hexalia.item.custom;

import net.astralya.hexalia.block.custom.MorphoraBlock;
import net.astralya.hexalia.particle.ModParticleType;
import net.astralya.hexalia.recipe.MutationRecipe;
import net.astralya.hexalia.recipe.ModRecipes;
import net.minecraft.world.SimpleContainer;
import net.astralya.hexalia.util.MutationOutput;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class MutavisItem extends Item {
    public MutavisItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) return InteractionResult.SUCCESS;
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();
        boolean success = tryMutate((ServerLevel) level, pos, stack, context.getPlayer());
        if (!success && level.getBlockState(pos).getBlock() instanceof MorphoraBlock morphoraBlock) {
            success = morphoraBlock.tryActivateWithMutavis((ServerLevel) level, pos, stack, context.getPlayer());
        }
        return success ? InteractionResult.CONSUME : InteractionResult.PASS;
    }

    public boolean tryMutate(ServerLevel level, BlockPos pos, ItemStack mutavisStack, Player player) {
        ItemStack inputStack = new ItemStack(level.getBlockState(pos).getBlock().asItem());
        if (inputStack.isEmpty()) return false;

        Optional<MutationRecipe> match = level.getRecipeManager()
                .getRecipeFor(MutationRecipe.Type.INSTANCE, new SimpleContainer(inputStack), level);
        if (match.isEmpty()) return false;

        level.destroyBlock(pos, false);
        ItemStack out = match.get().assemble(new SimpleContainer(inputStack), level.registryAccess());
        MutationOutput.apply(level, pos, out);
        if (player == null || !player.getAbilities().instabuild) {
            mutavisStack.shrink(1);
        }
        emitEffects(level, pos);
        return true;
    }

    public void emitEffects(ServerLevel level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 0.5F, 1.0F);
        level.sendParticles(
                ModParticleType.LEAVES.get(),
                pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5,
                15, 0.2, 0.25, 0.2, 0.0
        );
    }
}
