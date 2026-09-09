package com.noahbelstad.create_enriched.foundation.data.recipe;

import java.util.concurrent.CompletableFuture;

import com.noahbelstad.create_enriched.AllItems;
import com.simibubi.create.api.data.recipe.CrushingRecipeGen;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;

public class CreateEnrichedCrushingRecipes extends CrushingRecipeGen {

    public CreateEnrichedCrushingRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, "create_enriched");
    }

    GeneratedRecipe
            COAL_DUST = create("coal_dust", b -> b
            .require(ItemTags.COALS)
            .duration(150)
            .output(AllItems.COAL_DUST.get(), 1)
            .output(0.25f, AllItems.COAL_DUST.get(), 1)
            .output(0.25f, AllItems.GRAPHITE_DUST.get(), 1)),

    CRUSHED_PLATINUM = create("crushed_platinum", b -> b
            .require(AllItems.RAW_PLATINUM.get())
            .duration(150)
            .output(AllItems.CRUSHED_PLATINUM.get(), 1)
            .output(0.25f, AllItems.CRUSHED_PLATINUM.get(), 1)),

    CRUSHED_THORIUM_ORE = create("crushed_thorium_ore", b -> b
            .require(AllItems.RAW_THORIUM_ORE.get())
            .duration(100)
            .output(AllItems.CRUSHED_THORIUM_ORE.get(), 1)),

    LIMESTONE_DUST = create("limestone_dust", b -> b
            .require(AllPaletteStoneTypes.LIMESTONE.getBaseBlock().get())
            .duration(150)
            .output(AllItems.LIMESTONE_DUST.get(), 1)
            .output(0.75f, AllItems.LIMESTONE_DUST.get(), 1)),

    SULFUR_DUST = create("sulfur_dust", b -> b
            .require(AllItems.RAW_SULFUR.get())
            .duration(100)
            .output(AllItems.SULFUR_DUST.get(), 1));
}