package dev.isxander.moonmc.oxygen

import net.minecraft.block.Blocks
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.WaterCreatureEntity
import net.minecraft.util.math.BlockPos

const val MAX_OXYGEN = 200

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
    ) {
        -1
    } else {
        1
    }

    if (oxygen == 0) {
        damage(OxygenDamageSource, 0.5f)
    }
}
