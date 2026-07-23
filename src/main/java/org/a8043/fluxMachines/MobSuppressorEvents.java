package org.a8043.fluxMachines;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.a8043.fluxMachines.blockentity.MobSuppressorBlockEntity;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = Main.MOD_ID)
public final class MobSuppressorEvents {
    private static final Set<MobSuppressorBlockEntity> LOADED = Collections.newSetFromMap(new WeakHashMap<>());

    public static void register(MobSuppressorBlockEntity suppressor) {
        LOADED.add(suppressor);
    }

    public static void unregister(MobSuppressorBlockEntity suppressor) {
        LOADED.remove(suppressor);
    }

    @SubscribeEvent
    public static void checkSpawn(MobSpawnEvent.SpawnPlacementCheck event) {
        if (event.getSpawnType() != MobSpawnType.NATURAL || event.getEntityType().getCategory() != MobCategory.MONSTER
            || !(event.getLevel() instanceof ServerLevel level)) return;

        for (MobSuppressorBlockEntity suppressor : LOADED.toArray(MobSuppressorBlockEntity[]::new)) {
            if (suppressor.getLevel() != level || !suppressor.isSuppressing()) continue;
            long range = suppressor.getRange();
            if (suppressor.getBlockPos().distSqr(event.getPos()) <= range * range) {
                event.setResult(Event.Result.DENY);
                return;
            }
        }
    }

    private MobSuppressorEvents() {
    }
}
