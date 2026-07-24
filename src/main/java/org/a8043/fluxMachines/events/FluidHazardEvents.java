package org.a8043.fluxMachines.events;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.a8043.fluxMachines.Main;
import org.a8043.fluxMachines.registry.ModFluids;

@Mod.EventBusSubscriber(modid = Main.MOD_ID)
public final class FluidHazardEvents {
    @SubscribeEvent
    public static void livingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        Fluid fluid = entity.level().getFluidState(entity.blockPosition()).getType();
        if (is(fluid, ModFluids.INDUSTRIAL_ACID) || is(fluid, ModFluids.SPENT_ACID)) {
            if (entity.tickCount % 10 == 0) {
                entity.hurt(entity.damageSources().magic(), 2.0F);
            }
        } else if (is(fluid, ModFluids.CRYOGENIC_COOLANT)) {
            entity.setTicksFrozen(Math.min(entity.getTicksRequiredToFreeze() + 40, entity.getTicksFrozen() + 4));
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 1, false, false));
        }
    }

    private static boolean is(Fluid fluid, ModFluids.FluidEntry entry) {
        return fluid == entry.source.get() || fluid == entry.flowing.get();
    }

    private FluidHazardEvents() {
    }
}
