package com.noahbelstad.create_enriched.block;

import com.noahbelstad.create_enriched.CreateEnriched;
import com.simibubi.create.foundation.data.AssetLookup;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class CreateEnrichedBlocks {

    // --- BEDROCK DEPOSIT BLOCK ---
    public static final BlockEntry<Block> BEDROCK_DEPOSIT_BLOCK = CreateEnriched.REGISTRATE
            .block("bedrock_deposit", Block::new)
            .initialProperties(() -> Blocks.BEDROCK)
            .properties(p -> p.destroyTime(-1.0f).explosionResistance(3600000.0f).noLootTable())
            .blockstate((c, p) -> p.simpleBlock(c.get()))
            .simpleItem()
            .register();

    // --- BOILER ---
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

    // --- SMALL STEAM GENERATOR ---
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

    public static void init() {
    }
}