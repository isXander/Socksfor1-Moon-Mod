package dev.isxander.moonmc.client.render

import dev.isxander.moonmc.utils.mc
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.PlayerModelPart
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityPose
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3f
import software.bernie.geckolib3.compat.PatchouliCompat
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.core.IAnimatableModel
import software.bernie.geckolib3.core.controller.AnimationController
import software.bernie.geckolib3.core.event.predicate.AnimationEvent
import software.bernie.geckolib3.model.AnimatedGeoModel
import software.bernie.geckolib3.model.provider.GeoModelProvider
import software.bernie.geckolib3.model.provider.data.EntityModelData
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer
import software.bernie.geckolib3.renderers.geo.IGeoRenderer
import software.bernie.geckolib3.util.AnimationUtils

abstract class GeoNonLivingEntityRenderer<T>(ctx: EntityRendererFactory.Context, private val modelProvider: AnimatedGeoModel<T>) : EntityRenderer<T>(ctx), IGeoRenderer<T> where T : Entity, T : IAnimatable {
    protected val layerRenderers = mutableListOf<GeoLayerRenderer<T>>()

    override fun render(
        entity: T,
        yaw: Float,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int
    ) {
        matrices.push()
        val shouldSit = entity.hasVehicle() && (entity.vehicle != null)
        val entityModelData = EntityModelData()
        entityModelData.isSitting = shouldSit

        var f = MathHelper.lerpAngleDegrees(tickDelta, entity.prevYaw, entity.yaw)
        val f1 = MathHelper.lerpAngleDegrees(tickDelta, entity.headYaw, entity.headYaw)
        var netHeadYaw = f1 - f

        val vehicle = entity.vehicle
        if (shouldSit && vehicle is LivingEntity) {
            f = MathHelper.lerpAngleDegrees(tickDelta, vehicle.prevBodyYaw, vehicle.bodyYaw)
            netHeadYaw = f1 - f
            var f3 = MathHelper.wrapDegrees(netHeadYaw)
            if (f3 < -85.0F) {
                f3 = -85.0F
            }

            if (f3 >= 85.0F) {
                f3 = 85.0F
            }

            f = f1 - f3
            if (f3 * f3 > 2500.0F) {
                f += f3 * 0.2F
            }

            netHeadYaw = f1 - f
        }

        val headPitch = MathHelper.lerp(tickDelta, entity.prevPitch, entity.pitch);
        val f7 = this.handleRotationFloat(entity, tickDelta);
        this.applyRotations(entity, matrices, f7, f, tickDelta);

        val lastLimbDistance = 0f
        val limbSwing = 0f
        entityModelData.headPitch = -headPitch
        entityModelData.netHeadYaw = -netHeadYaw

        val predicate = AnimationEvent(entity, 0f, 0f, tickDelta,
                false, mutableListOf<Any>(entityModelData));
        val model = modelProvider.getModel(modelProvider.getModelLocation(entity));
        (modelProvider as IAnimatableModel<T>).setLivingAnimations(entity, this.getUniqueID(entity), predicate);

        matrices.translate(0.0, 0.01, 0.0);
        mc.textureManager.bindTexture(getTexture(entity));
        val renderColor = getRenderColor(entity, tickDelta, matrices, vertexConsumers, null, light);
        val renderType = getRenderType(entity, tickDelta, matrices, vertexConsumers, null, light,
                getTexture(entity));
        val invis = entity.isInvisibleTo(mc.player);
        render(model, entity, tickDelta, renderType, matrices, vertexConsumers, null, light,
                getPackedOverlay(0f), renderColor.red / 255f, renderColor.green / 255f,
                renderColor.blue / 255f, if (invis) 0f else (renderColor.alpha / 255f));

        if (!entity.isSpectator) {
            for (layerRenderer in layerRenderers) {
                layerRenderer.render(matrices, vertexConsumers, light, entity, limbSwing, lastLimbDistance, tickDelta,
                        f7, netHeadYaw, headPitch);
            }
        }
        if (FabricLoader.getInstance().isModLoaded("patchouli")) {
            PatchouliCompat.patchouliLoaded(matrices)
        }
        matrices.pop()
    }

    override fun getUniqueID(animatable: T) = animatable.uuid.hashCode()
    override fun getTexture(entity: T) = getTextureLocation(entity)
    override fun getGeoModelProvider() = modelProvider

    override fun hasLabel(entity: T): Boolean {
        val d0 = dispatcher.getSquaredDistanceToCamera(entity)
        val f = if (entity.isSneaking) 32.0f else 64.0f
        return if (d0 >= (f * f).toDouble()) {
            false
        } else {
            entity === dispatcher.targetedEntity && entity.hasCustomName()
        }
    }
    override fun getTextureLocation(instance: T): Identifier = modelProvider.getTextureLocation(instance)

    protected open fun applyRotations(
        entityLiving: T,
        matrixStackIn: MatrixStack,
        ageInTicks: Float,
        rotationYaw: Float,
        tickDelta: Float
    ) {
        val pose = entityLiving.pose
        if (pose != EntityPose.SLEEPING) {
            matrixStackIn.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f - rotationYaw))
        }
        if (entityLiving.hasCustomName() || entityLiving is PlayerEntity) {
            val s = Formatting.strip(entityLiving.name.string)
            if (("Dinnerbone" == s || "Grumm" == s) && (entityLiving !is PlayerEntity
                        || (entityLiving as PlayerEntity).isPartVisible(PlayerModelPart.CAPE))
            ) {
                matrixStackIn.translate(0.0, (entityLiving.height + 0.1f).toDouble(), 0.0)
                matrixStackIn.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0f))
            }
        }
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected open fun handleRotationFloat(livingBase: T, tickDelta: Float): Float {
        return livingBase.age.toFloat() + tickDelta
    }

    companion object {
        init {
            AnimationController.addModelFetcher { obj ->
                if (obj is Entity) {
                    return@addModelFetcher AnimationUtils.getGeoModelForEntity(obj as Entity) as IAnimatableModel<Any>
                }
                null
            }
        }

        fun getPackedOverlay(uIn: Float): Int {
            return OverlayTexture.getUv(OverlayTexture.getU(uIn).toFloat(), false)
        }
    }
}
