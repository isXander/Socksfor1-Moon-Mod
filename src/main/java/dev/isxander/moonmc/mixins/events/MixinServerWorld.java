package dev.isxander.moonmc.mixins.events;

import dev.isxander.moonmc.events.ServerTickChunkCallback;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class MixinServerWorld {
    /**
     * Used for randomly spawning in effects like lightning,
     * or in this case, asteroids
     *
     * @see ServerTickChunkCallback
     */
    @Inject(method = "tickChunk", at = @At("HEAD"))
    public void onTickChunk(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci) {
        ServerTickChunkCallback.getEVENT().invoker().onTick((ServerWorld) (Object) this, chunk, randomTickSpeed);
    }
}
