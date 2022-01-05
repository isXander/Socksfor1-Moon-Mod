package dev.isxander.moonmc.events

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.world.chunk.WorldChunk

fun interface ServerTickChunkCallback {
    fun onTick(world: ServerWorld, chunk: WorldChunk, randomTickSpeed: Int): ActionResult

    companion object {
        @JvmStatic
        val EVENT: Event<ServerTickChunkCallback> = EventFactory.createArrayBacked(ServerTickChunkCallback::class.java) { listeners ->
            ServerTickChunkCallback { world, chunk, randomTickSpeed ->
                for (listener in listeners) {
                    val result = listener.onTick(world, chunk, randomTickSpeed)

                    if (result != ActionResult.PASS) return@ServerTickChunkCallback result
                }

                return@ServerTickChunkCallback ActionResult.PASS
            }
        }
    }
}
