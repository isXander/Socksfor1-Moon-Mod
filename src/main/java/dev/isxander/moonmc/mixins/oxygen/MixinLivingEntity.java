package dev.isxander.moonmc.mixins.oxygen;

import dev.isxander.moonmc.oxygen.LivingEntityExtKt;
import dev.isxander.moonmc.oxygen.LivingEntityOxygenAccess;
import dev.isxander.moonmc.registry.MoonRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity implements LivingEntityOxygenAccess {
    @Shadow public abstract boolean isAlive();

    private int oxygen = LivingEntityExtKt.MAX_OXYGEN;

    @Override public int getOxygen() { return oxygen; }
    @Override public void setOxygen(int oxygen) { this.oxygen = oxygen; }

    @Inject(method = "baseTick", at = @At("RETURN"))
    public void updateOxygenTick(CallbackInfo ci) {
        if (this.isAlive()) {
            LivingEntityExtKt.updateOxygen((LivingEntity) (Object) this);
        }
    }
}
