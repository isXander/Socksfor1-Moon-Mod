package dev.isxander.moonmc.weapons.ranged

import dev.isxander.moonmc.registry.MoonRegistry
import dev.isxander.moonmc.utils.mc
import net.minecraft.advancement.criterion.Criteria
import net.minecraft.client.particle.BlockDustParticle
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.ProjectileDamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.World
import kotlin.random.Random

abstract class ParticleBullet(type: EntityType<out ParticleBullet>, world: World) : PersistentProjectileEntity(type, world) {
    abstract val strengthModifier: Double
    abstract fun spawnParticles()

    override fun tick() {
        super.tick()

        if (inGround) {
            discard()
            return
        }

        damage *= strengthModifier

        if (damage < 0.5) kill()

        spawnParticles()
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        val l: DamageSource
        val entity = entityHitResult.entity

        if (owner == null) {
            l = BulletDamageSource(this, this)
        } else {
            l = BulletDamageSource(this, owner!!)
            if (owner is LivingEntity) {
                (owner as LivingEntity).onAttacking(entity)
            }
        }
        val targetInvulnerable = entity.type === EntityType.ENDERMAN || entity.type === MoonRegistry.ALIEN_ENTITY

        if (this.isOnFire && !targetInvulnerable) {
            entity.setOnFireFor(5)
        }

        discard()

        if (entity.damage(l, damage.toFloat())) {
            if (targetInvulnerable) {
                return
            }
            if (entity is LivingEntity) {
                val velocity = velocity.multiply(1.0, 0.0, 1.0).normalize().multiply(punch.toDouble() * 0.6)
                if (punch > 0) {
                    if (velocity.lengthSquared() > 0.0) {
                        entity.addVelocity(velocity.x, 0.1, velocity.z)
                    }
                }

                if (!world.isClient && owner is LivingEntity) {
                    EnchantmentHelper.onUserDamaged(entity, owner)
                    EnchantmentHelper.onTargetDamaged(owner as LivingEntity?, entity)
                }
                onHit(entity)
                if (owner != null && entity != owner && entity is PlayerEntity && owner is ServerPlayerEntity && !this.isSilent) {
                    (owner as ServerPlayerEntity).networkHandler.sendPacket(
                        GameStateChangeS2CPacket(
                            GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER,
                            GameStateChangeS2CPacket.DEMO_OPEN_SCREEN.toFloat()
                        )
                    )
                }

                if (!world.isClient && owner is ServerPlayerEntity) {
                    val serverOwner = owner as ServerPlayerEntity
                    Criteria.KILLED_BY_CROSSBOW.trigger(serverOwner, listOf<Entity?>(entity))
                }
            }
            playSound(sound, 1.0f, 1.2f / (random.nextFloat() * 0.2f + 0.9f))
        } else {
            velocity = velocity.multiply(-0.1)
            yaw += 180.0f
            prevYaw += 180.0f
            if (!world.isClient && velocity.lengthSquared() < 1.0E-7) {
                if (pickupType == PickupPermission.ALLOWED) {
                    this.dropStack(asItemStack(), 0.1f)
                }
            }
        }
    }

    override fun getHitSound(): SoundEvent = MoonRegistry.BULLET_HIT_SOUND

    override fun onBlockHit(hitResult: BlockHitResult) {
        super.onBlockHit(hitResult)
        if (world.isClient) {
            mc.particleManager.addParticle(BlockDustParticle(world as ClientWorld, x, y, z, 0.0, 0.0, 0.0, world.getBlockState(hitResult.blockPos)))
        }
    }

    override fun shouldRender(distance: Double) = false
    override fun asItemStack(): ItemStack = ItemStack.EMPTY
    override fun hasNoGravity() = true

    companion object {
        @JvmStatic
        protected fun playBulletSound(world: World, x: Double, y: Double, z: Double, volume: Float = 1f, pitch: Float = 1f, pitchDivergence: Float = 0.6f) {
            world.playSound(null, x, y, z, MoonRegistry.LASER_GUN_SHOOT_SOUND, SoundCategory.PLAYERS, volume, (pitch - pitchDivergence / 2f) + (Random.nextFloat() * pitchDivergence))
        }
    }

    class BulletDamageSource(bullet: ParticleBullet, owner: Entity) : ProjectileDamageSource("bullet", bullet, owner)

    class EmptyRenderer<T : ParticleBullet>(context: EntityRendererFactory.Context) : EntityRenderer<T>(context) {
        override fun getTexture(entity: T) = null

        override fun render(
            entity: T?,
            yaw: Float,
            tickDelta: Float,
            matrices: MatrixStack?,
            vertexConsumers: VertexConsumerProvider?,
            light: Int
        ) {}
    }
}
