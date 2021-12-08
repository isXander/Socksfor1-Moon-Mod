package dev.isxander.moonmc.registry

import dev.isxander.moonmc.oxygen.OxygenMask
import dev.isxander.moonmc.weapons.melee.LaserSwordItem
import io.ejekta.kambrik.registration.KambrikAutoRegistrar
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

object MoonRegistry : KambrikAutoRegistrar {
    val OXYGEN_MASK = "oxygen_mask" forItem OxygenMask()
    val LASER_SWORD = "laser_sword" forItem LaserSwordItem()

    val MOON_ITEM_GROUP = FabricItemGroupBuilder.create(Identifier("moonmc", "moon_item_group"))
        .icon { ItemStack(OXYGEN_MASK) }
        .appendItems { _ -> ItemStack(OXYGEN_MASK) }
        .build()
}
