package dev.isxander.moonmc.mixins.oxygen;

import dev.isxander.moonmc.registry.MoonRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public class MixinMobEntity {
    @Shadow @Final
    private DefaultedList<ItemStack> armorItems;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void equipOxygenMask(EntityType<? extends MobEntity> entityType, World world, CallbackInfo ci) {
        armorItems.set(3, new ItemStack(MoonRegistry.INSTANCE.getOXYGEN_MASK()));
    }
}
