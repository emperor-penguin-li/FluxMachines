package org.a8043.fluxMachines.config;

import lombok.Getter;
import lombok.Setter;
import net.minecraftforge.common.ForgeConfigSpec;

/**
 * Shared configuration specification for all FluxMachines settings.
 */
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

    private ForgeConfigSpec.IntValue lifeSupportCapacity;
    private ForgeConfigSpec.IntValue firstAidCost;
    private ForgeConfigSpec.IntValue nutritionCost;
    private ForgeConfigSpec.IntValue forceFieldCost;

    private ForgeConfigSpec.IntValue mobSuppressorRange;
    private ForgeConfigSpec.IntValue mobSuppressorEnergyPerTick;

    private ForgeConfigSpec.IntValue chargingStationEnergyPerTick;
    private ForgeConfigSpec.IntValue chargingStationEnergyCapacity;
    private ForgeConfigSpec.IntValue chargingStationMaxReceive;

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

        builder.push("electricLifeSupportRing");
        lifeSupportCapacity = builder.defineInRange("capacity", 100_000_000, 1, Integer.MAX_VALUE);
        firstAidCost = builder.defineInRange("firstAidCost", 8192, 0, Integer.MAX_VALUE);
        nutritionCost = builder.defineInRange("nutritionCost", 8192, 0, Integer.MAX_VALUE);
        forceFieldCost = builder.defineInRange("forceFieldCost", 8192, 0, Integer.MAX_VALUE);
        builder.pop();

        builder.push("electricFlightRing");
        capacity = builder.defineInRange("capacity", 80_000_000, 1, Integer.MAX_VALUE);
        flightCost = builder.defineInRange("flightCostPerTick", 8192, 0, Integer.MAX_VALUE);
        boostCost = builder.defineInRange("boostAdditionalCostPerTick", 8192, 0, Integer.MAX_VALUE);
        fallCost = builder.defineInRange("fallCostPerBlock", 10, 0, Integer.MAX_VALUE);
        boostSpeed = builder.defineInRange("boostSpeedMultiplier", 1.5D, 1.0D, 10.0D);
        builder.pop();

        builder.push("mobSuppressor");
        mobSuppressorRange = builder.comment("Suppression radius in blocks.")
            .defineInRange("range", 64, 1, 256);
        mobSuppressorEnergyPerTick = builder.comment("FE consumed per tick while the suppressor is active.")
            .defineInRange("energyPerTick", 64, 1, 1_000_000);
        builder.pop();

        builder.push("chargingStation");
        chargingStationEnergyPerTick = builder.comment("Total FE per tick transferred to players standing on the charging station.")
            .defineInRange("energyPerTick", 8192, 0, Integer.MAX_VALUE);
        chargingStationEnergyCapacity = builder.comment("Internal FE buffer capacity.")
            .defineInRange("energyCapacity", 1_000_000, 1, Integer.MAX_VALUE);
        chargingStationMaxReceive = builder.comment("Maximum FE accepted per tick from external energy providers.")
            .defineInRange("maxReceive", 32768, 1, Integer.MAX_VALUE);
        builder.pop();

        spec = builder.build();
    }
}
