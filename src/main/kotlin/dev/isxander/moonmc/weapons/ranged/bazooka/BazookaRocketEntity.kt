package dev.isxander.moonmc.weapons.ranged.bazooka

import dev.isxander.moonmc.registry.MoonRegistry
import dev.isxander.moonmc.utils.EntitySpawnPacket
import dev.isxander.moonmc.weapons.ranged.ParticleBullet
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.network.Packet
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.world.GameRules
import net.minecraft.world.World
import net.minecraft.world.explosion.Explosion

class BazookaRocketEntity(type: EntityType<out BazookaRocketEntity>, world: World) : ParticleBullet(type, world) {
    override val strengthModifier = 1.0

    init {
        damage = 25.0
    }

    override fun spawnParticles() {
        repeat(50) {
            val velocity = velocity.multiply(0.1)
            world.addParticle(MoonRegistry.LASER_PARTICLE, getParticleX(1.0), randomBodyY, getParticleZ(1.0), velocity.x, velocity.y, velocity.z)
        }
    }

    override fun onCollision(hitResult: HitResult) {
        super.onCollision(hitResult)

        if (hitResult.type == HitResult.Type.MISS || (hitResult as? EntityHitResult)?.entity == owner) return

        val grief = world.gameRules.getBoolean(GameRules.DO_MOB_GRIEFING)
        world.createExplosion(this, x, y, z, 2f, grief, if (grief) Explosion.DestructionType.DESTROY else Explosion.DestructionType.NONE)
    }

    override fun createSpawnPacket(): Packet<*> = EntitySpawnPacket.create(this)

    companion object {
        fun createAndSpawn(world: World, entity: LivingEntity, speedMultiplier: Float, aiming: Boolean, playSound: Boolean = true): Boolean {
            val shell = MoonRegistry.BAZOOKA_ROCKET.create(world) ?: return false
            shell.setPosition(entity.x, entity.eyeY, entity.z)
            shell.owner = entity
            shell.setVelocity(entity, entity.pitch, entity.yaw, 0.0f, 3f * speedMultiplier, if (aiming) 1f else 6f)
            return world.spawnEntity(shell).also {
                if (it && playSound) {
                    playBulletSound(world, entity.x, entity.eyeY, entity.z, 2f, 0.5f, 0.2f)
                }
            }
        }
    }
}
