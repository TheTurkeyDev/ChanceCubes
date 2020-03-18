package chanceCubes.model;

import chanceCubes.CCubesCore;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public class ModelGiantCube extends Model
{
	public ModelRenderer block;

	private static final ResourceLocation GIANT_CUBE = new ResourceLocation(CCubesCore.MODID, "textures/models/giant_chance_cube.png");
	private final RenderType RENDER_TYPE = getRenderType(GIANT_CUBE);

	public ModelGiantCube()
	{
		super(RenderType::getEntityCutoutNoCull);
		block = new ModelRenderer(this, 0, 0);
		block.addBox(-8F, -8F, -8F, 16, 16, 16);
		block.setRotationPoint(0F, 0F, 0F);
		block.setTextureSize(256, 128);
		block.mirror = true;
	}

	public void render(MatrixStack matrix, IRenderTypeBuffer renderer, int light, int overlayLight)
	{
		render(matrix, renderer.getBuffer(RENDER_TYPE), light, overlayLight, 1, 1, 1, 1);
	}

	@Override
	public void render(MatrixStack matrix, IVertexBuilder vertexBuilder, int light, int overlayLight, float red, float green, float blue, float alpha)
	{
		this.block.render(matrix, vertexBuilder, light, overlayLight, red, green, blue, alpha);
	}
}