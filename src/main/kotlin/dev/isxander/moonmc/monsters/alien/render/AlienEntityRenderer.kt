package dev.isxander.moonmc.monsters.alien.render

import dev.isxander.moonmc.monsters.alien.AlienEntity
import net.minecraft.client.render.entity.EntityRendererFactory
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer

class AlienEntityRenderer(ctx: EntityRendererFactory.Context) : GeoEntityRenderer<AlienEntity>(ctx, AlienEntityModel()) {
}
