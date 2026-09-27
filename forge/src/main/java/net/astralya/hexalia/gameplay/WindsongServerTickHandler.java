package net.astralya.hexalia.gameplay;

import net.astralya.hexalia.HexaliaMod;
import net.astralya.hexalia.block.entity.custom.WindsongBlockEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HexaliaMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class WindsongServerTickHandler {
    private WindsongServerTickHandler() {}

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.level instanceof ServerLevel level) {
            WindsongBlockEntity.tickDetached(level);
        }
    }
}
