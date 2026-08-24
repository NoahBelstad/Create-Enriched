package com.noahbelstad.create_enriched.client;

import com.noahbelstad.create_enriched.fluid.CreateEnrichedFluids;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = "create_enriched", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientModEventBusSubscriber {

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override
            public @NotNull ResourceLocation getStillTexture() {
                return ResourceLocation.withDefaultNamespace("block/water_still");
            }

            @Override
            public @NotNull ResourceLocation getFlowingTexture() {
                return ResourceLocation.withDefaultNamespace("block/water_flow");
            }

            @Override
            public int getTintColor() {
                // ARGB format: AARRGGBB
                // 0x80 = Alpha (about 50% transparency)
                // 0x808080 = Gray color
                return 0x80808080;
            }
        }, CreateEnrichedFluids.STEAM_LIQUID_TYPE.get());
    }
}