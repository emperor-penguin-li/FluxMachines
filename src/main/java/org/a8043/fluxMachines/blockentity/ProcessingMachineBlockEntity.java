package org.a8043.fluxMachines.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.a8043.fluxMachines.menu.ProcessingMachineMenu;
import org.a8043.fluxMachines.recipe.MachineRecipe;
import org.a8043.fluxMachines.registry.ModBlockEntities;
import org.a8043.fluxMachines.registry.ModBlocks;
import org.a8043.fluxMachines.registry.ModRecipes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ProcessingMachineBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler inventory = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    private final MachineEnergyStorage energy = new MachineEnergyStorage();

    private final class MachineEnergyStorage extends EnergyStorage {
        private MachineEnergyStorage() {
            super(100_000, 4096, 0);
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int n = super.receiveEnergy(maxReceive, simulate);
            if (n > 0 && !simulate) setChanged();
            return n;
        }

        private boolean consume(int amount) {
            if (amount < 0 || energy < amount) return false;
            energy -= amount;
            setChanged();
            return true;
        }
    }

    private final LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> energy);
    private final IItemHandler automation = new IItemHandler() {
        private int outputSlot() {
            return kind() == 2 ? 3 : 1;
        }

        private int inputSlots() {
            return kind() == 2 ? 3 : 1;
        }

        @Override
        public int getSlots() {
            return 4;
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return inventory.getStackInSlot(slot);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (slot >= inputSlots()) return stack;
            return inventory.insertItem(slot, stack, simulate);
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return slot == outputSlot() ? inventory.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return slot < inputSlots() && isValidInput(slot, stack);
        }
    };
    private final LazyOptional<IItemHandler> itemCap = LazyOptional.of(() -> automation);
    private int progress;
    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> energy.getEnergyStored() & 0x7FFF;
                case 1 -> (energy.getEnergyStored() >>> 15) & 0x7FFF;
                case 2 -> progress;
                case 3 -> getProcessDuration();
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            if (index == 2) progress = value;
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public ProcessingMachineBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PROCESSING_MACHINE.get(), pos, state);
    }

    public IItemHandler getInventory() {
        return inventory;
    }

    public ContainerData getData() {
        return data;
    }

    public int getMachineKind() {
        return kind();
    }

    public int getOutputSlot() {
        return kind() == 2 ? 3 : 1;
    }

    public int getInputSlotCount() {
        return kind() == 2 ? 3 : 1;
    }

    public int getEnergyCapacity() {
        return energy.getMaxEnergyStored();
    }

    public boolean isValidInput(int slot, ItemStack stack) {
        if (slot < 0 || slot >= getInputSlotCount() || stack.isEmpty()) return false;
        if (level == null) return false;
        return level.getRecipeManager().getAllRecipesFor(ModRecipes.MACHINE_TYPE.get()).stream()
            .filter(recipe -> recipe.machine() == getMachine() && recipe.getIngredients().size() > slot)
            .anyMatch(recipe -> recipe.getIngredients().get(slot).test(stack));
    }

    private int getProcessDuration() {
        return findRecipe().map(MachineRecipe::duration).orElse(200);
    }

    public void insert(ItemStack held) {
        if (held.isEmpty()) return;
        for (int i = 0; i < (kind() == 2 ? 3 : 1); i++)
            if (inventory.getStackInSlot(i).isEmpty()) {
                inventory.setStackInSlot(i, held.copyWithCount(1));
                held.shrink(1);
                return;
            }
    }

    public void giveOutput(Player player) {
        int slot = kind() == 2 ? 3 : 1;
        ItemStack out = inventory.extractItem(slot, 64, false);
        if (!out.isEmpty() && !player.addItem(out)) player.drop(out, false);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ProcessingMachineBlockEntity machine) {
        if (level.isClientSide) return;
        MachineRecipe recipe = machine.findRecipe().orElse(null);
        int cost = recipe == null ? 0 : recipe.energyPerTick();
        int duration = recipe == null ? 200 : recipe.duration();
        if (recipe != null && machine.canOutput(recipe.result()) && machine.energy.consume(cost)) {
            machine.progress++;
            if (machine.progress >= duration) {
                machine.process(recipe);
                machine.progress = 0;
            }
            machine.setChanged();
        } else if (recipe == null || !machine.canOutput(recipe.result())) machine.progress = 0;
    }

    private int kind() {
        if (getBlockState().is(ModBlocks.WIRE_MILL.get())) return 1;
        if (getBlockState().is(ModBlocks.ALLOY_FURNACE.get())) return 2;
        return 0;
    }

    public MachineRecipe.Machine getMachine() {
        return kind() == 2 ? MachineRecipe.Machine.ALLOY_FURNACE : kind() == 1 ? MachineRecipe.Machine.WIRE_MILL : MachineRecipe.Machine.PULVERIZER;
    }

    private java.util.Optional<MachineRecipe> findRecipe() {
        if (level == null) return java.util.Optional.empty();
        SimpleContainer inputs = new SimpleContainer(getInputSlotCount());
        for (int slot = 0; slot < getInputSlotCount(); slot++) inputs.setItem(slot, inventory.getStackInSlot(slot));
        return level.getRecipeManager().getAllRecipesFor(ModRecipes.MACHINE_TYPE.get()).stream()
            .filter(recipe -> recipe.machine() == getMachine() && recipe.matches(inputs, level)).findFirst();
    }

    private boolean canOutput(ItemStack result) {
        ItemStack output = inventory.getStackInSlot(getOutputSlot());
        return output.isEmpty() || (ItemStack.isSameItemSameTags(output, result) && output.getCount() + result.getCount() <= output.getMaxStackSize());
    }

    private void process(MachineRecipe recipe) {
        ItemStack result = recipe.result();
        ItemStack output = inventory.getStackInSlot(getOutputSlot());
        if (output.isEmpty()) inventory.setStackInSlot(getOutputSlot(), result);
        else output.grow(result.getCount());
        for (int slot = 0; slot < recipe.getIngredients().size(); slot++) {
            inventory.extractItem(slot, 1, false);
        }
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        inventory.deserializeNBT(tag.getCompound("Inventory"));
        energy.deserializeNBT(tag.get("Energy"));
        progress = tag.getInt("Progress");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", inventory.serializeNBT());
        tag.put("Energy", energy.serializeNBT());
        tag.putInt("Progress", progress);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        energyCap.invalidate();
        itemCap.invalidate();
    }

    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable net.minecraft.core.Direction side) {
        if (cap == ForgeCapabilities.ENERGY) return energyCap.cast();
        if (cap == ForgeCapabilities.ITEM_HANDLER) return itemCap.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.fluxmachines." + (kind() == 2 ? "alloy_furnace" : kind() == 1 ? "wire_mill" : "pulverizer"));
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new ProcessingMachineMenu(id, playerInventory, this, data);
    }
}
