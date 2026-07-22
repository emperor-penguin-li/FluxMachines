package org.a8043.fluxMachines.registry;

import org.a8043.fluxMachines.Main;
import org.a8043.fluxMachines.item.AcceleratorConnectorItem;
import org.a8043.fluxMachines.item.ElectricFlightRingItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MOD_ID);
    public static final RegistryObject<Item> ACCELERATOR = REGISTER.register("accelerator",
        () -> new BlockItem(ModBlocks.ACCELERATOR.get(), new Item.Properties()));
    public static final RegistryObject<Item> PULVERIZER = REGISTER.register("pulverizer", () -> new BlockItem(ModBlocks.PULVERIZER.get(), new Item.Properties()));
    public static final RegistryObject<Item> WIRE_MILL = REGISTER.register("wire_mill", () -> new BlockItem(ModBlocks.WIRE_MILL.get(), new Item.Properties()));
    public static final RegistryObject<Item> ALLOY_FURNACE = REGISTER.register("alloy_furnace", () -> new BlockItem(ModBlocks.ALLOY_FURNACE.get(), new Item.Properties()));
    public static final RegistryObject<Item> TIN_ORE = blockItem("tin_ore", ModBlocks.TIN_ORE);
    public static final RegistryObject<Item> DEEPSLATE_TIN_ORE = blockItem("deepslate_tin_ore", ModBlocks.DEEPSLATE_TIN_ORE);
    public static final RegistryObject<Item> NICKEL_ORE = blockItem("nickel_ore", ModBlocks.NICKEL_ORE);
    public static final RegistryObject<Item> DEEPSLATE_NICKEL_ORE = blockItem("deepslate_nickel_ore", ModBlocks.DEEPSLATE_NICKEL_ORE);
    public static final RegistryObject<Item> ACCELERATOR_CONNECTOR = REGISTER.register("accelerator_connector",
        () -> new AcceleratorConnectorItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ACCELERATOR_CORE = REGISTER.register("accelerator_core",
        () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ELECTRIC_FLIGHT_RING = REGISTER.register("electric_flight_ring",
        () -> new ElectricFlightRingItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> IRON_DUST = REGISTER.register("iron_dust", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COPPER_DUST = REGISTER.register("copper_dust", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_DUST = REGISTER.register("gold_dust", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_TIN = simpleItem("raw_tin");
    public static final RegistryObject<Item> TIN_INGOT = simpleItem("tin_ingot");
    public static final RegistryObject<Item> TIN_DUST = simpleItem("tin_dust");
    public static final RegistryObject<Item> TIN_WIRE = simpleItem("tin_wire");
    public static final RegistryObject<Item> RAW_NICKEL = simpleItem("raw_nickel");
    public static final RegistryObject<Item> NICKEL_INGOT = simpleItem("nickel_ingot");
    public static final RegistryObject<Item> NICKEL_DUST = simpleItem("nickel_dust");
    public static final RegistryObject<Item> NICKEL_WIRE = simpleItem("nickel_wire");
    public static final RegistryObject<Item> COPPER_WIRE = REGISTER.register("copper_wire", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_WIRE = REGISTER.register("gold_wire", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> REDSTONE_ALLOY = REGISTER.register("redstone_alloy", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLUX_ALLOY = REGISTER.register("flux_alloy", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> REINFORCED_ALLOY = simpleItem("reinforced_alloy");
    public static final RegistryObject<Item> MACHINE_CASING = simpleItem("machine_casing");
    public static final RegistryObject<Item> INSULATED_WINDING = simpleItem("insulated_winding");
    public static final RegistryObject<Item> ENERGY_LATTICE = simpleItem("energy_lattice");
    public static final RegistryObject<Item> INERTIAL_STABILIZER = simpleItem("inertial_stabilizer");
    public static final RegistryObject<Item> LIFT_EMITTER = simpleItem("lift_emitter");
    public static final RegistryObject<Item> CONDUCTIVE_BAND = simpleItem("conductive_band");
    public static final RegistryObject<Item> INDUCTION_COIL = REGISTER.register("induction_coil", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HIGH_CAPACITY_CELL = REGISTER.register("high_capacity_cell", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLIGHT_CONTROL_CORE = REGISTER.register("flight_control_core", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WING_MATRIX = REGISTER.register("wing_matrix", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RING_FRAME = REGISTER.register("ring_frame", () -> new Item(new Item.Properties()));

    private static RegistryObject<Item> simpleItem(String name) {
        return REGISTER.register(name, () -> new Item(new Item.Properties()));
    }

    private static RegistryObject<Item> blockItem(String name, RegistryObject<Block> block) {
        return REGISTER.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private ModItems() {
    }
}
