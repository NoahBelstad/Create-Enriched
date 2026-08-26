package com.noahbelstad.create_enriched.block;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.List;

public class SmallSteamGeneratorBlockEntity extends GeneratingKineticBlockEntity implements IHaveGoggleInformation {

    private static final float GENERATED_RPM = 128.0f;

    private final FluidTank internalTank = new FluidTank(1000) {
        @Override
        public boolean isFluidValid(FluidStack stack) {
            return true;
        }

        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            setChanged();
            if (!level.isClientSide) {
                updateGeneratedRotation();
            }
        }
    };

    public SmallSteamGeneratorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public SmallSteamGeneratorBlockEntity(BlockPos pos, BlockState state) {
        this(CreateEnrichedBlocks.SMALL_STEAM_GENERATOR_BE.get(), pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!level.isClientSide) {
            updateGeneratedRotation();
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (level == null || level.isClientSide) {
            return;
        }

        if (internalTank.getFluidAmount() > 0) {
            internalTank.drain(1, IFluidHandler.FluidAction.EXECUTE);

            if (internalTank.getFluidAmount() == 0) {
                updateGeneratedRotation();
            }
        }
    }

    @Override
    public float getGeneratedSpeed() {
        if (internalTank.getFluidAmount() <= 0) {
            return 0.0f;
        }

        BlockState state = getBlockState();
        if (!(state.getBlock() instanceof SmallSteamGeneratorBlock)) {
            return 0;
        }
        Direction facing = state.getValue(SmallSteamGeneratorBlock.FACING);
        return convertToDirection(GENERATED_RPM, facing);
    }

    @Override
    protected Block getStressConfigKey() {
        return CreateEnrichedBlocks.SMALL_STEAM_GENERATOR_BLOCK.get();
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean added = super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        containedFluidTooltip(tooltip, isPlayerSneaking, internalTank);
        return true;
    }

    public IFluidHandler getFluidHandlerForSide(Direction side) {
        BlockState state = getBlockState();
        if (!(state.getBlock() instanceof SmallSteamGeneratorBlock)) {
            return null;
        }

        Direction facing = state.getValue(SmallSteamGeneratorBlock.FACING);

        if (side == facing.getOpposite()) {
            return new IFluidHandler() {
                @Override
                public int getTanks() {
                    return internalTank.getTanks();
                }

                @Override
                public FluidStack getFluidInTank(int tank) {
                    return internalTank.getFluidInTank(tank);
                }

                @Override
                public int getTankCapacity(int tank) {
                    return internalTank.getTankCapacity(tank);
                }

                @Override
                public boolean isFluidValid(int tank, FluidStack stack) {
                    return internalTank.isFluidValid(tank, stack);
                }

                @Override
                public int fill(FluidStack resource, FluidAction action) {
                    return internalTank.fill(resource, action);
                }

                @Override
                public FluidStack drain(FluidStack resource, FluidAction action) {
                    return FluidStack.EMPTY;
                }

                @Override
                public FluidStack drain(int maxDrain, FluidAction action) {
                    return FluidStack.EMPTY;
                }
            };
        }

        return null;
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        internalTank.writeToNBT(registries, tag);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        internalTank.readFromNBT(registries, tag);
    }
}