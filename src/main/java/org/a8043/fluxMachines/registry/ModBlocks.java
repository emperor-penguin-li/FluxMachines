package org.a8043.fluxMachines.registry;

import org.a8043.fluxMachines.Main;
import org.a8043.fluxMachines.block.AcceleratorBlock;
import org.a8043.fluxMachines.block.ProcessingMachineBlock;
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

    public static final RegistryObject<Block> PULVERIZER = REGISTER.register("pulverizer", () -> new ProcessingMachineBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(4.0F).sound(SoundType.METAL)));
    public static final RegistryObject<Block> WIRE_MILL = REGISTER.register("wire_mill", () -> new ProcessingMachineBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(4.0F).sound(SoundType.METAL)));
    public static final RegistryObject<Block> ALLOY_FURNACE = REGISTER.register("alloy_furnace", () -> new ProcessingMachineBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(4.0F).sound(SoundType.METAL)));
    public static final RegistryObject<Block> TIN_ORE = REGISTER.register("tin_ore", () -> ore(MapColor.STONE, SoundType.STONE));
    public static final RegistryObject<Block> DEEPSLATE_TIN_ORE = REGISTER.register("deepslate_tin_ore", () -> ore(MapColor.DEEPSLATE, SoundType.DEEPSLATE));
    public static final RegistryObject<Block> NICKEL_ORE = REGISTER.register("nickel_ore", () -> ore(MapColor.STONE, SoundType.STONE));
    public static final RegistryObject<Block> DEEPSLATE_NICKEL_ORE = REGISTER.register("deepslate_nickel_ore", () -> ore(MapColor.DEEPSLATE, SoundType.DEEPSLATE));

    private static Block ore(MapColor color, SoundType sound) {
        return new Block(BlockBehaviour.Properties.of().mapColor(color).strength(3.5F, 3.0F)
            .requiresCorrectToolForDrops().sound(sound));
    }

    private ModBlocks() {
    }
}
