package dev.isxander.moonmc.mixins.lowgravity;

import dev.isxander.moonmc.MoonMod;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends MixinEntity {
    @Shadow public abstract boolean canMoveVoluntarily();

    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow protected abstract boolean shouldSwimInFluids();

    @Shadow public abstract boolean canWalkOnFluid(Fluid fluid);

    @Shadow protected abstract float getBaseMovementSpeedMultiplier();

    @Shadow public abstract float getMovementSpeed();

    @Shadow public abstract boolean isClimbing();

    @Shadow public abstract Vec3d method_26317(double d, boolean bl, Vec3d vec3d);

    @Shadow public abstract boolean isFallFlying();

    @Shadow protected abstract SoundEvent getFallSound(int distance);

    @Shadow public abstract boolean damage(DamageSource source, float amount);

    @Shadow public abstract Vec3d applyMovementInput(Vec3d movementInput, float slipperiness);

    @Shadow
    public abstract @Nullable StatusEffectInstance getStatusEffect(StatusEffect effect);

    @Shadow public abstract boolean hasNoDrag();

    @Shadow public abstract void updateLimbs(LivingEntity entity, boolean flutter);

    @Overwrite
    public void travel(Vec3d movementInput) {
        if (this.canMoveVoluntarily() || this.isLogicalSideForUpdatingMovement()) {
            boolean bl;
            double d = 0.08;
            boolean bl2 = bl = this.getVelocity().y <= 0.0;
            if (bl && this.hasStatusEffect(StatusEffects.SLOW_FALLING)) {
                d = 0.01;
                this.onLanding();
            }
            FluidState fluidState = this.world.getFluidState(this.getBlockPos());
            if (this.isTouchingWater() && this.shouldSwimInFluids() && !this.canWalkOnFluid(fluidState.getFluid())) {
                double e = this.getY();
                float f = this.isSprinting() ? 0.9f : this.getBaseMovementSpeedMultiplier();
                float g = 0.02f;
                float h = EnchantmentHelper.getDepthStrider((LivingEntity) (Object) this);
                if (h > 3.0f) {
                    h = 3.0f;
                }
                if (!this.onGround) {
                    h *= 0.5f;
                }
                if (h > 0.0f) {
                    f += (0.54600006f - f) * h / 3.0f;
                    g += (this.getMovementSpeed() - g) * h / 3.0f;
                }
                if (this.hasStatusEffect(StatusEffects.DOLPHINS_GRACE)) {
                    f = 0.96f;
                }
                this.updateVelocity(g, movementInput);
                this.move(MovementType.SELF, this.getVelocity());
                Vec3d vec3d = this.getVelocity();
                if (this.horizontalCollision && this.isClimbing()) {
                    vec3d = new Vec3d(vec3d.x, 0.2, vec3d.z);
                }
                this.setVelocity(vec3d.multiply(f, 0.8f, f));
                Vec3d vec3d2 = this.method_26317(d, bl, this.getVelocity());
                this.setVelocity(vec3d2);
                if (this.horizontalCollision && this.doesNotCollide(vec3d2.x, vec3d2.y + (double)0.6f - this.getY() + e, vec3d2.z)) {
                    this.setVelocity(vec3d2.x, 0.3f, vec3d2.z);
                }
            } else if (this.isInLava() && this.shouldSwimInFluids() && !this.canWalkOnFluid(fluidState.getFluid())) {
                Vec3d f;
                double e = this.getY();
                this.updateVelocity(0.02f, movementInput);
                this.move(MovementType.SELF, this.getVelocity());
                if (this.getFluidHeight(FluidTags.LAVA) <= this.getSwimHeight()) {
                    this.setVelocity(this.getVelocity().multiply(0.5, 0.8f, 0.5));
                    f = this.method_26317(d, bl, this.getVelocity());
                    this.setVelocity(f);
                } else {
                    this.setVelocity(this.getVelocity().multiply(0.5));
                }
                if (!this.hasNoGravity()) {
                    this.setVelocity(this.getVelocity().add(0.0, -d / 4.0, 0.0));
                }
                f = this.getVelocity();
                if (this.horizontalCollision && this.doesNotCollide(f.x, f.y + (double)0.6f - this.getY() + e, f.z)) {
                    this.setVelocity(f.x, 0.3f, f.z);
                }
            } else if (this.isFallFlying()) {
                double l;
                float m;
                double k;
                Vec3d e = this.getVelocity();
                if (e.y > -0.5) {
                    this.fallDistance = 1.0f;
                }
                Vec3d vec3d3 = this.getRotationVector();
                float f = this.getPitch() * ((float)Math.PI / 180);
                double g = Math.sqrt(vec3d3.x * vec3d3.x + vec3d3.z * vec3d3.z);
                double vec3d = e.horizontalLength();
                double i = vec3d3.length();
                float j = MathHelper.cos(f);
                j = (float)((double)j * ((double)j * Math.min(1.0, i / 0.4)));
                e = this.getVelocity().add(0.0, d * (-1.0 + (double)j * 0.75), 0.0);
                if (e.y < 0.0 && g > 0.0) {
                    k = e.y * -0.1 * (double)j;
                    e = e.add(vec3d3.x * k / g, k, vec3d3.z * k / g);
                }
                if (f < 0.0f && g > 0.0) {
                    k = vec3d * (double)(-MathHelper.sin(f)) * 0.04;
                    e = e.add(-vec3d3.x * k / g, k * 3.2, -vec3d3.z * k / g);
                }
                if (g > 0.0) {
                    e = e.add((vec3d3.x / g * vec3d - e.x) * 0.1, 0.0, (vec3d3.z / g * vec3d - e.z) * 0.1);
                }
                this.setVelocity(e.multiply(0.99f, 0.98f, 0.99f));
                this.move(MovementType.SELF, this.getVelocity());
                if (this.horizontalCollision && !this.world.isClient && (m = (float)((vec3d - this.getVelocity().horizontalLength()) * 10.0 - 3.0)) > 0.0f) {
                    this.playSound(this.getFallSound((int)m), 1.0f, 1.0f);
                    this.damage(DamageSource.FLY_INTO_WALL, m);
                }
                if (this.onGround && !this.world.isClient) {
                    this.setFlag(AccessorEntity.getFallFlyingFlagIndex(), false);
                }
            } else {
                BlockPos e = this.getVelocityAffectingPos();
                float vec3d3 = this.world.getBlockState(e).getBlock().getSlipperiness();
                float f = this.onGround ? vec3d3 * 0.91f : 0.91f;
                Vec3d g = this.applyMovementInput(movementInput, vec3d3);
                double h = g.y;
                if (this.hasStatusEffect(StatusEffects.LEVITATION)) {
                    h += (0.05 * (double)(this.getStatusEffect(StatusEffects.LEVITATION).getAmplifier() + 1) - g.y) * 0.2;
                    this.onLanding();
                } else if (!this.world.isClient || this.world.isChunkLoaded(e)) {
                    if (!this.hasNoGravity()) {
                        h -= d * MoonMod.gravityMultiplier;
                    }
                } else {
                    h = this.getY() > (double)this.world.getBottomY() ? -0.1 : 0.0;
                }

                if (this.hasNoDrag()) {
                    this.setVelocity(g.x, h, g.z);
                } else {
                    this.setVelocity(g.x * (double)f, h * (double)0.98f, g.z * (double)f);
                }
            }
        }
        this.updateLimbs((LivingEntity) (Object) this, this instanceof Flutterer);
    }
}
