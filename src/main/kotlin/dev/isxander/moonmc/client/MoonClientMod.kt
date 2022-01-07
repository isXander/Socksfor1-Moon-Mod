package dev.isxander.moonmc.client

import dev.isxander.moonmc.disasters.asteroid.render.AsteroidRenderer
import dev.isxander.moonmc.monsters.render.MoonManEntityRenderer
import dev.isxander.moonmc.oxygen.MAX_OXYGEN
import dev.isxander.moonmc.oxygen.oxygen
import dev.isxander.moonmc.oxygen.render.OxygenMaskRenderer
import dev.isxander.moonmc.registry.MoonRegistry
import dev.isxander.moonmc.transport.rocket.render.RocketEntityRenderer
import dev.isxander.moonmc.utils.EntitySpawnPacket
import dev.isxander.moonmc.utils.EntitySpawnPacket.PacketBufUtil.readAngle
import dev.isxander.moonmc.utils.EntitySpawnPacket.PacketBufUtil.readVec3d
import dev.isxander.moonmc.utils.mc
import dev.isxander.moonmc.weapons.ranged.ParticleBullet
import io.ejekta.kambrik.Kambrik
import io.ejekta.kambrik.command.addCommand
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.`object`.builder.v1.client.model.FabricModelPredicateProviderRegistry
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback
import net.minecraft.client.particle.DamageParticle
import net.minecraft.client.render.entity.FlyingItemEntityRenderer
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer

object MoonClientMod : ClientModInitializer {
    override fun onInitializeClient() {
        EntityRendererRegistry.register(MoonRegistry.SHOTGUN_SHELL, ParticleBullet::EmptyRenderer)
        EntityRendererRegistry.register(MoonRegistry.PISTOL_BULLET, ParticleBullet::EmptyRenderer)
        EntityRendererRegistry.register(MoonRegistry.RIFLE_BULLET, ParticleBullet::EmptyRenderer)
        EntityRendererRegistry.register(MoonRegistry.SNIPER_BULLET, ParticleBullet::EmptyRenderer)
        EntityRendererRegistry.register(MoonRegistry.BAZOOKA_ROCKET, ParticleBullet::EmptyRenderer)
        EntityRendererRegistry.register(MoonRegistry.MOON_ROCK_ENTITY, ::FlyingItemEntityRenderer)
        EntityRendererRegistry.register(MoonRegistry.MOON_MAN_ENTITY, ::MoonManEntityRenderer)
        EntityRendererRegistry.register(MoonRegistry.ASTEROID_ENTITY, ::AsteroidRenderer)
        EntityRendererRegistry.register(MoonRegistry.ROCKET_ENTITY, ::RocketEntityRenderer)

        GeoArmorRenderer.registerArmorRenderer<Entity>(OxygenMaskRenderer(), MoonRegistry.OXYGEN_MASK)

        receiveEntityPacket()
        registerParticles()
    }

    private fun registerParticles() {
        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register { atlasTexture, registry ->
            registry.register(Identifier("moonmc", "particle/laser_particle"))
        }
        ParticleFactoryRegistry.getInstance().register(MoonRegistry.LASER_PARTICLE, DamageParticle::Factory)
    }

    private fun receiveEntityPacket() {
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
