@file:JvmName("EntityOxygenUtils")

package dev.isxander.moonmc.oxygen

import net.minecraft.block.Blocks
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.mob.WaterCreatureEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
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
        && (this as? PlayerEntity)?.isCreative == false
    ) {
        -1
    } else {
        40
    }

    if (oxygen % 20 == 0) {
        val amplifier = when (oxygen) {
            in 2000..2999 -> 0
            in 1000..1999 -> 1
            in 0..999 -> 2
            else -> return
        }
        addStatusEffect(StatusEffectInstance(StatusEffects.BLINDNESS, 100, amplifier, false, false, false))
        damage(OxygenDamageSource, amplifier.toFloat())

        if (amplifier >= 1) {
            addStatusEffect(StatusEffectInstance(StatusEffects.NAUSEA, 100, amplifier - 1, false, false, false))
        }
    }
}

fun LivingEntity.writeExtraNbt(nbt: NbtCompound) {
    nbt.putInt("oxygen", oxygen)
}

fun LivingEntity.readExtraNbt(nbt: NbtCompound) {
    oxygen = nbt.getInt("oxygen")
}
