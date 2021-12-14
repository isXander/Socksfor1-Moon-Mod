package dev.isxander.moonmc.mixins.noclouds;

import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
public class MixinGameOptions {
    @Shadow public CloudRenderMode cloudRenderMode;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void cloudRenderMode(CallbackInfo ci) {
        this.cloudRenderMode = CloudRenderMode.OFF;
    }
}
