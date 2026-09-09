package com.noahbelstad.create_enriched;

import com.noahbelstad.create_enriched.content.reactors.thorium.ThoriumReactorBoilerBlock;
import com.noahbelstad.create_enriched.content.reactors.thorium.ThoriumReactorBoilerBlockEntity;
import com.noahbelstad.create_enriched.content.reactors.thorium.ThoriumReactorCoreBlock;
import com.noahbelstad.create_enriched.content.reactors.thorium.ThoriumReactorCoreBlockEntity;
import com.noahbelstad.create_enriched.content.steam.boiler.BoilerBlock;
import com.noahbelstad.create_enriched.content.steam.boiler.BoilerBlockEntity;
import com.noahbelstad.create_enriched.content.steam.small_steam_generator.SmallSteamGeneratorBlock;
import com.noahbelstad.create_enriched.content.steam.small_steam_generator.SmallSteamGeneratorBlockEntity;
import com.noahbelstad.create_enriched.content.steam.steam_vent.BedrockSteamVentBlock;
import com.noahbelstad.create_enriched.content.steam.steam_vent.BedrockSteamVentBlockEntity;
import com.simibubi.create.foundation.data.AssetLookup;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;

public class AllBlocks {

    public static final BlockEntry<Block> DEEPSLATE_THORIUM_ORE = CreateEnriched.REGISTRATE
            .block("deepslate_thorium_ore", Block::new)
            .initialProperties(() -> Blocks.DEEPSLATE)
            .properties(p -> p.destroyTime(4.5f).explosionResistance(3.0f))
            .simpleItem()
            .register();

    public static final BlockEntry<BedrockSteamVentBlock> BEDROCK_STEAM_VENT_BLOCK = CreateEnriched.REGISTRATE
            .block("bedrock_steam_vent", BedrockSteamVentBlock::new)
            .initialProperties(() -> Blocks.BEDROCK)
            .properties(p -> p.destroyTime(-1.0f).explosionResistance(3600000.0f).noLootTable())
            .blockstate((c, p) -> p.simpleBlock(c.get()))
            .simpleItem()
            .register();

    public static final BlockEntityEntry<BedrockSteamVentBlockEntity> BEDROCK_STEAM_VENT_BE = CreateEnriched.REGISTRATE
            .<BedrockSteamVentBlockEntity>blockEntity("bedrock_steam_vent", BedrockSteamVentBlockEntity::new)
            .validBlocks(BEDROCK_STEAM_VENT_BLOCK)
            .register();

    public static final BlockEntry<BoilerBlock> BOILER_BLOCK = CreateEnriched.REGISTRATE
            .block("boiler", BoilerBlock::new)
            .properties(p -> p.destroyTime(2.0f))
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
            .simpleItem()
            .register();

    public static final BlockEntityEntry<BoilerBlockEntity> BOILER_BE = CreateEnriched.REGISTRATE
            .blockEntity("boiler", BoilerBlockEntity::new)
            .validBlocks(BOILER_BLOCK)
            .register();

    public static final BlockEntry<SmallSteamGeneratorBlock> SMALL_STEAM_GENERATOR_BLOCK = CreateEnriched.REGISTRATE
            .block("small_steam_generator", SmallSteamGeneratorBlock::new)
            .properties(p -> p.destroyTime(3.0f).noOcclusion())
            .blockstate((c, p) -> p.directionalBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
            .simpleItem()
            .register();

    public static final BlockEntityEntry<SmallSteamGeneratorBlockEntity> SMALL_STEAM_GENERATOR_BE = CreateEnriched.REGISTRATE
            .blockEntity("small_steam_generator", SmallSteamGeneratorBlockEntity::new)
            .validBlocks(SMALL_STEAM_GENERATOR_BLOCK)
            .register();

    // --- PLATINUM ORE ---
    public static final BlockEntry<Block> PLATINUM_ORE = CreateEnriched.REGISTRATE
            .block("platinum_ore", Block::new)
            .initialProperties(() -> Blocks.STONE)
            .properties(p -> p.destroyTime(3.0f).explosionResistance(3.0f))
            .simpleItem()
            .register();

    // --- THORIUM REACTOR CORE ---
    public static final BlockEntry<ThoriumReactorCoreBlock> THORIUM_REACTOR_CORE_BLOCK = CreateEnriched.REGISTRATE
            .block("thorium_reactor_core", ThoriumReactorCoreBlock::new)
            .properties(p -> p.destroyTime(3.0f).explosionResistance(6.0f))
            .blockstate((c, p) -> p.getVariantBuilder(c.get()).forAllStates(state -> {
                boolean active = state.getValue(ThoriumReactorCoreBlock.ACTIVE);
                String suffix = active ? "_powered" : "";
                return ConfiguredModel.builder()
                        .modelFile(p.models().getExistingFile(p.modLoc("block/thorium_reactor_core" + suffix)))
                        .build();
            }))
            .simpleItem()
            .register();

    public static final BlockEntityEntry<ThoriumReactorCoreBlockEntity> THORIUM_REACTOR_BE = CreateEnriched.REGISTRATE
            .blockEntity("thorium_reactor_core", ThoriumReactorCoreBlockEntity::new)
            .validBlocks(THORIUM_REACTOR_CORE_BLOCK)
            .register();

    // --- THORIUM REACTOR BOILER ---
    public static final BlockEntry<ThoriumReactorBoilerBlock> THORIUM_REACTOR_BOILER_BLOCK = CreateEnriched.REGISTRATE
            .block("thorium_reactor_boiler", ThoriumReactorBoilerBlock::new)
            .properties(p -> p.destroyTime(3.0f).explosionResistance(6.0f))
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
            .simpleItem()
            .register();

    public static final BlockEntityEntry<ThoriumReactorBoilerBlockEntity> THORIUM_REACTOR_BOILER_BE = CreateEnriched.REGISTRATE
            .blockEntity("thorium_reactor_boiler", ThoriumReactorBoilerBlockEntity::new)
            .validBlocks(THORIUM_REACTOR_BOILER_BLOCK)
            .register();

    public static void init() {
    }
}