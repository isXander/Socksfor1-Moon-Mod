package dev.isxander.moonmc.transport.rocket

import dev.isxander.moonmc.registry.MoonRegistry
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.stat.Stats
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.RaycastContext
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.core.PlayState
import software.bernie.geckolib3.core.builder.AnimationBuilder
import software.bernie.geckolib3.core.controller.AnimationController
import software.bernie.geckolib3.core.manager.AnimationData
import software.bernie.geckolib3.core.manager.AnimationFactory

class RocketItem : Item(FabricItemSettings().group(MoonRegistry.MOON_ITEM_GROUP)), IAnimatable {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)
        val hitResult = raycast(world, user, RaycastContext.FluidHandling.NONE)

        if (hitResult is BlockHitResult) {
            val rocket = MoonRegistry.ROCKET_ENTITY.create(world) ?: return TypedActionResult.pass(stack)
            rocket.setPosition(hitResult.pos)
            rocket.yaw = user.yaw

            if (!world.isSpaceEmpty(rocket, rocket.boundingBox))
                return TypedActionResult.fail(stack)

            if (!world.isClient) {
                world.spawnEntity(rocket)
                world.emitGameEvent(user, GameEvent.ENTITY_PLACE, BlockPos(rocket.pos))
                if (!user.abilities.creativeMode) {
                    stack.decrement(1)
                }
            }

            user.incrementStat(Stats.USED.getOrCreateStat(this))
            return TypedActionResult.success(stack, world.isClient)
        }
        return TypedActionResult.pass(stack)
    }

    val animations = AnimationFactory(this)

    override fun registerControllers(p0: AnimationData) {
        p0.addAnimationController(AnimationController(this, "controller", 20f) {
            it.controller.setAnimation(AnimationBuilder().addAnimation("idle", true))
            PlayState.CONTINUE
        })
    }
    override fun getFactory() = animations
}
