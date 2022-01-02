package dev.isxander.moonmc.registry

import dev.isxander.moonmc.monsters.MoonManEntity
import dev.isxander.moonmc.oxygen.OxygenMask
import dev.isxander.moonmc.weapons.melee.LaserSwordItem
import dev.isxander.moonmc.weapons.ranged.LaserShotgunItem
import dev.isxander.moonmc.weapons.ranged.MoonRockEntity
import dev.isxander.moonmc.weapons.ranged.MoonRockItem
import dev.isxander.moonmc.weapons.ranged.ShotgunShellEntity
import io.ejekta.kambrik.registration.KambrikAutoRegistrar
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.SpawnGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.SpawnEggItem
import net.minecraft.util.Identifier

object MoonRegistry : KambrikAutoRegistrar {
    val MOON_ITEM_GROUP = FabricItemGroupBuilder.create(Identifier("moonmc", "moon_item_group"))
        .icon { ItemStack(OXYGEN_MASK) }
        .appendItems { _ -> ItemStack(OXYGEN_MASK) }
        .build()

    val OXYGEN_MASK = "oxygen_mask" forItem OxygenMask()
    val LASER_SWORD = "laser_sword" forItem LaserSwordItem()
    val LASER_SHOTGUN = "laser_shotgun" forItem LaserShotgunItem()
    val SHOTGUN_SHELL = "shotgun_shell" forEntityType FabricEntityTypeBuilder.create(SpawnGroup.MISC, ::ShotgunShellEntity).apply {
        disableSaving()
        dimensions(EntityDimensions.fixed(0.5f, 0.5f))
        trackedUpdateRate(20)
        trackRangeBlocks(10)
    }.build()
    val MOON_ROCK_ITEM = ("moon_rock" forItem MoonRockItem()) as MoonRockItem
    val MOON_ROCK_ENTITY = "moon_rock" forEntityType FabricEntityTypeBuilder.create(SpawnGroup.MISC, ::MoonRockEntity).apply {
        trackRangeChunks(4)
        trackedUpdateRate(10)
        dimensions(EntityDimensions.fixed(1.25f, 1.25f))
    }.build()

    val MOON_MAN_ENTITY = "moon_man" forEntityType FabricEntityTypeBuilder.createLiving<MoonManEntity>().apply {
        spawnGroup(SpawnGroup.MONSTER)
        entityFactory(::MoonManEntity)
        defaultAttributes(MoonManEntity::createLivingAttributes)
        trackRangeChunks(8)
        dimensions(EntityDimensions.fixed(0.6f, 1.99f))
    }.build()

    val MOON_MAN_SPAWN_EGG = "moon_man_spawn_egg" forItem SpawnEggItem(MOON_MAN_ENTITY, 0, 0xff0000, FabricItemSettings().group(MOON_ITEM_GROUP))
}
