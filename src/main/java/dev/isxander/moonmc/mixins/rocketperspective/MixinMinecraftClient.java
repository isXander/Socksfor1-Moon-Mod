package dev.isxander.moonmc.mixins.rocketperspective;

import dev.isxander.moonmc.transport.rocket.RocketEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
    @ModifyArg(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;setPerspective(Lnet/minecraft/client/option/Perspective;)V"))
    public Perspective modifyPerspective(Perspective perspective) {
        MinecraftClient client = (MinecraftClient) (Object) this;

        if (client.player != null && client.player.getVehicle() instanceof RocketEntity) {
            if (perspective.isFirstPerson()) return perspective.next();
        }

        return perspective;
    }
}
