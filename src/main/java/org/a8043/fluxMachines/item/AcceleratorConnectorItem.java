package org.a8043.fluxMachines.item;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.a8043.fluxMachines.blockentity.AcceleratorBlockEntity;
import org.jetbrains.annotations.NotNull;

public class AcceleratorConnectorItem extends Item {
    private static final String SELECTED_DIMENSION = "SelectedDimension";
    private static final String SELECTED_POS = "SelectedPos";

    public AcceleratorConnectorItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult onItemUseFirst(@NotNull ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (context.getPlayer() == null) {
            return InteractionResult.PASS;
        }

        BlockPos clickedPos = context.getClickedPos();
        BlockEntity clickedEntity = level.getBlockEntity(clickedPos);
        CompoundTag tag = context.getItemInHand().getOrCreateTag();
        if (context.getPlayer().isShiftKeyDown()) {
            if (!(clickedEntity instanceof AcceleratorBlockEntity)) {
                context.getPlayer().sendSystemMessage(Component.translatable("message.fluxmachines.select_accelerator_failed"));
                return InteractionResult.CONSUME;
            }
            tag.putString(SELECTED_DIMENSION, level.dimension().location().toString());
            tag.putLong(SELECTED_POS, clickedPos.asLong());
            context.getPlayer().sendSystemMessage(Component.translatable("message.fluxmachines.accelerator_selected"));
            return InteractionResult.CONSUME;
        }

        if (!tag.contains(SELECTED_POS, Tag.TAG_LONG)) {
            context.getPlayer().sendSystemMessage(Component.translatable("message.fluxmachines.no_accelerator_selected"));
            return InteractionResult.CONSUME;
        }
        if (!level.dimension().location().toString().equals(tag.getString(SELECTED_DIMENSION))) {
            context.getPlayer().sendSystemMessage(Component.translatable("message.fluxmachines.wrong_dimension"));
            return InteractionResult.CONSUME;
        }
        BlockPos acceleratorPos = BlockPos.of(tag.getLong(SELECTED_POS));
        if (!(level.getBlockEntity(acceleratorPos) instanceof AcceleratorBlockEntity accelerator)) {
            tag.remove(SELECTED_POS);
            tag.remove(SELECTED_DIMENSION);
            context.getPlayer().sendSystemMessage(Component.translatable("message.fluxmachines.selected_accelerator_missing"));
            return InteractionResult.CONSUME;
        }
        if (clickedEntity == null) {
            context.getPlayer().sendSystemMessage(Component.translatable("message.fluxmachines.target_not_block_entity"));
            return InteractionResult.CONSUME;
        }
        if (clickedEntity instanceof AcceleratorBlockEntity) {
            context.getPlayer().sendSystemMessage(Component.translatable("message.fluxmachines.accelerator_target_forbidden"));
            return InteractionResult.CONSUME;
        }
        if (accelerator.hasTarget(clickedPos)) {
            accelerator.removeTarget(clickedPos);
            context.getPlayer().sendSystemMessage(Component.translatable("message.fluxmachines.target_unbound"));
        } else if (accelerator.addTarget(clickedPos)) {
            context.getPlayer().sendSystemMessage(Component.translatable("message.fluxmachines.target_bound"));
        } else {
            context.getPlayer().sendSystemMessage(Component.translatable("message.fluxmachines.connection_limit"));
        }
        return InteractionResult.CONSUME;
    }
}
