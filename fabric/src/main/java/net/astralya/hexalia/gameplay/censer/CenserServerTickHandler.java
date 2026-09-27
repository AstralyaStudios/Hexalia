package net.astralya.hexalia.gameplay.censer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class CenserServerTickHandler {
    private static final int UPDATE_INTERVAL = 5;

    public static void register() {
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (world.getGameTime() % UPDATE_INTERVAL == 0) {
                CenserEffectHandler.updateEffects(world);
            }
        });
    }
}
