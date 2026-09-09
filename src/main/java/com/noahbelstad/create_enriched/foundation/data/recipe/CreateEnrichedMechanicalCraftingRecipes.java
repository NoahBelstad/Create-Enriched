package com.noahbelstad.create_enriched.foundation.data.recipe;

import java.util.concurrent.CompletableFuture;

import com.noahbelstad.create_enriched.AllItems;
import com.simibubi.create.api.data.recipe.MechanicalCraftingRecipeGen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.crafting.Ingredient;

public class CreateEnrichedMechanicalCraftingRecipes extends MechanicalCraftingRecipeGen {

    public CreateEnrichedMechanicalCraftingRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, "create_enriched");
    }

    GeneratedRecipe
            GRAPHITE_ROD = create(AllItems.GRAPHITE_ROD::get)
            .recipe(b -> b
                    .key('A', Ingredient.of(AllItems.GRAPHITE_INGOT.get()))
                    .patternLine("A")
                    .patternLine("A")
                    .patternLine("A")
                    .patternLine("A")
                    .patternLine("A")
                    .disallowMirrored());
}