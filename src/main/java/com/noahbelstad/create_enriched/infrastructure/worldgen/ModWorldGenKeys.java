package com.noahbelstad.create_enriched.infrastructure.worldgen;

import com.noahbelstad.create_enriched.CreateEnriched;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModWorldGenKeys {

    // Configured Features
    public static final ResourceKey<ConfiguredFeature<?, ?>> BEDROCK_STEAM_VENT_CONFIGURED =
            ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(CreateEnriched.MODID, "bedrock_steam_vent"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> DEEPSLATE_THORIUM_ORE_CONFIGURED =
            ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(CreateEnriched.MODID, "deepslate_thorium_ore"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> PLATINUM_ORE_CONFIGURED =
            ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(CreateEnriched.MODID, "platinum_ore"));

    // Placed Features
    public static final ResourceKey<PlacedFeature> BEDROCK_STEAM_VENT_PLACED =
            ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(CreateEnriched.MODID, "bedrock_steam_vent"));
    public static final ResourceKey<PlacedFeature> DEEPSLATE_THORIUM_ORE_PLACED =
            ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(CreateEnriched.MODID, "deepslate_thorium_ore"));
    public static final ResourceKey<PlacedFeature> PLATINUM_ORE_PLACED =
            ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(CreateEnriched.MODID, "platinum_ore"));

    // Biome Modifiers
    public static final ResourceKey<BiomeModifier> ADD_BEDROCK_STEAM_VENT =
            ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(CreateEnriched.MODID, "add_bedrock_steam_vent"));
    public static final ResourceKey<BiomeModifier> ADD_DEEPSLATE_THORIUM_ORE =
            ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(CreateEnriched.MODID, "add_deepslate_thorium_ore"));
    public static final ResourceKey<BiomeModifier> ADD_PLATINUM_ORE =
            ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(CreateEnriched.MODID, "add_platinum_ore"));
}