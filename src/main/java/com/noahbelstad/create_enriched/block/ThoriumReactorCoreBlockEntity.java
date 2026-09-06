package com.noahbelstad.create_enriched.block;

import com.noahbelstad.create_enriched.item.CreateEnrichedItems;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ThoriumReactorCoreBlockEntity extends BlockEntity implements IHaveGoggleInformation {

    private final ItemStackHandler fuelInventory = new ItemStackHandler(1) {
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.is(CreateEnrichedItems.LOW_PURITY_ENRICHED_THORIUM_PELLET.get());
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            syncToClient();
        }
    };

    private final ItemStackHandler rodInventory = new ItemStackHandler(1) {
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.is(CreateEnrichedItems.GRAPHITE_ROD.get());
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            syncToClient();
        }
    };

    private final IItemHandler automationHandler = new IItemHandler() {
        @Override
        public int getSlots() {
            return 2;
        }

        @Override
        @NotNull
        public ItemStack getStackInSlot(int slot) {
            if (slot == 0) return fuelInventory.getStackInSlot(0);
            if (slot == 1) return rodInventory.getStackInSlot(0);
            return ItemStack.EMPTY;
        }

        @Override
        @NotNull
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (slot == 0) {
                return fuelInventory.insertItem(0, stack, simulate);
            } else if (slot == 1) {
                return rodInventory.insertItem(0, stack, simulate);
            }
            return stack;
        }

        @Override
        @NotNull
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            if (slot == 0) return fuelInventory.getSlotLimit(0);
            if (slot == 1) return rodInventory.getSlotLimit(0);
            return 0;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot == 0) return fuelInventory.isItemValid(0, stack);
            if (slot == 1) return rodInventory.isItemValid(0, stack);
            return false;
        }
    };

    private boolean isActive = false;
    private int burnProgress = 0;
    private final int maxBurnTime = 6000;

    public ThoriumReactorCoreBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;

        boolean hasFuel = !fuelInventory.getStackInSlot(0).isEmpty();
        boolean hasRod = !rodInventory.getStackInSlot(0).isEmpty();

        boolean shouldBeActive = hasFuel && hasRod;

        if (shouldBeActive) {
            burnProgress++;

            if (burnProgress >= maxBurnTime) {
                burnProgress = 0;
                fuelInventory.extractItem(0, 1, false);
                rodInventory.extractItem(0, 1, false);
            }
        } else {
            burnProgress = 0;
        }

        if (this.isActive != shouldBeActive) {
            this.isActive = shouldBeActive;
            BlockState newState = state.setValue(ThoriumReactorCoreBlock.ACTIVE, this.isActive);
            level.setBlock(pos, newState, 3);
            setChanged();
            syncToClient();
        }
    }

    private void syncToClient() {
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public boolean isActive() {
        return this.isActive;
    }

    public IItemHandler getAutomationHandler() {
        return automationHandler;
    }

    public ItemStackHandler getFuelInventory() {
        return fuelInventory;
    }

    public ItemStackHandler getRodInventory() {
        return rodInventory;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        tooltip.add(Component.literal("    Thorium Reactor Core").withStyle(ChatFormatting.GOLD));

        Component statusText = isActive
                ? Component.literal("Active").withStyle(ChatFormatting.GREEN)
                : Component.literal("Idle").withStyle(ChatFormatting.RED);
        tooltip.add(Component.literal(" Status: ").append(statusText));

        ItemStack fuelStack = fuelInventory.getStackInSlot(0);
        ItemStack rodStack = rodInventory.getStackInSlot(0);

        Component fuelText = fuelStack.isEmpty()
                ? Component.literal("Empty").withStyle(ChatFormatting.GRAY)
                : Component.literal("Low purity: ").withStyle(ChatFormatting.GOLD)
                .append(Component.literal(String.valueOf(fuelStack.getCount())).withStyle(ChatFormatting.WHITE));

        Component rodText = rodStack.isEmpty()
                ? Component.literal("Empty").withStyle(ChatFormatting.GRAY)
                : Component.literal("Low purity: ").withStyle(ChatFormatting.GOLD)
                .append(Component.literal(String.valueOf(rodStack.getCount())).withStyle(ChatFormatting.WHITE));

        tooltip.add(Component.literal(" Fuel Pellet: ").append(fuelText));
        tooltip.add(Component.literal(" Graphite Rod: ").append(rodText));

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
        tag.put("FuelInventory", fuelInventory.serializeNBT(registries));
        tag.put("RodInventory", rodInventory.serializeNBT(registries));
        tag.putInt("BurnProgress", burnProgress);
        tag.putBoolean("IsActive", isActive);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        fuelInventory.deserializeNBT(registries, tag.getCompound("FuelInventory"));
        rodInventory.deserializeNBT(registries, tag.getCompound("RodInventory"));
        burnProgress = tag.getInt("BurnProgress");
        isActive = tag.getBoolean("IsActive");
    }
}