package org.a8043.fluxMachines.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.a8043.fluxMachines.Main;
import org.a8043.fluxMachines.client.ProcessingMachineScreen;
import org.a8043.fluxMachines.recipe.MachineRecipe;
import org.a8043.fluxMachines.registry.ModBlocks;
import org.a8043.fluxMachines.registry.ModRecipes;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@JeiPlugin
public final class FluxMachinesJeiPlugin implements IModPlugin {
    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "jei_plugin");
    public static final RecipeType<MachineRecipe> PULVERIZING = RecipeType.create(Main.MOD_ID, "pulverizing", MachineRecipe.class);
    public static final RecipeType<MachineRecipe> WIRE_DRAWING = RecipeType.create(Main.MOD_ID, "wire_drawing", MachineRecipe.class);
    public static final RecipeType<MachineRecipe> ALLOYING = RecipeType.create(Main.MOD_ID, "alloying", MachineRecipe.class);

    @Override public @NotNull ResourceLocation getPluginUid() { return UID; }

    @Override public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        var helper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
            new MachineRecipeCategory(helper, PULVERIZING, MachineRecipe.Machine.PULVERIZER, new ItemStack(ModBlocks.PULVERIZER.get())),
            new MachineRecipeCategory(helper, WIRE_DRAWING, MachineRecipe.Machine.WIRE_MILL, new ItemStack(ModBlocks.WIRE_MILL.get())),
            new MachineRecipeCategory(helper, ALLOYING, MachineRecipe.Machine.ALLOY_FURNACE, new ItemStack(ModBlocks.ALLOY_FURNACE.get())));
    }

    @Override public void registerRecipes(@NotNull IRecipeRegistration registration) {
        var level = Minecraft.getInstance().level;
        if (level == null) return;
        List<MachineRecipe> recipes = level.getRecipeManager().getAllRecipesFor(ModRecipes.MACHINE_TYPE.get());
        registration.addRecipes(PULVERIZING, filtered(recipes, MachineRecipe.Machine.PULVERIZER));
        registration.addRecipes(WIRE_DRAWING, filtered(recipes, MachineRecipe.Machine.WIRE_MILL));
        registration.addRecipes(ALLOYING, filtered(recipes, MachineRecipe.Machine.ALLOY_FURNACE));
    }

    @Override public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.PULVERIZER.get()), PULVERIZING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.WIRE_MILL.get()), WIRE_DRAWING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ALLOY_FURNACE.get()), ALLOYING);
    }

    @Override public void registerGuiHandlers(@NotNull IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ProcessingMachineScreen.class, 75, 32, 24, 20, PULVERIZING, WIRE_DRAWING, ALLOYING);
    }

    private static List<MachineRecipe> filtered(List<MachineRecipe> recipes, MachineRecipe.Machine machine) {
        return recipes.stream().filter(recipe -> recipe.machine() == machine).toList();
    }
}
