package com.noahbelstad.create_enriched;

import com.noahbelstad.create_enriched.block.BoilerBlockEntity;
import com.noahbelstad.create_enriched.block.CreateEnrichedBlocks;
import com.noahbelstad.create_enriched.fluid.CreateEnrichedFluids;
import com.noahbelstad.create_enriched.item.CreateEnrichedItems;

import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(CreateEnriched.MODID)
public class CreateEnriched {
    public static final String MODID = "create_enriched";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CreateEnriched(IEventBus modEventBus, ModContainer modContainer) {
        CreateEnrichedFluids.init(modEventBus);
        CreateEnrichedItems.init(modEventBus);
        CreateEnrichedBlocks.init(modEventBus);

        // Registered to the MOD bus so NeoForge hooks up fluid capabilities to all boiler blocks
        modEventBus.addListener(this::registerCapabilities);

        NeoForge.EVENT_BUS.register(this);
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                CreateEnrichedBlocks.BOILER_BE.get(),
                (be, side) -> {
                    if (be instanceof BoilerBlockEntity boiler) {
                        return boiler.getCustomFluidHandler();
                    }
                    return null;
                }
        );
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Create enriched common");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Create enriched server");
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("Create enriched client");
        }
    }
}