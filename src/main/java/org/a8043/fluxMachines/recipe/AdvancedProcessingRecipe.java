package org.a8043.fluxMachines.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.a8043.fluxMachines.registry.ModRecipes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class AdvancedProcessingRecipe implements Recipe<SimpleContainer> {
    public enum Machine {
        RESONANT_CRUSHER("resonant_crusher"), LEACHING_REACTOR("leaching_reactor"),
        ELECTROLYTIC_PURIFIER("electrolytic_purifier"), PLASMA_FURNACE("plasma_furnace"),
        QUANTUM_ASSEMBLER("quantum_assembler");

        private final String id;

        Machine(String id) {
            this.id = id;
        }

        public String id() {
            return id;
        }

        public static Machine fromId(String id) {
            for (Machine machine : values()) {
                if (machine.id.equals(id)) {
                    return machine;
                }
            }
            throw new IllegalArgumentException("Unknown advanced machine: " + id);
        }
    }

    public record CountedIngredient(Ingredient ingredient, int count) {
    }

    private final ResourceLocation id;
    private final Machine machine;
    private final List<CountedIngredient> itemInputs;
    private final List<FluidStack> fluidInputs;
    private final List<ItemStack> itemOutputs;
    private final List<FluidStack> fluidOutputs;
    private final int duration;
    private final int energyPerTick;

    public AdvancedProcessingRecipe(ResourceLocation id, Machine machine, List<CountedIngredient> itemInputs,
                                    List<FluidStack> fluidInputs, List<ItemStack> itemOutputs,
                                    List<FluidStack> fluidOutputs, int duration, int energyPerTick) {
        this.id = id;
        this.machine = machine;
        this.itemInputs = List.copyOf(itemInputs);
        this.fluidInputs = copyFluids(fluidInputs);
        this.itemOutputs = copyItems(itemOutputs);
        this.fluidOutputs = copyFluids(fluidOutputs);
        this.duration = duration;
        this.energyPerTick = energyPerTick;
    }

    public Machine machine() {
        return machine;
    }

    public List<CountedIngredient> itemInputs() {
        return itemInputs;
    }

    public List<FluidStack> fluidInputs() {
        return copyFluids(fluidInputs);
    }

    public List<ItemStack> itemOutputs() {
        return copyItems(itemOutputs);
    }

    public List<FluidStack> fluidOutputs() {
        return copyFluids(fluidOutputs);
    }

    public int duration() {
        return duration;
    }

    public int energyPerTick() {
        return energyPerTick;
    }

    @Override
    public boolean matches(@NotNull SimpleContainer container, @NotNull Level level) {
        if (container.getContainerSize() < itemInputs.size()) {
            return false;
        }
        for (int slot = 0; slot < itemInputs.size(); slot++) {
            CountedIngredient input = itemInputs.get(slot);
            ItemStack stack = container.getItem(slot);
            if (!input.ingredient().test(stack) || stack.getCount() < input.count()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SimpleContainer container, @NotNull RegistryAccess access) {
        return itemOutputs.isEmpty() ? ItemStack.EMPTY : itemOutputs.get(0).copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess access) {
        return itemOutputs.isEmpty() ? ItemStack.EMPTY : itemOutputs.get(0).copy();
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.ADVANCED_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipes.ADVANCED_TYPE.get();
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        itemInputs.forEach(input -> ingredients.add(input.ingredient()));
        return ingredients;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    private static List<ItemStack> copyItems(List<ItemStack> stacks) {
        return stacks.stream().map(ItemStack::copy).toList();
    }

    private static List<FluidStack> copyFluids(List<FluidStack> stacks) {
        return stacks.stream().map(FluidStack::copy).toList();
    }

    public static final class Serializer implements RecipeSerializer<AdvancedProcessingRecipe> {
        @Override
        public @NotNull AdvancedProcessingRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
            Machine machine = Machine.fromId(GsonHelper.getAsString(json, "machine"));
            List<CountedIngredient> itemInputs = readItemInputs(json, "item_inputs", 9);
            List<FluidStack> fluidInputs = readFluids(json, "fluid_inputs", 2);
            List<ItemStack> itemOutputs = readItems(json, "item_outputs", 2);
            List<FluidStack> fluidOutputs = readFluids(json, "fluid_outputs", 2);
            if (itemOutputs.isEmpty() && fluidOutputs.isEmpty()) {
                throw new IllegalArgumentException("Advanced recipe requires an output");
            }
            int duration = GsonHelper.getAsInt(json, "duration");
            int energyPerTick = GsonHelper.getAsInt(json, "energy_per_tick");
            if (duration <= 0 || energyPerTick <= 0) {
                throw new IllegalArgumentException("Duration and energy must be positive");
            }
            return new AdvancedProcessingRecipe(id, machine, itemInputs, fluidInputs, itemOutputs, fluidOutputs,
                duration, energyPerTick);
        }

        @Override
        public AdvancedProcessingRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buffer) {
            Machine machine = buffer.readEnum(Machine.class);
            List<CountedIngredient> itemInputs = new ArrayList<>();
            int inputCount = buffer.readVarInt();
            for (int i = 0; i < inputCount; i++) {
                itemInputs.add(new CountedIngredient(Ingredient.fromNetwork(buffer), buffer.readVarInt()));
            }
            return new AdvancedProcessingRecipe(id, machine, itemInputs, readFluidNetwork(buffer), readItemNetwork(buffer),
                readFluidNetwork(buffer), buffer.readVarInt(), buffer.readVarInt());
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, AdvancedProcessingRecipe recipe) {
            buffer.writeEnum(recipe.machine);
            buffer.writeVarInt(recipe.itemInputs.size());
            recipe.itemInputs.forEach(input -> {
                input.ingredient().toNetwork(buffer);
                buffer.writeVarInt(input.count());
            });
            writeFluidNetwork(buffer, recipe.fluidInputs);
            writeItemNetwork(buffer, recipe.itemOutputs);
            writeFluidNetwork(buffer, recipe.fluidOutputs);
            buffer.writeVarInt(recipe.duration);
            buffer.writeVarInt(recipe.energyPerTick);
        }

        private static List<CountedIngredient> readItemInputs(JsonObject json, String key, int limit) {
            List<CountedIngredient> result = new ArrayList<>();
            if (!json.has(key)) {
                return result;
            }
            JsonArray array = GsonHelper.getAsJsonArray(json, key);
            if (array.size() > limit) {
                throw new IllegalArgumentException(key + " supports at most " + limit + " entries");
            }
            for (JsonElement element : array) {
                JsonObject object = element.getAsJsonObject();
                int count = GsonHelper.getAsInt(object, "count", 1);
                if (count < 1 || count > 64) {
                    throw new IllegalArgumentException("Item input count must be 1-64");
                }
                result.add(new CountedIngredient(Ingredient.fromJson(object.get("ingredient")), count));
            }
            return result;
        }

        private static List<ItemStack> readItems(JsonObject json, String key, int limit) {
            List<ItemStack> result = new ArrayList<>();
            if (!json.has(key)) {
                return result;
            }
            JsonArray array = GsonHelper.getAsJsonArray(json, key);
            if (array.size() > limit) {
                throw new IllegalArgumentException(key + " supports at most " + limit + " entries");
            }
            array.forEach(element -> result.add(ShapedRecipe.itemStackFromJson(element.getAsJsonObject())));
            return result;
        }

        private static List<FluidStack> readFluids(JsonObject json, String key, int limit) {
            List<FluidStack> result = new ArrayList<>();
            if (!json.has(key)) {
                return result;
            }
            JsonArray array = GsonHelper.getAsJsonArray(json, key);
            if (array.size() > limit) {
                throw new IllegalArgumentException(key + " supports at most " + limit + " entries");
            }
            for (JsonElement element : array) {
                JsonObject object = element.getAsJsonObject();
                ResourceLocation fluidId = ResourceLocation.parse(GsonHelper.getAsString(object, "fluid"));
                var fluid = ForgeRegistries.FLUIDS.getValue(fluidId);
                if (fluid == null) {
                    throw new IllegalArgumentException("Unknown fluid " + fluidId);
                }
                int amount = GsonHelper.getAsInt(object, "amount");
                if (amount <= 0) {
                    throw new IllegalArgumentException("Fluid amount must be positive");
                }
                result.add(new FluidStack(fluid, amount));
            }
            return result;
        }

        private static List<FluidStack> readFluidNetwork(FriendlyByteBuf buffer) {
            List<FluidStack> result = new ArrayList<>();
            int size = buffer.readVarInt();
            for (int i = 0; i < size; i++) {
                result.add(buffer.readFluidStack());
            }
            return result;
        }

        private static void writeFluidNetwork(FriendlyByteBuf buffer, List<FluidStack> stacks) {
            buffer.writeVarInt(stacks.size());
            stacks.forEach(buffer::writeFluidStack);
        }

        private static List<ItemStack> readItemNetwork(FriendlyByteBuf buffer) {
            List<ItemStack> result = new ArrayList<>();
            int size = buffer.readVarInt();
            for (int i = 0; i < size; i++) {
                result.add(buffer.readItem());
            }
            return result;
        }

        private static void writeItemNetwork(FriendlyByteBuf buffer, List<ItemStack> stacks) {
            buffer.writeVarInt(stacks.size());
            stacks.forEach(buffer::writeItem);
        }
    }
}
