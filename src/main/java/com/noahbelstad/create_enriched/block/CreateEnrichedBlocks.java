package com.noahbelstad.create_enriched.block;

import com.noahbelstad.create_enriched.CreateEnriched;
import com.simibubi.create.foundation.data.AssetLookup;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class CreateEnrichedBlocks {

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

    public static void init() {
    }
}