package org.a8043.fluxMachines.config;

import net.minecraftforge.common.ForgeConfigSpec;
import lombok.Getter;
import lombok.Setter;

/** Shared configuration specification for all FluxMachines settings. */
@Getter
@Setter
public final class FluxMachinesConfig {
    public static final FluxMachinesConfig INSTANCE = new FluxMachinesConfig();

    private ForgeConfigSpec spec;

    private ForgeConfigSpec.IntValue maxMultiplier;
    private ForgeConfigSpec.IntValue energyExponent;
    private ForgeConfigSpec.IntValue maxConnections;
    private ForgeConfigSpec.IntValue energyCapacity;

    private ForgeConfigSpec.IntValue capacity;
    private ForgeConfigSpec.IntValue flightCost;
    private ForgeConfigSpec.IntValue boostCost;
    private ForgeConfigSpec.IntValue fallCost;
    private ForgeConfigSpec.DoubleValue boostSpeed;

    private FluxMachinesConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("accelerator");
        maxMultiplier = builder.comment("Maximum accelerator multiplier.")
            .defineInRange("maxMultiplier", 40, 1, 10000);
        energyExponent = builder.comment("Exponent used in the energy cost formula.")
            .defineInRange("energyExponent", 2, 1, 10);
        maxConnections = builder.comment("Maximum number of bound block entities.")
            .defineInRange("maxConnections", 10, 1, 1024);
        energyCapacity = builder.comment("Internal FE buffer capacity.")
            .defineInRange("energyCapacity", 1_000_000, 1, Integer.MAX_VALUE);
        builder.pop();

        builder.push("electricFlightRing");
        capacity = builder.defineInRange("capacity", 80_000_000, 1, Integer.MAX_VALUE);
        flightCost = builder.defineInRange("flightCostPerTick", 8192, 0, Integer.MAX_VALUE);
        boostCost = builder.defineInRange("boostAdditionalCostPerTick", 8192, 0, Integer.MAX_VALUE);
        fallCost = builder.defineInRange("fallCostPerBlock", 10, 0, Integer.MAX_VALUE);
        boostSpeed = builder.defineInRange("boostSpeedMultiplier", 1.5D, 1.0D, 10.0D);
        builder.pop();

        spec = builder.build();
    }
}
