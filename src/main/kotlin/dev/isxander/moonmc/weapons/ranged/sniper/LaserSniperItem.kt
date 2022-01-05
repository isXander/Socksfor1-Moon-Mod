package dev.isxander.moonmc.weapons.ranged.sniper

import dev.isxander.moonmc.registry.MoonRegistry
import dev.isxander.moonmc.weapons.material.LaserMaterial
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.RangedWeaponItem
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import java.util.function.Predicate

class LaserSniperItem : RangedWeaponItem(FabricItemSettings().group(MoonRegistry.MOON_ITEM_GROUP).maxDamage(LaserMaterial.durability)) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (SniperBulletEntity.createAndSpawn(world, user, 1f, hand == Hand.OFF_HAND, true)) {
            user.itemCooldownManager.set(this, 40)
            user.activeItem.damage(1, user) { it.sendToolBreakStatus(hand) }
            user.stopUsingItem()
        }

        return super.use(world, user, hand)
    }

    override fun isUsedOnRelease(stack: ItemStack) = true
    override fun getMaxUseTime(stack: ItemStack) = 0
    override fun getProjectiles(): Predicate<ItemStack> = Predicate { true }
    override fun getRange(): Int = 100
}
