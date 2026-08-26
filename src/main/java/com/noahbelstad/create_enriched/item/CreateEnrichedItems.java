package com.noahbelstad.create_enriched.item;

import com.noahbelstad.create_enriched.CreateEnriched;
import com.tterrag.registrate.util.entry.ItemEntry;

public class CreateEnrichedItems {

    public static final ItemEntry<BoilerConverterItem> BOILER_CONVERTER = CreateEnriched.REGISTRATE
            .item("boiler_converter", BoilerConverterItem::new)
            .register();

    public static void init() {
    }
}