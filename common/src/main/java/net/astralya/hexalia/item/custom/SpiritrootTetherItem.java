package net.astralya.hexalia.item.custom;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.astralya.hexalia.item.data.SpiritrootTetherData;
import net.astralya.hexalia.particle.ModParticleType;
import net.astralya.hexalia.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class SpiritrootTetherItem extends Item {
  public SpiritrootTetherItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult interactLivingEntity(
      ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
    Level level = player.level();
    if (level.isClientSide) return InteractionResult.SUCCESS;
    if (!(target instanceof Mob mob)) return InteractionResult.PASS;

    ItemStack handStack = player.getItemInHand(hand);
    SpiritrootTetherData data = SpiritrootTetherData.load(handStack);

    if (data.hasMob()) {
      player.displayClientMessage(
          Component.translatable("message.hexalia.spiritroot_tether.already_occupied"), true);
      return InteractionResult.SUCCESS;
    }
    if (!canCapture(player, mob)) {
      player.displayClientMessage(
          Component.translatable("message.hexalia.spiritroot_tether.cannot_capture"), true);
      return InteractionResult.SUCCESS;
    }

    CompoundTag mobTag = new CompoundTag();
    mob.saveWithoutId(mobTag);
    ResourceLocation typeId = BuiltInRegistries.ENTITY_TYPE.getKey(mob.getType());
    UUID uuid = mob.getUUID();
    String nameJson = encodeNameJson(mob);

    data.withCapturedMob(typeId, mobTag, uuid, nameJson).save(handStack);

    if (level instanceof ServerLevel serverLevel) {
      serverLevel.sendParticles(
          ModParticleType.LEAVES.get(),
          mob.getX(),
          mob.getY() + (mob.getBbHeight() * 0.5D),
          mob.getZ(),
          18,
          0.25D,
          0.25D,
          0.25D,
          0.02D);
    }
    level.playSound(
        null,
        mob.getX(),
        mob.getY(),
        mob.getZ(),
        SoundEvents.SOUL_ESCAPE,
        SoundSource.PLAYERS,
        0.6F,
        1.2F);
    mob.discard();
    handStack.hurtAndBreak(
        1,
        player,
        p ->
            p.broadcastBreakEvent(
                hand == InteractionHand.MAIN_HAND
                    ? EquipmentSlot.MAINHAND
                    : EquipmentSlot.OFFHAND));
    player.displayClientMessage(
        Component.translatable("message.hexalia.spiritroot_tether.captured"), true);
    return InteractionResult.SUCCESS;
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Level level = context.getLevel();
    Player player = context.getPlayer();
    if (player == null) return InteractionResult.PASS;
    if (level.isClientSide) return InteractionResult.SUCCESS;

    ItemStack stack = context.getItemInHand();
    SpiritrootTetherData data = SpiritrootTetherData.load(stack);

    if (player.isShiftKeyDown()) {
      if (!data.hasMob()) {
        BlockPos pos = context.getClickedPos();
        if (!level.getBlockState(pos).is(ModTags.Blocks.SPIRITROOT_BOUND_BLOCKS)) {
          player.displayClientMessage(
              Component.translatable("message.hexalia.spiritroot_tether.invalid_bind_block"), true);
          return InteractionResult.SUCCESS;
        }
        data.withBound(level.dimension(), pos).save(stack);
        player.displayClientMessage(
            Component.translatable("message.hexalia.spiritroot_tether.bound"), true);
        return InteractionResult.SUCCESS;
      }

      Optional<SpiritrootTetherData.BoundLocation> boundOpt = data.bound();
      if (boundOpt.isEmpty()) {
        player.displayClientMessage(
            Component.translatable("message.hexalia.spiritroot_tether.not_bound"), true);
        return InteractionResult.SUCCESS;
      }

      SpiritrootTetherData.BoundLocation bound = boundOpt.get();
      ServerLevel targetLevel = ((ServerLevel) level).getServer().getLevel(bound.dimension());
      if (targetLevel == null) {
        player.displayClientMessage(
            Component.translatable("message.hexalia.spiritroot_tether.invalid_dimension"), true);
        return InteractionResult.SUCCESS;
      }

      BlockPos spawnPos = bound.pos().above();
      if (!spawnCapturedMob(targetLevel, spawnPos, data)) {
        player.displayClientMessage(
            Component.translatable("message.hexalia.spiritroot_tether.recall_failed"), true);
        return InteractionResult.SUCCESS;
      }

      targetLevel.playSound(
          null,
          spawnPos.getX() + 0.5D,
          spawnPos.getY(),
          spawnPos.getZ() + 0.5D,
          SoundEvents.AMETHYST_BLOCK_CHIME,
          SoundSource.PLAYERS,
          0.7F,
          1.1F);
      data.clearedMob().save(stack);
      stack.hurtAndBreak(
          1,
          player,
          p ->
              p.broadcastBreakEvent(
                  context.getHand() == InteractionHand.MAIN_HAND
                      ? EquipmentSlot.MAINHAND
                      : EquipmentSlot.OFFHAND));
      player.displayClientMessage(
          Component.translatable("message.hexalia.spiritroot_tether.sent_to_anchor"), true);
      return InteractionResult.SUCCESS;
    }

    if (!data.hasMob()) return InteractionResult.PASS;

    BlockPos spawnPos = context.getClickedPos().above();
    if (!spawnCapturedMob((ServerLevel) level, spawnPos, data)) {
      player.displayClientMessage(
          Component.translatable("message.hexalia.spiritroot_tether.recall_failed"), true);
      return InteractionResult.SUCCESS;
    }

    level.playSound(
        null,
        spawnPos.getX() + 0.5D,
        spawnPos.getY(),
        spawnPos.getZ() + 0.5D,
        SoundEvents.AMETHYST_BLOCK_CHIME,
        SoundSource.PLAYERS,
        0.7F,
        1.1F);
    data.clearedMob().save(stack);
    stack.hurtAndBreak(
        1,
        player,
        p ->
            p.broadcastBreakEvent(
                context.getHand() == InteractionHand.MAIN_HAND
                    ? EquipmentSlot.MAINHAND
                    : EquipmentSlot.OFFHAND));
    player.displayClientMessage(
        Component.translatable("message.hexalia.spiritroot_tether.released"), true);
    return InteractionResult.SUCCESS;
  }

  @Override
  public boolean isFoil(ItemStack stack) {
    return SpiritrootTetherData.load(stack).hasMob();
  }

  @Override
  public void appendHoverText(
      ItemStack stack,
      @Nullable Level level,
      List<Component> tooltipComponents,
      TooltipFlag tooltipFlag) {
    tooltipComponents.add(
        Component.translatable("tooltip.hexalia.spiritroot_tether.flavor")
            .withStyle(ChatFormatting.GOLD));
    SpiritrootTetherData data = SpiritrootTetherData.load(stack);
    Component mobName = data.hasMob() ? getCapturedMobName(data) : null;
    if (mobName != null) {
      tooltipComponents.add(
          Component.translatable("tooltip.hexalia.spiritroot_tether", mobName)
              .withStyle(ChatFormatting.AQUA));
    }
    data.bound()
        .ifPresent(
            bound ->
                tooltipComponents.add(
                    Component.translatable(
                            "tooltip.hexalia.spiritroot_tether.bound", boundPositionName(bound))
                        .withStyle(ChatFormatting.BLUE)));
    if (mobName != null) {
      tooltipComponents.add(
          Component.translatable("tooltip.hexalia.spiritroot_tether.release", mobName)
              .withStyle(ChatFormatting.GRAY));
      data.bound()
          .ifPresent(
              bound ->
                  tooltipComponents.add(
                      Component.translatable(
                              "tooltip.hexalia.spiritroot_tether.remote_release",
                              mobName,
                              boundPositionName(bound))
                          .withStyle(ChatFormatting.BLUE)));
    }
  }

  private static Component boundPositionName(SpiritrootTetherData.BoundLocation bound) {
    BlockPos pos = bound.pos();
    String coords = pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
    return Component.translatable(
        "tooltip.hexalia.spiritroot_tether.position",
        coords,
        boundDimensionName(bound.dimensionId()));
  }

  private static Component boundDimensionName(String dimensionId) {
    return switch (dimensionId) {
      case "minecraft:overworld" ->
          Component.translatable("tooltip.hexalia.spiritroot_tether.dimension.overworld");
      case "minecraft:the_nether" ->
          Component.translatable("tooltip.hexalia.spiritroot_tether.dimension.nether");
      case "minecraft:the_end" ->
          Component.translatable("tooltip.hexalia.spiritroot_tether.dimension.end");
      default -> Component.literal(dimensionId);
    };
  }

  private static boolean canCapture(Player player, Mob mob) {
    if (mob.getType().is(ModTags.EntityTypes.SPIRITROOT_UNCAPTURABLE)) return false;
    if (mob.isVehicle() || mob.hasPassenger((Entity passenger) -> true)) return false;
    if (mob instanceof OwnableEntity ownable) {
      UUID owner = ownable.getOwnerUUID();
      if (owner != null && !owner.equals(player.getUUID())) return false;
    }
    return mob.isAlive();
  }

  private static String encodeNameJson(Mob mob) {
    Component name = mob.getCustomName();
    if (name == null) return "";
    try {
      JsonElement element = Component.Serializer.toJsonTree(name);
      return element.toString();
    } catch (Exception ignored) {
      return "";
    }
  }

  private static Component getCapturedMobName(SpiritrootTetherData data) {
    Optional<SpiritrootTetherData.CapturedMob> mobOpt = data.mob();
    if (mobOpt.isEmpty())
      return Component.translatable("tooltip.hexalia.spiritroot_tether.unknown");

    SpiritrootTetherData.CapturedMob mob = mobOpt.get();
    if (mob.nameJson() != null && !mob.nameJson().isEmpty()) {
      try {
        JsonElement element = JsonParser.parseString(mob.nameJson());
        Component name = Component.Serializer.fromJson(element);
        if (name != null) return name;
      } catch (Exception ignored) {
      }
    }

    EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(mob.typeId());
    if (type == null) return Component.translatable("tooltip.hexalia.spiritroot_tether.unknown");
    return Component.translatable(type.getDescriptionId());
  }

  private static boolean spawnCapturedMob(
      ServerLevel level, BlockPos pos, SpiritrootTetherData data) {
    Optional<SpiritrootTetherData.CapturedMob> mobOpt = data.mob();
    if (mobOpt.isEmpty()) return false;

    SpiritrootTetherData.CapturedMob mob = mobOpt.get();
    CompoundTag tag;
    try {
      tag = TagParser.parseTag(mob.snbt());
    } catch (Exception ignored) {
      return false;
    }
    tag.putString("id", mob.typeId().toString());

    Entity spawned = EntityType.loadEntityRecursive(tag, level, entity -> entity);
    if (spawned == null) return false;
    spawned.moveTo(
        pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, spawned.getYRot(), spawned.getXRot());
    if (spawned instanceof Mob spawnedMob) spawnedMob.setPersistenceRequired();
    if (level.addFreshEntity(spawned)) return true;

    UUID uuid = mob.uuid();
    if (uuid == null) return false;
    spawned.discard();

    Entity retry = EntityType.loadEntityRecursive(tag, level, entity -> entity);
    if (retry == null) return false;
    retry.setUUID(uuid);
    retry.moveTo(
        pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, retry.getYRot(), retry.getXRot());
    if (retry instanceof Mob retryMob) retryMob.setPersistenceRequired();
    return level.addFreshEntity(retry);
  }
}
