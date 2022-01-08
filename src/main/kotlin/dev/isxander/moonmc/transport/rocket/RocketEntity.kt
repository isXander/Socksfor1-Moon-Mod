package dev.isxander.moonmc.transport.rocket

import dev.isxander.moonmc.registry.MoonRegistry
import dev.isxander.moonmc.utils.EntitySpawnPacket
import dev.isxander.moonmc.utils.getRandomPointInRadius
import net.minecraft.entity.*
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.Packet
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.World
import net.minecraft.world.explosion.Explosion
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.core.PlayState
import software.bernie.geckolib3.core.builder.AnimationBuilder
import software.bernie.geckolib3.core.controller.AnimationController
import software.bernie.geckolib3.core.manager.AnimationData
import software.bernie.geckolib3.core.manager.AnimationFactory

class RocketEntity(type: EntityType<out RocketEntity>, world: World) : Entity(type, world), IAnimatable {
    var accelerating = false
    private var durability = 20f

    init {
        inanimate = true
        setNoGravity(false)
    }

    override fun tick() {
        super.tick()
        if (isLogicalSideForUpdatingMovement) {
            refreshPosition()
            setRotation(yaw, pitch)
            if (accelerating) {
                addVelocity(0.0, 0.15, 0.0)
            }

            addVelocity(0.0, -0.1, 0.0)

            move(MovementType.SELF, velocity)
        }

        if (accelerating) {
            repeat(50) {
                val pos = getRandomPointInRadius(x.toFloat(), z.toFloat(), 0.75f, random)
                world.addParticle(ParticleTypes.FLAME, pos.x.toDouble(), y, pos.y.toDouble(), -0.4 + random.nextDouble() * 0.8, -2.0, -0.4 + random.nextDouble() * 0.8)
            }
            repeat(20) {
                val pos = getRandomPointInRadius(x.toFloat(), z.toFloat(), 0.75f, random)
                world.addParticle(ParticleTypes.SMOKE, pos.x.toDouble(), y, pos.y.toDouble(), -0.4 + random.nextDouble() * 0.8, -2.0, -0.4 + random.nextDouble() * 0.8)
            }

            if (world.isClient) {
                if (world.time % 30L == 0L) {
                    world.playSound(x, y, z, MoonRegistry.ASTEROID_AMBIENT_SOUND, SoundCategory.PLAYERS, 10f, 1f, false)
                }
            }
        }
    }

    override fun interact(player: PlayerEntity, hand: Hand): ActionResult {
        if (hasPassengers() || player.shouldCancelInteraction()) return ActionResult.PASS

        return if (!world.isClient) {
            if (player.startRiding(this)) ActionResult.CONSUME else ActionResult.PASS
        } else ActionResult.SUCCESS
    }

    override fun getPrimaryPassenger() = passengerList.firstOrNull()
    override fun canAddPassenger(passenger: Entity) = passengerList.size <= 0
    override fun getMountedHeightOffset() = 5.0

    override fun damage(source: DamageSource, amount: Float): Boolean {
        if (isInvulnerableTo(source)) return false
        if (world.isClient || isRemoved) return true
        durability -= amount
        if (durability <= 0f) {
            kill()
            world.createExplosion(this, x, y, z, 5f, false, Explosion.DestructionType.NONE)
            world.createExplosion(this, x, y + height / 2f, z, 5f, false, Explosion.DestructionType.NONE)
            world.createExplosion(this, x, y + height, z, 5f, false, Explosion.DestructionType.NONE)
        }
        return true
    }
    override fun handleFallDamage(fallDistance: Float, damageMultiplier: Float, damageSource: DamageSource) = false

    override fun collidesWith(other: Entity) = canCollide(this, other)
    override fun isPushable() = false
    override fun isCollidable() = true
    override fun collides() = !isRemoved

    val animations = AnimationFactory(this)

    override fun registerControllers(p0: AnimationData) {
        p0.addAnimationController(AnimationController(this, "controller", 20f) {
            it.controller.setAnimation(AnimationBuilder().addAnimation("idle", true))
            PlayState.CONTINUE
        })
    }
    override fun getFactory() = animations

    override fun initDataTracker() {}
    override fun readCustomDataFromNbt(nbt: NbtCompound) {}
    override fun writeCustomDataToNbt(nbt: NbtCompound) {}
    override fun createSpawnPacket(): Packet<*> = EntitySpawnPacket.create(this, EntitySpawnPacket.packetId)

    companion object {
        @JvmStatic
        fun canCollide(entity: Entity, other: Entity): Boolean {
            return (other.isCollidable || other.isPushable) && !entity.isConnectedThroughVehicle(other)
        }
    }
}
