package dev.isxander.moonmc.mixins.guns.ads;

import dev.isxander.moonmc.weapons.ranged.IGun;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHud {
    @Shadow @Final private MinecraftClient client;

    /**
     * prevents crosshair from being drawn when in ads
     *
     * @param matrices
     * @param ci
     */
    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void onRenderCrosshair(MatrixStack matrices, CallbackInfo ci) {
        if (client.player != null) {
            ItemStack stack = client.player.getStackInHand(Hand.OFF_HAND);
            if (stack != null && stack.getItem() instanceof IGun) {
                ci.cancel();
            }
        }
    }
}
