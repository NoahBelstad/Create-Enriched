package com.noahbelstad.create_enriched.foundation.data;

import com.noahbelstad.create_enriched.CreateEnriched;
import com.noahbelstad.create_enriched.foundation.data.recipe.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = CreateEnriched.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CreateEnrichedDataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        boolean includeServer = event.includeServer();

        generator.addProvider(includeServer, new CreateEnrichedStandardRecipes(packOutput, lookupProvider));
        generator.addProvider(includeServer, new CreateEnrichedCompactingRecipes(packOutput, lookupProvider));
        generator.addProvider(includeServer, new CreateEnrichedCrushingRecipes(packOutput, lookupProvider));
        generator.addProvider(includeServer, new CreateEnrichedMechanicalCraftingRecipes(packOutput, lookupProvider));
        generator.addProvider(includeServer, new CreateEnrichedMixingRecipes(packOutput, lookupProvider));
        generator.addProvider(includeServer, new CreateEnrichedSplashingRecipes(packOutput, lookupProvider));
    }
}