package dev.isxander.moonmc.packets.client

import dev.isxander.moonmc.oxygen.oxygen
import dev.isxander.moonmc.packets.OXYGEN_PACKET
import dev.isxander.moonmc.utils.EntitySpawnPacket
import dev.isxander.moonmc.utils.mc
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.entity.EntityType
import net.minecraft.util.registry.Registry

fun receiveOxygenPacket() {
    ClientPlayNetworking.registerGlobalReceiver(OXYGEN_PACKET) { client, handler, buf, sender ->
        val player = handler.world.getPlayerByUuid(buf.readUuid())
        val oxygen = buf.readInt()

        player?.oxygen = oxygen
    }
}

fun receiveEntityPacket() {
    ClientPlayNetworking.registerGlobalReceiver(EntitySpawnPacket.packetId) { client, handler, buf, response ->
        val et: EntityType<*> = Registry.ENTITY_TYPE.get(buf.readVarInt())
        val uuid = buf.readUuid()
        val entityId = buf.readVarInt()
        val pos = EntitySpawnPacket.PacketBufUtil.readVec3d(buf)
        val pitch = EntitySpawnPacket.PacketBufUtil.readAngle(buf)
        val yaw = EntitySpawnPacket.PacketBufUtil.readAngle(buf)
        client.execute {
            checkNotNull(mc.world) { "Tried to spawn entity in a null world!" }
            val e = et.create(mc.world)
                ?: error(
                    "Failed to create instance of entity \"" + Registry.ENTITY_TYPE.getId(
                        et
                    ).toString() + "\"!"
                )
            e.updateTrackedPosition(pos)
            e.setPos(pos.x, pos.y, pos.z)
            e.pitch = pitch
            e.yaw = yaw
            e.id = entityId
            e.uuid = uuid
            mc.world!!.addEntity(entityId, e)
        }
    }
}
