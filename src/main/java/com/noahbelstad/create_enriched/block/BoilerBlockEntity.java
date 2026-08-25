package com.noahbelstad.create_enriched.block;

import com.noahbelstad.create_enriched.fluid.CreateEnrichedFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class BoilerBlockEntity extends BlockEntity {
    private final FluidTank tank = new FluidTank(10000);

    public BoilerBlockEntity(BlockPos pos, BlockState state) {
        super(CreateEnrichedBlocks.BOILER_BE.get(), pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (tank.getFluidAmount() < tank.getCapacity()) {
            tank.fill(new FluidStack(CreateEnrichedFluids.STEAM_LIQUID_STILL.get(), 10), IFluidHandler.FluidAction.EXECUTE);
            setChanged();
        }
    }
}