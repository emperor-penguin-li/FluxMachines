package org.a8043.fluxMachines.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.a8043.fluxMachines.Main;

public final class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(Main.MOD_ID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void register() {
        CHANNEL.registerMessage(0, SetMultiplierPacket.class, SetMultiplierPacket::encode, SetMultiplierPacket::decode, SetMultiplierPacket::handle);
        CHANNEL.registerMessage(1, SprintKeyPacket.class, SprintKeyPacket::encode, SprintKeyPacket::decode, SprintKeyPacket::handle);
        CHANNEL.registerMessage(2, SetMobSuppressorEnabledPacket.class, SetMobSuppressorEnabledPacket::encode, SetMobSuppressorEnabledPacket::decode, SetMobSuppressorEnabledPacket::handle);
    }

    private NetworkHandler() {
    }
}
