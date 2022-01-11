package dev.isxander.moonmc.registry

import dev.isxander.moonmc.disasters.asteroid.AsteroidEntity
import dev.isxander.moonmc.food.AsteroidShardItem
import dev.isxander.moonmc.material.AlienArmourMaterial
import dev.isxander.moonmc.monsters.alien.AlienEntity
import dev.isxander.moonmc.monsters.moonman.MoonManEntity
import dev.isxander.moonmc.oxygen.OxygenMask
import dev.isxander.moonmc.transport.rocket.RocketEntity
import dev.isxander.moonmc.transport.rocket.RocketItem
import dev.isxander.moonmc.weapons.melee.LaserSwordItem
import dev.isxander.moonmc.weapons.ranged.bazooka.BazookaRocketEntity
import dev.isxander.moonmc.weapons.ranged.bazooka.LaserBazookaItem
import dev.isxander.moonmc.weapons.ranged.shotgun.LaserShotgunItem
import dev.isxander.moonmc.weapons.ranged.moonrock.MoonRockEntity
import dev.isxander.moonmc.weapons.ranged.moonrock.MoonRockItem
import dev.isxander.moonmc.weapons.ranged.pistol.LaserPistolItem
import dev.isxander.moonmc.weapons.ranged.pistol.PistolBulletEntity
import dev.isxander.moonmc.weapons.ranged.rifle.LaserRifleItem
import dev.isxander.moonmc.weapons.ranged.rifle.RifleBulletEntity
import dev.isxander.moonmc.weapons.ranged.shotgun.ShotgunShellEntity
import dev.isxander.moonmc.weapons.ranged.sniper.LaserSniperItem
import dev.isxander.moonmc.weapons.ranged.sniper.SniperBulletEntity
import io.ejekta.kambrik.registration.KambrikAutoRegistrar
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.fabricmc.fabric.mixin.`object`.builder.SpawnRestrictionAccessor
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.SpawnRestriction
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterials
import net.minecraft.item.ItemStack
import net.minecraft.item.SpawnEggItem
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.Heightmap
import net.minecraft.world.biome.Biome

object MoonRegistry : KambrikAutoRegistrar {
    val MOON_ITEM_GROUP = FabricItemGroupBuilder.create(Identifier("moonmc", "moon_item_group"))
        .icon { ItemStack(OXYGEN_MASK) }
        .appendItems { _ -> ItemStack(OXYGEN_MASK) }
        .build()

    val OXYGEN_MASK = "oxygen_mask" forItem OxygenMask()

    val LASER_SWORD = "laser_sword" forItem LaserSwordItem()
    val LASER_SWORD_AMBIENT_SOUND = "item.laser_sword.ambient" forSoundEvent SoundEvent(Identifier("moonmc", "item.laser_sword.ambient"))
    val LASER_SWORD_WAVE_SOUND = "item.laser_sword.wave" forSoundEvent SoundEvent(Identifier("moonmc", "item.laser_sword.wave"))

    val LASER_SHOTGUN = "laser_shotgun" forItem LaserShotgunItem()
    val SHOTGUN_SHELL = "shotgun_shell" forEntityType FabricEntityTypeBuilder.create(SpawnGroup.MISC, ::ShotgunShellEntity).apply {
        disableSaving()
        dimensions(EntityDimensions.fixed(0.1f, 0.1f))
        trackedUpdateRate(20)
        trackRangeBlocks(10)
    }.build()
    val SHOTGUN_SHOOT_SOUND = "entity.shotgun_shell.shoot" forSoundEvent SoundEvent(Identifier("moonmc", "shotgun_shell"))

    val LASER_PISTOL = "laser_pistol" forItem LaserPistolItem()
    val PISTOL_BULLET = "pistol_bullet" forEntityType FabricEntityTypeBuilder.create(SpawnGroup.MISC, ::PistolBulletEntity).apply {
        disableSaving()
        dimensions(EntityDimensions.fixed(0.1f, 0.1f))
        trackedUpdateRate(20)
        trackRangeBlocks(20)
    }.build()

    val LASER_RIFLE = "laser_rifle" forItem LaserRifleItem()
    val RIFLE_BULLET = "rifle_bullet" forEntityType FabricEntityTypeBuilder.create(SpawnGroup.MISC, ::RifleBulletEntity).apply {
        disableSaving()
        dimensions(EntityDimensions.fixed(0.1f, 0.1f))
        trackedUpdateRate(20)
        trackRangeBlocks(20)
    }.build()

