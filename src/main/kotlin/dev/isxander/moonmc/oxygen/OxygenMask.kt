package dev.isxander.moonmc.oxygen

import dev.isxander.moonmc.registry.MoonRegistry
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents

class OxygenMask : ArmorItem(Material, EquipmentSlot.HEAD, FabricItemSettings().group(MoonRegistry.MOON_ITEM_GROUP).customDamage { _, _, _, _ -> 0 }.maxCount(1)) {
    object Material : ArmorMaterial {
        override fun getDurability(slot: EquipmentSlot) = 20
        override fun getProtectionAmount(slot: EquipmentSlot) = 0
        override fun getEnchantability() = 0
        override fun getEquipSound(): SoundEvent = SoundEvents.ITEM_ARMOR_EQUIP_CHAIN
        override fun getRepairIngredient(): Ingredient = Ingredient.ofItems(Items.GLASS)
        override fun getName() = "oxygen"
        override fun getToughness() = 0f
        override fun getKnockbackResistance() = 0f
    }
}
