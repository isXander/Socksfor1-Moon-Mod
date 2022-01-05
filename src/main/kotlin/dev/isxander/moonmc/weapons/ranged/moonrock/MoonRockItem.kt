package dev.isxander.moonmc.weapons.ranged.moonrock

import dev.isxander.moonmc.registry.MoonRegistry
import dev.isxander.moonmc.weapons.ranged.moonrock.MoonRockEntity
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.stat.Stats
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class MoonRockItem : Item(FabricItemSettings().maxCount(16).group(MoonRegistry.MOON_ITEM_GROUP)) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)
        if (!world.isClient) {
            val entity = MoonRegistry.MOON_ROCK_ENTITY.create(world) ?: return TypedActionResult.fail(stack)
            entity.setPosition(user.eyePos)
            entity.owner = user
            entity.setItem(stack)
            entity.setVelocity(user, user.pitch, user.yaw, 0.0f, 1f, 1f)
            world.spawnEntity(entity)
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this))
        if (!user.abilities.creativeMode) {
            stack.decrement(1)
        }
        return TypedActionResult.success(stack, world.isClient)
    }

    fun createEntity(world: World, stack: ItemStack, shooter: LivingEntity): MoonRockEntity {
        val entity = MoonRegistry.MOON_ROCK_ENTITY.create(world) ?: error("Couldn't create entity!")
        entity.setPosition(shooter.pos)
        entity.owner = shooter
        entity.setItem(stack)
        entity.setVelocity(shooter, shooter.pitch, shooter.yaw, 0.0f, 1f, 1f)
        return entity
    }
}
