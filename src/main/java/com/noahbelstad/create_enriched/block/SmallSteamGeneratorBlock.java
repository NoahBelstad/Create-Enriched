package com.noahbelstad.create_enriched.block;

import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SmallSteamGeneratorBlock extends DirectionalKineticBlock implements IBE<SmallSteamGeneratorBlockEntity> {

    public SmallSteamGeneratorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getClickedFace());
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING);
    }

    @Override
    public Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Override
    public Class<SmallSteamGeneratorBlockEntity> getBlockEntityClass() {
        return SmallSteamGeneratorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends SmallSteamGeneratorBlockEntity> getBlockEntityType() {
        return CreateEnrichedBlocks.SMALL_STEAM_GENERATOR_BE.get();
    }
}