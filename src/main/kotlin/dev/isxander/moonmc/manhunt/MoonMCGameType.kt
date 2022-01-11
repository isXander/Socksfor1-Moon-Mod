package dev.isxander.moonmc.manhunt

import dev.isxander.manhunt.api.ManhuntGameType
import dev.isxander.manhunt.server.ManhuntGame
import dev.isxander.moonmc.oxygen.MAX_OXYGEN
import dev.isxander.moonmc.oxygen.oxygen
import dev.isxander.moonmc.packets.server.sendOxygenPacket
import dev.isxander.moonmc.registry.MoonRegistry
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ItemStack

object MoonMCGameType : ManhuntGameType {
    override val id = "moonmc"

    override fun onGameStart(game: ManhuntGame) {
        for (player in game.world.players) {
            if (player != game.speedrunner) {
                player.equipStack(EquipmentSlot.HEAD, ItemStack(MoonRegistry.OXYGEN_MASK))
            }
        }
    }

    override fun provideTrophies(): List<(ManhuntGame) -> Unit> = listOf(
        {
            it.speedrunner.giveItemStack(ItemStack(MoonRegistry.LASER_SWORD))

            it.speedrunner.oxygen = MAX_OXYGEN
            for (player in it.world.players) sendOxygenPacket(player, it.speedrunner, it.speedrunner.oxygen)
        },
        {
            it.speedrunner.giveItemStack(ItemStack(MoonRegistry.LASER_PISTOL))

            it.speedrunner.oxygen = MAX_OXYGEN
            for (player in it.world.players) sendOxygenPacket(player, it.speedrunner, it.speedrunner.oxygen)
        },
        {
            it.speedrunner.giveItemStack(ItemStack(MoonRegistry.LASER_SHOTGUN))

            it.speedrunner.oxygen = MAX_OXYGEN
            for (player in it.world.players) sendOxygenPacket(player, it.speedrunner, it.speedrunner.oxygen)
        },
        {
            it.speedrunner.giveItemStack(ItemStack(MoonRegistry.LASER_RIFLE))

            it.speedrunner.oxygen = MAX_OXYGEN
            for (player in it.world.players) sendOxygenPacket(player, it.speedrunner, it.speedrunner.oxygen)
        },
        {
            it.speedrunner.giveItemStack(ItemStack(MoonRegistry.LASER_SNIPER))

            it.speedrunner.oxygen = MAX_OXYGEN
            for (player in it.world.players) sendOxygenPacket(player, it.speedrunner, it.speedrunner.oxygen)
        },
        {
            it.speedrunner.giveItemStack(ItemStack(MoonRegistry.LASER_BAZOOKA))
            it.speedrunner.giveItemStack(ItemStack(MoonRegistry.ROCKET_ITEM))

            it.speedrunner.oxygen = MAX_OXYGEN
            for (player in it.world.players) sendOxygenPacket(player, it.speedrunner, it.speedrunner.oxygen)
        },
    )
}
