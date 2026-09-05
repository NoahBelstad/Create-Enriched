package com.noahbelstad.create_enriched.block;

import com.noahbelstad.create_enriched.fluid.CreateEnrichedFluids;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.List;

public class BoilerBlockEntity extends FluidTankBlockEntity {
    public static final int BASE_CONVERSION_RATE = 14;
    public static final int BLOCKS_PER_HEAT_LEVEL = 4;
    public static final int MAX_HEAT_LEVEL = 18;

    protected FluidTank waterBuffer = new FluidTank(8000);
    private final BoilerFluidHandler customFluidHandler = new BoilerFluidHandler(this);

    private int tickCounter = 0;
    private int heatLevel = 0;

    // Rate Tracking Fields
    private int actualBoilRate = 0;
    private int waterInputRate = 0;
    private int waterInputAccumulator = 0;

    public BoilerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void updateConnectivity() {
        super.updateConnectivity();
        if (isController()) {
            int capacity = getWidth() * getWidth() * getHeight() * 8000;
            waterBuffer.setCapacity(capacity);
        }
    }

    public IFluidHandler getCustomFluidHandler() {
        return customFluidHandler;
    }

    public void recordWaterInput(int amount) {
        if (isController()) {
            waterInputAccumulator += amount;
        } else if (getControllerBE() instanceof BoilerBlockEntity controller) {
            controller.waterInputAccumulator += amount;
        }
    }

    public int getMaxBoilRate() {
        int heatMaxRate = heatLevel * BASE_CONVERSION_RATE;
        int totalTankBlocks = getWidth() * getWidth() * getHeight();
        int sizeMaxRate = (totalTankBlocks * BASE_CONVERSION_RATE) / BLOCKS_PER_HEAT_LEVEL;

        return Math.min(heatMaxRate, sizeMaxRate);
    }

    @Override
    public void tick() {
        super.tick();

        if (level != null && !level.isClientSide && isController()) {
            tickCounter++;

            int currentHeat = calculateHeatUnderneath();
            if (currentHeat != heatLevel) {
                heatLevel = currentHeat;
                setChanged();
                sendData();
            }

            int lastBoilRate = actualBoilRate;
            actualBoilRate = 0;

            if (heatLevel > 0 && !waterBuffer.isEmpty()) {
                int maxConversionRate = getMaxBoilRate();

                if (tankInventory.isEmpty() || tankInventory.getFluid().is(CreateEnrichedFluids.STEAM_LIQUID.get())) {
                    int spaceForSteam = tankInventory.getCapacity() - tankInventory.getFluidAmount();
                    int actualConversion = Math.min(maxConversionRate, Math.min(waterBuffer.getFluidAmount(), spaceForSteam));

                    if (actualConversion > 0) {
                        waterBuffer.drain(actualConversion, IFluidHandler.FluidAction.EXECUTE);
                        tankInventory.fill(new FluidStack(CreateEnrichedFluids.STEAM_LIQUID.get(), actualConversion), IFluidHandler.FluidAction.EXECUTE);

                        actualBoilRate = actualConversion;
                    }
                }
            }

            // Cycle water input tracking per tick
            waterInputRate = waterInputAccumulator;
            waterInputAccumulator = 0;

            if (lastBoilRate != actualBoilRate || waterInputRate > 0) {
                setChanged();
                sendData();
            }
        }
    }

    public int calculateHeatUnderneath() {
        if (level == null) return 0;
        int activeHeat = 0;
        boolean hasPassiveHeat = false;

        BlockPos controllerPos = getBlockPos();
        BlockPos layerBelow = controllerPos.below();

        for (int x = 0; x < getWidth(); x++) {
            for (int z = 0; z < getWidth(); z++) {
                BlockPos checkPos = layerBelow.offset(x, 0, z);
                BlockState state = level.getBlockState(checkPos);

                if (state.getBlock() instanceof BlazeBurnerBlock) {
                    BlazeBurnerBlock.HeatLevel heat = state.getValue(BlazeBurnerBlock.HEAT_LEVEL);
                    if (heat == BlazeBurnerBlock.HeatLevel.SEETHING) {
                        activeHeat += 2;
                    } else if (heat == BlazeBurnerBlock.HeatLevel.KINDLED) {
                        activeHeat += 1;
                    } else if (heat == BlazeBurnerBlock.HeatLevel.SMOULDERING || heat == BlazeBurnerBlock.HeatLevel.FADING) {
                        hasPassiveHeat = true;
                    }
                } else if (state.is(Blocks.FIRE) || state.is(Blocks.SOUL_FIRE) || state.is(Blocks.LAVA) || state.is(Blocks.MAGMA_BLOCK)) {
                    hasPassiveHeat = true;
                } else if (state.hasProperty(BlockStateProperties.LIT) && state.getValue(BlockStateProperties.LIT)) {
                    hasPassiveHeat = true;
                }
            }
        }

        if (activeHeat > 0) {
            return activeHeat;
        } else if (hasPassiveHeat) {
            return 1;
        }

        return 0;
    }

