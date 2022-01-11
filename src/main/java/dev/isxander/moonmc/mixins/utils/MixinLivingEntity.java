package dev.isxander.moonmc.mixins.utils;

import dev.isxander.moonmc.utils.ItemListener;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
    @Shadow public abstract ItemStack getMainHandStack();

    @Inject(method = "swingHand(Lnet/minecraft/util/Hand;Z)V", at = @At("HEAD"))
    public void onSwingHand(Hand hand, boolean fromServerPlayer, CallbackInfo ci) {
        ItemStack stack = getMainHandStack();
        if (stack != null && stack.getItem() instanceof ItemListener listener) {
            listener.onSwing((LivingEntity) (Object) this, ((LivingEntity) (Object) this).world, hand, stack);
        }
    }
}
