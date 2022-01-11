package dev.isxander.moonmc.mixins.guns.ads;

import dev.isxander.moonmc.weapons.ranged.IGun;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public class MixinAbstractClientPlayerEntity {
    /**
     * Zooms in camera using "movement speed fov"
     * (doesn't actually modify movement speed)
     */
    @Inject(method = "getSpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getActiveItem()Lnet/minecraft/item/ItemStack;"), cancellable = true)
    public void onGetSpeed(CallbackInfoReturnable<Float> cir) {
        ItemStack stack = ((AbstractClientPlayerEntity) (Object) this).getStackInHand(Hand.OFF_HAND);
        if (stack == null) return;

        if (stack.getItem() instanceof IGun gun) {
            cir.setReturnValue(gun.getAdsZoom());
        }
    }
}
