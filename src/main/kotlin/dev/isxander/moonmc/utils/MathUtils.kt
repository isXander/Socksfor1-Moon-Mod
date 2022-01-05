package dev.isxander.moonmc.utils

import net.minecraft.util.math.Vec2f
import java.util.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

fun getRandomPointInRadius(centerX: Float, centerY: Float, radius: Float, random: Random): Vec2f {
    val randomRadius = radius * sqrt(random.nextDouble())
    val theta = random.nextDouble() * 2 * Math.PI

    val x = centerX + randomRadius * cos(theta)
    val y = centerY + randomRadius * sin(theta)

    return Vec2f(x.toFloat(), y.toFloat())
}
