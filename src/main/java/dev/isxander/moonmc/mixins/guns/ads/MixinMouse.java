package dev.isxander.moonmc.mixins.guns.ads;

import dev.isxander.moonmc.weapons.ranged.IGun;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Mouse.class)
public class MixinMouse {
    @Shadow @Final private MinecraftClient client;

    /**
     * changes the mouse sensitivity when in ads
     */
    @ModifyVariable(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/SmoothUtil;clear()V", ordinal = 2), index = 13)
    public double modifyMouseSensitivity(double sensitivity) {
        if (client.player == null) return sensitivity;
        ItemStack stack = client.player.getOffHandStack();

        if (stack != null && stack.getItem() instanceof IGun gun) {
            return sensitivity * gun.getAdsSensitivity();
        }

        return sensitivity;
    }
}
