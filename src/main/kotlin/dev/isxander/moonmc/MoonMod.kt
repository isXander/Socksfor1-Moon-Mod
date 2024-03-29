package dev.isxander.moonmc

import dev.isxander.moonmc.disasters.asteroid.AsteroidEntity
import dev.isxander.moonmc.events.ServerTickChunkCallback
import dev.isxander.moonmc.oxygen.MAX_OXYGEN
import dev.isxander.moonmc.oxygen.oxygen
import dev.isxander.moonmc.packets.server.sendOxygenPacket
import dev.isxander.moonmc.registry.MoonRegistry
import io.ejekta.kambrik.command.addCommand
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.util.ActionResult
import software.bernie.example.GeckoLibMod
import software.bernie.geckolib3.GeckoLib

object MoonMod : ModInitializer {
    const val gravityMultiplier = 0.25

    override fun onInitialize() {
        GeckoLibMod.DISABLE_IN_DEV = true
        GeckoLib.initialize()

        registerCommands()

        ServerTickChunkCallback.EVENT.register { world, chunk, _ ->
            AsteroidEntity.tryNaturalSpawn(world, chunk)
            return@register ActionResult.PASS
        }
    }

    private fun registerCommands() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.addCommand("moonmc") {
                "set_oxygen" {
                    argInt("level", 0..MAX_OXYGEN) { oxygen ->
                        runs {
                            source.player?.oxygen = oxygen()

                            for (player in source.world?.players ?: emptyList())
                                sendOxygenPacket(player, source.player, source.player.oxygen)
                        }
                    }
                }

                "asteroid" {
                    runs {
                        val world = source.world
                        val player = source.player ?: return@runs

                        val asteroid = MoonRegistry.ASTEROID_ENTITY.create(world) ?: return@runs
                        asteroid.owner = player
                        asteroid.refreshPositionAndAngles(player.x, player.y, player.z, player.yaw, player.pitch)
                        asteroid.velocity = player.rotationVector
                        asteroid.powerX = asteroid.velocity.x * 0.1
                        asteroid.powerY = asteroid.velocity.y * 0.1
                        asteroid.powerZ = asteroid.velocity.z * 0.1
                        world.spawnEntity(asteroid)
                    }
                }
            }
        }
    }
}
