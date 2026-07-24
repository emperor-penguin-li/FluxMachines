package org.a8043.fluxMachines.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.a8043.fluxMachines.Main;

import java.util.List;
import java.util.function.Consumer;

public final class ModFluids {
    public static final DeferredRegister<FluidType> FLUID_TYPES =
        DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Main.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Main.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MOD_ID);

    public static final FluidEntry INDUSTRIAL_ACID = new FluidEntry("industrial_acid", 0xFFD6E34A, 1200, 1400);
    public static final FluidEntry SPENT_ACID = new FluidEntry("spent_acid", 0xFF76813A, 1300, 1600);
    public static final FluidEntry CRYOGENIC_COOLANT = new FluidEntry("cryogenic_coolant", 0xFF57D9FF, 900, 900);
    public static final FluidEntry NUTRIENT_GEL = new FluidEntry("nutrient_gel", 0xFFE65C89, 1500, 2400);
    public static final FluidEntry TITANIUM_SLURRY = new FluidEntry("titanium_slurry", 0xFF91A7B8, 1700, 2600);
    public static final FluidEntry COBALT_SLURRY = new FluidEntry("cobalt_slurry", 0xFF315FB5, 1700, 2600);
    public static final FluidEntry TUNGSTEN_SLURRY = new FluidEntry("tungsten_slurry", 0xFF4B4F59, 1800, 3000);
    public static final FluidEntry OSMIUM_SLURRY = new FluidEntry("osmium_slurry", 0xFF738FC6, 1800, 3000);
    public static final FluidEntry IRIDIUM_SLURRY = new FluidEntry("iridium_slurry", 0xFFC79CE8, 1900, 3400);
    public static final List<FluidEntry> ALL = List.of(INDUSTRIAL_ACID, SPENT_ACID, CRYOGENIC_COOLANT,
        NUTRIENT_GEL, TITANIUM_SLURRY, COBALT_SLURRY, TUNGSTEN_SLURRY, OSMIUM_SLURRY, IRIDIUM_SLURRY);

    public static void register(IEventBus bus) {
        FLUID_TYPES.register(bus);
        FLUIDS.register(bus);
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }

    public static final class FluidEntry {
        private static final ResourceLocation STILL = ResourceLocation.withDefaultNamespace("block/water_still");
        private static final ResourceLocation FLOW = ResourceLocation.withDefaultNamespace("block/water_flow");

        public final String name;
        public final int tint;
        public final RegistryObject<FluidType> type;
        public final RegistryObject<FlowingFluid> source;
        public final RegistryObject<FlowingFluid> flowing;
        public final RegistryObject<LiquidBlock> block;
        public final RegistryObject<BucketItem> bucket;

        private FluidEntry(String name, int tint, int density, int viscosity) {
            this.name = name;
            this.tint = tint;
            type = FLUID_TYPES.register(name, () -> new FluidType(FluidType.Properties.create()
                .density(density).viscosity(viscosity).canSwim(true).canDrown(false)) {
                @Override
                public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                    consumer.accept(new IClientFluidTypeExtensions() {
                        @Override
                        public ResourceLocation getStillTexture() {
                            return STILL;
                        }

                        @Override
                        public ResourceLocation getFlowingTexture() {
                            return FLOW;
                        }

                        @Override
                        public int getTintColor() {
                            return tint;
                        }
                    });
                }
            });
            source = FLUIDS.register(name, () -> new ForgeFlowingFluid.Source(properties()));
            flowing = FLUIDS.register("flowing_" + name, () -> new ForgeFlowingFluid.Flowing(properties()));
            block = BLOCKS.register(name, () -> new LiquidBlock(source,
                BlockBehaviour.Properties.of().noCollission().strength(100.0F).noLootTable()));
            bucket = ITEMS.register(name + "_bucket", () -> new BucketItem(source,
                new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
        }

        private ForgeFlowingFluid.Properties properties() {
            return new ForgeFlowingFluid.Properties(type, source, flowing).block(block).bucket(bucket)
                .slopeFindDistance(3).levelDecreasePerBlock(2).explosionResistance(100.0F);
        }
    }

    private ModFluids() {
    }
}
