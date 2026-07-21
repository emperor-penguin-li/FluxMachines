package org.a8043.fluxMachines.registry;

import org.a8043.fluxMachines.Main;
import org.a8043.fluxMachines.item.AcceleratorConnectorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MOD_ID);
    public static final RegistryObject<Item> ACCELERATOR = REGISTER.register("accelerator",
        () -> new BlockItem(ModBlocks.ACCELERATOR.get(), new Item.Properties()));
    public static final RegistryObject<Item> ACCELERATOR_CONNECTOR = REGISTER.register("accelerator_connector",
        () -> new AcceleratorConnectorItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ACCELERATOR_CORE = REGISTER.register("accelerator_core",
        () -> new Item(new Item.Properties()));

    private ModItems() {
    }
}
