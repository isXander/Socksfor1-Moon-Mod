package dev.isxander.moonmc.food

import dev.isxander.moonmc.registry.MoonRegistry
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.FoodComponent
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import kotlin.random.Random
import kotlin.random.nextInt

class AsteroidShardItem : Item(FabricItemSettings().group(MoonRegistry.MOON_ITEM_GROUP).food(FoodComponent.Builder().alwaysEdible().build())) {
    private val effects = Registry.STATUS_EFFECT.entries
        .filter { !it.value.isInstant }
        .map { it.value }

    override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack {
        if (!world.isClient) {
            val effect = effects.random()
            val instance = StatusEffectInstance(effect, 1200, Random.nextInt(0, 3))
            (user as? PlayerEntity)?.itemCooldownManager?.set(this, 20)
            user.addStatusEffect(instance)
        }
        return super.finishUsing(stack, world, user)
    }

    companion object {
        fun getRandomType(): Item {
            return when (Random.nextInt(0..2)) {
                0 -> MoonRegistry.BLUE_ASTEROID_SHARD_ITEM
                1 -> MoonRegistry.RED_ASTEROID_SHARD_ITEM
                2 -> MoonRegistry.YELLOW_ASTEROID_SHARD_ITEM
                else -> MoonRegistry.BLUE_ASTEROID_SHARD_ITEM
            }
        }
    }
}
