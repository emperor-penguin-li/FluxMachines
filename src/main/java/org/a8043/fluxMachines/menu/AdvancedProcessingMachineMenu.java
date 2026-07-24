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
import org.a8043.fluxMachines.blockentity.AdvancedProcessingMachineBlockEntity;
import org.a8043.fluxMachines.registry.ModMenus;
import org.jetbrains.annotations.NotNull;

public final class AdvancedProcessingMachineMenu extends AbstractContainerMenu {
    private final AdvancedProcessingMachineBlockEntity machine;
    private final IItemHandler items;
    private final ContainerData data;
    private final int inputCount;
    private final int outputStart;

    public AdvancedProcessingMachineMenu(int id, Inventory playerInventory,
                                         AdvancedProcessingMachineBlockEntity machine, ContainerData data) {
        super(ModMenus.ADVANCED_PROCESSING_MACHINE.get(), id);
        this.machine = machine;
        this.items = machine.getInventory();
        this.data = data;
        inputCount = machine.getSpec().inputSlots();
        outputStart = machine.getSpec().outputStart();
        addDataSlots(data);
        addMachineSlots();
        addPlayerInventory(playerInventory);
    }

    public static AdvancedProcessingMachineMenu fromNetwork(int id, Inventory inventory, FriendlyByteBuf buffer) {
        if (!(inventory.player.level().getBlockEntity(buffer.readBlockPos()) instanceof AdvancedProcessingMachineBlockEntity machine)) {
            throw new IllegalStateException("Missing advanced processing machine");
        }
        return new AdvancedProcessingMachineMenu(id, inventory, machine, new SimpleContainerData(11));
    }

    private void addMachineSlots() {
        for (int slot = 0; slot < 9; slot++) {
            final int index = slot;
            int row = slot / 3;
            int column = slot % 3;
            addSlot(new SlotItemHandler(items, slot, 44 + column * 18, 30 + row * 18) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return index < inputCount && machine.isValidInput(index, stack);
                }
            });
        }
        for (int output = 0; output < 2; output++) {
            addSlot(new SlotItemHandler(items, outputStart + output, 126 + output * 18, 39) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return false;
                }
            });
        }
    }

    private void addPlayerInventory(Inventory inventory) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 104 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(inventory, col, 8 + col * 18, 162));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return player.level().getBlockEntity(machine.getBlockPos()) == machine
               && player.distanceToSqr(machine.getBlockPos().getX() + 0.5D, machine.getBlockPos().getY() + 0.5D,
            machine.getBlockPos().getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack source = slot.getItem();
        ItemStack copy = source.copy();
        int machineSlots = 11;
        if (index < machineSlots) {
            if (!moveItemStackTo(source, machineSlots, slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else {
            boolean moved = false;
            for (int input = 0; input < inputCount; input++) {
                if (machine.isValidInput(input, source) && moveItemStackTo(source, input, input + 1, false)) {
                    moved = true;
                    break;
                }
            }
            if (!moved) {
                return ItemStack.EMPTY;
            }
        }
        if (source.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return copy;
    }

    public int getEnergy() {
        return (data.get(0) & 0x7FFF) | ((data.get(1) & 0x7FFF) << 15) | ((data.get(2) & 0x3) << 30);
    }

    public int getCapacity() {
        return machine.getEnergyCapacity();
    }

    public int getProgress() {
        return data.get(3);
    }

    public int getDuration() {
        return data.get(4);
    }

    public int getTankAmount(int tank) {
        return data.get(5 + tank);
    }

    public int getTankCapacity() {
        return machine.getTankCapacity();
    }

    public int getEnergyPerTick() {
        return (data.get(9) & 0x7FFF) | ((data.get(10) & 0x7FFF) << 15);
    }

    public int getMachineOrdinal() {
        return machine.getSpec().ordinal();
    }
}
