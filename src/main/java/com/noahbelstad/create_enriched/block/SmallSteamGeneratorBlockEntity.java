package com.noahbelstad.create_enriched.block;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import net.minecraft.ChatFormatting;
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
    private static final int TARGET_STEAM_CONSUMPTION = 126;
    private static final int BUFFER_START_THRESHOLD = 2000;
    private static final int TANK_CAPACITY = 2500; // Optimized buffer size for equal pipe sharing

    private boolean active = false;
    private float stressMultiplier = 0.0f;
    private int fluidDrainedLastTick = 0;

    private final FluidTank internalTank = new FluidTank(TANK_CAPACITY) {
        @Override
        public boolean isFluidValid(FluidStack stack) {
            return true;
        }

        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            setChanged();
        }
    };

    public SmallSteamGeneratorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level != null && !level.isClientSide) {
            updateGeneratedRotation();
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (level == null || level.isClientSide) {
            return;
        }

        int availableFluid = internalTank.getFluidAmount();

        // Startup Hysteresis: Requires 2000 mB to turn on, runs until completely dry (0 mB)
        if (!active && availableFluid >= BUFFER_START_THRESHOLD) {
            active = true;
            updateGeneratedRotation();
            notifyUpdate();
        } else if (active && availableFluid <= 0) {
            active = false;
            stressMultiplier = 0.0f;
            fluidDrainedLastTick = 0;
            updateGeneratedRotation();
            notifyUpdate();
            return;
        }

        if (active) {
            int amountToDrain = Math.min(availableFluid, TARGET_STEAM_CONSUMPTION);
            float newMultiplier = 0.0f;

            if (amountToDrain > 0) {
                internalTank.drain(amountToDrain, IFluidHandler.FluidAction.EXECUTE);
                fluidDrainedLastTick = amountToDrain;
                newMultiplier = (float) amountToDrain / (float) TARGET_STEAM_CONSUMPTION;
            } else {
                fluidDrainedLastTick = 0;
            }

            // Sync rotation network & client goggles if burn rate shifts significantly
            if (Math.abs(newMultiplier - stressMultiplier) > 0.01f) {
                stressMultiplier = newMultiplier;
                updateGeneratedRotation();
                notifyUpdate();
            }
        }
    }

    @Override
    public float getGeneratedSpeed() {
        if (!active) {
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
    public float calculateAddedStressCapacity() {
        float baseCapacityPerRpm = super.calculateAddedStressCapacity();
        float rawTotalSU = baseCapacityPerRpm * GENERATED_RPM * stressMultiplier;

        // Guarantees clean integer SU on Create network tooltips
        float roundedTotalSU = Math.round(rawTotalSU);
        return roundedTotalSU / GENERATED_RPM;
    }

    @Override
    protected Block getStressConfigKey() {
        return CreateEnrichedBlocks.SMALL_STEAM_GENERATOR_BLOCK.get();
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);

        tooltip.add(Component.literal("    ").append(Component.literal("Burn Rate Stats:").withStyle(ChatFormatting.GRAY)));

        tooltip.add(Component.literal("     ")
                .append(Component.literal("Burn Rate: ").withStyle(ChatFormatting.GRAY))
                .append(Component.literal((active ? fluidDrainedLastTick : 0) + " mB/t").withStyle(ChatFormatting.GOLD)));

        tooltip.add(Component.literal("     ")
                .append(Component.literal("Max Burn Rate: ").withStyle(ChatFormatting.GRAY))
                .append(Component.literal(TARGET_STEAM_CONSUMPTION + " mB/t").withStyle(ChatFormatting.GOLD)));

        tooltip.add(Component.literal("     ")
                .append(Component.literal("Buffer Tank: ").withStyle(ChatFormatting.GRAY))
                .append(Component.literal(internalTank.getFluidAmount() + " / " + TANK_CAPACITY + " mB").withStyle(ChatFormatting.AQUA)));

        return true;
    }

    public IFluidHandler getFluidHandlerForSide(Direction side) {
        BlockState state = getBlockState();
        if (!(state.getBlock() instanceof SmallSteamGeneratorBlock)) {
            return null;
        }

        Direction facing = state.getValue(SmallSteamGeneratorBlock.FACING);

        if (side == facing.getOpposite()) {
            return internalTank;
        }

        return null;
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        tag.putBoolean("Active", active);
        tag.putFloat("StressMultiplier", stressMultiplier);
        tag.putInt("FluidDrainedLastTick", fluidDrainedLastTick);
        internalTank.writeToNBT(registries, tag);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        active = tag.getBoolean("Active");
        stressMultiplier = tag.getFloat("StressMultiplier");
        fluidDrainedLastTick = tag.getInt("FluidDrainedLastTick");
        internalTank.readFromNBT(registries, tag);
    }
}