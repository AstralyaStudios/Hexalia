package net.astralya.hexalia.forge.gameplay.censer;

import net.astralya.hexalia.gameplay.censer.CenserEffectHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CenserServerTickHandler {
    private static final int UPDATE_INTERVAL = 5;

    @SubscribeEvent
    public static void onServerTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.level instanceof ServerLevel level)) return;

        if (level.getGameTime() % UPDATE_INTERVAL == 0) {
            CenserEffectHandler.updateEffects(level);
        }
    }
}
