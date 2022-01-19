package dev.isxander.moonmc.weapons.ranged.pistol

import dev.isxander.moonmc.registry.MoonRegistry
import dev.isxander.moonmc.material.LaserMaterial
import dev.isxander.moonmc.weapons.ranged.IReloadable
import dev.isxander.moonmc.weapons.ranged.IGun
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.RangedWeaponItem
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import java.util.function.Predicate

class LaserPistolItem : RangedWeaponItem(FabricItemSettings().group(MoonRegistry.MOON_ITEM_GROUP).maxDamage(
    LaserMaterial.durability)), IGun, IReloadable {
    override val adsZoom = 0.95f
    override val adsSensitivity = 0.8f

    override var bulletsInClip = 6

    override fun onStoppedUsing(stack: ItemStack, world: World, user: LivingEntity, remainingUseTicks: Int) {
        if (PistolBulletEntity.createAndSpawn(world, user, 1f, user.activeHand == Hand.OFF_HAND, true)) {
            (user as? PlayerEntity)?.itemCooldownManager?.set(this, 5)
            user.activeItem.damage(1, user) { it.sendToolBreakStatus(user.activeHand) }
            nextBullet(user)
        }
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val itemStack = user.getStackInHand(hand)
        user.setCurrentHand(hand)
        return TypedActionResult.consume(itemStack)
    }

    override fun reload(user: LivingEntity) {
        bulletsInClip = 6
        (user as? PlayerEntity)?.itemCooldownManager?.set(this, 200)
    }

    override fun isUsedOnRelease(stack: ItemStack) = true
    override fun getMaxUseTime(stack: ItemStack) = 0
    override fun getProjectiles(): Predicate<ItemStack> = Predicate { true }
    override fun getRange(): Int = 20
}
