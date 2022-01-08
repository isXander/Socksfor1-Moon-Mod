package dev.isxander.moonmc.transport.rocket.render

import dev.isxander.moonmc.transport.rocket.RocketEntity
import dev.isxander.moonmc.transport.rocket.RocketItem
import net.minecraft.util.Identifier
import software.bernie.geckolib3.model.AnimatedGeoModel

class RocketEntityModel : AnimatedGeoModel<RocketEntity>() {
    override fun getModelLocation(`object`: RocketEntity) = Identifier("moonmc", "geo/rocket.geo.json")
    override fun getTextureLocation(`object`: RocketEntity) = Identifier("moonmc", "textures/entity/rocket.png")
    override fun getAnimationFileLocation(animatable: RocketEntity) = Identifier("moonmc", "animations/blank.animation.json")
}

class RocketItemModel : AnimatedGeoModel<RocketItem>() {
    override fun getModelLocation(`object`: RocketItem) = Identifier("moonmc", "geo/rocket.item.geo.json")
    override fun getTextureLocation(`object`: RocketItem) = Identifier("moonmc", "textures/entity/rocket.png")
    override fun getAnimationFileLocation(animatable: RocketItem) = Identifier("moonmc", "animations/blank.animation.json")
}
