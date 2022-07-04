package com.technopig.technopig.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.technopig.technopig.TechnoPigMod;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Mod.EventBusSubscriber(modid = TechnoPigMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
final class ModPigRenderer extends PigRenderer {
    private ModPigRenderer(@NotNull EntityRendererProvider.Context context) {
        super(context);
        this.addLayer(new CrownLayer(this));
    }

    @SubscribeEvent
    static void registerRenderer(@NotNull EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityType.PIG, ModPigRenderer::new);
    }

    private static final class CrownLayer extends RenderLayer<Pig, PigModel<Pig>> {
        private final static ResourceLocation CROWN_TEXTURE = new ResourceLocation(TechnoPigMod.MOD_ID, "textures/entity/pig/crown.png");

        private final PigModel<Pig> model = new PigModel<>(getModelRoot());

        private CrownLayer(@NotNull RenderLayerParent<Pig, PigModel<Pig>> parent) {
            super(parent);
        }

        private static @NotNull ModelPart getModelRoot() {
            PartDefinition root = new MeshDefinition().getRoot();

            root.addOrReplaceChild("head", CubeListBuilder.create()
                            .texOffs(0, 0)
                            .addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F))
                    , PartPose.offset(0.0F, 12.0F, -6.0F)
            );

            List.of("body", "right_hind_leg", "left_hind_leg", "right_front_leg", "left_front_leg")
                    .forEach(name -> root.addOrReplaceChild(name, CubeListBuilder.create(), PartPose.ZERO));

            return root.bake(32, 16);
        }

        @Override
        public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i0, @NotNull Pig entity, float f0, float f1, float f2, float f3, float f4, float f5) {
            if (!TechnoPigMod.isTechnoPig(entity)) return;

            this.getParentModel().copyPropertiesTo(model);

            model.setupAnim(entity, f0, f1, f2, f4, f5);

            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(model.renderType(CROWN_TEXTURE));

            model.renderToBuffer(poseStack, vertexConsumer, i0, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
