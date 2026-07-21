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
        }
    }

    private ModCreativeTabs() {
    }
}
