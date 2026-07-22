package org.a8043.fluxMachines;

import org.a8043.fluxMachines.config.FluxMachinesConfig;
import org.a8043.fluxMachines.registry.ModBlockEntities;
import org.a8043.fluxMachines.registry.ModBlocks;
import org.a8043.fluxMachines.registry.ModItems;
import org.a8043.fluxMachines.registry.ModMenus;
import org.a8043.fluxMachines.registry.ModRecipes;
import org.a8043.fluxMachines.network.NetworkHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Main.MOD_ID)
public final class Main {
    public static final String MOD_ID = "fluxmachines";

    public Main() {
        ModLoadingContext.get().registerConfig(
            ModConfig.Type.SERVER, FluxMachinesConfig.INSTANCE.getSpec(), "fluxmachines.toml");
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.REGISTER.register(modBus);
        ModItems.REGISTER.register(modBus);
        ModBlockEntities.REGISTER.register(modBus);
        ModMenus.REGISTER.register(modBus);
        ModRecipes.RECIPE_SERIALIZERS.register(modBus);
        ModRecipes.RECIPE_TYPES.register(modBus);
        NetworkHandler.register();
        MinecraftForge.EVENT_BUS.register(this);
    }
}
