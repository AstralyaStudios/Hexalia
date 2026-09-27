package net.astralya.hexalia.gameplay;

import net.astralya.hexalia.block.entity.custom.WindsongBlockEntity;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public final class WindsongServerTickHandler {
    private WindsongServerTickHandler() {}

    public static void register() {
        ServerTickEvents.END_WORLD_TICK.register(WindsongBlockEntity::tickDetached);
    }
}
