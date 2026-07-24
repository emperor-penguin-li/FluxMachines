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
import org.a8043.fluxMachines.recipe.MachineRecipe;
import org.jetbrains.annotations.NotNull;

final class MachineRecipeCategory implements IRecipeCategory<MachineRecipe> {
    private final RecipeType<MachineRecipe> recipeType;
    private final MachineRecipe.Machine machine;
    private final IDrawable background;
    private final IDrawable icon;

    MachineRecipeCategory(IGuiHelper guiHelper, RecipeType<MachineRecipe> recipeType,
                          MachineRecipe.Machine machine, ItemStack iconStack) {
        this.recipeType = recipeType;
        this.machine = machine;
        this.background = guiHelper.createBlankDrawable(140, 54);
        this.icon = guiHelper.createDrawableItemStack(iconStack);
    }

    @Override
    public @NotNull RecipeType<MachineRecipe> getRecipeType() {
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
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull MachineRecipe recipe, @NotNull IFocusGroup focuses) {
        int count = recipe.getIngredients().size();
        int startX = count == 1 ? 24 : 6;
        for (int slot = 0; slot < count; slot++) {
            builder.addSlot(RecipeIngredientRole.INPUT, startX + slot * 20, 19)
                .addIngredients(recipe.getIngredients().get(slot));
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 112, 19).addItemStack(recipe.result());
    }
}
