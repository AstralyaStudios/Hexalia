package net.astralya.hexalia.item.custom;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class CustomModelSpawnEggItem extends Item {
  private final Supplier<? extends EntityType<? extends Mob>> defaultType;

  public CustomModelSpawnEggItem(
      Supplier<? extends EntityType<? extends Mob>> defaultType, Properties properties) {
    super(properties);
    this.defaultType = defaultType;
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Level level = context.getLevel();
    if (!(level instanceof ServerLevel server)) return InteractionResult.SUCCESS;

    ItemStack stack = context.getItemInHand();
    BlockPos pos = context.getClickedPos();
    Direction face = context.getClickedFace();
    BlockState state = level.getBlockState(pos);
    BlockEntity be = level.getBlockEntity(pos);

    if (be instanceof SpawnerBlockEntity spawnerBE) {
      BaseSpawner spawner = spawnerBE.getSpawner();
      EntityType<?> type = this.getType(stack);
      spawner.setEntityId(type, level, level.getRandom(), pos); // 1.20.1 signature
      spawnerBE.setChanged();
      level.sendBlockUpdated(pos, state, state, 3);
      level.gameEvent(context.getPlayer(), GameEvent.BLOCK_CHANGE, pos);
      if (context.getPlayer() == null || !context.getPlayer().getAbilities().instabuild)
        stack.shrink(1);
      return InteractionResult.CONSUME;
    }

    BlockPos spawnPos = state.getCollisionShape(level, pos).isEmpty() ? pos : pos.relative(face);
    EntityType<?> type = this.getType(stack);

    if (type.spawn(
            server,
            stack,
            context.getPlayer(),
            spawnPos,
            MobSpawnType.SPAWN_EGG,
            true,
            !Objects.equals(pos, spawnPos) && face == Direction.UP)
        != null) {
      if (context.getPlayer() == null || !context.getPlayer().getAbilities().instabuild)
        stack.shrink(1);
      level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, pos);
    }

    return InteractionResult.CONSUME;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);
    BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
    if (hit.getType() != HitResult.Type.BLOCK) return InteractionResultHolder.pass(stack);
    if (!(level instanceof ServerLevel server)) return InteractionResultHolder.success(stack);

    BlockPos pos = hit.getBlockPos();
    if (!(level.getBlockState(pos).getBlock() instanceof LiquidBlock))
      return InteractionResultHolder.pass(stack);

    if (level.mayInteract(player, pos) && player.mayUseItemAt(pos, hit.getDirection(), stack)) {
      EntityType<?> type = this.getType(stack);
      Entity e = type.spawn(server, stack, player, pos, MobSpawnType.SPAWN_EGG, false, false);
      if (e == null) return InteractionResultHolder.pass(stack);
      if (!player.getAbilities().instabuild) stack.shrink(1);
      player.awardStat(Stats.ITEM_USED.get(this));
      level.gameEvent(player, GameEvent.ENTITY_PLACE, e.position());
      return InteractionResultHolder.consume(stack);
    }

    return InteractionResultHolder.fail(stack);
  }

  public boolean spawnsEntity(ItemStack stack, EntityType<?> entityType) {
    return Objects.equals(this.getType(stack), entityType);
  }

  public EntityType<?> getType(ItemStack stack) {
    CompoundTag tag = stack.getTag();
    if (tag != null) {
      if (tag.contains("EntityTag", 10)) {
        CompoundTag ent = tag.getCompound("EntityTag");
        if (ent.contains("id", 8)) {
          ResourceLocation rl = ResourceLocation.tryParse(ent.getString("id"));
          if (rl != null) {
            Optional<EntityType<?>> t = EntityType.byString(rl.toString());
            if (t.isPresent()) return t.get();
          }
        }
      }
      if (tag.contains("EntityType", 8)) {
        ResourceLocation rl = ResourceLocation.tryParse(tag.getString("EntityType"));
        if (rl != null) {
          Optional<EntityType<?>> t = EntityType.byString(rl.toString());
          if (t.isPresent()) return t.get();
        }
      }
    }
    return getDefaultType();
  }

  public Optional<Mob> spawnOffspringFromSpawnEgg(
      Player player,
      Mob parent,
      EntityType<? extends Mob> entityType,
      ServerLevel serverLevel,
      Vec3 pos,
      ItemStack stack) {
    if (!this.spawnsEntity(stack, entityType)) return Optional.empty();
    Mob baby =
        (parent instanceof AgeableMob ageable)
            ? ageable.getBreedOffspring(serverLevel, ageable)
            : (Mob) entityType.create(serverLevel);
    if (baby == null) return Optional.empty();

    baby.setBaby(true);
    if (!baby.isBaby()) return Optional.empty();

    baby.moveTo(pos.x(), pos.y(), pos.z(), 0.0F, 0.0F);
    serverLevel.addFreshEntityWithPassengers(baby);
    if (stack.hasCustomHoverName()) baby.setCustomName(stack.getHoverName());
    if (!player.getAbilities().instabuild) stack.shrink(1);
    return Optional.of(baby);
  }

  @Override
  public FeatureFlagSet requiredFeatures() {
    return this.getDefaultType().requiredFeatures();
  }

  protected EntityType<?> getDefaultType() {
    return this.defaultType.get();
  }
}