    val LASER_SNIPER = "laser_sniper" forItem LaserSniperItem()
    val SNIPER_BULLET = "sniper_bullet" forEntityType FabricEntityTypeBuilder.create(SpawnGroup.MISC, ::SniperBulletEntity).apply {
        disableSaving()
        dimensions(EntityDimensions.fixed(0.1f, 0.1f))
        trackedUpdateRate(20)
        trackRangeBlocks(100)
    }.build()

    val LASER_BAZOOKA = "laser_bazooka" forItem LaserBazookaItem()
    val BAZOOKA_ROCKET = "bazooka_rocket" forEntityType FabricEntityTypeBuilder.create(SpawnGroup.MISC, ::BazookaRocketEntity).apply {
        disableSaving()
        dimensions(EntityDimensions.fixed(0.5f, 0.5f))
        trackedUpdateRate(20)
        trackRangeBlocks(20)
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

    val ASTEROID_ENTITY = "asteroid" forEntityType FabricEntityTypeBuilder.create<AsteroidEntity>().apply {
        entityFactory(::AsteroidEntity)
        spawnGroup(SpawnGroup.MISC)
        trackRangeChunks(20)
        trackedUpdateRate(10)
        dimensions(EntityDimensions.fixed(10f, 10f))
    }.build()

    val BLUE_ASTEROID_SHARD_ITEM = "blue_asteroid_shard" forItem AsteroidShardItem()
    val RED_ASTEROID_SHARD_ITEM = "red_asteroid_shard" forItem AsteroidShardItem()
    val YELLOW_ASTEROID_SHARD_ITEM = "yellow_asteroid_shard" forItem AsteroidShardItem()

    val ASTEROID_AMBIENT_SOUND = "entity.asteroid.ambient" forSoundEvent SoundEvent(Identifier("moonmc", "entity.asteroid.ambient"))
    val LASER_GUN_SHOOT_SOUND = "item.laser_gun.shoot" forSoundEvent SoundEvent(Identifier("moonmc", "item.laser_gun.shoot"))
    val BULLET_HIT_SOUND = "entity.bullet.hit" forSoundEvent SoundEvent(Identifier("moonmc", "entity.bullet.hit"))

    val LASER_PARTICLE = FabricParticleTypes.simple()

    val ROCKET_ENTITY = "rocket" forEntityType FabricEntityTypeBuilder.create<RocketEntity>().apply {
        entityFactory(::RocketEntity)
        spawnGroup(SpawnGroup.MISC)
        trackRangeChunks(8)
        dimensions(EntityDimensions.fixed(3f, 7.5f))
    }.build()
    val ROCKET_ITEM = "rocket" forItem RocketItem()

    val ALIEN_ENTITY = "alien" forEntityType FabricEntityTypeBuilder.createLiving<AlienEntity>().apply {
        entityFactory(::AlienEntity)
        spawnGroup(SpawnGroup.MONSTER)
        trackRangeChunks(8)
        defaultAttributes(AlienEntity::createAlienAttributes)
        dimensions(EntityDimensions.fixed(1.2f, 3f))
    }.build()
    val ALIEN_SPAWN_EGG_ITEM = "alien_spawn_egg" forItem SpawnEggItem(ALIEN_ENTITY, 0x303030, 0x414141, FabricItemSettings().group(MOON_ITEM_GROUP))

    val ALIEN_ARMOUR_HELMET_ITEM = "alien_helmet" forItem ArmorItem(AlienArmourMaterial, EquipmentSlot.HEAD, FabricItemSettings().group(MOON_ITEM_GROUP))
    val ALIEN_ARMOUR_CHESTPLATE_ITEM = "alien_chestplate" forItem ArmorItem(AlienArmourMaterial, EquipmentSlot.CHEST, FabricItemSettings().group(MOON_ITEM_GROUP))
    val ALIEN_ARMOUR_LEGGINGS_ITEM = "alien_leggings" forItem ArmorItem(AlienArmourMaterial, EquipmentSlot.LEGS, FabricItemSettings().group(MOON_ITEM_GROUP))
    val ALIEN_ARMOUR_BOOTS_ITEM = "alien_boots" forItem ArmorItem(AlienArmourMaterial, EquipmentSlot.FEET, FabricItemSettings().group(MOON_ITEM_GROUP))

    override fun afterRegistration() {
        Registry.register(Registry.PARTICLE_TYPE, Identifier("moonmc", "laser_particle"), LASER_PARTICLE)

        BiomeModifications.addSpawn({ it.biome.category != Biome.Category.NETHER && it.biome.category != Biome.Category.THEEND && it.biome.category != Biome.Category.NONE }, SpawnGroup.MONSTER, MOON_MAN_ENTITY, 100, 1, 2)
        SpawnRestrictionAccessor.callRegister(MOON_MAN_ENTITY, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnIgnoreLightLevel)
    }
}
