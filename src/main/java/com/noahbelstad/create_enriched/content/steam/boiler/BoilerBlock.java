package com.noahbelstad.create_enriched.content.steam.boiler;

import com.noahbelstad.create_enriched.AllBlocks;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BoilerBlock extends FluidTankBlock {
    public BoilerBlock(Properties properties) {
        super(properties, false);
    }

    @Override
    public BlockEntityType<? extends BoilerBlockEntity> getBlockEntityType() {
        return AllBlocks.BOILER_BE.get();
    }
}