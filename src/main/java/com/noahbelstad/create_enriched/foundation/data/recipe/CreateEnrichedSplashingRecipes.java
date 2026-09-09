package com.noahbelstad.create_enriched.foundation.data.recipe;

import java.util.concurrent.CompletableFuture;

import com.noahbelstad.create_enriched.AllItems;
import com.simibubi.create.api.data.recipe.BaseRecipeProvider;
import com.simibubi.create.api.data.recipe.WashingRecipeGen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

public class CreateEnrichedSplashingRecipes extends WashingRecipeGen {

    public CreateEnrichedSplashingRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, "create_enriched");
    }

    BaseRecipeProvider.GeneratedRecipe
            RAW_THORIUM_DUST = create("raw_thorium_dust", b -> b
            .require(AllItems.CRUSHED_THORIUM_ORE.get())
            .output(AllItems.RAW_THORIUM_DUST.get(), 1)),

    RAW_THORIUM_FROM_DIRTY_THORIUM_DUST = create("raw_thorium_from_dirty_thorium_dust", b -> b
            .require(AllItems.DIRTY_THORIUM_DUST.get())
            .output(AllItems.RAW_THORIUM_DUST.get(), 1));
}