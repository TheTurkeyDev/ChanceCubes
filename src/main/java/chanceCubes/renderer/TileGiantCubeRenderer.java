package chanceCubes.renderer;

import chanceCubes.model.ModelGiantCube;
import chanceCubes.tileentities.TileGiantCube;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public class TileGiantCubeRenderer extends TileEntityRenderer<TileGiantCube>
{
	protected final ModelGiantCube model;

	/**
	 * Make a new instance.
	 */
	public TileGiantCubeRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
		model = new ModelGiantCube();
	}

	@Override
	public void render(TileGiantCube tile, float partialTicks, MatrixStack matrix, IRenderTypeBuffer bufferIn, int light, int overlayLight)
	{
		if(!tile.isMaster())
			return;

		matrix.push();
		matrix.translate(0.5f, 0.5f, 0.5F);
		matrix.scale(3, 3, 3);
		//TODO: Breaks with block on top
		int lightToUse = light;
		if(tile.getWorld() != null)
			lightToUse = tile.getWorld().getLight(tile.getPos().add(0, 2, 0)) * 100;

		model.render(matrix, bufferIn, lightToUse, overlayLight);
		matrix.pop();

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

	@Override
	public boolean isGlobalRenderer(TileGiantCube tile)
	{
		return true;
	}
}