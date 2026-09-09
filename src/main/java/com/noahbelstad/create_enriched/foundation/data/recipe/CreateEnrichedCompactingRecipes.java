package com.noahbelstad.create_enriched.foundation.data.recipe;

import java.util.concurrent.CompletableFuture;

import com.noahbelstad.create_enriched.AllItems;
import com.simibubi.create.api.data.recipe.CompactingRecipeGen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;

public class CreateEnrichedCompactingRecipes extends CompactingRecipeGen {

    public CreateEnrichedCompactingRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, "create_enriched");
    }

    GeneratedRecipe
            COAL_FROM_DUST = create("coal_from_dust", b -> b
            .require(AllItems.COAL_DUST.get())
            .require(AllItems.COAL_DUST.get())
            .output(Items.COAL)),

    GRAPHITE_NUGGET = create("graphite_nugget", b -> b
            .require(AllItems.GRAPHITE_DUST.get())
            .require(AllItems.GRAPHITE_DUST.get())
            .output(AllItems.GRAPHITE_NUGGET.get())
            .output(0.25f, AllItems.GRAPHITE_NUGGET.get())),

    LOW_PURITY_THORIUM_PELLET = create("low_purity_thorium_pellet", b -> b
            .require(AllItems.LOW_PURITY_ENRICHED_THORIUM_DUST.get())
            .output(AllItems.LOW_PURITY_ENRICHED_THORIUM_PELLET.get()));
}