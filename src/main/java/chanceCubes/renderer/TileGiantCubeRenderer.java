package chanceCubes.renderer;

import chanceCubes.model.ModelGiantCube;
import chanceCubes.tileentities.TileGiantCube;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;

public class TileGiantCubeRenderer implements BlockEntityRenderer<TileGiantCube>
{
	protected final ModelGiantCube model;

	/**
	 * Make a new instance.
	 */
	public TileGiantCubeRenderer()
	{
		model = new ModelGiantCube();
	}

	@Override
	public void render(TileGiantCube tile, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int light, int overlayLight)
	{
		if(!tile.isMaster())
			return;

		poseStack.pushPose();
		poseStack.translate(0.5f, 0.5f, 0.5F);
		poseStack.scale(3, 3, 3);
		//TODO: Breaks with block on top
		int lightToUse = light;
		if(tile.getLevel() != null)
			lightToUse = tile.getLevel().getLightEmission(tile.getBlockPos().offset(0, 2, 0)) * 100;

		model.render(poseStack, bufferIn, lightToUse, overlayLight);
		poseStack.popPose();

//		if(destroyStage >= 0)
//		{
//			RenderSystem.bindTexture(DESTROY_STAGES[destroyStage]);
//			GlStateManager.matrixMode(5890);
//			GlStateManager.pushMatrix();
//			GlStateManager.scalef(4.0F, 4.0F, 1.0F);
//			GlStateManager.translatef(0.0625F, 0.0625F, 0.0625F);
//			GlStateManager.matrixMode(5888);
//		}

//		if(destroyStage >= 0)
//		{
//			GlStateManager.matrixMode(5890);
//			GlStateManager.popMatrix();
//			GlStateManager.matrixMode(5888);
//		}
	}

	//TODO: Make more efficient so not EVERY block is rendered?
	public boolean shouldRenderOffScreen(TileGiantCube tile)
	{
		return true;
	}
}