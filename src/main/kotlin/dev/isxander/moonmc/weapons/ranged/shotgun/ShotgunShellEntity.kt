package dev.isxander.moonmc.weapons.ranged.shotgun

import dev.isxander.moonmc.registry.MoonRegistry
import dev.isxander.moonmc.utils.EntitySpawnPacket
import dev.isxander.moonmc.weapons.ranged.ParticleBullet
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.network.Packet
import net.minecraft.particle.ParticleTypes
import net.minecraft.world.World

class ShotgunShellEntity(type: EntityType<out ShotgunShellEntity>, world: World) : ParticleBullet(type, world) {
    override val strengthModifier = 0.7

    init {
        // needs to be low because there are 10 bullets per shot
        damage = 2.0
    }

    override fun spawnParticles() {
        val velocity = velocity.multiply(0.5)
        repeat(4) {
            world.addParticle(MoonRegistry.LASER_PARTICLE, getParticleX(0.5), randomBodyY, getParticleZ(0.5), velocity.x, velocity.y, velocity.z)
        }
    }

    override fun createSpawnPacket(): Packet<*> = EntitySpawnPacket.create(this)

    companion object {
        fun createAndSpawn(world: World, entity: LivingEntity, speedMultiplier: Float, aiming: Boolean, playSound: Boolean = true): Boolean {
            var spawned = false
            repeat(10) {
                val shell = MoonRegistry.SHOTGUN_SHELL.create(world) ?: return false
                shell.setPosition(entity.x, entity.eyeY, entity.z)
                shell.owner = entity
                shell.setVelocity(entity, entity.pitch, entity.yaw, 0.0f, 5f * speedMultiplier, if (aiming) 10f else 20f)

                if (world.spawnEntity(shell)) {
                    if (playSound) {
                        playBulletSound(world, entity.x, entity.eyeY, entity.z)
                    }
                    spawned = true
                }
            }
            return spawned
        }
    }
}
