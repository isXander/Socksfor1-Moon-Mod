package dev.isxander.moonmc.mixins.moonlight;

import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRules.BooleanRule.class)
public interface AccessorBooleanRule {
    @Invoker("create")
    static GameRules.Type<GameRules.BooleanRule> create(boolean initialValue) {
        throw new AssertionError();
    }
}
