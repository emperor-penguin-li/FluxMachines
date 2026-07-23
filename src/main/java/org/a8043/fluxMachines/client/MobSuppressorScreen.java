package org.a8043.fluxMachines.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.a8043.fluxMachines.Main;
import org.a8043.fluxMachines.menu.MobSuppressorMenu;
import org.a8043.fluxMachines.network.NetworkHandler;
import org.a8043.fluxMachines.network.SetMobSuppressorEnabledPacket;

public class MobSuppressorScreen extends AbstractContainerScreen<MobSuppressorMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "textures/gui/mob_suppressor.png");
    private Button toggle;

    public MobSuppressorScreen(MobSuppressorMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 176;
        imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        toggle = addRenderableWidget(Button.builder(toggleLabel(), button -> {
            boolean enabled = !menu.isEnabled();
            NetworkHandler.CHANNEL.sendToServer(new SetMobSuppressorEnabledPacket(menu.getBlockPos(), enabled));
        }).bounds(leftPos + 112, topPos + 94, 56, 20).build());
    }

    @Override
    public void containerTick() {
        super.containerTick();
        toggle.setMessage(toggleLabel());
    }

    private Component toggleLabel() {
        return Component.translatable(menu.isEnabled() ? "gui.fluxmachines.disable" : "gui.fluxmachines.enable");
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics g, int mouseX, int mouseY) {
        g.drawString(font, title, 8, 6, 0x404040, false);
        g.drawString(font, Component.translatable("gui.fluxmachines.energy", menu.getEnergyStored(), menu.getEnergyCapacity()), 8, 34, 0x404040, false);
        g.drawString(font, Component.translatable("gui.fluxmachines.cost", menu.getEnergyPerTick()), 8, 50, 0x404040, false);
        g.drawString(font, Component.translatable("gui.fluxmachines.range", menu.getRange()), 8, 66, 0x404040, false);
        g.drawString(font, Component.translatable("gui.fluxmachines.structure", statusKey(menu.isFormed(), "formed", "incomplete")), 8, 98, 0x404040, false);
        g.drawString(font, Component.translatable("gui.fluxmachines.status", statusKey(menu.isActive(), "running", menu.isEnabled() ? "idle" : "disabled")), 8, 114, 0x404040, false);
    }

    private Component statusKey(boolean condition, String yes, String no) {
        return Component.translatable("gui.fluxmachines." + (condition ? yes : no));
    }
}
