package org.a8043.fluxMachines.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.a8043.fluxMachines.Main;
import org.a8043.fluxMachines.menu.AdvancedProcessingMachineMenu;

public final class AdvancedProcessingMachineScreen extends AbstractContainerScreen<AdvancedProcessingMachineMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Main.MOD_ID,
        "textures/gui/advanced_machine.png");
    private static final int[] ACCENTS = {0xFF55D6E8, 0xFFC8D83C, 0xFF6A8DF0, 0xFFF06B3E, 0xFFD764FF};

    public AdvancedProcessingMachineScreen(AdvancedProcessingMachineMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 176;
        imageHeight = 186;
        inventoryLabelY = 92;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
        int accent = ACCENTS[Math.min(menu.getMachineOrdinal(), ACCENTS.length - 1)];
        int energyHeight = Math.round(66.0F * menu.getEnergy() / Math.max(1, menu.getCapacity()));
        graphics.fill(leftPos + 162, topPos + 86 - energyHeight, leftPos + 169, topPos + 86, accent);
        if (menu.getDuration() > 0) {
            int progressWidth = Math.round(28.0F * Math.min(menu.getProgress(), menu.getDuration()) / menu.getDuration());
            graphics.fill(leftPos + 88, topPos + 48, leftPos + 88 + progressWidth, topPos + 54, accent);
        }
        for (int tank = 0; tank < 4; tank++) {
            int tankHeight = Math.round(42.0F * menu.getTankAmount(tank) / Math.max(1, menu.getTankCapacity()));
            int x = leftPos + 6 + tank * 8;
            int color = tank < 2 ? 0xFF46AEE8 : 0xFFE89B46;
            graphics.fill(x, topPos + 78 - tankHeight, x + 5, topPos + 78, color);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, 8, 6, 0x30363A, false);
        graphics.drawString(font, Component.translatable("gui.fluxmachines.advanced_energy",
            menu.getEnergy(), menu.getCapacity(), menu.getEnergyPerTick()), 8, 17, 0x4A5054, false);
        graphics.drawString(font, Component.translatable("container.inventory"), 8, inventoryLabelY, 0x4A5054, false);
    }
}
