package com.noahbelstad.create_enriched;

import com.mojang.logging.LogUtils;
import com.noahbelstad.create_enriched.block.CreateEnrichedBlocks;
import com.noahbelstad.create_enriched.fluid.CreateEnrichedFluids;
import com.noahbelstad.create_enriched.item.CreateEnrichedItems;
import com.noahbelstad.create_enriched.worldgen.CreateEnrichedFeatures;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(CreateEnriched.MODID)
public class CreateEnriched {
    public static final String MODID = "create_enriched";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    public CreateEnriched(IEventBus modEventBus, ModContainer modContainer) {
        REGISTRATE.registerEventListeners(modEventBus);

        CreateEnrichedFluids.init();
        CreateEnrichedItems.init();
        CreateEnrichedBlocks.init();
        CreateEnrichedFeatures.register(modEventBus);

        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            BlockStressValues.CAPACITIES.register(
                    CreateEnrichedBlocks.SMALL_STEAM_GENERATOR_BLOCK.get(),
                    () -> 1728
            );
        });
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

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                net.neoforged.neoforge.capabilities.Capabilities.FluidHandler.BLOCK,
                CreateEnrichedBlocks.BOILER_BE.get(),
                (be, side) -> be.getCustomFluidHandler()
        );

        event.registerBlockEntity(
                net.neoforged.neoforge.capabilities.Capabilities.FluidHandler.BLOCK,
                CreateEnrichedBlocks.SMALL_STEAM_GENERATOR_BE.get(),
                (be, side) -> be.getFluidHandlerForSide(side)
        );
    }
}