package dev.isxander.moonmc.weapons.ranged

import dev.isxander.moonmc.utils.EntitySpawnPacket
import net.minecraft.entity.EntityType
import net.minecraft.network.Packet
import net.minecraft.particle.ParticleTypes
import net.minecraft.world.World

class ShotgunShellEntity(type: EntityType<out ShotgunShellEntity>, world: World) : ParticleBullet(type, world) {
    override val baseStrength = 20.0
    override val strengthModifier = 0.9

    override fun spawnParticles() {
        repeat(10) {
            val velocity = velocity.multiply(0.1)
            world.addParticle(ParticleTypes.CRIT, getParticleX(0.5), randomBodyY, getParticleZ(0.5), velocity.x, velocity.y, velocity.z)
        }
    }

    override fun createSpawnPacket(): Packet<*> = EntitySpawnPacket.create(this, EntitySpawnPacket.packetId)
}
