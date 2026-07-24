package org.a8043.fluxMachines;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.a8043.fluxMachines.config.FluxMachinesConfig;
import org.a8043.fluxMachines.network.NetworkHandler;
import org.a8043.fluxMachines.registry.*;
import top.theillusivec4.curios.api.CuriosApi;

@Mod(Main.MOD_ID)
public final class Main {
    public static final String MOD_ID = "fluxmachines";

    public Main() {
        ModLoadingContext.get().registerConfig(
            ModConfig.Type.SERVER, FluxMachinesConfig.INSTANCE.getSpec(), "fluxmachines.toml");
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::setup);
        ModBlocks.REGISTER.register(modBus);
        ModItems.REGISTER.register(modBus);
        ModFluids.register(modBus);
        ModBlockEntities.REGISTER.register(modBus);
        ModMenus.REGISTER.register(modBus);
        ModRecipes.RECIPE_SERIALIZERS.register(modBus);
        ModRecipes.RECIPE_TYPES.register(modBus);
        NetworkHandler.register();
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(FMLCommonSetupEvent event) {
        CuriosApi.registerCurio(ModItems.ELECTRIC_FLIGHT_RING.get(), ModItems.ELECTRIC_FLIGHT_RING.get());
        CuriosApi.registerCurio(ModItems.ELECTRIC_LIFE_SUPPORT_RING.get(), ModItems.ELECTRIC_LIFE_SUPPORT_RING.get());
    }
}
