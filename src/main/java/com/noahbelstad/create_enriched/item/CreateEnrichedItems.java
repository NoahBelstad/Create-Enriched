package com.noahbelstad.create_enriched.item;

import com.noahbelstad.create_enriched.CreateEnriched;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CreateEnrichedItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CreateEnriched.MODID);

    public static final DeferredItem<Item> BOILER_CONVERTER = ITEMS.register("boiler_converter",
            () -> new BoilerConverterItem(new Item.Properties()));

    public static void init(IEventBus bus) {
        ITEMS.register(bus);
    }
}