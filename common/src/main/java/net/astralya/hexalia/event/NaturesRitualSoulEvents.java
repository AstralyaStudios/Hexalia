package net.astralya.hexalia.event;

import dev.architectury.event.events.common.EntityEvent;
import net.astralya.hexalia.gameplay.naturesritual.SummoningRitual;

public final class NaturesRitualSoulEvents {
  private NaturesRitualSoulEvents() {}

  public static void register() {
    EntityEvent.LIVING_DEATH.register(SummoningRitual::onLivingDeath);
  }
}
