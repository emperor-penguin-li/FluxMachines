package org.a8043.fluxMachines.config;

import lombok.Getter;
import lombok.Setter;
import net.minecraftforge.common.ForgeConfigSpec;

@Getter
@Setter
public final class FlightRingConfig {
    public static final FlightRingConfig INSTANCE = new FlightRingConfig();
    private final ForgeConfigSpec spec = FluxMachinesConfig.INSTANCE.getSpec();
    private final ForgeConfigSpec.IntValue capacity = FluxMachinesConfig.INSTANCE.getCapacity();
    private final ForgeConfigSpec.IntValue flightCost = FluxMachinesConfig.INSTANCE.getFlightCost();
    private final ForgeConfigSpec.IntValue boostCost = FluxMachinesConfig.INSTANCE.getBoostCost();
    private final ForgeConfigSpec.IntValue fallCost = FluxMachinesConfig.INSTANCE.getFallCost();
    private final ForgeConfigSpec.DoubleValue boostSpeed = FluxMachinesConfig.INSTANCE.getBoostSpeed();

    private FlightRingConfig() {
    }
}
