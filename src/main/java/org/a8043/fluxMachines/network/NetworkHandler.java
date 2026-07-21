package org.a8043.fluxMachines.network;

import org.a8043.fluxMachines.Main;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public final class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(Main.MOD_ID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void register() {
        CHANNEL.registerMessage(0, SetMultiplierPacket.class, SetMultiplierPacket::encode, SetMultiplierPacket::decode, SetMultiplierPacket::handle);
    }

    private NetworkHandler() {
    }
}
