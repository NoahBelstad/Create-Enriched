package com.noahbelstad.create_enriched;

import com.noahbelstad.create_enriched.infrastructure.worldgen.BedrockSteamVentFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AllFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, CreateEnriched.MODID);

    public static final DeferredHolder<Feature<?>, BedrockSteamVentFeature> BEDROCK_STEAM_VENT =
            FEATURES.register("bedrock_steam_vent", BedrockSteamVentFeature::new);

    public static void register(IEventBus modEventBus) {
        FEATURES.register(modEventBus);
    }
}