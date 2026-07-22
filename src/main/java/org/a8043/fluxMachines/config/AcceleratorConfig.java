package org.a8043.fluxMachines.config;

import lombok.Getter;
import lombok.Setter;
import net.minecraftforge.common.ForgeConfigSpec;

@Getter
@Setter
public final class AcceleratorConfig {
    public static final AcceleratorConfig INSTANCE = new AcceleratorConfig();
    private final ForgeConfigSpec spec = FluxMachinesConfig.INSTANCE.getSpec();
    private final ForgeConfigSpec.IntValue maxMultiplier = FluxMachinesConfig.INSTANCE.getMaxMultiplier();
    private final ForgeConfigSpec.IntValue energyExponent = FluxMachinesConfig.INSTANCE.getEnergyExponent();
    private final ForgeConfigSpec.IntValue maxConnections = FluxMachinesConfig.INSTANCE.getMaxConnections();
    private final ForgeConfigSpec.IntValue energyCapacity = FluxMachinesConfig.INSTANCE.getEnergyCapacity();

    private AcceleratorConfig() {}
}
