package dev.isxander.moonmc.monsters

import dev.isxander.moonmc.registry.MoonRegistry
import dev.isxander.moonmc.weapons.ranged.MoonRockEntity
import dev.isxander.moonmc.weapons.ranged.MoonRockItem
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.RangedAttackMob
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.entity.projectile.ProjectileUtil
import net.minecraft.item.BowItem
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundEvents
import net.minecraft.world.LocalDifficulty
import net.minecraft.world.World
import java.util.*
import kotlin.math.sqrt

class MoonManEntity(type: EntityType<out MoonManEntity>, world: World) : HostileEntity(type, world), RangedAttackMob {
    override fun initGoals() {
        this.goalSelector.add(1, WanderAroundFarGoal(this, 1.0))
        this.goalSelector.add(2, LookAtEntityGoal(this, PlayerEntity::class.java, 10f))
        this.goalSelector.add(3, LookAroundGoal(this))
        this.targetSelector.add(1, RevengeGoal(this))
        this.targetSelector.add(2, ActiveTargetGoal(this, PlayerEntity::class.java, true))
        this.targetSelector.add(3, MoonRockAttackGoal(this, 1.0, 20, 15f))
    }

    override fun attack(target: LivingEntity, pullProgress: Float) {
        val itemStack = getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, MoonRegistry.MOON_ROCK_ITEM))
        val entity = this.createProjectile(itemStack)
        val d = target.x - this.x
        val e = target.getBodyY(0.3333333333333333) - entity.y
        val f = target.z - this.z
        val g = sqrt(d * d + f * f)
        entity.setVelocity(d, e + g * 0.2, f, 1.6f, (14 - world.difficulty.id * 4).toFloat())
        playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0f, 1.0f / (getRandom().nextFloat() * 0.4f + 0.8f))
        world.spawnEntity(entity)

    }

    private fun createProjectile(stack: ItemStack): MoonRockEntity {
        return (stack.item as? MoonRockItem ?: MoonRegistry.MOON_ROCK_ITEM).createEntity(this.world, stack, this)
    }

    val holdingMoonRock: Boolean
        get() = this.isHolding(MoonRegistry.MOON_ROCK_ITEM)

    class MoonRockAttackGoal(private val actor: MoonManEntity, private val speed: Double, var attackInterval: Int, private val range: Float) : Goal() {
        val squaredRange = range * range
        private var cooldown = -1
        private var targetSeeingTicker = 0
        private var movingToLeft = false
        private var backward = false
        private var combatTicks = -1

        init {
            controls = EnumSet.of(Control.MOVE, Control.LOOK)
        }

        override fun canStart(): Boolean = actor.target != null && actor.holdingMoonRock
        override fun shouldContinue(): Boolean = (canStart() || !actor.navigation.isIdle) && actor.holdingMoonRock

        override fun start() {
            super.start()
            actor.isAttacking = true
        }

        override fun stop() {
            super.stop()
            actor.isAttacking = false
            targetSeeingTicker = 0
            cooldown = -1
            actor.clearActiveItem()
        }

        override fun shouldRunEveryTick(): Boolean = true

        override fun tick() {
            val bl2 = targetSeeingTicker > 0
            val livingEntity = actor.target ?: return
            val d = actor.squaredDistanceTo(livingEntity.x, livingEntity.y, livingEntity.z)
            val bl = actor.visibilityCache.canSee(livingEntity)
            if (bl != bl2) {
                targetSeeingTicker = 0
            }
            targetSeeingTicker = if (bl) ++targetSeeingTicker else --targetSeeingTicker
            if (d > squaredRange.toDouble() || targetSeeingTicker < 20) {
                actor.navigation.startMovingTo(livingEntity, speed)
                combatTicks = -1
            } else {
                actor.navigation.stop()
                ++combatTicks
            }
            if (combatTicks >= 20) {
                if (actor.random.nextFloat().toDouble() < 0.3) {
                    movingToLeft = !movingToLeft
                }
                if (actor.random.nextFloat().toDouble() < 0.3) {
                    backward = !backward
                }
                combatTicks = 0
            }
            if (combatTicks > -1) {
                if (d > (squaredRange * 0.75f).toDouble()) {
                    backward = false
                } else if (d < (squaredRange * 0.25f).toDouble()) {
                    backward = true
                }
                actor.moveControl.strafeTo(
                    if (backward) -0.5f else 0.5f,
                    if (movingToLeft) 0.5f else -0.5f
                )
                actor.lookAtEntity(livingEntity, 30.0f, 30.0f)
            } else {
                actor.lookControl.lookAt(livingEntity, 30.0f, 30.0f)
            }
            if (actor.isUsingItem) {
                if (!bl && targetSeeingTicker < -60) {
                    actor.clearActiveItem()
                } else if (bl && actor.itemUseTime >= 20) {
                    val i = actor.itemUseTime
                    actor.clearActiveItem()
                    actor.attack(livingEntity, BowItem.getPullProgress(i))
                    cooldown = attackInterval
                }
            } else if (--cooldown <= 0 && targetSeeingTicker >= -60) {
                actor.setCurrentHand(ProjectileUtil.getHandPossiblyHolding(actor, MoonRegistry.MOON_ROCK_ITEM))
            }
        }
    }

    override fun initEquipment(difficulty: LocalDifficulty?) {
        super.initEquipment(difficulty)
        equipStack(EquipmentSlot.MAINHAND, ItemStack(MoonRegistry.MOON_ROCK_ITEM, 64))
    }

    companion object {
        fun createLivingAttributes(): DefaultAttributeContainer.Builder {
            return createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
        }
    }
}
