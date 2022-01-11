package dev.isxander.moonmc.material

import net.fabricmc.yarn.constants.MiningLevels
import net.minecraft.item.ToolMaterial

object LaserMaterial : ToolMaterial {
    override fun getDurability() = 2031
    override fun getMiningSpeedMultiplier() = 9f
    override fun getAttackDamage() = 4.5f
    override fun getMiningLevel() = MiningLevels.NETHERITE
    override fun getEnchantability() = 14
    override fun getRepairIngredient() = null
}
