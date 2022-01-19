package dev.isxander.moonmc.weapons.ranged

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity

interface IReloadable {
    var bulletsInClip: Int

    fun nextBullet(user: LivingEntity) {
        if (bulletsInClip > 0) {
            bulletsInClip--
        }

        if (bulletsInClip == 0) {
            reload(user)
        }
    }

    fun reload(user: LivingEntity)
}
