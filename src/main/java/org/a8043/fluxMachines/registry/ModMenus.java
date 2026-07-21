package org.a8043.fluxMachines.registry;

import org.a8043.fluxMachines.Main;
import org.a8043.fluxMachines.menu.AcceleratorMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModMenus {
    public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Main.MOD_ID);
    public static final RegistryObject<MenuType<AcceleratorMenu>> ACCELERATOR = REGISTER.register("accelerator", () -> IForgeMenuType.create(AcceleratorMenu::fromNetwork));

    private ModMenus() {
    }
}
