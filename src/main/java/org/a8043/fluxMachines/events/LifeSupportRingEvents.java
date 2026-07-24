package org.a8043.fluxMachines.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.a8043.fluxMachines.Main;
import org.a8043.fluxMachines.config.LifeSupportRingConfig;
import org.a8043.fluxMachines.item.ElectricLifeSupportRingItem;
import org.a8043.fluxMachines.registry.ModItems;
import top.theillusivec4.curios.api.CuriosApi;

@Mod.EventBusSubscriber(modid = Main.MOD_ID)
public final class LifeSupportRingEvents {
    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide
            || !(event.player instanceof ServerPlayer player)) {
            return;
        }
        CuriosApi.getCuriosInventory(player).resolve()
            .flatMap(handler -> handler.findFirstCurio(ModItems.ELECTRIC_LIFE_SUPPORT_RING.get()))
            .ifPresent(result -> support(player, result.stack()));
    }

    private static void support(ServerPlayer player, ItemStack stack) {
        ElectricLifeSupportRingItem ring = (ElectricLifeSupportRingItem) stack.getItem();
        if (player.getHealth() < player.getMaxHealth()
            && ring.consumeExact(stack, LifeSupportRingConfig.INSTANCE.getFirstAidCost().get())) {
            player.heal(1.0F);
        }
        FoodData food = player.getFoodData();
        if (food.needsFood() && ring.consumeExact(stack, LifeSupportRingConfig.INSTANCE.getNutritionCost().get())) {
            food.eat(1, 0.2F);
        }
        if (player.getAbsorptionAmount() < 20.0F
            && ring.consumeExact(stack, LifeSupportRingConfig.INSTANCE.getForceFieldCost().get())) {
            player.setAbsorptionAmount(Math.min(20.0F, player.getAbsorptionAmount() + 0.5F));
        }
    }

    private LifeSupportRingEvents() {
    }
}
