package com.noahbelstad.create_enriched.worldgen;

import com.noahbelstad.create_enriched.block.CreateEnrichedBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class BedrockDepositFeature extends Feature<NoneFeatureConfiguration> {

    public BedrockDepositFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        var random = context.random();

        int x = origin.getX() + random.nextInt(16);
        int z = origin.getZ() + random.nextInt(16);

        int minY = level.getMinBuildHeight();
        int maxY = minY + 10;

        BlockPos topBedrockPos = null;

        // Scans from Y=-54 down to Y=-64 to locate the top-most bedrock block
        for (int y = maxY; y >= minY; y--) {
            BlockPos checkPos = new BlockPos(x, y, z);
            if (level.getBlockState(checkPos).is(Blocks.BEDROCK)) {
                topBedrockPos = checkPos;
                break;
            }
        }

        // Replaces only the top-level bedrock block
        if (topBedrockPos != null) {
            level.setBlock(topBedrockPos, CreateEnrichedBlocks.BEDROCK_STEAM_VENT_BLOCK.getDefaultState(), 2);
            return true;
        }

        return false;
    }
}