package org.a8043.fluxMachines.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Client's current sprint-key state; unlike isSprinting this is not toggle-mode state. */
public record SprintKeyPacket(boolean down) {
    public static void encode(SprintKeyPacket packet, FriendlyByteBuf buf) { buf.writeBoolean(packet.down); }
    public static SprintKeyPacket decode(FriendlyByteBuf buf) { return new SprintKeyPacket(buf.readBoolean()); }
    public static void handle(SprintKeyPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) player.getPersistentData().putBoolean("fluxmachinesSprintKeyDown", packet.down);
        });
        context.setPacketHandled(true);
    }
}
