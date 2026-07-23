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
        }
    }

    private ModCreativeTabs() {
    }
}
