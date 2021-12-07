package dev.isxander.moonmc.mixins.oxygen;

import dev.isxander.moonmc.oxygen.LivingEntityExtKt;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class MixinInGameHud extends DrawableHelper {
    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", shift = At.Shift.AFTER, ordinal = 2), cancellable = true)
    public void renderOxygenLevel(MatrixStack matrices, CallbackInfo ci) {
        PlayerEntity playerEntity = getCameraPlayer();

        int maxAir = playerEntity.getMaxAir();
        int currentAir = playerEntity.getAir();

        int maxOxygen = LivingEntityExtKt.MAX_OXYGEN;
        int currentOxygen = LivingEntityExtKt.getOxygen(playerEntity);

        int o2Percent = currentOxygen / maxOxygen;
        int airPercent = currentAir / maxAir;

        int max;
        int current;

        if (airPercent < o2Percent) {
            max = maxAir;
            current = currentAir;
        } else {
            max = maxOxygen;
            current = currentOxygen;
        }

        if (current < max || playerEntity.isSubmergedInWater() || !LivingEntityExtKt.isWearingOxygenMask(playerEntity)) {
            int ab = MathHelper.ceil((double)(current - 2) * 10.0 / (double)max);
            int ac = MathHelper.ceil((double)current * 10.0 / (double)max) - ab;

            int n = this.scaledWidth / 2 + 91;
            int t = this.scaledHeight - 49;

            for (int ad = 0; ad < ab + ac; ++ad) {
                if (ad < ab) {
                    this.drawTexture(matrices, n - ad * 8 - 9, t, 16, 18, 9, 9);
                    continue;
                }
                this.drawTexture(matrices, n - ad * 8 - 9, t, 25, 18, 9, 9);
            }
        }

        this.client.getProfiler().pop();
        ci.cancel();
    }
}
