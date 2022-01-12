package dev.isxander.moonmc.mixins.lowgravity;

import dev.isxander.moonmc.MoonMod;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public class MixinEntity {
    @ModifyVariable(method = "fall", at = @At("HEAD"), argsOnly = true)
    public double modifyFallDistance(double heightDifference) {
        return heightDifference * MoonMod.gravityMultiplier;
    }
}
