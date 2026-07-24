package org.a8043.fluxMachines.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.a8043.fluxMachines.Main;
import org.a8043.fluxMachines.menu.ProcessingMachineMenu;

public class ProcessingMachineScreen extends AbstractContainerScreen<ProcessingMachineMenu> {
    private static final ResourceLocation PULVERIZER_TEXTURE = guiTexture("pulverizer");
    private static final ResourceLocation WIRE_MILL_TEXTURE = guiTexture("wire_mill");
    private static final ResourceLocation ALLOY_FURNACE_TEXTURE = guiTexture("alloy_furnace");

    public ProcessingMachineScreen(ProcessingMachineMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 176;
        imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics g, float partialTick, int mouseX, int mouseY) {
        g.blit(getTexture(), leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
        int height = Math.round(52F * menu.getEnergy() / Math.max(1, menu.getCapacity()));
        g.fill(leftPos + 154, topPos + 76 - height, leftPos + 162, topPos + 76, getAccentColor());
        if (menu.getDuration() > 0) {
            int width = Math.round(24F * Math.min(menu.getProgress(), menu.getDuration()) / menu.getDuration());
            g.fill(leftPos + 84, topPos + 39, leftPos + 84 + width, topPos + 47, getAccentColor());
        }
    }

    @Override
    protected void renderLabels(GuiGraphics g, int mouseX, int mouseY) {
        g.drawString(font, title, 8, 6, 0x30363A, false);
        g.drawString(font, Component.translatable("gui.fluxmachines.machine_energy", menu.getEnergy(), menu.getCapacity()), 8, 18, 0x4A5054, false);
        g.drawString(font, Component.translatable("gui.fluxmachines.machine_input"), 26, 59, 0x4A5054, false);
        g.drawString(font, Component.translatable("gui.fluxmachines.machine_output"), 111, 59, 0x4A5054, false);
        g.drawString(font, Component.translatable("container.inventory"), 8, 73, 0x4A5054, false);
    }

    private ResourceLocation getTexture() {
        return switch (menu.getMachineKind()) {
            case 1 -> WIRE_MILL_TEXTURE;
            case 2 -> ALLOY_FURNACE_TEXTURE;
            default -> PULVERIZER_TEXTURE;
        };
    }

    private int getAccentColor() {
        return switch (menu.getMachineKind()) {
            case 1 -> 0xFFD58A32;
            case 2 -> 0xFFE25A32;
            default -> 0xFF49AFC4;
        };
    }

    private static ResourceLocation guiTexture(String machine) {
        return ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "textures/gui/" + machine + ".png");
    }
}
