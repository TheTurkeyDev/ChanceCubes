package chanceCubes.model;

import chanceCubes.CCubesCore;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class ModelGiantCube extends Model
{
	public ModelRenderer block;

	private static final ResourceLocation GIANT_CUBE = new ResourceLocation(CCubesCore.MODID, "textures/models/giant_chance_cube.png");
	private final RenderType RENDER_TYPE = renderType(GIANT_CUBE);

	public ModelGiantCube()
	{
		super(RenderType::getEntityCutoutNoCull);
		block = new ModelRenderer(this, 0, 0);
		block.addBox(-8F, -8F, -8F, 16, 16, 16);
		block.setRotationPoint(0F, 0F, 0F);
		block.setTextureSize(256, 128);
		block.mirror = true;
	}

	public void render(PoseStack poseStack, IRenderTypeBuffer renderer, int light, int overlayLight)
	{
		render(poseStack, renderer.getBuffer(RENDER_TYPE), light, overlayLight, 1, 1, 1, 1);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexBuilder, int light, int overlayLight, float red, float green, float blue, float alpha)
	{
		this.block.render(poseStack, vertexBuilder, light, overlayLight, red, green, blue, alpha);
	}
}