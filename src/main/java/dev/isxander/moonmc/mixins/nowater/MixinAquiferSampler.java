package dev.isxander.moonmc.mixins.nowater;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.chunk.AquiferSampler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(AquiferSampler.class)
public interface MixinAquiferSampler {
    /**
     * @author isXander
     */
    @Overwrite
    static AquiferSampler seaLevel(final AquiferSampler.FluidLevelSampler fluidLevelSampler) {
        return new AquiferSampler() {
            @Nullable
            @Override
            public BlockState apply(int x, int y, int z, double d, double e) {
                return Blocks.AIR.getDefaultState();
            }

            @Override
            public boolean needsFluidTick() {
                return false;
            }
        };
    }
}
