package dev.isxander.moonmc.mixins.lowgravity;

import dev.isxander.moonmc.MoonMod;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.fluid.Fluid;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class MixinEntity {
    @Shadow protected abstract BlockPos getVelocityAffectingPos();

    @Shadow protected abstract void setFlag(int index, boolean value);

    @Shadow public abstract void playSound(SoundEvent sound, float volume, float pitch);

    @Shadow public abstract float getPitch();

    @Shadow public abstract Vec3d getRotationVector();

    @Shadow
    public float fallDistance;

    @Shadow public abstract boolean hasNoGravity();

    @Shadow public abstract double getSwimHeight();

    @Shadow public abstract double getFluidHeight(Tag<Fluid> fluid);

    @Shadow public abstract boolean isInLava();

    @Shadow public abstract boolean doesNotCollide(double offsetX, double offsetY, double offsetZ);

    @Shadow public abstract void setVelocity(Vec3d velocity);

    @Shadow public abstract void setVelocity(double x, double y, double z);

    @Shadow public boolean horizontalCollision;

    @Shadow public abstract void move(MovementType movementType, Vec3d movement);

    @Shadow public abstract void updateVelocity(float speed, Vec3d movementInput);

    @Shadow protected boolean onGround;

    @Shadow public abstract boolean isSprinting();

    @Shadow public abstract double getY();

    @Shadow public abstract boolean isTouchingWater();

    @Shadow public abstract BlockPos getBlockPos();

    @Shadow public World world;

    @Shadow public abstract void onLanding();

    @Shadow public abstract boolean isLogicalSideForUpdatingMovement();

    @Shadow public abstract Vec3d getVelocity();

    @ModifyVariable(method = "fall", at = @At("HEAD"))
    public double modifyFallDistance(double heightDifference) {
        return heightDifference * MoonMod.gravityMultiplier;
    }
}
