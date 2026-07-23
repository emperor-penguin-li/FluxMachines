package org.a8043.fluxMachines.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.a8043.fluxMachines.blockentity.MobSuppressorBlockEntity;
import org.a8043.fluxMachines.registry.ModMenus;
import org.jetbrains.annotations.NotNull;

public class MobSuppressorMenu extends AbstractContainerMenu {
    private final MobSuppressorBlockEntity suppressor;
    private final BlockPos blockPos;
    private final ContainerData data;

    public MobSuppressorMenu(int id, Inventory inventory, MobSuppressorBlockEntity suppressor, ContainerData data) {
        super(ModMenus.MOB_SUPPRESSOR.get(), id);
        this.suppressor = suppressor;
        this.blockPos = suppressor.getBlockPos();
        this.data = data;
        checkContainerDataCount(data, 8);
        addDataSlots(data);
    }

    public static MobSuppressorMenu fromNetwork(int id, Inventory inventory, FriendlyByteBuf buffer) {
        BlockPos pos = buffer.readBlockPos();
        BlockEntity entity = inventory.player.level().getBlockEntity(pos);
        if (!(entity instanceof MobSuppressorBlockEntity suppressor))
            throw new IllegalStateException("Missing mob suppressor at " + pos);
        return new MobSuppressorMenu(id, inventory, suppressor, new SimpleContainerData(8));
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public int getEnergyStored() {
        return data.get(0) | (data.get(1) << 15);
    }

    public int getEnergyCapacity() {
        return data.get(2) | (data.get(3) << 15);
    }

    public int getEnergyPerTick() {
        return data.get(4) | (data.get(5) << 15);
    }

    public int getRange() {
        return data.get(6);
    }

    public boolean isEnabled() {
        return (data.get(7) & 1) != 0;
    }

    public boolean isFormed() {
        return (data.get(7) & 2) != 0;
    }

    public boolean isActive() {
        return (data.get(7) & 4) != 0;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return player.level().getBlockEntity(blockPos) == suppressor
               && player.distanceToSqr(blockPos.getX() + .5D, blockPos.getY() + .5D, blockPos.getZ() + .5D) <= 64D;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slot) {
        return ItemStack.EMPTY;
    }
}
