package dev.isxander.moonmc.weapons.ranged

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

class LaserShotgunItem : RangedWeaponItem(FabricItemSettings().group(MoonRegistry.MOON_ITEM_GROUP).maxDamage(LaserMaterial.durability)) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        repeat(10) {
            val shell = MoonRegistry.SHOTGUN_SHELL.create(world)

            if (shell != null) {
                shell.setPosition(user.x, user.eyeY, user.z)
                shell.owner = user
                shell.setVelocity(user, user.pitch, user.yaw, 0.0f, 20f, 20f)
                world.spawnEntity(shell)
                user.activeItem.damage(1, user) { it.sendToolBreakStatus(hand) }
            }
        }

        return super.use(world, user, hand)
    }

    override fun isUsedOnRelease(stack: ItemStack) = true
    override fun getMaxUseTime(stack: ItemStack) = 1
    override fun getProjectiles(): Predicate<ItemStack> = Predicate { true }
    override fun getRange(): Int = 5
}
