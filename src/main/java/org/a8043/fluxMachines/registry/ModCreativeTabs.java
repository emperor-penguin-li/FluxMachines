package org.a8043.fluxMachines.registry;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.a8043.fluxMachines.Main;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModCreativeTabs {
    @SubscribeEvent
    public static void addCreativeItems(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModItems.ACCELERATOR);
            event.accept(ModItems.ACCELERATOR_CONNECTOR);
            event.accept(ModItems.ACCELERATOR_CORE);
            event.accept(ModItems.ELECTRIC_FLIGHT_RING);
            event.accept(ModItems.PULVERIZER);
            event.accept(ModItems.WIRE_MILL);
            event.accept(ModItems.ALLOY_FURNACE);
            event.accept(ModItems.CHARGING_STATION);
            event.accept(ModItems.MOB_SUPPRESSOR);
            event.accept(ModItems.SUPPRESSOR_CASING);
            event.accept(ModItems.SUPPRESSOR_COIL);
            event.accept(ModItems.SUPPRESSOR_EMITTER);
            event.accept(ModItems.TIN_ORE);
            event.accept(ModItems.DEEPSLATE_TIN_ORE);
            event.accept(ModItems.NICKEL_ORE);
            event.accept(ModItems.DEEPSLATE_NICKEL_ORE);
            event.accept(ModItems.IRON_DUST);
            event.accept(ModItems.COPPER_DUST);
            event.accept(ModItems.GOLD_DUST);
            event.accept(ModItems.RAW_TIN);
            event.accept(ModItems.TIN_INGOT);
            event.accept(ModItems.TIN_DUST);
            event.accept(ModItems.TIN_WIRE);
            event.accept(ModItems.RAW_NICKEL);
            event.accept(ModItems.NICKEL_INGOT);
            event.accept(ModItems.NICKEL_DUST);
            event.accept(ModItems.NICKEL_WIRE);
            event.accept(ModItems.COPPER_WIRE);
            event.accept(ModItems.GOLD_WIRE);
            event.accept(ModItems.REDSTONE_ALLOY);
            event.accept(ModItems.FLUX_ALLOY);
            event.accept(ModItems.REINFORCED_ALLOY);
            event.accept(ModItems.MACHINE_CASING);
            event.accept(ModItems.INSULATED_WINDING);
            event.accept(ModItems.ENERGY_LATTICE);
            event.accept(ModItems.INERTIAL_STABILIZER);
            event.accept(ModItems.LIFT_EMITTER);
            event.accept(ModItems.CONDUCTIVE_BAND);
            event.accept(ModItems.INDUCTION_COIL);
            event.accept(ModItems.HIGH_CAPACITY_CELL);
            event.accept(ModItems.FLIGHT_CONTROL_CORE);
            event.accept(ModItems.WING_MATRIX);
            event.accept(ModItems.RING_FRAME);
            event.accept(ModItems.ELECTRIC_LIFE_SUPPORT_RING);
            event.accept(ModItems.RESONANT_CRUSHER);
            event.accept(ModItems.LEACHING_REACTOR);
            event.accept(ModItems.ELECTROLYTIC_PURIFIER);
            event.accept(ModItems.PLASMA_FURNACE);
            event.accept(ModItems.QUANTUM_ASSEMBLER);
            event.accept(ModItems.TITANIUM_ORE);
            event.accept(ModItems.DEEPSLATE_TITANIUM_ORE);
            event.accept(ModItems.COBALT_ORE);
            event.accept(ModItems.DEEPSLATE_COBALT_ORE);
            event.accept(ModItems.TUNGSTEN_ORE);
            event.accept(ModItems.OSMIUM_ORE);
            event.accept(ModItems.IRIDIUM_ORE);
            event.accept(ModItems.RAW_TITANIUM);
            event.accept(ModItems.RAW_COBALT);
            event.accept(ModItems.RAW_TUNGSTEN);
            event.accept(ModItems.RAW_OSMIUM);
            event.accept(ModItems.RAW_IRIDIUM);
            event.accept(ModItems.CRUSHED_TITANIUM);
            event.accept(ModItems.CRUSHED_COBALT);
            event.accept(ModItems.CRUSHED_TUNGSTEN);
            event.accept(ModItems.CRUSHED_OSMIUM);
            event.accept(ModItems.CRUSHED_IRIDIUM);
            event.accept(ModItems.PURIFIED_TITANIUM_DUST);
            event.accept(ModItems.PURIFIED_COBALT_DUST);
            event.accept(ModItems.PURIFIED_TUNGSTEN_DUST);
            event.accept(ModItems.PURIFIED_OSMIUM_DUST);
            event.accept(ModItems.PURIFIED_IRIDIUM_DUST);
            event.accept(ModItems.TITANIUM_INGOT);
            event.accept(ModItems.COBALT_INGOT);
            event.accept(ModItems.TUNGSTEN_INGOT);
            event.accept(ModItems.OSMIUM_INGOT);
            event.accept(ModItems.IRIDIUM_INGOT);
            event.accept(ModItems.SULFUR_DUST);
            event.accept(ModItems.DIAMOND_DUST);
            event.accept(ModItems.BIO_INERT_ALLOY);
            event.accept(ModItems.TUNGSTEN_OSMIUM_ALLOY);
            event.accept(ModItems.IRIDIUM_SUPERCONDUCTOR);
            event.accept(ModItems.CORROSION_RESISTANT_CASING);
            event.accept(ModItems.ELECTROLYTIC_CELL);
            event.accept(ModItems.PLASMA_COIL);
            event.accept(ModItems.QUANTUM_PROCESSOR);
            event.accept(ModItems.DIAMOND_CAPACITOR_MATRIX);
            event.accept(ModItems.NETHERITE_PRESSURE_FRAME);
            event.accept(ModItems.STELLAR_REGULATION_CORE);
            event.accept(ModItems.FIRST_AID_NANITE_MATRIX);
            event.accept(ModItems.NUTRITION_RECYCLING_MODULE);
            event.accept(ModItems.FORCE_FIELD_MATRIX);
            event.accept(ModItems.LIFE_MONITORING_CORE);
            event.accept(ModItems.IRIDIUM_RING_FRAME);
            ModFluids.ALL.forEach(fluid -> event.accept(fluid.bucket));
        }
    }

    private ModCreativeTabs() {
    }
}
