package dev.isxander.moonmc.packets.server

import dev.isxander.moonmc.packets.OXYGEN_PACKET
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity

fun sendOxygenPacket(receiver: ServerPlayerEntity, player: PlayerEntity, oxygen: Int) {
    val pbb = PacketByteBufs.create()
    pbb.writeUuid(player.uuid)
    pbb.writeInt(oxygen)

    ServerPlayNetworking.send(receiver, OXYGEN_PACKET, pbb)
}
