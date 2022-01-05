package dev.isxander.moonmc.oxygen.render

import dev.isxander.moonmc.oxygen.OxygenMask
import net.minecraft.util.Identifier
import software.bernie.geckolib3.model.AnimatedGeoModel

class OxygenMaskModel : AnimatedGeoModel<OxygenMask>() {
    override fun getModelLocation(`object`: OxygenMask) = Identifier("moonmc", "geo/oxygen_mask.geo.json")
    override fun getTextureLocation(`object`: OxygenMask) = Identifier("moonmc", "textures/item/oxygen_mask.png")
    override fun getAnimationFileLocation(animatable: OxygenMask) = Identifier("moonmc", "animations/blank.animation.json")
}
