package dev.isxander.moonmc.client.render;

import com.google.common.collect.Lists;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import software.bernie.geckolib3.compat.PatchouliCompat;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.util.AnimationUtils;

import java.awt.*;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class GeoNonLivingEntityRenderer<T extends Entity & IAnimatable> extends EntityRenderer<T>
        implements IGeoRenderer<T> {
    static {
        AnimationController.addModelFetcher((IAnimatable object) -> {
            if (object instanceof Entity) {
                return (IAnimatableModel<Object>) AnimationUtils.getGeoModelForEntity((Entity) object);
            }
            return null;
        });
    }

    protected final List<GeoLayerRenderer<T>> layerRenderers = Lists.newArrayList();
    private final AnimatedGeoModel<T> modelProvider;

    public ItemStack mainHand;
    public ItemStack offHand;
    public ItemStack helmet;
    public ItemStack chestplate;
    public ItemStack leggings;
    public ItemStack boots;
    public VertexConsumerProvider rtb;
    public Identifier whTexture;

    protected GeoNonLivingEntityRenderer(EntityRendererFactory.Context ctx, AnimatedGeoModel<T> modelProvider) {
        super(ctx);
        this.modelProvider = modelProvider;
    }

    public static int getPackedOverlay(float uIn) {
        return OverlayTexture.getUv(OverlayTexture.getU(uIn), false);
    }

    private static float getFacingAngle(Direction facingIn) {
        switch (facingIn) {
            case SOUTH:
                return 90.0F;
            case NORTH:
                return 270.0F;
            case EAST:
                return 180.0F;
            case WEST:
            default:
                return 0.0F;
        }
    }

    @SuppressWarnings("resource")
    @Override
    public void render(T entity, float entityYaw, float partialTicks, MatrixStack stack,
                       VertexConsumerProvider bufferIn, int packedLightIn) {
        stack.push();
        boolean shouldSit = entity.hasVehicle() && (entity.getVehicle() != null);
        EntityModelData entityModelData = new EntityModelData();
        entityModelData.isSitting = shouldSit;

        float f = MathHelper.lerpAngleDegrees(partialTicks, entity.prevYaw, entity.getYaw());
        float f1 = MathHelper.lerpAngleDegrees(partialTicks, entity.getHeadYaw(), entity.getHeadYaw());
        float netHeadYaw = f1 - f;
        if (shouldSit && entity.getVehicle() instanceof LivingEntity livingentity) {
            f = MathHelper.lerpAngleDegrees(partialTicks, livingentity.prevBodyYaw, livingentity.bodyYaw);
            netHeadYaw = f1 - f;
            float f3 = MathHelper.wrapDegrees(netHeadYaw);
            if (f3 < -85.0F) {
                f3 = -85.0F;
            }

            if (f3 >= 85.0F) {
                f3 = 85.0F;
            }

            f = f1 - f3;
            if (f3 * f3 > 2500.0F) {
                f += f3 * 0.2F;
            }

            netHeadYaw = f1 - f;
        }

        float headPitch = MathHelper.lerp(partialTicks, entity.prevPitch, entity.getPitch());
        float f7 = this.handleRotationFloat(entity, partialTicks);
        this.applyRotations(entity, stack, f7, f, partialTicks);

        float lastLimbDistance = 0.0F;
        float limbSwing = 0.0F;
        entityModelData.headPitch = -headPitch;
        entityModelData.netHeadYaw = -netHeadYaw;

        AnimationEvent<T> predicate = new AnimationEvent<T>(entity, 0f, 0f, partialTicks,
                false, Collections.singletonList(entityModelData));
        GeoModel model = modelProvider.getModel(modelProvider.getModelLocation(entity));
        ((IAnimatableModel<T>) modelProvider).setLivingAnimations(entity, this.getUniqueID(entity), predicate);

        stack.translate(0, 0.01f, 0);
        MinecraftClient.getInstance().getTextureManager().bindTexture(getTexture(entity));
        Color renderColor = getRenderColor(entity, partialTicks, stack, bufferIn, null, packedLightIn);
        RenderLayer renderType = getRenderType(entity, partialTicks, stack, bufferIn, null, packedLightIn,
                getTexture(entity));
        boolean invis = entity.isInvisibleTo(MinecraftClient.getInstance().player);
        render(model, entity, partialTicks, renderType, stack, bufferIn, null, packedLightIn,
                getPackedOverlay(0), (float) renderColor.getRed() / 255f, (float) renderColor.getGreen() / 255f,
                (float) renderColor.getBlue() / 255f, invis ? 0.0F : (float) renderColor.getAlpha() / 255);

        if (!entity.isSpectator()) {
            for (GeoLayerRenderer<T> layerRenderer : this.layerRenderers) {
                layerRenderer.render(stack, bufferIn, packedLightIn, entity, limbSwing, lastLimbDistance, partialTicks,
                        f7, netHeadYaw, headPitch);
            }
        }
        if (FabricLoader.getInstance().isModLoaded("patchouli")) {
            PatchouliCompat.patchouliLoaded(stack);
        }
        stack.pop();
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    @Override
    public Integer getUniqueID(T animatable) {
        return animatable.getUuid().hashCode();
    }

    @Override
    public Identifier getTexture(T entity) {
        return getTextureLocation(entity);
    }

    @Override
    public GeoModelProvider<T> getGeoModelProvider() {
        return this.modelProvider;
    }

    protected void applyRotations(T entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        EntityPose pose = entityLiving.getPose();
        if (pose != EntityPose.SLEEPING) {
            matrixStackIn.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F - rotationYaw));
        }

        if (entityLiving.hasCustomName() || entityLiving instanceof PlayerEntity) {
            String s = Formatting.strip(entityLiving.getName().getString());
            if (("Dinnerbone".equals(s) || "Grumm".equals(s)) && (!(entityLiving instanceof PlayerEntity)
                    || ((PlayerEntity) entityLiving).isPartVisible(PlayerModelPart.CAPE))) {
                matrixStackIn.translate(0.0D, entityLiving.getHeight() + 0.1F, 0.0D);
                matrixStackIn.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0F));
            }
        }
    }

    @Override
    protected boolean hasLabel(T entity) {
        double d0 = this.dispatcher.getSquaredDistanceToCamera(entity);
        float f = entity.isSneaking() ? 32.0F : 64.0F;
        if (d0 >= (double) (f * f)) {
            return false;
        } else {
            return entity == this.dispatcher.targetedEntity && entity.hasCustomName();
        }
    }

    protected boolean isVisible(T livingEntityIn) {
        return !livingEntityIn.isInvisible();
    }

    protected float getDeathMaxRotation(T entityLivingBaseIn) {
        return 90.0F;
    }

    /**
     * Returns where in the swing animation the living entity is (from 0 to 1). Args
     * : entity, partialTickTime
     */
    protected float getHandSwingProgress(T livingBase, float partialTickTime) {
        return 0;
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float handleRotationFloat(T livingBase, float partialTicks) {
        return (float) livingBase.age + partialTicks;
    }

    @Override
    public Identifier getTextureLocation(T instance) {
        return this.modelProvider.getTextureLocation(instance);
    }

    public final boolean addLayer(GeoLayerRenderer<T> layer) {
        return this.layerRenderers.add(layer);
    }
}
