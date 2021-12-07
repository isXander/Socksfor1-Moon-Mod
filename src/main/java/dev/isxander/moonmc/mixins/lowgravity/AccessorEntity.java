package dev.isxander.moonmc.mixins.lowgravity;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface AccessorEntity {
    @Accessor("FALL_FLYING_FLAG_INDEX")
    static int getFallFlyingFlagIndex() {
        throw new AssertionError();
    }
}
