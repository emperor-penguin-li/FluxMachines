package org.a8043.fluxMachines.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.a8043.fluxMachines.Main;
import org.a8043.fluxMachines.item.AbstractElectricRingItem;
import org.a8043.fluxMachines.item.AcceleratorConnectorItem;
import org.a8043.fluxMachines.item.ElectricFlightRingItem;
import org.a8043.fluxMachines.item.ElectricLifeSupportRingItem;

public final class ModItems {
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MOD_ID);
    public static final RegistryObject<Item> ACCELERATOR = REGISTER.register("accelerator",
        () -> new BlockItem(ModBlocks.ACCELERATOR.get(), new Item.Properties()));
    public static final RegistryObject<Item> PULVERIZER = REGISTER.register("pulverizer", () -> new BlockItem(ModBlocks.PULVERIZER.get(), new Item.Properties()));
    public static final RegistryObject<Item> WIRE_MILL = REGISTER.register("wire_mill", () -> new BlockItem(ModBlocks.WIRE_MILL.get(), new Item.Properties()));
    public static final RegistryObject<Item> ALLOY_FURNACE = REGISTER.register("alloy_furnace", () -> new BlockItem(ModBlocks.ALLOY_FURNACE.get(), new Item.Properties()));
    public static final RegistryObject<Item> RESONANT_CRUSHER = blockItem("resonant_crusher", ModBlocks.RESONANT_CRUSHER);
    public static final RegistryObject<Item> LEACHING_REACTOR = blockItem("leaching_reactor", ModBlocks.LEACHING_REACTOR);
    public static final RegistryObject<Item> ELECTROLYTIC_PURIFIER = blockItem("electrolytic_purifier", ModBlocks.ELECTROLYTIC_PURIFIER);
    public static final RegistryObject<Item> PLASMA_FURNACE = blockItem("plasma_furnace", ModBlocks.PLASMA_FURNACE);
    public static final RegistryObject<Item> QUANTUM_ASSEMBLER = blockItem("quantum_assembler", ModBlocks.QUANTUM_ASSEMBLER);
    public static final RegistryObject<Item> MOB_SUPPRESSOR = blockItem("mob_suppressor", ModBlocks.MOB_SUPPRESSOR);
    public static final RegistryObject<Item> SUPPRESSOR_CASING = blockItem("suppressor_casing", ModBlocks.SUPPRESSOR_CASING);
    public static final RegistryObject<Item> SUPPRESSOR_COIL = blockItem("suppressor_coil", ModBlocks.SUPPRESSOR_COIL);
    public static final RegistryObject<Item> SUPPRESSOR_EMITTER = blockItem("suppressor_emitter", ModBlocks.SUPPRESSOR_EMITTER);
    public static final RegistryObject<Item> TIN_ORE = blockItem("tin_ore", ModBlocks.TIN_ORE);
    public static final RegistryObject<Item> DEEPSLATE_TIN_ORE = blockItem("deepslate_tin_ore", ModBlocks.DEEPSLATE_TIN_ORE);
    public static final RegistryObject<Item> NICKEL_ORE = blockItem("nickel_ore", ModBlocks.NICKEL_ORE);
    public static final RegistryObject<Item> DEEPSLATE_NICKEL_ORE = blockItem("deepslate_nickel_ore", ModBlocks.DEEPSLATE_NICKEL_ORE);
    public static final RegistryObject<Item> TITANIUM_ORE = blockItem("titanium_ore", ModBlocks.TITANIUM_ORE);
    public static final RegistryObject<Item> DEEPSLATE_TITANIUM_ORE = blockItem("deepslate_titanium_ore", ModBlocks.DEEPSLATE_TITANIUM_ORE);
    public static final RegistryObject<Item> COBALT_ORE = blockItem("cobalt_ore", ModBlocks.COBALT_ORE);
    public static final RegistryObject<Item> DEEPSLATE_COBALT_ORE = blockItem("deepslate_cobalt_ore", ModBlocks.DEEPSLATE_COBALT_ORE);
    public static final RegistryObject<Item> TUNGSTEN_ORE = blockItem("tungsten_ore", ModBlocks.TUNGSTEN_ORE);
    public static final RegistryObject<Item> OSMIUM_ORE = blockItem("osmium_ore", ModBlocks.OSMIUM_ORE);
    public static final RegistryObject<Item> IRIDIUM_ORE = blockItem("iridium_ore", ModBlocks.IRIDIUM_ORE);
    public static final RegistryObject<Item> ACCELERATOR_CONNECTOR = REGISTER.register("accelerator_connector",
        () -> new AcceleratorConnectorItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ACCELERATOR_CORE = REGISTER.register("accelerator_core",
        () -> new Item(new Item.Properties()));
    public static final RegistryObject<AbstractElectricRingItem> ELECTRIC_FLIGHT_RING = REGISTER.register("electric_flight_ring",
        () -> new ElectricFlightRingItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<AbstractElectricRingItem> ELECTRIC_LIFE_SUPPORT_RING = REGISTER.register("electric_life_support_ring",
        () -> new ElectricLifeSupportRingItem(new Item.Properties().stacksTo(1)));
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
    public static final RegistryObject<Item> RAW_TITANIUM = simpleItem("raw_titanium");
    public static final RegistryObject<Item> RAW_COBALT = simpleItem("raw_cobalt");
    public static final RegistryObject<Item> RAW_TUNGSTEN = simpleItem("raw_tungsten");
    public static final RegistryObject<Item> RAW_OSMIUM = simpleItem("raw_osmium");
    public static final RegistryObject<Item> RAW_IRIDIUM = simpleItem("raw_iridium");
    public static final RegistryObject<Item> CRUSHED_TITANIUM = simpleItem("crushed_titanium");
    public static final RegistryObject<Item> CRUSHED_COBALT = simpleItem("crushed_cobalt");
    public static final RegistryObject<Item> CRUSHED_TUNGSTEN = simpleItem("crushed_tungsten");
    public static final RegistryObject<Item> CRUSHED_OSMIUM = simpleItem("crushed_osmium");
    public static final RegistryObject<Item> CRUSHED_IRIDIUM = simpleItem("crushed_iridium");
    public static final RegistryObject<Item> PURIFIED_TITANIUM_DUST = simpleItem("purified_titanium_dust");
    public static final RegistryObject<Item> PURIFIED_COBALT_DUST = simpleItem("purified_cobalt_dust");
    public static final RegistryObject<Item> PURIFIED_TUNGSTEN_DUST = simpleItem("purified_tungsten_dust");
    public static final RegistryObject<Item> PURIFIED_OSMIUM_DUST = simpleItem("purified_osmium_dust");
    public static final RegistryObject<Item> PURIFIED_IRIDIUM_DUST = simpleItem("purified_iridium_dust");
    public static final RegistryObject<Item> TITANIUM_INGOT = simpleItem("titanium_ingot");
    public static final RegistryObject<Item> COBALT_INGOT = simpleItem("cobalt_ingot");
    public static final RegistryObject<Item> TUNGSTEN_INGOT = simpleItem("tungsten_ingot");
    public static final RegistryObject<Item> OSMIUM_INGOT = simpleItem("osmium_ingot");
    public static final RegistryObject<Item> IRIDIUM_INGOT = simpleItem("iridium_ingot");
    public static final RegistryObject<Item> SULFUR_DUST = simpleItem("sulfur_dust");
    public static final RegistryObject<Item> DIAMOND_DUST = simpleItem("diamond_dust");
    public static final RegistryObject<Item> BIO_INERT_ALLOY = simpleItem("bio_inert_alloy");
    public static final RegistryObject<Item> TUNGSTEN_OSMIUM_ALLOY = simpleItem("tungsten_osmium_alloy");
    public static final RegistryObject<Item> IRIDIUM_SUPERCONDUCTOR = simpleItem("iridium_superconductor");
    public static final RegistryObject<Item> CORROSION_RESISTANT_CASING = simpleItem("corrosion_resistant_casing");
    public static final RegistryObject<Item> ELECTROLYTIC_CELL = simpleItem("electrolytic_cell");
    public static final RegistryObject<Item> PLASMA_COIL = simpleItem("plasma_coil");
    public static final RegistryObject<Item> QUANTUM_PROCESSOR = simpleItem("quantum_processor");
    public static final RegistryObject<Item> DIAMOND_CAPACITOR_MATRIX = simpleItem("diamond_capacitor_matrix");
    public static final RegistryObject<Item> NETHERITE_PRESSURE_FRAME = simpleItem("netherite_pressure_frame");
    public static final RegistryObject<Item> STELLAR_REGULATION_CORE = simpleItem("stellar_regulation_core");
    public static final RegistryObject<Item> FIRST_AID_NANITE_MATRIX = simpleItem("first_aid_nanite_matrix");
    public static final RegistryObject<Item> NUTRITION_RECYCLING_MODULE = simpleItem("nutrition_recycling_module");
    public static final RegistryObject<Item> FORCE_FIELD_MATRIX = simpleItem("force_field_matrix");
    public static final RegistryObject<Item> LIFE_MONITORING_CORE = simpleItem("life_monitoring_core");
    public static final RegistryObject<Item> IRIDIUM_RING_FRAME = simpleItem("iridium_ring_frame");

    private static RegistryObject<Item> simpleItem(String name) {
        return REGISTER.register(name, () -> new Item(new Item.Properties()));
    }

    private static RegistryObject<Item> blockItem(String name, RegistryObject<Block> block) {
        return REGISTER.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private ModItems() {
    }
}
