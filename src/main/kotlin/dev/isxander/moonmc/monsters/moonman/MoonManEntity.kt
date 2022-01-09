package dev.isxander.moonmc.monsters.moonman

import dev.isxander.moonmc.registry.MoonRegistry
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.RangedAttackMob
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.sound.SoundEvents
import net.minecraft.world.World
import kotlin.math.sqrt

class MoonManEntity(type: EntityType<out MoonManEntity>, world: World) : HostileEntity(type, world), RangedAttackMob {
    override fun initGoals() {
        this.goalSelector.add(1, ProjectileAttackGoal(this, 1.0, 20, 15f))
        this.goalSelector.add(2, WanderAroundFarGoal(this, 1.0))
        this.goalSelector.add(3, LookAtEntityGoal(this, PlayerEntity::class.java, 10f))
        this.goalSelector.add(4, LookAroundGoal(this))
        this.targetSelector.add(1, RevengeGoal(this))
        this.targetSelector.add(2, ActiveTargetGoal(this, PlayerEntity::class.java, true))
    }

    override fun attack(target: LivingEntity, pullProgress: Float) {
        val rock = MoonRegistry.MOON_ROCK_ENTITY.create(world) ?: return
        rock.owner = this
        rock.setPosition(x, eyeY - 0.10000000149011612, z)
        val targetY = target.eyeY - 1.100000023841858
        val xDiff = target.x - x
        val yDiff = targetY - rock.y
        val zDiff = target.z - z
        val aim = sqrt(xDiff * xDiff + zDiff * zDiff) * 0.15000000298023225
        rock.setVelocity(xDiff, yDiff + aim, zDiff, 1.6f, 6.0f)
        playSound(SoundEvents.ENTITY_SNOW_GOLEM_SHOOT, 1.0f, 0.4f / (getRandom().nextFloat() * 0.4f + 0.8f))
        world.spawnEntity(rock)
    }

    companion object {
        fun createLivingAttributes(): DefaultAttributeContainer.Builder {
            return createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
        }
    }
}
