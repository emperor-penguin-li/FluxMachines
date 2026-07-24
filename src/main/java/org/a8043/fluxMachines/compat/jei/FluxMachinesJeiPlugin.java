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
import org.a8043.fluxMachines.client.AdvancedProcessingMachineScreen;
import org.a8043.fluxMachines.client.ProcessingMachineScreen;
import org.a8043.fluxMachines.recipe.AdvancedProcessingRecipe;
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
    public static final RecipeType<AdvancedProcessingRecipe> RESONANT_CRUSHING = RecipeType.create(Main.MOD_ID, "resonant_crushing", AdvancedProcessingRecipe.class);
    public static final RecipeType<AdvancedProcessingRecipe> LEACHING = RecipeType.create(Main.MOD_ID, "leaching", AdvancedProcessingRecipe.class);
    public static final RecipeType<AdvancedProcessingRecipe> ELECTROLYTIC_PURIFYING = RecipeType.create(Main.MOD_ID, "electrolytic_purifying", AdvancedProcessingRecipe.class);
    public static final RecipeType<AdvancedProcessingRecipe> PLASMA_PROCESSING = RecipeType.create(Main.MOD_ID, "plasma_processing", AdvancedProcessingRecipe.class);
    public static final RecipeType<AdvancedProcessingRecipe> QUANTUM_ASSEMBLY = RecipeType.create(Main.MOD_ID, "quantum_assembly", AdvancedProcessingRecipe.class);

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        var helper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
            new MachineRecipeCategory(helper, PULVERIZING, MachineRecipe.Machine.PULVERIZER, new ItemStack(ModBlocks.PULVERIZER.get())),
            new MachineRecipeCategory(helper, WIRE_DRAWING, MachineRecipe.Machine.WIRE_MILL, new ItemStack(ModBlocks.WIRE_MILL.get())),
            new MachineRecipeCategory(helper, ALLOYING, MachineRecipe.Machine.ALLOY_FURNACE, new ItemStack(ModBlocks.ALLOY_FURNACE.get())),
            new AdvancedRecipeCategory(helper, RESONANT_CRUSHING, AdvancedProcessingRecipe.Machine.RESONANT_CRUSHER, new ItemStack(ModBlocks.RESONANT_CRUSHER.get())),
            new AdvancedRecipeCategory(helper, LEACHING, AdvancedProcessingRecipe.Machine.LEACHING_REACTOR, new ItemStack(ModBlocks.LEACHING_REACTOR.get())),
            new AdvancedRecipeCategory(helper, ELECTROLYTIC_PURIFYING, AdvancedProcessingRecipe.Machine.ELECTROLYTIC_PURIFIER, new ItemStack(ModBlocks.ELECTROLYTIC_PURIFIER.get())),
            new AdvancedRecipeCategory(helper, PLASMA_PROCESSING, AdvancedProcessingRecipe.Machine.PLASMA_FURNACE, new ItemStack(ModBlocks.PLASMA_FURNACE.get())),
            new AdvancedRecipeCategory(helper, QUANTUM_ASSEMBLY, AdvancedProcessingRecipe.Machine.QUANTUM_ASSEMBLER, new ItemStack(ModBlocks.QUANTUM_ASSEMBLER.get())));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        var level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        List<MachineRecipe> recipes = level.getRecipeManager().getAllRecipesFor(ModRecipes.MACHINE_TYPE.get());
        registration.addRecipes(PULVERIZING, filtered(recipes, MachineRecipe.Machine.PULVERIZER));
        registration.addRecipes(WIRE_DRAWING, filtered(recipes, MachineRecipe.Machine.WIRE_MILL));
        registration.addRecipes(ALLOYING, filtered(recipes, MachineRecipe.Machine.ALLOY_FURNACE));
        List<AdvancedProcessingRecipe> advanced = level.getRecipeManager().getAllRecipesFor(ModRecipes.ADVANCED_TYPE.get());
        registration.addRecipes(RESONANT_CRUSHING, filteredAdvanced(advanced, AdvancedProcessingRecipe.Machine.RESONANT_CRUSHER));
        registration.addRecipes(LEACHING, filteredAdvanced(advanced, AdvancedProcessingRecipe.Machine.LEACHING_REACTOR));
        registration.addRecipes(ELECTROLYTIC_PURIFYING, filteredAdvanced(advanced, AdvancedProcessingRecipe.Machine.ELECTROLYTIC_PURIFIER));
        registration.addRecipes(PLASMA_PROCESSING, filteredAdvanced(advanced, AdvancedProcessingRecipe.Machine.PLASMA_FURNACE));
        registration.addRecipes(QUANTUM_ASSEMBLY, filteredAdvanced(advanced, AdvancedProcessingRecipe.Machine.QUANTUM_ASSEMBLER));
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.PULVERIZER.get()), PULVERIZING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.WIRE_MILL.get()), WIRE_DRAWING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ALLOY_FURNACE.get()), ALLOYING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.RESONANT_CRUSHER.get()), RESONANT_CRUSHING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.LEACHING_REACTOR.get()), LEACHING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ELECTROLYTIC_PURIFIER.get()), ELECTROLYTIC_PURIFYING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.PLASMA_FURNACE.get()), PLASMA_PROCESSING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.QUANTUM_ASSEMBLER.get()), QUANTUM_ASSEMBLY);
    }

    @Override
    public void registerGuiHandlers(@NotNull IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ProcessingMachineScreen.class, 75, 32, 24, 20, PULVERIZING, WIRE_DRAWING, ALLOYING);
        registration.addRecipeClickArea(AdvancedProcessingMachineScreen.class, 84, 42, 36, 18,
            RESONANT_CRUSHING, LEACHING, ELECTROLYTIC_PURIFYING, PLASMA_PROCESSING, QUANTUM_ASSEMBLY);
    }

    private static List<MachineRecipe> filtered(List<MachineRecipe> recipes, MachineRecipe.Machine machine) {
        return recipes.stream().filter(recipe -> recipe.machine() == machine).toList();
    }

    private static List<AdvancedProcessingRecipe> filteredAdvanced(List<AdvancedProcessingRecipe> recipes,
                                                                   AdvancedProcessingRecipe.Machine machine) {
        return recipes.stream().filter(recipe -> recipe.machine() == machine).toList();
    }
}
