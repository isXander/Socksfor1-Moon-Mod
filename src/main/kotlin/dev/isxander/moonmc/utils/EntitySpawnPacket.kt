package dev.isxander.moonmc.utils

import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.Entity
import net.minecraft.network.Packet
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.util.registry.Registry

object EntitySpawnPacket {
    val packetId = Identifier("moonmc", "spawn_packet")

    fun create(e: Entity, packetID: Identifier?): Packet<*> {
        check(!e.world.isClient) { "SpawnPacketUtil.create called on the logical client!" }
        val byteBuf = PacketByteBuf(Unpooled.buffer())
        byteBuf.writeVarInt(Registry.ENTITY_TYPE.getRawId(e.type))
        byteBuf.writeUuid(e.uuid)
        byteBuf.writeVarInt(e.id)
        PacketBufUtil.writeVec3d(byteBuf, e.pos)
        PacketBufUtil.writeAngle(byteBuf, e.pitch)
        PacketBufUtil.writeAngle(byteBuf, e.yaw)
        return ServerPlayNetworking.createS2CPacket(packetID, byteBuf)
    }

    object PacketBufUtil {
        /**
         * Packs a floating-point angle into a `byte`.
         *
         * @param angle
         * angle
         * @return packed angle
         */
        fun packAngle(angle: Float): Byte {
            return MathHelper.floor(angle * 256 / 360).toByte()
        }

        /**
         * Unpacks a floating-point angle from a `byte`.
         *
         * @param angleByte
         * packed angle
         * @return angle
         */
        fun unpackAngle(angleByte: Byte): Float {
            return angleByte * 360 / 256f
        }

        /**
         * Writes an angle to a [PacketByteBuf].
         *
         * @param byteBuf
         * destination buffer
         * @param angle
         * angle
         */
        fun writeAngle(byteBuf: PacketByteBuf, angle: Float) {
            byteBuf.writeByte(packAngle(angle).toInt())
        }

        /**
         * Reads an angle from a [PacketByteBuf].
         *
         * @param byteBuf
         * source buffer
         * @return angle
         */
        fun readAngle(byteBuf: PacketByteBuf): Float {
            return unpackAngle(byteBuf.readByte())
        }

        /**
         * Writes a [Vec3d] to a [PacketByteBuf].
         *
         * @param byteBuf
         * destination buffer
         * @param vec3d
         * vector
         */
        fun writeVec3d(byteBuf: PacketByteBuf, vec3d: Vec3d) {
            byteBuf.writeDouble(vec3d.x)
            byteBuf.writeDouble(vec3d.y)
            byteBuf.writeDouble(vec3d.z)
        }

        /**
         * Reads a [Vec3d] from a [PacketByteBuf].
         *
         * @param byteBuf
         * source buffer
         * @return vector
         */
        fun readVec3d(byteBuf: PacketByteBuf): Vec3d {
            val x = byteBuf.readDouble()
            val y = byteBuf.readDouble()
            val z = byteBuf.readDouble()
            return Vec3d(x, y, z)
        }
    }
}
