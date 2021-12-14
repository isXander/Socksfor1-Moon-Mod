package dev.isxander.moonmc.mixins.nowater;

import net.minecraft.block.Blocks;
import net.minecraft.world.gen.chunk.AquiferSampler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(AquiferSampler.Impl.class)
public class MixinAquiferSamplerImpl {
    /**
     * @author isXander
     */
    @Overwrite
    public AquiferSampler.FluidLevel getFluidLevel(int x, int y, int z) {
        return new AquiferSampler.FluidLevel(-64, Blocks.AIR.getDefaultState());
    }
}
