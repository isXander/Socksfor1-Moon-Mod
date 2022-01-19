package dev.isxander.moonmc.weapons.ranged.rifle

import dev.isxander.moonmc.registry.MoonRegistry
import dev.isxander.moonmc.material.LaserMaterial
import dev.isxander.moonmc.weapons.ranged.IReloadable
import dev.isxander.moonmc.weapons.ranged.IGun
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.RangedWeaponItem
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import java.util.function.Predicate

class LaserRifleItem : RangedWeaponItem(FabricItemSettings().group(MoonRegistry.MOON_ITEM_GROUP).maxDamage(LaserMaterial.durability)), IGun,
    IReloadable {
    override val adsZoom = 0.95f
    override val adsSensitivity = 0.8f

    override var bulletsInClip = 10

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (RifleBulletEntity.createAndSpawn(world, user, 1f, hand == Hand.OFF_HAND, true)) {
            user.itemCooldownManager.set(this, 1)
            user.activeItem.damage(1, user) { it.sendToolBreakStatus(hand) }
            nextBullet(user)
        }
        world.playSound(null, user.x, user.eyeY, user.z, MoonRegistry.LASER_GUN_SHOOT_SOUND, SoundCategory.PLAYERS, 0.1f, .7f)

        return super.use(world, user, hand)
    }

    override fun reload(user: LivingEntity) {
        bulletsInClip = 10
        (user as? PlayerEntity)?.itemCooldownManager?.set(this, 200)
    }

    override fun isUsedOnRelease(stack: ItemStack) = true
    override fun getMaxUseTime(stack: ItemStack) = 0
    override fun getProjectiles(): Predicate<ItemStack> = Predicate { true }
    override fun getRange(): Int = 50
}
