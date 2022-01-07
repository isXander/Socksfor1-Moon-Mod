package dev.isxander.moonmc.transport.rocket.render

import dev.isxander.moonmc.client.render.GeoNonLivingEntityRenderer
import dev.isxander.moonmc.transport.rocket.RocketEntity
import net.minecraft.client.render.entity.EntityRendererFactory

class RocketEntityRenderer(ctx: EntityRendererFactory.Context)
    : GeoNonLivingEntityRenderer<RocketEntity>(ctx, RocketEntityModel())
