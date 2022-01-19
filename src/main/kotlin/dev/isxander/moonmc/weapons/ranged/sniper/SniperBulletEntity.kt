package dev.isxander.moonmc.weapons.ranged.sniper

import dev.isxander.moonmc.registry.MoonRegistry
import dev.isxander.moonmc.utils.EntitySpawnPacket
import dev.isxander.moonmc.weapons.ranged.ParticleBullet
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.network.Packet
import net.minecraft.world.World

class SniperBulletEntity(type: EntityType<out SniperBulletEntity>, world: World) : ParticleBullet(type, world) {
    override val strengthModifier = 0.99

    init {
        damage = 17.0
    }

    override fun spawnParticles() {
        repeat(10) {
            val velocity = velocity.multiply(0.5)
            world.addParticle(MoonRegistry.LASER_PARTICLE, getParticleX(0.5), randomBodyY, getParticleZ(0.5), velocity.x, velocity.y, velocity.z)
        }
    }

    override fun createSpawnPacket(): Packet<*> = EntitySpawnPacket.create(this)

    companion object {
        fun createAndSpawn(world: World, entity: LivingEntity, speedMultiplier: Float, aiming: Boolean, playSound: Boolean = true): Boolean {
            val bullet = MoonRegistry.SNIPER_BULLET.create(world) ?: return false
            bullet.setPosition(entity.x, entity.eyeY, entity.z)
            bullet.owner = entity
            bullet.setVelocity(entity, entity.pitch, entity.yaw, 0.0f, 10f * speedMultiplier, if (aiming) .1f else 10f)
            return world.spawnEntity(bullet).also {
                if (it && playSound) {
                    playBulletSound(world, entity.x, entity.eyeY, entity.z, 5f)
                }
            }
        }
    }
}
