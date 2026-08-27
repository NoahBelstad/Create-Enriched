package com.noahbelstad.create_enriched.worldgen;

import com.noahbelstad.create_enriched.CreateEnriched;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CreateEnrichedFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, CreateEnriched.MODID);

    public static final DeferredHolder<Feature<?>, BedrockDepositFeature> BEDROCK_DEPOSIT =
            FEATURES.register("bedrock_deposit", BedrockDepositFeature::new);

    public static void register(IEventBus modEventBus) {
        FEATURES.register(modEventBus);
    }
}