package dev.isxander.moonmc.oxygen

import net.minecraft.block.Blocks
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.mob.WaterCreatureEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos

const val MAX_OXYGEN = 12_000

var LivingEntity.oxygen: Int
    get() = (this as LivingEntityOxygenAccess).oxygen
    set(value) { (this as LivingEntityOxygenAccess).oxygen = value.coerceIn(0..MAX_OXYGEN) }

val LivingEntity.isWearingOxygenMask: Boolean
    get() = this.getEquippedStack(EquipmentSlot.HEAD).item is OxygenMask

fun LivingEntity.updateOxygen() {
    oxygen += if (
        !isWearingOxygenMask
        && !world.getBlockState(BlockPos(x, eyeY, z)).isOf(Blocks.BUBBLE_COLUMN)
        && this !is WaterCreatureEntity
        && !this.isInvulnerable
    ) {
        -1
    } else {
        40
    }

    if (oxygen <= 1000) {
        val amplifier = (((oxygen - 11_000) / 200) + 1).coerceAtMost(4)
        this.addStatusEffect(StatusEffectInstance(StatusEffects.BLINDNESS, 20, amplifier))
    }

    if (oxygen == 0) {
        damage(OxygenDamageSource, 0.5f)
    }
}
