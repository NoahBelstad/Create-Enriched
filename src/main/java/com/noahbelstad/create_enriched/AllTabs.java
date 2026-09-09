package com.noahbelstad.create_enriched;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class AllTabs {

    public static final RegistryEntry<CreativeModeTab, CreativeModeTab> MAIN_TAB = CreateEnriched.REGISTRATE
            .defaultCreativeTab("main_tab", builder -> builder
                    .icon(() -> {
                        Item tankItem = BuiltInRegistries.ITEM.get(
                                ResourceLocation.fromNamespaceAndPath("create", "fluid_tank")
                        );
                        return new ItemStack(tankItem);
                    })
                    .title(Component.translatable("itemGroup.create_enriched.main_tab"))
            )
            .register();

    public static void init() {
        // Classloading trigger
    }
}