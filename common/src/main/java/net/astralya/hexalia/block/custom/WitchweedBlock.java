package net.astralya.hexalia.block.custom;

import net.astralya.hexalia.entity.custom.SilkMothEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class WitchweedBlock extends HerbBlock {

    protected static final VoxelShape SHAPE = Shapes.or(Block.box(2, 0, 1, 15, 7, 15));

    public WitchweedBlock(Supplier<MobEffect> effectSupplier, int pEffectDuration, Properties pProperties) {
        super(effectSupplier, pEffectDuration, pProperties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Vec3 off = pState.getOffset(pLevel, pPos);
        return SHAPE.move(off.x, off.y, off.z);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (!(pEntity instanceof LivingEntity living)) return;
        if (living instanceof Player p && p.getAbilities().instabuild) return;
        if (living.isSteppingCarefully() || living instanceof Frog || living instanceof SilkMothEntity || living instanceof Bee) return;

        living.makeStuckInBlock(pState, new Vec3(0.8F, 0.75D, 0.8F));

        if (!pLevel.isClientSide && pLevel.getDifficulty() != Difficulty.PEACEFUL) {
            living.addEffect(new MobEffectInstance(MobEffects.POISON, 100));
        }
    }
}
