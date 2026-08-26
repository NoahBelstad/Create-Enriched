package com.noahbelstad.create_enriched.fluid;

import com.noahbelstad.create_enriched.CreateEnriched;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.resources.ResourceLocation;

public class CreateEnrichedFluids {

    public static final FluidEntry<VirtualFluid> STEAM_LIQUID = CreateEnriched.REGISTRATE
            .virtualFluid("steam_liquid",
                    ResourceLocation.withDefaultNamespace("block/water_still"),
                    ResourceLocation.withDefaultNamespace("block/water_flow"))
            .properties(p -> p.density(-500).viscosity(100))
            .register();

    public static void init() {
    }
}