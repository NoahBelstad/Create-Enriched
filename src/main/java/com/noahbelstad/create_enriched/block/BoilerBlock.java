package com.noahbelstad.create_enriched.block;

import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BoilerBlock extends FluidTankBlock {
    public BoilerBlock(Properties properties) {
        super(properties, false);
    }

    @Override
    public BlockEntityType<? extends BoilerBlockEntity> getBlockEntityType() {
        return CreateEnrichedBlocks.BOILER_BE.get();
    }
}