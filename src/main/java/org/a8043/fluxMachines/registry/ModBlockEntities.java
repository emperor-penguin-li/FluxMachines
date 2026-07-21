package org.a8043.fluxMachines.registry;

import org.a8043.fluxMachines.Main;
import org.a8043.fluxMachines.blockentity.AcceleratorBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Main.MOD_ID);
    public static final RegistryObject<BlockEntityType<AcceleratorBlockEntity>> ACCELERATOR = REGISTER.register("accelerator",
        () -> BlockEntityType.Builder.of(AcceleratorBlockEntity::new, ModBlocks.ACCELERATOR.get()).build(null));

    private ModBlockEntities() {
    }
}
