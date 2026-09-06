package com.noahbelstad.create_enriched.block;

import com.noahbelstad.create_enriched.fluid.CreateEnrichedFluids;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ThoriumReactorBoilerBlockEntity extends BlockEntity implements IHaveGoggleInformation {

    private static final int TANK_CAPACITY = 8000;
    private static final int CONVERSION_RATE = 25; // Matches Level 9 Boiler output (90 mB/t)

    private final FluidTank waterTank = new FluidTank(TANK_CAPACITY) {
        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid().isSame(Fluids.WATER);
        }

        @Override
        protected void onContentsChanged() {
            setChanged();
            syncToClient();
        }
    };

    private final FluidTank steamTank = new FluidTank(TANK_CAPACITY) {
        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid().isSame(CreateEnrichedFluids.STEAM_LIQUID.get());
        }

        @Override
        protected void onContentsChanged() {
            setChanged();
            syncToClient();
        }
    };

    private final IFluidHandler combinedFluidHandler = new IFluidHandler() {
        @Override
        public int getTanks() {
            return 2;
        }

        @NotNull
        @Override
        public FluidStack getFluidInTank(int tank) {
            return tank == 0 ? waterTank.getFluid() : steamTank.getFluid();
        }

        @Override
        public int getTankCapacity(int tank) {
            return TANK_CAPACITY;
        }

        @Override
        public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            return tank == 0 ? waterTank.isFluidValid(stack) : steamTank.isFluidValid(stack);
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (resource.getFluid().isSame(Fluids.WATER)) {
                return waterTank.fill(resource, action);
            }
            return 0;
        }

        @NotNull
        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            if (resource.getFluid().isSame(CreateEnrichedFluids.STEAM_LIQUID.get())) {
                return steamTank.drain(resource, action);
            }
            return FluidStack.EMPTY;
        }

        @NotNull
        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            return steamTank.drain(maxDrain, action);
        }
    };

    public ThoriumReactorBoilerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;

        BlockPos posBelow = pos.below();
        BlockState stateBelow = level.getBlockState(posBelow);

        boolean isCoreActive = stateBelow.getBlock() instanceof ThoriumReactorCoreBlock
                && stateBelow.hasProperty(ThoriumReactorCoreBlock.ACTIVE)
                && stateBelow.getValue(ThoriumReactorCoreBlock.ACTIVE);

        if (isCoreActive) {
            int waterAvailable = waterTank.getFluidAmount();
            int steamSpace = steamTank.getCapacity() - steamTank.getFluidAmount();

            int amountToConvert = Math.min(CONVERSION_RATE, Math.min(waterAvailable, steamSpace));

            if (amountToConvert > 0) {
                waterTank.drain(amountToConvert, IFluidHandler.FluidAction.EXECUTE);
                steamTank.fill(new FluidStack(CreateEnrichedFluids.STEAM_LIQUID.get(), amountToConvert), IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }

    private void syncToClient() {
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public IFluidHandler getFluidHandler() {
        return combinedFluidHandler;
    }

    public FluidTank getWaterTank() {
        return waterTank;
    }

    public FluidTank getSteamTank() {
        return steamTank;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        tooltip.add(Component.literal("    Thorium Boiler").withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.literal("    Boil Rate: ").withStyle(ChatFormatting.GRAY)
                .append(Component.literal(CONVERSION_RATE + " mB/t").withStyle(ChatFormatting.WHITE)));

        Component waterText = Component.literal(waterTank.getFluidAmount() + " / " + TANK_CAPACITY + " mB").withStyle(ChatFormatting.BLUE);
        Component steamText = Component.literal(steamTank.getFluidAmount() + " / " + TANK_CAPACITY + " mB").withStyle(ChatFormatting.WHITE);

        tooltip.add(Component.literal("    Water: ").append(waterText));
        tooltip.add(Component.literal("    Steam: ").append(steamText));

        return true;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        loadAdditional(tag, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("WaterTank", waterTank.writeToNBT(registries, new CompoundTag()));
        tag.put("SteamTank", steamTank.writeToNBT(registries, new CompoundTag()));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        waterTank.readFromNBT(registries, tag.getCompound("WaterTank"));
        steamTank.readFromNBT(registries, tag.getCompound("SteamTank"));
    }
}