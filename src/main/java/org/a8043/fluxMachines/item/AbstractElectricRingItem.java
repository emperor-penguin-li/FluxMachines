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

import java.util.List;

public abstract class AbstractElectricRingItem extends Item implements ICurioItem {
    public static final String ENERGY_TAG = "Energy";

    protected AbstractElectricRingItem(Properties properties) {
        super(properties);
    }

    protected abstract int capacity();

    protected abstract int maxReceive();

    public int energy(ItemStack stack) {
        return stack.getOrCreateTag().getInt(ENERGY_TAG);
    }

    public boolean consumeExact(ItemStack stack, int amount) {
        int requested = Math.max(0, amount);
        int stored = energy(stack);
        if (stored < requested) {
            return false;
        }
        if (requested > 0) {
            stack.getOrCreateTag().putInt(ENERGY_TAG, stored - requested);
        }
        return true;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ICapabilityProvider() {
            private final StackEnergyStorage storage = new StackEnergyStorage(stack);
            private final LazyOptional<IEnergyStorage> optional = LazyOptional.of(() -> storage);

            @Override
            public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap,
                                                              @Nullable net.minecraft.core.Direction side) {
                return cap == ForgeCapabilities.ENERGY ? optional.cast() : LazyOptional.empty();
            }
        };
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        return Math.round(13.0F * energy(stack) / Math.max(1, capacity()));
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        return 0x22DDEE;
    }

    protected void addEnergyTooltip(ItemStack stack, List<Component> lines) {
        lines.add(Component.translatable("tooltip.fluxmachines.energy", energy(stack), capacity()).withStyle(ChatFormatting.AQUA));
    }

    private final class StackEnergyStorage extends EnergyStorage {
        private final ItemStack stack;

        private StackEnergyStorage(ItemStack stack) {
            super(capacity(), maxReceive(), 0, energy(stack));
            this.stack = stack;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            energy = energy(stack);
            int amount = super.receiveEnergy(maxReceive, simulate);
            if (!simulate && amount > 0) {
                stack.getOrCreateTag().putInt(ENERGY_TAG, energy);
            }
            return amount;
        }

        @Override
        public int getEnergyStored() {
            energy = AbstractElectricRingItem.this.energy(stack);
            return energy;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return 0;
        }
    }
}
