package org.a8043.fluxMachines.config;

import lombok.Getter;
import net.minecraftforge.common.ForgeConfigSpec;

@Getter
public final class LifeSupportRingConfig {
    public static final LifeSupportRingConfig INSTANCE = new LifeSupportRingConfig();
    private final ForgeConfigSpec.IntValue capacity = FluxMachinesConfig.INSTANCE.getLifeSupportCapacity();
    private final ForgeConfigSpec.IntValue firstAidCost = FluxMachinesConfig.INSTANCE.getFirstAidCost();
    private final ForgeConfigSpec.IntValue nutritionCost = FluxMachinesConfig.INSTANCE.getNutritionCost();
    private final ForgeConfigSpec.IntValue forceFieldCost = FluxMachinesConfig.INSTANCE.getForceFieldCost();

    private LifeSupportRingConfig() {
    }
}
