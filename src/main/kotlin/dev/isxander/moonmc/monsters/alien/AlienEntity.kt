package dev.isxander.moonmc.monsters.alien

import dev.isxander.moonmc.weapons.ranged.ParticleBullet
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.core.PlayState
import software.bernie.geckolib3.core.builder.AnimationBuilder
import software.bernie.geckolib3.core.controller.AnimationController
import software.bernie.geckolib3.core.manager.AnimationData
import software.bernie.geckolib3.core.manager.AnimationFactory
import kotlin.math.sqrt

class AlienEntity(type: EntityType<out AlienEntity>, world: World) : HostileEntity(type, world), IAnimatable {
    override fun initGoals() {
        goalSelector.add(1, AlienAttackGoal(this))
        goalSelector.add(2, WanderAroundFarGoal(this, 1.0))
        goalSelector.add(3, LookAtEntityGoal(this, PlayerEntity::class.java, 10f))
        goalSelector.add(3, LookAtEntityGoal(this, MobEntity::class.java, 8f))
        goalSelector.add(3, LookAroundGoal(this))

        targetSelector.add(1, RevengeGoal(this))
        targetSelector.add(2, ActiveTargetGoal(this, PlayerEntity::class.java, false))
        targetSelector.add(3, ActiveTargetGoal(this, MobEntity::class.java, true))
    }

    override fun damage(source: DamageSource, amount: Float): Boolean {
        if (isInvulnerableTo(source)) return false

        if (source is ParticleBullet.BulletDamageSource) {
            val sideways = if (random.nextInt(2) == 0) -2.0 else 2.0
            val velocity = movementInputToVelocity(Vec3d(sideways, 0.2, 0.0), 1f, yaw)
            addVelocity(velocity.x, velocity.y, velocity.z)
            return false
        }

        return super.damage(source, amount)
    }

    override fun getLootTableId() = Identifier("moonmc", "entities/alien")

    val attackingState = 0
    val jumpingState = 1
    var state: Int
        get() = dataTracker.get(STATE)
        set(value) = dataTracker.set(STATE, value)

    override fun initDataTracker() {
        super.initDataTracker()
        dataTracker.startTracking(STATE, -1)
    }

    val animations = AnimationFactory(this)

    override fun registerControllers(p0: AnimationData) {
        p0.addAnimationController(AnimationController(this, "passive", 20f) { event ->
            when {
                !isOnGround ->
                    event.controller.setAnimation(AnimationBuilder().addAnimation("jump"))
                event.isMoving ->
                    if (sqrt((x - prevX) * (x - prevX) + (z - prevZ) * (z - prevZ)) > 0.2)
                        event.controller.setAnimation(AnimationBuilder().addAnimation("run"))
                    else
                        event.controller.setAnimation(AnimationBuilder().addAnimation("walk"))
                else ->
                    event.controller.setAnimation(AnimationBuilder().addAnimation("idle"))
            }
            PlayState.CONTINUE
        })

        p0.addAnimationController(AnimationController(this, "action", 10f) { event ->
            when (state) {
                attackingState -> {
                    event.controller.setAnimation(AnimationBuilder().addAnimation("attack"))
                    PlayState.CONTINUE
                }
                jumpingState -> {
                    event.controller.setAnimation(AnimationBuilder().addAnimation("jump"))
                    PlayState.CONTINUE
                }
                else -> {
                    PlayState.STOP
                }
            }
        })
    }
    override fun getFactory() = animations

    class AlienAttackGoal(private val alien: AlienEntity) : MeleeAttackGoal(alien, 1.0, true) {
        override fun start() {
            super.start()
            alien.state = -1
        }

        override fun stop() {
            super.stop()
            alien.state = -1
        }

        override fun attack(target: LivingEntity, squaredDistance: Double) {
            val d = getSquaredMaxAttackDistance(target)
            if (squaredDistance <= d && isCooledDown) {
                resetCooldown()
                alien.swingHand(Hand.MAIN_HAND)
                alien.tryAttack(target)
                alien.state = alien.attackingState
            } else if (cooldown == 1) {
                alien.state = -1
            }
        }
    }

    companion object {
        val STATE = DataTracker.registerData(AlienEntity::class.java, TrackedDataHandlerRegistry.INTEGER)

        fun createAlienAttributes(): DefaultAttributeContainer.Builder =
            createHostileAttributes().apply {
                add(EntityAttributes.GENERIC_FOLLOW_RANGE, 15.0)
                add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5)
                add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10.0)
                add(EntityAttributes.GENERIC_ARMOR, 5.0)
                add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0)
            }

        private fun movementInputToVelocity(movementInput: Vec3d, speed: Float, yaw: Float): Vec3d {
            val d = movementInput.lengthSquared()
            if (d < 1.0E-7) {
                return Vec3d.ZERO
            }
            val vec3d = (if (d > 1.0) movementInput.normalize() else movementInput).multiply(speed.toDouble())
            val f = MathHelper.sin(yaw * (Math.PI.toFloat() / 180))
            val g = MathHelper.cos(yaw * (Math.PI.toFloat() / 180))
            return Vec3d(
                vec3d.x * g.toDouble() - vec3d.z * f.toDouble(),
                vec3d.y,
                vec3d.z * g.toDouble() + vec3d.x * f.toDouble()
            )
        }
    }
}
