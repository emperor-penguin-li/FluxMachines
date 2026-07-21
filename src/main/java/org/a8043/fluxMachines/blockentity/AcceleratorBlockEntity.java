package org.a8043.fluxMachines.blockentity;

import lombok.Getter;
import org.a8043.fluxMachines.config.AcceleratorConfig;
import org.a8043.fluxMachines.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.Set;

public class AcceleratorBlockEntity extends BlockEntity implements MenuProvider {
    private static final String ENERGY_TAG = "Energy";
    private static final String MULTIPLIER_TAG = "Multiplier";
    private static final String TARGETS_TAG = "Targets";

    private final Set<BlockPos> targets = new LinkedHashSet<>();
    private final EnergyStorage energy = new EnergyStorage(AcceleratorConfig.ENERGY_CAPACITY.get()) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int received = super.receiveEnergy(maxReceive, simulate);
            if (received > 0 && !simulate) {
                setChanged();
            }
            return received;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            int extracted = super.extractEnergy(maxExtract, simulate);
            if (extracted > 0 && !simulate) {
                setChanged();
            }
            return extracted;
        }
    };
    private final LazyOptional<IEnergyStorage> energyCapability = LazyOptional.of(() -> energy);
    @Getter
    private int multiplier = 1;

    public AcceleratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ACCELERATOR.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AcceleratorBlockEntity accelerator) {
        if (level.isClientSide || accelerator.targets.isEmpty()) {
            return;
        }
        int cost = accelerator.getEnergyCost();
        if (accelerator.energy.getEnergyStored() < cost) {
            return;
        }
        accelerator.energy.extractEnergy(cost, false);
        int extraTicks = accelerator.multiplier - 1;
        for (BlockPos targetPos : accelerator.targets) {
            if (!level.hasChunkAt(targetPos)) {
                continue;
            }
            BlockEntity target = level.getBlockEntity(targetPos);
            if (target == null || target.isRemoved() || target instanceof AcceleratorBlockEntity) {
                continue;
            }
            for (int tick = 0; tick < extraTicks && !target.isRemoved(); tick++) {
                tickTarget(level, targetPos, target);
            }
        }
    }

    private static void tickTarget(Level level, BlockPos pos, BlockEntity target) {
        BlockState targetState = level.getBlockState(pos);
        BlockEntityType<?> type = target.getType();
        if (!(targetState.getBlock() instanceof EntityBlock entityBlock)) {
            return;
        }
        BlockEntityTicker<BlockEntity> ticker = (BlockEntityTicker<BlockEntity>)
            entityBlock.getTicker(level, targetState, type);
        if (ticker != null) {
            ticker.tick(level, pos, targetState, target);
        }
    }

    public boolean addTarget(BlockPos pos) {
        if (targets.size() >= AcceleratorConfig.MAX_CONNECTIONS.get()) {
            return false;
        }
        boolean added = targets.add(pos.immutable());
        if (added) {
            setChanged();
        }
        return added;
    }

    public void removeTarget(BlockPos pos) {
        boolean removed = targets.remove(pos);
        if (removed) {
            setChanged();
        }
    }

    public boolean hasTarget(BlockPos pos) {
        return targets.contains(pos);
    }

    public int getConnectionCount() {
        return targets.size();
    }

    public void setMultiplier(int multiplier) {
        int bounded = Math.max(1, Math.min(multiplier, AcceleratorConfig.MAX_MULTIPLIER.get()));
        if (this.multiplier != bounded) {
            this.multiplier = bounded;
            setChanged();
        }
    }

    public int getEnergyStored() {
        return energy.getEnergyStored();
    }

    public int getEnergyCapacity() {
        return energy.getMaxEnergyStored();
    }

    public int getEnergyCost() {
        long power = 1L;
        for (int exponent = 0; exponent < AcceleratorConfig.ENERGY_EXPONENT.get(); exponent++) {
            power = Math.min(Integer.MAX_VALUE, power * multiplier);
        }
        long cost = Math.min(Integer.MAX_VALUE, power * 10L * targets.size());
        return (int) cost;
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        Tag serializedEnergy = tag.get(ENERGY_TAG);
        energy.deserializeNBT(serializedEnergy instanceof IntTag ? serializedEnergy : IntTag.valueOf(0));
        multiplier = Math.max(1, Math.min(tag.getInt(MULTIPLIER_TAG), AcceleratorConfig.MAX_MULTIPLIER.get()));
        targets.clear();
        ListTag serializedTargets = tag.getList(TARGETS_TAG, Tag.TAG_COMPOUND);
        for (Tag serializedTarget : serializedTargets) {
            targets.add(BlockPos.of(((CompoundTag) serializedTarget).getLong("Pos")));
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(ENERGY_TAG, energy.serializeNBT());
        tag.putInt(MULTIPLIER_TAG, multiplier);
        ListTag serializedTargets = new ListTag();
        for (BlockPos target : targets) {
            CompoundTag targetTag = new CompoundTag();
            targetTag.putLong("Pos", target.asLong());
            serializedTargets.add(targetTag);
        }
        tag.put(TARGETS_TAG, serializedTargets);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag) {
        load(tag);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        energyCapability.invalidate();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable net.minecraft.core.Direction side) {
        if (capability == ForgeCapabilities.ENERGY) {
            return energyCapability.cast();
        }
        return super.getCapability(capability, side);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.fluxmachines.accelerator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        return new org.a8043.fluxMachines.menu.AcceleratorMenu(id, this);
    }
}
