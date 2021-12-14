package dev.isxander.moonmc.mixins.nofoliage;

import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.MiscPlacedFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(DefaultBiomeFeatures.class)
public class MixinDefaultBiomeFeatures {
    @Overwrite
    public static void addMossyRocks(GenerationSettings.Builder builder) {}

    @Overwrite
    public static void addLargeFerns(GenerationSettings.Builder builder) {}

    @Overwrite
    public static void addSweetBerryBushesSnowy(GenerationSettings.Builder builder) {}

    @Overwrite
    public static void addSweetBerryBushes(GenerationSettings.Builder builder) {}

    @Overwrite
    public static void addBamboo(GenerationSettings.Builder builder) {}

    @Overwrite
    public static void addJungleGrass(GenerationSettings.Builder builder) {}

    @Overwrite
    public static void addSavannaTallGrass(GenerationSettings.Builder builder) {}

    @Overwrite
    public static void addWindsweptSavannaGrass(GenerationSettings.Builder builder) {}

    @Overwrite
    public static void addSavannaGrass(GenerationSettings.Builder builder) {}

    @Overwrite
    public static void addBadlandsGrass(GenerationSettings.Builder builder) {}

    @Overwrite
    public static void addForestFlowers(GenerationSettings.Builder builder) {}

    @Overwrite
    public static void addForestGrass(GenerationSettings.Builder builder) {}

    @Overwrite
    public static void addSwampFeatures(GenerationSettings.Builder builder) {
        builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.TREES_SWAMP);
    }

    @Overwrite
    public static void addMushroomFieldsFeatures(GenerationSettings.Builder builder) {}

    @Overwrite
    public static void addPlainsFeatures(GenerationSettings.Builder builder) {
        builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.TREES_PLAINS);
    }

    @Overwrite
    public static void addDesertDeadBushes(GenerationSettings.Builder builder) {}

    @Overwrite
    public static void addGiantTaigaGrass(GenerationSettings.Builder builder) {}

    @Overwrite
    public static void addDefaultFlowers(GenerationSettings.Builder builder) {}

    @Overwrite
    public static void addMeadowFlowers(GenerationSettings.Builder builder) {}

    @Overwrite
    public static void addExtraDefaultFlowers(GenerationSettings.Builder builder) {}

    @Overwrite
    public static void addDefaultGrass(GenerationSettings.Builder builder) {}

    @Overwrite
    public static void addTaigaGrass(GenerationSettings.Builder builder) {}

    @Overwrite
    public static void addPlainsTallGrass(GenerationSettings.Builder builder) {}

    @Overwrite
    public static void addDefaultMushrooms(GenerationSettings.Builder builder) {}

    @Overwrite
    public static void addDefaultVegetation(GenerationSettings.Builder builder) {}

    @Overwrite
    public static void addBadlandsVegetation(GenerationSettings.Builder builder) {}

    @Overwrite
    public static void addMelons(GenerationSettings.Builder builder) {}

    @Overwrite
    public static void addSparseMelons(GenerationSettings.Builder builder) {}

    @Overwrite
    public static void addVines(GenerationSettings.Builder builder) {}

    @Overwrite
    public static void addDesertVegetation(GenerationSettings.Builder builder) {}

    @Overwrite
    public static void addSwampVegetation(GenerationSettings.Builder builder) {}
}
