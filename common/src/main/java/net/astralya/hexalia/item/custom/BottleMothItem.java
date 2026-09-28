package net.astralya.hexalia.item.custom;

import java.util.List;
import net.astralya.hexalia.entity.ModEntities;
import net.astralya.hexalia.entity.custom.SilkMothEntity;
import net.astralya.hexalia.entity.custom.variant.SilkMothVariant;
import net.astralya.hexalia.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class BottleMothItem extends Item {

  public static final String MOTH_NAME = "MothName";

  public BottleMothItem(Properties pProperties) {
    super(pProperties);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Level world = context.getLevel();
    Player player = context.getPlayer();
    ItemStack itemStack = context.getItemInHand();
    BlockPos blockPos = context.getClickedPos();
    Direction direction = context.getClickedFace();

    if (!world.isClientSide && player != null) {
      BlockPos spawnPos = blockPos.relative(direction);

      SilkMothEntity silkMothEntity = new SilkMothEntity(ModEntities.SILK_MOTH_ENTITY.get(), world);
      CompoundTag nbt = itemStack.getTag();

      if (nbt != null) {
        if (nbt.contains("SilkMothVariant")) {
          silkMothEntity.setVariant(SilkMothVariant.byId(nbt.getInt("SilkMothVariant")));
        }
        if (nbt.contains(MOTH_NAME)) {
          silkMothEntity.setCustomName(Component.literal(nbt.getString(MOTH_NAME)));
        }
      }

      silkMothEntity.moveTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, 0, 0);
      world.addFreshEntity(silkMothEntity);

      world.playSound(
          null,
          player.getX(),
          player.getY(),
          player.getZ(),
          SoundEvents.ITEM_PICKUP,
          SoundSource.PLAYERS,
          1.0F,
          1.0F);

      itemStack.shrink(1);
      player.getInventory().add(new ItemStack(ModItems.RUSTIC_BOTTLE.get()));

      return InteractionResult.SUCCESS;
    }
    return InteractionResult.PASS;
  }

  @Override
  public void appendHoverText(
      ItemStack pStack,
      @Nullable Level pLevel,
      List<Component> pTooltipComponents,
      TooltipFlag pIsAdvanced) {
    super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    if (pStack.hasTag() && pStack.getTag() != null) {
      CompoundTag pTag = pStack.getTag();

      if (pTag.contains(MOTH_NAME)) {
        String mothName = pTag.getString(MOTH_NAME);

        MutableComponent nameTooltip =
            Component.translatable("tooltip.hexalia.bottled_moth", mothName)
                .withStyle(Style.EMPTY.withItalic(true).withColor(0x55FF55));
        pTooltipComponents.add(nameTooltip);
      }
    }
  }
}
