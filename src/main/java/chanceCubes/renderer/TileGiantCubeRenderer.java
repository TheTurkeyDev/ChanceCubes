package chanceCubes.renderer;

import chanceCubes.CCubesCore;
import chanceCubes.model.ModelGiantCube;
import chanceCubes.tileentities.TileGiantCube;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;

public class TileGiantCubeRenderer extends TileEntityRenderer<TileGiantCube>
{
	protected final ModelGiantCube model;
	private static final ResourceLocation GIANT_CUBE_ATLAS = new ResourceLocation("textures/atlas/giant_chance_cube.png");
	private static final Material GIANT_CUBE_TEXTURE = new Material(GIANT_CUBE_ATLAS, new ResourceLocation(CCubesCore.MODID,"textures/models/giant_chance_cube"));

	/**
	 * Make a new instance.
	 */
	public TileGiantCubeRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
		model = new ModelGiantCube();
	}

	@Override
	public void render(TileGiantCube tile, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
	{
		if(!tile.isMaster())
			return;
//		if(destroyStage >= 0)
//		{
//			RenderSystem.bindTexture(DESTROY_STAGES[destroyStage]);
//			GlStateManager.matrixMode(5890);
//			GlStateManager.pushMatrix();
//			GlStateManager.scalef(4.0F, 4.0F, 1.0F);
//			GlStateManager.translatef(0.0625F, 0.0625F, 0.0625F);
//			GlStateManager.matrixMode(5888);
//		}

		matrixStackIn.push();
		RenderSystem.enableRescaleNormal();
		matrixStackIn.translate(2.5f, 1.5f, 2.5F);
		IVertexBuilder ivertexbuilder = GIANT_CUBE_TEXTURE.getBuffer(bufferIn, RenderType::getEntityCutoutNoCull);
		model.render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1, 1, 1, 1);
		matrixStackIn.translate(-1, -1, -1);
		matrixStackIn.scale(3f, 3f, 3f);
		RenderSystem.disableRescaleNormal();
		matrixStackIn.pop();

//		if(destroyStage >= 0)
//		{
//			GlStateManager.matrixMode(5890);
//			GlStateManager.popMatrix();
//			GlStateManager.matrixMode(5888);
//		}
	}
}