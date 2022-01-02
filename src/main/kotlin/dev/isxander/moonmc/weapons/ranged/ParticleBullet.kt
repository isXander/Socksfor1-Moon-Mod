package dev.isxander.moonmc.weapons.ranged

import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.world.World
import kotlin.math.pow

abstract class ParticleBullet(type: EntityType<out ParticleBullet>, world: World) : PersistentProjectileEntity(type, world) {
    abstract val strengthModifier: Double
    abstract val baseStrength: Double
    abstract fun spawnParticles()

    override fun tick() {
        if (inGround) {
            discard()
            return
        }

        damage = baseStrength * strengthModifier.pow(age * 0.25)

        if (damage < 0.05) kill()

        spawnParticles()
    }

    override fun shouldRender(distance: Double) = false
    override fun asItemStack(): ItemStack = ItemStack.EMPTY
    override fun hasNoGravity() = true

    class EmptyRenderer<T : ParticleBullet>(context: EntityRendererFactory.Context) : EntityRenderer<T>(context) {
        override fun getTexture(entity: T) = null

        override fun render(
            entity: T?,
            yaw: Float,
            tickDelta: Float,
            matrices: MatrixStack?,
            vertexConsumers: VertexConsumerProvider?,
            light: Int
        ) {

        }
    }
}
