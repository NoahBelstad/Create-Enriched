package com.noahbelstad.create_enriched.item;

import com.noahbelstad.create_enriched.block.BoilerBlockEntity;
import com.noahbelstad.create_enriched.block.CreateEnrichedBlocks;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

public class BoilerConverterItem extends Item {
    public BoilerConverterItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockEntity be = level.getBlockEntity(pos);
        Player player = context.getPlayer();

        // Check if it's a fluid tank, but explicitly NOT an already converted boiler block entity
        if (be instanceof FluidTankBlockEntity tankBE && !(be instanceof BoilerBlockEntity)) {
            if (!level.isClientSide) {
                FluidTankBlockEntity controller = tankBE.getControllerBE();
                if (controller == null) {
                    controller = tankBE;
                }

                int width = controller.getWidth();
                int height = controller.getHeight();
                BlockPos controllerPos = controller.getBlockPos();

                List<BlockPos> multiblockPositions = new ArrayList<>();
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        for (int z = 0; z < width; z++) {
                            multiblockPositions.add(controllerPos.offset(x, y, z));
                        }
                    }
                }

                // 1. Replace all fluid tank blocks with Boiler blocks
                for (BlockPos tankPos : multiblockPositions) {
                    level.setBlock(tankPos, CreateEnrichedBlocks.BOILER_BLOCK.get().defaultBlockState(), 3);
                }

                // 2. Trigger multiblock assembly on the new BoilerBlockEntities
                for (BlockPos tankPos : multiblockPositions) {
                    if (level.getBlockEntity(tankPos) instanceof BoilerBlockEntity boilerBE) {
                        boilerBE.updateConnectivity();
                    }
                }

                if (player != null && !player.getAbilities().instabuild) {
                    context.getItemInHand().shrink(1);
                }

                level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}