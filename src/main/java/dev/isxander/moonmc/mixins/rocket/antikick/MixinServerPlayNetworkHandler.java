package dev.isxander.moonmc.mixins.rocket.antikick;

import dev.isxander.moonmc.transport.rocket.RocketEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class MixinServerPlayNetworkHandler {
    @Shadow protected abstract boolean isEntityOnAir(Entity entity);

    /**
     * Fixes the server kicking the player when they are flying in the rocket ship.
     */
    @Redirect(method = "onVehicleMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;isEntityOnAir(Lnet/minecraft/entity/Entity;)Z"))
    public boolean onIsEntityOnAir(ServerPlayNetworkHandler networkHandler, Entity entity) {
        return isEntityOnAir(entity) && !(entity.getRootVehicle() instanceof RocketEntity);
    }
}
