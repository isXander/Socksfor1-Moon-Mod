package dev.isxander.moonmc.monsters.render

import dev.isxander.moonmc.monsters.MoonManEntity
import dev.isxander.moonmc.monsters.model.MoonManEntityModel
import net.minecraft.client.render.entity.BipedEntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer
import net.minecraft.client.render.entity.model.EntityModelLayers
import net.minecraft.util.Identifier

class MoonManEntityRenderer(ctx: EntityRendererFactory.Context) : BipedEntityRenderer<MoonManEntity, MoonManEntityModel>(ctx, MoonManEntityModel(ctx.getPart(EntityModelLayers.ZOMBIE)), 0.5f) {
    private val texture = Identifier("moonmc", "textures/entity/moon_man.png")

    init {
        addFeature(ArmorFeatureRenderer(this, MoonManEntityModel(ctx.getPart(EntityModelLayers.ZOMBIE_INNER_ARMOR)), MoonManEntityModel(ctx.getPart(EntityModelLayers.ZOMBIE_OUTER_ARMOR))))
    }

    override fun getTexture(entity: MoonManEntity?): Identifier = texture
}
