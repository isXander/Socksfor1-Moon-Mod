package dev.isxander.moonmc.oxygen

import dev.isxander.moonmc.registry.MoonRegistry
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.model.BipedEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

class OxygenMaskRenderer : ArmorRenderer {
    val model = OxygenMaskModel()
    val texture = Identifier("moonmc", "textures/armor/oxygen_mask.png")

    override fun render(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        stack: ItemStack,
        entity: LivingEntity,
        slot: EquipmentSlot,
        light: Int,
        contextModel: BipedEntityModel<LivingEntity>
    ) {
        if (slot != EquipmentSlot.HEAD) return

        ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, model, texture)
    }

}
