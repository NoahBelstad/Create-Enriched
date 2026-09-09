package com.noahbelstad.create_enriched.infrastructure.worldgen;

import com.noahbelstad.create_enriched.AllBlocks;
import com.noahbelstad.create_enriched.AllFeatures;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.levelgen.placement.*;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;

import java.util.List;

public class ModWorldGenBootstrap {

    public static void bootstrapConfiguredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        var deepslateReplaceable = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        // Bedrock Steam Vent
        context.register(
                ModWorldGenKeys.BEDROCK_STEAM_VENT_CONFIGURED,
                new ConfiguredFeature<>(AllFeatures.BEDROCK_STEAM_VENT.get(), NoneFeatureConfiguration.INSTANCE)
        );

        // Thorium Ore
        List<OreConfiguration.TargetBlockState> thoriumTargets = List.of(
                OreConfiguration.target(deepslateReplaceable, AllBlocks.DEEPSLATE_THORIUM_ORE.get().defaultBlockState())
        );
        context.register(
                ModWorldGenKeys.DEEPSLATE_THORIUM_ORE_CONFIGURED,
                new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(thoriumTargets, 6, 0.75f))
        );

        // Platinum Ore
        List<OreConfiguration.TargetBlockState> platinumTargets = List.of(
                OreConfiguration.target(deepslateReplaceable, AllBlocks.PLATINUM_ORE.get().defaultBlockState())
        );
        context.register(
                ModWorldGenKeys.PLATINUM_ORE_CONFIGURED,
                new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(platinumTargets, 4, 0.75f))
        );
    }

    public static void bootstrapPlacedFeatures(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        // Bedrock Steam Vent Placed
        context.register(
                ModWorldGenKeys.BEDROCK_STEAM_VENT_PLACED,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(ModWorldGenKeys.BEDROCK_STEAM_VENT_CONFIGURED),
                        List.of(
                                RarityFilter.onAverageOnceEvery(128),
                                InSquarePlacement.spread(),
                                BiomeFilter.biome()
                        )
                )
        );

        // Thorium Ore Placed
        context.register(
                ModWorldGenKeys.DEEPSLATE_THORIUM_ORE_PLACED,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(ModWorldGenKeys.DEEPSLATE_THORIUM_ORE_CONFIGURED),
                        List.of(
                                CountPlacement.of(6),
                                InSquarePlacement.spread(),
                                HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(0)),
                                BiomeFilter.biome()
                        )
                )
        );

        // Platinum Ore Placed
        context.register(
                ModWorldGenKeys.PLATINUM_ORE_PLACED,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(ModWorldGenKeys.PLATINUM_ORE_CONFIGURED),
                        List.of(
                                CountPlacement.of(4),
                                InSquarePlacement.spread(),
                                HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-16)),
                                BiomeFilter.biome()
                        )
                )
        );
    }

    public static void bootstrapBiomeModifiers(BootstrapContext<BiomeModifier> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        HolderSet<Biome> overworldBiomes = biomes.getOrThrow(BiomeTags.IS_OVERWORLD);

        context.register(
                ModWorldGenKeys.ADD_BEDROCK_STEAM_VENT,
                new BiomeModifiers.AddFeaturesBiomeModifier(
                        overworldBiomes,
                        HolderSet.direct(placedFeatures.getOrThrow(ModWorldGenKeys.BEDROCK_STEAM_VENT_PLACED)),
                        GenerationStep.Decoration.UNDERGROUND_ORES
                )
        );

        context.register(
                ModWorldGenKeys.ADD_DEEPSLATE_THORIUM_ORE,
                new BiomeModifiers.AddFeaturesBiomeModifier(
                        overworldBiomes,
                        HolderSet.direct(placedFeatures.getOrThrow(ModWorldGenKeys.DEEPSLATE_THORIUM_ORE_PLACED)),
                        GenerationStep.Decoration.UNDERGROUND_ORES
                )
        );

        context.register(
                ModWorldGenKeys.ADD_PLATINUM_ORE,
                new BiomeModifiers.AddFeaturesBiomeModifier(
                        overworldBiomes,
                        HolderSet.direct(placedFeatures.getOrThrow(ModWorldGenKeys.PLATINUM_ORE_PLACED)),
                        GenerationStep.Decoration.UNDERGROUND_ORES
                )
        );
    }
}