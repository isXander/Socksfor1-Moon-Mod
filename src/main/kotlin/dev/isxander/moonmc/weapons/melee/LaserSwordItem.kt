package dev.isxander.moonmc.weapons.melee

import dev.isxander.moonmc.registry.MoonRegistry
import dev.isxander.moonmc.utils.ItemListener
import dev.isxander.moonmc.utils.mc
import dev.isxander.moonmc.material.LaserMaterial
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.StackReference
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.screen.slot.Slot
import net.minecraft.sound.SoundCategory
import net.minecraft.util.ClickType
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import kotlin.random.Random

class LaserSwordItem : SwordItem(LaserMaterial, 0, -1.5f, FabricItemSettings().group(MoonRegistry.MOON_ITEM_GROUP)), ItemListener {
    private var wasSelected = false

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        if (!selected) {
            if (wasSelected && world.isClient) {
                mc.soundManager.stopSounds(Registry.SOUND_EVENT.getId(MoonRegistry.LASER_SWORD_AMBIENT_SOUND), SoundCategory.PLAYERS)
                wasSelected = false
            }
            return
        }
        wasSelected = true

        if (world.time % 70L == 0L) {
            world.playSound(null, entity.x, entity.y, entity.z, MoonRegistry.LASER_SWORD_AMBIENT_SOUND, SoundCategory.PLAYERS, 1f, 1f)
        }
    }

    override fun onSwing(entity: LivingEntity, world: World, hand: Hand, stack: ItemStack) {
        world.playSound(
            null,
            entity.x,
            entity.y,
            entity.z,
            MoonRegistry.LASER_SWORD_WAVE_SOUND,
            SoundCategory.PLAYERS,
            1f,
            1f + (Random.nextFloat() * 0.2f)
        )
    }
}
