package org.a8043.fluxMachines.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.a8043.fluxMachines.Main;
import org.a8043.fluxMachines.block.*;

public final class ModBlocks {
    public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MOD_ID);
    public static final RegistryObject<Block> ACCELERATOR = REGISTER.register("accelerator",
        () -> new AcceleratorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 6.0F).sound(SoundType.METAL)));

    public static final RegistryObject<Block> PULVERIZER = REGISTER.register("pulverizer", () -> new ProcessingMachineBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(4.0F).sound(SoundType.METAL)));
    public static final RegistryObject<Block> WIRE_MILL = REGISTER.register("wire_mill", () -> new ProcessingMachineBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(4.0F).sound(SoundType.METAL)));
    public static final RegistryObject<Block> ALLOY_FURNACE = REGISTER.register("alloy_furnace", () -> new ProcessingMachineBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(4.0F).sound(SoundType.METAL)));
    public static final RegistryObject<Block> CHARGING_STATION = REGISTER.register("charging_station",
        () -> new ChargingStationBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(4.0F, 6.0F).sound(SoundType.METAL)
            .requiresCorrectToolForDrops().lightLevel(state -> state.getValue(ChargingStationBlock.LIT) ? 10 : 0)));
    public static final RegistryObject<Block> RESONANT_CRUSHER = advancedMachine("resonant_crusher");
    public static final RegistryObject<Block> LEACHING_REACTOR = advancedMachine("leaching_reactor");
    public static final RegistryObject<Block> ELECTROLYTIC_PURIFIER = advancedMachine("electrolytic_purifier");
    public static final RegistryObject<Block> PLASMA_FURNACE = advancedMachine("plasma_furnace");
    public static final RegistryObject<Block> QUANTUM_ASSEMBLER = advancedMachine("quantum_assembler");
    public static final RegistryObject<Block> MOB_SUPPRESSOR = REGISTER.register("mob_suppressor",
        () -> new MobSuppressorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 6.0F).sound(SoundType.METAL)));
    public static final RegistryObject<Block> SUPPRESSOR_CASING = REGISTER.register("suppressor_casing",
        () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 6.0F).sound(SoundType.METAL).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> SUPPRESSOR_COIL = REGISTER.register("suppressor_coil",
        () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN).strength(4.0F, 5.0F).sound(SoundType.METAL).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> SUPPRESSOR_EMITTER = REGISTER.register("suppressor_emitter",
        () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN).strength(4.0F, 5.0F).sound(SoundType.METAL).requiresCorrectToolForDrops().lightLevel(state -> 7)));
    public static final RegistryObject<Block> TIN_ORE = REGISTER.register("tin_ore", () -> ore(MapColor.STONE, SoundType.STONE));
    public static final RegistryObject<Block> DEEPSLATE_TIN_ORE = REGISTER.register("deepslate_tin_ore", () -> ore(MapColor.DEEPSLATE, SoundType.DEEPSLATE));
    public static final RegistryObject<Block> NICKEL_ORE = REGISTER.register("nickel_ore", () -> ore(MapColor.STONE, SoundType.STONE));
    public static final RegistryObject<Block> DEEPSLATE_NICKEL_ORE = REGISTER.register("deepslate_nickel_ore", () -> ore(MapColor.DEEPSLATE, SoundType.DEEPSLATE));
    public static final RegistryObject<Block> TITANIUM_ORE = REGISTER.register("titanium_ore", () -> ore(MapColor.STONE, SoundType.STONE));
    public static final RegistryObject<Block> DEEPSLATE_TITANIUM_ORE = REGISTER.register("deepslate_titanium_ore", () -> ore(MapColor.DEEPSLATE, SoundType.DEEPSLATE));
    public static final RegistryObject<Block> COBALT_ORE = REGISTER.register("cobalt_ore", () -> ore(MapColor.STONE, SoundType.STONE));
    public static final RegistryObject<Block> DEEPSLATE_COBALT_ORE = REGISTER.register("deepslate_cobalt_ore", () -> ore(MapColor.DEEPSLATE, SoundType.DEEPSLATE));
    public static final RegistryObject<Block> TUNGSTEN_ORE = REGISTER.register("tungsten_ore", () -> ore(MapColor.NETHER, SoundType.NETHER_ORE));
    public static final RegistryObject<Block> OSMIUM_ORE = REGISTER.register("osmium_ore", () -> ore(MapColor.COLOR_BLUE, SoundType.NETHER_ORE));
    public static final RegistryObject<Block> IRIDIUM_ORE = REGISTER.register("iridium_ore", () -> ore(MapColor.COLOR_PURPLE, SoundType.STONE));

    private static RegistryObject<Block> advancedMachine(String name) {
        return REGISTER.register(name, () -> new AdvancedProcessingMachineBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL).strength(8.0F, 12.0F).sound(SoundType.METAL).requiresCorrectToolForDrops()));
    }

    private static Block ore(MapColor color, SoundType sound) {
        return new Block(BlockBehaviour.Properties.of().mapColor(color).strength(3.5F, 3.0F)
            .requiresCorrectToolForDrops().sound(sound));
    }

    private ModBlocks() {
    }
}
