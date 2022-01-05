package dev.isxander.moonmc.disasters.asteroid.render

import dev.isxander.moonmc.disasters.asteroid.AsteroidEntity
import net.minecraft.util.Identifier
import software.bernie.geckolib3.model.AnimatedGeoModel

class AsteroidModel : AnimatedGeoModel<AsteroidEntity>() {
    override fun getModelLocation(`object`: AsteroidEntity) = Identifier("moonmc", "geo/asteroid.geo.json")
    override fun getTextureLocation(`object`: AsteroidEntity) = Identifier("moonmc", "textures/entity/asteroid.png")
    override fun getAnimationFileLocation(animatable: AsteroidEntity) = Identifier("moonmc", "animations/asteroid.animation.json")
}
