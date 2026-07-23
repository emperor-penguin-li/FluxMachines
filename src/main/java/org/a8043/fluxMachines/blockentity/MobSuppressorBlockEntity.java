package org.a8043.fluxMachines.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.a8043.fluxMachines.MobSuppressorEvents;
import org.a8043.fluxMachines.block.MobSuppressorBlock;
import org.a8043.fluxMachines.config.FluxMachinesConfig;
import org.a8043.fluxMachines.menu.MobSuppressorMenu;
import org.a8043.fluxMachines.registry.ModBlockEntities;
import org.a8043.fluxMachines.registry.ModBlocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MobSuppressorBlockEntity extends BlockEntity implements MenuProvider {
    public static final int ENERGY_CAPACITY = 100_000;
    private static final String ENERGY_TAG = "Energy";
    private static final String ENABLED_TAG = "Enabled";

    private final SuppressorEnergyStorage energy = new SuppressorEnergyStorage();

    private final class SuppressorEnergyStorage extends EnergyStorage {
        private SuppressorEnergyStorage() {
            super(ENERGY_CAPACITY, 4096, 0);
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int received = super.receiveEnergy(maxReceive, simulate);
            if (received > 0 && !simulate) setChanged();
            return received;
        }

        private void consume(int amount) {
            energy -= amount;
            setChanged();
        }
    }

    private final LazyOptional<IEnergyStorage> energyCapability = LazyOptional.of(() -> energy);
    private boolean enabled = true;
    private boolean formed;
    private boolean active;

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            int cost = getEnergyPerTick();
            return switch (index) {
                case 0 -> energy.getEnergyStored() & 0x7FFF;
                case 1 -> (energy.getEnergyStored() >>> 15) & 0x7FFF;
                case 2 -> ENERGY_CAPACITY & 0x7FFF;
                case 3 -> (ENERGY_CAPACITY >>> 15) & 0x7FFF;
                case 4 -> cost & 0x7FFF;
                case 5 -> (cost >>> 15) & 0x7FFF;
                case 6 -> getRange();
                case 7 -> (enabled ? 1 : 0) | (formed ? 2 : 0) | (active ? 4 : 0);
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
        }

        @Override
        public int getCount() {
            return 8;
        }
    };

    public MobSuppressorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MOB_SUPPRESSOR.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, MobSuppressorBlockEntity suppressor) {
        if (level.isClientSide) return;
        suppressor.refreshStructure();
        int cost = suppressor.getEnergyPerTick();
        boolean shouldRun = suppressor.enabled && suppressor.formed && suppressor.energy.getEnergyStored() >= cost;
        if (shouldRun) suppressor.energy.consume(cost);
        suppressor.setActive(shouldRun);
    }

    public void refreshStructure() {
        if (level == null || level.isClientSide) return;
        boolean newFormed = validateStructure();
        if (formed != newFormed) {
            formed = newFormed;
            setChanged();
        }
    }

    private boolean validateStructure() {
        Direction front = getBlockState().getValue(MobSuppressorBlock.FACING);
        Direction back = front.getOpposite();
        Direction right = front.getClockWise();

        for (int z = 0; z <= 2; z++) {
            for (int x = -1; x <= 1; x++) {
                if (z == 0 && x == 0) continue;
                if (!level.getBlockState(offset(right, back, x, 0, z)).is(ModBlocks.SUPPRESSOR_CASING.get()))
                    return false;
            }
        }
        for (int y = 1; y <= 3; y++) {
            if (!level.getBlockState(offset(right, back, -1, y, 0)).is(ModBlocks.SUPPRESSOR_CASING.get())
                || !level.getBlockState(offset(right, back, 1, y, 0)).is(ModBlocks.SUPPRESSOR_CASING.get())
                || !level.getBlockState(offset(right, back, -1, y, 2)).is(ModBlocks.SUPPRESSOR_CASING.get())
                || !level.getBlockState(offset(right, back, 1, y, 2)).is(ModBlocks.SUPPRESSOR_CASING.get())
                || !level.getBlockState(offset(right, back, 0, y, 1)).is(ModBlocks.SUPPRESSOR_COIL.get())) return false;
        }
        return level.getBlockState(offset(right, back, 0, 4, 1)).is(ModBlocks.SUPPRESSOR_EMITTER.get())
               && level.getBlockState(offset(right, back, -1, 4, 1)).is(ModBlocks.SUPPRESSOR_EMITTER.get())
               && level.getBlockState(offset(right, back, 1, 4, 1)).is(ModBlocks.SUPPRESSOR_EMITTER.get())
               && level.getBlockState(offset(right, back, 0, 4, 0)).is(ModBlocks.SUPPRESSOR_EMITTER.get())
               && level.getBlockState(offset(right, back, 0, 4, 2)).is(ModBlocks.SUPPRESSOR_EMITTER.get());
    }

    private BlockPos offset(Direction right, Direction back, int x, int y, int z) {
        return worldPosition.offset(right.getStepX() * x + back.getStepX() * z, y, right.getStepZ() * x + back.getStepZ() * z);
    }

    private void setActive(boolean value) {
        if (active == value) return;
        active = value;
        setChanged();
        BlockState state = getBlockState();
        if (state.hasProperty(MobSuppressorBlock.LIT) && state.getValue(MobSuppressorBlock.LIT) != value) {
            level.setBlock(worldPosition, state.setValue(MobSuppressorBlock.LIT, value), 3);
        }
    }

    public boolean isSuppressing() {
        return active && enabled && formed && !isRemoved();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isFormed() {
        return formed;
    }

    public boolean isActive() {
        return active;
    }

    public int getEnergyStored() {
        return energy.getEnergyStored();
    }

    public int getEnergyPerTick() {
        return FluxMachinesConfig.INSTANCE.getMobSuppressorEnergyPerTick().get();
    }

    public int getRange() {
        return FluxMachinesConfig.INSTANCE.getMobSuppressorRange().get();
    }

    public ContainerData getData() {
        return data;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        if (!enabled) setActive(false);
        setChanged();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level != null && !level.isClientSide) MobSuppressorEvents.register(this);
    }

    @Override
    public void setRemoved() {
        MobSuppressorEvents.unregister(this);
        super.setRemoved();
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        if (tag.contains(ENERGY_TAG)) energy.deserializeNBT(tag.get(ENERGY_TAG));
        enabled = !tag.contains(ENABLED_TAG) || tag.getBoolean(ENABLED_TAG);
        active = false;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(ENERGY_TAG, energy.serializeNBT());
        tag.putBoolean(ENABLED_TAG, enabled);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        energyCapability.invalidate();
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
        if (capability == ForgeCapabilities.ENERGY) return energyCapability.cast();
        return super.getCapability(capability, side);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.fluxmachines.mob_suppressor");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        return new MobSuppressorMenu(id, inventory, this, data);
    }
}
