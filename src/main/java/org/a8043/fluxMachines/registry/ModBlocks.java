package org.a8043.fluxMachines.registry;

import org.a8043.fluxMachines.Main;
import org.a8043.fluxMachines.block.AcceleratorBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlocks {
    public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MOD_ID);
    public static final RegistryObject<Block> ACCELERATOR = REGISTER.register("accelerator",
        () -> new AcceleratorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 6.0F).sound(SoundType.METAL)));

    private ModBlocks() {
    }
}
