package dev.isxander.moonmc.transport.rocket.render

import dev.isxander.moonmc.client.render.GeoNonLivingEntityRenderer
import dev.isxander.moonmc.transport.rocket.RocketEntity
import dev.isxander.moonmc.transport.rocket.RocketItem
import net.minecraft.client.render.entity.EntityRendererFactory
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer

class RocketEntityRenderer(ctx: EntityRendererFactory.Context)
    : GeoNonLivingEntityRenderer<RocketEntity>(ctx, RocketEntityModel())

class RocketItemRenderer
    : GeoItemRenderer<RocketItem>(RocketItemModel())
