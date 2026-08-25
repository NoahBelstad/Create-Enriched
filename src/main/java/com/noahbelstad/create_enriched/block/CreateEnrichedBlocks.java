package com.noahbelstad.create_enriched.block;

import com.noahbelstad.create_enriched.CreateEnriched;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CreateEnrichedBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CreateEnriched.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CreateEnriched.MODID);

    public static final DeferredBlock<Block> BOILER_BLOCK = BLOCKS.register("boiler",
            () -> new BoilerBlock(BlockBehaviour.Properties.of().destroyTime(2.0f)));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BoilerBlockEntity>> BOILER_BE =
            BLOCK_ENTITIES.register("boiler",
                    () -> BlockEntityType.Builder.of(BoilerBlockEntity::new, BOILER_BLOCK.get()).build(null));

    public static void init(IEventBus bus) {
        BLOCKS.register(bus);
        BLOCK_ENTITIES.register(bus);
    }
}