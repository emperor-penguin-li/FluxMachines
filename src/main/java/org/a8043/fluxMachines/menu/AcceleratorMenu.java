package org.a8043.fluxMachines.menu;

import lombok.Getter;
import org.a8043.fluxMachines.blockentity.AcceleratorBlockEntity;
import org.a8043.fluxMachines.registry.ModMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@Getter
public class AcceleratorMenu extends AbstractContainerMenu {
    private final AcceleratorBlockEntity accelerator;
    private final BlockPos blockPos;
    private int energyStored;
    private int energyCapacity;
    private int multiplier;
    private int connectionCount;
    private int energyCost;

    public AcceleratorMenu(int id, AcceleratorBlockEntity accelerator) {
        super(ModMenus.ACCELERATOR.get(), id);
        this.accelerator = accelerator;
        this.blockPos = accelerator.getBlockPos();
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return AcceleratorMenu.this.accelerator.getEnergyStored();
            }

            @Override
            public void set(int value) {
                energyStored = value;
            }
        });
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return AcceleratorMenu.this.accelerator.getEnergyCapacity();
            }

            @Override
            public void set(int value) {
                energyCapacity = value;
            }
        });
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return AcceleratorMenu.this.accelerator.getMultiplier();
            }

            @Override
            public void set(int value) {
                multiplier = value;
            }
        });
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return AcceleratorMenu.this.accelerator.getConnectionCount();
            }

            @Override
            public void set(int value) {
                connectionCount = value;
            }
        });
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return AcceleratorMenu.this.accelerator.getEnergyCost();
            }

            @Override
            public void set(int value) {
                energyCost = value;
            }
        });
    }

    public static AcceleratorMenu fromNetwork(int id, Inventory inventory, FriendlyByteBuf buffer) {
        BlockPos pos = buffer.readBlockPos();
        BlockEntity entity = inventory.player.level().getBlockEntity(pos);
        if (!(entity instanceof AcceleratorBlockEntity accelerator)) {
            throw new IllegalStateException("Missing accelerator at " + pos);
        }
        return new AcceleratorMenu(id, accelerator);
    }

    @Override
    public boolean stillValid(Player player) {
        return player.level().getBlockEntity(blockPos) == accelerator && player.distanceToSqr(blockPos.getX() + .5D, blockPos.getY() + .5D, blockPos.getZ() + .5D) <= 64D;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slot) {
        return ItemStack.EMPTY;
    }
}
