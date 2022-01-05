package dev.isxander.moonmc.disasters.asteroid.render

import dev.isxander.moonmc.disasters.asteroid.AsteroidEntity
import net.minecraft.client.render.entity.EntityRendererFactory
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer

class AsteroidRenderer(ctx: EntityRendererFactory.Context)
    : GeoProjectilesRenderer<AsteroidEntity>(ctx, AsteroidModel())
