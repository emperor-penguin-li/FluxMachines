package org.a8043.fluxMachines.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.a8043.fluxMachines.Main;
import org.a8043.fluxMachines.registry.ModFluids;
import org.a8043.fluxMachines.registry.ModMenus;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {
    @SubscribeEvent
    public static void registerScreens(FMLClientSetupEvent event) {
        event.enqueueWork(() -> MenuScreens.register(ModMenus.ACCELERATOR.get(), AcceleratorScreen::new));
        event.enqueueWork(() -> MenuScreens.register(ModMenus.PROCESSING_MACHINE.get(), ProcessingMachineScreen::new));
        event.enqueueWork(() -> MenuScreens.register(ModMenus.MOB_SUPPRESSOR.get(), MobSuppressorScreen::new));
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenus.ADVANCED_PROCESSING_MACHINE.get(), AdvancedProcessingMachineScreen::new);
            ModFluids.ALL.forEach(fluid -> {
                ItemBlockRenderTypes.setRenderLayer(fluid.source.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(fluid.flowing.get(), RenderType.translucent());
            });
        });
    }

    private ClientModEvents() {
    }
}
