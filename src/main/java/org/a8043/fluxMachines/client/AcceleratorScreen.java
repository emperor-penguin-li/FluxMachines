package org.a8043.fluxMachines.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.a8043.fluxMachines.Main;
import org.a8043.fluxMachines.menu.AcceleratorMenu;
import org.a8043.fluxMachines.network.NetworkHandler;
import org.a8043.fluxMachines.network.SetMultiplierPacket;

public class AcceleratorScreen extends AbstractContainerScreen<AcceleratorMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "textures/gui/accelerator.png");
    private EditBox multiplier;

    public AcceleratorScreen(AcceleratorMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 176;
        imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        multiplier = new EditBox(font, leftPos + 82, topPos + 97, 40, 18, Component.translatable("gui.fluxmachines.multiplier"));
        multiplier.setValue(Integer.toString(menu.getMultiplier()));
        multiplier.setFilter(value -> value.isEmpty() || value.matches("\\d{0,5}"));
        addRenderableWidget(multiplier);
        addRenderableWidget(Button.builder(Component.translatable("gui.fluxmachines.apply"), button -> apply()).bounds(leftPos + 126, topPos + 97, 42, 18).build());
    }

    private void apply() {
        try {
            NetworkHandler.CHANNEL.sendToServer(new SetMultiplierPacket(menu.getBlockPos(), Integer.parseInt(multiplier.getValue())));
        } catch (NumberFormatException ignored) {
            multiplier.setValue(Integer.toString(menu.getMultiplier()));
        }
    }

    @Override
    public void containerTick() {
        super.containerTick();
        if (!multiplier.isFocused()) {
            multiplier.setValue(Integer.toString(menu.getMultiplier()));
        }
    }

    @Override
    public boolean keyPressed(int key, int scan, int modifiers) {
        if (key == 257 && multiplier.isFocused()) {
            apply();
            return true;
        }
        return super.keyPressed(key, scan, modifiers);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics g, int mouseX, int mouseY) {
        g.drawString(font, title, 8, 6, 0x404040, false);
        g.drawString(font, Component.translatable("gui.fluxmachines.energy", menu.getEnergyStored(), menu.getEnergyCapacity()), 8, 34, 0x404040, false);
        g.drawString(font, Component.translatable("gui.fluxmachines.connections", menu.getConnectionCount()), 8, 52, 0x404040, false);
        g.drawString(font, Component.translatable("gui.fluxmachines.cost", menu.getEnergyCost()), 8, 70, 0x404040, false);
        g.drawString(font, Component.translatable("gui.fluxmachines.multiplier"), 8, 101, 0x404040, false);
    }
}
