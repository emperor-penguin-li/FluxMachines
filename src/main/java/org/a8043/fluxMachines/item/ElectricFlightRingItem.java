package org.a8043.fluxMachines.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.a8043.fluxMachines.config.FlightRingConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class ElectricFlightRingItem extends AbstractElectricRingItem {
    public ElectricFlightRingItem(Properties properties) {
        super(properties);
    }

    @Override
    protected int capacity() {
        return FlightRingConfig.INSTANCE.getCapacity().get();
    }

    @Override
    protected int maxReceive() {
        return Integer.MAX_VALUE;
    }

    public static int getEnergy(ItemStack stack) {
        return stack.getOrCreateTag().getInt(ENERGY_TAG);
    }

    public static int consume(ItemStack stack, int amount) {
        int used = Math.min(Math.max(amount, 0), getEnergy(stack));
        if (used > 0) {
            stack.getOrCreateTag().putInt(ENERGY_TAG, getEnergy(stack) - used);
        }
        return used;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable net.minecraft.world.level.Level level,
                                @NotNull List<Component> lines, @NotNull net.minecraft.world.item.TooltipFlag flag) {
        addEnergyTooltip(stack, lines);
        lines.add(Component.translatable("tooltip.fluxmachines.flight_ring_cost",
                FlightRingConfig.INSTANCE.getFlightCost().get(), FlightRingConfig.INSTANCE.getBoostCost().get())
            .withStyle(ChatFormatting.GRAY));
        lines.add(Component.translatable("tooltip.fluxmachines.flight_ring_fall",
            FlightRingConfig.INSTANCE.getFallCost().get()).withStyle(ChatFormatting.GRAY));
    }
}
