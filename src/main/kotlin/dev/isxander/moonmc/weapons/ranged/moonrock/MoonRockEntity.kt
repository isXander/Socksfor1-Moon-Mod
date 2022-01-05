package dev.isxander.moonmc.weapons.ranged.moonrock

import dev.isxander.moonmc.registry.MoonRegistry
import dev.isxander.moonmc.utils.EntitySpawnPacket
import net.minecraft.entity.EntityType
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.network.Packet
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.world.World

class MoonRockEntity(type: EntityType<out MoonRockEntity>, world: World) : ThrownItemEntity(type, world) {
    override fun getDefaultItem(): Item = MoonRegistry.MOON_ROCK_ITEM

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        super.onEntityHit(entityHitResult)
        val entity = entityHitResult.entity
        entity.damage(DamageSource.thrownProjectile(this, owner), 10f)
    }

    override fun onCollision(hitResult: HitResult) {
        super.onCollision(hitResult)
        if (!world.isClient) {
            world.sendEntityStatus(this, 3.toByte())
            discard()
        }
    }

    override fun createSpawnPacket(): Packet<*> = EntitySpawnPacket.create(this, EntitySpawnPacket.packetId)
}
