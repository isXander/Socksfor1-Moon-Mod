package dev.isxander.moonmc.mixins.guns.damage;

import dev.isxander.moonmc.weapons.ranged.ParticleBullet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {
    /**
     * Remove the damage timer for bullets
     */
    @Redirect(method = "damage", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;timeUntilRegen:I", opcode = Opcodes.GETFIELD, ordinal = 0))
    public int onGetTimeUntilRegen(LivingEntity instance, DamageSource source, float amount) {
        if (source instanceof ParticleBullet.BulletDamageSource) return 10;
        return instance.timeUntilRegen;
    }
}
