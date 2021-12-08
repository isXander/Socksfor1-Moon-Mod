package dev.isxander.moonmc.weapons.melee

import dev.isxander.moonmc.registry.MoonRegistry
import dev.isxander.moonmc.weapons.material.LaserMaterial
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.SwordItem

class LaserSwordItem : SwordItem(LaserMaterial, 5, -1.5f, FabricItemSettings().group(MoonRegistry.MOON_ITEM_GROUP)) {
}
