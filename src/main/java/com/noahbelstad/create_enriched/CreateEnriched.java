package com.noahbelstad.create_enriched;

import com.noahbelstad.create_enriched.block.BoilerBlockEntity;
import com.noahbelstad.create_enriched.block.CreateEnrichedBlocks;
import com.noahbelstad.create_enriched.block.SmallSteamGeneratorBlockEntity;
import com.noahbelstad.create_enriched.fluid.CreateEnrichedFluids;
import com.noahbelstad.create_enriched.item.CreateEnrichedItems;

import com.mojang.logging.LogUtils;
import com.simibubi.create.api.stress.BlockStressValues;
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

        modEventBus.addListener(this::commonSetup);

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
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                CreateEnrichedBlocks.SMALL_STEAM_GENERATOR_BE.get(),
                (be, side) -> {
                    if (be instanceof SmallSteamGeneratorBlockEntity generator) {
                        return generator.getFluidHandlerForSide(side);
                    }
                    return null;
                }
        );
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            BlockStressValues.CAPACITIES.register(
                    CreateEnrichedBlocks.SMALL_STEAM_GENERATOR_BLOCK.get(),
                    () -> 2304.0D
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
}