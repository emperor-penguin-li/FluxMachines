package org.a8043.fluxMachines.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.a8043.fluxMachines.block.ChargingStationBlock;
import org.a8043.fluxMachines.config.FluxMachinesConfig;
import org.a8043.fluxMachines.registry.ModBlockEntities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;

public class ChargingStationBlockEntity extends BlockEntity {
    private static final String ENERGY_TAG = "Energy";

    private final StationEnergyStorage energy = new StationEnergyStorage();
    private final LazyOptional<IEnergyStorage> energyCapability = LazyOptional.of(() -> energy);
    private boolean active;

    private final class StationEnergyStorage extends EnergyStorage {
        private StationEnergyStorage() {
            super(getConfiguredCapacity(), getConfiguredMaxReceive(), 0);
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int received = super.receiveEnergy(maxReceive, simulate);
            if (received > 0 && !simulate) setChanged();
            return received;
        }

        private int consume(int amount) {
            int consumed = Math.min(Math.max(0, amount), energy);
            if (consumed > 0) {
                energy -= consumed;
                setChanged();
            }
            return consumed;
        }
    }

    public ChargingStationBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CHARGING_STATION.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ChargingStationBlockEntity station) {
        if (level.isClientSide) return;

        List<Player> players = level.getEntitiesOfClass(Player.class, new AABB(pos.above()), player ->
            !player.isSpectator() && player.onGround() && player.blockPosition().below().equals(pos));
        int available = Math.min(station.getEnergyPerTick(), station.energy.getEnergyStored());
        int remaining = available;
        for (Player player : players) {
            if (remaining <= 0) break;
            remaining -= station.chargePlayer(player, remaining);
        }

        int charged = available - remaining;
        station.energy.consume(charged);
        station.setActive(!players.isEmpty() && (charged > 0 || station.energy.getEnergyStored() > 0));
    }

    private int chargePlayer(Player player, int maxAmount) {
        int remaining = maxAmount;
        remaining -= chargeStacks(player.getInventory().items, remaining);
        remaining -= chargeStacks(player.getInventory().armor, remaining);
        remaining -= chargeStacks(player.getInventory().offhand, remaining);
        remaining -= chargeCurios(player, remaining);
        return maxAmount - remaining;
    }

    private int chargeStacks(Iterable<ItemStack> stacks, int maxAmount) {
        int remaining = maxAmount;
        for (ItemStack stack : stacks) {
            if (remaining <= 0) break;
            remaining -= chargeStack(stack, remaining);
        }
        return maxAmount - remaining;
    }

    private int chargeCurios(Player player, int maxAmount) {
        int[] remaining = {maxAmount};
        CuriosApi.getCuriosInventory(player).resolve().ifPresent(handler ->
            handler.getCurios().values().forEach(stacksHandler -> {
                var stacks = stacksHandler.getStacks();
                for (int slot = 0; slot < stacks.getSlots() && remaining[0] > 0; slot++) {
                    remaining[0] -= chargeStack(stacks.getStackInSlot(slot), remaining[0]);
                }
            }));
        return maxAmount - remaining[0];
    }

    private int chargeStack(ItemStack stack, int maxAmount) {
        if (stack.isEmpty() || maxAmount <= 0) return 0;
        return stack.getCapability(ForgeCapabilities.ENERGY)
            .map(storage -> storage.canReceive() ? Math.max(0, storage.receiveEnergy(maxAmount, false)) : 0)
            .orElse(0);
    }

    private void setActive(boolean value) {
        if (active == value) return;
        active = value;
        setChanged();
        BlockState state = getBlockState();
        if (level != null && state.hasProperty(ChargingStationBlock.LIT) && state.getValue(ChargingStationBlock.LIT) != value) {
            level.setBlock(worldPosition, state.setValue(ChargingStationBlock.LIT, value), 3);
        }
    }

    private int getEnergyPerTick() {
        return FluxMachinesConfig.INSTANCE.getChargingStationEnergyPerTick().get();
    }

    private static int getConfiguredCapacity() {
        return FluxMachinesConfig.INSTANCE.getChargingStationEnergyCapacity().get();
    }

    private static int getConfiguredMaxReceive() {
        return FluxMachinesConfig.INSTANCE.getChargingStationMaxReceive().get();
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        if (tag.contains(ENERGY_TAG)) energy.deserializeNBT(tag.get(ENERGY_TAG));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(ENERGY_TAG, energy.serializeNBT());
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        energyCapability.invalidate();
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable net.minecraft.core.Direction side) {
        if (capability == ForgeCapabilities.ENERGY) return energyCapability.cast();
        return super.getCapability(capability, side);
    }
}
