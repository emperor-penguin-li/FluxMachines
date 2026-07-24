package org.a8043.fluxMachines.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.a8043.fluxMachines.config.LifeSupportRingConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class ElectricLifeSupportRingItem extends AbstractElectricRingItem {
    public ElectricLifeSupportRingItem(Properties properties) {
        super(properties);
    }

    @Override
    protected int capacity() {
        return LifeSupportRingConfig.INSTANCE.getCapacity().get();
    }

    @Override
    protected int maxReceive() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable net.minecraft.world.level.Level level,
                                @NotNull List<Component> lines, @NotNull net.minecraft.world.item.TooltipFlag flag) {
        addEnergyTooltip(stack, lines);
        lines.add(Component.translatable("tooltip.fluxmachines.life_support_heal",
            LifeSupportRingConfig.INSTANCE.getFirstAidCost().get()).withStyle(ChatFormatting.GRAY));
        lines.add(Component.translatable("tooltip.fluxmachines.life_support_food",
            LifeSupportRingConfig.INSTANCE.getNutritionCost().get()).withStyle(ChatFormatting.GRAY));
        lines.add(Component.translatable("tooltip.fluxmachines.life_support_shield",
            LifeSupportRingConfig.INSTANCE.getForceFieldCost().get()).withStyle(ChatFormatting.GRAY));
    }
}
