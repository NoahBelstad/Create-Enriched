package com.noahbelstad.create_enriched.fluid;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.bus.api.IEventBus;

public class CreateEnrichedFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, "create_enriched");
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, "create_enriched");

    public static final DeferredHolder<FluidType, FluidType> STEAM_LIQUID_TYPE = FLUID_TYPES.register("steam_liquid",
            () -> new FluidType(FluidType.Properties.create()
                    .density(-500)
                    .viscosity(100)
            ));

    public static final DeferredHolder<Fluid, FlowingFluid> STEAM_LIQUID_STILL = FLUIDS.register("steam_liquid",
            () -> new BaseFlowingFluid.Source(CreateEnrichedFluids.STEAM_LIQUID_PROPERTIES));
    public static final DeferredHolder<Fluid, FlowingFluid> STEAM_LIQUID_FLOWING = FLUIDS.register("flowing_steam_liquid",
            () -> new BaseFlowingFluid.Flowing(CreateEnrichedFluids.STEAM_LIQUID_PROPERTIES));

    public static final BaseFlowingFluid.Properties STEAM_LIQUID_PROPERTIES = new BaseFlowingFluid.Properties(
            STEAM_LIQUID_TYPE, STEAM_LIQUID_STILL, STEAM_LIQUID_FLOWING
    );

    public static void init(IEventBus eventBus) {
        FLUIDS.register(eventBus);
        FLUID_TYPES.register(eventBus);
    }
}