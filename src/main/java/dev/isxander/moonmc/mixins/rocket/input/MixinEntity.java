package dev.isxander.moonmc.mixins.rocket.input;

import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public abstract class MixinEntity {
    @Shadow @Nullable public abstract Entity getVehicle();
}
