package com.noahbelstad.create_enriched.item;

import com.noahbelstad.create_enriched.CreateEnriched;
import com.tterrag.registrate.util.entry.ItemEntry;

public class CreateEnrichedItems {

    public static final ItemEntry<BoilerConverterItem> BOILER_CONVERTER = CreateEnriched.REGISTRATE
            .item("boiler_converter", BoilerConverterItem::new)
            .register();

    public static final ItemEntry<BedrockDetectorItem> BEDROCK_DETECTOR = CreateEnriched.REGISTRATE
            .item("bedrock_detector", BedrockDetectorItem::new)
            .properties(p -> p.stacksTo(1))
            .register();

    public static void init() {
    }
}