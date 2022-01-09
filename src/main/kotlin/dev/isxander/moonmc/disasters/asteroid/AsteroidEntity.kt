package dev.isxander.moonmc.disasters.asteroid

import dev.isxander.moonmc.food.AsteroidShardItem
import dev.isxander.moonmc.registry.MoonRegistry
import dev.isxander.moonmc.utils.getRandomPointInRadius
import net.minecraft.block.Blocks
import net.minecraft.block.entity.ChestBlockEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.projectile.ExplosiveProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.network.packet.s2c.play.PlaySoundIdS2CPacket
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.GameRules
import net.minecraft.world.World
import net.minecraft.world.chunk.WorldChunk
import net.minecraft.world.explosion.Explosion
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.core.PlayState
import software.bernie.geckolib3.core.builder.AnimationBuilder
import software.bernie.geckolib3.core.controller.AnimationController
import software.bernie.geckolib3.core.manager.AnimationData
import software.bernie.geckolib3.core.manager.AnimationFactory
import kotlin.random.Random
import kotlin.random.nextInt

class AsteroidEntity(type: EntityType<out AsteroidEntity>, world: World) : ExplosiveProjectileEntity(type, world), IAnimatable {
    private val animator = AnimationFactory(this)
    val explosionPower = 50f

    override fun tick() {
        super.tick()

        if (world.isClient) {
            if (world.time % 30L == 0L) {
                world.playSound(x, y, z, MoonRegistry.ASTEROID_AMBIENT_SOUND, SoundCategory.BLOCKS, 30f, 1f, false)
            }
        }
    }

    override fun onCollision(hitResult: HitResult) {
        super.onCollision(hitResult)
        if (!world.isClient) {
            val grief = world.gameRules.getBoolean(GameRules.DO_MOB_GRIEFING)
            world.createExplosion(null, x, y, z, explosionPower, false, if (grief) Explosion.DestructionType.DESTROY else Explosion.DestructionType.NONE)

            val chestPos = getChestPosition(blockPos)
            world.setBlockState(chestPos, Blocks.CHEST.defaultState)
            val chest = world.getBlockEntity(chestPos) as? ChestBlockEntity
            chest!!.setLootTable(Identifier("moonmc", "asteroid_chest"), world.random.nextLong())

            repeat(random.nextInt(10, 21)) {
                val itemPos = getRandomPointInRadius(x.toFloat(), z.toFloat(), explosionPower / 4f, random)
                world.spawnEntity(ItemEntity(world, itemPos.x.toDouble(), y, itemPos.y.toDouble(), ItemStack(AsteroidShardItem.getRandomType())))
            }

            if (random.nextInt(2) == 0) {
                val alien = MoonRegistry.ALIEN_ENTITY.create(world)!!
                alien.setPosition(pos)
                world.spawnEntity(alien)
            }

            discard()
        } else {
            world.playSound(x, y, z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 15f, .25f, false)
        }
    }

    private fun getChestPosition(explosionOrigin: BlockPos): BlockPos {
        val chestPos = getRandomPointInRadius(explosionOrigin.x.toFloat(), explosionOrigin.z.toFloat(), explosionPower / 4, random)
        val x = chestPos.x.toInt()
        val z = chestPos.y.toInt()

        return BlockPos(x, getBlockingY(world, x, (explosionOrigin.y + 30).coerceAtMost(world.topY), z) ?: explosionOrigin.y, z)
    }

    override fun isOnFire() = false

    override fun registerControllers(p0: AnimationData) {
        p0.addAnimationController(AnimationController(this, "controller", 20f) {
            it.controller.setAnimation(AnimationBuilder().addAnimation("fall", true))
            PlayState.CONTINUE
        })
    }
    override fun getFactory(): AnimationFactory = animator

    companion object {
        fun tryNaturalSpawn(world: ServerWorld, chunk: WorldChunk) {
            if (world.random.nextInt(250_000) == 0) {
                val chunkPos = chunk.pos
                val asteroid = MoonRegistry.ASTEROID_ENTITY.create(world) ?: return
                val pos = getAsteroidPos(world, world.getRandomPosInChunk(chunkPos.startX, 0, chunkPos.startZ, 15))
                asteroid.setPosition(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
                asteroid.velocity = Vec3d.fromPolar(90f - Random.nextFloat() * 20f, Random.nextFloat() * 360f).multiply(0.5)
                asteroid.powerX = asteroid.velocity.x * 0.1
                asteroid.powerY = asteroid.velocity.y * 0.1
                asteroid.powerZ = asteroid.velocity.z * 0.1
                world.spawnEntity(asteroid)
            }
        }

        private fun getAsteroidPos(world: World, pos: BlockPos): BlockPos {
            val y = ((getBlockingY(world, pos.x, world.topY, pos.z) ?: world.topY) + Random.nextInt(100..150)).coerceAtMost(world.topY)
            return BlockPos(pos.x, y, pos.z)
        }

        private fun getBlockingY(world: World, x: Int, yOrigin: Int, z: Int): Int? {
            var y = yOrigin
            while (y >= world.bottomY) {
                val pos = BlockPos(x, y, z)

                // is collidable
                if (world.getBlockState(pos).getCollisionShape(world, pos) != VoxelShapes.empty()) {
                    return y + 1
                }

                y--
            }

            return null
        }
    }
}
