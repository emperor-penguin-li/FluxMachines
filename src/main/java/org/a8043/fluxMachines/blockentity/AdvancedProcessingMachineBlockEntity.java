package org.a8043.fluxMachines.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.a8043.fluxMachines.menu.AdvancedProcessingMachineMenu;
import org.a8043.fluxMachines.recipe.AdvancedProcessingRecipe;
import org.a8043.fluxMachines.registry.ModBlockEntities;
import org.a8043.fluxMachines.registry.ModRecipes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class AdvancedProcessingMachineBlockEntity extends BlockEntity implements MenuProvider {
    private final AdvancedMachineSpec spec;
    private final ItemStackHandler inventory;
    private final MachineEnergyStorage energy;
    private final FluidTank[] tanks;
    private final IItemHandler inputItems = new MachineItemHandler(false);
    private final IItemHandler outputItems = new MachineItemHandler(true);
    private final IFluidHandler inputFluids = new MachineFluidHandler(FluidMode.INPUT);
    private final IFluidHandler outputFluids = new MachineFluidHandler(FluidMode.OUTPUT);
    private final IFluidHandler interactionFluids = new MachineFluidHandler(FluidMode.BOTH);
    private final LazyOptional<IEnergyStorage> energyCap;
    private final LazyOptional<IItemHandler> inputItemCap;
    private final LazyOptional<IItemHandler> outputItemCap;
    private final LazyOptional<IFluidHandler> inputFluidCap;
    private final LazyOptional<IFluidHandler> outputFluidCap;
    private int progress;
    private int duration;
    private int activeEnergyCost;
    private ResourceLocation activeRecipe;
    private List<ItemStack> lockedItemOutputs = List.of();
    private List<FluidStack> lockedFluidOutputs = List.of();

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            int stored = energy.getEnergyStored();
            return switch (index) {
                case 0 -> stored & 0x7FFF;
                case 1 -> (stored >>> 15) & 0x7FFF;
                case 2 -> (stored >>> 30) & 0x3;
                case 3 -> progress;
                case 4 -> duration;
                case 5 -> tanks[0].getFluidAmount();
                case 6 -> tanks[1].getFluidAmount();
                case 7 -> tanks[2].getFluidAmount();
                case 8 -> tanks[3].getFluidAmount();
                case 9 -> activeEnergyCost & 0x7FFF;
                case 10 -> (activeEnergyCost >>> 15) & 0x7FFF;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            if (index == 3) {
                progress = value;
            }
            if (index == 4) {
                duration = value;
            }
        }

        @Override
        public int getCount() {
            return 11;
        }
    };

    public AdvancedProcessingMachineBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ADVANCED_PROCESSING_MACHINE.get(), pos, state);
        spec = AdvancedMachineSpec.fromState(state);
        inventory = new ItemStackHandler(spec.totalSlots()) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
        energy = new MachineEnergyStorage(spec.energyCapacity(), spec.maxReceive());
        tanks = new FluidTank[]{tank(), tank(), tank(), tank()};
        energyCap = LazyOptional.of(() -> energy);
        inputItemCap = LazyOptional.of(() -> inputItems);
        outputItemCap = LazyOptional.of(() -> outputItems);
        inputFluidCap = LazyOptional.of(() -> inputFluids);
        outputFluidCap = LazyOptional.of(() -> outputFluids);
    }

    private FluidTank tank() {
        return new FluidTank(spec.tankCapacity()) {
            @Override
            protected void onContentsChanged() {
                setChanged();
            }
        };
    }

    public AdvancedMachineSpec getSpec() {
        return spec;
    }

    public IItemHandler getInventory() {
        return inventory;
    }

    public IFluidHandler getInteractionFluidHandler() {
        return interactionFluids;
    }

    public ContainerData getData() {
        return data;
    }

    public int getEnergyCapacity() {
        return energy.getMaxEnergyStored();
    }

    public int getTankCapacity() {
        return spec.tankCapacity();
    }

    public boolean isValidInput(int slot, ItemStack stack) {
        if (level == null || slot < 0 || slot >= spec.inputSlots() || stack.isEmpty()) {
            return false;
        }
        return level.getRecipeManager().getAllRecipesFor(ModRecipes.ADVANCED_TYPE.get()).stream()
            .filter(recipe -> recipe.machine() == spec.machine() && recipe.itemInputs().size() > slot)
            .anyMatch(recipe -> recipe.itemInputs().get(slot).ingredient().test(stack));
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state,
                                  AdvancedProcessingMachineBlockEntity machine) {
        if (level.isClientSide) {
            return;
        }
        if (machine.activeRecipe == null) {
            machine.tryStartRecipe();
        }
        if (machine.activeRecipe == null) {
            return;
        }
        if (machine.progress < machine.duration) {
            if (machine.energy.consume(machine.activeEnergyCost)) {
                machine.progress++;
                machine.setChanged();
            }
            return;
        }
        if (machine.canAcceptLockedOutputs()) {
            machine.finishRecipe();
        }
    }

    private void tryStartRecipe() {
        if (level == null) {
            return;
        }
        for (AdvancedProcessingRecipe recipe : level.getRecipeManager().getAllRecipesFor(ModRecipes.ADVANCED_TYPE.get())) {
            if (recipe.machine() != spec.machine() || !matches(recipe)) {
                continue;
            }
            consumeInputs(recipe);
            activeRecipe = recipe.getId();
            duration = recipe.duration();
            activeEnergyCost = recipe.energyPerTick();
            progress = 0;
            lockedItemOutputs = recipe.itemOutputs();
            lockedFluidOutputs = recipe.fluidOutputs();
            setChanged();
            return;
        }
    }

    private boolean matches(AdvancedProcessingRecipe recipe) {
        if (recipe.itemInputs().size() > spec.inputSlots() || recipe.fluidInputs().size() > 2) {
            return false;
        }
        for (int slot = 0; slot < recipe.itemInputs().size(); slot++) {
            var input = recipe.itemInputs().get(slot);
            ItemStack stack = inventory.getStackInSlot(slot);
            if (!input.ingredient().test(stack) || stack.getCount() < input.count()) {
                return false;
            }
        }
        for (int tank = 0; tank < recipe.fluidInputs().size(); tank++) {
            FluidStack required = recipe.fluidInputs().get(tank);
            FluidStack stored = tanks[tank].getFluid();
            if (!stored.isFluidEqual(required) || stored.getAmount() < required.getAmount()) {
                return false;
            }
        }
        return canAccept(recipe.itemOutputs(), recipe.fluidOutputs());
    }

    private void consumeInputs(AdvancedProcessingRecipe recipe) {
        for (int slot = 0; slot < recipe.itemInputs().size(); slot++) {
            inventory.extractItem(slot, recipe.itemInputs().get(slot).count(), false);
        }
        for (int tank = 0; tank < recipe.fluidInputs().size(); tank++) {
            tanks[tank].drain(recipe.fluidInputs().get(tank).getAmount(), IFluidHandler.FluidAction.EXECUTE);
        }
    }

    private boolean canAcceptLockedOutputs() {
        return canAccept(lockedItemOutputs, lockedFluidOutputs);
    }

    private boolean canAccept(List<ItemStack> itemOutputsToCheck, List<FluidStack> fluidOutputsToCheck) {
        if (itemOutputsToCheck.size() > 2 || fluidOutputsToCheck.size() > 2) {
            return false;
        }
        for (int index = 0; index < itemOutputsToCheck.size(); index++) {
            ItemStack result = itemOutputsToCheck.get(index);
            ItemStack stored = inventory.getStackInSlot(spec.outputStart() + index);
            if (!stored.isEmpty() && (!ItemStack.isSameItemSameTags(stored, result)
                                      || stored.getCount() + result.getCount() > stored.getMaxStackSize())) {
                return false;
            }
        }
        for (int index = 0; index < fluidOutputsToCheck.size(); index++) {
            FluidStack result = fluidOutputsToCheck.get(index);
            FluidTank tank = tanks[2 + index];
            if (!tank.isEmpty() && !tank.getFluid().isFluidEqual(result)) {
                return false;
            }
            if (tank.getFluidAmount() + result.getAmount() > tank.getCapacity()) {
                return false;
            }
        }
        return true;
    }

    private void finishRecipe() {
        for (int index = 0; index < lockedItemOutputs.size(); index++) {
            int slot = spec.outputStart() + index;
            ItemStack result = lockedItemOutputs.get(index).copy();
            ItemStack stored = inventory.getStackInSlot(slot);
            if (stored.isEmpty()) {
                inventory.setStackInSlot(slot, result);
            } else {
                stored.grow(result.getCount());
            }
        }
        for (int index = 0; index < lockedFluidOutputs.size(); index++) {
            tanks[2 + index].fill(lockedFluidOutputs.get(index), IFluidHandler.FluidAction.EXECUTE);
        }
        clearJob();
        setChanged();
    }

    private void clearJob() {
        activeRecipe = null;
        progress = 0;
        duration = 0;
        activeEnergyCost = 0;
        lockedItemOutputs = List.of();
        lockedFluidOutputs = List.of();
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        inventory.deserializeNBT(tag.getCompound("Inventory"));
        energy.deserializeNBT(tag.get("Energy"));
        for (int i = 0; i < tanks.length; i++) {
            tanks[i].readFromNBT(tag.getCompound("Tank" + i));
        }
        progress = tag.getInt("Progress");
        duration = tag.getInt("Duration");
        activeEnergyCost = tag.getInt("ActiveEnergyCost");
        if (tag.contains("ActiveRecipe")) {
            activeRecipe = ResourceLocation.tryParse(tag.getString("ActiveRecipe"));
        }
        lockedItemOutputs = readItems(tag.getList("LockedItems", Tag.TAG_COMPOUND));
        lockedFluidOutputs = readFluids(tag.getList("LockedFluids", Tag.TAG_COMPOUND));
        if (activeRecipe != null && lockedItemOutputs.isEmpty() && lockedFluidOutputs.isEmpty()) {
            clearJob();
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", inventory.serializeNBT());
        tag.put("Energy", energy.serializeNBT());
        for (int i = 0; i < tanks.length; i++) {
            tag.put("Tank" + i, tanks[i].writeToNBT(new CompoundTag()));
        }
        tag.putInt("Progress", progress);
        tag.putInt("Duration", duration);
        tag.putInt("ActiveEnergyCost", activeEnergyCost);
        if (activeRecipe != null) {
            tag.putString("ActiveRecipe", activeRecipe.toString());
        }
        tag.put("LockedItems", writeItems(lockedItemOutputs));
        tag.put("LockedFluids", writeFluids(lockedFluidOutputs));
    }

    private static ListTag writeItems(List<ItemStack> stacks) {
        ListTag list = new ListTag();
        stacks.forEach(stack -> list.add(stack.save(new CompoundTag())));
        return list;
    }

    private static List<ItemStack> readItems(ListTag list) {
        List<ItemStack> result = new ArrayList<>();
        list.forEach(tag -> result.add(ItemStack.of((CompoundTag) tag)));
        return result;
    }

    private static ListTag writeFluids(List<FluidStack> stacks) {
        ListTag list = new ListTag();
        stacks.forEach(stack -> list.add(stack.writeToNBT(new CompoundTag())));
        return list;
    }

    private static List<FluidStack> readFluids(ListTag list) {
        List<FluidStack> result = new ArrayList<>();
        list.forEach(tag -> result.add(FluidStack.loadFluidStackFromNBT((CompoundTag) tag)));
        return result;
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        energyCap.invalidate();
        inputItemCap.invalidate();
        outputItemCap.invalidate();
        inputFluidCap.invalidate();
        outputFluidCap.invalidate();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return energyCap.cast();
        }
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return side == Direction.DOWN ? outputItemCap.cast() : inputItemCap.cast();
        }
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return side == Direction.DOWN ? outputFluidCap.cast() : inputFluidCap.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.fluxmachines." + spec.machine().id());
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory,
                                                      @NotNull Player player) {
        return new AdvancedProcessingMachineMenu(id, playerInventory, this, data);
    }

    private final class MachineEnergyStorage extends EnergyStorage {
        private MachineEnergyStorage(int capacity, int maxReceive) {
            super(capacity, maxReceive, 0);
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int received = super.receiveEnergy(maxReceive, simulate);
            if (received > 0 && !simulate) {
                setChanged();
            }
            return received;
        }

        private boolean consume(int amount) {
            if (amount <= 0 || energy < amount) {
                return false;
            }
            energy -= amount;
            setChanged();
            return true;
        }
    }

    private final class MachineItemHandler implements IItemHandler {
        private final boolean output;

        private MachineItemHandler(boolean output) {
            this.output = output;
        }

        @Override
        public int getSlots() {
            return inventory.getSlots();
        }

        @Override
        public @NotNull ItemStack getStackInSlot(int slot) {
            return inventory.getStackInSlot(slot);
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return !output && slot < spec.inputSlots() && isValidInput(slot, stack)
                ? inventory.insertItem(slot, stack, simulate) : stack;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return output && slot >= spec.outputStart() ? inventory.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            return inventory.getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return !output && slot < spec.inputSlots() && isValidInput(slot, stack);
        }
    }

    private enum FluidMode {INPUT, OUTPUT, BOTH}

    private final class MachineFluidHandler implements IFluidHandler {
        private final FluidMode mode;

        private MachineFluidHandler(FluidMode mode) {
            this.mode = mode;
        }

        @Override
        public int getTanks() {
            return tanks.length;
        }

        @Override
        public @NotNull FluidStack getFluidInTank(int tank) {
            return tanks[tank].getFluid();
        }

        @Override
        public int getTankCapacity(int tank) {
            return tanks[tank].getCapacity();
        }

        @Override
        public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            return tank < 2 && mode != FluidMode.OUTPUT;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (mode == FluidMode.OUTPUT || resource.isEmpty()) {
                return 0;
            }
            for (int i = 0; i < 2; i++) {
                int filled = tanks[i].fill(resource, action);
                if (filled > 0) {
                    return filled;
                }
            }
            return 0;
        }

        @Override
        public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
            if (mode == FluidMode.INPUT || resource.isEmpty()) {
                return FluidStack.EMPTY;
            }
            for (int i = 2; i < 4; i++) {
                FluidStack drained = tanks[i].drain(resource, action);
                if (!drained.isEmpty()) {
                    return drained;
                }
            }
            return FluidStack.EMPTY;
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
            if (mode == FluidMode.INPUT || maxDrain <= 0) {
                return FluidStack.EMPTY;
            }
            for (int i = 2; i < 4; i++) {
                FluidStack drained = tanks[i].drain(maxDrain, action);
                if (!drained.isEmpty()) {
                    return drained;
                }
            }
            return FluidStack.EMPTY;
        }
    }
}
