package com.noahbelstad.create_enriched.block;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BedrockSteamVentBlockEntity extends BlockEntity implements IHaveGoggleInformation {

    private static final int PRODUCTION_RATE = 40; // mB per tick

    private final FluidTank tank = new FluidTank(2500) {
        @Override
        protected void onContentsChanged() {
            setChanged();
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid().builtInRegistryHolder().key().location().getPath().contains("steam");
        }
    };

    public BedrockSteamVentBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public BedrockSteamVentBlockEntity(BlockPos pos, BlockState state) {
        this(CreateEnrichedBlocks.BEDROCK_STEAM_VENT_BE.get(), pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) return;

        // Produce Steam continuously up to capacity
        if (tank.getFluidAmount() < tank.getCapacity()) {
            Fluid steamFluid = getSteamFluid();
            if (steamFluid != null) {
                int filled = tank.fill(new FluidStack(steamFluid, PRODUCTION_RATE), IFluidHandler.FluidAction.EXECUTE);
                if (filled > 0) {
                    setChanged();
                    level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
                }
            }
        }
    }

    private Fluid getSteamFluid() {
        if (!tank.getFluid().isEmpty()) {
            return tank.getFluid().getFluid();
        }
        return BuiltInRegistries.FLUID.stream()
                .filter(f -> f.builtInRegistryHolder().key().location().getPath().contains("steam"))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        tooltip.add(Component.literal("    ").append(Component.translatable("block.create_enriched.bedrock_steam_vent").withStyle(ChatFormatting.GOLD)));

        tooltip.add(Component.literal("  ").append(Component.literal("Production: ").withStyle(ChatFormatting.GRAY))
                .append(Component.literal(PRODUCTION_RATE + " mB/t").withStyle(ChatFormatting.AQUA)));

        FluidStack fluid = tank.getFluid();
        Component fluidName = fluid.isEmpty()
                ? Component.literal("Empty")
                : fluid.getHoverName();

        tooltip.add(Component.literal("  ").append(Component.literal("Stored: ").withStyle(ChatFormatting.GRAY))
                .append(Component.literal(tank.getFluidAmount() + " / " + tank.getCapacity() + " mB").withStyle(ChatFormatting.GOLD))
                .append(Component.literal(" (").withStyle(ChatFormatting.DARK_GRAY))
                .append(fluidName.copy().withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(")").withStyle(ChatFormatting.DARK_GRAY)));

        return true;
    }

    public FluidTank getTank() {
        return tank;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tank.writeToNBT(registries, tag);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        tank.readFromNBT(registries, tag);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}