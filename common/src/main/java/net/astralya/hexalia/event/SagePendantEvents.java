package net.astralya.hexalia.event;

import dev.architectury.event.events.common.TickEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import net.astralya.hexalia.integration.accessories.AccessoriesIntegration;
import net.astralya.hexalia.sound.ModSoundEvents;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.astralya.hexalia.item.ModItems;
import net.astralya.hexalia.item.custom.SagePendantItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class SagePendantEvents {
  private static final Map<Player, Release> ACTIVE_RELEASES = new WeakHashMap<>();
  private static final int PULSE_INTERVAL = 4;
  private static final int XP_PER_PULSE = 10;

  private SagePendantEvents() {}

  public static void register() {
    TickEvent.PLAYER_POST.register(SagePendantEvents::tickRelease);
  }

  public static void startRelease(Player player, ItemStack stack) {
    if (player.level().isClientSide() || SagePendantItem.storedExperience(stack) == 0
        || ACTIVE_RELEASES.containsKey(player)) return;
    if (!(player.level() instanceof ServerLevel server)) return;
    List<UUID> listeners = new ArrayList<>();
    for (ServerPlayer listener : server.players())
      if (listener.distanceToSqr(player) < 1024.0)
        listeners.add(listener.getUUID());
    ACTIVE_RELEASES.put(player, new Release(stack,
        player.level().getGameTime() + PULSE_INTERVAL, listeners));
    player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
        ModSoundEvents.ABSORBING_SOULS.get(), SoundSource.PLAYERS, 0.6F, 1.0F);
  }

  private static void tickRelease(Player player) {
    if (player.level().isClientSide()) return;
    Release release = ACTIVE_RELEASES.get(player);
    if (release == null) return;
    if (!hasPendant(player, release.stack) || SagePendantItem.storedExperience(release.stack) == 0) {
      finishRelease(player);
      return;
    }
    if (player.level().getGameTime() < release.nextPulseTick) return;
    int amount = Math.min(XP_PER_PULSE, SagePendantItem.storedExperience(release.stack));
    SagePendantItem.releaseExperience(release.stack, amount);
    // Direct player XP grants do not pass through the orb pickup storage hook.
    player.giveExperiencePoints(amount);
    if (SagePendantItem.storedExperience(release.stack) == 0) finishRelease(player);
    else release.nextPulseTick += PULSE_INTERVAL;
  }

  private static boolean hasPendant(Player player, ItemStack stack) {
    if (player.getOffhandItem() == stack || player.getMainHandItem() == stack
        || AccessoriesIntegration.getEquippedStack(player, ModItems.SAGE_PENDANT.get()) == stack)
      return true;
    for (ItemStack inventoryStack : player.getInventory().items)
      if (inventoryStack == stack) return true;
    return false;
  }

  private static void finishRelease(Player player) {
    Release release = ACTIVE_RELEASES.remove(player);
    if (release == null || !(player.level() instanceof ServerLevel server)) return;
    var packet = new ClientboundStopSoundPacket(
        ModSoundEvents.ABSORBING_SOULS.get().getLocation(), SoundSource.PLAYERS);
    for (UUID listenerId : release.listeners) {
      ServerPlayer listener = server.getServer().getPlayerList().getPlayer(listenerId);
      if (listener != null) listener.connection.send(packet);
    }
  }

  private static final class Release {
    private final ItemStack stack;
    private long nextPulseTick;
    private final List<UUID> listeners;

    private Release(ItemStack stack, long nextPulseTick, List<UUID> listeners) {
      this.stack = stack;
      this.nextPulseTick = nextPulseTick;
      this.listeners = listeners;
    }
  }

  public static void storeExperience(Player player, int value) {
    if (player.level().isClientSide || value <= 0) return;
    ItemStack pendant = getSagePendant(player);
    if (!pendant.isEmpty()) SagePendantItem.storeBonus(pendant, value);
  }

  private static ItemStack getSagePendant(Player player) {
    ItemStack offhand = player.getOffhandItem();
    if (offhand.is(ModItems.SAGE_PENDANT.get())) return offhand;
    return AccessoriesIntegration.getEquippedStack(player, ModItems.SAGE_PENDANT.get());
  }
}
