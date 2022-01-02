package dev.isxander.moonmc.oxygen

import net.minecraft.client.model.Model
import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.util.math.MatrixStack

class OxygenMaskModel : Model(RenderLayer::getEntityCutoutNoCull) {
    val textureWidth = 64f
    val textureHeight = 64f

    val group2 = ModelPart(
        listOf(
            ModelPart.Cuboid(28, 10, -0.6207f, -3.6207f, -1.5f, 7.0f, 7.0f, 2.0f, 0.0f, 0f, 0f, false, textureWidth, textureHeight),
            ModelPart.Cuboid(12, 21, 0.6793f, -0.7207f, -1.8f, 4.0f, 4.0f, 2.0f, 0.0f, 0f, 0f, true, textureWidth, textureHeight),

        ),
        mapOf(
            "cube_r1" to ModelPart(
                listOf(
                    ModelPart.Cuboid(0, 19, -1.0784F, 4.1358F, 1.4F, 5.0F, 5.0F, 1.0F, 0.0F, 0.0F, 0.0F, false, textureWidth, textureHeight),
                    ModelPart.Cuboid(9, 0, -0.1784F, 5.1358F, -1.8F, 4.0F, 4.0F, 3.0F, 0.0F, 0.0F, 0.0F, true, textureWidth, textureHeight),
                    ModelPart.Cuboid(33, 24, -1.0784F, 4.1358F, -1.4F, 5.0F, 1.0F, 2.0F, 0.0F, 0.0F, 0.0F, false, textureWidth, textureHeight),
                    ModelPart.Cuboid(0, 34, -1.0784F, 5.2358F, -1.4F, 0.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F, false, textureWidth, textureHeight),
                    ModelPart.Cuboid(2, 34, 3.9216F, 5.2358F, -1.4F, 0.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F, true, textureWidth, textureHeight),
                    ModelPart.Cuboid(5, 34, -1.0784F, 8.1358F, -1.4F, 0.0F, 1.0F, 2.0F, 0.0F, 0.0F, 0.0F, false, textureWidth, textureHeight),
                    ModelPart.Cuboid(18, 33, 3.9216F, 8.1358F, -1.4F, 0.0F, 1.0F, 2.0F, 0.0F, 0.0F, 0.0F, false, textureWidth, textureHeight),
                    ModelPart.Cuboid(21, 25, -1.0784F, 4.1358F, -2.6F, 5.0F, 5.0F, 1.0F, 0.0F, 0.0F, 0.0F, false, textureWidth, textureHeight),
                ),
                mapOf()
            ).apply {
                setPivot(2.9293f, -1.0707f, 0.0f)
                setAngles(0.0f, 0.0f, -0.7854f)
            },
            "cube_r2" to ModelPart(
                listOf(
                    ModelPart.Cuboid(0, 33, -4.9642F, 4.3784F, 1.4F, 5.0F, 5.0F, 1.0F, 0.0F, 0.0F, 0.0F, false, textureWidth, textureHeight),
                    ModelPart.Cuboid(2, 17, 0.0358F, 4.3784F, -1.4F, 0.0F, 5.0F, 2.0F, 0.0F, 0.0F, 0.0F, true, textureWidth, textureHeight),
                    ModelPart.Cuboid(0, 14, -4.9642F, 4.3784F, -1.4F, 0.0F, 5.0F, 2.0F, 0.0F, 0.0F, 0.0F, false, textureWidth, textureHeight),
                    ModelPart.Cuboid(6, 29, -4.0642F, 4.3784F, -1.4F, 4.0F, 5.0F, 3.0F, 0.0F, 0.0F, 0.0F, false, textureWidth, textureHeight),
                    ModelPart.Cuboid(6, 29, -4.0642F, 5.3784F, -1.4F, 4.0F, 4.0F, 3.0F, 0.0F, 0.0F, 0.0F, false, textureWidth, textureHeight),
                    ModelPart.Cuboid(0, 31, -4.9642F, 4.3784F, -2.6F, 5.0F, 5.0F, 1.0F, 0.0F, 0.0F, 0.0F, false, textureWidth, textureHeight),
                    ModelPart.Cuboid(0, 2, -4.5642F, -4.4216F, -1.0F, 8.0F, 8.0F, 2.0F, 0.0F, 0.0F, 0.0F, false, textureWidth, textureHeight),
                    ModelPart.Cuboid(30, 29, 1.437F, 1.437F, -2.2F, 2.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F, false, textureWidth, textureHeight),
                ),
                mapOf()
            ).apply {
                setPivot(2.9293f, -1.0707f, 0.0f)
                setAngles(0.0f, 0.0f, 0.7854f)
            },
            "cube" to ModelPart(
                listOf(),
                mapOf()
            ).apply {
                setPivot(1.1615f, 4.4971f, -2.05f)
            }
        )
    ).apply { setPivot(-11.0793f, 25.1207f, 0.4f) }

    override fun render(
        matrices: MatrixStack,
        vertices: VertexConsumer,
        light: Int,
        overlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        group2.render(matrices, vertices, light, overlay, red, green, blue, alpha)
    }

}
