package dev.isxander.moonmc.client

import com.mojang.brigadier.arguments.IntegerArgumentType
import dev.isxander.moonmc.monsters.render.MoonManEntityRenderer
import dev.isxander.moonmc.oxygen.MAX_OXYGEN
import dev.isxander.moonmc.oxygen.OxygenMaskRenderer
import dev.isxander.moonmc.oxygen.oxygen
import dev.isxander.moonmc.registry.MoonRegistry
import dev.isxander.moonmc.utils.EntitySpawnPacket
import dev.isxander.moonmc.utils.EntitySpawnPacket.PacketBufUtil.readAngle
import dev.isxander.moonmc.utils.EntitySpawnPacket.PacketBufUtil.readVec3d
import dev.isxander.moonmc.utils.mc
import dev.isxander.moonmc.weapons.ranged.ParticleBullet
import io.ejekta.kambrik.Kambrik
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.FlyingItemEntityRenderer
import net.minecraft.entity.EntityType
import net.minecraft.util.registry.Registry

object MoonClientMod : ClientModInitializer {
    override fun onInitializeClient() {
        EntityRendererRegistry.register(MoonRegistry.SHOTGUN_SHELL, ParticleBullet::EmptyRenderer)
        EntityRendererRegistry.register(MoonRegistry.MOON_ROCK_ENTITY, ::FlyingItemEntityRenderer)
        EntityRendererRegistry.register(MoonRegistry.MOON_MAN_ENTITY, ::MoonManEntityRenderer)

        receiveEntityPacket()

        ArmorRenderer.register(OxygenMaskRenderer(), MoonRegistry.OXYGEN_MASK)

        Kambrik.Command.addClientCommand("moonmc") {
            "set_oxygen" {
                argInt("level", 0..MAX_OXYGEN) {
                    executes {
                        mc.player!!.oxygen = argFrom(arg)
                    }
                }
            }
        }
    }

    fun receiveEntityPacket() {
        ClientPlayNetworking.registerGlobalReceiver(EntitySpawnPacket.packetId) { client, handler, buf, response ->
            val et: EntityType<*> = Registry.ENTITY_TYPE.get(buf.readVarInt())
            val uuid = buf.readUuid()
            val entityId = buf.readVarInt()
            val pos = readVec3d(buf)
            val pitch = readAngle(buf)
            val yaw = readAngle(buf)
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
}
