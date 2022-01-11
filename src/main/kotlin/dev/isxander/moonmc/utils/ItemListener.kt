package dev.isxander.moonmc.utils

import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.world.World

interface ItemListener {
    fun onSwing(entity: LivingEntity, world: World, hand: Hand, stack: ItemStack) {}
}
