package dev.isxander.moonmc

import net.fabricmc.api.ModInitializer
import net.minecraft.block.Blocks

object MoonMod : ModInitializer {
    const val gravityMultiplier = 0.25

    override fun onInitialize() {
        Blocks.OAK_LEAVES
    }
}
