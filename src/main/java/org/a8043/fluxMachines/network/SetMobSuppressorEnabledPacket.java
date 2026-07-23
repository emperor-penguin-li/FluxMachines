package org.a8043.fluxMachines.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.a8043.fluxMachines.blockentity.MobSuppressorBlockEntity;
import org.a8043.fluxMachines.menu.MobSuppressorMenu;

import java.util.function.Supplier;

public record SetMobSuppressorEnabledPacket(BlockPos pos, boolean enabled) {
    public static void encode(SetMobSuppressorEnabledPacket packet, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.pos);
        buffer.writeBoolean(packet.enabled);
    }

    public static SetMobSuppressorEnabledPacket decode(FriendlyByteBuf buffer) {
        return new SetMobSuppressorEnabledPacket(buffer.readBlockPos(), buffer.readBoolean());
    }

    public static void handle(SetMobSuppressorEnabledPacket packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null || !(player.containerMenu instanceof MobSuppressorMenu menu)
                || !menu.getBlockPos().equals(packet.pos)
                || player.distanceToSqr(packet.pos.getX() + .5D, packet.pos.getY() + .5D, packet.pos.getZ() + .5D) > 64D)
                return;
            if (player.level().getBlockEntity(packet.pos) instanceof MobSuppressorBlockEntity suppressor) {
                suppressor.setEnabled(packet.enabled);
            }
        });
        context.setPacketHandled(true);
    }
}
