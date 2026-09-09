package com.noahbelstad.create_enriched.content.reactors.thorium;

import com.noahbelstad.create_enriched.AllBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ThoriumReactorBoilerBlock extends Block implements EntityBlock {

    public ThoriumReactorBoilerBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ThoriumReactorBoilerBlockEntity(AllBlocks.THORIUM_REACTOR_BOILER_BE.get(), pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide() ? null : (lvl, pos, st, be) -> {
            if (be instanceof ThoriumReactorBoilerBlockEntity boiler) {
                boiler.tick(lvl, pos, st);
            }
        };
    }
}