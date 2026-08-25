package com.noahbelstad.create_enriched.item;

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

        // Check if the target block entity is Create's Fluid Tank
        if (be instanceof FluidTankBlockEntity tankBE) {
            if (!level.isClientSide) {
                // Fetch the main controller for this specific multiblock
                FluidTankBlockEntity controller = tankBE.getControllerBE();
                if (controller == null) {
                    controller = tankBE;
                }

                int width = controller.getWidth();
                int height = controller.getHeight();

                // OPTIONAL: Require the tank to be an assembled multiblock (larger than 1x1x1)
                // If you want to require at least a 2x2 or taller structure, uncomment below:
                /*
                if (width == 1 && height == 1) {
                    return InteractionResult.FAIL;
                }
                */

                BlockPos controllerPos = controller.getBlockPos();

                // Calculate all positions belonging strictly to THIS multiblock structure
                List<BlockPos> multiblockPositions = new ArrayList<>();
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        for (int z = 0; z < width; z++) {
                            multiblockPositions.add(controllerPos.offset(x, y, z));
                        }
                    }
                }

                // Convert only the blocks belonging to this controller
                for (BlockPos tankPos : multiblockPositions) {
                    level.setBlock(tankPos, CreateEnrichedBlocks.BOILER_BLOCK.get().defaultBlockState(), 3);
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