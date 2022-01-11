package dev.isxander.moonmc.material

import dev.isxander.moonmc.registry.MoonRegistry
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorMaterial
import net.minecraft.recipe.Ingredient
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents

object AlienArmourMaterial : ArmorMaterial {
    private val baseDurability = arrayOf(13, 15, 16, 11)
    private val baseProtection = arrayOf(3, 6, 8, 3)

    override fun getDurability(slot: EquipmentSlot) = baseDurability[slot.entitySlotId] * 37
    override fun getProtectionAmount(slot: EquipmentSlot) = baseProtection[slot.entitySlotId]
    override fun getEnchantability() = 15
    override fun getEquipSound(): SoundEvent = SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE
    override fun getRepairIngredient(): Ingredient = Ingredient.ofItems(MoonRegistry.BLUE_ASTEROID_SHARD_ITEM, MoonRegistry.RED_ASTEROID_SHARD_ITEM, MoonRegistry.YELLOW_ASTEROID_SHARD_ITEM)
    override fun getName() = "alien"
    override fun getToughness() = 3f
    override fun getKnockbackResistance() = 0.1f
}
