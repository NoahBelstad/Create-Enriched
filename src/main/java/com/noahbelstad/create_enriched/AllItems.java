package com.noahbelstad.create_enriched;

import com.noahbelstad.create_enriched.content.steam.boiler.BoilerConverterItem;
import com.noahbelstad.create_enriched.content.steam.steam_vent.BedrockDetectorItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

public class AllItems {

    public static final ItemEntry<BoilerConverterItem> BOILER_CONVERTER = CreateEnriched.REGISTRATE
            .item("boiler_converter", BoilerConverterItem::new)
            .register();

    public static final ItemEntry<BedrockDetectorItem> BEDROCK_DETECTOR = CreateEnriched.REGISTRATE
            .item("bedrock_detector", BedrockDetectorItem::new)
            .properties(p -> p.stacksTo(1))
            .register();
    public static final ItemEntry<Item> RAW_SULFUR = CreateEnriched.REGISTRATE
            .item("raw_sulfur", Item::new)
            .register();

    public static final ItemEntry<Item> SULFUR_DUST = CreateEnriched.REGISTRATE
            .item("sulfur_dust", Item::new)
            .register();

    public static final ItemEntry<Item> LIMESTONE_DUST = CreateEnriched.REGISTRATE
            .item("limestone_dust", Item::new)
            .register();

    // --- PLATINUM PROCESSING CHAIN ITEMS ---
    public static final ItemEntry<Item> RAW_PLATINUM = CreateEnriched.REGISTRATE
            .item("raw_platinum", Item::new)
            .register();

    public static final ItemEntry<Item> CRUSHED_PLATINUM = CreateEnriched.REGISTRATE
            .item("crushed_platinum", Item::new)
            .register();

    public static final ItemEntry<Item> PLATINUM_INGOT = CreateEnriched.REGISTRATE
            .item("platinum_ingot", Item::new)
            .register();

    public static final ItemEntry<Item> PLATINUM_NUGGET = CreateEnriched.REGISTRATE
            .item("platinum_nugget", Item::new)
            .register();

    public static final ItemEntry<Item> RAW_THORIUM_ORE = CreateEnriched.REGISTRATE
            .item("raw_thorium_ore", Item::new)
            .register();

    public static final ItemEntry<Item> CRUSHED_THORIUM_ORE = CreateEnriched.REGISTRATE
            .item("crushed_thorium_ore", Item::new)
            .register();

    public static final ItemEntry<Item> RAW_THORIUM_DUST = CreateEnriched.REGISTRATE
            .item("raw_thorium_dust", Item::new)
            .register();

    public static final ItemEntry<Item> LOW_PURITY_ENRICHED_THORIUM_DUST = CreateEnriched.REGISTRATE
            .item("low_purity_enriched_thorium_dust", Item::new)
            .register();

    public static final ItemEntry<Item> DIRTY_THORIUM_DUST = CreateEnriched.REGISTRATE
            .item("dirty_thorium_dust", Item::new)
            .register();

    public static final ItemEntry<Item> LOW_PURITY_ENRICHED_THORIUM_PELLET = CreateEnriched.REGISTRATE
            .item("low_purity_enriched_thorium_pellet", Item::new)
            .register();

    public static final ItemEntry<Item> GRAPHITE_ROD = CreateEnriched.REGISTRATE
            .item("graphite_rod", Item::new)
            .register();

    public static final ItemEntry<Item> GRAPHITE_DUST = CreateEnriched.REGISTRATE
            .item("graphite_dust", Item::new)
            .register();

    public static final ItemEntry<Item> COAL_DUST = CreateEnriched.REGISTRATE
            .item("coal_dust", Item::new)
            .register();

    public static final ItemEntry<Item> GRAPHITE_NUGGET = CreateEnriched.REGISTRATE
            .item("graphite_nugget", Item::new)
            .register();

    public static final ItemEntry<Item> GRAPHITE_INGOT = CreateEnriched.REGISTRATE
            .item("graphite_ingot", Item::new)
            .register();

    public static void init() {
    }
}