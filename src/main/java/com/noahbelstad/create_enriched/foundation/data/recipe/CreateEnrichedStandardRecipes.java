package com.noahbelstad.create_enriched.foundation.data.recipe;

import com.noahbelstad.create_enriched.AllItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class CreateEnrichedStandardRecipes extends RecipeProvider {

    public CreateEnrichedStandardRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        // Blasting
        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(AllItems.CRUSHED_PLATINUM.get()),
                        RecipeCategory.MISC,
                        AllItems.PLATINUM_INGOT.get(),
                        0.5f,
                        100
                ).unlockedBy("has_crushed_platinum", has(AllItems.CRUSHED_PLATINUM.get()))
                .save(output, "create_enriched:platinum_ingot_from_crushed");

        // Shaped Crafting
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AllItems.PLATINUM_INGOT.get())
                .pattern("NNN")
                .pattern("NNN")
                .pattern("NNN")
                .define('N', AllItems.PLATINUM_NUGGET.get())
                .unlockedBy("has_platinum_nugget", has(AllItems.PLATINUM_NUGGET.get()))
                .save(output, "create_enriched:platinum_ingot_from_nugget");

        // Shapeless Crafting
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllItems.GRAPHITE_INGOT.get())
                .requires(AllItems.GRAPHITE_NUGGET.get(), 4)
                .unlockedBy("has_graphite_nugget", has(AllItems.GRAPHITE_NUGGET.get()))
                .save(output, "create_enriched:graphite_ingot_from_nugget");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllItems.GRAPHITE_NUGGET.get(), 3)
                .requires(AllItems.GRAPHITE_INGOT.get())
                .unlockedBy("has_graphite_ingot", has(AllItems.GRAPHITE_INGOT.get()))
                .save(output, "create_enriched:graphite_nuggets_from_ingot");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllItems.PLATINUM_NUGGET.get(), 9)
                .requires(AllItems.PLATINUM_INGOT.get())
                .unlockedBy("has_platinum_ingot", has(AllItems.PLATINUM_INGOT.get()))
                .save(output, "create_enriched:platinum_nuggets_from_ingot");
    }
}