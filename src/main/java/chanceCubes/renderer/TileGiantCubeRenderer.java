package chanceCubes.renderer;

import chanceCubes.CCubesCore;
import chanceCubes.model.ModelGiantCube;
import chanceCubes.tileentities.TileGiantCube;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class TileGiantCubeRenderer extends TileEntitySpecialRenderer<TileGiantCube>
{
	protected final ModelGiantCube model;
	private ResourceLocation blockTexture = new ResourceLocation(CCubesCore.MODID, "textures/models/giant_chance_cube.png");

	/**
	 * Make a new instance.
	 */
	public TileGiantCubeRenderer()
	{
		model = new ModelGiantCube();
	}

	@Override
	public void render(TileGiantCube tile, double x, double y, double z, float partialTicks, int destroyStage, float p_192841_10_)
	{
		if(!tile.isMaster())
			return;
		if(destroyStage >= 0)
		{
			this.bindTexture(DESTROY_STAGES[destroyStage]);
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4.0F, 4.0F, 1.0F);
			GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
			GlStateManager.matrixMode(5888);
		}

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.translate((float) x + 2.5f, (float) y + 1.5f, (float) z + 2.5F);
		bindTexture(this.blockTexture);
		GlStateManager.translate(-1, -1, -1);
		GlStateManager.scale(3f, 3f, 3f);
		GlStateManager.pushMatrix();

		model.renderAll();
		GlStateManager.popMatrix();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();

		if(destroyStage >= 0)
		{
			GlStateManager.matrixMode(5890);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
		}
	}
}