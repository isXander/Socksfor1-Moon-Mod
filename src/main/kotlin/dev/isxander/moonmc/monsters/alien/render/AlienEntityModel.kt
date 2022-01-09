package dev.isxander.moonmc.monsters.alien.render

import dev.isxander.moonmc.monsters.alien.AlienEntity
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3f
import software.bernie.geckolib3.core.event.predicate.AnimationEvent
import software.bernie.geckolib3.model.AnimatedGeoModel
import software.bernie.geckolib3.model.provider.data.EntityModelData


class AlienEntityModel : AnimatedGeoModel<AlienEntity>() {
    override fun getModelLocation(`object`: AlienEntity) = Identifier("moonmc", "geo/alien.geo.json")
    override fun getTextureLocation(`object`: AlienEntity) = Identifier("moonmc", "textures/entity/alien.png")
    override fun getAnimationFileLocation(animatable: AlienEntity) = Identifier("moonmc", "animations/alien.animation.json")

    override fun setLivingAnimations(entity: AlienEntity, uniqueID: Int, customPredicate: AnimationEvent<*>) {
        super.setLivingAnimations(entity, uniqueID, customPredicate)

        val head = animationProcessor.getBone("h_bone")
        val extraData = customPredicate.getExtraDataOfType(EntityModelData::class.java).first() as EntityModelData
        if (head != null) {
            head.rotationX = Vec3f.POSITIVE_X.getRadialQuaternion(extraData.headPitch * (Math.PI.toFloat() / 180f)).x
            head.rotationY = Vec3f.POSITIVE_Y.getRadialQuaternion(extraData.netHeadYaw * (Math.PI.toFloat() / 180f)).y
        }
    }
}
