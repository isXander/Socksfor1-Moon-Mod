package dev.isxander.moonmc.mixins.oxygen;

import dev.isxander.moonmc.oxygen.EntityOxygenUtils;
import dev.isxander.moonmc.oxygen.LivingEntityOxygenAccess;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity implements LivingEntityOxygenAccess {
    @Shadow public abstract boolean isAlive();

    private int oxygen = EntityOxygenUtils.MAX_OXYGEN;

    @Override public int getOxygen() { return oxygen; }
    @Override public void setOxygen(int oxygen) { this.oxygen = oxygen; }

    @Inject(method = "baseTick", at = @At("RETURN"))
    public void updateOxygenTick(CallbackInfo ci) {
        if (this.isAlive()) {
            EntityOxygenUtils.updateOxygen((LivingEntity) (Object) this);
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    public void writeCustomNbt(NbtCompound nbt, CallbackInfo ci) {
        EntityOxygenUtils.writeExtraNbt((LivingEntity) (Object) this, nbt);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    public void readCustomNbt(NbtCompound nbt, CallbackInfo ci) {
        EntityOxygenUtils.readExtraNbt((LivingEntity) (Object) this, nbt);
    }
}
