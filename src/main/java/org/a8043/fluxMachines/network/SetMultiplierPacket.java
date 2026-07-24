package org.a8043.fluxMachines.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.a8043.fluxMachines.blockentity.AcceleratorBlockEntity;
import org.a8043.fluxMachines.menu.AcceleratorMenu;

import java.util.function.Supplier;

public record SetMultiplierPacket(BlockPos pos, int multiplier) {
    public static void encode(SetMultiplierPacket packet, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.pos);
        buffer.writeVarInt(packet.multiplier);
    }

    public static SetMultiplierPacket decode(FriendlyByteBuf buffer) {
        return new SetMultiplierPacket(buffer.readBlockPos(), buffer.readVarInt());
    }

    public static void handle(SetMultiplierPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null || !(player.containerMenu instanceof AcceleratorMenu menu) || !menu.getBlockPos().equals(packet.pos) || player.distanceToSqr(packet.pos.getX() + .5D, packet.pos.getY() + .5D, packet.pos.getZ() + .5D) > 64D) {
                return;
            }
            if (player.level().getBlockEntity(packet.pos) instanceof AcceleratorBlockEntity accelerator) {
                accelerator.setMultiplier(packet.multiplier);
            }
        });
        context.setPacketHandled(true);
    }
}