    private Component buildCleanBar(String label, int current, int max, String unit, int barLength, ChatFormatting fillColor, boolean showPercent) {
        float progress = max > 0 ? Math.min(1.0f, Math.max(0.0f, (float) current / max)) : 0;
        int filled = Math.round(progress * barLength);
        int empty = barLength - filled;

        String paddedLabel = switch (label) {
            case "Boil Rate" -> "Boil Rate  ";
            case "Water"     -> "Water      ";
            case "Steam Tank" -> "Steam Tank ";
            default          -> String.format("%-11s", label);
        };

        String barFilled = "█".repeat(filled);
        String barEmpty = "░".repeat(empty);

        int percent = max > 0 ? (int) (((float) current / max) * 100) : 0;
        String numberStr = current + " / " + max + (unit.isEmpty() ? "" : " " + unit);
        if (showPercent) {
            numberStr += " (" + percent + "%)";
        }

        MutableComponent line = Component.literal("    " + paddedLabel + ": ").withStyle(ChatFormatting.GRAY);
        line.append(Component.literal("[").withStyle(ChatFormatting.DARK_GRAY));
        if (filled > 0) {
            line.append(Component.literal(barFilled).withStyle(fillColor));
        }
        if (empty > 0) {
            line.append(Component.literal(barEmpty).withStyle(ChatFormatting.DARK_GRAY));
        }
        line.append(Component.literal("] ").withStyle(ChatFormatting.DARK_GRAY));
        line.append(Component.literal(numberStr).withStyle(ChatFormatting.WHITE));
        return line;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        BoilerBlockEntity controller = (BoilerBlockEntity) getControllerBE();
        if (controller == null) controller = this;

        int heat = controller.heatLevel;
        int maxRate = controller.getMaxBoilRate();
        int totalTankBlocks = controller.getWidth() * controller.getWidth() * controller.getHeight();
        int sizeMaxRate = (totalTankBlocks * BASE_CONVERSION_RATE) / BLOCKS_PER_HEAT_LEVEL;
        int potentialHeatRate = heat * BASE_CONVERSION_RATE;

        // Reduced from 6 spaces to 5 spaces to pull text closer to goggles icon
        tooltip.add(Component.literal("    ").append(Component.literal("Boiler Stats").withStyle(ChatFormatting.GOLD)));

        // Heat Level
        tooltip.add(Component.literal("    Heat Level: ").withStyle(ChatFormatting.GRAY)
                .append(Component.literal(heat + " / " + MAX_HEAT_LEVEL).withStyle(ChatFormatting.YELLOW)));

        // Boil Rate
        tooltip.add(buildCleanBar("Boil Rate", controller.actualBoilRate, maxRate, "mB/t", 10, ChatFormatting.GREEN, false));

        // Water
        tooltip.add(buildCleanBar("Water", controller.waterInputRate, maxRate, "mB/t", 10, ChatFormatting.AQUA, true));

        // Steam Tank Output
        int steamAmount = controller.tankInventory.getFluidAmount();
        int steamCap = controller.tankInventory.getCapacity();
        tooltip.add(buildCleanBar("Steam Tank", steamAmount, steamCap, "mB", 10, ChatFormatting.WHITE, false));

        if (sizeMaxRate < potentialHeatRate) {
            tooltip.add(Component.literal("    (!) Tank size limits output").withStyle(ChatFormatting.RED));
        }

        return true;
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.put("WaterBuffer", waterBuffer.writeToNBT(registries, new CompoundTag()));
        compound.putInt("HeatLevel", heatLevel);
        compound.putInt("ActualBoilRate", actualBoilRate);
        compound.putInt("WaterInputRate", waterInputRate);
    }

    @Override
    public void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        waterBuffer.readFromNBT(registries, compound.getCompound("WaterBuffer"));
        heatLevel = compound.getInt("HeatLevel");
        actualBoilRate = compound.getInt("ActualBoilRate");
        waterInputRate = compound.getInt("WaterInputRate");
    }

    public static class BoilerFluidHandler implements IFluidHandler {
        private final BoilerBlockEntity blockEntity;

        public BoilerFluidHandler(BoilerBlockEntity blockEntity) {
            this.blockEntity = blockEntity;
        }

        private BoilerBlockEntity getController() {
            if (blockEntity.getControllerBE() instanceof BoilerBlockEntity controller) {
                return controller;
            }
            return blockEntity;
        }

        @Override
        public int getTanks() {
            return 2;
        }

        @Override
        public FluidStack getFluidInTank(int tank) {
            BoilerBlockEntity c = getController();
            return tank == 0 ? c.waterBuffer.getFluid() : c.tankInventory.getFluid();
        }

        @Override
        public int getTankCapacity(int tank) {
            BoilerBlockEntity c = getController();
            return tank == 0 ? c.waterBuffer.getCapacity() : c.tankInventory.getCapacity();
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            if (tank == 0) return stack.is(Fluids.WATER);
            if (tank == 1) return stack.is(CreateEnrichedFluids.STEAM_LIQUID.get());
            return false;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (resource.isEmpty()) return 0;
            BoilerBlockEntity c = getController();

            if (resource.is(Fluids.WATER)) {
                int filled = c.waterBuffer.fill(resource, action);
                if (filled > 0 && action.execute()) {
                    c.recordWaterInput(filled);
                    c.setChanged();
                    c.sendData();
                }
                return filled;
            }
            return 0;
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            if (resource.isEmpty()) return FluidStack.EMPTY;
            BoilerBlockEntity c = getController();

            if (resource.is(CreateEnrichedFluids.STEAM_LIQUID.get())) {
                FluidStack drained = c.tankInventory.drain(resource, action);
                if (!drained.isEmpty() && action.execute()) {
                    c.setChanged();
                    c.sendData();
                }
                return drained;
            }

            return FluidStack.EMPTY;
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            BoilerBlockEntity c = getController();

            if (!c.tankInventory.isEmpty()) {
                FluidStack drained = c.tankInventory.drain(maxDrain, action);
                if (!drained.isEmpty() && action.execute()) {
                    c.setChanged();
                    c.sendData();
                }
                return drained;
            }

            return FluidStack.EMPTY;
        }
    }
}