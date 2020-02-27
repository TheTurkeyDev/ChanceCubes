package chanceCubes.renderer;

import chanceCubes.CCubesCore;
import chanceCubes.model.ModelGiantCube;
import chanceCubes.tileentities.TileGiantCube;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.ResourceLocation;

public class TileGiantCubeRenderer extends TileEntityRenderer<TileGiantCube>
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
	public void render(TileGiantCube tile, double x, double y, double z, float partialTicks, int destroyStage)
	{
		if(!tile.isMaster())
			return;
		if(destroyStage >= 0)
		{
			this.bindTexture(DESTROY_STAGES[destroyStage]);
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			GlStateManager.scalef(4.0F, 4.0F, 1.0F);
			GlStateManager.translatef(0.0625F, 0.0625F, 0.0625F);
			GlStateManager.matrixMode(5888);
		}

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.translatef((float) x + 2.5f, (float) y + 1.5f, (float) z + 2.5F);
		bindTexture(this.blockTexture);
		GlStateManager.translated(-1, -1, -1);
		GlStateManager.scalef(3f, 3f, 3f);
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