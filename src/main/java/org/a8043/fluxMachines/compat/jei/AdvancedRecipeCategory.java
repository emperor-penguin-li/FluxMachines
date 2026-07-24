package org.a8043.fluxMachines.compat.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.a8043.fluxMachines.recipe.AdvancedProcessingRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

final class AdvancedRecipeCategory implements IRecipeCategory<AdvancedProcessingRecipe> {
    private final RecipeType<AdvancedProcessingRecipe> recipeType;
    private final AdvancedProcessingRecipe.Machine machine;
    private final IDrawable background;
    private final IDrawable icon;

    AdvancedRecipeCategory(IGuiHelper helper, RecipeType<AdvancedProcessingRecipe> recipeType,
                           AdvancedProcessingRecipe.Machine machine, ItemStack iconStack) {
        this.recipeType = recipeType;
        this.machine = machine;
        background = helper.createBlankDrawable(166, 76);
        icon = helper.createDrawableItemStack(iconStack);
    }

    @Override
    public @NotNull RecipeType<AdvancedProcessingRecipe> getRecipeType() {
        return recipeType;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jei.fluxmachines.category." + machine.id());
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull AdvancedProcessingRecipe recipe,
                          @NotNull IFocusGroup focuses) {
        for (int index = 0; index < recipe.itemInputs().size(); index++) {
            var input = recipe.itemInputs().get(index);
            int x = 2 + (index % 3) * 19;
            int y = 2 + (index / 3) * 19;
            builder.addSlot(RecipeIngredientRole.INPUT, x, y)
                .addItemStacks(Arrays.stream(input.ingredient().getItems())
                    .map(stack -> stack.copyWithCount(input.count())).toList());
        }
        for (int index = 0; index < recipe.fluidInputs().size(); index++) {
            var fluid = recipe.fluidInputs().get(index);
            builder.addSlot(RecipeIngredientRole.INPUT, 64 + index * 20, 2)
                .setFluidRenderer(fluid.getAmount(), false, 16, 52).addFluidStack(fluid.getFluid(), fluid.getAmount());
        }
        for (int index = 0; index < recipe.itemOutputs().size(); index++) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 124 + index * 19, 20).addItemStack(recipe.itemOutputs().get(index));
        }
        for (int index = 0; index < recipe.fluidOutputs().size(); index++) {
            var fluid = recipe.fluidOutputs().get(index);
            builder.addSlot(RecipeIngredientRole.OUTPUT, 104 + index * 20, 2)
                .setFluidRenderer(fluid.getAmount(), false, 16, 52).addFluidStack(fluid.getFluid(), fluid.getAmount());
        }
    }
}
