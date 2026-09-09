package com.noahbelstad.create_enriched.content.steam.small_steam_generator;

import com.noahbelstad.create_enriched.AllBlocks;
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
    private static final int BATCH_INTERVAL = 5;
    private static final int TARGET_STEAM_PER_TICK = 45; // Up to 45 mB/t max burn rate
    private static final int TARGET_STEAM_PER_BATCH = TARGET_STEAM_PER_TICK * BATCH_INTERVAL; // 450 mB per 5 ticks
    private static final float POWER_MULTIPLIER = 3.33333f; // Maintains exact same SU output per mB of steam
    private static final int TANK_CAPACITY = 10000;

    private boolean active = false;
    private float stressMultiplier = 0.0f;
    private int fluidDrainedLastTick = 0;
    private int batchTimer = 0;

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

        batchTimer++;

        if (batchTimer >= BATCH_INTERVAL) {
            batchTimer = 0;
            processBatch();
        }
    }

    private void processBatch() {
        int availableFluid = internalTank.getFluidAmount();

        if (availableFluid > 0) {
            int amountToDrain = Math.min(availableFluid, TARGET_STEAM_PER_BATCH);
            internalTank.drain(amountToDrain, IFluidHandler.FluidAction.EXECUTE);

            fluidDrainedLastTick = amountToDrain / BATCH_INTERVAL;
            float newMultiplier = (float) amountToDrain / (float) TARGET_STEAM_PER_BATCH;

            if (!active) {
                active = true;
            }

            if (Math.abs(newMultiplier - stressMultiplier) > 0.001f) {
                stressMultiplier = newMultiplier;
                updateGeneratedRotation();
                notifyUpdate();
            }
        } else {
            fluidDrainedLastTick = 0;
            if (active || stressMultiplier != 0.0f) {
                active = false;
                stressMultiplier = 0.0f;
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
        float rawTotalSU = baseCapacityPerRpm * GENERATED_RPM * stressMultiplier * POWER_MULTIPLIER;
        float roundedTotalSU = Math.round(rawTotalSU);
        return roundedTotalSU / GENERATED_RPM;
    }

    @Override
    protected Block getStressConfigKey() {
        return AllBlocks.SMALL_STEAM_GENERATOR_BLOCK.get();
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
                .append(Component.literal(TARGET_STEAM_PER_TICK + " mB/t").withStyle(ChatFormatting.GOLD)));

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
        tag.putInt("BatchTimer", batchTimer);
        internalTank.writeToNBT(registries, tag);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        active = tag.getBoolean("Active");
        stressMultiplier = tag.getFloat("StressMultiplier");
        fluidDrainedLastTick = tag.getInt("FluidDrainedLastTick");
        batchTimer = tag.getInt("BatchTimer");
        internalTank.readFromNBT(registries, tag);
    }
}