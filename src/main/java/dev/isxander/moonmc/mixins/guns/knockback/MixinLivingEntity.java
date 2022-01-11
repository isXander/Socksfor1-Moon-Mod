package dev.isxander.moonmc.mixins.guns.knockback;

import dev.isxander.moonmc.weapons.ranged.ParticleBullet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {
    /**
     * Lessen knockback on bullet hits.
     */
    @ModifyArgs(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;takeKnockback(DDD)V"))
    public void modifyKnockback(Args args, DamageSource source, float amount) {
        if (source instanceof ParticleBullet.BulletDamageSource) {
            double strength = args.get(0);
            args.set(0, strength * 0.3);
        }
    }
}
