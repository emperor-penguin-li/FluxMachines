package org.a8043.fluxMachines.network;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.a8043.fluxMachines.Main;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, value = Dist.CLIENT)
public final class ClientSprintKeyState {
    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            NetworkHandler.CHANNEL.sendToServer(new SprintKeyPacket(minecraft.options.keySprint.isDown()));
        }
    }

    private ClientSprintKeyState() {
    }
}
