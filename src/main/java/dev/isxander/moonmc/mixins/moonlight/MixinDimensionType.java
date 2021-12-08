package dev.isxander.moonmc.mixins.moonlight;

import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.OptionalLong;

@Mixin(DimensionType.class)
public abstract class MixinDimensionType {
    @Shadow @Final public static Identifier OVERWORLD_ID;

    @Shadow @Final @Mutable
    protected static DimensionType OVERWORLD = create(OptionalLong.empty(), true, false, false, true, 1.0, false, false, true, false, true, -64, 384, 384, BlockTags.INFINIBURN_OVERWORLD.getId(), OVERWORLD_ID, .2f);

    @Shadow
    public static DimensionType create(OptionalLong fixedTime, boolean hasSkylight, boolean hasCeiling, boolean ultrawarm, boolean natural, double coordinateScale, boolean hasEnderDragonFight, boolean piglinSafe, boolean bedWorks, boolean respawnAnchorWorks, boolean hasRaids, int minimumY, int height, int logicalHeight, Identifier infiniburn, Identifier effects, float ambientLight) {
        return null;
    }
}
