package dev.isxander.moonmc.oxygen.render

import dev.isxander.moonmc.oxygen.OxygenMask
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer

class OxygenMaskRenderer : GeoArmorRenderer<OxygenMask>(OxygenMaskModel()) {
    init {
        headBone = "head"
        bodyBone = null
        rightArmBone = null
        leftArmBone = null
        rightLegBone = null
        leftLegBone = null
        rightBootBone = null
        leftBootBone = null
    }
}
