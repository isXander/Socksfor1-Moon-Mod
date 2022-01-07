package dev.isxander.moonmc.transport.rocket

import dev.isxander.moonmc.utils.EntitySpawnPacket
import net.minecraft.entity.*
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.Packet
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.World
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.core.PlayState
import software.bernie.geckolib3.core.builder.AnimationBuilder
import software.bernie.geckolib3.core.controller.AnimationController
import software.bernie.geckolib3.core.manager.AnimationData
import software.bernie.geckolib3.core.manager.AnimationFactory

class RocketEntity(type: EntityType<out RocketEntity>, world: World) : Entity(type, world), IAnimatable {
    init {
        inanimate = true
        setNoGravity(false)
    }

    override fun tick() {
        super.tick()
        if (world.isClient) {
            refreshPosition()
            setRotation(yaw, pitch)
            return
        }
        addVelocity(0.0, -0.1, 0.0)
        move(MovementType.SELF, velocity)
    }

    override fun interact(player: PlayerEntity, hand: Hand): ActionResult {
        if (hasPassengers() || player.shouldCancelInteraction()) return ActionResult.PASS

        return if (!world.isClient) {
            if (player.startRiding(this)) ActionResult.CONSUME else ActionResult.PASS
        } else ActionResult.SUCCESS
    }

    override fun getPrimaryPassenger() = passengerList.firstOrNull()
    override fun canAddPassenger(passenger: Entity) = passengerList.size <= 0
    override fun getMountedHeightOffset() = 2.0

    override fun damage(source: DamageSource, amount: Float): Boolean {
        if (isInvulnerableTo(source)) return false
        if (world.isClient || isRemoved) return true
        if (amount > 0f) {
            kill()
            return true
        }
        return false
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
