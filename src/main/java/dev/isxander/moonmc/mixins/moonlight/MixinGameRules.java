package dev.isxander.moonmc.mixins.moonlight;

import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(GameRules.class)
public abstract class MixinGameRules {
    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/GameRules;register(Ljava/lang/String;Lnet/minecraft/world/GameRules$Category;Lnet/minecraft/world/GameRules$Type;)Lnet/minecraft/world/GameRules$Key;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=doDaylightCycle"
            )
        )
    )
    private static GameRules.Type<GameRules.BooleanRule> disableDaylightCycle(GameRules.Type<GameRules.BooleanRule> currentRule) {
        return AccessorBooleanRule.create(false);
    }
}

