package org.a8043.fluxMachines.registry;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.a8043.fluxMachines.Main;
import org.a8043.fluxMachines.recipe.MachineRecipe;

public final class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
        DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Main.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
        DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Main.MOD_ID);

    public static final RegistryObject<RecipeSerializer<MachineRecipe>> MACHINE_SERIALIZER =
        RECIPE_SERIALIZERS.register("machine_processing", MachineRecipe.Serializer::new);
    public static final RegistryObject<RecipeType<MachineRecipe>> MACHINE_TYPE =
        RECIPE_TYPES.register("machine_processing", () -> new RecipeType<>() {
            @Override public String toString() { return Main.MOD_ID + ":machine_processing"; }
        });

    private ModRecipes() {}
}
