package org.a8043.fluxMachines.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import org.a8043.fluxMachines.config.FlightRingConfig;

import java.util.List;

public class ElectricFlightRingItem extends Item implements ICurioItem {
    public static final String ENERGY_TAG = "Energy";

    public ElectricFlightRingItem(Properties properties) { super(properties); }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ICapabilityProvider() {
            private final StackEnergy energy = new StackEnergy(stack);
            private final LazyOptional<IEnergyStorage> optional = LazyOptional.of(() -> energy);
            @Override public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable net.minecraft.core.Direction side) {
                return cap == ForgeCapabilities.ENERGY ? optional.cast() : LazyOptional.empty();
            }
        };
    }

    public static int getEnergy(ItemStack stack) { return stack.getOrCreateTag().getInt(ENERGY_TAG); }
    public static int getCapacity() { return FlightRingConfig.INSTANCE.getCapacity().get(); }
    public static int consume(ItemStack stack, int amount) {
        int used = Math.min(Math.max(amount, 0), getEnergy(stack));
        if (used > 0) stack.getOrCreateTag().putInt(ENERGY_TAG, getEnergy(stack) - used);
        return used;
    }

    @Override public boolean isBarVisible(ItemStack stack) { return true; }
    @Override public int getBarWidth(ItemStack stack) { return Math.round(13.0F * getEnergy(stack) / getCapacity()); }
    @Override public int getBarColor(ItemStack stack) { return 0x22DDEE; }
    @Override public void appendHoverText(ItemStack stack, @Nullable net.minecraft.world.level.Level level, List<Component> lines, net.minecraft.world.item.TooltipFlag flag) {
        lines.add(Component.translatable("tooltip.fluxmachines.energy", getEnergy(stack), getCapacity()).withStyle(ChatFormatting.AQUA));
        lines.add(Component.translatable("tooltip.fluxmachines.flight_ring_cost", FlightRingConfig.INSTANCE.getFlightCost().get(), FlightRingConfig.INSTANCE.getBoostCost().get()).withStyle(ChatFormatting.GRAY));
        lines.add(Component.translatable("tooltip.fluxmachines.flight_ring_fall", FlightRingConfig.INSTANCE.getFallCost().get()).withStyle(ChatFormatting.GRAY));
    }

    private static final class StackEnergy extends EnergyStorage {
        private final ItemStack stack;
        StackEnergy(ItemStack stack) { super(FlightRingConfig.INSTANCE.getCapacity().get(), Integer.MAX_VALUE, 0, getEnergy(stack)); this.stack = stack; }
        @Override public int receiveEnergy(int maxReceive, boolean simulate) {
            int amount = super.receiveEnergy(maxReceive, simulate);
            if (!simulate && amount > 0) stack.getOrCreateTag().putInt(ENERGY_TAG, energy);
            return amount;
        }
        @Override public int getEnergyStored() { energy = getEnergy(stack); return energy; }
        @Override public int extractEnergy(int maxExtract, boolean simulate) { return 0; }
    }
}
