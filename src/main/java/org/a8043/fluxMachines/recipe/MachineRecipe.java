package org.a8043.fluxMachines.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.a8043.fluxMachines.registry.ModRecipes;
import org.jetbrains.annotations.NotNull;

public final class MachineRecipe implements Recipe<SimpleContainer> {
    public enum Machine {
        PULVERIZER("pulverizer"), WIRE_MILL("wire_mill"), ALLOY_FURNACE("alloy_furnace");

        private final String id;
        Machine(String id) { this.id = id; }
        public String id() { return id; }
        public static Machine fromId(String id) {
            for (Machine machine : values()) if (machine.id.equals(id)) return machine;
            throw new IllegalArgumentException("Unknown processing machine: " + id);
        }
    }

    private final ResourceLocation id;
    private final Machine machine;
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack result;
    private final int duration;
    private final int energyPerTick;

    public MachineRecipe(ResourceLocation id, Machine machine, NonNullList<Ingredient> ingredients,
                         ItemStack result, int duration, int energyPerTick) {
        this.id = id;
        this.machine = machine;
        this.ingredients = ingredients;
        this.result = result;
        this.duration = duration;
        this.energyPerTick = energyPerTick;
    }

    @Override public boolean matches(@NotNull SimpleContainer container, @NotNull Level level) {
        for (int slot = 0; slot < ingredients.size(); slot++) {
            if (!ingredients.get(slot).test(container.getItem(slot))) return false;
        }
        for (int slot = ingredients.size(); slot < container.getContainerSize(); slot++) {
            if (!container.getItem(slot).isEmpty()) return false;
        }
        return true;
    }
    @Override public @NotNull ItemStack assemble(@NotNull SimpleContainer container, @NotNull RegistryAccess access) { return result.copy(); }
    @Override public boolean canCraftInDimensions(int width, int height) { return true; }
    @Override public @NotNull ItemStack getResultItem(@NotNull RegistryAccess access) { return result.copy(); }
    @Override public @NotNull ResourceLocation getId() { return id; }
    @Override public @NotNull RecipeSerializer<?> getSerializer() { return ModRecipes.MACHINE_SERIALIZER.get(); }
    @Override public @NotNull RecipeType<?> getType() { return ModRecipes.MACHINE_TYPE.get(); }
    @Override public @NotNull NonNullList<Ingredient> getIngredients() { return ingredients; }
    @Override public boolean isSpecial() { return true; }

    public Machine machine() { return machine; }
    public ItemStack result() { return result.copy(); }
    public int duration() { return duration; }
    public int energyPerTick() { return energyPerTick; }

    public static final class Serializer implements RecipeSerializer<MachineRecipe> {
        @Override public @NotNull MachineRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
            Machine machine = Machine.fromId(GsonHelper.getAsString(json, "machine"));
            JsonArray inputArray = GsonHelper.getAsJsonArray(json, "ingredients");
            if (inputArray.isEmpty() || inputArray.size() > 3) throw new IllegalArgumentException("Machine recipes require 1-3 ingredients");
            NonNullList<Ingredient> ingredients = NonNullList.create();
            inputArray.forEach(element -> ingredients.add(Ingredient.fromJson(element)));
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            int duration = GsonHelper.getAsInt(json, "duration", 200);
            int energyPerTick = GsonHelper.getAsInt(json, "energy", 40);
            if (duration <= 0 || energyPerTick <= 0) throw new IllegalArgumentException("Machine recipe duration and energy must be positive");
            return new MachineRecipe(id, machine, ingredients, result, duration, energyPerTick);
        }

        @Override public MachineRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buffer) {
            Machine machine = buffer.readEnum(Machine.class);
            int size = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);
            for (int i = 0; i < size; i++) ingredients.set(i, Ingredient.fromNetwork(buffer));
            return new MachineRecipe(id, machine, ingredients, buffer.readItem(), buffer.readVarInt(), buffer.readVarInt());
        }

        @Override public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull MachineRecipe recipe) {
            buffer.writeEnum(recipe.machine);
            buffer.writeVarInt(recipe.ingredients.size());
            recipe.ingredients.forEach(ingredient -> ingredient.toNetwork(buffer));
            buffer.writeItem(recipe.result);
            buffer.writeVarInt(recipe.duration);
            buffer.writeVarInt(recipe.energyPerTick);
        }
    }
}
