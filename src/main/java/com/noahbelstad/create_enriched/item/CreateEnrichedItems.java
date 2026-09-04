package com.noahbelstad.create_enriched.item;

import com.noahbelstad.create_enriched.CreateEnriched;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

public class CreateEnrichedItems {

    public static final ItemEntry<BoilerConverterItem> BOILER_CONVERTER = CreateEnriched.REGISTRATE
            .item("boiler_converter", BoilerConverterItem::new)
            .register();

    public static final ItemEntry<BedrockDetectorItem> BEDROCK_DETECTOR = CreateEnriched.REGISTRATE
            .item("bedrock_detector", BedrockDetectorItem::new)
            .properties(p -> p.stacksTo(1))
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

    public static void init() {
    }
}