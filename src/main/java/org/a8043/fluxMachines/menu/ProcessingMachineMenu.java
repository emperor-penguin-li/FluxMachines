package org.a8043.fluxMachines.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.a8043.fluxMachines.blockentity.ProcessingMachineBlockEntity;
import org.a8043.fluxMachines.registry.ModMenus;

public class ProcessingMachineMenu extends AbstractContainerMenu {
    private final ProcessingMachineBlockEntity machine;
    private final IItemHandler items;
    private final int inputCount;
    private final int outputSlot;
    private final ContainerData data;
    private int energy, progress, duration;

    public ProcessingMachineMenu(int id, Inventory playerInventory, ProcessingMachineBlockEntity machine, ContainerData data) {
        super(ModMenus.PROCESSING_MACHINE.get(), id);
        this.machine = machine;
        this.items = machine.getInventory();
        this.inputCount = machine.getInputSlotCount();
        this.outputSlot = machine.getOutputSlot();
        this.data = data;
        addDataSlots(data);
        for (int slot = 0; slot < inputCount; slot++) {
            final int index = slot;
            int startX = inputCount == 1 ? 44 : 26;
            addSlot(new SlotItemHandler(items, slot, startX + slot * 18, 35) {
                @Override public boolean mayPlace(@NotNull ItemStack stack) { return machine.isValidInput(index, stack); }
            });
        }
        addSlot(new SlotItemHandler(items, outputSlot, 116, 35) {
            @Override public boolean mayPlace(@NotNull ItemStack stack) { return false; }
        });
        addPlayerInventory(playerInventory);
    }

    public static ProcessingMachineMenu fromNetwork(int id, Inventory inventory, FriendlyByteBuf buffer) {
        if (!(inventory.player.level().getBlockEntity(buffer.readBlockPos()) instanceof ProcessingMachineBlockEntity machine)) throw new IllegalStateException("Missing processing machine");
        return new ProcessingMachineMenu(id, inventory, machine, new SimpleContainerData(4));
    }
    private void addPlayerInventory(Inventory inventory) {
        for (int row = 0; row < 3; row++) for (int col = 0; col < 9; col++) addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
        for (int col = 0; col < 9; col++) addSlot(new Slot(inventory, col, 8 + col * 18, 142));
    }
    @Override public boolean stillValid(Player player) { return player.level().getBlockEntity(machine.getBlockPos()) == machine && player.distanceToSqr(machine.getBlockPos().getX() + .5D, machine.getBlockPos().getY() + .5D, machine.getBlockPos().getZ() + .5D) <= 64D; }
    @Override public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack source = slot.getItem();
        ItemStack copy = source.copy();
        int machineSlots = inputCount + 1;
        if (index < machineSlots) {
            if (!moveItemStackTo(source, machineSlots, slots.size(), true)) return ItemStack.EMPTY;
        } else {
            boolean moved = false;
            for (int i = 0; i < inputCount; i++) if (machine.isValidInput(i, source) && moveItemStackTo(source, i, i + 1, false)) { moved = true; break; }
            if (!moved) return ItemStack.EMPTY;
        }
        if (source.isEmpty()) slot.set(ItemStack.EMPTY); else slot.setChanged();
        return copy;
    }
    public int getEnergy() { return (data.get(0) & 0x7FFF) | ((data.get(1) & 0x7FFF) << 15); }
    public int getCapacity() { return machine.getEnergyCapacity(); }
    public int getProgress() { return data.get(2); }
    public int getDuration() { return data.get(3); }
    public int getInputCount() { return inputCount; }
    public int getMachineKind() { return machine.getMachineKind(); }
}
