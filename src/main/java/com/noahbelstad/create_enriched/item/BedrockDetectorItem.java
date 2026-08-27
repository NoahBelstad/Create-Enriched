package com.noahbelstad.create_enriched.item;

import com.noahbelstad.create_enriched.block.CreateEnrichedBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BedrockDetectorItem extends Item {
    public BedrockDetectorItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            BlockPos playerPos = serverPlayer.blockPosition();
            int radius = 128; // Search radius in blocks
            BlockPos closestPos = null;
            double minDistanceSq = Double.MAX_VALUE;

            int minY = level.getMinBuildHeight();
            int maxY = minY + 15;

            // Search surrounding bedrock Y-levels for the block
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    for (int y = minY; y <= maxY; y++) {
                        BlockPos checkPos = new BlockPos(playerPos.getX() + x, y, playerPos.getZ() + z);
                        if (level.isLoaded(checkPos) && level.getBlockState(checkPos).is(CreateEnrichedBlocks.BEDROCK_DEPOSIT_BLOCK.get())) {
                            double distSq = playerPos.distSqr(checkPos);
                            if (distSq < minDistanceSq) {
                                minDistanceSq = distSq;
                                closestPos = checkPos;
                            }
                        }
                    }
                }
            }

            if (closestPos != null) {
                int distance = (int) Math.sqrt(minDistanceSq);
                serverPlayer.sendSystemMessage(
                        Component.literal("Found Bedrock Deposit at [")
                                .append(Component.literal(closestPos.getX() + ", " + closestPos.getY() + ", " + closestPos.getZ()).withStyle(ChatFormatting.GREEN))
                                .append("] (" + distance + " blocks away)")
                );
            } else {
                serverPlayer.sendSystemMessage(
                        Component.literal("No Bedrock Deposit found within " + radius + " blocks of loaded chunks.").withStyle(ChatFormatting.RED)
                );
            }
        }

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }
}