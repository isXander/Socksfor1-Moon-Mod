package dev.isxander.moonmc.mixins.rocket.input;

import dev.isxander.moonmc.transport.rocket.RocketEntity;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends MixinEntity {
    @Shadow public Input input;

    @Inject(method = "tickRiding", at = @At("RETURN"))
    public void onTickRiding(CallbackInfo ci) {
        if (getVehicle() instanceof RocketEntity rocket) {
            rocket.setAccelerating(input.jumping);
        }
    }
}
