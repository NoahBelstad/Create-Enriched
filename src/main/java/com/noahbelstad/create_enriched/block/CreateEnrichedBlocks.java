package com.noahbelstad.create_enriched.block;

import com.noahbelstad.create_enriched.CreateEnriched;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CreateEnrichedBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CreateEnriched.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CreateEnriched.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CreateEnriched.MODID);

    // --- BOILER ---
    public static final DeferredBlock<Block> BOILER_BLOCK = BLOCKS.register("boiler",
            () -> new BoilerBlock(BlockBehaviour.Properties.of().destroyTime(2.0f)));

    public static final DeferredItem<BlockItem> BOILER_ITEM = ITEMS.registerSimpleBlockItem("boiler", BOILER_BLOCK);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BoilerBlockEntity>> BOILER_BE =
            BLOCK_ENTITIES.register("boiler",
                    () -> BlockEntityType.Builder.of(BoilerBlockEntity::new, BOILER_BLOCK.get()).build(null));

    // --- SMALL STEAM GENERATOR ---
    public static final DeferredBlock<Block> SMALL_STEAM_GENERATOR_BLOCK = BLOCKS.register("small_steam_generator",
            () -> new SmallSteamGeneratorBlock(BlockBehaviour.Properties.of().destroyTime(3.0f).noOcclusion()));

    public static final DeferredItem<BlockItem> SMALL_STEAM_GENERATOR_ITEM = ITEMS.registerSimpleBlockItem("small_steam_generator", SMALL_STEAM_GENERATOR_BLOCK);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SmallSteamGeneratorBlockEntity>> SMALL_STEAM_GENERATOR_BE =
            BLOCK_ENTITIES.register("small_steam_generator",
                    () -> BlockEntityType.Builder.of(SmallSteamGeneratorBlockEntity::new, SMALL_STEAM_GENERATOR_BLOCK.get()).build(null));

    public static void init(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITIES.register(bus);
    }
}