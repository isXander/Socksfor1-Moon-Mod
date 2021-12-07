package dev.isxander.moonmc.oxygen

import net.minecraft.entity.damage.DamageSource

object OxygenDamageSource : DamageSource("ranOutOfOxygen") {
    init {
        setBypassesArmor()
    }
}
