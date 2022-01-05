package dev.isxander.moonmc.weapons.ranged.pistol

import dev.isxander.moonmc.registry.MoonRegistry
import dev.isxander.moonmc.utils.EntitySpawnPacket
import dev.isxander.moonmc.weapons.ranged.ParticleBullet
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.network.Packet
import net.minecraft.world.World

class PistolBulletEntity(type: EntityType<out PistolBulletEntity>, world: World) : ParticleBullet(type, world) {
    override val strengthModifier = 0.9

    init {
        damage = 2.0
    }

    override fun spawnParticles() {
        repeat(3) {
            val velocity = velocity.multiply(0.5)
            world.addParticle(MoonRegistry.LASER_PARTICLE, getParticleX(0.5), randomBodyY, getParticleZ(0.5), velocity.x, velocity.y, velocity.z)
        }
    }

    override fun createSpawnPacket(): Packet<*> = EntitySpawnPacket.create(this, EntitySpawnPacket.packetId)

    companion object {
        fun createAndSpawn(world: World, entity: LivingEntity, speedMultiplier: Float, playSound: Boolean = true): Boolean {
            val shell = MoonRegistry.PISTOL_BULLET.create(world) ?: return false
            shell.setPosition(entity.x, entity.eyeY, entity.z)
            shell.owner = entity
            shell.setVelocity(entity, entity.pitch, entity.yaw, 0.0f, 5f * speedMultiplier, 1f)
            return world.spawnEntity(shell).also {
                if (it && playSound) {
                    playBulletSound(world, entity.x, entity.eyeY, entity.z, pitch = 1.5f)
                }
            }
        }
    }
}
