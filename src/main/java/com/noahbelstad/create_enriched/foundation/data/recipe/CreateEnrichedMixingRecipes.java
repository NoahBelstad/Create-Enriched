package com.noahbelstad.create_enriched.foundation.data.recipe;

import java.util.concurrent.CompletableFuture;

import com.noahbelstad.create_enriched.AllFluids;
import com.noahbelstad.create_enriched.AllItems;
import com.simibubi.create.api.data.recipe.MixingRecipeGen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;

public class CreateEnrichedMixingRecipes extends MixingRecipeGen {

    public CreateEnrichedMixingRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, "create_enriched");
    }

    GeneratedRecipe
            LOW_PURITY_ENRICHED_THORIUM_DUST = create("low_purity_enriched_thorium_dust", b -> b
            .require(AllFluids.LOW_PURITY_THORIUM_SOLUTION.get(), 250)
            .require(AllItems.LIMESTONE_DUST.get())
            .duration(150)
            .output(AllItems.LOW_PURITY_ENRICHED_THORIUM_DUST.get())
            .output(AllItems.DIRTY_THORIUM_DUST.get())),

    LOW_PURITY_SULFUR_DIOXIDE = create("low_purity_sulfur_dioxide", b -> b
            .require(AllItems.SULFUR_DUST.get())
            .duration(80)
            .output(AllFluids.LOW_PURITY_SULFUR_DIOXIDE.get(), 250)),

    LOW_PURITY_SULFUR_TRIOXIDE = create("low_purity_sulfur_trioxide", b -> b
            .require(AllFluids.LOW_PURITY_SULFUR_DIOXIDE.get(), 250)
            .require(AllItems.PLATINUM_NUGGET.get())
            .duration(100)
            .output(AllFluids.LOW_PURITY_SULFUR_TRIOXIDE.get(), 250)
            .output(AllItems.PLATINUM_NUGGET.get())),

    LOW_PURITY_SULFURIC_ACID = create("low_purity_sulfuric_acid", b -> b
            .require(AllFluids.LOW_PURITY_SULFUR_TRIOXIDE.get(), 250)
            .require(Fluids.WATER, 250)
            .duration(100)
            .output(AllFluids.LOW_PURITY_SULFURIC_ACID.get(), 250)),

    LOW_PURITY_SULFURIC_ACID_SCALING = create("low_purity_sulfuric_acid_scaling", b -> b
            .require(AllFluids.LOW_PURITY_SULFURIC_ACID.get(), 250)
            .require(Fluids.WATER, 250)
            .require(Items.IRON_NUGGET)
            .duration(150)
            .output(AllFluids.LOW_PURITY_SULFURIC_ACID.get(), 250)),

    LOW_PURITY_THORIUM_SOLUTION = create("low_purity_thorium_solution", b -> b
            .require(AllFluids.LOW_PURITY_SULFURIC_ACID.get(), 250)
            .require(Fluids.WATER, 250)
            .require(AllItems.RAW_THORIUM_DUST.get())
            .duration(150)
            .output(AllFluids.LOW_PURITY_THORIUM_SOLUTION.get(), 250));
}