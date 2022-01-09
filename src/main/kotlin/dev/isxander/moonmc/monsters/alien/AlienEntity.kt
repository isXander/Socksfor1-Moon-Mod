package dev.isxander.moonmc.monsters.alien

import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.world.World
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.core.PlayState
import software.bernie.geckolib3.core.builder.AnimationBuilder
import software.bernie.geckolib3.core.controller.AnimationController
import software.bernie.geckolib3.core.manager.AnimationData
import software.bernie.geckolib3.core.manager.AnimationFactory

class AlienEntity(type: EntityType<out AlienEntity>, world: World) : HostileEntity(type, world), IAnimatable {
    override fun initGoals() {
        goalSelector.add(2, WanderAroundFarGoal(this, 1.0))
        goalSelector.add(3, LookAtEntityGoal(this, PlayerEntity::class.java, 10f))
        goalSelector.add(3, LookAtEntityGoal(this, MobEntity::class.java, 8f))
        goalSelector.add(3, LookAroundGoal(this))

        targetSelector.add(1, RevengeGoal(this))
        targetSelector.add(2, ActiveTargetGoal(this, PlayerEntity::class.java, false))
        targetSelector.add(3, ActiveTargetGoal(this, MobEntity::class.java, true))
    }

    init {
        headYaw = 90f
    }

    private val attackingState = 0
    private val jumpingState = 1
    private var state: Int
        get() = dataTracker.get(STATE)
        set(value) = dataTracker.set(STATE, value)

    override fun initDataTracker() {
        super.initDataTracker()
        dataTracker.startTracking(STATE, 0)
    }

    val animations = AnimationFactory(this)

    override fun registerControllers(p0: AnimationData) {
        p0.addAnimationController(AnimationController(this, "passive", 20f) { event ->
            when {
                event.isMoving ->
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
                else -> PlayState.STOP
            }
        })
    }
    override fun getFactory() = animations

    companion object {
        val STATE = DataTracker.registerData(AlienEntity::class.java, TrackedDataHandlerRegistry.INTEGER)

        fun createAlienAttributes(): DefaultAttributeContainer.Builder =
            createHostileAttributes().apply {
                add(EntityAttributes.GENERIC_FOLLOW_RANGE, 15.0)
                add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5)
                add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0)
                add(EntityAttributes.GENERIC_ARMOR, 5.0)
                add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0)
            }
    }
}
