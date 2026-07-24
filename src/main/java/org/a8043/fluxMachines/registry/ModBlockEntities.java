package org.a8043.fluxMachines.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.a8043.fluxMachines.Main;
import org.a8043.fluxMachines.blockentity.AcceleratorBlockEntity;
import org.a8043.fluxMachines.blockentity.AdvancedProcessingMachineBlockEntity;
import org.a8043.fluxMachines.blockentity.MobSuppressorBlockEntity;
import org.a8043.fluxMachines.blockentity.ProcessingMachineBlockEntity;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Main.MOD_ID);
    public static final RegistryObject<BlockEntityType<AcceleratorBlockEntity>> ACCELERATOR = REGISTER.register("accelerator",
        () -> BlockEntityType.Builder.of(AcceleratorBlockEntity::new, ModBlocks.ACCELERATOR.get()).build(null));
    public static final RegistryObject<BlockEntityType<ProcessingMachineBlockEntity>> PROCESSING_MACHINE = REGISTER.register("processing_machine",
        () -> BlockEntityType.Builder.of(ProcessingMachineBlockEntity::new, ModBlocks.PULVERIZER.get(), ModBlocks.WIRE_MILL.get(), ModBlocks.ALLOY_FURNACE.get()).build(null));
    public static final RegistryObject<BlockEntityType<MobSuppressorBlockEntity>> MOB_SUPPRESSOR = REGISTER.register("mob_suppressor",
        () -> BlockEntityType.Builder.of(MobSuppressorBlockEntity::new, ModBlocks.MOB_SUPPRESSOR.get()).build(null));
    public static final RegistryObject<BlockEntityType<AdvancedProcessingMachineBlockEntity>> ADVANCED_PROCESSING_MACHINE =
        REGISTER.register("advanced_processing_machine", () -> BlockEntityType.Builder.of(
            AdvancedProcessingMachineBlockEntity::new, ModBlocks.RESONANT_CRUSHER.get(), ModBlocks.LEACHING_REACTOR.get(),
            ModBlocks.ELECTROLYTIC_PURIFIER.get(), ModBlocks.PLASMA_FURNACE.get(), ModBlocks.QUANTUM_ASSEMBLER.get()).build(null));

    private ModBlockEntities() {
    }
}
