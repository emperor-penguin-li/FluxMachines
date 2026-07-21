package org.a8043.fluxMachines.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class AcceleratorConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.IntValue MAX_MULTIPLIER;
    public static final ForgeConfigSpec.IntValue ENERGY_EXPONENT;
    public static final ForgeConfigSpec.IntValue MAX_CONNECTIONS;
    public static final ForgeConfigSpec.IntValue ENERGY_CAPACITY;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("accelerator");
        MAX_MULTIPLIER = builder.comment("Maximum accelerator multiplier.")
            .defineInRange("maxMultiplier", 40, 1, 10000);
        ENERGY_EXPONENT = builder.comment("Exponent used in the energy cost formula.")
            .defineInRange("energyExponent", 2, 1, 10);
        MAX_CONNECTIONS = builder.comment("Maximum number of bound block entities.")
            .defineInRange("maxConnections", 10, 1, 1024);
        ENERGY_CAPACITY = builder.comment("Internal FE buffer capacity.")
            .defineInRange("energyCapacity", 1_000_000, 1, Integer.MAX_VALUE);
        builder.pop();
        SPEC = builder.build();
    }

    private AcceleratorConfig() {
    }
}
