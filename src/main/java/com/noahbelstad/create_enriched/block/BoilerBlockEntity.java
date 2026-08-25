package com.noahbelstad.create_enriched.block;

import com.noahbelstad.create_enriched.CreateEnriched;
import com.noahbelstad.create_enriched.fluid.CreateEnrichedFluids;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.List;

public class BoilerBlockEntity extends FluidTankBlockEntity {
    // Shared conversion multiplier variable (mB/t per heat level)
    public static final int BASE_CONVERSION_RATE = 14;

    protected FluidTank waterBuffer = new FluidTank(8000);
    private final BoilerFluidHandler customFluidHandler = new BoilerFluidHandler(this);

    private int tickCounter = 0;
    private int heatLevel = 0;

    public BoilerBlockEntity(BlockPos pos, BlockState state) {
        super(CreateEnrichedBlocks.BOILER_BE.get(), pos, state);
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

    @Override
    public void tick() {
        super.tick();

        if (level != null && !level.isClientSide && isController()) {
            tickCounter++;

            // Recalculate heat every tick & sync immediately if heat state changes
            int currentHeat = calculateHeatUnderneath();
            if (currentHeat != heatLevel) {
                heatLevel = currentHeat;
                setChanged();
                sendData();
            }

            // Conversion: Water -> Steam
            if (heatLevel > 0 && !waterBuffer.isEmpty()) {
                int maxConversionRate = heatLevel * BASE_CONVERSION_RATE;

                if (tankInventory.isEmpty() || tankInventory.getFluid().is(CreateEnrichedFluids.STEAM_LIQUID_STILL.get())) {
                    int spaceForSteam = tankInventory.getCapacity() - tankInventory.getFluidAmount();
                    int actualConversion = Math.min(maxConversionRate, Math.min(waterBuffer.getFluidAmount(), spaceForSteam));

                    if (actualConversion > 0) {
                        waterBuffer.drain(actualConversion, IFluidHandler.FluidAction.EXECUTE);
                        tankInventory.fill(new FluidStack(CreateEnrichedFluids.STEAM_LIQUID_STILL.get(), actualConversion), IFluidHandler.FluidAction.EXECUTE);

                        setChanged();
                        sendData(); // Syncs Goggles UI continuously during conversion
                    }
                }
            }
        }
    }

    /**
     * Standard Create Mod Boiler Logic:
     * - Passive sources (Campfires, Smouldering Burners, Lava) = Level 1 Total Heat
     * - Kindled Burners (Fueled) = +1 Level each
     * - Seething Burners (Blaze Cake) = +2 Levels each
     */
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
            return 1; // Baseline Level 1 for passive heat
        }

        return 0;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        BoilerBlockEntity controller = (BoilerBlockEntity) getControllerBE();
        if (controller == null) controller = this;

        int heat = controller.heatLevel;
        int rate = heat * BASE_CONVERSION_RATE;

        tooltip.add(Component.literal("  ").append(Component.literal("Boiler Stats").withStyle(ChatFormatting.GOLD)));
        tooltip.add(Component.literal("    Heat Level: ").withStyle(ChatFormatting.GRAY)
                .append(Component.literal(String.valueOf(heat)).withStyle(ChatFormatting.YELLOW)));
        tooltip.add(Component.literal("    Boil Rate: ").withStyle(ChatFormatting.GRAY)
                .append(Component.literal(rate + " mB/t").withStyle(ChatFormatting.GREEN)));
        tooltip.add(Component.literal("    Water Buffer: ").withStyle(ChatFormatting.GRAY)
                .append(Component.literal(controller.waterBuffer.getFluidAmount() + " / " + controller.waterBuffer.getCapacity() + " mB").withStyle(ChatFormatting.AQUA)));
        tooltip.add(Component.literal("    Steam Output: ").withStyle(ChatFormatting.GRAY)
                .append(Component.literal(controller.tankInventory.getFluidAmount() + " / " + controller.tankInventory.getCapacity() + " mB").withStyle(ChatFormatting.WHITE)));

        return true;
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.put("WaterBuffer", waterBuffer.writeToNBT(registries, new CompoundTag()));
        compound.putInt("HeatLevel", heatLevel);
    }

    @Override
    public void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        waterBuffer.readFromNBT(registries, compound.getCompound("WaterBuffer"));
        heatLevel = compound.getInt("HeatLevel");
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
            if (tank == 1) return stack.is(CreateEnrichedFluids.STEAM_LIQUID_STILL.get());
            return false;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (resource.isEmpty()) return 0;
            BoilerBlockEntity c = getController();

            if (resource.is(Fluids.WATER)) {
                int filled = c.waterBuffer.fill(resource, action);
                if (filled > 0 && action.execute()) {
                    c.setChanged();
                    c.sendData(); // Sync Goggles UI immediately when water enters
                }
                return filled;
            }
            return 0;
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            if (resource.isEmpty()) return FluidStack.EMPTY;
            BoilerBlockEntity c = getController();

            // Only allow draining steam from the tank inventory. Water cannot be drained externally.
            if (resource.is(CreateEnrichedFluids.STEAM_LIQUID_STILL.get())) {
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

            // Only allow draining from the steam tank inventory.
            // This stops pumps from pulling raw water out of the waterBuffer.
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