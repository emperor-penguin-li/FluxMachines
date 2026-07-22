package org.a8043.fluxMachines;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import org.a8043.fluxMachines.config.FlightRingConfig;
import org.a8043.fluxMachines.item.ElectricFlightRingItem;
import org.a8043.fluxMachines.registry.ModItems;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = Main.MOD_ID)
public final class FlightRingEvents {
    private static final String GRANTED_TAG = "fluxmachinesFlightGranted";
    private static final String SPRINT_KEY_TAG = "fluxmachinesSprintKeyDown";

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide || !(event.player instanceof ServerPlayer player)) return;
        if (player.isCreative() || player.isSpectator()) return;
        Optional<SlotResult> found = CuriosApi.getCuriosInventory(player).resolve().flatMap(h -> h.findFirstCurio(ModItems.ELECTRIC_FLIGHT_RING.get()));
        if (found.isEmpty()) {
            revoke(player);
            return;
        }
        ItemStack ring = found.get().stack();
        int normal = FlightRingConfig.INSTANCE.getFlightCost().get();
        boolean canFly = ElectricFlightRingItem.getEnergy(ring) >= normal;
        if (!canFly) {
            revoke(player);
            return;
        }
        grant(player);
        int boostCost = FlightRingConfig.INSTANCE.getBoostCost().get();
        boolean boosted = player.getAbilities().flying
                && player.getPersistentData().getBoolean(SPRINT_KEY_TAG)
                && ElectricFlightRingItem.getEnergy(ring) >= normal + boostCost;
        if (player.getAbilities().flying) {
            ElectricFlightRingItem.consume(ring, normal + (boosted ? boostCost : 0));
            if (boosted) applyBoost(player);
        }
        player.onUpdateAbilities();
    }

    @SubscribeEvent
    public static void fall(LivingFallEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || event.getEntity().level().isClientSide || event.getDistance() <= 0) return;
        Optional<SlotResult> found = CuriosApi.getCuriosInventory(player).resolve().flatMap(h -> h.findFirstCurio(ModItems.ELECTRIC_FLIGHT_RING.get()));
        if (found.isEmpty()) return;
        ItemStack ring = found.get().stack();
        int blocks = (int) Math.ceil(event.getDistance());
        int affordable = ElectricFlightRingItem.getEnergy(ring) / Math.max(1, FlightRingConfig.INSTANCE.getFallCost().get());
        int protectedBlocks = Math.min(blocks, affordable);
        ElectricFlightRingItem.consume(ring, protectedBlocks * FlightRingConfig.INSTANCE.getFallCost().get());
        if (protectedBlocks >= blocks) event.setCanceled(true);
        else event.setDistance(Math.max(0, event.getDistance() - protectedBlocks));
    }

    private static void grant(Player player) {
        if (!player.getPersistentData().getBoolean(GRANTED_TAG)) {
            player.getPersistentData().putBoolean(GRANTED_TAG, true);
        }
        player.getAbilities().mayfly = true;
    }

    private static void revoke(Player player) {
        if (!player.getPersistentData().getBoolean(GRANTED_TAG)) return;
        player.getAbilities().mayfly = false;
        player.getAbilities().flying = false;
        player.getPersistentData().remove(GRANTED_TAG);
        player.onUpdateAbilities();
    }

    /** Applies the acceleration used by the original ring instead of changing
     * the player's creative flying-speed attribute. */
    private static void applyBoost(ServerPlayer player) {
        Vec3 look = player.getLookAngle();
        Vec3 motion = player.getDeltaMovement();
        player.setDeltaMovement(motion.scale(0.5D).add(look.scale(1.15D)));
        player.hurtMarked = true;

        ServerLevel level = player.serverLevel();
        RandomSource random = level.random;
        float pitch = player.getXRot();
        float yaw = player.getYRot();
        for (int i = 0; i < 3; i++) {
            float particleYaw = yaw + (random.nextFloat() * 80.0F - 40.0F);
            float particlePitch = pitch + (random.nextFloat() * 80.0F - 40.0F);
            float yawRad = particleYaw * ((float) Math.PI / 180F);
            float pitchRad = particlePitch * ((float) Math.PI / 180F);
            Vec3 direction = new Vec3(
                    -Mth.sin(yawRad) * Mth.cos(pitchRad),
                    -Mth.sin(pitchRad),
                    Mth.cos(yawRad) * Mth.cos(pitchRad));
            level.sendParticles(ParticleTypes.FIREWORK,
                    player.getX(), player.getY() + player.getEyeHeight() - 0.2D, player.getZ(),
                    1, direction.x, direction.y, direction.z, 0.0D);
        }
    }

    private FlightRingEvents() {}
}
