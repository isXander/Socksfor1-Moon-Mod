package dev.isxander.moonmc.mixins.rocket.perspective;

import dev.isxander.moonmc.transport.rocket.RocketEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity {
    @Shadow @Final protected MinecraftClient client;

    @Inject(method = "startRiding", at = @At("TAIL"))
    public void modifyPerspectiveOnRocket(Entity entity, boolean force, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof RocketEntity) {
            Perspective currentPerspective = client.options.getPerspective();
            if (currentPerspective.isFirstPerson()) {
                client.options.setPerspective(currentPerspective.next());
            }
        }
    }
}
