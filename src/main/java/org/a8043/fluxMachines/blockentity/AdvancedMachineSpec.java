package org.a8043.fluxMachines.blockentity;

import net.minecraft.world.level.block.state.BlockState;
import org.a8043.fluxMachines.recipe.AdvancedProcessingRecipe;
import org.a8043.fluxMachines.registry.ModBlocks;

public enum AdvancedMachineSpec {
    RESONANT_CRUSHER(AdvancedProcessingRecipe.Machine.RESONANT_CRUSHER, 2, 16_000_000, 16_384, 16_000),
    LEACHING_REACTOR(AdvancedProcessingRecipe.Machine.LEACHING_REACTOR, 3, 64_000_000, 32_768, 32_000),
    ELECTROLYTIC_PURIFIER(AdvancedProcessingRecipe.Machine.ELECTROLYTIC_PURIFIER, 3, 128_000_000, 65_536, 32_000),
    PLASMA_FURNACE(AdvancedProcessingRecipe.Machine.PLASMA_FURNACE, 4, 512_000_000, 262_144, 64_000),
    QUANTUM_ASSEMBLER(AdvancedProcessingRecipe.Machine.QUANTUM_ASSEMBLER, 9, 2_000_000_000, 1_048_576, 64_000);

    private final AdvancedProcessingRecipe.Machine machine;
    private final int inputSlots;
    private final int energyCapacity;
    private final int maxReceive;
    private final int tankCapacity;

    AdvancedMachineSpec(AdvancedProcessingRecipe.Machine machine, int inputSlots, int energyCapacity,
                        int maxReceive, int tankCapacity) {
        this.machine = machine;
        this.inputSlots = inputSlots;
        this.energyCapacity = energyCapacity;
        this.maxReceive = maxReceive;
        this.tankCapacity = tankCapacity;
    }

    public AdvancedProcessingRecipe.Machine machine() {
        return machine;
    }

    public int inputSlots() {
        return inputSlots;
    }

    public int energyCapacity() {
        return energyCapacity;
    }

    public int maxReceive() {
        return maxReceive;
    }

    public int tankCapacity() {
        return tankCapacity;
    }

    public int outputStart() {
        return 9;
    }

    public int totalSlots() {
        return 11;
    }

    public static AdvancedMachineSpec fromState(BlockState state) {
        if (state.is(ModBlocks.LEACHING_REACTOR.get())) {
            return LEACHING_REACTOR;
        }
        if (state.is(ModBlocks.ELECTROLYTIC_PURIFIER.get())) {
            return ELECTROLYTIC_PURIFIER;
        }
        if (state.is(ModBlocks.PLASMA_FURNACE.get())) {
            return PLASMA_FURNACE;
        }
        if (state.is(ModBlocks.QUANTUM_ASSEMBLER.get())) {
            return QUANTUM_ASSEMBLER;
        }
        return RESONANT_CRUSHER;
    }
}
